/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (19/07/2005)
 */


package timescale.audio.format.mpeg;

import util.functions.Convert;
import timescale.audio.format.generalFormat.BorrowBytesHandler;
import timescale.audio.format.generalFormat.Header;
import timescale.audio.util.constants.GeneralFormatConstants;
import timescale.audio.util.constants.MPEGConstants;


public abstract class MPEGHeaderA extends Header  {
	
	protected char id;
	protected String layer;
	protected char protectionAbsent;
	protected char privateBit;
	protected char copyright;
	protected char original;
	protected byte[] errorCheck;
	
	public MPEGHeaderA () {
	}
	
	public MPEGHeaderA (char id, String layer, char protectionAbsent,String samplingFrequency,
			char privateBit, char copyright, char original, byte[] errorCheck, BorrowBytesHandler borrowBytesHandler,
			GeneralFormatConstants constants) {
		this.id = id;
		this.layer = layer;
		this.protectionAbsent = protectionAbsent;
		this.samplingFrequency = samplingFrequency;
		this.privateBit = privateBit;
		this.copyright = copyright;
		this.original = original;
		this.setErrorCheck(errorCheck);
		this.setBorrowBytes(borrowBytesHandler);
		this.constants = constants;
	}
	
	
	protected char getBinaryId() {
		return this.id;
	}
	
	/**
	 * Retorna bytes de tratamento de erro do frame.
	 * @return bytes de tratamento de erro do frame
	 */
	public byte[] getErrorCheck () {
		return this.errorCheck;
	}
	
	/**
	 * Modifica bytes de tratamento de erro do frame.
	 * @param value valor a ser atribuido ao tratamento de erro  
	 */
	public void setErrorCheck (byte[] value) {
		this.errorCheck = value;
	}
	
	/**
	 * Retorna o id do frame. 
	 * Este atributo indica se é um frame MPEG-1 ou MPEG-2. 
	 *
	 * @return id do frame.
	 */
	public int getId() {
		return Convert.bitsToInt(this.getBinaryId()+"");
	}


	protected char getBinaryProtectionBit() {
		return this.protectionAbsent; 
	}
	
	protected boolean getProtectionBit() {
		return Convert.bitToBoolean(this.getBinaryProtectionBit()); 
	}
	
	protected void setProtectionBit(boolean value) {
		this.protectionAbsent = Convert.booleanToBit(value);
	}
	
	/**
	 * Retorna se o frame contem campos de verificacao de erros.
	 * @return true se e somente se o frame contem campos de verificacao de erros. 
	 */
	public boolean isErrorCheckEnable() {
		return !(this.getProtectionBit());
	}
	
	/**
	 * Modifica o campo que indica se frame contem campos de verificacao de erros.
	 * @param value indica se deve habilitar (true) ou desabilitar (false) o campo 
	 */
	public void enableErrorCheck (boolean value) {
		this.setProtectionBit(!value);
	}

	protected char getBinaryPrivateBit() {
		return this.privateBit; 
	}

	protected char getBinaryCopyright() {
		return this.copyright; 
	}

	protected char getBinaryOriginal() {
		return this.original; 
	}

	/**
	 * Retorna tamanho do campo verificador de erros do frame.
	 * @return tamanho do campo verificador de erros do frame
	 */
	public int lengthErrorCheckInBytes () {
		int lengthErrorCheck = 0;
		if (this.isErrorCheckEnable()) {
			lengthErrorCheck = getMPEGConstants().ERROR_CHECK_LENGTH/8;
		}			
		return lengthErrorCheck;		
	}
	
	public MPEGConstants getMPEGConstants () {
		return (MPEGConstants) this.constants;
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
	public abstract Object clone();
}
