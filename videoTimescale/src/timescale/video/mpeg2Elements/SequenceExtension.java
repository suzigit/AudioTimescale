/*
 * Created on 02/08/2004
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;

import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * Representa a estrutura Sequence_Extension, conforme padrão H.262, item 6.2.2.3.
 * @author Sergio Cavendish
 */
public class SequenceExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer seqExtensionBytes;
	private int progressiveSequence, lowDelay;
/*
	/**
	 * profileAndLevelIndication indicates the profile and level to which the bitstream complies.
	 * The first bit is a escape bit. If it is set to zero, then the following bits mean:
	 * bits 6 to 4 - provide profile identification
	 * bits 3 to 0 - provide level identification
	 * 
	 * If escape bit is set to one, the meaning of profileAndLevelIndication follows table 8-4 (H.262)
	 */
/*	private String profileAndLevelIndication; // length = 8 coded bits
	
	/**
	 * When progressiveSequence = 1, there are only progressive frame-pictures.
	 * When progressiveSequence = 0, there could be either frame-pictures of field-pictures and 
	 * progressive or interlaced frames 
	 */
/*	private char progressiveSequence; // length = 1 coded bit
	
	/**
	 * This parameter is used by CodedBlockPattern class. Once the initial focus is 
	 * non-scalable video bitstream, CodedBlockPattern class will not be coded.
	 */
/*	private String chromaFormat; // length = 2 coded bits
	private String horizontalSizeExtension; // length = 2 coded bits
	private String verticalSizeExtension; // length = 2 coded bits
	private String bitRateExtension; // length = 12 coded bits
	private char markerBit; // length = 1 coded bit
	private String vbvBufferSizeExtension; // length = 8 coded bits
	private char lowDelay; // length = 1 coded bit
	private String frameRateExtensionN; // length = 2 coded bits
	private String frameRateExtensionD; // length = 5 coded bits
	private String stuffing; // variable length

//===========================================
*/
	public SequenceExtension(IoTools io) throws IOException {
		
		ioTools = io;
		seqExtensionBytes = new GrowableByteBuffer(4);
		seqExtensionBytes.put(ioTools.getBytes(4));
		seqExtensionBytes.put(ioTools.nextStartCode());
		seqExtensionBytes.trimToSize();
		progressiveSequence = seqExtensionBytes.getBits(44,1);
		lowDelay = seqExtensionBytes.getBits(72, 1);
	}
	
	/**
 * 
 */
public SequenceExtension() {

}

	/**
	 * @return array of bytes of this element.
	 */
	public byte[] getObject(){
		seqExtensionBytes.trimToSize();
		return seqExtensionBytes.array();
	}

	/**
	 * @return progressiveSequence
	 */
	public int getProgressiveSequence() {
		return this.progressiveSequence;
	}
	
	
	/**
	 * @return Returns the lowDelay.
	 */
	public int getLowDelay() {
		return lowDelay;
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		SequenceExtension result = new SequenceExtension();
		GrowableByteBuffer sExtBytes = (GrowableByteBuffer)seqExtensionBytes.clone();
		result.setSequenceExtensionBytes(sExtBytes);
		result.setProgressiveSequence(progressiveSequence);
		result.setLowDelay(lowDelay);
		return result;
	}

	/**
	 * @param value
	 */
	private void setLowDelay(int value) {
		this.lowDelay = value;
		seqExtensionBytes.setBits(72, value, 1);
	}

	/**
	 * @param value
	 */
	private void setProgressiveSequence(int value) {
		this.progressiveSequence = value;
		seqExtensionBytes.setBits(44, value, 1);
	}

	/**
	 * @param value
	 */
	private void setSequenceExtensionBytes(GrowableByteBuffer value) {
		this.seqExtensionBytes = value;
	}
}
