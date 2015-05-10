/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (17/01/2006)
 */

package timescale.audio.assembler;

import timescale.audio.format.generalFormat.Frame;
import util.io.OutputTools;

public class AudioDisassembler implements IAudioDisassembler {
	
	private OutputTools outputTools;
	
	public AudioDisassembler (OutputTools outputTools) {
		this.outputTools = outputTools;
	}

	public void output (Frame f) throws Exception {
		byte[] result = f.toBytesWithoutMetadata();
		this.output(result);
	}
	
	public void output (byte[] b) throws Exception {
		this.outputTools.output(b);
	}

	public void finish () throws Exception {
		this.outputTools.finish();
	}
	
	public boolean isFinished() {
		return this.outputTools.isFinished();		
	}
	
	public OutputTools getOutputTools () {
		return this.outputTools;
	}

	
}
