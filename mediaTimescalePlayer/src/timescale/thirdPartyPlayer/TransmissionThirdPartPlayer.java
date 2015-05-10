/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (01/08/2005)
 */

package timescale.thirdPartyPlayer;

import timescale.event.PresentationFinishListener;
import timescale.event.PresentationFinishedEvent;
import timescale.media.protocol.datasource.MediaDataSource;
import timescale.net.RTPServer;
import util.io.InputTools;

public abstract class TransmissionThirdPartPlayer 
    implements IThirdPartyPlayer, PresentationFinishListener {

	protected boolean mustEndTransmission;
	protected RTPServer rtpServer;
	private InputTools inputTools;
	protected String rtpMachine;
	private int rtpPortBase;
	protected int waitingTime;

	public TransmissionThirdPartPlayer (String rtpMachine, int rtpPortBase, int waitingTime) {		
		this.rtpMachine = rtpMachine;
		this.rtpPortBase = rtpPortBase;
		this.waitingTime = waitingTime;
	}

	public void setInput (Object o) {
		this.inputTools = (InputTools) o;
		this.mustEndTransmission = false;
	}
	
	public void prepare() throws Exception {
		MediaDataSource dSource = new MediaDataSource(inputTools);				
		dSource.addTimescaleListener(this);
		rtpServer = new RTPServer(dSource, rtpMachine, rtpPortBase,waitingTime);
		rtpServer.prepare();
	}	
	
	public void endTransmission() {
		this.mustEndTransmission = true;
	}
	
	/** 
	 * Realiza acoes depois da realizacao de um ajuste.
	 * @param event evento de finalizacao do ajuste
	 */			
	public void actionPosPresentation (PresentationFinishedEvent event) throws Exception {
		if (this.mustEndTransmission) {
			this.stop();		
		}
	}	
	
	public void play()throws Exception {
		Thread t = new Thread (this.rtpServer);
		t.start();
	}
	
	public synchronized void stop() {
		this.rtpServer.endTransmissionSoon();
	}
	
}
