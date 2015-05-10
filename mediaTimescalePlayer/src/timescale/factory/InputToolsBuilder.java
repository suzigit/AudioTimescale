package timescale.factory;

import timescale.util.constants.Constants;
import timescale.util.constants.IOConstants;
import util.data.ContentType;
import util.data.SharedBuffer;
import util.functions.Functions;
import util.io.FileInputTools;
import util.io.InputTools;
import util.io.RemoteFileInputTools;

public class InputToolsBuilder {

	/**
	 * Recupera InputTools como uma thread
	 */
    public static InputTools getInputTools (String inputFile, int inputType)
    		throws Exception {
    	return getInputTools(inputFile, inputType, 
    			Constants.SHARED_BUFFER_LENGTH,
    			Constants.RESERVED_BUFFER_LENGTH, 
    			IOConstants.BUFFER_INPUT_FILE_LENGTH); 	
	}	
	
	/**
	 * Recupera InputTools como uma thread
	 */
    public static InputTools getInputTools (String inputFile, int inputType,
    		int bufferLength, int reservedBufferLength, 
			int bufferInputFileLength) throws Exception {
    	
    	//Cria buffer para dados
    	FileInputTools inputTools = null;
    	SharedBuffer buffer = new SharedBuffer(inputFile, 
    			bufferLength, reservedBufferLength);    	
		String extension = Functions.getExtension(buffer.getName());
		ContentType contentType = ContentType.createByExtension(extension);
		buffer.setContentType(contentType);
		
		//Determina quantidade de dados a ser lida por vez
    	if (inputType==IOConstants.InputSource.LOCAL) {
    		inputTools = new FileInputTools(buffer, bufferInputFileLength);    		
    	}
    	else if (inputType==IOConstants.InputSource.REMOTE) {
    		inputTools = new RemoteFileInputTools (buffer, bufferInputFileLength);
    	}
    	
		Thread t = new Thread(inputTools);
		t.start(); 	
    	return inputTools;
	}
        

}
