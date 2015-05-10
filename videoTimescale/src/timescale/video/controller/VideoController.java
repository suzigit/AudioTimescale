/*
 * Created on 05/10/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import timescale.assembler.IDisassembler;
import timescale.data.FinalReport;
import timescale.data.ParametersProcessment;
import timescale.event.TimescaleFinishedEvent;
import timescale.event.TimescaleFinishedListener;
import timescale.event.TimescaleInstantListener;
import timescale.video.assembler.VideoAssembler;
import timescale.video.assembler.VideoDisassembler;
import timescale.video.processor.Buffer;
import timescale.video.processor.VideoProcessor;
import timescale.video.statistics.LastSequenceStrategy;
import timescale.video.statistics.Stats;
import timescale.video.statistics.StatsCollector;
import timescale.video.statistics.StatsStrategy;

/**
 * @author Sergio Cavendish
 */
public final class VideoController {

	private VideoProcessor videoProcessorThread;
	private StatsCollector collector;
	private VideoAssembler inputThread;
	private VideoDisassembler outputThread;
	private Buffer inputSharedBuffer, outputSharedBuffer;
//	private boolean taskFinished;
	private int inputBufferSize, inputSharedBufferSize, outputSharedBufferSize;
	
	private List listeners = new ArrayList();	
	private ParametersProcessment parameters;
	

	public VideoController() {

		// Criação do coletor de dados estatísticos. Define a estratégia segundo a qual a estatística
		// será efetuada.
		LastSequenceStrategy strategy = new LastSequenceStrategy();
		collector = StatsCollector.instance(strategy);
		
	  // Valores default.
		inputBufferSize = 32000;
		inputSharedBufferSize = outputSharedBufferSize = 2;
	}
	
	public void setSizes(int inputBufferSize, int inputSharedBufferSize, int outputSharedBufferSize) {
		this.inputBufferSize = inputBufferSize;
		this.inputSharedBufferSize = inputSharedBufferSize;
		this.outputSharedBufferSize = outputSharedBufferSize;		
	}
	
	public void config(VideoAssembler assembler, VideoDisassembler disassembler, 
			ParametersProcessment parameters) throws Exception {
		this.inputThread = assembler;
		this.outputThread = disassembler;
		this.parameters = parameters;
		inputSharedBuffer = new Buffer(inputSharedBufferSize);
		outputSharedBuffer = new Buffer(outputSharedBufferSize);
		
		inputThread.setBuffer(inputSharedBuffer);
		
		videoProcessorThread = new VideoProcessor(inputSharedBuffer, 
				outputSharedBuffer, this.parameters.getReport());
		
		outputThread.setBuffer(outputSharedBuffer);
		
		setStretchRate(this.parameters.getFactor().getValue());		
	}

	public void run() {

		outputThread.setController(this);		
		
		inputThread.start();
		videoProcessorThread.start();
		outputThread.start();
	}
	
	public void addAnchorListener(TimescaleInstantListener listener) {
		this.videoProcessorThread.addAnchorListener(listener);
	}
	
	public IDisassembler getDisassembler() {
		return this.outputThread;
	}
	
	/**
	 * @param strategy
	 */
	public void setStatsStrategy(StatsStrategy strategy) {
		collector.setStatsStrategy(strategy);
	}

	public FinalReport getReport() {
		return this.parameters.getReport();
	}	

	/**
	 * @param statsType
	 * @return
	 */
//	TODO: Sergio, de preferencia unifica Stats e Report, podendo extender o FinalReport..
	public Stats getStats(int statsType) {
		Stats stats = collector.getStats(statsType);
		return stats;
	}

	/**
	 * 
	 */
	public void stopProcessing() {
		inputThread.setEnd();
		videoProcessorThread.setEnd();
		outputThread.setEnd();

		inputSharedBuffer.clearBuffer();
		outputSharedBuffer.clearBuffer();
		collector.clearAllStats();		
	}

	/**
	 * @param rate
	 */
	public void setStretchRate(double rate) {
		videoProcessorThread.setDesiredRate(rate);
	}

	public double getActualStretchRate() {
		return videoProcessorThread.getActualStretchRate();
	}

	
	public void setTaskFinished() throws Exception {
		stopProcessing();
		fireTimescaleEvent();
	}
	
	/** 
	 * Adiciona novo listener do evento de finalizacao do ajuste.
	 * @param l listener a ser adicionado 
	 */	
	public void addTimescaleListener (TimescaleFinishedListener l) {
        listeners.add(l);
    }
	
	/** 
	 * Avisa aos listeneres que o evento de finalizacao do ajuste aconteceu.
	 */	
	private void fireTimescaleEvent() throws Exception {
		TimescaleFinishedEvent event = new TimescaleFinishedEvent(parameters);
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext() ) {
            ((TimescaleFinishedListener) iterator.next()).actionPosTimescale(event);
        }
    }
	
}
