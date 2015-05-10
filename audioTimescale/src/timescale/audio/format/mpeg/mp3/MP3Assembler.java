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

import timescale.audio.format.generalFormat.Frame;
import timescale.audio.format.generalFormat.DataField;
import timescale.audio.format.mpeg.MPEGBCHeader;
import timescale.audio.format.mpeg.MPEGBCAssembler;
import timescale.audio.util.constants.MP3Constants;
import util.functions.Functions;
import util.io.InputTools;

/**
 * Essa classe extrai frames MP3 a partir de um subsistema de entrada.
 */
public class MP3Assembler extends MPEGBCAssembler {

	/** 
	 * Controi instancia da classe.
	 * @param inputTools fonte de dados de entrada
	 */	
	public MP3Assembler (InputTools inputTools) throws Exception {
		super (inputTools);
		this.constants = MP3Constants.getInstance();
	}
	
	/**
	 * Cria e retorna um novo quadro. 
	 * @return um novo quadro
	 */		
	protected Frame createFrame(InputTools io) throws Exception {

		int indexInitialDataInFrame = indexInData; 
		
		//******************* Cria o Header ***********************************
		String headerData = io.getBits(constants.HEADER_LENGTH);
		MP3Header header = new MP3Header(headerData);	
		MP3Constants constants = MP3Constants.getInstance(header.getId());
		indexInData += constants.HEADER_LENGTH;
		 
		//Cria o ErrorCheck
		byte[] errorCheck = null;
		if (header.lengthErrorCheckInBytes()>0) {
			errorCheck = io.getBytes((constants.ERROR_CHECK_LENGTH)/8);
			indexInData += constants.ERROR_CHECK_LENGTH;
			header.setErrorCheck(errorCheck);
		}
		
		//Cria SideInformation
		String sSideInfo = io.getBits(this.lengthSideInformationInBytes(constants, header)*8);
		MP3SideInformation sideInfo = new MP3SideInformation(sSideInfo, header.getNumberOfChannels(), constants);
		indexInData += this.lengthSideInformationInBytes(constants, header)*8;
		
		//Cria BorrowBytes
		header.setBorrowBytes(new MP3BorrowBytesHandler(sideInfo));
		
		//******************* Cria PhysicalData ***********************************
		int indexFinalDataInFrame = indexInitialDataInFrame + header.lengthFrameInBytes()*8;
		int legthPhysicalDataInBits = indexFinalDataInFrame-indexInData;
		
		byte[] bytesPhysicalData = new byte[legthPhysicalDataInBits/8];
		int length = io.read(bytesPhysicalData, 0, bytesPhysicalData.length);
		if (length!=bytesPhysicalData.length) {
			bytesPhysicalData = Functions.copyParameters(bytesPhysicalData,length);
		}
		
		DataField physicalData = new DataField(bytesPhysicalData);
		indexInData = indexFinalDataInFrame;
		
		//******** Cria o frame ************
		Frame frame = new Frame (header, physicalData);				
		
		return frame;
	}
	
	/**
	 * Retorna tamanho do side information do frame MP3.
	 * @return tamanho do side information do frame MP3
	 */
	private int lengthSideInformationInBytes (MP3Constants constants, MPEGBCHeader header) {
		int lengthSideInformation = constants.SIDE_INFORMATION_LENGTH_NOT_SINGLE;
		if (header.getNumberOfChannels() == 1) {
			lengthSideInformation = constants.SIDE_INFORMATION_LENGTH_SINGLE;
		}			
		return lengthSideInformation/8;		
	}	
}
