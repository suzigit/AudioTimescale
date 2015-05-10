/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (23/06/2004)
 */
 
package timescale.audio.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.audio.assembler.IAudioAssembler;
import timescale.audio.assembler.IAudioDisassembler;
import timescale.audio.timescaleAnalyzer.AlgorithmAnalyzer;
import timescale.audio.util.constants.ConfigConstants;
import timescale.data.FinalReport;
import timescale.data.ParametersProcessment;
import timescale.data.Factor;
import timescale.event.TimescaleFinishedEvent;
import timescale.event.TimescaleFinishedListener;
import timescale.event.TimescaleInstantListener;
import timescale.facade.MediaTimescaleFacade;

/**
 * Essa classe e ponto de entrada da aplicacao que realiza ajuste elastico
 * em audio comprimido.
 */
public class AudioTimescaleFacade implements MediaTimescaleFacade {
	
	protected AudioController controller;
	protected ParametersProcessment parameters;
	protected Set listeners = new HashSet();
	
	/** 
	 * Controi instancia da classe.
	 * @param assembler mecanismo de extracao de quadros do fluxo
	 * @param outputTools fonte de dados de saida
	 * @param parameters parametros de processamento
	 */
	public void config (IAssembler assembler, IDisassembler disassembler,
			ParametersProcessment parameters) {
		IAudioAssembler audioAssembler = (IAudioAssembler) assembler; 
		IAudioDisassembler audioDisassembler = (IAudioDisassembler) disassembler;
		this.parameters = parameters;
		this.controller = new AudioController(audioAssembler, audioDisassembler, this.parameters);
	}
	
	public IDisassembler getDisassembler() {
		return this.controller.getDisassembler(); 		
	}	

	/**
	 * Executa algoritmo de ajuste elastico. 
	 */
	public void run () {
		try {
			long initialTime = System.currentTimeMillis();
		
			this.controller.run();
		
			long finalTime = System.currentTimeMillis();		
			this.parameters.getReport().setProcessmentTime(finalTime-initialTime);
			
			this.fireTimescaleEvent();
			
			if (ConfigConstants.ENABLE_ALGORITHM_ANALYZER) {			
				AlgorithmAnalyzer analyzer = new AlgorithmAnalyzer(parameters);
				analyzer.analyze(ConfigConstants.REPORT_URL, ConfigConstants.REPORT_DATA_URL);
			}
			
		}
		catch (Exception e) {
			System.out.println("Erro no processamento do fluxo de audio!!");
			System.out.println("message=" + e.getMessage());
			e.printStackTrace();
		}
	}
		
	/** 
	 * Para processamento de ajuste elastico.
	 */	
	public void stop() {
	    this.parameters.setAlive(false);
	}
	
	/** 
	 * Modifica taxa de ajuste a ser utilizada em ajuste elastico.
	 * @param rate nova taxa de ajuste
	 */	
	public void setFactor (Factor rate) {
		controller.setRate(rate);
	}
	
	/** 
	 * Adiciona novo listener do evento de finalizacao do ajuste.
	 * @param l listener a ser adicionado 
	 */	
	public void addTimescaleListener (TimescaleFinishedListener l) {
        listeners.add(l);
    }

	public void addInstantListener (TimescaleInstantListener l) {
		this.controller.addAnchorListener(l);
    }
	 
	
	public FinalReport getReport() {
		return this.parameters.getReport();
	}
	
	/** 
	 * Avisa aos listeneres que o evento de finalizacao do ajuste aconteceu.
	 */	
	private void fireTimescaleEvent() throws Exception {
		TimescaleFinishedEvent event = new TimescaleFinishedEvent(this.parameters);
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext() ) {
            ((TimescaleFinishedListener) iterator.next()).actionPosTimescale(event);
        }
    }
	
}
