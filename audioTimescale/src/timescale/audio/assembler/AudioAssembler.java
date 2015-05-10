/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/03/2005)
 */

package timescale.audio.assembler;

import timescale.audio.format.generalFormat.Frame;
import timescale.audio.util.constants.GeneralFormatConstants;
import util.data.ContentType;
import util.io.InputTools;

/**
 * Essa classe extrai frames a partir de um subsistema de entrada.
 */
public class AudioAssembler implements IAudioAssembler {
	
	protected GenericAudioAssembler assembler;
	
	/** 
	 * Controi instancia da classe.
	 * @param inputTools fonte de dados de entrada
	 */
	public AudioAssembler (GenericAudioAssembler assembler) {
		this.assembler = assembler;
	}
	
	public GeneralFormatConstants getConstants() {
	    return this.assembler.getConstants();
	}
	
	public Frame createCompleteFrame() throws Exception {
		return this.assembler.createCompleteFrame();
	}

	public byte[] getInitialBitsWithoutFrames() throws Exception {
		return this.assembler.getInitialBitsWithoutFrames();
	}
	
	public byte[] extractTail() throws Exception {
		return this.assembler.extractTail();
	}
	
	public ContentType getType () {
		return this.assembler.getType();
	}
	
	public InputTools getInputTools() {
		return this.assembler.getInputTools();
	}
}
