/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/03/2005)
 */

package timescale.audio.format.mpeg.mp3;

import util.functions.Convert;
import timescale.audio.util.constants.MP3Constants;

/**
 * Essa classe armazena dados do side information de um frame MP3. 
 */
public class MP3SideInformation implements Cloneable {
	
	private String mainDataBegin;
	
	private boolean validMainDataBegin = true;
	
	private String restOfData;

	private int numberOfChannels;
	private MP3Constants constants;

	
	/** 
	 * Controi instancia da classe.
	 * @param data stream de dados do MP3 que corresponde ao side information
	 * @param numberOfChannels número de canais do frame MP3
	 * @param constants conjunto de constantes da aplicacao
	 */		
	public MP3SideInformation (String data, int numberOfChannels, MP3Constants constants) {

		this.constants = constants;

		int indexInData = 0;
		
		this.numberOfChannels = numberOfChannels;
				
		this.mainDataBegin = data.substring(indexInData,indexInData+constants.MAIN_DATA_BEGIN_LENGTH);
		indexInData = indexInData + constants.MAIN_DATA_BEGIN_LENGTH;
		
		this.restOfData = data.substring(indexInData);
										
	}
	
	private MP3SideInformation (String mainDataBegin, String privateBits, 
			char scfsiBand[][], String sideInfos, int numberOfChannels, MP3Constants constants) {
		this.mainDataBegin = mainDataBegin;
		this.restOfData = sideInfos; 
		this.numberOfChannels = numberOfChannels; 
		this.constants = constants;
	}

	/**
	 * Modifica quantidade de bytes emprestados de frames anteriores.
	 * @param deltaInBytes numero de bytes emprestados a ser adicionado
	 */		
	public void incrementNumberOfBorrowBytes(int deltaInBytes) {
		int n = Convert.bitsToInt(this.mainDataBegin);	
		n += deltaInBytes;	
		this.mainDataBegin = Convert.intToBits(n, constants.MAIN_DATA_BEGIN_LENGTH);						
	}

	/**
	 * Retorna numero de bytes emprestados de frames anteriores.
	 * @return numero de bytes emprestados de frames anteriores.
	 */		
	public int getNumberOfBorrowBytes() {		
		int number = 0;
		if (this.validMainDataBegin) {
			number = this.getMainDataBegin(); 
		}	
		return number;
	}
	
	/**
	 * Retorna numero maximo de bytes que pode pedir emprestado.
	 * @return numero maximo de bytes que pode pedir emprestado
	 */		
	public int maxNumberOfBorrowedBytes () {
		int exp = constants.MAIN_DATA_BEGIN_LENGTH;
		return (int) Math.pow(2,exp)-1;
	}
	
	
	/**
	 * Indica que bytes emprestados de frames anteriores sao invalidos.
	 */		
	public void invalidateBorrowBytes () {
		this.validMainDataBegin = false;
	}
	
	/**
	 * Retorna validade dos bytes emprestados de frames anteriores.
	 * @return validade dos bytes emprestados de frames anteriores
	 */			
	public boolean isValidBorrowBytes() {
		return this.validMainDataBegin;
	}			

	/**
	 * Modifica numero de bytes emprestados para 0.
	 */	
	public void initNumberOfBorrowBytes() {		
			int n = 0;	
			this.mainDataBegin = Convert.intToBits(n, constants.MAIN_DATA_BEGIN_LENGTH);										
	}
	
	private int getMainDataBegin () {
		return Convert.bitsToInt(this.mainDataBegin);	
	}


	/**
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */	
	public String toString() {

		StringBuffer value = new StringBuffer("");
				
		value.append(this.mainDataBegin);
		value.append(this.restOfData);
		return value.toString();  		
	}
	
	/**
	 * Retorna bytes que representa objeto.
	 * @return bytes que representa objeto
	 */	
	public byte[] toBytes() {
		return Convert.bitsToBytes(this.toString());
	}

	/**
	 * Retorna clone do objeto.
	 * @return clone do objeto
	 */		
	public Object clone() {
		
		String sideInfosClone = this.restOfData.toString();

		MP3SideInformation s = new MP3SideInformation(new String(this.mainDataBegin),
				null, null, sideInfosClone,numberOfChannels, constants);
		s.validMainDataBegin = this.validMainDataBegin;
		return s;
	}

}
