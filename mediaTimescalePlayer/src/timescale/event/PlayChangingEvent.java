/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (14/09/2005)
 */
 

package timescale.event;

import java.util.EventObject;

import timescale.thirdPartyPlayer.JMFIntegratedThirdPartPlayer;

public class PlayChangingEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	
	private JMFIntegratedThirdPartPlayer tpPlayer;
	
	/** 
	 * Controi instancia da classe.
	 */
	public PlayChangingEvent (JMFIntegratedThirdPartPlayer tpPlayer) {
		super(new Object());
		this.tpPlayer = tpPlayer;		
	}
	
	/** 
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */
	public String toString() {
		return "Fim da apresentação";
	}
	
	public JMFIntegratedThirdPartPlayer getPlayer() {
		return this.tpPlayer;
	}
}