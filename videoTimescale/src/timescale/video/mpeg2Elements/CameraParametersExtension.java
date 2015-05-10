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
public class CameraParametersExtension extends VideoObject{
	
	private IoTools ioTools;
	private GrowableByteBuffer cameraData;
	
	public CameraParametersExtension(IoTools io) throws IOException {
		
		ioTools = io;
		cameraData = new GrowableByteBuffer(4);
		cameraData.put(ioTools.nextStartCode());
		cameraData.trimToSize();
	}
	
	/**
	 * 
	 */
	private CameraParametersExtension() {
		
		
	}

	public byte[] getObject(){
		cameraData.trimToSize();
		return cameraData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		CameraParametersExtension result = new CameraParametersExtension();
		GrowableByteBuffer camData = (GrowableByteBuffer)cameraData.clone();
		
		result.setCameraData(camData);
		
		return result;
	}

	/**
	 * @param camData
	 */
	private void setCameraData(GrowableByteBuffer value) {
		this.cameraData = value;
	}
}
