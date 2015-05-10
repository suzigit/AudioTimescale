/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (10/05/2004)
 */
 
package timescale.audio.util.constants;

/**
 * Essa classe contem contantes especificas para o tratamento de 
 * fluxos MP3 MPEG-1.
 */
public class MPEG1_MP3Constants extends MP3Constants {

	private static MPEG1_MP3Constants instance = null;
	
	/** 
	 * Controi instancia da classe. 
	 */  	
	public static MPEGConstants getInstance () {
		if (instance==null) {
			instance = new MPEG1_MP3Constants(); 
		}
		return instance;
	}
	
	private MPEG1_MP3Constants () {
		
		NUMBER_OF_SAMPLES_PER_FRAME = 1152;
		
		this.SIDE_INFORMATION_LENGTH_SINGLE = 17*8;
		this.SIDE_INFORMATION_LENGTH_NOT_SINGLE = 32*8;

		this.MAIN_DATA_BEGIN_LENGTH = 9;

		this.BIT_RATE_TABLE.put("0","0");
		this.BIT_RATE_TABLE.put("1","32");
		this.BIT_RATE_TABLE.put("2","40");
		this.BIT_RATE_TABLE.put("3","48");
		this.BIT_RATE_TABLE.put("4","56");
		this.BIT_RATE_TABLE.put("5","64");
		this.BIT_RATE_TABLE.put("6","80");
		this.BIT_RATE_TABLE.put("7","96");
		this.BIT_RATE_TABLE.put("8","112");
		this.BIT_RATE_TABLE.put("9","128");
		this.BIT_RATE_TABLE.put("10","160");
		this.BIT_RATE_TABLE.put("11","192");
		this.BIT_RATE_TABLE.put("12","224");
		this.BIT_RATE_TABLE.put("13","256");
		this.BIT_RATE_TABLE.put("14","320"); 	
		
		this.SAMPLING_FREQUENCY_TABLE.put("0","44.1");
		this.SAMPLING_FREQUENCY_TABLE.put("1","48");
		this.SAMPLING_FREQUENCY_TABLE.put("2","32");		
	}

}
