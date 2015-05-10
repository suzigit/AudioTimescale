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
public class ItuTExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer ituData;
	
	public ItuTExtension(IoTools io) throws IOException {
		
		ioTools = io;
		ituData = new GrowableByteBuffer(4);
		ituData.put(ioTools.nextStartCode());
		ituData.trimToSize();
	}
	
	/**
	 * 
	 */
	public ItuTExtension() {
	}

	public byte[] getObject(){
		ituData.trimToSize();
		return ituData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		ItuTExtension result = new ItuTExtension();
		GrowableByteBuffer iData = (GrowableByteBuffer)ituData.clone();
		result.setItuData(iData);
		return result;
	}

	/**
	 * @param value
	 */
	private void setItuData(GrowableByteBuffer value) {
		this.ituData = value;		
	}
}
