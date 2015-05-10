/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/03/2005)
 */
 
package timescale.audio.util.constants;

import java.util.Hashtable;

/**
 * Essa classe contem contantes especificas para o tratamento de 
 * fluxos MPEG Backword Compatible.
 */
public class MPEGBCConstants extends MPEGConstants {

	public MPEGBCConstants () {
		SAMPLING_FREQUENCY_TABLE = new Hashtable(3);
		this.SYNC_WORD = "111111111111";
	    this.HEADER_LENGTH = 32;
	    
	    modeTable.put("00","0");
		modeTable.put("01","1");
		modeTable.put("10","2");
		modeTable.put("11","3");
	}
	
	/**
	 * Indica a posicao do ID dentro de um header MPEG BC.
	 */
	public static int INDEX_OF_ID_MPEG = 12;
	
	public static int INDEX_LAYER_BEGGIN = 13;
	public static int INDEX_LAYER_END = 15;
	
	/**
	 * Indica que fluxo e MPEG-1.
	 */
	public static int MPEG1 = 1;
	
	/**
	 * Indica que fluxo e MPEG-2.
	 */	
	public static int MPEG2 = 0;
	
	/**
	 * Indica modo de reproducao entre canais do audio de um frame.
	 */	
	public int SINGLE_CHANNEL_MODE = 3;

	/**
	 * Numero de possibilidades do campo BIT_RATE_INDEX. 
	 */
	public int MAX_BIT_RATE_INDEX = 14;

	/**
	 * Tabela contendo valores do campo BIT_RATE_INDEX.
	 * Mapeia index (que equivale ao valor inteiro do seu código de bits) nos valores do campo.  
	 */
	public Hashtable BIT_RATE_TABLE = new Hashtable(MAX_BIT_RATE_INDEX+1);
	
	
	public Hashtable modeTable = new Hashtable(4);

	/** 
	 * Indica numero de bits para cada slot de um quadro 
	 */  	
	public int BITS_PER_SLOT = 8;


	/** 
	 * Controi instancia da classe.
	 * @param id identificador se e MPEG-1 ou MPEG-2
	 * @param layer identifica se e MP2 ou MP3 
	 */  	
	public static MPEGBCConstants getInstance (int id, String layer) {		
		MPEGBCConstants constants = null;
		if (LAYER3.equals(layer)) {
			constants = MP3Constants.getInstance(id);
		}
		else if (LAYER2.equals(layer)) {
			constants = MP2Constants.getInstance(id);
		}		
		return constants;
	}


	
}
