/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:cavendi@telemidia.puc-rio.br">Sergio Cavendish</a>
 * Creation date: (25/02/2005)
 */

package util.io;

import util.data.ContentType;
import util.data.GrowableByteBuffer;

public interface InputTools {
	
	public byte[] getBytes(int length) throws Exception;
	
	public String getBits(int length) throws Exception;
	
	public int read(byte[] buffer, int offset, int length) throws Exception;
	
	public boolean lookBitsAhead(String s) throws Exception; 	
	
	public GrowableByteBuffer nextStartCode(String startCode) throws Exception;

	public GrowableByteBuffer getTail() throws Exception;
	
	public void close() throws Exception;
	
	public ContentType getType();
	
	public void reset() throws Exception;
	
	public String getInputName();
}