/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (19/09/2005)
 */

package timescale.remoteApplication;

import util.data.SharedBuffer;

import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceTransferHandler;

import timescale.util.constants.Constants;
import timescale.util.constants.IOConstants;
import util.data.ContentType;
import util.io.ByteOutputTools;


public class HandlerReceivedData implements SourceTransferHandler { 
	
	private ByteOutputTools outputTools;
	private int bufferLength;
	private boolean callPlayer;
	private TimescaleCaller timescaleCaller;
	

	public HandlerReceivedData(int bufferLength, ContentType contentType) throws Exception {
		this.bufferLength = bufferLength;
		SharedBuffer buffer = new SharedBuffer(IOConstants.ORIGINAL_STREAM_READER_PLAYER_NAME, 
				Constants.SHARED_BUFFER_LENGTH, Constants.RESERVED_BUFFER_LENGTH);
		this.outputTools = new ByteOutputTools(buffer);	
		callPlayer = true;
		this.outputTools.setContentType(contentType);
		timescaleCaller = new TimescaleCaller(this.outputTools.getInputTools());
	}
	
	public synchronized void transferData(PushSourceStream pss) {	
		byte[] bytes = new byte[bufferLength];
		try {
			int bytesRead = pss.read(bytes, 0, bytes.length);
			this.outputTools.output(bytes, bytesRead);
			if (callPlayer) {
				callPlayer=false;
				timescaleCaller.start();
			}
		}
		catch (Exception e) {
			System.err.println("ERRO HandlerReceiverData: " + e.getMessage());
		}	
	}
	
}
