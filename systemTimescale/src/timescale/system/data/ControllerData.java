package timescale.system.data;

import java.util.HashMap;
import java.util.Iterator;

import timescale.data.RunResult;
import util.functions.Semaphore;

public class ControllerData {
	
	//Uma colecao de (streamID, StreamReadyInfo)
	private HashMap hashReady;
	
	//Uma colecao de (streamID, RunResult)	
	private HashMap hashResults;
	
	private Semaphore waitThreads;
		
	public ControllerData() {
		this.hashReady = new HashMap();
		this.hashResults = new HashMap();
		this.waitThreads = new Semaphore();
	}
	
	public void clearThreads() {
		this.hashReady.clear();
		this.waitThreads.setInitiated(false);
	}	
	
	public void addStream(String streamID) {
		StreamReadyInfo sReady = new StreamReadyInfo(false);
		this.hashReady.put(streamID, sReady);
	}
	
	public void waitForThreads() {
		if (hashReady.size()>0) {
			this.waitThreads.waitForInitialization();	
		}
	}
	
	public void setEndOfThread(String streamID) {
		StreamReadyInfo sr = (StreamReadyInfo) this.hashReady.get(streamID);
		sr.isReady = true;
		
		//Se todos estiverem true, deve setar waitThreads
		boolean isAllReady = true;
		Iterator iterator = this.hashReady.values().iterator();
		while (iterator.hasNext()) {
			sr = (StreamReadyInfo) iterator.next();
			if (!sr.isReady) {
				isAllReady = false;
			}
		}
		this.waitThreads.setInitiated(isAllReady);		
	}

	public void addResult(String streamID, RunResult result) {
		this.hashResults.put(streamID, result);
	}	
	
	public RunResult getResult(String streamID) {
		return (RunResult) this.hashResults.get(streamID);
	}
	
	
	class StreamReadyInfo {
		private boolean isReady;
		
		public StreamReadyInfo (boolean b) {
			this.isReady = b;
		}
	}
	
}
