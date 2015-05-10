/*
 * Created on 05/10/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package videoGui;

import javax.swing.JOptionPane;

/**
 * @author Sergio Cavendish
 */
public class UpdateThread extends Thread {

	VideoGUI videoGUI = VideoGUI.instance();
	private int type;
	private int sleepTime = 2000;
	private boolean end = false;
	private long initialTime;
	private long currentTime;

	public UpdateThread(int panelType) {
		type = panelType;
	}
	
	public UpdateThread(long initial){
		initialTime = initial;
		
	}

	public void run() {
		while (!end) {
			currentTime = System.currentTimeMillis();
			videoGUI.updateGUI((currentTime - initialTime));
			try {
				sleep(sleepTime);
			} catch (InterruptedException exception) {
				JOptionPane.showMessageDialog(
					null,
					exception.toString(),
					"Update Panel Error",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void setEnd(){
		end = true;
	}
}
