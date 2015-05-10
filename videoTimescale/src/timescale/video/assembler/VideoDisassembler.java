/*
 * Created on 04/10/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.assembler;

import timescale.assembler.IDisassembler;
import timescale.video.controller.VideoController;
import timescale.video.mpeg2Elements.VideoObject;
import timescale.video.processor.Buffer;
import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;
import util.io.OutputTools;

/**
 * @author Sergio Cavendish
 */
public class VideoDisassembler extends Thread implements IDisassembler {
	
	private Buffer sharedBuffer;
	
	//Esse objeto abstrai se o resultado do ajuste de video
	//ira para um arquivo ou para um buffer		
	private OutputTools outputTools;
	
	private Object object;
	
	private boolean end = false;
	
	private boolean seqEndCodeInserted;
	
	VideoController controller;
	
	public VideoDisassembler(OutputTools outputTools) {
		super("VideoOutput");
		this.outputTools = outputTools;
		seqEndCodeInserted = false;
	}
	
	public OutputTools getOutputTools() {
		return this.outputTools;
	}
	
	public String getOutputName() {
		return this.outputTools.getName();
	}

	public void setBuffer (Buffer shared) {
		sharedBuffer = shared; 
	}
	
	//TODO: Sergio, tratar por evento
	public void setController(VideoController control) {
		controller = control;
	}
	
	public void run() {
		
		try {
			
			do {
				object = sharedBuffer.getSharedObject();
					if (object instanceof GrowableByteBuffer) {
						if (!((GrowableByteBuffer) object).isEmpty()) {
							byte[] b = ((GrowableByteBuffer) object).array();
							this.outputTools.output(b, 0, 
									((GrowableByteBuffer) object).position());
							/*
							 * Verifica se o objeto corresponde ao código
							 * Sequence_end_code
							 */
							boolean seqEndCode = true;
							int count = 0;
							if (b.length != Constants.SEQUENCE_END_CODE.length)
								seqEndCode = false;
							else
								while ((count < b.length) && seqEndCode == true) {
									if (b[count] != Constants.SEQUENCE_END_CODE[count]){
										seqEndCode = false;
									}
									count++;
								}
							if (seqEndCode == true){
								seqEndCodeInserted = true;
								this.close();
								controller.setTaskFinished();
							}
						}
					} else if (object instanceof VideoObject) {
						outputTools.output(((VideoObject) object).getObject());
					}
				
				object = null;
			} while (end == false);
			/*
			 * Garante que o arquivo de saída sempre contenha o código Sequece_End_Code.
			 */
			if (!seqEndCodeInserted) {
					outputTools.output(Constants.SEQUENCE_END_CODE);
					//controller.closeOutputFile();
			}
		} catch (Exception e) {
			System.out.println("Video Output Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	public void close() throws Exception {
		outputTools.finish();
	} 
	
	public void setEnd() {
		end = true;
	}
}