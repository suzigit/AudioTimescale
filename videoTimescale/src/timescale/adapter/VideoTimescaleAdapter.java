/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (23/08/2004)
 */

package timescale.adapter;


import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.data.FinalReport;
import timescale.data.ParametersProcessment;
import timescale.data.Factor;
import timescale.event.TimescaleFinishedListener;
import timescale.event.TimescaleInstantListener;
import timescale.facade.MediaTimescaleFacade;
import timescale.video.assembler.VideoAssembler;
import timescale.video.assembler.VideoDisassembler;
import timescale.video.controller.VideoController;

public class VideoTimescaleAdapter implements MediaTimescaleFacade {
	
	private VideoController videoController;
	
	public void config(IAssembler assembler, IDisassembler disassembler, 
			ParametersProcessment parameters) throws Exception {
		VideoAssembler videoAssembler = (VideoAssembler) assembler;
		VideoDisassembler videoDisassembler = (VideoDisassembler) disassembler;
		this.videoController = new VideoController();
		this.videoController.config(videoAssembler,	videoDisassembler, parameters);		
	}
	
	public void run () {
		try {
			System.out.println("Vai realizar ajuste elastico do video....");
			this.videoController.run();
		} catch (Exception e) {
			System.out.println("Erro ao disparar evento - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void setFactor(Factor rate) {
		this.videoController.setStretchRate(rate.getValue());
	}

	public void stop() {
		this.videoController.stopProcessing();
	}	

	/** 
	 * Adiciona novo listener do evento de finalizacao do ajuste.
	 * @param l listener a ser adicionado 
	 */	
	public void addTimescaleListener (TimescaleFinishedListener l) {
        this.videoController.addTimescaleListener(l);
    }	

	public IDisassembler getDisassembler() {
		return this.videoController.getDisassembler();
	}	
		
	public FinalReport getReport() {
		return this.videoController.getReport();
	}

	public void addInstantListener(TimescaleInstantListener listener) {
		this.videoController.addAnchorListener(listener);
	}
	


}
