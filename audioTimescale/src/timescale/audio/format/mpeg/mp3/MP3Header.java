/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (20/07/2005)
 */

package timescale.audio.format.mpeg.mp3;

import util.data.GrowableByteBuffer;
import util.functions.Convert;
import timescale.audio.format.generalFormat.BorrowBytesHandler;
import timescale.audio.format.generalFormat.FrameInvalidException;
import timescale.audio.format.mpeg.MPEGBCHeader;


public class MP3Header extends MPEGBCHeader {

	public MP3Header(String data) throws FrameInvalidException {
		super (data);		
	}
	
	private MP3Header (MPEGBCHeader header, BorrowBytesHandler borrowBytesHandler) {
		super (header);
		this.borrowBytesHandler = borrowBytesHandler;
	}
	
	public String toString () {
		StringBuffer value = new StringBuffer("");
		value.append(super.toString());
		value.append(this.getSideInformation().toString());
		return value.toString();
	}
	
	private MP3SideInformation getSideInformation() {
		return ((MP3BorrowBytesHandler) this.borrowBytesHandler).getSideInformation();
	}

	/**
	 * Retorna bytes que representa objeto.
	 * @return bytes que representa objeto
	 */	
	public byte[] toBytes () {
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();
		byte[] bytesSuper = Convert.bitsToBytes(super.toString());
		bytesFrame.put(bytesSuper);
		byte[] bytesSideInformation = this.getSideInformation().toBytes();
		bytesFrame.put(bytesSideInformation);
		bytesFrame.trimToSize();
		return bytesFrame.array();
	}
	
	public Object clone() {
		MPEGBCHeader header = (MPEGBCHeader) super.clone();
		BorrowBytesHandler borrowBytesHandlerClone = (BorrowBytesHandler) this.borrowBytesHandler.clone();  
		return new MP3Header (header, borrowBytesHandlerClone);
	}	
}
