/*
 * Created on 04/08/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;

import timescale.video.statistics.StatsCollector;
import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * @author Sergio Cavendish
 */
public class GOPHeader extends VideoObject{

	private GrowableByteBuffer gopBytes;
	private IoTools ioTools;
	private StatsCollector collector = StatsCollector.instance();
/*	private String timeCode; // length = 25 coded bits
	private char closedGOP; // lenght = 1 coded bit
	private char brokenLink; // length = 1 coded bit
	private String stuffing; // variable length

	StatsCollector collector = StatsCollector.instance();
*/
	public GOPHeader(IoTools io) throws IOException  {
		
		ioTools = io;
		collector.updateStats(Constants.GOP, Constants.INPUT);
		gopBytes = new GrowableByteBuffer(4);
		gopBytes.put(ioTools.getBytes(4));
		gopBytes.put(ioTools.nextStartCode());
		gopBytes.trimToSize();
	}

	/**
 * 
 */
public GOPHeader() {
	

}

	public byte[] getObject() {
		collector.updateStats(Constants.GOP, Constants.OUTPUT);
		gopBytes.trimToSize();
		return gopBytes.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		GOPHeader result = new GOPHeader();
		GrowableByteBuffer gBytes = (GrowableByteBuffer)gopBytes.clone();
		
		result.setGOPHeader(gBytes);
		return result;
	}

	/**
	 * @param value
	 */
	private void setGOPHeader(GrowableByteBuffer value) {
		this.gopBytes = value;		
	}
}
