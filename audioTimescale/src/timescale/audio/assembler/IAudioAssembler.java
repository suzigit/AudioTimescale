/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (06/02/2006)
 */
 
package timescale.audio.assembler;

import timescale.assembler.IAssembler;
import timescale.audio.format.generalFormat.Frame;
import timescale.audio.util.constants.GeneralFormatConstants;
import util.io.InputTools;

public interface IAudioAssembler extends IAssembler {

	public InputTools getInputTools();
	
	public Frame createCompleteFrame() throws Exception;
	
	public GeneralFormatConstants getConstants();
	
	public byte[] getInitialBitsWithoutFrames() throws Exception;
	
	public byte[] extractTail() throws Exception;
}
