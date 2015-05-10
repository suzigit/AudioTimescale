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

import javax.media.Manager;
import javax.media.protocol.DataSource;
import javax.media.Player;
import timescale.media.protocol.datasource.MediaDataSource;
import util.io.InputTools;


public class JMFDataSourcePlayer extends JMFIntegratedThirdPartPlayer {

	private DataSource ds;
	
	public JMFDataSourcePlayer() {}
	
	public JMFDataSourcePlayer(Player p) {
		super(p);
	}
	
	//TODO: passar apenas datasource (tirar esse metodo)
	public void setInput (Object o) throws Exception {
		InputTools inputTools = (InputTools) o;
		this.ds = new MediaDataSource(inputTools);
	}
	
	public void setInput (DataSource ds) {
		this.ds = ds;
	}
	
	public void prepare() throws Exception {		
		//DataSource ds = new MediaDataSource(inputTools);
		this.player = Manager.createRealizedPlayer(ds);
	}
	
	/** 
	 * Exibe midia.
	 * @param outputTools fonte de dados de saida de dados ajustados
	 */		
	public synchronized void play () throws Exception {
	  this.player.start();
	}


}
