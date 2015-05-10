/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (20/05/2005)
 */

package timescale.audio.util.constants;

/**
 * Essa classe contem contantes especificas para o tratamento de fluxos MP2.
 * Seja MPEG-1 ou MPEG-2.
 */
public class MP2Constants extends MPEGBCConstants {
	
	private static MP2Constants instance = null;
	 
    /** 
	 * Controi instancia da classe. 
	 */     
	protected MP2Constants () {
	    NUMBER_OF_SAMPLES_PER_FRAME = 1152;
	    MAX_DEPENDENCY_NUMBER_OF_FRAMES_BEFORE = 1;
	}
	
	/** 
	 * Controi instancia da classe.
	 * @param id identificador se e MPEG-1 ou MPEG-2 
	 */  
    public static MP2Constants getInstance (int id) {
		MP2Constants constants = null;
		if (id==MPEG1) {
			constants = (MP2Constants) MPEG1_MP2Constants.getInstance();
		}
		else {
			constants = (MP2Constants) MPEG2_MP2Constants.getInstance();
		}
		return constants;
	}

	public static MPEGConstants getInstance () {		
		if (instance==null) {
			instance = new MP2Constants(); 
		}
		return instance;	
	}	

}
