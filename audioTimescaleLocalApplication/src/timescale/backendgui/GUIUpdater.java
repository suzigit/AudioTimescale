/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (23/08/2005)
 */

package timescale.backendgui;

import java.awt.Component;
import java.awt.Rectangle;

import timescale.backendgui.PresentationFrame;
import timescale.event.TimescaleFinishedEvent;
import timescale.event.TimescaleFinishedListener;
import timescale.event.TimescaleInstantEvent;
import timescale.event.TimescaleInstantListener;
import timescale.thirdPartyPlayer.IIntegratedThirthPartyPlayer;
import timescale.thirdPartyPlayer.IThirdPartyPlayer;

public class GUIUpdater implements TimescaleFinishedListener, TimescaleInstantListener {
	
	private PresentationFrame frame;
	private int width;
	private int heigth;
	
	private GUIController guiController;
	
	public GUIUpdater (PresentationFrame frame, int width, int heigth) {
		this.frame = frame;
		this.width = width;
		this.heigth = heigth;
	}

	public void setController (GUIController controller) {
		this.guiController = controller;
	}
	
	/** 
	 * Realiza acoes depois da realizacao de um ajuste.
	 * @param event evento de finalizacao do ajuste
	 */			
	public void actionPosTimescale (TimescaleFinishedEvent event) throws Exception {		
		this.frame.getPanelPlayer().removeAll();
		this.frame.getPanelPlayer().repaint();
		String mediaFile = this.guiController.getPlayer().getOutputTools().getName();
        if (mediaFile!=null) {
        	updateResult(mediaFile);        	
            if (event.getParameters().mustPlay()) {
            	updatePlayer();
            }
        }

		//Teste
        /*
        IThirdPartyPlayer thirdPartPlayer = this.guiController.getPlayer().getThirdPartyPlayer();
		if (thirdPartPlayer instanceof IIntegratedThirthPartyPlayer) {
	    	for (int i=0; i<1000; i++) {
	    		double a = ((IIntegratedThirthPartyPlayer) thirdPartPlayer).getMediaTimeInSeconds();
	    		System.out.println("tempo atual=" + a);
	    		
	    		double s = ((IIntegratedThirthPartyPlayer) thirdPartPlayer).getMediaDurationInSeconds();
				System.out.println("tempo de duracao=" + s);
	    		Thread.sleep(1000);
	    	}			
		} 
		*/       
        
	}
	
	private void updateResult (String mediaFile) {
        String s = "Gerou arquivo " + mediaFile + "\n";
        System.out.println(s);
        this.frame.showResult(s);		
	}
	
	private void updatePlayer () throws Exception  {
    	IThirdPartyPlayer thirdPartPlayer = this.guiController.getPlayer().getThirdPartyPlayer();
    	thirdPartPlayer.prepare();
    	thirdPartPlayer.play();
    	showPlayer(thirdPartPlayer);    	    	
	}

	public void showPlayer(IThirdPartyPlayer thirdPartPlayer) {
		if (thirdPartPlayer instanceof IIntegratedThirthPartyPlayer) {    		
    		IIntegratedThirthPartyPlayer integratedPlayer =  (IIntegratedThirthPartyPlayer) thirdPartPlayer;
   	         
    		int delta = 0;
    		Component component = integratedPlayer.getControlPanelComponent();
        	if (component!=null) {
        		delta = 30;
        		component.setSize(this.width,delta);
        		this.frame.getPanelPlayer().add(component);
        	}
        	
        	Component visualComponent = integratedPlayer.getVisualComponent();
        	if (visualComponent!=null) {   		
        		visualComponent.setSize(this.width,heigth-delta);
        		this.frame.getPanelPlayer().add(visualComponent);
        		Rectangle r= visualComponent.getBounds();
        		r.y += delta;
        		visualComponent.setBounds(r);
        	}
    	}
	}
	
	public void receiveUpdatedValueAnchor(TimescaleInstantEvent event) {
		String result = "Mudou ancora " + event.getOriginalInstant() + " por " + event.getUpdatedInstant()+ "\n";
		frame.showResult(result);
	}
}
