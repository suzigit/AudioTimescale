
package timescale.audio.format.ac3;

import timescale.audio.assembler.GenericAudioAssembler;
import timescale.audio.format.generalFormat.Frame;
import timescale.audio.format.generalFormat.DataField;
import timescale.audio.format.generalFormat.ZeroBorrowBytesHandler;
import timescale.audio.util.constants.AC3Constants;
import util.data.ContentType;
import util.functions.Functions;
import util.io.InputTools;

/**
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (12/07/2005)
 */
public class AC3Assembler extends GenericAudioAssembler {
	
	/** 
	 * Controi instancia da classe.
	 * @param inputTools fonte de dados de entrada
	 */
	public AC3Assembler (InputTools inputTools) throws Exception {
	    super (inputTools);
		this.constants = AC3Constants.getInstance();  
	}
	

	/**
	 * Cria e retorna um novo quadro. 
	 * @return um novo quadro
	 */	
	protected Frame createFrame(InputTools io) throws Exception {
		
	    int indexInitialDataInFrame = indexInData;
	    
	    //******************* Cria o Header ***********************************   
	    String headerData = io.getBits(constants.HEADER_LENGTH);
		AC3Header header = new AC3Header(headerData);
		indexInData += constants.HEADER_LENGTH;
		
		//Cria BorrowBytes
		header.setBorrowBytes(new ZeroBorrowBytesHandler());
		
		//******************* Cria PhysicalData ***********************************
		int indexFinalDataInFrame = indexInitialDataInFrame + header.lengthFrameInBytes()*8;		
		int lengthPhysicalDataInBits = indexFinalDataInFrame-indexInData;
		
		byte[] bytesPhysicalData = new byte[lengthPhysicalDataInBits/8];
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
	 * Retorna formato de dados do fluxo.
	 * @return formato de dados do fluxo
	 */		
	public static ContentType getStaticType() {
	    return new ContentType(ContentType.ID_AC3);
	}
	

}
