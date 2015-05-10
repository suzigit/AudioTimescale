package util.data;

public class RunResult {
	
	private double finalInstantInProcessedStream;
	private boolean isFinished;

	public RunResult (double finalInstantInProcessedStream, boolean isFinished) {
		setFinalInstantInProcessedStream(finalInstantInProcessedStream);
		setFinished(isFinished);
	}	
	
	public double getFinalInstantInProcessedStream() {
		return finalInstantInProcessedStream;
	}

	public void setFinalInstantInProcessedStream(
			double finalInstantInProcessedStream) {
		this.finalInstantInProcessedStream = finalInstantInProcessedStream;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	
	
}
