/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (17/02/2005)
 */

package timescale.data;

/**
 * Essa classe identifica unicamente um frame de audio.
 */
public class ElementID {
	
	private double id;

	/** 
	 * Controi instancia da classe.
	 * @param id identificador do quadro 
	 */	
	public ElementID (double id) {
		this.id = id;
	}
	
	public static ElementID createIDBetweenElements(ElementID before, ElementID after) {
		double id = (before.getValue() + after.getValue())/2;
		return new ElementID(id);
	}

	public static ElementID nextIDToNewFrame(ElementID before) {
		double id = before.getValue() +  0.1;
		return new ElementID(id);
	}
	
	public static ElementID nextID(ElementID before) {
		double id = before.getValue() +  1;
		return new ElementID(id);
	}
 
	
	/** 
	 * Retorna valor do identificador
	 * @return valor do identificador 
	 */	
	public double getValue() {
		return id;
	}
	
	public Object clone() {
		return new ElementID(this.id);
	}
	
	/**
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */	
	public String toString () {
		return id + "";
	}
	
	/**
	 * Verifica se objeto do argumento e igual a este objeto.
	 * @return objeto a ser comparado
	 */	
	public boolean equals (ElementID other) {
		return (other.getValue()==id);
	}

	/**
	 * Verifica se valor do identificador e numero inteiro.
	 * @return true sse valor do identificador e numero inteiro 
	 */		
	public boolean isInteger() {
		return ((int)id) == id;
	}
}

	

