/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (14/06/2004)
 */

package timescale.data;

import java.util.*;

/**
 * Essa classe armazena dados de todo o processamento de um fluxo de midia.
 */
public class FinalReport extends RateReport {
	
	private Collection framesProcessmentInfo;
	private FrameProcessmentInfo lastFramesProcessmentInfo;
	private InstantsSet anchors;
	private long processmentTime;
	private double frameTimeDuration; 

	
	/** 
	 * Controi instancia da classe.
	 */
	public FinalReport (InstantsSet anchors) {
		this.framesProcessmentInfo = new ArrayList();
		this.anchors = anchors;
	}
	
	public double actualInstantInOriginalStream() {
		return this.getNumberOfReadFrames()*getFrameDuration();
	}
	
	public double actualInstantInProcessedStream () {
		return this.getNumberOfWrittenFrames()*getFrameDuration();
	}
	
	public double actualDeltaInstant() {
		return this.getNumberOfInsertedMinusNumberOfRemovedFrames()*getFrameDuration();
	}
	
	public double getFrameDuration () {
		return this.frameTimeDuration;
	}

	public void setFrameDuration (double t) {
		this.frameTimeDuration = t;
	}
	

	
	/** 
	 * Informa que o frame desse id foi processado.
	 * @param id id do frame processado
	 */	
	public void putIdProcessedFrame(double id, int action) {
		FrameProcessmentInfo fpi = new FrameProcessmentInfo ((int)id, action);
		this.lastFramesProcessmentInfo = fpi;
		this.framesProcessmentInfo.add(fpi);
		this.incrementNumberOfProcessedFrames(action);	
	}
	


	/** 
	 * Retorna conjunto de quadros processados.
	 * @return conjunto de quadros processados
	 */		
	public FrameProcessmentInfo[] getArrayProcessedFrames() {		
		Iterator iterator = this.framesProcessmentInfo.iterator();
		FrameProcessmentInfo[] fpi = new FrameProcessmentInfo[this.framesProcessmentInfo.size()];
		for (int i=0; i<fpi.length; i++){
			FrameProcessmentInfo id = (FrameProcessmentInfo) iterator.next();
			fpi[i] = id;
		}		
		return fpi;
	}
	
	public FrameProcessmentInfo getLastProcessedFrame() {
		return this.lastFramesProcessmentInfo;
	}

	
	/** 
	 * Registra tempo de processamento.
	 * @param processmentTime valor do tempo de processamento
	 */
	public void setProcessmentTime (long processmentTime) {
		this.processmentTime = processmentTime;
	}

	/** 
	 * Recupera tempo de processamento.
	 * @return Time valor do tempo de processamento
	 */	
	public long getProcessmentTime () {
		return this.processmentTime;
	}
		
	public InstantsSet getAnchors() {
		return this.anchors;
	}
}
