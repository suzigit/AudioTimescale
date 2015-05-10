/*
 * Created on 04/08/2004
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;

import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * @author Sergio Cavendish
 */
public class PictureCodingExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer picCodExtData;
	private int repeatFirstField;
	private int topFieldFirst;
	private int pictureStructure;
	private int progressiveFrame;

	public PictureCodingExtension(IoTools io) throws IOException {
		
		ioTools = io;
		picCodExtData = new GrowableByteBuffer(4);
		picCodExtData.put(ioTools.getBytes(4));
		picCodExtData.put(ioTools.nextStartCode());
		picCodExtData.trimToSize();
		repeatFirstField = picCodExtData.getBits(62, 1);
		topFieldFirst = picCodExtData.getBits(56, 1);
		pictureStructure = picCodExtData.getBits(54, 2);
		progressiveFrame = picCodExtData.getBits(64, 1);
		
	}
	/*
	 * Construtor utilizado na operação de clone.
	 */
	private PictureCodingExtension(){
		
	}
	
	public byte[] getObject(){
		picCodExtData.trimToSize();
		return picCodExtData.array();
	}

	/**
	 * @return repeatFirstField
	 */
	public int getRepeatFirstField() {
		return this.repeatFirstField;
	}

	/**
	 * @param repeatFirstField The repeatFirstField to set.
	 */
	public void setRepeatFirstField(int value) {
		this.repeatFirstField = value;
		picCodExtData.setBits(62, value, 1);
	}
	/**
	 * @return topFieldFirst
	 */
	public int getTopFieldFirst() {
		return this.topFieldFirst;
	}

	/**
	 * @return pictureStructure - used to calculate numberOfFrameCenterOffsets by VideoSequenceElement
	 */
	public int getPictureStructure() {
		return pictureStructure;
	}
	
	/**
	 * @return progressiveFrame
	 */
	public int getProgressiveFrame() {
		return progressiveFrame;
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		PictureCodingExtension result = new PictureCodingExtension();
		GrowableByteBuffer picCExtD = (GrowableByteBuffer)picCodExtData.clone();
		
		result.setPicCodExtData(picCExtD);
		result.setRepeatFirstField(repeatFirstField);
		result.setTopFieldFirst(topFieldFirst);
		result.setPictureStructure(pictureStructure);
		result.setProgressiveFrame(progressiveFrame);
		
		return result;
	}
	/**
	 * @param picCodExtData The picCodExtData to set.
	 */
	private void setPicCodExtData(GrowableByteBuffer value) {
		this.picCodExtData = value;
	}
	/**
	 * @param pictureStructure The pictureStructure to set.
	 */
	public void setPictureStructure(int value) {
		this.pictureStructure = value;
		picCodExtData.setBits(54, value, 2);
	}
	/**
	 * @param progressiveFrame The progressiveFrame to set.
	 */
	public void setProgressiveFrame(int value) {
		this.progressiveFrame = value;
		picCodExtData.setBits(64, value, 1);
	}
	/**
	 * @param topFieldFirst The topFieldFirst to set.
	 */
	public void setTopFieldFirst(int value) {
		this.topFieldFirst = value;
		picCodExtData.setBits(56, value, 1);
	}
}
