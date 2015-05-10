package timescale.system.elementaryStreams;

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.data.Factor;
import timescale.data.ParametersProcessment;
import timescale.event.TimescaleFinishedListener;
import timescale.facade.ElementaryStreamFacade;

public class OtherESTimescaleFacade implements ElementaryStreamFacade {

	private String streamID;
	
	public OtherESTimescaleFacade(String streamID) {
		this.streamID = streamID;
	}
	
	public void config(IAssembler assembler, IDisassembler disassembler, 
			ParametersProcessment parameters) throws Exception {
		throw new UnsupportedOperationException();
	}	
	
	public String getStreamID() {
		return this.streamID;
	}
	
	public void setFinalInstant(double finalInstantInOriginalStream) {
		throw new UnsupportedOperationException();
	}
	
	public boolean isFinished() {
		return true;
	}
	
	public void setFactor(Factor rate) {
		throw new UnsupportedOperationException();
	}
	
	public void addTimescaleListener (TimescaleFinishedListener l) {
		throw new UnsupportedOperationException();
    }
	
	public IDisassembler getDisassembler() {
		throw new UnsupportedOperationException();
	}
	
	public void run() {
		
	}
}
