/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/03/2005)
 */

package timescale.audio.format.generalFormat;

import util.functions.Convert;
import util.functions.Functions;

/**
 * Essa classe representa as amostras de audio de um frame de audio.
 */
public class DataField implements Cloneable {
	
	private byte[] data;
	
	/** 
	 * Controi instancia da classe.
	 * @param d dado fisico do quadro 
	 */			
	public DataField (byte[] d) {
		this.data = d;	
	}

	/** 
	 * Recupera dado fisico do quadro.
	 * @return dado fisico do quadro 
	 */		
	public byte[] getData() {		
		return this.data;
	}
	
	/** 
	 * Recupera substring dado fisico do quadro.
	 * @param index index inicial do substring
	 * @return substring dado fisico do quadro 
	 */		
	public byte[] substring(int index) {
		return this.substring(index, this.lengthInBytes());
	}

	/** 
	 * Recupera substring dado fisico do quadro.
	 * @param begIndexInBytes index inicial do substring
	 * @param endIndexInBytes index final do substring 
	 * @return substring dado fisico do quadro 
	 */			
	public byte[] substring(int begIndexInBytes, int endIndexInBytes) {
		byte[] value = new byte[endIndexInBytes - begIndexInBytes];

		for (int i=0; i<value.length; i++) {
			value[i] = this.data[i+begIndexInBytes];
		}				
		return value;
	}	

	/** 
	 * Retorna tamanho dodado fisico do quadro.
	 * @return tamanho dodado fisico do quadro 
	 */		
	public int lengthInBytes() {
		int length = 0;
		if (this.data!=null) {
			length = this.data.length; 
		}
		return length;
	}

	/**
	 * Modifica o dado interno para:
	 * 1- Ficar como está até beginIndexInBits
	 * 2- Acrescentar lengthInBytes bytes de newData
	 * 3- Ficar como esta depois de beginIndexInBytes + lengthInBits.
	 * @param newData dado a ser utilizado na modificacao
	 * @param beginIndexInBytes index inicial a ser modificado
	 * @param lengthInBytes numero de bytes a ser modificado
	 */
	public void setData(byte[] newData, int beginIndexInBytes, int lengthInBytes) {
		int endIndexInBits = beginIndexInBytes + lengthInBytes;		
		int i=0;
		for (int indexInBytes = beginIndexInBytes; 
			indexInBytes < endIndexInBits;
			indexInBytes++) {
			this.data[indexInBytes] = newData[i]; 
			i++;
		}		
	}

	
	/**
	 * Retorna bytes que representam objeto.
	 * @return bytes que representam objeto
	 */	
	public byte[] toBytes() {
		return this.getData();
	}
	
	/**
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */	
	public String toString() {
		return Convert.bytesToString(this.getData());
	}

	/**
	 * Retorna clone do objeto.
	 * @return clone do objeto
	 */		
	public Object clone() {
		byte[] dataClone = Functions.copyParameters(this.getData());
		return new DataField(dataClone);	
	}
	
}
