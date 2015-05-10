/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (05/05/2004)
 */
 
package timescale.audio.util.constants;

/**
 * Essa classe contem constantes de configuracao para iniciar a aplicacao. 
 */
public class ConfigConstants {

	/** 
	 * Indica se deve habilitar coleta de estatisticas ou nao.
	 */
	public static final boolean ENABLE_ALGORITHM_ANALYZER = false;	
	
	/** 
	 * Tamanho, em frames, para adicionar ao tamanho do buffer que seria 
	 * utilizado para processar os streams. No caso de MP3, um buffer do tamanho de 
	 * MAX_DEPENDENCY_NUMBER_OF_FRAMES (ContanstsMP3) eh o tamanho minimo
	 * do buffer.  
	 */
	public static final int NUMBER_FRAMES_PROCESSMENT_BUFFER = 10;
	
	/** 
	 * Caminho de arquivo de coleta gerais de estatisticas.
	 */	
	public static final String REPORT_URL = "report.txt";
	
	/** 
	 * Caminho de arquivo de coleta gerais de estatisticas.
	 */
	public static final String REPORT_DATA_URL = "report.csv";
	
	/** 
	 * Caminho de arquivo de coleta de quais quadros foram processados.
	 */
	public static final String REPORT_IDS_DATA_URL = "reportIds.csv";
					
}
