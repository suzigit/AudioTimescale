/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (04/09/2005)
 */

package timescale.data;

public class FrameProcessmentInfo {
	
	private double id;
	private int action;
	
	public FrameProcessmentInfo (double id, int action) {
		this.id = id;
		this.action = action;
	}
	
	public double getId() {
		return this.id;
	}

	public int getAction () {
		return this.action;
	}
	
}
