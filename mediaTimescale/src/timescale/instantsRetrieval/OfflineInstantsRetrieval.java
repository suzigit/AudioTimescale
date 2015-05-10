/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (09/09/2005)
 */

package timescale.instantsRetrieval;

import timescale.data.FinalReport;
import timescale.data.FrameProcessmentInfo;
import timescale.util.constants.Constants;

public class OfflineInstantsRetrieval {
	
//	TODO: Lembrar que nao funciona se taxa de amostragem for modificada entre frames
	public static double getTimescaleInstant (FinalReport report, double originalInstant) {

		/* Sendo OF o frame que contem o instante indicado.
		 * 1- Se OF nao foi processado, basta ajustar o instante considerando o numero de frames 
		 * processados que se localizam antes de OF.
		 * 2.Se OF foi processado
		 * - Se INSERCAO, mesmo passo de 1.
		 * - SE CORTE, deve-se retornar o instante inicial do frame sucessor a OF.
		 */		

				//Recupera frame que continha amostra do originalInstant	
				double frameDuration = report.getFrameDuration();
				int originalIDFrame = (int) (originalInstant/frameDuration);
						
				//Calcula quantos frames foram inseridos antes do originalFrame
				int totalInsertedFrames = 0;
				int actionOriginalFrame = Constants.TimescaleOperation.NONE;
				FrameProcessmentInfo fpis [] = report.getArrayProcessedFrames();
				for (int i=0; i < fpis.length; i++) {
					if (fpis[i].getId() <= originalIDFrame) {
						if (fpis[i].getAction()==Constants.TimescaleOperation.INSERT) {
							totalInsertedFrames++;
						}
						else {
							totalInsertedFrames--;
						}
						if (fpis[i].getId() == originalIDFrame) {
							actionOriginalFrame = fpis[i].getAction();
						}
					}		
				}
				
				//Calcula diferenca temporal devido ao ajuste
				double deltaInstant = totalInsertedFrames*frameDuration;
				
				//Calcula novo instante da amostra
				double newInstant;	
				if (actionOriginalFrame == Constants.TimescaleOperation.CUT) {
					int numberPreviousFrames = originalIDFrame+totalInsertedFrames+1;  
					newInstant = numberPreviousFrames*frameDuration;  
				}
				else if (actionOriginalFrame == Constants.TimescaleOperation.INSERT) {
					newInstant = originalInstant + deltaInstant - frameDuration;
				}
				else {
					newInstant = originalInstant + deltaInstant;
				}
				return newInstant;
			}

}
