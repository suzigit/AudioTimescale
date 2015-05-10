/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (09/05/2004)
 */

package timescale.audio.processor;

import timescale.audio.format.generalFormat.Frame;
import timescale.audio.format.generalFormat.FramesCollection;
import timescale.data.ElementID;
import timescale.data.Factor;
import timescale.data.ParametersProcessment;
import timescale.event.TimescaleInstantListener;
import timescale.instantsRetrieval.InstantsRetrieval;
import timescale.metadata.IMediaMetadata;
import timescale.util.constants.Constants;


/**
 * Essa classe realiza ajuste elastico em um conjunto de frames de audio.
 */
public class StreamProcessor {

	private PADData pad;
	private FrameToProcessSelector selector;
	private FrameProcessor frameProcessor;
	private ParametersProcessment parameters;
	private InstantsRetrieval anchorsRetrieval;
	
	private ElementID nextIdToProcess;	
	
	/** 
	 * Controi instancia da classe.
	 * @param parameters parametros de processamento
	 */
	public StreamProcessor(ParametersProcessment parameters) {
		this.parameters = parameters;
		this.pad = new PADData();
		this.selector = new FrameToProcessSelector(parameters);
		this.frameProcessor = new FrameProcessor(parameters.getFactor());
		this.anchorsRetrieval = new InstantsRetrieval(parameters.getReport());
		this.nextIdToProcess = new ElementID(0);
	}
	
	public void addAnchorListener (TimescaleInstantListener l) {          
		this.anchorsRetrieval.addAnchorListener(l);
    }	
	
	public boolean processFrame(FramesCollection frames) {
		
		ElementID idFrame = this.nextIdToProcess;
		Frame f = frames.elementAtById(idFrame);
		boolean result = false;
		
		if (f!=null) {

			IMediaMetadata metadata = f.getMetadata();
			//System.out.println("processou nextIdToProcess=" + nextIdToProcess);

			PADProcessor.adjustPAD(frames, pad, idFrame);
	
			//Se for retirar, verifica qual proximo frame
			if (this.parameters.getFactor().getMode() == Constants.TimescaleOperation.CUT) {
				this.nextIdToProcess = ElementID.nextID(this.nextIdToProcess);
			}
	
			boolean isThereProcessment = false;
			if (selector.mustProcessFrame(f)) {	
				isThereProcessment = frameProcessor.tryProcessFrame(frames, f, pad);
			} 
			
  		    /* Atualiza clock de metadados
			 * Se (operacao for de insercao) ou (operacao for de corte e nao aconteceu) -> (nao atualiza)
			 */
			if (metadata!=null) {
				if ((this.parameters.getFactor().getMode() == Constants.TimescaleOperation.INSERT) ||
						(this.parameters.getFactor().getMode() == Constants.TimescaleOperation.CUT
								&& !isThereProcessment)
					) {
					metadata.adjustClock(this.parameters.getReport().actualDeltaInstant());
				}			
				
			}
			

			//Se inseriu, pega frame inserido como proximo
			if (this.parameters.getFactor().getMode() == Constants.TimescaleOperation.INSERT) {
				this.calculateNextIdToProcess(frames);
			}
			
			//Atualiza ancoras
			this.anchorsRetrieval.updateNewAnchors(idFrame.getValue());
			
			// ***** Atualiza report de leitura e escrita - INICIO ****
			if (isThereProcessment) {
				selector.markProcessment(f, this.parameters.getFactor().getMode());												
			}
						
			if (idFrame.isInteger()) {
				// O teste verifica que eh um frame do fluxo original
				// marca que um quadro do fluxo original foi lido
				this.selector.markRead();
			}
			
			if (!isThereProcessment 
					|| (this.parameters.getFactor().getMode()==Constants.TimescaleOperation.INSERT)) {
				//Marca que um quadro vai ser escrito no fluxo de saida
				//Isso ocorre quando nao houve processamento do quadro OU
				//quando houve, mas o processamento eh de duplicacao de quadros
				this.selector.markWritten();
			}		
			// ***** Atualiza report de leitura e escrita - FIM ****
			
									
			result = true;
		}
		return result;
	}
	
	public void calculateNextIdToProcess(FramesCollection frames) {
				
		//Atualiza valor de nextIdToProcess
		int index = frames.indexInCollection(this.nextIdToProcess);
		if (frames.isValidIndex (index+1)) {
			Frame f = frames.elementAt(index+1);
			this.nextIdToProcess = f.getId();
		}
		else {
			this.nextIdToProcess = new ElementID(-1);			
		}			
	}

	public ElementID getNextIDToProcess() {
		return this.nextIdToProcess;
	}
	
	public void finalizeProcessment() {
		this.anchorsRetrieval.updateLastsAnchors();
	}
	
	public void setRate (Factor rate) {	
		//System.out.println("Mudou fator para " + rate.getValue());
		this.selector.setRate(rate);
	}
	
}
