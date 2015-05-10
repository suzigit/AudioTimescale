/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (28/04/2005)
 */

package timescale.audio.format.generalFormat;

import timescale.audio.util.constants.GeneralFormatConstants;
import util.functions.Convert;

/**
 * Essa classe representa o header de um frame de audio.
 */
public abstract class Header implements Cloneable {

	protected BorrowBytesHandler borrowBytesHandler;
	protected GeneralFormatConstants constants;
    protected String samplingFrequency;

	public void setBorrowBytes (BorrowBytesHandler borrowBytesHandler) {
		this.borrowBytesHandler = borrowBytesHandler;
	}
	
	public BorrowBytesHandler getBorrowBytesHandler () {
		return this.borrowBytesHandler;
	}
	

	/**
	 * Retorna bytes representam objeto.
	 * @return bytes representam objeto
	 */
    public abstract byte[] toBytes();
    
	/**
	 * Retorna clone do objeto.
	 * @return clone do objeto
	 */    
	public abstract Object clone();

	protected double getSamplingFrequency() {
		int index = Convert.bitsToInt(this.samplingFrequency);
		return Double.parseDouble((String)(this.constants.SAMPLING_FREQUENCY_TABLE.get(index+""))); 
	}	
	
	public double getTimeDuration() {
		double sf = this.getSamplingFrequency();
		return (1/(1000*sf))*this.constants.NUMBER_OF_SAMPLES_PER_FRAME;
	}
	
	public abstract int lengthFrameInBytes();


}
