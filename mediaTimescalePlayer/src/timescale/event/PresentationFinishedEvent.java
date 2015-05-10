
/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (17/08/2005)
 */
 

package timescale.event;

import java.util.EventObject;

public class PresentationFinishedEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	/** 
	 * Controi instancia da classe.
	 */
	public PresentationFinishedEvent () {
		super(new Object());
	}
	
	/** 
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */
	public String toString() {
		return "Fim da apresentação";
	}
}

