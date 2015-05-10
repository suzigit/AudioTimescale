/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (12/07/2005) 
 */ 
package timescale.audio.util.constants;

import java.util.Hashtable;


public class AC3Constants extends GeneralFormatConstants {
    
	private static AC3Constants instance = null;
	
	public int[][] wordsPerframe = {
	        {96, 69, 64},
	        {96, 70, 64},
	        {120, 87, 80},
	        {120, 88, 80},
	        {144, 104, 96},
	        {144, 105, 96},
	        {168, 121, 112},
	        {168, 122, 112},
	        {192, 139, 128},
	        {192, 140, 128},
	        {240, 174, 160},
	        {240, 175, 160},
	        {288, 208, 192},
	        {288, 209, 192},
	        {336, 243, 224},
	        {336, 244, 224},
	        {384, 278, 256},
	        {384, 279, 256},
	        {480, 348, 320},
	        {480, 349, 320},
	        {576, 417, 384},
	        {576, 418, 384},
	        {672, 487, 448},
	        {672, 488, 448},
	        {768, 557, 512},
	        {768, 558, 512},
	        {960, 696, 640},
	        {960, 697, 640},
	        {1152, 835, 768},
	        {1152, 836, 768},
	        {1344, 975, 896},
	        {1344, 976, 896},
	        {1536, 1114, 1024},
	        {1536, 1115, 1024},
	        {1728, 1253, 1152},
	        {1728, 1254, 1152},
	        {1920, 1393, 1280},
	        {1920, 1394, 1280}
	};
	
    private AC3Constants() {
    	this.SAMPLING_FREQUENCY_TABLE = new Hashtable(3);
        this.SYNC_WORD = "0000101101110111";
        this.HEADER_LENGTH = 40;
        NUMBER_OF_SAMPLES_PER_FRAME = 1536;
        
		this.SAMPLING_FREQUENCY_TABLE.put("0","48");
		this.SAMPLING_FREQUENCY_TABLE.put("1","44.1");
		this.SAMPLING_FREQUENCY_TABLE.put("2","32");
    }
    
	public static AC3Constants getInstance () {		
		if (instance==null) {
			instance = new AC3Constants();
		}
		return instance;		
	}
	
   
}
