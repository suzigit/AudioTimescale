/*
 * Created on Aug 2, 2004
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;

import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * The information in this extension do not affect the decoding proccess. It denotes the caracteristics
 * of the video stream concerning to video format (PAL, NTSC etc) and color coefficients.
 * @author Sergio Cavendish
 */
public class SequenceDisplayExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer seqDisplayExtData;
/*	private String videoFormat; // length = 3 bits
	private char colourDescription; // length = 1 bit. Indicates if the next 3 fields exist.
	private String colourPrimaries = ""; // length = 8 bits
	private String tranferCharacteristics = ""; // length = 8 bits
	private String matrixCoeficients = ""; // length = 8 bits
	private String displayHorizontalSize; // length = 14 bits
	private char markerBit; // length = 1 bit
	private String displayVerticalSize; // length = 14 bits
	private String stuffing; // variable length
*/

	/**
	 * Initiate the object
	 * @param data -  String which contains the data of this object
	 */
	public SequenceDisplayExtension(IoTools io) throws IOException {
		
		ioTools = io;
		seqDisplayExtData = new GrowableByteBuffer(4);
		seqDisplayExtData.put(ioTools.nextStartCode());
		seqDisplayExtData.trimToSize();
		
/*		this.videoFormat = data.toString(3);
		this.colourDescription = data.nextBit();
		
		if(colourDescription == '1'){
			this.colourPrimaries = data.toString(8);
			this.tranferCharacteristics = data.toString(8);
			this.matrixCoeficients = data.toString(8);
		}
		
		this.displayHorizontalSize = data.toString(14);
		this.markerBit = data.nextBit();
		this.displayVerticalSize = data.toString(14);
		
		this.stuffing = data.nextStartCode();
*/
	}
	
	/**
 * 
 */
public SequenceDisplayExtension() {
	
}

	public byte[] getObject(){
		seqDisplayExtData.trimToSize();
		return seqDisplayExtData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		SequenceDisplayExtension result = new SequenceDisplayExtension();
		GrowableByteBuffer sDisplayExtData = 
			(GrowableByteBuffer)seqDisplayExtData.clone();
		result.setSequenceDisplayExtensionData(sDisplayExtData);
		return result;
	}

	/**
	 * @param value
	 */
	private void setSequenceDisplayExtensionData(GrowableByteBuffer value) {
		this.seqDisplayExtData = value;
	}
}
