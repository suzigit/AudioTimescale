/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (09/08/2005)
 */

package timescale.thirdPartyPlayer;


public class VLCRTPPlayer extends TransmissionThirdPartPlayer {
	
	private String vlcPath;
	
	public VLCRTPPlayer (String vlcPath, String rtpMachine, int rtpPortBase, int waitingTime) {
		super (rtpMachine, rtpPortBase, waitingTime);
		this.vlcPath = vlcPath;
	}

	/** 
	 * Exibe midia.
	 */		
	public synchronized void play () throws Exception {
		
		int ports[] = this.rtpServer.getUsedPorts();
		for (int i=0; i<ports.length; i++) {
			Runtime r = Runtime.getRuntime();
			r.exec(this.vlcPath + "vlc -vvv udp://@" + this.rtpServer.getIpAdd() + ":" + ports[i]);			
		}
		
		super.play();
	}
	
	public void stop() {
		super.stop();
	}	

}
