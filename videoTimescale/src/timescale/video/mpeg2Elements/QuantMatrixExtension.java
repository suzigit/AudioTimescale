/*
 * Created on 03/08/2004
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;

import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * @author Sergio Cavendish
 */
public class QuantMatrixExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer quantData;
/*	private char loadIntraQuantiserMatrix; // length = 1 coded bit
	private String intraQuantiserMatrix = ""; // length = 8 * 64 coded bits
	private char loadNonIntraQuantiserMatrix; // length = 1 coded bit
	private String nonIntraQuantiserMatrix = ""; // length = 8 * 64 coded bits
	private char loadChromaIntraQuantiserMatrix; // length = 1 coded bit
	private String chromaIntraQuantiserMatrix = ""; // length = 8 * 64 coded bits
	private char loadChromaNonIntraQuantiserMatrix; // length = 1 coded bit
	private String chromaNonIntraQuantiserMatrix = ""; // length = 8 * 64 coded bits
	private String stuffing;
*/

	public QuantMatrixExtension(IoTools io) throws IOException  {
		
		ioTools = io;
		quantData = new GrowableByteBuffer(4);
		quantData.put(ioTools.nextStartCode());
		quantData.trimToSize();
		
/*		this.loadIntraQuantiserMatrix = data.nextBit();
		if(loadIntraQuantiserMatrix == '1'){
			this.intraQuantiserMatrix = data.toString(8 * 64);
		}
		
		this.loadNonIntraQuantiserMatrix = data.nextBit();
		if(loadNonIntraQuantiserMatrix == '1'){
			this.nonIntraQuantiserMatrix = data.toString(8 * 64);
		}
		
		this.loadChromaIntraQuantiserMatrix = data.nextBit();
		if(loadChromaIntraQuantiserMatrix == '1'){
			this.chromaIntraQuantiserMatrix = data.toString(8 * 64);
		}
		
		this.loadChromaNonIntraQuantiserMatrix = data.nextBit();
		if(loadChromaNonIntraQuantiserMatrix == '1'){
			this.chromaNonIntraQuantiserMatrix = data.toString(8 * 64);
		}

		this.stuffing = data.nextStartCode();
*/
	}
	
	/**
 * 
 */
public QuantMatrixExtension() {
	
}

	public byte[] getObject(){
		quantData.trimToSize();
		return quantData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		QuantMatrixExtension result = new QuantMatrixExtension();
		GrowableByteBuffer qData = (GrowableByteBuffer)quantData.clone();
		result.setQuantData(qData);
		return result;
	}

	/**
	 * @param value
	 */
	private void setQuantData(GrowableByteBuffer value) {
		this.quantData = value;
	}

	/**
	 * @return
	 */
/*	public int length() {
		return this.toString().length();
	}
*/
}
