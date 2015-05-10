/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (24/08/2005)
 */

package timescale.facade;

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.data.FinalReport;
import timescale.data.Factor;
import timescale.data.ParametersProcessment;
import timescale.event.TimescaleFinishedListener;
import timescale.event.TimescaleInstantListener;


public interface MediaTimescaleFacade extends Runnable {
	
	public void config (IAssembler extractor, IDisassembler disassembler,
			ParametersProcessment parameters) throws Exception; 

	public IDisassembler getDisassembler();
	
	public void addTimescaleListener (TimescaleFinishedListener l);
	
	public void addInstantListener(TimescaleInstantListener listener);
	
	public void stop();	
	
	public void setFactor(Factor rate) throws Exception;
	
	public FinalReport getReport();
	
	public void run();
		
	
}
