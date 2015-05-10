/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (28/11/2005)
 */

package timescale.system.metadata;

public class MPEG2SystemConstants {
	public static final int PACK_BEGIN_HEADER_BYTES_LENGTH = 10;
	public static final String SYSTEM_HEADER_START_CODE = 
		"00000000000000000000000110111011";	
	public static final String PACK_HEADER_START_CODE = 
		"00000000000000000000000110111010";
	public static final String PES_HEADER_START_CODE = 
		"000000000000000000000001";
	public static final String FINISH_CODE = 
		"00000000000000000000000110111001";
	
	public static final long SYSTEM_CLOCK_FREQUENCE = 27000000;
	public static int TIME_FACTOR_CTE = 300;
	public static final double MAX_TIME_EXTENSION = TIME_FACTOR_CTE/((double) MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE);
}
