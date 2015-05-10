/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (28/11/2005)
 */
package timescale.system.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import timescale.facade.ElementaryStreamFacade;
import timescale.system.metadata.CollectionPESPacket;
import timescale.system.metadata.SystemPESPacket;
import util.functions.Semaphore;


public class ElementaryStreams {
	
	//Uma colecao de (streamID, ElementaryStreamFacade)
	private Hashtable hashFacades;
	
	/** Uma colecao de (streamID, CollectionPES)
	 *  As CollectionsPES sao criadas no demux, atualizadas por chamada do 
	 *  demux a essa classe e finalizadas quando demux termina.
	 */
	private Hashtable inputHashCollectionPES;

	/** Uma colecao de (streamID, CollectionPES)
	 *  As CollectionsPES por essa classe, atualizadas e finalizadas pelo 
	 *  dissambler de fluxos elementares.
	 * 	Estrutura de onde o mux pega os dados.
	 */
	private Hashtable outputHashCollectionPES;
	
	private Collection shortCircuit;

	private Semaphore initiated;
	private Semaphore thereWasProcessment;
	private Object synchFacades;
	private Object synchCollectionsPES;
	
	private boolean isDemuxAlive;
		
	
	public ElementaryStreams() {
		this.hashFacades = new Hashtable();
		this.inputHashCollectionPES = new Hashtable();
		this.outputHashCollectionPES = new Hashtable();
		
		this.shortCircuit = new HashSet();
		
		this.initiated = new Semaphore();
		this.thereWasProcessment = new Semaphore();
		
		this.synchFacades = new Object();
		this.synchCollectionsPES = new Object();
		
		this.isDemuxAlive = true;
	}
	
	public Iterator getAliveStreams() {
		synchronized (this.synchFacades) {
			Collection aliveStreams = new ArrayList();
			Iterator it = this.hashFacades.values().iterator();
			while (it.hasNext()) {
				ElementaryStreamFacade facade = (ElementaryStreamFacade) it.next();
				if (!facade.isFinished()) {
					aliveStreams.add(facade);
				}
			}
			return aliveStreams.iterator();			
		}
	}
	
	public void addStream(String streamID, ElementaryStreamFacade e) {
		synchronized (this.synchFacades) {
			this.hashFacades.put(streamID, e);	
		}		
	}
	
	public boolean containStream(String streamID) {
		synchronized (this.synchFacades) {
			return this.hashFacades.containsKey(streamID);	
		}
	}
	
	public boolean containAnyAliveStream() {
		synchronized (this.synchFacades) {
			boolean result = false;
			Iterator it = this.hashFacades.values().iterator();
			while (it.hasNext()) {
				ElementaryStreamFacade facade = (ElementaryStreamFacade) it.next();
				if (!facade.isFinished()) {
					result = true;
				}
			}
			return result;			
		}
	}
	
	//	----------------------------------------------

	//Precisa retornar clone para nao problema de concorrencia
	public Collection getCloneOutputStreams() {
		synchronized (this.synchCollectionsPES) {
			Iterator iterator = this.outputHashCollectionPES.values().iterator();
			
			Collection newCol = new ArrayList();
			while (iterator.hasNext()) {
				newCol.add(iterator.next());
			}
			
			return newCol;			
		}
	}
	
	/**
	 * Esse metodo recebe verifica se ja existe uma collection do streamID em alguma 
	 * CollectionPES. Se sim, acrescenta esse pes la. Se nao, cria uma nova collection.
	 */
	public void outputData (SystemPESPacket pes) throws Exception {
		synchronized (this.synchCollectionsPES) {
			CollectionPESPacket cp;
			CollectionPESPacket outputCp;
			if (this.inputHashCollectionPES.containsKey(pes.getStreamIdentifier())) {
				cp = (CollectionPESPacket) this.inputHashCollectionPES.get(pes.getStreamIdentifier());
				outputCp = (CollectionPESPacket) this.outputHashCollectionPES.get(pes.getStreamIdentifier());
			}
			else {
				cp = new CollectionPESPacket("entrada");
				this.inputHashCollectionPES.put(pes.getStreamIdentifier(), cp);
				
				outputCp = new CollectionPESPacket("saida");
				this.outputHashCollectionPES.put(pes.getStreamIdentifier(), outputCp);
			}
			
			if (!this.shortCircuit.contains(pes.getStreamIdentifier())) {
				cp.add(pes);
			}
			else {
				outputCp.add(pes);
			}			
		}		
	}
	
	
	public CollectionPESPacket getInputCollectionPES(String streamID) {
		synchronized (this.synchCollectionsPES) {
			return (CollectionPESPacket) this.inputHashCollectionPES.get(streamID);	
		} 
	}
	
	public CollectionPESPacket getOutputCollectionPES(String streamID) {
		synchronized (this.synchCollectionsPES) {
			return (CollectionPESPacket) this.outputHashCollectionPES.get(streamID);	
		} 
	}
	
	
	public void finishDemux() throws Exception {
		synchronized (this.synchCollectionsPES) {
			Iterator values = this.inputHashCollectionPES.values().iterator();
			while (values.hasNext()) {
				CollectionPESPacket cp = (CollectionPESPacket) values.next();			
				cp.finish();
			}
			this.isDemuxAlive = false;			
		}
	}
	
	/**
	 * Pode ser utilizado pelo controlador e multiplexador para verificar se
	 * deve continuar a execucao de suas threads.
	 * 
	 * @return true sse deve continuar execucao.
	 */
	public boolean mustContinue() {
		boolean value = true;
		if (!this.isDemuxAlive && !this.containAnyAliveStream()) {
			value = false;
		}
		return value;
	}
	
	
	public synchronized void makeShortCircuit(String streamID) {
		synchronized (this.synchCollectionsPES) {
			this.shortCircuit.add(streamID);	
		}
	}
	

	//----------------------------------------------
	
	public void waitForInitialization() {
		this.initiated.waitForInitialization();
	}
	
	public void setInitialization(boolean b) {
		this.initiated.setInitiated(b);
	}
	
	public void waitForProcessment() {
		this.thereWasProcessment.waitForInitialization();
	}	

	public void setThereWasProcessment(boolean b) {
		this.thereWasProcessment.setInitiated(b);
	}
	

}
