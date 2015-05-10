/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/03/2005)
 */

package timescale.audio.format.mpeg.mp2;

import timescale.audio.format.generalFormat.Frame;
import timescale.audio.format.generalFormat.DataField;
import timescale.audio.format.generalFormat.ZeroBorrowBytesHandler;
import timescale.audio.format.mpeg.MPEGBCHeader;
import timescale.audio.format.mpeg.MPEGBCAssembler;
import timescale.audio.util.constants.MP2Constants;
import util.functions.Functions;
import util.io.InputTools;

/**
 * Essa classe extrai frames MP2 a partir de um subsistema de entrada.
 */
public class MP2Assembler extends MPEGBCAssembler {

	/** 
	 * Controi instancia da classe.
	 * @param inputTools fonte de dados de entrada
	 */
	public MP2Assembler (InputTools inputTools) throws Exception {
		super (inputTools);
		this.constants = MP2Constants.getInstance();
	}
	
	/**
	 * Cria e retorna um novo quadro. 
	 * @return um novo quadro
	 */		
	protected Frame createFrame(InputTools io) throws Exception {

		int indexInitialDataInFrame = indexInData;  
		
		//******************* Cria o Header ***********************************
		String headerData = io.getBits(constants.HEADER_LENGTH);						
		MPEGBCHeader header = new MPEGBCHeader(headerData);	
		MP2Constants constants = MP2Constants.getInstance(header.getId());
		indexInData += constants.HEADER_LENGTH;
		 
		//Cria o ErrorCheck
		byte[] errorCheck = null;
		if (header.lengthErrorCheckInBytes()>0) {
			errorCheck = io.getBytes((constants.ERROR_CHECK_LENGTH)/8);
			indexInData += constants.ERROR_CHECK_LENGTH;
			header.setErrorCheck(errorCheck);
		}		
		
		//Cria BorrowBytes
		header.setBorrowBytes(new ZeroBorrowBytesHandler());
		
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
}
