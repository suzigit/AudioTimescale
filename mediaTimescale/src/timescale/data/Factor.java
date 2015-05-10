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
 * Essa classe representa uma taxa de ajuste elastico.
 */
public class Factor {
	
	private double scaleFactor;

	
	public Factor () {
		this(1);
	}	
	
	/** 
	 * Controi instancia da classe.
	 * @param scaleRate taxa de ajuste solicitada
	 */		
	public Factor (double scaleRate) {
		setFactor(scaleRate);
	}
		
	public void setFactor(double scaleRate) {
		this.scaleFactor = scaleRate;
	}
	
	/** 
	 * Retorna valor da taxa de ajuste
	 * @return valor da taxa de ajuste
	 */		
	public double getValue() {
		return scaleFactor;
	}
	
	/** 
	 * Retorna constante indicando se ajuste deve aumentar ou diminuir fluxo
	 * @return constante indicando se ajuste deve aumentar ou diminuir fluxo
	 */		
	public int getMode () {
		int mode;		
		if (scaleFactor > 1) {
			mode = Constants.TimescaleOperation.INSERT;
		}
		else {						
			mode = Constants.TimescaleOperation.CUT;
		}		
		return mode;
	}

	/** 
	 * Retorna taxa de ajuste indicada
	 * @return taxa de ajuste indicada
	 */		
	public double getScaleFactor () {
		return this.scaleFactor;
	}
	
	/** 
	 * Retorna taxa de processamento a ser aplicada
	 * Representa a percentagem a ser ajustada 
	 * Varia entre 0 e 1
	 * @return taxa de processamento a ser aplicada
	 */			
	public double getProcessingRate () {
		double processingRate;
		if (scaleFactor > 1) {
			//Quero que arquivo dure 110% do tempo -> quero inserir 10%
			processingRate = scaleFactor-1;
		}
		else {						
			//Quero que arquivo dure 80% do tempo -> quero cortar 20% 
			processingRate = 1-scaleFactor;
		}		
		return processingRate;
	}
	
}
