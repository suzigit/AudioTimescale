/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (07/03/2005)
 */
 
package timescale.audio.util.constants;

/**
 * Essa classe contem contantes especificas para o tratamento de 
 * fluxos MP3 MPEG-2.
 */
public class MPEG2_MP3Constants extends MP3Constants {

	private static MPEG2_MP3Constants instance = null;
	
	/** 
	 * Controi instancia da classe. 
	 */  
	public static MPEGConstants getInstance () {
		if (instance==null) {
			instance = new MPEG2_MP3Constants();			
		}
		return instance;
	}

	private MPEG2_MP3Constants () {
		
		NUMBER_OF_SAMPLES_PER_FRAME = 576;
		
		this.SIDE_INFORMATION_LENGTH_SINGLE = 9*8;
		this.SIDE_INFORMATION_LENGTH_NOT_SINGLE = 17*8;

		this.MAIN_DATA_BEGIN_LENGTH = 8;
		
		this.BIT_RATE_TABLE.put("0","0");
		this.BIT_RATE_TABLE.put("1","8");
		this.BIT_RATE_TABLE.put("2","16");
		this.BIT_RATE_TABLE.put("3","24");
		this.BIT_RATE_TABLE.put("4","32");
		this.BIT_RATE_TABLE.put("5","40");
		this.BIT_RATE_TABLE.put("6","48");
		this.BIT_RATE_TABLE.put("7","56");
		this.BIT_RATE_TABLE.put("8","64");
		this.BIT_RATE_TABLE.put("9","80");
		this.BIT_RATE_TABLE.put("10","96");
		this.BIT_RATE_TABLE.put("11","112");
		this.BIT_RATE_TABLE.put("12","128");
		this.BIT_RATE_TABLE.put("13","144");
		this.BIT_RATE_TABLE.put("14","160"); 		

		this.SAMPLING_FREQUENCY_TABLE.put("0","22.05");
		this.SAMPLING_FREQUENCY_TABLE.put("1","24");
		this.SAMPLING_FREQUENCY_TABLE.put("2","16");	
	}


}
