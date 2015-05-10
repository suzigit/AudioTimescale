/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (17/01/2005)
 */

package timescale.system.elementaryStreams;

import timescale.audio.assembler.IAudioDisassembler;
import timescale.audio.format.generalFormat.Frame;
import timescale.metadata.IMediaMetadata;
import timescale.system.metadata.CollectionPESPacket;
import timescale.system.metadata.MediaMetadata;
import timescale.system.metadata.AVSystemPESPacket;
import util.io.OutputTools;

public class AudioESDisassembler implements IAudioDisassembler {
	
	/**
	 * Colecao de pacotes PES processados pelo algoritmo de ajuste de audio.
	 * So contem os pacotes PES que ja foram processados e ja estao prontos. 
	 */
	private CollectionPESPacket collectionPes;
	
	
	private AVSystemPESPacket actualPES;

	public AudioESDisassembler (CollectionPESPacket collectionPes) {
		this.collectionPes = collectionPes;
	}
	
	public void output (Frame f) throws Exception {
		
//		System.out.println("##### AudioESDisassembler de frame " + f.getId());	
		
		IMediaMetadata metadata = f.getMetadata();
		if (metadata!=null) {
			int indexMetadata = metadata.getIndexMetadata();
			byte[] data = f.toBytesWithoutMetadata();
			
			byte[] dataBeforeMetadata = new byte[indexMetadata];
			for (int i=0; i<dataBeforeMetadata.length; i++) {
				dataBeforeMetadata[i] = data[i];
			}
			
			//Adiciona dados no pes atual e o adiciona no collectionPES
			if (this.actualPES!=null) {
				this.actualPES.addProcessedData(dataBeforeMetadata);
				this.collectionPes.add(this.actualPES);
			}
									
			AVSystemPESPacket pes = ((MediaMetadata) metadata).getPES();
			this.actualPES = pes;
			
			//Comeca novo pes
			byte[] dataAfterMetadata = new byte[f.getHeader().lengthFrameInBytes()-indexMetadata];
			for (int i=0; i<dataAfterMetadata.length; i++) {
				dataAfterMetadata[i] = data[i+indexMetadata];
			}
			this.actualPES.addProcessedData(dataAfterMetadata);
			
		}
		else {
			//Adiciona dados no pes atual
			byte[] data = f.toBytesWithoutMetadata();
			this.actualPES.addProcessedData(data);
		}
		
	}
	
	
	public void output (byte[] b) throws Exception {
		throw new IllegalArgumentException("Tratar em AudioElementaryStreamDisassembler");
	}
	
	public void finish () throws Exception {
		this.collectionPes.add(this.actualPES);
		this.collectionPes.finish();
		System.out.println("Finish do AudioElementaryStreamDisassembler");
	}
	
	public boolean isFinished() {
		return this.collectionPes.isFinished();
	}
	
	public OutputTools getOutputTools () {
		throw new IllegalArgumentException("Tratar em AudioElementaryStreamDisassembler");
	}

}
