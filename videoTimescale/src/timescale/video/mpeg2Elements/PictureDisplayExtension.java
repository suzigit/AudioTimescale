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
public class PictureDisplayExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer picDisplayExtData;
	
	public PictureDisplayExtension(IoTools io) throws IOException {
		
		ioTools = io;
		picDisplayExtData = new GrowableByteBuffer(4);
		picDisplayExtData.put(ioTools.nextStartCode());
		picDisplayExtData.trimToSize();
/*		this.seqElem = seqElem;
		this.pic = pic;
		this.picCodExt = picCodExt;
		
		numberOfFrameCenterOffsetsValue = numberOfFrameCenterOffsets();
		
		pictureDisplayExtensionData = new StringBuffer("");
		
		data.toString(VideoConstants.EXTENSION_START_CODE.length());
		
		for (int i = 0; i < numberOfFrameCenterOffsetsValue; i++){
			pictureDisplayExtensionData.append(data.toString(34));
		}
		
		stuffing = data.nextStartCode();
*/
	}
	
	/**
	 * 
	 */
	public PictureDisplayExtension() {
		
	}

	public byte[] getObject(){
		picDisplayExtData.trimToSize();
		return picDisplayExtData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		PictureDisplayExtension result = new PictureDisplayExtension();
		GrowableByteBuffer pDispExtData = (GrowableByteBuffer)picDisplayExtData.clone();
		
		result.setPicDisplayExtData(pDispExtData);
		return result;
	}

	/**
	 * @param value
	 */
	private void setPicDisplayExtData(GrowableByteBuffer value) {
		this.picDisplayExtData = value;
	}
	
	/**
	 * Calculate numberOfFramesCenterOffsetsValue used by PictureDisplayExtension
	 * @return number
	 */
/*	private int numberOfFrameCenterOffsets() {

		int number;

		char progressiveSequence = seqElem.getProgressiveSequence();
		char repeatFirstField = picCodExt.getRepeatFirstField();
		char topFieldFirst = picCodExt.getTopFieldFirst();
		String pictureStructure = picCodExt.getPictureStructure();

		if (progressiveSequence == '1') {
			if (repeatFirstField == '1') {
				if (topFieldFirst == '1')
					number = 3;
				else
					number = 2;
			} else
				number = 1;
		} else {
			if (pictureStructure
				.equals(
					VideoConstants.PICTURE_STRUCTURE_VALUES.get("BottomField"))
				|| pictureStructure.equals(
					VideoConstants.PICTURE_STRUCTURE_VALUES.get("TopField")))
				number = 1;
			else {
				if (repeatFirstField == '1')
					number = 3;
				else
					number = 2;
			}
		}

		return number;
	}

	/**
	 * @return
	 */
/*	public int length() {
		return this.toString().length();
	}
*/
}
