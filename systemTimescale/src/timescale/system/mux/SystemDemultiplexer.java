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

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.audio.assembler.GenericAudioAssembler;
import timescale.audio.controller.AudioTimescaleFactory;
import timescale.data.ParametersProcessment;
import timescale.facade.ElementaryStreamFacade;
import timescale.system.assembler.SystemAssembler;
import timescale.system.data.ElementaryStreams;
import timescale.system.elementaryStreams.AudioESAssembler;
import timescale.system.elementaryStreams.AudioESDisassembler;
import timescale.system.elementaryStreams.AudioESTimescaleFacade;
import timescale.system.elementaryStreams.OtherESTimescaleFacade;
import timescale.system.metadata.CollectionPESPacket;
import timescale.system.metadata.SystemPESPacket;
import timescale.system.metadata.SystemPackHeader;
import util.data.ContentType;
import util.io.FileOutputTools;


public class SystemDemultiplexer implements Runnable {
	
	private SystemAssembler assembler;
	private ElementaryStreams streams;
	private ParametersProcessment parameters;
	
	public SystemDemultiplexer(SystemAssembler assembler, ElementaryStreams streams, 
			ParametersProcessment parameters) {
		this.assembler = assembler;
		this.streams = streams;
		this.parameters = parameters;
	}
	
	public void run() {		
		try {
			long generatorPesIdentifier = 0;
			boolean endOfStream = false;
			FileOutputTools fot = new FileOutputTools("testedemux.mpeg");
			
			while (!endOfStream && this.assembler.hasMorePacks() && this.parameters.getAlive()) {
				
				SystemPackHeader packHeader = this.assembler.getPack();
				//System.out.println("-montou pack!");
				boolean firstPes = true;
				
				while (!endOfStream && this.assembler.hasMorePES()) {
					SystemPESPacket pes = this.assembler.getPES();										
					
					pes.setId(generatorPesIdentifier);
					generatorPesIdentifier++;
										
					
					System.out.print("-montou pes de num=" + pes.getId() + " com tam="+ pes.getOriginalElementaryStreamLength());
					if (pes.isAudio()) {
						System.out.println("  de AUDIO");	
					}
					else {
						
						this.streams.makeShortCircuit(pes.getStreamIdentifier());
						
						if (pes.isVideo()) {
							System.out.println("  de VIDEO");
						}
						else {
							System.out.println("  de OUTRO TIPO");
						}
					}
					
					
					if (firstPes) {
						pes.setPack(packHeader);
						firstPes=false;
					}	
					
					fot.output(pes.toOriginalBytes());
			
					if (!this.streams.containStream(pes.getStreamIdentifier())) {			
						this.streams.outputData(pes);
						handleNewStream(pes);
						
						//Indica que processamento pode iniciar
						this.streams.setInitialization(true);
					}
					else {
						this.streams.outputData(pes);					
					}
															
					
					//Se final do fluxo
					if (this.assembler.isFinished()) {
						System.out.println("VAI SAIR DEMUX: ENCONTROU FIM DA LEITURA!!!!!!!!!!!");
						endOfStream = true;
					}
								
					//Se agora vem um pack -> deve sair do conjunto de PES
					if (this.assembler.hasMorePacks()) {
						break;
					}

				}
			}
			
			fot.finish();
			this.assembler.finish();
			this.streams.finishDemux();
			System.out.println("Fim demux");
			
		}
		catch (Exception e) {
			System.err.println("Erro no demux: " + e.getMessage());
			e.printStackTrace();
		}			
	}


	public void handleNewStream(SystemPESPacket pes) throws Exception {
		
		String streamID = pes.getStreamIdentifier();
		ElementaryStreamFacade facade = null;
		
		if (pes.isAudio()) {
			//Assembler
			AudioTimescaleFactory factory = new AudioTimescaleFactory();
			ContentType ct = new ContentType(ContentType.ID_MPEG_AUDIO_BC);			
			GenericAudioAssembler genericAudioAssembler = 
				factory.getElementaryAssembler(null, ct);			
			CollectionPESPacket collectionPES = this.streams.getInputCollectionPES(streamID);			
			IAssembler assembler = 
				new AudioESAssembler(collectionPES, genericAudioAssembler);
						
			//Disassembler
			collectionPES = this.streams.getOutputCollectionPES(streamID);
			IDisassembler disassembler = new AudioESDisassembler(collectionPES);
			
			//Facade
			facade = new AudioESTimescaleFacade(streamID);
			ParametersProcessment newParameters = (ParametersProcessment) parameters.clone();
			facade.config(assembler,disassembler,newParameters);
			this.streams.addStream(streamID, facade);
			System.out.println("Demux: Adicionou um audio");			
		}
		
		else {		
			//TODO: fazer video e dados direito!!
			facade = new OtherESTimescaleFacade(streamID);
			this.streams.addStream(streamID, facade);		
			System.out.println("Demux: Adicionou um tipo que nao eh audio");		
		}
	}
	
}
