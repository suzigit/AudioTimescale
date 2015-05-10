/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (19/05/2005)
 */
 
package timescale.util.constants;

/**
 * Essa classe contem contantes utilizadas na aplicacao.
 */
public class Constants {
	
	/**
	 * Indica operação a ser realizada nos frames de um fluxo.
	 */	
	public static class TimescaleOperation {
		public static final int CUT = 0;		
		public static final int INSERT = 1;		
		public static final int NONE = 2;			
	}
	
	public static class Media {
		public static final int AUDIO = 0;
		public static final int VIDEO = 1;
		public static final int SYSTEM = 2;
	}

	public static class Extension {
		public static final String MP2 = "mp2";
		public static final String MP3 = "mp3";
		public static final String AC3 = "ac3";
		public static final String AAC = "aac";
		public static final String MPEG2_VIDEO = "m2v";
		public static final String MPEG_SYSTEM_A = "mpeg";
		public static final String MPEG_SYSTEM_B = "mpg";
	}
	
	public static class ContentType {
		public static final String MPEG_AUDIO_BC = "audio.mpeg";
		public static final String MP2 = "audio.mpeg";
		public static final String MP3 = "audio.mpeg";
		public static final String AAC = "audio.aac";
		public static final String AC3 = "audio.ac3";
		public static final String MPEG2_VIDEO = "video.mpeg";
		public static final String MPEG_SYSTEM = "video.mpeg";		
	}

	/** 
	 * Tamanho do buffer de saida dos dados ajustados.
	 * O tamanho reservado eh importante porque o player pede para voltar
	 * ate 65536 bytes	 
	 */	
	//TODO: pensar como definir tamanho desse buffer
	//Isso ocorreu com o VLC no Windows, mas nao no JMF no linux
	//Se pequeno, da problema na exibicao do video. Usei fator de 10
	//Com audio deve ser menor para permitir mudar taxa mais rapidamente (fator 3)
	//Cuidado ao mexer nesses fatores tb para considerar leitura de arquivos de sistemas
	public final static int RESERVED_BUFFER_LENGTH = 80000;
	public final static int SHARED_BUFFER_LENGTH = (int) (RESERVED_BUFFER_LENGTH*10);
	
	
	public static int BUFFER_PES_LENGTH = 50;

}
