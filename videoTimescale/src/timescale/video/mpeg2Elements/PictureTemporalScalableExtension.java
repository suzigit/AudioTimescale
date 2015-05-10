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
public class PictureTemporalScalableExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer picTemporalExtData;
	/*
	 * Stores all data together but extension_start_code_identifier
	 * @author Sergio Cavendish
	 */
/*	private String pictureTemporalScalableExtensionData; // length = 23 coded bits
	private String stuffing; // variable length
	*/	
	public PictureTemporalScalableExtension(IoTools io) throws IOException {
		
		ioTools = io;
		picTemporalExtData = new GrowableByteBuffer(4);
		picTemporalExtData.put(ioTools.nextStartCode());
		picTemporalExtData.trimToSize();
		
/*		data.toString(VideoConstants.PICTURE_TEMPORAL_SCALABLE_EXTENSION_ID.length());
		
		this.pictureTemporalScalableExtensionData = data.toString(23);
		this.stuffing = data.nextStartCode();
		*/
	}
	
	/**
	 * 
	 */
	public PictureTemporalScalableExtension() {
		
	}

	public byte[] getObject(){
		picTemporalExtData.trimToSize();
		return picTemporalExtData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		PictureTemporalScalableExtension result = new PictureTemporalScalableExtension();
		GrowableByteBuffer picTempExtData = 
			(GrowableByteBuffer)picTemporalExtData.clone();
		
		result.setPictureTemporalExtensionData(picTempExtData);
		
		return result;
	}

	/**
	 * @param picTempExtData
	 */
	private void setPictureTemporalExtensionData(GrowableByteBuffer value) {
		this.picTemporalExtData = value;
	}
}
