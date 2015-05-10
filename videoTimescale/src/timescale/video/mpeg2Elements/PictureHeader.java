/*
 * Created on 04/08/2004
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;


import timescale.video.statistics.StatsCollector;
import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * Representa o cabeçalho de uma figura.
 * @author Sergio Cavendish
 */
public class PictureHeader extends VideoObject {

	private IoTools ioTools;
	private GrowableByteBuffer picHeaderData;
	StatsCollector collector = StatsCollector.instance();

	/**
	 * The temporalReference value is significant when low_delay is equals to zero.
	 * When a coded frame is in the form of two field pictures, the temporal reference associated with each
	 * picture shall be the same.
	 * The first frame picture to be displayed after a GOP header has temporalReference = 0. It is
	 * incremented by 1 for each picture (modulo 1024).
	 * 
	 * If low_delay is either 0 or 1, the following applies:
	 * - if A is not a big picture with temporalReference N, and B is the following picture, the
	 * temporalReference of B is according the rule above.
	 * - PS: there is a special case if A is a big picture. It is necessary to examine in detail. 
	 * 
	 * So, it denotes the display order of each frame picture within a GOP.  
	 * 
	 * low_delay is a parameter of SequenceExtension 
	 */
	/*	private String temporalReference; // length = 10 coded bits
	
		/**
		 * It denotes the type (I, P or B) of the picture. It is going to be used to choose the pictures to
		 * be dropped or replicated.
		 */
	private int pictureCodingType; // lenght = 3 coded bits
	/*	private String vbvDelay; // length = 16 coded bits
		private String fullPelForwardVector = ""; // length = 1 coded bit
		private String forwardFCode = ""; // length = 3 coded bits
		private String fullPelBackwardVector = ""; // length = 1 coded bit
		private String backwardFCode = ""; // length = 3 coded bits
		private Vector extraData;
		// variable length. Gathers extra_bit_picture and extra_information_picture fields
		private String stuffing; // variable length
	*/

	public PictureHeader(IoTools io) throws IOException  {

		ioTools = io;
		picHeaderData = new GrowableByteBuffer(4);
		picHeaderData.put(ioTools.getBytes(4));
		picHeaderData.put(ioTools.nextStartCode());
		picHeaderData.trimToSize();

		//		this.temporalReference = data.toString(10);
		pictureCodingType = picHeaderData.getBits(42, 3);

		// Atualização dos dados estatísticos
		collector.updateStats(pictureCodingType, Constants.INPUT);

		// Parâmetros de PictureHeader
		/*		this.vbvDelay = data.toString(16);
		
				if (pictureCodingType.equals(VideoConstants.PCT_P)
					|| pictureCodingType.equals(VideoConstants.PCT_B)) {
					this.fullPelForwardVector = data.toString(1);
					this.forwardFCode = data.toString(3);
				}
				if (pictureCodingType.equals(VideoConstants.PCT_B)) {
					this.fullPelBackwardVector = data.toString(1);
					this.backwardFCode = data.toString(3);
				}
		
				extraData = new Vector();
		
				while (data.lookAhead("1")) {
					extraData.addElement(data.toString(1));
					extraData.addElement(data.toString(8));
				}
		
				extraData.addElement(data.toString(1));
				extraData.trimToSize();
		
				this.stuffing = data.nextStartCode();
		*/
	}
	
	/**
	 * Construtor da classe para ser utilizado na operacao de cloning.
	 */
	private PictureHeader(){
		
	}

	public byte[] getObject() {
		collector.updateStats(pictureCodingType, Constants.OUTPUT);
		picHeaderData.trimToSize();
		return picHeaderData.array();
	}
	/**
	 * @return pictureCodingType - used by SequenceElement
	 */
	public int getPictureCodingType() {
		return pictureCodingType;
	}
	
	/**
	 * @param pictureCodingType The pictureCodingType to set.
	 */
	public void setPictureCodingType(int value) {
		this.pictureCodingType = value;
		this.picHeaderData.setBits(42, value, 3);
	}
	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		PictureHeader result = new PictureHeader();
		GrowableByteBuffer picHData = (GrowableByteBuffer)picHeaderData.clone();
		int picCodingType = pictureCodingType;
		
		result.setPictureHeaderData(picHData);
		result.setPictureCodingType(picCodingType);
		return result;
	}

	/**
	 * @param picHData
	 */
	private void setPictureHeaderData(GrowableByteBuffer picHData) {
		this.picHeaderData = picHData;		
	}
}
