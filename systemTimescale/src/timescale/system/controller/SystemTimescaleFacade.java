/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (28/11/2005)
 */

package timescale.system.controller;

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.data.FinalReport;
import timescale.data.ParametersProcessment;
import timescale.data.Factor;
import timescale.event.TimescaleFinishedListener;
import timescale.event.TimescaleInstantListener;
import timescale.facade.MediaTimescaleFacade;
import timescale.system.assembler.SystemAssembler;
import timescale.system.assembler.SystemDisassembler;
import timescale.system.data.ElementaryStreams;
import timescale.system.mux.SystemDemultiplexer;
import timescale.system.mux.SystemMultiplexer;

public class SystemTimescaleFacade implements MediaTimescaleFacade {

	private SystemController controller;
	private SystemDemultiplexer demux;
	private SystemMultiplexer mux;
	private ParametersProcessment parameters;
	
	public void config(IAssembler assembler, IDisassembler disassembler, 
			ParametersProcessment parameters) throws Exception {
		
		SystemAssembler systemAssembler = (SystemAssembler) assembler; 
		SystemDisassembler systemDisassembler = (SystemDisassembler) disassembler;
		this.parameters = parameters;
		ElementaryStreams streams = new ElementaryStreams();
		this.demux = new SystemDemultiplexer(systemAssembler, streams, parameters);
		this.controller = new SystemController(streams, parameters);
		this.mux = new SystemMultiplexer(systemDisassembler, streams, parameters);
	}
	
	public void run() {
		Thread t1 = new Thread(this.demux);
		t1.start();		
		Thread t2 = new Thread(this.controller);
		t2.start();
		Thread t3 = new Thread(this.mux);
		t3.start();
	}
	
	public void addTimescaleListener (TimescaleFinishedListener l) {
		this.mux.addTimescaleListener(l);			
	}
	
	public void addInstantListener(TimescaleInstantListener listener) {
		throw new UnsupportedOperationException();
	}
	
	public void stop() {
		this.parameters.setAlive(false);		
	}
	
	public IDisassembler getDisassembler() {
		return this.mux.getDisassembler();		
	}
	
	public void setFactor(Factor factor) throws Exception {
		throw new UnsupportedOperationException();	
	}
	
	public FinalReport getReport() {
		throw new UnsupportedOperationException();		
	}
	
	public double getTimescaleInstant (double originalInstant) {
		throw new UnsupportedOperationException();		
	}
	
}
