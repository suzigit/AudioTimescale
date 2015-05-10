/*
 * Created on 03/08/2004
 */
package timescale.video.mpeg2Elements;

import java.io.IOException;

import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.io.IoTools;

/**
 * @author Sergio Cavendish
 */
public class UserData extends VideoObject{
	
	private GrowableByteBuffer userData; // variable length
	private IoTools ioTools;
	
	public UserData(IoTools io) throws IOException {
		
		ioTools = io;
		userData = new GrowableByteBuffer(4);
		userData.put(ioTools.getBytes(4));
		
		while(!ioTools.lookBytesAhead(Constants.START_CODE_PREFIX)){
			userData.put(ioTools.getBytes(1));
		}
		
		userData.put(ioTools.nextStartCode());
		userData.trimToSize();
	}

	/**
	 * 
	 */
	public UserData() {
		
	}

	public byte[] getObject(){
		userData.trimToSize();
		return userData.array();
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#clone()
	 */
	public Object clone() {
		UserData result = new UserData();
		GrowableByteBuffer uData = (GrowableByteBuffer)userData.clone();
		result.setUserData(uData);
		return result;
	}

	/**
	 * @param value
	 */
	private void setUserData(GrowableByteBuffer value) {
		this.userData = value;
	}	
}
