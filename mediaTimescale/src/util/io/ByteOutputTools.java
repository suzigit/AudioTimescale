/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (06/05/2005)
 */
 
package util.io;

import util.data.SharedBuffer;
import util.data.Bits;
import util.data.ContentType;


/**
 * Essa classe disponibiliza um conjunto de operacoes de acesso
 * a um conjunto de bytes em memoria.
 */
public class ByteOutputTools implements OutputTools {

	private SharedBuffer buffer;

	public ByteOutputTools (SharedBuffer buffer) {
		this.buffer = buffer;
	}
	
	
	public void setContentType (ContentType ct) {
		this.buffer.setContentType(ct);
	}
	
	public InputTools getInputTools() {
		return new CircularInputTools(buffer);
	}
	
	/**
	 * Escreve conjunto de bits em objeto em memoria.
	 * @param sBits conjunto de bits a serem escritos
	 */
	public synchronized void output (String sBits) throws Exception {					
		if (sBits!=null && !sBits.equals("")) {
			Bits bits = new Bits(sBits);
			byte bytes[] = bits.getBytes();
			buffer.put(bytes);						
		}
	}
	
	/**
	 * Escreve conjunto de bytes em objeto em memoria.
	 * @param bytes conjunto de bytes a serem escritos
	 */
	public synchronized void output (byte bytes[]) throws Exception {
		if (bytes!=null) {
			buffer.put(bytes);
		}
	}
	
	public synchronized void output (byte bytes[], int delta, int length) throws Exception {
		if (bytes!=null) {
			byte[] newBytes = new byte[length];
			for (int i=delta; i<length; i++) {
				newBytes[i] = bytes[i];
			}
			buffer.put(newBytes);
		}
	}	

	
	public synchronized void output (byte bytes[], int length) throws Exception {
		output(bytes,0,length);
	}	
	

    /**
     * Indica que ja terminou a insercao de bytes no buffer.
     */    	
	public void finish () {
		this.buffer.finish();		
	}
	
	public boolean isFinished() {
		return this.buffer.isFinished();
	}
	
	public String getName() {
		return this.buffer.getName();
	}
	
}
