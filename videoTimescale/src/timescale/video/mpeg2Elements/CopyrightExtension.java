/*
 * Created on 03/08/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;


import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * @author Sergio Cavendish
 */
public class CopyrightExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer copyrightData;
	
	public CopyrightExtension(IoTools io) throws IOException {
		
		ioTools = io;
		copyrightData = new GrowableByteBuffer(4);
		copyrightData.put(ioTools.nextStartCode());
		copyrightData.trimToSize();
	}
	
	/**
	 * 
	 */
	private CopyrightExtension() {
		
		
	}

	public byte[] getObject(){
		copyrightData.trimToSize();
		return copyrightData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		CopyrightExtension result = new CopyrightExtension();
		GrowableByteBuffer cData = (GrowableByteBuffer)copyrightData.clone();
		
		result.setCopyrightData(cData);
		
		return result;
	}

	/**
	 * @param data
	 */
	private void setCopyrightData(GrowableByteBuffer value) {
		this.copyrightData = value;		
	}
}
