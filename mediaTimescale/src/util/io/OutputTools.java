/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (28/02/2005)
 */
 
package util.io;

import java.io.IOException;

/**
 * Essa classe disponibiliza um conjunto de operacoes de acesso
 * a um conjunto de bytes.
 */
public interface OutputTools {

	/**
	 * Dar saida a um conjunto de bytes.
	 * @param bytes bytes a serem enviados para saida
	 */	    
	public void output (byte bytes[]) throws Exception;
	public void output (byte bytes[], int length) throws Exception;
	public void output (byte bytes[], int delta, int length) throws Exception;
	
	/**
	 * Dar saida a um conjunto de bits.
	 * @param sBits bits a serem enviados para saida
	 */		
	public void output (String sBits) throws Exception;
	
    /**
     * Indica que ja terminou a insercao de bytes nos buffers.
     */  	
	public void finish() throws IOException;
	
	public boolean isFinished();
	
	public String getName();
}
