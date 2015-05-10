/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (03/09/2005)
 */

package timescale.data;

public class InstantsSet {
	private double[] originalAnchors;
	private double[] newAnchors;
		
	public InstantsSet(double[] originalAnchors) {		
		if (originalAnchors!=null) {
			this.originalAnchors = originalAnchors;
		}
		else {
			this.originalAnchors = new double[0];
		}
		this.newAnchors = new double[this.originalAnchors.length];	
	}
	
	public double[] getOriginalAnchors() {
		return this.originalAnchors;
	}
	
	public double[] getNewanchors() {
		return this.newAnchors;
	}
	
	public void setNewAnchor (double a, int index) {
		this.newAnchors[index] = a;
	}

}
