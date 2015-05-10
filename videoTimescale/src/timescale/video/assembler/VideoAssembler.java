/*
 * Created on 04/10/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.assembler;

import timescale.assembler.IAssembler;
import timescale.video.controller.VideoController;
import timescale.video.mpeg2Elements.Sequence;
import timescale.video.processor.Buffer;
import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.data.ContentType;
import util.io.IoTools;

/**
 * @author Sergio Cavendish
 */
public class VideoAssembler extends Thread implements IAssembler {

	private Buffer inputBuffer;
	private Sequence seq;
	private GrowableByteBuffer stuffing;
	private boolean end = false;
	private IoTools ioTools;
	private VideoController controller;

	public VideoAssembler(IoTools io) {
		super("VideoInput");		
		ioTools = io;
	}
	
	public void setBuffer (Buffer input) {
		inputBuffer = input; 
	}	
	
	public ContentType getType () {
		return new ContentType (ContentType.ID_MPEG2_VIDEO);
	}
	
	public void run() {
		
		int indexOfExtractedPicture = 0;
		
		try {
			stuffing = new GrowableByteBuffer(4);
			stuffing.put(ioTools.nextStartCode());
			inputBuffer.setSharedObject(stuffing);
	
			while (!ioTools.lookBytesAhead(Constants.SEQUENCE_END_CODE) && !end) {
				seq = new Sequence(ioTools, indexOfExtractedPicture);
				indexOfExtractedPicture += seq.getTotalPictures();
				inputBuffer.setSharedObject(seq);
			}
			stuffing = new GrowableByteBuffer(4);
			stuffing.put(Constants.SEQUENCE_END_CODE);
			inputBuffer.setSharedObject(stuffing);
			
			ioTools.close();
			
		} catch (Exception e) {
			System.out.println("Video Input Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void setEnd() {
		end = true;
	}
}
