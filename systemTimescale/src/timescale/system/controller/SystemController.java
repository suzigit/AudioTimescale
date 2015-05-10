/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (28/11/2005)
 */

package timescale.system.controller;

import java.util.Iterator;

import timescale.system.data.ControllerData;
import timescale.system.data.ElementaryStreams;
import timescale.system.util.Constants;
import timescale.data.Factor;
import timescale.data.ParametersProcessment;
import timescale.data.RunResult;
import timescale.event.TimescaleFinishedEvent;
import timescale.event.TimescaleFinishedListener;
import timescale.facade.ElementaryStreamFacade;


public class SystemController implements Runnable, TimescaleFinishedListener {
	
	private ElementaryStreams streams;
	private ParametersProcessment parameters;
	private ControllerData controllerData;
	private double actualInstantInOriginalStream = 0;
	
	public SystemController(ElementaryStreams streams, ParametersProcessment parameters) {
		this.streams = streams;
		this.parameters = parameters;
		this.controllerData = new ControllerData();
	}
	
	public void run() {
		this.streams.waitForInitialization();
		System.out.println("comecou processamento");
		try { 	
			
			while (this.streams.mustContinue() && this.parameters.getAlive()) {
		
				System.out.println("Ciclo controller");
				Iterator streams = this.streams.getAliveStreams();
				if (streams.hasNext()) {
					actualInstantInOriginalStream += Constants.INTERMEDIA_ANALYSIS_INTERVAL;
				}
				while (streams.hasNext()) {
					ElementaryStreamFacade esFacade = 
						(ElementaryStreamFacade) streams.next();
					
					this.controllerData.addStream(esFacade.getStreamID());
					
					//Seta fator
					setFactor(esFacade);
					
					//Seta instante final para processamento
					esFacade.setFinalInstant(actualInstantInOriginalStream);
					
					esFacade.addTimescaleListener(this);
					
					Thread t1 = new Thread(esFacade);
					t1.start();	
										
				}
				System.out.println("Controller esperando threads");
				
				this.controllerData.waitForThreads();
				this.controllerData.clearThreads();				
				this.streams.setThereWasProcessment(true);
				System.out.println("Controller acordou");
			}    	
			
			System.out.println("Terminou processamento");     	
		}
		catch (Exception e) {
			System.out.println("Exception in systemcontroller:" +e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*
	 * A cada intervalo de sincronismo, verificar o tempo de cada fluxo no final
	 * do intervalo.
	 * Processadores nao precisam enviar fator obtido, somente o tempo no fluxo
	 * original relativo ao intervalo processado.
	 */	
	//TODO: esse metodo nao suporta mudanca de fator, pois considera o tempo desde do inicio do fluxo e nao da ultima mudanca
	private void setFactor(ElementaryStreamFacade esFacade) throws Exception {
		RunResult result = this.controllerData.getResult(esFacade.getStreamID());
		Factor f = null;
		if (result!=null) {
			double actualInstantInProcessedStream = result.getFinalInstantInProcessedStream();
			double actualInstantInOriginalStream = 
				(this.actualInstantInOriginalStream - Constants.INTERMEDIA_ANALYSIS_INTERVAL);
			
			
			//Fator atingido pela facade
			double dObtainedF = actualInstantInProcessedStream /actualInstantInOriginalStream;
			
			
			long n = Math.round(actualInstantInOriginalStream/Constants.INTERMEDIA_ANALYSIS_INTERVAL);			
			
			//Essa equacao considera o numero de intervalos do qual o fator atual foi obtido 
			/*Ex.: Se nos dois primeiros intervalos, obter fator 0.87, sendo que o fator original foi 0.9 
			 * no terceiro intervalo sera necessario eh necessario aplicar fator igual a:
			 * 2*0.87+f=3*0.9
			 * f=3*0.9-2*0.87
			 */
			double dNewF = ((n+1)*this.parameters.getFactor().getValue())-(n*dObtainedF);
			
			//Arredonda com 2 casas 
			dNewF = dNewF*10000;
			dNewF = Math.round(dNewF);
			dNewF = dNewF/10000;							
			
			f = new Factor(dNewF);
			
			//Nao pode mudar de corte para insercao e vice-versa
			if (f.getMode()!=this.parameters.getFactor().getMode()) {
				f = new Factor();
			}
		}
		else {
			f = this.parameters.getFactor();
		}
			
		esFacade.setFactor(f);
	}
	
	public void actionPosTimescale(TimescaleFinishedEvent event) 
	throws Exception {
		RunResult result = event.getResult();
		ElementaryStreamFacade facade = result.getFacade();
		
		this.controllerData.addResult(facade.getStreamID(), result);
		this.controllerData.setEndOfThread(facade.getStreamID());	
	}

}
