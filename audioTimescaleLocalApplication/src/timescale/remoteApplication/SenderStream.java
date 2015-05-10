/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (16/09/2005)
 */

package timescale.remoteApplication;

import javax.media.Manager;
import javax.media.MediaLocator;

import timescale.net.RTPServer;
import timescale.util.constants.IOConstants;


/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (16/09/2005)
 */

import javax.media.protocol.DataSource;

/**
 * 
 * @author smbm
 *
 * Essa classe envia stream a ser processado pela aplicacao de ajuste em tempo real.
 */
public class SenderStream implements Runnable {
	
	private String URL;

	public SenderStream (String URL) {
		this.URL = URL;
	}
	
	public void run() {
		try {
			MediaLocator locator = new MediaLocator(URL);
			DataSource dataSouce = Manager.createDataSource(locator);
			String rtpMachine = IOConstants.RTP_MACHINE_MULTICAST;
			int rtpPortBase = IOConstants.ORIGINAL_STREAM_READER_RTP_PORT;
			int waitTime = 10*IOConstants.RTP_WAIT_TIME;
			RTPServer rtpServer = new RTPServer (dataSouce, rtpMachine, rtpPortBase, waitTime);
			rtpServer.prepare();
			Thread t = new Thread(rtpServer);
			t.start();
		}
		catch (Exception e) {
			System.out.println("Erro no SenderStream: " + e.getMessage());
		}
	}
}
