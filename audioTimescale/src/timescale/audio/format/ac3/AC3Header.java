/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (13/07/2005)
 */
 
package timescale.audio.format.ac3;

import timescale.audio.format.generalFormat.BorrowBytesHandler;
import timescale.audio.format.generalFormat.Header;
import timescale.audio.util.constants.AC3Constants;
import util.functions.Convert;

public class AC3Header extends Header {

    private String crc;
    private String frmsizecod;
    
    
    public AC3Header(String data) {
        this.crc = data.substring(16,32);
        this.samplingFrequency = data.substring(32,34);
        this.frmsizecod = data.substring(34,40);
        this.constants = AC3Constants.getInstance();
    }
    
    private AC3Header(String crc, String samplingFrequency, String frmsizecod, 
    		BorrowBytesHandler borrowBytesHandler) {
        this.crc = crc;
        this.samplingFrequency = samplingFrequency;
        this.frmsizecod = frmsizecod;
        this.constants = AC3Constants.getInstance();
        this.borrowBytesHandler = borrowBytesHandler;
    }    
    
	private int getIntSamplingFrequency() {
		return Convert.bitsToInt(this.samplingFrequency);
	}
	
	public double getSamplingFrequency() {
		return Double.parseDouble((String)(this.constants.SAMPLING_FREQUENCY_TABLE.get(getIntSamplingFrequency()+"")));
	}
	
	private int getFrmsizecod() {
		return Convert.bitsToInt(this.frmsizecod);
	} 
    
	/**
	 * Retorna tamanho do frame. 
	 * @return tamanho do frame
	 */
	public int lengthFrameInBytes () {
	    int x = 2 - this.getIntSamplingFrequency();
	    int y = this.getFrmsizecod();
	    int wordSize = ((AC3Constants) constants).wordsPerframe[y][x];
	    return wordSize*2;
	}
    
   
	/**
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */    
	public String toString () {
		StringBuffer value = new StringBuffer("");
		
		value.append(this.constants.SYNC_WORD);
		value.append(crc);
		value.append(samplingFrequency);
		value.append(frmsizecod);
		
		return  value.toString(); 
	}
    
	/**
	 * Retorna bytes representam objeto.
	 * @return bytes representam objeto
	 */
    public byte[] toBytes() {
        return Convert.bitsToBytes(this.toString());        
    }
    
	/**
	 * Retorna clone do objeto.
	 * @return clone do objeto
	 */    
	public Object clone() {
	    AC3Header h = new AC3Header (this.crc,this.samplingFrequency, this.frmsizecod, 
	    		this.borrowBytesHandler);
	    return h;
	}
}
