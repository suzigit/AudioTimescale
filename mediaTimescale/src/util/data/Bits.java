/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (05/05/2004)
 */

package util.data;

import util.functions.Convert;



/**
 * Essa classe prove acesso individual a bits.
 */
public class Bits {
	
	private byte[] bytes;
	
	/** 
	 * Controi instancia da classe.
	 * @param bytes conjunto de bytes cujos bits devem ser acessados individualmente
	 */			
	public Bits (byte[] bytes) {
		this.bytes = bytes;		
	}
	
	public Bits (byte b) {
		this.bytes = new byte[1];
		this.bytes[0] = b;
	}

	//É bom testar melhor antes de utilizar esse construtor!
	/**
	 * Controi instancia da classe.
	 * @param s string cujos bits devem ser acessados individualmente
	 */
	public Bits (String s) {
		int length = s.length()/8;
		int indexInData = 0;
		this.bytes = new byte[length];
		for (int j=0; j<length; j++) {
			byte b = (byte) Convert.bitsToByte(s.substring(indexInData, indexInData+8));	  		
			indexInData += 8; 
			bytes[j] = b;
		}
	}
	

	/**
	 * Retorna numero de bits possiveis de serem acessados.
	 * @return numero de bits possiveis de serem acessados.
	 */
	public int length () {
		 return bytes.length * 8;
	}
		
	/**
	 * Retorna bit na posicao passada como argumento. 
	 * @param index posicao do bit a ser retornado
	 * @return caracter que representa bit encontrado.
	 */
	public char bitAt (int index) {
		int indexInBytes = index/8;
		String bitsFormatInByte = Convert.byteToString(bytes[indexInBytes]); 
		return bitsFormatInByte.charAt(index%8); 
	}
	
	/**
	 * Retorna byte na posicao passada como argumento. 
	 * @param indexInBytes posicao do byte a ser retornado
	 * @return byte encontrado
	 */	
	public byte byteAt(int indexInBytes) {
		return this.bytes[indexInBytes];
	}
	
	/**
	 * Retorna conjunto de bits a partir dos parametros especificados. 
	 * @param index posicao do primeiro bit a ser retornado
	 * @param length tamanho do conjunto de bits a ser retornado 
	 * @return string contendo caracteres que representam bits encontrados
	 */
	public String toString (int index, int length) {
		char charFormat[] = new char[length];
		for (int i=0; i < length; i++) {
			charFormat[i] = this.bitAt(index+i);
		}
		return new String(charFormat);
	}

	/**
	 * Retorna todo conjunto de bits. 
	 * @return string contendo caracteres que representam todos os bits armazenados.
	 */
	public String toString () {
		return toString (0,this.length());
	}

	public String toStringWithSpace () {
		StringBuffer sFormat = new StringBuffer();
		for (int i=0; i < this.length(); i++) {
			sFormat.append(this.bitAt(i));
			if (i%8==7){
				sFormat.append(' ');
			}
		}
		return sFormat.toString();
	}
	
	
	/**
	 * Retorna conjunto de bytes contendo todos os bits.
	 * @return conjunto de bytes contendo todos os bits.
	 */
	public byte[] getBytes() {
		return this.bytes;
	}
	
}
