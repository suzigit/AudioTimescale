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

public interface IThirdPartyPlayer {
	
	public void prepare () throws Exception;
	
	public void play () throws Exception;
	
	public void stop();
	
	public void setInput (Object o) throws Exception;

			
}
