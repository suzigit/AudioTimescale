/*
 * Created on 03/08/2004
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;

import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * This class is present only in scalable hierarchy. As the first objective of this application 
 * is to work with non-scalable video bitstreams, this class should not be instantiated.
 * 
 * @author Sergio Cavendish
 */
public class SequenceScalableExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer seqScalableExtData;
	
	/**
	 * It denotes the type of scalability of this bitstream.
	 */
/*	private String scalableMode; // length = 2 coded bits
	/**
	 * It denotes the bitstream layer in a scalable stream. The base layer has layerID = 0.
	 */
/*	private String layerID; // length = 4 coded bits
	private String lowerLayerPredictionHorizontalSize = ""; // length = 14 coded bits
	private String markerBit = ""; // length = 1 coded bit
	private String lowerLayerPredictionVerticalSize = ""; // length = 14 coded bits
	private String horizontalSubsamplingFactorM = ""; // length = 5 coded bits
	private String horizontalSubsamplingFactorN = ""; // length = 5 coded bits
	private String verticalSubsamplingFactorM = ""; // length = 5 coded bits
	private String verticalSubsamplingFactorN = ""; // length = 5 coded bits
	private String pictureMuxEnable = ""; // length = 1 coded bit
	private String muxToProgressiveSequence = ""; // length = 1 coded bit
	/**
	 * It denotes the numbre of enhancement layer pictures prior to the first vase layer picture. I believe
	 * it is going to be used to specify which enhancement and base pictures should be decoded
	 * simultaneously.
	 */
/*	private String pictureMuxOrder = ""; // length = 3 coded bits
	/**
	 * It denotes the number of enhancement layer pictures 
	 */
/*	private String pictureMuxFactor = ""; // length = 3 coded bits
	private String stuffing; // variable length

	private Sequence seqElem;
*/

	public SequenceScalableExtension(Sequence seqElem, IoTools io)
	throws IOException {
		
		ioTools = io;
		seqScalableExtData = new GrowableByteBuffer(4);
		seqScalableExtData.put(ioTools.nextStartCode());
		seqScalableExtData.trimToSize();
		
/*		this.scalableMode = data.toString(2);
		seqElem.setScalableMode(scalableMode);
		
		this.layerID = data.toString(4);
		
		if(scalableMode.equals(VideoConstants.SM_SPATIAL_SCALABILITY)){
			System.out.println("scalableMode = Spatial Scalability");
			
			this.lowerLayerPredictionHorizontalSize = data.toString(14);
			this.markerBit = data.toString(1);
			this.lowerLayerPredictionVerticalSize = data.toString(14);
			this.horizontalSubsamplingFactorM = data.toString(5);
			this.horizontalSubsamplingFactorN = data.toString(5);
			this.verticalSubsamplingFactorM = data.toString(5);
			this.verticalSubsamplingFactorN = data.toString(5);
		}
		
		if(scalableMode.equals(VideoConstants.SM_TEMPORAL_SCALABILITY)){
			System.out.println("scalableMode = Temporal Scalability");
			
			this.pictureMuxEnable = data.toString(1);
			if(pictureMuxEnable == "1"){
				this.muxToProgressiveSequence = data.toString(1);
			}
			this.pictureMuxOrder = data.toString(3);
			this.pictureMuxFactor = data.toString(3);
		}
		
		this.stuffing = data.nextStartCode();
*/
	}
	
	/**
	 * 
	 */
	public SequenceScalableExtension() {
		
	}

	public byte[] getObject(){
		seqScalableExtData.trimToSize();
		return seqScalableExtData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		SequenceScalableExtension result = new SequenceScalableExtension();
		GrowableByteBuffer sSED = (GrowableByteBuffer)seqScalableExtData.clone();
		result.setSequenceScapableExtensionData(sSED);
		return result;
	}

	/**
	 * @param value
	 */
	private void setSequenceScapableExtensionData(GrowableByteBuffer value) {
		this.seqScalableExtData = value;
	}
}
