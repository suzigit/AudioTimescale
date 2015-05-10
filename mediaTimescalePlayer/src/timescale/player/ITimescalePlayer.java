/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/08/2004)
 */

package timescale.player;

import timescale.data.InstantsSet;
import timescale.data.FinalReport;
import timescale.event.TimescaleFinishedListener;
import timescale.event.TimescaleInstantListener;
import timescale.thirdPartyPlayer.IThirdPartyPlayer;
import util.io.InputTools;
import util.io.OutputTools;

/**
 * API de ajuste elastico
 */
public interface ITimescalePlayer extends Runnable {

	/** 
	 * Cria arquivo de audio audio ajustado.
	 * @param inputFile nome do arquivo de entrada
	 * @param outputFile nome do arquivo de saida (pode ser null)
	 * @param rate taxa de ajuste
	 */		
	public void config (InputTools inputTools, String outputFile, double factor) 
			throws Exception;
	
	public void config (InputTools inputTools, double factor) throws Exception;	

	/** 
	 * Cria arquivo de audio audio ajustado e configura player para toca-lo.
	 * @param inputFile nome do arquivo de entrada
	 * @param outputFile nome do arquivo de saida (pode ser null)
	 * @param rate taxa de ajuste
	 * @param ap player a exibir o arquivo de saida 
	 */		
	public void config (InputTools inputTools, String outputFile, 
			double factor, IThirdPartyPlayer ap) throws Exception;
	
	/** 
	 * Cria fluxo de audio audio ajustado e configura player para toca-lo.
	 * @param inputFile nome do arquivo de entrada
	 * @param rate taxa de ajuste
	 * @param ap player a exibir o fluxo ajustado 
	 */
	public void config (InputTools inputTools, double factor, IThirdPartyPlayer ap)
		throws Exception;	
	
	public void config (InputTools inputTools, double factor, InstantsSet instants, 
			IThirdPartyPlayer ap) throws Exception;	
		
	
	/** 
	 * Modifica taxa de ajuste a ser utilizada em ajuste elastico.
	 * @param factor nova taxa de ajuste
	 */		
	public void setFactor(double factor) throws Exception;
	

	public void pause();
	
	public void resume();
	
	public void stop();
	
	public void run();
	
	public void addTimescaleListener(TimescaleFinishedListener listener);

	public void addTimescaleInstantListener(TimescaleInstantListener listener);
	
	public OutputTools getOutputTools();
	
	public IThirdPartyPlayer getThirdPartyPlayer();
	
	public FinalReport getReport();

	/**
	 * Retorna o tempo da amostra da midia depois de realizado o ajuste.
	 * Caso a amostra tenha sido removida, retorna tempo onde ela deveria estar
	 * Caso tenha sido duplicada, retorna sua primeira ocorrencia
	 * @param originalInstant tempo original da amostra
	 * @return tempo atualizado da amostra
	 */
	public double getTimescaleInstant (double originalInstant);
		
}
