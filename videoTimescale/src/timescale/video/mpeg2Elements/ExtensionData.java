/*
 * Created on Aug 2, 2004
 */
package timescale.video.mpeg2Elements;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * @author Sergio Cavendish
 */
public class ExtensionData extends VideoObject{

	private byte[] extensionStartCode;
	private Vector extensionDataVector; // variable length
	
	private IoTools ioTools;
	private Sequence seqElem;
	private Picture pic;
	private PictureCodingExtension picCodExt;

	public ExtensionData(int i, IoTools io) throws IOException {
		this(i, null, io);
	}
	
	public ExtensionData(int i, Sequence seqElem, IoTools io) throws IOException {
		this(i, seqElem, null, null, io);
	}
	
	public ExtensionData(int i, Sequence seqElem, Picture pic, PictureCodingExtension picCodExt, IoTools io)
	throws IOException {
		
		ioTools = io;	
		this.seqElem = seqElem;
		this.pic = pic;
		this.picCodExt = picCodExt;
		extensionDataVector = new Vector();
		
		while (ioTools.lookBytesAhead(Constants.EXTENSION_START_CODE)) {

			extensionStartCode = ioTools.getBytes(4);

			if (i == 0) { // follow SequenceExtension
				if (ioTools.lookExtensionIDAhead(Constants.SEQUENCE_DISPLAY_EXTENSION_ID)) {
					SequenceDisplayExtension sequenceDisplayExtension =	new SequenceDisplayExtension(ioTools);
					extensionDataVector.addElement(sequenceDisplayExtension);
				}
				else if(ioTools.lookExtensionIDAhead(Constants.SEQUENCE_SCALABLE_EXTENSION_ID)){
					SequenceScalableExtension sequenceScalableExtension = new SequenceScalableExtension(seqElem, ioTools);
					extensionDataVector.addElement(sequenceScalableExtension);
				}
			}
			
			if (i == 2){ // follow pictureCodingExtension
				if(ioTools.lookExtensionIDAhead(Constants.QUANT_MATRIX_EXTENSION_ID)){
					QuantMatrixExtension quantMatrixExtension = new QuantMatrixExtension(ioTools);
					extensionDataVector.addElement(quantMatrixExtension);
				}
				
				else if(ioTools.lookExtensionIDAhead(Constants.COPYRIGHT_EXTENSION_ID)){
					CopyrightExtension copyrightExtension = new CopyrightExtension(ioTools);
					extensionDataVector.addElement(copyrightExtension);
				}
				
				else if(ioTools.lookExtensionIDAhead(Constants.PICTURE_DISPLAY_EXTENSION_ID)){
					PictureDisplayExtension pictureDisplayExtension = 
						new PictureDisplayExtension(ioTools);
					extensionDataVector.addElement(pictureDisplayExtension);
				}
				
				else if(ioTools.lookExtensionIDAhead(Constants.PICTURE_SPATIAL_SCALABLE_EXTENSION_ID)){
					PictureSpatialScalableExtension pictureSpatialScalableExtension = new PictureSpatialScalableExtension(ioTools);
					extensionDataVector.addElement(pictureSpatialScalableExtension);
				}
				
				else if(ioTools.lookExtensionIDAhead(Constants.PICTURE_TEMPORAL_SCALABLE_EXTENSION_ID)){
					PictureTemporalScalableExtension pictureTemporalScalableExtension = new PictureTemporalScalableExtension(ioTools);
					extensionDataVector.addElement(pictureTemporalScalableExtension);
				}
				
				else if(ioTools.lookExtensionIDAhead(Constants.CAMERA_PARAMETERS_EXTENSION_ID)){
					CameraParametersExtension cameraParametersExtension = new CameraParametersExtension(ioTools);
					extensionDataVector.addElement(cameraParametersExtension);
				}
				
				else if(ioTools.lookExtensionIDAhead(Constants.ITU_T_EXTENSION_ID)){
					ItuTExtension ituTExtension = new ItuTExtension(ioTools);
					extensionDataVector.addElement(ituTExtension);
				}
			}
			extensionDataVector.trimToSize();
		}
	}

	/**
	 * 
	 */
	private ExtensionData() {
		
		
	}

	public byte[] getObject(){
		GrowableByteBuffer result = new GrowableByteBuffer();
			
		if(extensionDataVector.isEmpty())
			result.put(Constants.EXTENSION_START_CODE);
		else {
			Enumeration enume = extensionDataVector.elements();
			while(enume.hasMoreElements()){
				result.put(Constants.EXTENSION_START_CODE);
				result.put(((VideoObject)enume.nextElement()).getObject());
			}
		}
		result.trimToSize();
		return result.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone(Picture p, PictureCodingExtension pce) {
		ExtensionData result = new ExtensionData();
		byte[] extStartCode = new byte[4];
		for(int count = 0; count < extensionStartCode.length; count++)
			extStartCode[count] = extensionStartCode[count];
		
		Vector extDataVector = new Vector();
		Enumeration enume = extensionDataVector.elements();
		while(enume.hasMoreElements()){
			extDataVector.add(((VideoObject)enume.nextElement()).clone());
		}
		
		result.setExtensionStartCode(extStartCode);
		result.setExtensionDataVector(extDataVector);
		result.setSequenceElement(seqElem);
		result.setPicture(p);
		result.setPictureCodingExtension(pce);

		return result;
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		/* Esse método não deve ser utilizado. Foi criado apenas para ser a implementacao
		 * exigida por VideoObject.
		 */
		return null;
	}

	/**
	 * @param value
	 */
	private void setSequenceElement(Sequence value) {
		this.seqElem = value;
	}

	/**
	 * @param value
	 */
	private void setPictureCodingExtension(PictureCodingExtension value) {
		this.picCodExt = value;
	}

	/**
	 * @param value
	 */
	private void setPicture(Picture value) {
		this.pic = value;
	}

	/**
	 * @param value
	 */
	private void setExtensionDataVector(Vector value) {
		this.extensionDataVector = value;
	}

	/**
	 * @param value
	 */
	private void setExtensionStartCode(byte[] value) {
		this.extensionStartCode = value;
		
	}	
}
