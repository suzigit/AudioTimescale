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

public abstract class MPEGConstants extends GeneralFormatConstants {
	
	/**
	 * Constantes que equivalem ao MP1, MP2 e MP3, AAC seja em MPEG-1 ou MPEG-2.
	 */
	public static String LAYER1 = "11";
	public static String LAYER2 = "10";
	public static String LAYER3 = "01";
	public static String LAYER4 = "00";
	
	/**
	 * Indica numero de bits de tratamento de erros de um frame MPEG BC.
	 */
	public int ERROR_CHECK_LENGTH = 16;

	
}
