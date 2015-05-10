/*
 * Created on Aug 01, 2004
 */

package timescale.video.mpeg2Elements;

import java.io.IOException;

import timescale.video.statistics.StatsCollector;
import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.data.Bits;
import util.functions.Convert;
import util.io.IoTools;

/**
 * Representa a estrutura Sequence Header, conforme o padrão H.262. 
 */
public class SequenceHeader extends VideoObject{

	private GrowableByteBuffer seqHeaderBytes;
	private IoTools ioTools;
	private StatsCollector collector = StatsCollector.instance();
	
/*	
	private String horizontalSizeValue; // length = 12 coded bits
	private String verticalSizeValue; // length = 12 coded bits
	private String aspectRatioInformation; // length = 4 coded bits
	private String frameRateCode; // length = 4 coded bits
	private String bitRateValue; // length = 18 coded bits
	private char markerBit; // lenght = 1 coded bit
	private String vbvBufferSizeValue; // length = 10 coded bits
	private char constrainedParametersFlag; // length = 1 coded bit
	private char loadIntraQuantiserMatrix;
	// length = 1 coded bit. Indicates if intraQuantiserMatrix exists
	private String intraQuantiserMatrix = ""; // length = 8 * 64 coded bits
	private char loadNonIntraQuantiserMatrix;
	// length = 1 coded bit. Indicates if nonIntraQuantiserMatrix exists
	private String nonIntraQuantiserMatrix = ""; // length = 8 * 64 coded bits
	private String stuffing; // variable length;

	//========== Video parameters ==========
	private static String frameRate = "";
*/
	
	//TODO: sergio, esse metodo esta correto??????????????
	public double getDuration() {
		byte eigthteenByte = this.seqHeaderBytes.array()[7];
		Bits bitsEigthteenByte = new Bits(eigthteenByte);
		String sFrameRateCode = bitsEigthteenByte.toString(4,4);
		int frameRateCode = Convert.bitsToInt(sFrameRateCode);
		double frameRate = frameRateValue[frameRateCode];
		return 1/frameRate;	
	}
	
	private static double [] frameRateValue = {0,23.976,24,25,29,97,30,50,59,94,60};	  

	

	/**
	 * Constructor of the class
	 * @param data - header data to be assigned
	 */
	public SequenceHeader(IoTools io) throws IOException  {
		
		ioTools = io;
		collector.updateStats(Constants.SEQ, Constants.INPUT);
		
		seqHeaderBytes = new GrowableByteBuffer(20);
		seqHeaderBytes.put(ioTools.getBytes(4));
		seqHeaderBytes.put(ioTools.nextStartCode());
		seqHeaderBytes.trimToSize();

/*
		data.toString(Constants.SEQUENCE_HEADER_CODE.length());

		this.horizontalSizeValue = data.toString(12);
		this.verticalSizeValue = data.toString(12);
		this.aspectRatioInformation = data.toString(4);
		this.frameRateCode = data.toString(4);

		if (frameRate.equals(""))
			frameRate = (String) Constants.FRAME_RATE.get(frameRateCode);

		this.bitRateValue = data.toString(18);
		this.markerBit = data.nextBit();
		this.vbvBufferSizeValue = data.toString(10);
		this.constrainedParametersFlag = data.nextBit();
		this.loadIntraQuantiserMatrix = data.nextBit();

		if (loadIntraQuantiserMatrix == '1') {
			intraQuantiserMatrix = data.toString(8 * 64);
		}

		this.loadNonIntraQuantiserMatrix = data.nextBit();

		if (loadNonIntraQuantiserMatrix == '1') {
			nonIntraQuantiserMatrix = data.toString(8 * 64);
		}

		this.stuffing = data.nextStartCode();
*/
	}

	/**
 * 
 */
public SequenceHeader() {

}

	public byte[] getObject() {

		collector.updateStats(Constants.SEQ, Constants.OUTPUT);
		seqHeaderBytes.trimToSize();
		return seqHeaderBytes.array();
	}
/*	
	/**
	 * @return verticalSizeValue - used to calculate verticalSize by SequenceElement class
	 */
/*
	public String getVerticalSizeValue() {
		return verticalSizeValue;
	}

	/**
	 * @return
	 */
/*	public static String getFrameRate() {
		return frameRate;
	}

	/**
	 * @return
	 */
/*
	public int length() {
		return this.toString().length();
	}
*/

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		SequenceHeader result = new SequenceHeader();
		GrowableByteBuffer sHBytes = (GrowableByteBuffer)seqHeaderBytes.clone();
		
		result.setSequenceHeaderBytes(sHBytes);
		
		return result;
	}

		/**
	 * @param value
	 */
	private void setSequenceHeaderBytes(GrowableByteBuffer value) {
		this.seqHeaderBytes = value;
	}
}
