/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (29/11/2005)
 */

package timescale.system.metadata;

import util.data.Bits;
import util.data.GrowableByteBuffer;

public class SystemHeader {

	private byte[] bSystemLength;
	private byte[] parameters;
	
	public SystemHeader(byte[] bSystemLength, byte[] parameters) {
		this.bSystemLength = bSystemLength;
		this.parameters = parameters;
	}
	
	public byte[] toBytes() {
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();		
		byte[] startCode = (new Bits(MPEG2SystemConstants.SYSTEM_HEADER_START_CODE)).getBytes();
		bytesFrame.put(startCode);
		bytesFrame.put(bSystemLength);		
		bytesFrame.put(parameters);
		bytesFrame.trimToSize();
		return bytesFrame.array();
	}
}
