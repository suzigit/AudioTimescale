/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (30/11/2005)
 */

package timescale.facade;

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.data.Factor;
import timescale.data.ParametersProcessment;
import timescale.event.TimescaleFinishedListener;

public interface ElementaryStreamFacade extends Runnable {

	public String getStreamID();
	
	public IDisassembler getDisassembler();
	
	public void config (IAssembler extractor, IDisassembler disassembler,
			ParametersProcessment parameters) throws Exception; 
	
	public void setFactor(Factor rate) throws Exception;
	
	public void setFinalInstant(double finalInstantInOriginalStream);
	
	public void addTimescaleListener (TimescaleFinishedListener l);
	
	public boolean isFinished();
	
}
