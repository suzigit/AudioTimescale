/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (13/09/2004)
 */

package timescale.net;

import javax.media.*;
import javax.media.rtp.*;

import javax.media.control.BufferControl;

import java.net.*;
import javax.media.rtp.event.*;
import javax.media.protocol.DataSource;

public class RTPClient implements ReceiveStreamListener, SessionListener, Runnable {
    
	private IRTPHandlerData handler;
	
    private RTPManager rtpMgrs[] = null;
    
	private Object dSync = new Object();
	private boolean dRcvd = false; // dados recebidos
	
    private boolean continueTransmission;  
    private int waitTime;
   
	public RTPClient (IRTPHandlerData handler, int waitTime) {
		this.handler = handler;
		this.continueTransmission = true;
		this.waitTime = waitTime;
	}
	
    public void run () {		
		try {
			this.waitData();
		    while (this.continueTransmission)
		    	Thread.sleep(this.waitTime);
		} catch (Exception e) {} 
		close();
    }	
    
    public void endTransmissionSoon() {
		System.out.println("Mandou transmissão RTP cliente mesmo parar: EndTransmissionSoon");
    	this.continueTransmission=false;
    }
    
    public void prepare (SessionData sessions[]) throws Exception {
        try {
    	    rtpMgrs = new RTPManager[sessions.length];
    	    SessionAddress localAddr = new SessionAddress();
    	    SessionAddress destAddr;
    	    InetAddress ipAddr;
    	    SessionData session;

    	    for (int i = 0; i < sessions.length; i++) {

   		    session = sessions[i];
    		System.out.println("  --> Open session for address: " + session.address + " port: " + session.port + " ttl: " + session.ttl);

    		rtpMgrs[i] = (RTPManager) RTPManager.newInstance();
    		rtpMgrs[i].addReceiveStreamListener(this);
    		rtpMgrs[i].addSessionListener(this);

    		ipAddr = InetAddress.getByName(session.address);

    		if( ipAddr.isMulticastAddress()) {
    		    localAddr= new SessionAddress( ipAddr,
    						   session.port,
    						   session.ttl);

    		    destAddr = new SessionAddress( ipAddr,
    						   session.port,
    						   session.ttl);
    		} else {
    		    localAddr= new SessionAddress( InetAddress.getLocalHost(),
    			  		           session.port);

                destAddr = new SessionAddress( ipAddr, session.port);
    		}
    			
    		rtpMgrs[i].initialize(localAddr);

    		BufferControl bControl = (BufferControl)rtpMgrs[i].getControl("javax.media.control.BufferControl");

    		if (bControl != null)
    		    bControl.setBufferLength(350);
    		    rtpMgrs[i].addTarget(destAddr);
    	    }

            } catch (Exception e){
                System.err.println("Cannot create session: " + e.getMessage());
                throw e;
            }
    }
    
    /*
     * Espera dados chegarem
     */
    private void waitData() throws Exception {
	
		long then = System.currentTimeMillis();
	
		try{
		    synchronized (dSync) {
			while (!dRcvd && 
				System.currentTimeMillis() - then < 2*waitTime) {
			    if (!dRcvd)
				//System.out.println("  --> Waiting for data to arrive...");
			    dSync.wait(1000);
			}
		    }
		} catch (Exception e) {}
	
		if (!dRcvd) {
		    throw new Exception ("No data was received.");
		}
    }


    /**
     * SessionListener
     */
    public synchronized void update(SessionEvent evt) {
		if (evt instanceof NewParticipantEvent) {
		    Participant p = ((NewParticipantEvent)evt).getParticipant();
		    System.out.println("  --> A new participant joined: " + p.getCNAME());
		}
    }


    /**
     * ReceiveStreamListener
     */
    public synchronized void update( ReceiveStreamEvent evt) {

		Participant participant = evt.getParticipant();
		ReceiveStream stream = evt.getReceiveStream();
	
		if (evt instanceof RemotePayloadChangeEvent) {
	     
		    System.out.println("  --> Received an RTP PayloadChangeEvent.");
		    System.out.println("Sorry, cannot handle payload change.");
		    System.exit(0);
	
		}
	    
		else if (evt instanceof NewReceiveStreamEvent) {
	
		    try {
				receiveNewStream(evt, participant);
	
		    } catch (Exception e) {
			System.err.println("NewReceiveStreamEvent exception " + e.getMessage());
			return;
		    }
	        
		}
	
		else if (evt instanceof StreamMappedEvent) {
	
		    if (stream != null && stream.getDataSource() != null) {
		    	
				DataSource dSource = stream.getDataSource();
				// Descobrir os formatos
				RTPControl ctl = (RTPControl)dSource.getControl("javax.media.rtp.RTPControl");
				System.out.println("  --> The previously unidentified stream ");
				if (ctl != null) {
				    System.out.println("      " + ctl.getFormat());
				}
				System.out.println("      has now been identified as sent by: " + participant.getCNAME());
		     }
		}
	
		else if (evt instanceof ByeEvent) {			 
		     System.out.println("  - Got 'bye' from: " + participant.getCNAME() + this.hashCode());
		}

    }

    
    private void receiveNewStream(ReceiveStreamEvent evt, Participant participant) throws Exception, NoPlayerException, CannotRealizeException {
		ReceiveStream stream;
		stream = ((NewReceiveStreamEvent)evt).getReceiveStream();
		DataSource dSource = stream.getDataSource();

		// Descobre os formatos
		RTPControl ctl = (RTPControl)dSource.getControl("javax.media.rtp.RTPControl");
		if (ctl != null){
		    System.out.println("  --> Received new stream: " + ctl.getFormat());
		} else
		    System.out.println("  --> Received new stream");

		if (participant == null)
		    System.out.println("      The sender of this stream had no identification");
		else {
		    System.out.println("      The stream comes from: " + participant.getCNAME()); 
		}

		// Cria um player passando o datasource para o Media Manager
		System.out.println(dSource.getContentType());

		this.handler.receiveStream(dSource);
		
		// Um novo fluxo se apresenta
		synchronized (dSync) {
			dRcvd = true;
			dSync.notifyAll();
		}
	}

    private void close() {

    	// fecha a sessão RTP
    	for (int i = 0; i < rtpMgrs.length; i++) {
    	    if (rtpMgrs[i] != null) {
                    rtpMgrs[i].removeTargets( "Closing session from RTPClient");
                    rtpMgrs[i].dispose();
                    rtpMgrs[i] = null;
    	    }
    	}
    }
    
 
    /**
     * Enderecos de sessao passados pelo usuario
     */
    public class SessionData {
    	public String address;
    	public int port;
    	public int ttl;
    }
    
}


