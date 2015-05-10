/*
 * Created on Nov 5, 2004
 */
package timescale.video.mpeg2Elements;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;


import timescale.data.ElementID;
import timescale.video.statistics.StatsCollector;
import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * Contém todos os elementos de uma seqüência, conforme definido no padrão H.262.
 * 
 * @author Sergio Cavendish
 */
public class Sequence extends VideoObject{
	
	private SequenceHeader sequenceHeader;
	private SequenceExtension sequenceExtension;
	private Vector sequenceElementVector;
	private IoTools ioTools;
	private StatsCollector collector = StatsCollector.instance();
	private boolean sequenceExtensionFlag;
	private SequenceInformation seqInformation;
	
	private int totalPictures = 0;
	
	public Sequence(IoTools io, int beginElementIdGenerator) throws IOException {

		ioTools = io;
		sequenceExtensionFlag = false;
		seqInformation = new SequenceInformation();
		
		sequenceHeader = new SequenceHeader(ioTools);
		sequenceElementVector = new Vector();
		
		if (ioTools.lookBytesAhead(Constants.EXTENSION_START_CODE)) {
			sequenceExtension = new SequenceExtension(ioTools);
			sequenceExtensionFlag = true;
			
			/*
			collector.incProgressiveSequenceCounter(
					sequenceExtension.getProgressiveSequence());
			collector.incLowDelayCounter(sequenceExtension.getLowDelay());
			*/
			
			extensionAndUserData(0, sequenceElementVector);
			
			do {
				if (ioTools.lookBytesAhead(Constants.GROUP_START_CODE)) {
					GOPHeader gopHeader = new GOPHeader(ioTools);
					sequenceElementVector.addElement(gopHeader);

					extensionAndUserData(1, sequenceElementVector);
				}
				
				ElementID id = new ElementID(beginElementIdGenerator+totalPictures);
				Picture picture = new Picture(id, this, ioTools);
				totalPictures++;
				
				seqInformation.atualizeLists(picture);
				
				sequenceElementVector.addElement(picture);

			} while (
				ioTools.lookBytesAhead(Constants.PICTURE_START_CODE)
					|| ioTools.lookBytesAhead(Constants.GROUP_START_CODE));
		}
		else{
//			System.out.println("\nThis is a MPEG-1 file");
//			videoFormat = "MPEG1";
		}
		sequenceElementVector.trimToSize();
	}
	
	
	/**
	 * 
	 */
	public Sequence() {
		
	}
	
	public int getTotalPictures () {
		return this.totalPictures;
	}

	public byte[] getObject(){
		GrowableByteBuffer result = new GrowableByteBuffer();
		
		result.put(sequenceHeader.getObject());
		
		if (sequenceExtensionFlag == true)
			result.put(sequenceExtension.getObject());
		
		for(Enumeration enume = sequenceElementVector.elements(); enume.hasMoreElements();)
				result.put(((VideoObject)enume.nextElement()).getObject());
		
		result.trimToSize();
		return result.array();
	}
	
	private void extensionAndUserData(int i, Vector vector) 
	throws IOException {

			while (ioTools.lookBytesAhead(Constants.EXTENSION_START_CODE)
				|| ioTools.lookBytesAhead(Constants.USER_DATA_START_CODE)) {

				if ((i != 1)
					&& (ioTools.lookBytesAhead(Constants.EXTENSION_START_CODE))) {
					ExtensionData extensionData = new ExtensionData(i, this, ioTools);
				
					vector.addElement(extensionData);
				}
				if (ioTools.lookBytesAhead(Constants.USER_DATA_START_CODE)) {
					UserData userData = new UserData(ioTools);
					vector.addElement(userData);
				}
			}
		}

		/**
		 * @return
		 */
		public SequenceHeader getSequenceHeader() {
			return sequenceHeader;
		}

		/**
		 * @return
		 */
		public Vector getSequenceElementVector() {
			return sequenceElementVector;
		}

		/**
		 * @return
		 */
		public SequenceExtension getSequenceExtension() {
			return sequenceExtension;
		}
		
	/**
	 * @return Returns the seqInformation.
	 */
	public SequenceInformation getSeqInformation() {
		return seqInformation;
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		Sequence result = new Sequence();
		SequenceInformation sInf = new SequenceInformation();
		
		SequenceHeader sHeader = (SequenceHeader)sequenceHeader.clone();
		if(sequenceExtensionFlag){
			SequenceExtension sExt = (SequenceExtension)sequenceExtension.clone();
			result.setSequenceExtensionFlag(sequenceExtensionFlag);
			result.setSequenceExtension(sExt);
		}
		
		Vector sElemVector = new Vector();
		Enumeration enume = sequenceElementVector.elements();
		while(enume.hasMoreElements()){
			Object object = ((VideoObject)enume.nextElement()).clone();
			sElemVector.add(object);
			if(object instanceof Picture)
				sInf.atualizeLists((Picture)object);
		}
		
		result.setSequenceHeader(sHeader);
		result.setSequenceElementVector(sElemVector);
		result.setSequenceInformation(sInf);
		
		return result;
	}

	/**
	 * @param value
	 */
	private void setSequenceInformation(SequenceInformation value) {
		this.seqInformation = value;
	}

	/**
	 * @param value
	 */
	private void setSequenceElementVector(Vector value) {
		this.sequenceElementVector = value;
	}

	/**
	 * @param value
	 */
	private void setSequenceHeader(SequenceHeader value) {
		this.sequenceHeader = value;
	}

	/**
	 * @param value
	 */
	private void setSequenceExtension(SequenceExtension value) {
		this.sequenceExtension = value;
	}

	/**
	 * @param value
	 */
	private void setSequenceExtensionFlag(boolean value) {
		this.sequenceExtensionFlag = value;
	}
}
