/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (16/12/2005)
 */

package timescale.system.elementaryStreams;

import java.util.Iterator;

import timescale.audio.assembler.GenericAudioAssembler;
import timescale.audio.assembler.IAudioAssembler;
import timescale.audio.format.generalFormat.Frame;
import timescale.audio.util.constants.GeneralFormatConstants;
import timescale.system.metadata.CollectionPESPacket;
import timescale.system.metadata.SystemInputTools;
import timescale.system.metadata.MediaMetadata;
import timescale.system.metadata.AVSystemPESPacket;
import util.data.ContentType;
import util.data.EOFException;
import util.io.InputTools;


public class AudioESAssembler implements IAudioAssembler {
	
	private SystemInputTools io;
	protected GenericAudioAssembler assembler;
		
	public AudioESAssembler (CollectionPESPacket collectionPES, GenericAudioAssembler assembler) {
		this.assembler = assembler;
		this.io = new SystemInputTools(collectionPES);
	}
	
	public Frame createCompleteFrame() throws Exception {
		Frame f = null;
		
		try {
			//Armazena index em que frame iniciou
			int actualByteIndexInBeginning = this.io.getActualByteIndex();
			AVSystemPESPacket actualPesInBeginning = (AVSystemPESPacket) this.io.getActualPES(); 
			
			
			this.io.initCollectedPES();
			f = this.assembler.createCompleteFrame(io);
			
			Iterator collectedPESCollection = this.io.getCollectedPES().iterator();			
			if (collectedPESCollection.hasNext()) {
				
				//TODO: nao estou tratando o fato de um frame ser maior do que um pes.
				AVSystemPESPacket pes = (AVSystemPESPacket) collectedPESCollection.next();
				
				//TESTE
				if (collectedPESCollection.hasNext()) {
					System.err.println("AudioESAssembler: Nao estou tratando isso ainda!! URGENTE");
				}
				
				MediaMetadata metadata = new MediaMetadata(pes);
				int index = actualByteIndexInBeginning;
				if (index!=0) {
					index = actualPesInBeginning.getOriginalElementaryStreamLength() - actualByteIndexInBeginning;
				}
				metadata.setIndexMetadata(index);
				f.setMetadata(metadata);
			}
		
		}
		catch (EOFException e) {
			//Se chegou ate o final do stream, esse metodo retorna null.
		}
		
		if (f!=null) {
			//System.out.println("AudioESAssembler: Criou frame " + f.getId().getValue());
		}
		return f;
	}

	
	public byte[] getInitialBitsWithoutFrames() {
		return null;
	}
	
	public byte[] extractTail() throws Exception {
		return null;
	}
	
	public GeneralFormatConstants getConstants() {
	    return this.assembler.getConstants();
	}
	
	public InputTools getInputTools() {
		return this.io;
	}
	
	public ContentType getType () {
		return this.assembler.getType();
	}	

}
