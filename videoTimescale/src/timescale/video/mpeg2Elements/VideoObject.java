/*
 * Created on Nov 7, 2004
 */
package timescale.video.mpeg2Elements;

/**
 * @author Sergio Cavendish
 */
public abstract class VideoObject {
	
	public abstract byte[] getObject();
	public abstract Object clone();	
}
