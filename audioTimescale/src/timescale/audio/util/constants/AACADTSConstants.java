/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (19/07/2004)
 */

package timescale.audio.util.constants;

import java.util.Hashtable;

public class AACADTSConstants extends MPEGConstants {

	private static AACADTSConstants instance = null;
	
	public String BUFFER_FULLNESS_NOT_APLICABLE = "11111111111";
	
	public String ONE_RAW_DATA_BLOCK = "00";
	
	public int MPEG2=1;
	public int MPEG4=0;
	
	/** 
	 * Controi instancia da classe. 
	 */  	
	public static MPEGConstants getInstance () {
		if (instance==null) {
			instance = new AACADTSConstants(); 
		}
		return instance;
	}
	
	private AACADTSConstants () {
		this.SYNC_WORD = "111111111111";
		this.HEADER_LENGTH = 56;
		NUMBER_OF_SAMPLES_PER_FRAME = 1024;
		
    	this.SAMPLING_FREQUENCY_TABLE = new Hashtable(16);
    	
		this.SAMPLING_FREQUENCY_TABLE.put("0","96");
		this.SAMPLING_FREQUENCY_TABLE.put("1","88.2");
		this.SAMPLING_FREQUENCY_TABLE.put("2","64");
		this.SAMPLING_FREQUENCY_TABLE.put("3","48");
		this.SAMPLING_FREQUENCY_TABLE.put("4","44.1");
		this.SAMPLING_FREQUENCY_TABLE.put("5","32");
		this.SAMPLING_FREQUENCY_TABLE.put("6","24");
		this.SAMPLING_FREQUENCY_TABLE.put("7","22.05");
		this.SAMPLING_FREQUENCY_TABLE.put("8","16");
		this.SAMPLING_FREQUENCY_TABLE.put("9","11.025");
		this.SAMPLING_FREQUENCY_TABLE.put("10","8");
	}
	
}
