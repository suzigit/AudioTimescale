/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (14/10/2004)
 */
 
package timescale.audio.assembler;

import timescale.assembler.IDisassembler;
import timescale.audio.format.generalFormat.Frame;
import util.io.OutputTools;

/**
 * Essa classe recupera os bytes dos frames criados.
 */
public interface IAudioDisassembler extends IDisassembler {

	public void output (Frame f) throws Exception;
	
	public void output (byte[] b) throws Exception;

	public void finish () throws Exception;
	
	public boolean isFinished();
	
	public OutputTools getOutputTools ();
	
	

}
