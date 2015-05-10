/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (15/06/2004)
 */

package timescale.audio.controller;

import timescale.audio.assembler.IAudioAssembler;
import timescale.audio.assembler.IAudioDisassembler;
import timescale.audio.format.generalFormat.Frame;
import timescale.audio.format.generalFormat.FramesCollection;
import timescale.audio.processor.StreamProcessor;
import timescale.data.Factor;
import timescale.data.ParametersProcessment;
import timescale.event.TimescaleInstantListener;

/**
 * Essa classe controla a geração do stream ajustado utilizando servicos
 * do subsistema de entrada e saida. Prove servicos a Facade. 
 */
public class AudioController {
	
	private IAudioAssembler assembler;	
	private IAudioDisassembler disassembler;
	private ParametersProcessment parameters;
	private StreamProcessor processor;
	private FramesCollection frames;
	
	/** 
	 * Controi instancia da classe.
	 * @param outputTools fonte de dados de saida
	 * @param assembler mecanismo de extracao de quadros do fluxo
	 * @param parameters parametros de processamento
	 */
	public AudioController (IAudioAssembler assembler, 
			IAudioDisassembler disassembler,
			ParametersProcessment parameters) {
		this.processor = new StreamProcessor(parameters);
		this.disassembler = disassembler;
		this.assembler = assembler;
		this.parameters = parameters;
		this.frames = new FramesCollection();
	}
	
	public boolean handleOneFrame() throws Exception {
		boolean makeAnyOperation = false;
		
		
		//Cria frame
		Frame newFrame = this.assembler.createCompleteFrame();
		if (newFrame!=null) {
			this.frames.add(newFrame);
			makeAnyOperation = true;
			setFrameDuration();
		}
					
		//Processa frame
		if	(this.frames.size()>assembler.getConstants().MAX_DEPENDENCY_NUMBER_OF_FRAMES_BEFORE
					|| newFrame==null) {
			makeAnyOperation = this.processor.processFrame(this.frames);			
		}
					
		//Escreve frame
		if (this.frames.size() > 0) {
			double max = Math.max(assembler.getConstants().MAX_DEPENDENCY_NUMBER_OF_FRAMES,assembler.getConstants().MAX_DEPENDENCY_NUMBER_OF_FRAMES_BEFORE); 
			if (this.frames.size()>max	|| newFrame==null) {						
				Frame f = frames.elementAt(0); 
				this.disassembler.output(f);
				this.frames.remove(0);
				makeAnyOperation = true;
			}
		}
		
		return makeAnyOperation;
	}
	
	public void run() throws Exception {
		this.outputInitialBytes();
		boolean mustContinue = this.handleOneFrame();
		while(mustContinue) {
			mustContinue = this.handleOneFrame();
		}
		this.finish();
	}
	
	/*
	 * Extrai dados iniciais, que não pertence a nenhum frame.
	 */
	public void outputInitialBytes() throws Exception {				
		byte[] initialData = assembler.getInitialBitsWithoutFrames();
		if (initialData!=null) {
			this.disassembler.output(initialData);	
		}
	}
	
	public void finish() throws Exception {
		this.processor.finalizeProcessment();
		byte b[] = assembler.extractTail();
		if (b!=null) {
			this.disassembler.output(b);	
		}	
		this.disassembler.finish();		
	}
	
	public boolean isFinished() {
		return this.disassembler.isFinished();
	}
	
	public void setFrameDuration() {
		double d = frames.elementAt(0).getHeader().getTimeDuration();
		this.parameters.getReport().setFrameDuration(d);
	}


	/** 
	 * Modifica taxa de ajuste a ser utilizada em ajuste elastico.
	 * @param rate nova taxa de ajuste
	 */		
	public void setRate (Factor rate) {		
		this.processor.setRate(rate);
	}
	
	/** 
	 * Retorna fonte de dados de saida.
	 * @return fonte de dados de saida
	 */	
	public IAudioDisassembler getDisassembler () {
		return this.disassembler;
	}
	
	public void addAnchorListener (TimescaleInstantListener l) {          
		this.processor.addAnchorListener(l);
    }
	
}
