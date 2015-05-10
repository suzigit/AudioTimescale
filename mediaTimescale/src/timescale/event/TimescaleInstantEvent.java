/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (09/09/2005)
 */
 
package timescale.event;

import java.util.EventObject;


/**
 * Essa classe e um evento que representa a atualizacao de uma ancora devido 
 * ao ajuste elastico de uma midia.
 *
 * @author Suzana Mesquita de Borba Maranhão
 */
public class TimescaleInstantEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	private double originalInstant;
	private double updatedInstant;
	
	/** 
	 * Controi instancia da classe.
	 * @param newAnchor
	 */
	public TimescaleInstantEvent (double originalInstant, double updatedInstant) {
		super(new Object());
		this.originalInstant = originalInstant;
		this.updatedInstant = updatedInstant;
	}
	
	/** 
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */
	public String toString() {
		return "Ancora atualizada com sucesso";
	}

	public double getOriginalInstant() {
		return this.originalInstant;
	}

	public double getUpdatedInstant() {
		return this.updatedInstant;
	}

}
