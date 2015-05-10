/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (14/10/2004)
 */

 
package timescale.audio.processor;

import timescale.audio.format.generalFormat.Frame;
import timescale.data.Factor;
import timescale.data.ParametersProcessment;
import timescale.data.RateReport;
import timescale.util.constants.Constants;

/**
 * Essa classe escolhe quais frames de audio devem ser processados
 * para realizar o ajuste elastico desejado.
 *
 */
public class FrameToProcessSelector {

	private ParametersProcessment parameters;

	//A taxa atingida ate o momento
	private RateReport rateReport;
	
	
	/** 
	 * Controi instancia da classe.
	 * @param parameters parametros de processamento
	 */	
	public FrameToProcessSelector (ParametersProcessment parameters) {
		this.parameters = parameters;
		this.rateReport = new RateReport();		
	}
	
	
	/**
	 * Verifica se quadro deve ser processado.
	 * @param frame quadro que se deseja verificar
	 * @return true sse quadro deve ser processado
	 */	
	public boolean mustProcessFrame (Frame frame) {
			
		boolean enable = false;
				
		//Se a taxa a ser atingida é maior do que a atingida até o momento
		if (this.parameters.getFactor().getProcessingRate()>rateReport.getRealRate()) {

			//Não é interessante duplicar frame com número de bytes emprestados inválidos, 
			// pois replicado o erro
			if (frame.isValidBorrowBytes() 
					|| this.parameters.getFactor().getMode()==Constants.TimescaleOperation.CUT) {
				enable = true;
			}			
		}	
		return enable;				 
	}	
	
	/**
	 * Indica que quadro foi processado.
	 * @param f quadro processado
	 */	
	public void markProcessment(Frame f, int action) {
		this.rateReport.incrementNumberOfProcessedFrames(action);
		this.parameters.getReport().putIdProcessedFrame(f.getId().getValue(), action);					
	}	

	/**
	 * Indica que mais um quadro foi lido. 
	 */		
	public void markRead() {
		this.rateReport.incrementNumberOfReadFrames();
		this.parameters.getReport().incrementNumberOfReadFrames();		
	}

	/**
	 * Indica que mais um quadro foi enviado para saida. 
	 */
	public void markWritten() {
		this.rateReport.incrementNumberOfWrittenFrames();
		this.parameters.getReport().incrementNumberOfWrittenFrames();		
	}	
	
	
	/** 
	 * Modifica taxa de ajuste a ser utilizada em ajuste elastico.
	 * @param rate nova taxa de ajuste
	 */			
	public void setRate (Factor rate) {		
		this.parameters.setRate(rate);
		this.rateReport.reset();
	}	
	
}
