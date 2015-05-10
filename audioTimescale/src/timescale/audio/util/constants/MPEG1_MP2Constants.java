/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (14/03/2005)
 */
 
package timescale.audio.util.constants;

/**
 * Essa classe contem contantes especificas para o tratamento de 
 * fluxos MP2 MPEG-1.
 */
public class MPEG1_MP2Constants extends MP2Constants {
	private static MPEG1_MP2Constants instance = null;
	
	
	/** 
	 * Controi instancia da classe. 
	 */  
	public static MPEGConstants getInstance () {
		if (instance==null) {
			instance = new MPEG1_MP2Constants(); 
		}
		return instance;
	}	
		
	private MPEG1_MP2Constants () {

		this.BIT_RATE_TABLE.put("0","0");
		this.BIT_RATE_TABLE.put("1","32");
		this.BIT_RATE_TABLE.put("2","48");
		this.BIT_RATE_TABLE.put("3","56");
		this.BIT_RATE_TABLE.put("4","64");
		this.BIT_RATE_TABLE.put("5","80");
		this.BIT_RATE_TABLE.put("6","96");
		this.BIT_RATE_TABLE.put("7","112");
		this.BIT_RATE_TABLE.put("8","128");
		this.BIT_RATE_TABLE.put("9","160");
		this.BIT_RATE_TABLE.put("10","192");
		this.BIT_RATE_TABLE.put("11","224");
		this.BIT_RATE_TABLE.put("12","256");
		this.BIT_RATE_TABLE.put("13","320");
		this.BIT_RATE_TABLE.put("14","384"); 	
		
		this.SAMPLING_FREQUENCY_TABLE.put("0","44.1");
		this.SAMPLING_FREQUENCY_TABLE.put("1","48");
		this.SAMPLING_FREQUENCY_TABLE.put("2","32");
				
	}	
}
