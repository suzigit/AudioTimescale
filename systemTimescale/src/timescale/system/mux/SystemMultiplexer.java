/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (28/11/2005)
 */

package timescale.system.mux;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import timescale.data.ParametersProcessment;
import timescale.event.TimescaleFinishedEvent;
import timescale.event.TimescaleFinishedListener;
import timescale.system.assembler.SystemDisassembler;
import timescale.system.data.ElementaryStreams;
import timescale.system.metadata.CollectionPESPacket;
import timescale.system.metadata.AVSystemPESPacket;
import timescale.system.metadata.SystemPESPacket;
import util.data.EOFException;

/**
 * Essa classe devera multiplexar pacotes PES de varios Fluxos Elementares.
 * Atualmente, ela apenas dar saida aos pacotes PES que estao prontos, 
 * recuperando-os do buffer outputCollectionPes de ElementaryStreams 
 * 
 * @author Suzana Mesquita
 *
 */
public class SystemMultiplexer implements Runnable {

	private SystemDisassembler disassembler;
	private ElementaryStreams streams;
	private ParametersProcessment parameters;
	private Set listeners = new HashSet();
	
	public SystemMultiplexer(SystemDisassembler disassembler, ElementaryStreams streams, 
			ParametersProcessment parameters) {
		this.disassembler = disassembler;
		this.streams = streams;
		this.parameters = parameters;
	}
	
	public SystemDisassembler getDisassembler() {
		return this.disassembler;
	}
	
	public void run() {
		try {
			long nextIDToMux = 0;
			this.streams.waitForProcessment();
			System.out.println("Comecou Mux");
			
			try {				
				while (this.streams.mustContinue() && this.parameters.getAlive()) {
					System.out.println("Mux em acao com num= " + nextIDToMux);
					
					//Recupera todos os fluxos elementares
					Iterator itCollectionPES = this.streams.getCloneOutputStreams().iterator();

					boolean wasPESFound = false;
					while (!wasPESFound && itCollectionPES.hasNext()) {
						//Para cada fluxo elementar
						CollectionPESPacket collectionPES = (CollectionPESPacket) itCollectionPES.next();
						if (collectionPES.hasOneFinishedPES()) {
							SystemPESPacket pes = collectionPES.getFirstElement();

							//Achou PES procurado
							if (pes.getId()==nextIDToMux) {
								extractFirstPES(collectionPES);
								wasPESFound = true;
								nextIDToMux++;
							}
						}
					}
					
					//Espera haver novo processamento					
					if (!wasPESFound) {
						System.out.println("Mux foi dormir");
						this.streams.setThereWasProcessment(false);
						this.streams.waitForProcessment();
						System.out.println("Mux acordou");
					}																
				}
			}
			catch (EOFException e) {
				
			}
			
	    	this.disassembler.finish();
	    	this.fireTimescaleEvent();
	    	System.out.println("Fim normal do mux");    	
		}
		catch (Exception e) {
			System.out.println("Erro no mux: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void extractFirstPES(CollectionPESPacket collectionPES) throws EOFException, Exception {
		SystemPESPacket pes = collectionPES.getFirstElement();		
		byte[] data = null;
		
		//TODO: somente para contemplar fato de alguns fluxos elementares (como video)
		//ainda nao estar sendo processado
		if (pes.wasProcessed()) {
			data = ((AVSystemPESPacket) pes).toProcessedBytes();
		}
		else {
			data = pes.toOriginalBytes();
		}
		collectionPES.removeFirstPES();		
		this.disassembler.output(data);
	}
	
	public void addTimescaleListener (TimescaleFinishedListener l) {
		this.listeners.add(l);	
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
