/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (12/09/2005)
 */

package timescale.thirdPartyPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.*;
import javax.media.protocol.DataSource;
import timescale.event.PlayChangingEvent;
import timescale.event.PlayChangingListener;
import timescale.event.PresentationFinishedEvent;
import timescale.net.IRTPHandlerData;
import timescale.net.RTPClient;

public class JMFRTPTransmissionPlayer extends TransmissionThirdPartPlayer
                          implements IRTPHandlerData {

	private RTPClient rtpClient;
	private List listeners = new ArrayList();
	private int rtpTtl;
	
	
	public JMFRTPTransmissionPlayer (String rtpMachine, int rtpPortBase, int rtpTtl, int waitingTime) {
		super (rtpMachine, rtpPortBase, waitingTime);
		this.rtpTtl = rtpTtl;
	}
	
	public void prepare() throws Exception {
		super.prepare();

		this.rtpClient = new RTPClient(this, waitingTime);		
		int ports[] = this.rtpServer.getUsedPorts();
		RTPClient.SessionData sessions[] = new RTPClient.SessionData[ports.length];
		for (int i=0; i<ports.length; i++) {
			sessions[i] = rtpClient.new SessionData();
			sessions[i].address = this.rtpMachine;
			sessions[i].port = ports[i];
			sessions[i].ttl = this.rtpTtl;			
		}		
		this.rtpClient.prepare(sessions);
	}
	
	/** 
	 * Exibe midia.
	 */		
	public void play () throws Exception {
		
		Thread t = new Thread (this.rtpClient);
		t.start();		
		super.play();
	}
	
	public synchronized void stop() {
		super.stop();
	}

	/** 
	 * Realiza acoes depois da realizacao de um ajuste.
	 * @param event evento de finalizacao do ajuste
	 */			
	public void actionPosPresentation (PresentationFinishedEvent event) throws Exception {
		if (this.mustEndTransmission) {
			this.rtpServer.endTransmissionSoon();
			this.rtpClient.endTransmissionSoon();
		}
	}    
	
	
	/**
	 * Toca dados recebidos remotamente.
	 */
    public void receiveStream  (DataSource dataSource) throws Exception {
    	JMFDataSourcePlayer dsPlayer = new JMFDataSourcePlayer();
    	dsPlayer.setInput(dataSource);
    	dsPlayer.prepare();

		this.fireNewPlayerEvent(dsPlayer);
		
		dsPlayer.getPlayer().addControllerListener(new ControllerListener() {
			public void controllerUpdate (ControllerEvent ce) {
				System.out.println("controller update");
		    	Player player = (Player)ce.getSourceController();
		    	if (ce instanceof RealizeCompleteEvent) {
		    		fireRealizedPlayerEvent(new JMFDataSourcePlayer(player));
		    		player.start();
		    	}
			}
		});
		
		dsPlayer.getPlayer().realize();
	}
    
	public void addPlayChangingListener (PlayChangingListener l) {
        listeners.add(l);
    }
	
	private void fireNewPlayerEvent(JMFIntegratedThirdPartPlayer tpPlayer) {
		PlayChangingEvent event = new PlayChangingEvent(tpPlayer);
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext() ) {
            ((PlayChangingListener) iterator.next()).actionPosCreateNewPlayer(event);
        }
    }

	private void fireRealizedPlayerEvent(JMFIntegratedThirdPartPlayer tpPlayer) {
		PlayChangingEvent event = new PlayChangingEvent(tpPlayer);
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext() ) {
            ((PlayChangingListener) iterator.next()).actionPosRealizeNewPlayer(event);
        }
    }
	

}
