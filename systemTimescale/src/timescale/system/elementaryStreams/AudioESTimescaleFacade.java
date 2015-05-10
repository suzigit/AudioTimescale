package timescale.system.elementaryStreams;

import java.util.Iterator;

import timescale.audio.controller.AudioTimescaleFacade;
import timescale.data.RunResult;
import timescale.event.TimescaleFinishedEvent;
import timescale.event.TimescaleFinishedListener;
import timescale.facade.ElementaryStreamFacade;
import timescale.instantsRetrieval.OfflineInstantsRetrieval;

public class AudioESTimescaleFacade extends AudioTimescaleFacade 
		implements ElementaryStreamFacade {

	private String streamID;
	private boolean initiated = false;
	private double finalInstantInOriginalStream;
	
	public AudioESTimescaleFacade(String streamID) {
		this.streamID = streamID;
	}
	
	public String getStreamID() {
		return this.streamID;
	}
		
	private void beginProcessment() throws Exception {
		this.controller.outputInitialBytes();
	}
	
	public boolean isFinished() {
		return this.controller.isFinished();
	}
	
	public void setFinalInstant(double finalInstantInOriginalStream) {
		this.finalInstantInOriginalStream = finalInstantInOriginalStream;
	}
	
	
	public void run() {
		try {
			if (!initiated) {
				this.beginProcessment();
				this.initiated = true;
			}	
			
			boolean mustContinue = true;
			double actualInstant = -1;
			
			while (mustContinue && actualInstant < finalInstantInOriginalStream) {
				mustContinue = this.controller.handleOneFrame();
				actualInstant = this.parameters.getReport().actualInstantInOriginalStream();
			}
			
			double finalResultInProcessedStream = OfflineInstantsRetrieval.getTimescaleInstant(
					this.parameters.getReport(),finalInstantInOriginalStream);
			RunResult result = new RunResult(this, finalResultInProcessedStream);
			
			if (!mustContinue) {
				this.controller.finish();					
			}			
			
			fireTimescaleEvent(result);
		}
		catch (Exception e) {
			System.out.println("Erro em ESAudioTimescaleFacade");
			e.printStackTrace();
		}
	}
	
	private void fireTimescaleEvent(RunResult result) throws Exception {
		TimescaleFinishedEvent event = new TimescaleFinishedEvent(result);
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext() ) {
            ((TimescaleFinishedListener) iterator.next()).actionPosTimescale(event);
        }
    }
	
}
