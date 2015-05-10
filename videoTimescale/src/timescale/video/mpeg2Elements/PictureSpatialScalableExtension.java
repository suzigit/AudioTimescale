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
public class PictureSpatialScalableExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer picSpatialExtData;
/*	private String lowerLayerTemporalReference; // length = 10 coded bits
	private char markerBit1; // length = 1 coded bit
	private String lowerLayerHorizontalOffset; // length = 15 coded bits
	private char markerBit2; // length = 1 coded bit
	private String lowerLayerVerticalOffset; // length = 15 coded bits
	private String spatialTemporalWeightCodeTableIndex; // length = 2 coded bits
	private char lowerLayerProgressiveFrame; // length = 1 coded bit
	private char lowerLayerDeinterlacedFieldSelect; // length = 1 coded bit
	private String stuffing; // variable length

	private Sequence seqElem;
	private Picture pic;
*/
	public PictureSpatialScalableExtension(IoTools io) throws IOException {
		this(null, null, io);
	}
	
	public PictureSpatialScalableExtension(Sequence seqElem, IoTools io)
	throws IOException {
		this(seqElem, null, io);
	}
	
	public PictureSpatialScalableExtension(Sequence seqElem, Picture pic, IoTools io)
	throws IOException {
		
		ioTools = io;
		picSpatialExtData = new GrowableByteBuffer(4);
		picSpatialExtData.put(ioTools.nextStartCode());
		picSpatialExtData.trimToSize();
/*		this.seqElem = seqElem;
		this.pic = pic;
		pic.setPictureSpatialScalableExtFlag(true);

		data.toString(VideoConstants.PICTURE_SPATIAL_SCALABLE_EXTENSION_ID.length());
		
		this.lowerLayerTemporalReference = data.toString(10);
		this.markerBit1 = data.nextBit();
		this.lowerLayerHorizontalOffset = data.toString(15);
		this.markerBit2 = data.nextBit();
		this.lowerLayerVerticalOffset = data.toString(15);
		this.spatialTemporalWeightCodeTableIndex = data.toString(2);
		
		pic.setSpatialTemporalWeightCodeTableIndex(spatialTemporalWeightCodeTableIndex);		
		
		this.lowerLayerProgressiveFrame = data.nextBit();
		this.lowerLayerDeinterlacedFieldSelect = data.nextBit();
		this.stuffing = data.nextStartCode();
*/
	}
	
	/**
	 * 
	 */
	public PictureSpatialScalableExtension() {
		
	}

	public byte[] getObject(){
		picSpatialExtData.trimToSize();
		return picSpatialExtData.array();	
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		PictureSpatialScalableExtension result = new PictureSpatialScalableExtension();
		GrowableByteBuffer pSpatialExtData =
			(GrowableByteBuffer)picSpatialExtData.clone();
		result.setPictureSpatialScalableExtensionData(pSpatialExtData);
		return result;
	}

	/**
	 * @param value
	 */
	private void setPictureSpatialScalableExtensionData(GrowableByteBuffer value) {
		this.picSpatialExtData = value;		
	}
}
