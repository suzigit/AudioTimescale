package timescale.frontendgui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import timescale.event.PlayChangingEvent;
import timescale.event.PlayChangingListener;
import timescale.thirdPartyPlayer.JMFIntegratedThirdPartPlayer;


public class PlayWindowManager implements PlayChangingListener {

    private Vector windows = new Vector();
    
	public void actionPosCreateNewPlayer (PlayChangingEvent event) {
		JMFIntegratedThirdPartPlayer tpPlayer = event.getPlayer();
		PlayWindow pw = new PlayWindow(tpPlayer);
		windows.addElement(pw);
	}
    
	public void actionPosRealizeNewPlayer(PlayChangingEvent event) {
		JMFIntegratedThirdPartPlayer tpPlayer = event.getPlayer();
		PlayWindow pw = find(tpPlayer);
	    pw.initialize();
	    pw.setVisible(true);
	}
	
    private PlayWindow find(JMFIntegratedThirdPartPlayer player) {
    	for (int i = 0; i < windows.size(); i++) {
    		PlayWindow pw = (PlayWindow)windows.elementAt(i);
    	    if (pw.tpPlayer.equals(player))
    		return pw;
    	}
    	return null;
    }
    
	public class PlayWindow extends Frame {

		private static final long serialVersionUID = 1L;
		JMFIntegratedThirdPartPlayer tpPlayer;
	
		public PlayWindow (JMFIntegratedThirdPartPlayer tpPlayer) {
		    this.tpPlayer = tpPlayer;
		    
	        this.addWindowListener( new WindowAdapter() {
	            public void windowClosing(WindowEvent e ) {
	                close();
	            }
	        });		

		}
		
		public void initialize() {
		    add(new PlayPanel(tpPlayer));
		}	
				
		public void close() {
			tpPlayer.close();
		    setVisible(false);
		    dispose();
		}
	
		public void addNotify() {
		    super.addNotify();
		    pack();
		}		
    }    
    
    // Interface gráfica
    class PlayPanel extends Panel {

		private static final long serialVersionUID = 1L;
		Component vc, cc;
	
		PlayPanel(JMFIntegratedThirdPartPlayer tpPlayer) {
		    setLayout(new BorderLayout());
		    if ((vc = tpPlayer.getVisualComponent()) != null)
		    	add("Center", vc);
		    if ((cc = tpPlayer.getControlPanelComponent()) != null)
		    	add("South", cc);
		}
		
		public Dimension getPreferredSize() {
		    int w = 0, h = 0;
		    if (vc != null) {
			Dimension size = vc.getPreferredSize();
			w = size.width;
			h = size.height;
		    }
		    if (cc != null) {
			Dimension size = cc.getPreferredSize();
			if (w == 0)
			    w = size.width;
			h += size.height;
		    }
		    if (w < 160)
			w = 160;
		    return new Dimension(w, h);
		}
    }    
}
