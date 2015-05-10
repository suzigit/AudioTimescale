/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (06/05/2005)
 */

package timescale.backendgui;

import timescale.data.InstantsSet;
import timescale.data.FinalReport;
import timescale.player.ITimescalePlayer;
import timescale.player.TimescalePlayer;
import timescale.thirdPartyPlayer.IThirdPartyPlayer;
import util.io.InputTools;


/**
 * Essa classe concentra a logica da GUI para chamar metodos
 * da aplicacao e exibir resultados ao usuario.
 */
public class GUIController  {
	
	private ITimescalePlayer timescalePlayer;
	private GUIUpdater guiUpdater;
	
	//A GUI só deve ter uma thread da fachada
	private Thread t;
	
	
	/** 
	 * Controi instancia da classe.
	 * @param frame janela de exibicao cujas funcoes estao sendo controladas
	 */		
	public GUIController (GUIUpdater guiUpdater) { 
        this.timescalePlayer = new TimescalePlayer();
		this.guiUpdater = guiUpdater;
		this.guiUpdater.setController(this);
	}

	/** 
	 * Modifica taxa de ajuste a ser utilizada em ajuste elastico.
	 * @param dRate nova taxa de ajuste
	 */		
	public void setRate(double dRate) throws Exception {
		this.timescalePlayer.setFactor(dRate);
	}

	/** 
	 * Cria arquivo de audio audio ajustado.
	 * @param inputFile nome do arquivo de entrada
	 * @param outputFile nome do arquivo de saida
	 * @param rate taxa de ajuste
	 */	
	public void create(InputTools inputTools, String outputFile, double rate) throws Exception {	    
		stop();
		this.timescalePlayer.config(inputTools, outputFile, rate);
		this.timescalePlayer.addTimescaleListener(this.guiUpdater);
		t = new Thread(this.timescalePlayer);
		t.start();
	}
	
	/** 
	 * Cria arquivo de audio audio ajustado e cria player para exibi-la.
	 * @param inputFile nome do arquivo de entrada
	 * @param outputFile nome do arquivo de saida
	 * @param rate taxa de ajuste
	 */	
	public void createAndPlay (InputTools inputTools, String outputFile, 
			double rate, IThirdPartyPlayer ap) throws Exception {    
		stop();
		this.timescalePlayer.config(inputTools, outputFile, rate, ap);
		this.timescalePlayer.addTimescaleListener(this.guiUpdater);	
		t = new Thread(this.timescalePlayer);
		t.start();
    }

	/** 
	 * Ajusta arquivo de audio e o exibe em tempo real.
	 * @param inputFile nome do arquivo de entrada
	 * @param rate taxa de ajuste
	 */			
	public void play (InputTools inputTools, double rate, double[] dAnchors, 
			IThirdPartyPlayer ap) throws Exception {       
		stop();
		InstantsSet anchors = new InstantsSet(dAnchors);
		this.timescalePlayer.config(inputTools, rate, anchors, ap);
		this.timescalePlayer.addTimescaleInstantListener(this.guiUpdater);
		t = new Thread(this.timescalePlayer);
		t.start();
		
		ap.prepare();
		this.guiUpdater.showPlayer(ap);	
		ap.play();

	}

	/** 
	 * Para realizacao de ajusta elastico.
	 */				
	public void stop() {
		this.timescalePlayer.stop();
	}
	
	public ITimescalePlayer getPlayer() {
		return this.timescalePlayer;
	}
	
	/** 
	 * Retorna estatisticas do ajuste realizado.
	 * @return estatisticas do ajuste realizado
	 */
	public FinalReport getReport() {
		return this.timescalePlayer.getReport();
	}	
	
	public double getTimescaleInstant (double originalInstant) {
		double d = 0;
		if (this.t != null) {
			d = this.timescalePlayer.getTimescaleInstant(originalInstant);
		}
		return d;
	}
	
}
