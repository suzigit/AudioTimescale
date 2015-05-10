package timescale.data;

import timescale.facade.ElementaryStreamFacade;

public class RunResult {

	private ElementaryStreamFacade facade;
	private double finalInstantInProcessedStream;

	public RunResult (ElementaryStreamFacade facade, double finalInstantInProcessedStream) {
		setFacade(facade);
		setFinalInstantInProcessedStream(finalInstantInProcessedStream);
	}	
	
	public double getFinalInstantInProcessedStream() {
		return finalInstantInProcessedStream;
	}

	public void setFinalInstantInProcessedStream(
			double finalInstantInProcessedStream) {
		this.finalInstantInProcessedStream = finalInstantInProcessedStream;
	}

	public ElementaryStreamFacade getFacade () {
		return this.facade;
	}

	public void setFacade (ElementaryStreamFacade facade) {
		this.facade = facade;
	}	
	
}
