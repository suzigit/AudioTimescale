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
 * Essa classe contem contantes especificas para o tratamento de fluxos MP3.
 * Seja MPEG-1 ou MPEG-2.
 */
public class MP3Constants extends MPEGBCConstants {
				
	private static MP3Constants instance = null;
	
	protected MP3Constants () {
	    MAX_DEPENDENCY_NUMBER_OF_FRAMES = 10;
	    MAX_DEPENDENCY_NUMBER_OF_FRAMES_BEFORE = 1;
	}
    
    /**
	 * Indica numero de bits do side information de um frame MP3 
	 * quando este tem modo single channel. 
	 */
	public int SIDE_INFORMATION_LENGTH_SINGLE;

	/**
	 * Indica numero de bits do side information de um frame MP3 
	 * quando este tem modo diferente de single channel. 
	 */
	public int SIDE_INFORMATION_LENGTH_NOT_SINGLE;

	/**
	 * Indica numero de bits do campo MAIN_DATA_BEGIN de um frame MP3. 
	 */
	public int MAIN_DATA_BEGIN_LENGTH;

	/** 
	 * Controi instancia da classe.
	 * @param id identificador se e MPEG-1 ou MPEG-2 
	 */  
	public static MP3Constants getInstance (int id) {	    
	    MP3Constants constants = null;
		if (id==MPEG1) {
			constants = (MP3Constants) MPEG1_MP3Constants.getInstance();
		}
		else {
			constants = (MP3Constants) MPEG2_MP3Constants.getInstance();
		}		
		return constants;
	}

	
	public static MPEGConstants getInstance () {		
		if (instance==null) {
			instance = new MP3Constants(); 
		}
		return instance;		
	}	
}
