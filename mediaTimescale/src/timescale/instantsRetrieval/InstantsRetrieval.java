/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (02/09/2005)
 */

package timescale.instantsRetrieval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import timescale.data.FinalReport;
import timescale.data.FrameProcessmentInfo;
import timescale.event.TimescaleInstantEvent;
import timescale.event.TimescaleInstantListener;
import timescale.util.constants.Constants;

//TODO: Lembrar que nao funciona se taxa de amostragem for modificada entre frames
public class InstantsRetrieval {
	
	private FinalReport report;
	private int indexUpdatedAnchorSet = 0;
	private List listeners = new ArrayList();
	
	public InstantsRetrieval (FinalReport report) {
		this.report = report;
	}

	/**
	 * Esse metodo deve ser chamado para todo quadro do fluxo, incusive para os que foram
	 * removidos ou inseridos. Ele levanta um evento se o valor de uma ancora 
	 * deve ser atualizada antes do instante final desse quadro.
	 * 
	 * @param originalIDFrame
	 */
	public void updateNewAnchors (double originalIDFrame) {
		
		//System.out.println("updateANchor =" + originalIDFrame);

		if (this.report.getAnchors()!=null) {
	
			//Busca dados do originalIDFrame no relatorio
			FrameProcessmentInfo last = report.getLastProcessedFrame();
				
			//Calcula instante ate onde pode calcular novas ancoras
			//Esse instante eh o fim do frame em analise			
			double actualInstantNewStream = getActualInstantNewStream (originalIDFrame);
			double actualInstantOriginalStream = (originalIDFrame+1)*report.getFrameDuration();
			
			//Se existe ancora antes de actualInstant, atualiza-as
			double [] originalAnchors = this.report.getAnchors().getOriginalAnchors();
			while (indexUpdatedAnchorSet<originalAnchors.length
					&& originalAnchors[indexUpdatedAnchorSet]<actualInstantOriginalStream) {
				double newAnchor;
				
				//Se o quadro analisado foi processado agora
				if (last!=null && last.getId() == originalIDFrame) { 
					if (last.getAction()==Constants.TimescaleOperation.CUT) {
						newAnchor = actualInstantNewStream;
					}
					else if (last.getAction()==Constants.TimescaleOperation.INSERT) { 
						double deltaInstant = (getTotalOfInsertedFrames()-1) * report.getFrameDuration();
						newAnchor = originalAnchors[indexUpdatedAnchorSet] + deltaInstant;						
					}
					else throw new IllegalArgumentException("operacao invalida");
				}
				else {
					double deltaInstant = getTotalOfInsertedFrames() * report.getFrameDuration();
					newAnchor = originalAnchors[indexUpdatedAnchorSet] + deltaInstant;
				}
				this.setAnchor(newAnchor, indexUpdatedAnchorSet);								
				indexUpdatedAnchorSet++;
			}	
			
		}
	}

	public void updateLastsAnchors () {
		if (this.report.getAnchors()!=null) {
			double [] originalAnchors = this.report.getAnchors().getOriginalAnchors();
			while (indexUpdatedAnchorSet<originalAnchors.length) {
				double deltaInstant = getTotalOfInsertedFrames() * report.getFrameDuration();
				double newAnchor = originalAnchors[indexUpdatedAnchorSet] + deltaInstant;	
				this.setAnchor(newAnchor, indexUpdatedAnchorSet);
				indexUpdatedAnchorSet++;
			}
		}
	}
	
	private int getTotalOfInsertedFrames() {
		return this.report.getNumberOfInsertedMinusNumberOfRemovedFrames();
	}
	
	private double getActualInstantNewStream (double originalIDFrame) {
		int numberPreviousFrames = (int)originalIDFrame+getTotalOfInsertedFrames()+1;
		double actualInstantNewStream = numberPreviousFrames*report.getFrameDuration();
		return actualInstantNewStream;
	}
	
	private void setAnchor (double newAnchor, int index) {
		double[] originalAnchors = this.report.getAnchors().getOriginalAnchors();
		this.report.getAnchors().setNewAnchor(newAnchor,index);
		this.fireTimescaleEvent(originalAnchors[index], newAnchor);
	}
	
	
	public void addAnchorListener (TimescaleInstantListener l) {
        listeners.add(l);
    }

	/** 
	 * Avisa aos listeneres que o evento de finalizacao do ajuste aconteceu.
	 */	
	private void fireTimescaleEvent(double original, double updated) {
		TimescaleInstantEvent event = new TimescaleInstantEvent(original, updated);
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext() ) {
            ((TimescaleInstantListener) iterator.next()).receiveUpdatedValueAnchor(event);
        }
    }
}
