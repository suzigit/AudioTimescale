/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (08/08/2004)
 */

package timescale.util.constants;

public class IOConstants {
	
	//public final static String VLC_PATH = "C:\\Arquivos de programas/VideoLan/VLC/";
	public static String VLC_PATH = "C:\\Temp\\videolan\\VLC\\";
	
	public final static String WINAMP_PATH = "C:\\Temp/";
	
	public final static String RTP_MACHINE = "localhost";
	
	public final static String RTP_MACHINE_MULTICAST = "224.255.255.255";	
	
	public final static int RTP_TTL = 0;
	
	public final static int THIRD_PART_PLAYER_RTP_PORT = 1234;
	
	public final static int ORIGINAL_STREAM_READER_RTP_PORT = 2345;	
	
	public final static int RTP_WAIT_TIME = 10000;
	
	public final static String THIRD_PART_PLAYER_NAME = "thirdpartplayer";
	public final static String ORIGINAL_STREAM_READER_PLAYER_NAME = "remote";
	
	/**
	 * Quantidade de dados a ser lida por vez do arquivo de entrada
	 * para ser jogada no SharedBuffer.
	 */
	public static final int BUFFER_INPUT_FILE_LENGTH = 1000;
	
	public static class InputSource {
		public static final int REMOTE = 0;		
		public static final int LOCAL = 1;		
		public static final int NO_INPUT = 2;	
	}

}
