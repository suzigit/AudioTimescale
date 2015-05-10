package timescale.system.metadata;

import java.util.Vector;

import timescale.util.constants.Constants;
import util.data.EOFException;


public class CollectionPESPacket {
	
	private String streamIdentifier;
	private Vector collectionPesPacket;
	private boolean isFinished;
	

	private String nome;
	
	public CollectionPESPacket(String nome) {
		this.collectionPesPacket = new Vector();
		this.isFinished = false;
		this.nome = nome;
	}
	
	public synchronized boolean hasOneFinishedPES () {
		return this.collectionPesPacket.size()>0;
	}
	
	public synchronized void add(SystemPESPacket pes) {
		System.out.println("COLLECTIONPES " + this.nome + " : Adicionou pes -total=" + this.collectionPesPacket.size());		
		if (collectionPesPacket.size()>=Constants.BUFFER_PES_LENGTH) {
			try {
				wait();
			}
			catch (Exception e) {}
		}
		this.streamIdentifier = pes.getStreamIdentifier();
		this.collectionPesPacket.add(pes);
		notifyAll();
	}
	
	public String getStreamIdentifier() {
		return this.streamIdentifier;
	}
	
	public void finish() {
		this.isFinished = true;
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
	
	public synchronized SystemPESPacket elementAt(int index) throws EOFException {
		//Se nao tiver index ainda do pes, mandar dormir
		if (isFinished() && !isValidIndex(index)) {
			throw new EOFException();
		}
		while (!isValidIndex(index)) {
			try {
				System.out.println("Collection PES "+ this.nome +" dormiu em elementAT - ID=" + this.streamIdentifier + " inidex=" + index);
				wait();
			}
			catch (Exception e) {}
		}
		return (SystemPESPacket) this.collectionPesPacket.elementAt(index);
	}

	
	public SystemPESPacket getFirstElement() throws EOFException {
		SystemPESPacket pes = (SystemPESPacket) this.elementAt(0);
		return pes;
	}	

	public synchronized void removeFirstPES() throws EOFException {
		SystemPESPacket pes = this.elementAt(0);
		pes.setData(null);
		//System.out.print("COLLECTIONPES  "+ this.nome +" ID=" + this.streamIdentifier +" : remove pes -total antes=" + this.collectionPes.size());
		this.collectionPesPacket.remove(0);
		//System.out.println(" -total depois=" + this.collectionPes.size());
		notifyAll();
	}
	
	public synchronized boolean isValidIndex (int index) {
		return (index >= 0 && index < this.collectionPesPacket.size());
	}	
	
	public synchronized Object clone() {
		CollectionPESPacket clone = new CollectionPESPacket(this.nome);
		for (int i=0; i<this.collectionPesPacket.size(); i++) {
			try {
				clone.add(this.elementAt(i));
			}
			catch (EOFException e) {}
		}
		return clone;
	}
}
