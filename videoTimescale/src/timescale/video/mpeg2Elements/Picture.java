/*
 * Created on 05/08/2004
 */

package timescale.video.mpeg2Elements;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import timescale.data.ElementID;
import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * @author Sergio Cavendish
 */
public class Picture extends VideoObject{

	//Sergio, acrescentei esse atributo (necessario para ancora)
	private ElementID id;
	
	private PictureHeader pictureHeader;
	private PictureCodingExtension pictureCodingExtension;
	private Vector extensionDataVector;
	/**
	 * Todos os slices de uma figura são armazenados em pictureData.
	 */
	private GrowableByteBuffer pictureData;
	private IoTools ioTools;
	private Sequence seqElem;

	/**
	 * Constructor of the class. Receives all references to obtain the necessary
	 * parameters
	 */
	public Picture(ElementID id, Sequence seqElem, IoTools io) throws IOException {
//System.out.println("Picture: Criou picture com id " + id.getValue());		
		this.id = id;
		ioTools = io;
		this.seqElem = seqElem;

		pictureHeader = new PictureHeader(ioTools);

		pictureCodingExtension = new PictureCodingExtension(ioTools);
		
		extensionDataVector = new Vector();
		extensionAndUserData(2, extensionDataVector);
		
		extensionDataVector.trimToSize();
		
		pictureData = new GrowableByteBuffer();
		while (ioTools.lookSliceStartCodeAhead()) {
				this.pictureData.put(ioTools.getBytes(4));
				this.pictureData.put(ioTools.nextStartCode());
		}
		pictureData.trimToSize();
	}
	
	public ElementID getID() {
		return this.id;
	}
	public void setID(ElementID id) {
		this.id = id;
	}
	
	/**
	 * Construtor da classe para ser utilizado na operacao de cloning.
	 */
	private Picture(){
		
	}

	/**
	 * Returns the Picture object as a array of bytes
	 */
	public byte[] getObject() {
		GrowableByteBuffer result = new GrowableByteBuffer();

		result.put(pictureHeader.getObject());
		result.put(pictureCodingExtension.getObject());
		
		for(Enumeration enume = extensionDataVector.elements(); enume.hasMoreElements();)
			result.put(((VideoObject)enume.nextElement()).getObject());
		
		if(!pictureData.isEmpty()){
			pictureData.trimToSize();
			result.put(pictureData);
		}
		
		result.trimToSize();
		return result.array();
	}

	/**
	 * Used to verify the existence of extension data (extensions) and user data. Stores the created objects
	 * in vector
	 * @param i
	 * @param vector
	 * @throws IOException
	 */
	private void extensionAndUserData(int i, Vector vector) throws IOException {

		while (ioTools.lookBytesAhead(Constants.EXTENSION_START_CODE)
			|| ioTools.lookBytesAhead(Constants.USER_DATA_START_CODE)) {

			if ((i != 1)
				&& (ioTools.lookBytesAhead(Constants.EXTENSION_START_CODE))) {
				ExtensionData extensionData =
					new ExtensionData(i, seqElem, this, pictureCodingExtension, ioTools);
				vector.addElement(extensionData);
			}
			if (ioTools.lookBytesAhead(Constants.USER_DATA_START_CODE)) {
				UserData userData = new UserData(ioTools);
				vector.addElement(userData);
			}
		}
	}

	public int getPictureCodingType() {
		return pictureHeader.getPictureCodingType();
	}
	
	/**
	 * @return Returns the repeatFieldFirst.
	 */
	public int getRepeatFirstField() {
		return pictureCodingExtension.getRepeatFirstField();
	}
	
	public void setRepeatFirstField(int value) {
		pictureCodingExtension.setRepeatFirstField(value);
	}
	
	/**
	 * @return Returns the pictureStructure.
	 */
	public int getPictureStructure() {
		return pictureCodingExtension.getPictureStructure();
	}
	
	/**
	 * @return Returns the progressiveFrame.
	 */
	public int getProgressiveFrame() {
		return pictureCodingExtension.getProgressiveFrame();
	}
	
	/**
	 * @return Returns topFieldFirst.
	 */
	public int getTopFieldFirst() {
		return pictureCodingExtension.getTopFieldFirst();
	}
	
	public Object clone(){
		Picture result = new Picture();		
		result.id = new ElementID(-1);
		PictureHeader picHeader = (PictureHeader)pictureHeader.clone();
		PictureCodingExtension picCodingExtension = 
			(PictureCodingExtension)pictureCodingExtension.clone();
		Vector extDataVector = new Vector();
			Enumeration enume = extensionDataVector.elements();
			while(enume.hasMoreElements()){
				extDataVector.add(((VideoObject)enume.nextElement()).clone());
			}
		GrowableByteBuffer picData = (GrowableByteBuffer)pictureData.clone();
		Sequence newSeqElem = seqElem;
		
		result.setPictureHeader(picHeader);
		result.setPictureCodingExtension(picCodingExtension);
		result.setExtensionDataVector(extDataVector);
		result.setPictureData(picData);
		result.setSequenceElement(newSeqElem);
		
		return (Object)result;
	}
	
	private void setPictureHeader(PictureHeader picHeader){
		this.pictureHeader = picHeader;
	}
	
	private void setPictureCodingExtension(PictureCodingExtension picCodExt){
		this.pictureCodingExtension = picCodExt;
	}
	
	private void setExtensionDataVector( Vector extDataVector){
		this.extensionDataVector = extDataVector;
	}
	
	private void setPictureData(GrowableByteBuffer picData){
		this.pictureData = picData;
	}
	
	private void setSequenceElement(Sequence seqElem){
		this.seqElem = seqElem;
	}
}
