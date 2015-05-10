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

import timescale.audio.assembler.GenericAudioAssembler;
import timescale.audio.format.generalFormat.Frame;
import timescale.audio.format.generalFormat.DataField;
import timescale.audio.util.constants.AACADTSConstants;
import util.data.ContentType;
import util.functions.Functions;
import util.io.InputTools;


public class AACAssembler extends GenericAudioAssembler {
	
	public AACAssembler (InputTools inputTools) throws Exception {
		super (inputTools);
		this.constants = AACADTSConstants.getInstance();
	}
	
	public static ContentType getStaticType() {
	    return new ContentType(ContentType.ID_MPEG_ADTS_AAC);
	}	
	
	protected Frame createFrame(InputTools io) throws Exception {
		int indexInitialDataInFrame = indexInData;
		
		//******************* Cria o Header ***********************************
		String headerData = io.getBits(constants.HEADER_LENGTH);
		AACHeader header = new AACHeader(headerData);
		indexInData += constants.HEADER_LENGTH;
		
		//Cria o ErrorCheck
		byte[] errorCheck = null;
		if (header.lengthErrorCheckInBytes()>0) {
			AACADTSConstants constants = (AACADTSConstants) AACADTSConstants.getInstance(); 
			errorCheck = io.getBytes((constants.ERROR_CHECK_LENGTH)/8);
			indexInData += constants.ERROR_CHECK_LENGTH;
			header.setErrorCheck(errorCheck);
		}
		
		//Cria BorrowBytes
		header.setBorrowBytes(new AACBorrowBytesHandler());
		
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
