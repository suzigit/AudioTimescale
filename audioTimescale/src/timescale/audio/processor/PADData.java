/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (13/10/2004)
 */
 
package timescale.audio.processor;

/**
 * Essa classe contem informacoes do PAD que esta pode ser aproveitado 
 * durante o processamento do stream.
 */
public class PADData {
	
	// Armazena o número de bytes do PAD que está sendo deslizado
	private int numberOfBytes;

	/** 
	 * Controi instancia da classe.
	 */	
	public PADData () {
		this.numberOfBytes = 0;
	}
	
	/** 
	 * Retorna numero de bytes do PAD.
	 * @return numero de bytes do PAD
	 */			
	public int getNumberOfBytes () {
		return this.numberOfBytes;
	}
	
	/** 
	 * Modifica numero de bytes do PAD.
	 * @param delta novo numero de bytes do PAD
	 */			
	public void setNumberOfBytes(int delta) {
		this.numberOfBytes = delta;
	}
}
