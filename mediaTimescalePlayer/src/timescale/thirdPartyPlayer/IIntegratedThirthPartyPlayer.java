/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (12/08/2004)
 */

package timescale.thirdPartyPlayer;

import java.awt.Component;


public interface IIntegratedThirthPartyPlayer {

	public Component getControlPanelComponent ();
	
	public Component getVisualComponent();
	
	/**
	 * Retorna tempo de duracao da midia a ser exibida
	 * So pode ser chamado depois do prepare e a partir de um arquivo
	 * Se for um dataSource, so traz um valor valido depois da apresentacao total
	 * 
	 * @return tempo de duracao da midia.
	 */
	public double getMediaDurationInSeconds();
	
	
	/**
	 * Tempo decorrido desde o inicio da apresentacao, em segundos.
	 * 
	 * @return Tempo decorrido desde o inicio da apresentacao.
	 */
	public double getMediaTimeInSeconds();
	
}
