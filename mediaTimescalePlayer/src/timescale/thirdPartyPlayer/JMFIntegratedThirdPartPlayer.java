package timescale.thirdPartyPlayer;

import java.awt.Component;

import javax.media.Player;

public abstract class JMFIntegratedThirdPartPlayer 
     implements IIntegratedThirthPartyPlayer, IThirdPartyPlayer {
	
	protected Player player;
	
	public JMFIntegratedThirdPartPlayer() {
	}	

	
	public JMFIntegratedThirdPartPlayer(Player player) {
		this.player = player;
	}
	

	/** 
	 * Retorna componente grafico em que do player.
	 * @return componente grafico em que do player
	 */	
	public synchronized Component getControlPanelComponent () {
		Component c = null;
		if (player.getControlPanelComponent() != null) {
			c = player.getControlPanelComponent();
		}		
		return c;
	}

	public synchronized Component getVisualComponent() {
		Component c = null;
		if (player.getVisualComponent() != null) {
			c = player.getVisualComponent();
		}		
		return c;
	}
	
	public double getMediaDurationInSeconds() {
		return this.player.getDuration().getSeconds();
	}
	
	public double getMediaTimeInSeconds() {
		return this.player.getMediaTime().getSeconds();
	}
	
	public Player getPlayer() {
	  return player;
	}
	
	/** 
	 * Libera o player de previa exibicao de midia.
	 */		
	public synchronized void stop() {
        if (this.player!=null) {
        	this.player.stop();
        	this.player.deallocate();
        	this.player.close();
        	this.player = null;
        }
	}	
	
	public void close() {
		this.player.close();
	}
	
	public boolean equals (Object o) {
		Player arg = ((JMFIntegratedThirdPartPlayer) o).player;
		return this.player.equals(arg);
	}
		
}
