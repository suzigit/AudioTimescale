/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (19/09/2005)
 */

package util.io;

import util.data.ContentType;
import util.data.GrowableByteBuffer;
import util.data.SharedBuffer;
import util.functions.Convert;

public class CircularInputTools implements InputTools {
	
	private SharedBuffer buffer;
	private int indexInBits;
	
	public CircularInputTools (SharedBuffer buffer) {
		this.buffer = buffer;
		this.indexInBits = 0;
	}
	
	/** 
	 * Realiza a leitura de conjunto de dados.
	 * @param buffer local a ser armazenado os bytes lidos
	 * @param offset index no buffer em que dados devem comecar a ser armazenados
	 * @param length numero de bytes solicitados 
	 * @return numero de bytes lidos ou -1 se não houver mais bytes
	 */
	public int read(byte[] buffer, int offset, int length) throws Exception {
		return this.buffer.read(buffer,offset,length);
	}
	
    /**
     * Indica se ja terminou a insercao de bytes no buffer.
     * @return true sse ja terminou a insercao de bytes no buffer
     */  		
	public boolean isEOF() {
		return this.buffer.isFinished();
	}
	
	public long tell () {
		return this.buffer.tell();
	}
	
	public long seek (long where) throws Exception {
		return this.buffer.seek(where);
	}
	
	public byte[] getBytes(int length) throws Exception {
		return this.buffer.getBytes(length);
	}
	
	private String getBytes(int length, boolean updateReader) throws Exception {
		byte bytes[] = new byte[length];
		this.buffer.read(bytes,0,bytes.length, updateReader);		
		String resultBytes = Convert.bytesToString(bytes);
		return resultBytes;
	}
	
	public String getBits(int length) throws Exception {
		return getBits(length,true);
	}	
	
	public String getBits(int length, boolean updateReader) throws Exception {
		int totalInBytes = length/8;
			
		//Deve pegar um byte a mais, pois precisar de alguns bits
		if (length%8!=0) {
			totalInBytes++;
			if (updateReader) {
				this.indexInBits = (length+this.indexInBits)%8;
			}
		}
		String resultBytes = getBytes(totalInBytes, updateReader);			
		return resultBytes.substring(0,length);
	}

	public GrowableByteBuffer nextStartCode(String startCode) throws Exception {		
		GrowableByteBuffer gbb = new GrowableByteBuffer(1);
		while (!lookBitsAhead(startCode)) {
			byte b[] = getBytes(1);
			gbb.put(b);
		}
				
		if(!gbb.isEmpty()) {
			gbb.trimToSize();
		}
		return gbb;
	}
	
	public boolean lookBitsAhead(String s) throws Exception {
		String bitsAhead = this.getBits(s.length(), false);
		return s.equals(bitsAhead);
	}	
	
	public GrowableByteBuffer getTail() throws Exception {
		GrowableByteBuffer gbb = new GrowableByteBuffer();
		int total = this.buffer.getLength()/10;
		byte[] bytes = new byte[total];
		while(!this.isEOF()) {
			int t = this.read(bytes,0,total);
			gbb.put(bytes,t);
		}
		gbb.trimToSize();		
		return gbb;
	}
	
	public ContentType getType() {
		return this.buffer.getContentType();
	}
	
	public void reset() throws Exception {
		this.indexInBits = 0;
		this.seek(0);
	}
	
	public void close() throws Exception {
		this.buffer = null;
	}
	
	public String getInputName() {
		return this.buffer.getName();
	}
	
	protected SharedBuffer getBuffer() {
		return this.buffer;
	}

}
