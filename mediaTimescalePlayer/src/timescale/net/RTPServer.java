/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (08/08/2004)
 */

package timescale.net;

import javax.media.control.TrackControl;
import javax.media.protocol.*;
import javax.media.rtp.*;
import java.net.InetAddress;
import javax.media.protocol.DataSource;
import javax.media.*;

public class RTPServer implements Runnable {

    private Processor proc;
    private DataSource dSource;
    
    private RTPManager rtpMgrs[];
    private DataSource dOutput;
    
    private String ipAddr;
    private int portBase;
    
    private int[] usedPorts;
    
    private boolean continueTransmission;  
    private int waitTime;

    public RTPServer(DataSource dSource, String ipAddr, int pb, int waitTime) {	
		this.ipAddr = ipAddr;
		this.dSource = dSource;
		this.portBase = pb;
		this.continueTransmission = true;
		this.waitTime = waitTime;
    }
    
    public String getIpAdd() {
    	return this.ipAddr;
    }
    
    public int[] getUsedPorts() {
    	return this.usedPorts;
    }

    public void run () {   	    	
		
		this.proc.start();

		while (this.continueTransmission) {
			try {
			    Thread.sleep(this.waitTime);
			} catch (InterruptedException ie) {
				System.out.println("Excecao: interrompeu espera do RTPServer");				
			}			
		}
		// Para a transmissão
		this.stopTransmission();
		System.out.println("Terminou transmissao do servidor");		
    }
    
    public void endTransmissionSoon() {
		System.out.println("Mandou transmissão do servidor RTP parar: EndTransmissionSoon");
    	this.continueTransmission=false;
    }

    public void prepare () throws Exception {
		try {
	    	createProc();
			createSessions();						    			
		}
		catch (Exception e) {
			this.proc.close();
			System.err.println(e.getMessage());
			throw e;
		}		
    }
    
    private void createProc() throws Exception {
    	
		this.proc = Manager.createProcessor(dSource);

		// Espera a configuração
		waitState(this.proc, Processor.Configured);
	
		// Faixas do processador
		TrackControl[] tControls = this.proc.getTrackControls();
	
		// Deve haver ao menos uma faixa
		if (tControls == null || tControls.length < 1)
		    throw new Exception ("Couldn't find tControls in proc");
	
		ContentDescriptor cDesc = new ContentDescriptor(ContentDescriptor.RAW_RTP);
		this.proc.setContentDescriptor(cDesc);
	
		Format chosen;
		Format supported[];
	
		boolean oneTrack = false;
	
		// Programar as faixas
		for (int i = 0; i < tControls.length; i++) {
		    if (tControls[i].isEnabled()) {
	
			supported = tControls[i].getSupportedFormats();
	
			if (supported.length > 0) {
				chosen = supported[0];
			    tControls[i].setFormat(chosen);
			    System.out.println("Track " + i + " transmitting:");
			    System.out.println("  " + chosen);
			    oneTrack = true;
			} else
			    tControls[i].setEnabled(false);
		    } else
			tControls[i].setEnabled(false);
		}
	
		if (!oneTrack)
		    throw new Exception ("Couldn't set any of the tracks to a valid format");
	
		waitState(this.proc, Controller.Realized);

		this.dOutput = this.proc.getDataOutput();
    }
    
    // Cria sessões para cada faixa do processador
    private void createSessions() throws Exception {

		SessionAddress local, dest;
		int port;
		InetAddress ip;
		SendStream sendStream;
		PushBufferDataSource pbdsource = (PushBufferDataSource) this.dOutput;
		PushBufferStream pbs[] = pbdsource.getStreams();
	
		this.rtpMgrs = new RTPManager[pbs.length];
		this.usedPorts = new int [pbs.length];
	
		for (int i = 0; i < pbs.length; i++) {
			port = this.portBase + 2*i;
			this.usedPorts[i] = port;
			this.rtpMgrs[i] = RTPManager.newInstance();	    
			ip = InetAddress.getByName(this.ipAddr);
			local = new SessionAddress(InetAddress.getLocalHost(),port);
			dest = new SessionAddress(ip, port);
			this.rtpMgrs[i].initialize(local);
			this.rtpMgrs[i].addTarget(dest);
			
			System.out.println("Created session: " + this.ipAddr + " " + port);
			
			sendStream = this.rtpMgrs[i].createSendStream(this.dOutput, i);		
			sendStream.start();	    
		}
    }

    // Mudanças de estado do processador 
    private boolean failed = false;

    private Integer stateLock = new Integer(0);
    
    private Integer getStateLock() {
    	return stateLock;
    }

    private void setFailed() {
    	failed = true;
    }
    
    private synchronized void waitState(Processor p, int state) throws Exception {
		p.addControllerListener(new StateListener());
		failed = false;
	
		if (state == Processor.Configured) {
		    p.configure();
		} 
		else if (state == Processor.Realized) {
		    p.realize();
		}
		
		while (p.getState() < state && !failed) {
		    synchronized (getStateLock()) {
				try {
				    getStateLock().wait();
				} catch (InterruptedException ie) {
				    throw new Exception("Interrompeu espera do waitState");
				}
		    }
		}
		if (failed)
		    throw new Exception("Falhou em waitState");
    }

    private void stopTransmission() {
		synchronized (this) {
		    if (this.proc != null) {	
		    	this.proc.stop();
		    	this.proc.close();
		    	this.proc = null;		
				for (int i = 0; i < this.rtpMgrs.length; i++) {
					this.rtpMgrs[i].removeTargets("End of session");
					this.rtpMgrs[i].dispose();
				}
		    }
		}
    }

    private class StateListener implements ControllerListener {

		public void controllerUpdate(ControllerEvent ce) {
	
		    // Caso tenha havido erro durante o configure
		    // ou o realize, o processador será fechado
		    if (ce instanceof ControllerClosedEvent)
		    	setFailed();
	
		    if (ce instanceof ControllerEvent) {
				synchronized (getStateLock()) {
				    getStateLock().notifyAll();
				}
		    }
		}
    }
}
