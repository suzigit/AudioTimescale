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

public abstract class GeneralFormatConstants {

	/**
	 * String de sincronizacao no inicio de um frame. 
	 */
	public String SYNC_WORD;
	
	/**
	 * Indica numero de bits do header de um frame.
	 */
	public int HEADER_LENGTH;	
	
	/**
	 * Constante que indica de quantos frames, no maximo, um frame depende de outro.
	 * Na realidade, o numero pode ser menor, dependendo do tamanho do frame.  
	 */
	public int MAX_DEPENDENCY_NUMBER_OF_FRAMES = 0;

	/**
	 * Indica dependencia de quadros anteriores (caso exista)
	 */
	public int MAX_DEPENDENCY_NUMBER_OF_FRAMES_BEFORE = 0;	
	
	/**
	 * Tabela contendo valores do campo SAMPLING_FREQUENCY.
	 * Mapeia index (que equivale ao valor inteiro do seu código de bits) nos valores do campo.  
	 */
	public Hashtable SAMPLING_FREQUENCY_TABLE;
	
	/** 
	 * Indica numero de amostras em um quadro do fluxo 
	 */  	
	public int NUMBER_OF_SAMPLES_PER_FRAME = -1;
}
