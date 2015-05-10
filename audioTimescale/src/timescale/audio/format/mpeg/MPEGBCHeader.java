/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/03/2005)
 */

package timescale.audio.format.mpeg;

import timescale.audio.format.generalFormat.BorrowBytesHandler;
import timescale.audio.format.generalFormat.FrameInvalidException;
import timescale.audio.util.constants.GeneralFormatConstants;
import timescale.audio.util.constants.MPEGBCConstants;
import util.functions.Convert;
import util.functions.Functions;

/**
 * Essa classe representa o header de um frame MPEG Backword Compatible.
 */
public class MPEGBCHeader extends MPEGHeaderA  {
	
	private String bitRate;
	private char paddingBit;
	private String mode;
	private String modeExtension;
	protected String emphasis;

	/** 
	 * Controi instancia da classe.
	 * @param data dados necessários para montar o header
	 */	
	public MPEGBCHeader(String data) throws FrameInvalidException {
		//0,11 -> Synch
		this.id = data.charAt(MPEGBCConstants.INDEX_OF_ID_MPEG);
		this.layer = data.substring(MPEGBCConstants.INDEX_LAYER_BEGGIN,MPEGBCConstants.INDEX_LAYER_END);
		
		this.constants = MPEGBCConstants.getInstance(this.getId(), layer);
		
		//13,15
		if (!data.substring(0,13).equals(constants.SYNC_WORD+this.getBinaryId())) {
			throw new FrameInvalidException(FrameInvalidException.INVALID_HEADER_EXCEPTION);
		}
		this.protectionAbsent = data.charAt(15);
		this.bitRate = data.substring(16,20);
		this.samplingFrequency = data.substring(20,22);
		this.paddingBit = data.charAt(22);
		this.privateBit = data.charAt(23);
		this.mode = data.substring(24,26);
		this.modeExtension = data.substring(26,28);
		this.copyright = data.charAt(28);
		this.original = data.charAt(29);
		this.emphasis = data.substring(30,32);
	}
		
	private MPEGBCHeader(char id, String layer, char protectionAbsent, String bitRate, String samplingFrequency,
			char paddingBit, char privateBit, String mode, String modeExtension,
			char copyright, char original, String emphasis, byte[] errorCheck, 
			GeneralFormatConstants constants, BorrowBytesHandler borrowBytesHandler) {
		super (id,layer,protectionAbsent,samplingFrequency,privateBit,copyright,original,errorCheck,borrowBytesHandler,constants);
		this.bitRate = bitRate;
		this.paddingBit = paddingBit;
		this.mode = mode;
		this.modeExtension = modeExtension;
		this.emphasis = emphasis;
	}

	
	public MPEGBCHeader (MPEGBCHeader header) {
		this (header.id, header.layer, header.protectionAbsent, header.bitRate, header.samplingFrequency,
				header.paddingBit, header.privateBit, header.mode, header.modeExtension,
				header.copyright, header.original, header.emphasis, header.errorCheck, 
				header.constants, header.borrowBytesHandler);
	}	
	
	protected String getBinaryEmphasis() {
		return this.emphasis; 
	}	
	
	/**
	 * Retorna BitRate do frame em formato binário.
	 * @return BitRate do frame em formato binário
	 */
	public String getBinaryBitRate() {
		return this.bitRate; 
	}
	
	/**
	 * Retorna BitRate do frame.
	 * @return inteiro que representa o formato binario da string do BitRate do frame.
	 */
	 public int getBitRate() {
 		int index = Convert.bitsToInt(this.getBinaryBitRate());
 		return Integer.parseInt((String)(getMPEGBCConstants().BIT_RATE_TABLE.get(index+""))); 
	}

	/**
	 * Modifica BitRate do frame.
	 * @param index inteiro que representa o formato binario da string do novo BitRate do frame
	 */
	public void setBitRate(int index) {
		this.bitRate = Convert.intToBits(index);
		while (this.bitRate.length() < 4) {
			 this.bitRate = "0" + this.bitRate; 	
		}
	}		

	/**
	 * Retorna bit de pad do frame.
	 * @return caracter '1' ou '0' que indica se frame possui byte de pad ou nao.
	 */
	public char getBinaryPaddingBit() {
		return this.paddingBit; 
	}

	/**
	 * Retorna bit de pad do frame.
	 * @return valor true ou false que indica se frame possui byte de pad ou nao
	 */	
	public boolean getPaddingBit() {
		return Convert.bitToBoolean(this.getBinaryPaddingBit()); 
	}

	private String getBinaryMode() {
		return this.mode; 
	}

	/**
	 * Retorna inteiro que representa mode do frame.
	 * @return inteiro que representa mode do frame
	 */	
	private int getMode() {
		return Integer.parseInt((String)(getMPEGBCConstants().modeTable.get(this.getBinaryMode())));
	}

	/**
	 * Retorna numero de canais de audio do frame. 
	 * @return numero de canais de audio do frame.
	 */	
	public int getNumberOfChannels() {
		int numberOfChannels = 1;
 		MPEGBCConstants c = (MPEGBCConstants) this.constants;
		if (this.getMode()!=c.SINGLE_CHANNEL_MODE) {
			numberOfChannels = 2; 
		}	
		return numberOfChannels;	
	}
	
	private String getBinaryModeExtension() {
		return this.modeExtension; 
	}
   
	/**
	 * Retorna tamanho do frame. 
	 * @return tamanho do frame
	 */
	public int lengthFrameInBytes () {
		int padding = 0;
		if (this.getPaddingBit()) {
			padding += 1;
		}
		int factor = constants.NUMBER_OF_SAMPLES_PER_FRAME/getMPEGBCConstants().BITS_PER_SLOT;
		int div = (int) (factor*this.getBitRate()/this.getSamplingFrequency());
		int lengthData = (div + padding);
		return lengthData;
	}
	
	public MPEGBCConstants getMPEGBCConstants() {
		return (MPEGBCConstants) this.constants;
	}

 	/**
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */
	public String toString () {
		StringBuffer value = new StringBuffer("");
		
		value.append(constants.SYNC_WORD);
		value.append(this.getBinaryId());
		value.append(this.layer);
		value.append(this.getBinaryProtectionBit());
		value.append(this.getBinaryBitRate());
		value.append(this.samplingFrequency);
		value.append(this.getBinaryPaddingBit());
		value.append(this.getBinaryPrivateBit());
		value.append(this.getBinaryMode());
		value.append(this.getBinaryModeExtension());
		value.append(this.getBinaryCopyright());
		value.append(this.getBinaryOriginal());
		value.append(this.getBinaryEmphasis());						
		if (this.getErrorCheck()!=null) {
			value.append(this.getErrorCheck().toString());
		}
		return  value.toString(); 
	}
	
	
	/**
	 * Cria clone do objeto.
	 * @return clone do objeto
	 */	
	public Object clone() {
	    byte[] crc = Functions.copyParameters(this.getErrorCheck());
		MPEGBCHeader h = new MPEGBCHeader(this.id, layer, this.protectionAbsent,new String(this.bitRate),
				new String (this.samplingFrequency), this.paddingBit, this.privateBit,
				new String(mode), new String (modeExtension), copyright, original,
				new String (emphasis), crc, constants, this.borrowBytesHandler);
		return h;
	}
}
