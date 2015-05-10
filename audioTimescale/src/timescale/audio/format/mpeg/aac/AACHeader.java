/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (19/07/2005)
 */

package timescale.audio.format.mpeg.aac;

import util.functions.Convert;
import timescale.audio.format.generalFormat.BorrowBytesHandler;
import timescale.audio.format.mpeg.MPEGHeaderA;
import timescale.audio.util.constants.AACADTSConstants;


public class AACHeader extends MPEGHeaderA {

	private String profile;
	private String channelConfiguration;
	private char home;
	
	private char copyrightIdentificationStart;
	
	//Tamanho total do quadro
	private String frameLength;
	
	//Numero de palavras no codificador apos codificacao do primeiro RawDataBlock
	//Se 7FF nao aplicavel, pois eh VBR!
	//TODO: eh o que esta acontecendo. Testar com CBR
	private String adtsBufferFullness;
	
	//Indica numero de blocos no quadro, comecando de zero.
	private String numberOfRawDataBlocks;
	
	public AACHeader(String data) {
		System.out.println("header ="+ data);
		this.id = data.charAt(12);
		this.layer = data.substring(13,15);
		this.protectionAbsent = data.charAt(15);
		this.profile = data.substring(16,18);
		this.samplingFrequency = data.substring(18,22);
		this.privateBit = data.charAt(22);
		this.channelConfiguration = data.substring(23,26);
		this.original = data.charAt(26);
		this.home = data.charAt(27);
		
		this.copyright = data.charAt(28);
		this.copyrightIdentificationStart = data.charAt(29);
		this.frameLength = data.substring(30,43);
		this.adtsBufferFullness = data.substring(43,54);
		this.numberOfRawDataBlocks = data.substring(54,56);
		this.constants = AACADTSConstants.getInstance();
		
		if (!this.adtsBufferFullness.equals(((AACADTSConstants)constants).BUFFER_FULLNESS_NOT_APLICABLE)) {
			throw new IllegalArgumentException("Existe reservatório de bytes no AAC");
		}
		
		if (!this.numberOfRawDataBlocks.equals(((AACADTSConstants)constants).ONE_RAW_DATA_BLOCK)) {
			throw new IllegalArgumentException("Mais de um data block por quadro:" + this.numberOfRawDataBlocks);
		}		
		
	}
	
	public AACHeader(char id, String layer, char protectionAbsent, String profile, 
			String samplingFrequency, char privateBit, String channelConfiguration,
			char original, char home, char copyright, char copyrightIdentificationStart,
			String frameLength, String adtsBufferFullness, String numberOfRawDataBlocks,
			byte[] errorCheck, BorrowBytesHandler borrowBytesHandler) {
		super (id,layer,protectionAbsent,samplingFrequency,privateBit,copyright,original,errorCheck, borrowBytesHandler, AACADTSConstants.getInstance());
		this.profile = profile;
		this.channelConfiguration = channelConfiguration;
		this.home = home;
		this.copyrightIdentificationStart = copyrightIdentificationStart;
		this.frameLength = frameLength;
		this.adtsBufferFullness = adtsBufferFullness;
		this.numberOfRawDataBlocks = numberOfRawDataBlocks;
	}
	

	/**
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */
	public String toString () {
		StringBuffer value = new StringBuffer("");
		
		value.append(constants.SYNC_WORD);
		value.append(this.id);
		value.append(this.layer);
		value.append(this.protectionAbsent);
		value.append(this.profile);		
		value.append(this.samplingFrequency);
		value.append(this.privateBit);
		value.append(this.channelConfiguration);
		value.append(this.original);
		value.append(this.home);
		value.append(this.copyright);
		value.append(this.copyrightIdentificationStart);
		value.append(this.frameLength);
		value.append(this.adtsBufferFullness);
		value.append(this.numberOfRawDataBlocks);
						
		if (this.getErrorCheck()!=null) {
			value.append(this.getErrorCheck().toString());
		}
		return  value.toString(); 
	}	
	
	/**
	 * Retorna clone do objeto.
	 * @return clone do objeto
	 */    
	public Object clone() {
		AACHeader h = new AACHeader (this.id, this.layer, this.protectionAbsent, this.profile,
				this.samplingFrequency, this.privateBit, this.channelConfiguration,
				this.original, this.home, this.copyright, this.copyrightIdentificationStart,
				this.frameLength, this.adtsBufferFullness, this.numberOfRawDataBlocks,
				this.errorCheck,this.borrowBytesHandler);
	    return h;
	}	
	
	/**
	 * Retorna tamanho do frame. 
	 * @return tamanho do frame
	 */
	public int lengthFrameInBytes () {
		int length = Convert.bitsToInt(this.frameLength);
		return length;
	}	
}
