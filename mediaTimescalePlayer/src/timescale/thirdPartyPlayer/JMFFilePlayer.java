/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (05/05/2005)
 */
 
package timescale.thirdPartyPlayer;

import javax.media.Manager;
import javax.media.MediaLocator;



/**
 * Essa classe exibe um fluxo de audio utilizando o JMF.
 *
 * @author Suzana Mesquita de Borba Maranhão
 */
public class JMFFilePlayer extends JMFIntegratedThirdPartPlayer {

	private String url;
	
	
	public void setInput (Object o) {
		this.url = (String) o;
	}
		
	/** 
	 * Prepara player para exibicao de midia.
	 * @param url da midia a ser exibida
	 */		
	public synchronized void prepare () throws Exception {		      	        
		MediaLocator mlr = new MediaLocator("file:" + url);
		javax.media.protocol.DataSource ds = Manager.createDataSource(mlr);
		this.player = Manager.createRealizedPlayer(ds);
	}	
	
	public void play () throws Exception {
	}
	
	/** 
	 * Libera o player de previa exibicao de midia.
	 */		
	public synchronized void stop() {
        if (this.player!=null) {
        	this.player.stop();
        	this.player.deallocate();
        	this.player.close();
        	this.player = null;
        }
	}

}
