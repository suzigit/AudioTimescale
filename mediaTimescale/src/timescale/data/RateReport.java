/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (15/04/2005)
 */
 
package timescale.data;

import timescale.util.constants.Constants;

/**
 * Essa classe armazena dados do processamento de um fluxo de midia
 * para uma taxa de ajuste especifica. 
 */
public class RateReport {

	private int numberOfReadFrames;
	private int numberOfWrittenFrames;
	private int numberOfInsertedFrames;
	private int numberOfRemovedFrames;	

	/** 
	 * Controi instancia da classe.
	 */	
	public RateReport () {
		reset();
	}
	
	/**
	 * Inicia contagem dos dados de estatistica.
	 */	
	public void reset() {
		this.numberOfReadFrames = 0;
		this.numberOfWrittenFrames = 0;
		this.numberOfInsertedFrames = 0;
		this.numberOfRemovedFrames = 0;	
	}
	
	
	/**
	 * Incrementa numero de frames ja lidos.
	 */
	public void incrementNumberOfReadFrames() {
		this.numberOfReadFrames++;
	}

	/**
	 * Retorna numero de frames ja lidos.
	 * @return numero de frames ja lidos.
	 */	
	public int getNumberOfReadFrames() {
		return this.numberOfReadFrames;
	}

	/**
	 * Incrementa número de frames ja escritos.
	 */
	public void incrementNumberOfWrittenFrames() {
		this.numberOfWrittenFrames++;
	}
	
	/**
	 * Retorna numero de frames ja enviados para saida. 
	 * @return numero de frames ja ja enviados para saida
	 */
	public int getNumberOfWrittenFrames() {
		return this.numberOfWrittenFrames;
	}

	/**
	 * Incrementa numero de frames processados.
	 */		
	public void incrementNumberOfProcessedFrames(int action) {
		if (action==Constants.TimescaleOperation.CUT) {
			this.numberOfRemovedFrames++;
		}
		else if (action==Constants.TimescaleOperation.INSERT) {
			this.numberOfInsertedFrames++;
		}
	}	
	
	
	public int getNumberOfInsertedMinusNumberOfRemovedFrames() {
		return this.numberOfInsertedFrames - this.numberOfRemovedFrames;
	}	
	
	/**
	 * Retorna numero de frames processados.
	 * @return numero de frames processados
	 */	
	public int getNumberProcessedFrames() {
		return this.numberOfInsertedFrames + this.numberOfRemovedFrames;
	}		
	
	/** Retorna a taxa efetivamente atingida pelo processamento 
	 *  do fluxo.
	 * @return taxa efetivamente atingida pelo processamento do fluxo 
	 */
	public double getRealRate() {
		double rate = 0;
		if (this.getNumberOfReadFrames()!=0) {
			rate = ((double) this.getNumberProcessedFrames())/this.getNumberOfReadFrames(); 
		}
		return rate;
	}	
	
	
}
