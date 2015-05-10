/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (20/09/2005)
 */

package timescale.remoteApplication;

import timescale.backendgui.GUIController;
import timescale.backendgui.GUIUpdater;
import timescale.frontendgui.TimescaleAudioFrame;
import timescale.thirdPartyPlayer.JMFDataSourcePlayer;
import util.io.InputTools;

public class TimescaleCaller extends Thread {

	private InputTools inputTools;
	private GUIController controller;

	public TimescaleCaller(InputTools inputTools) throws Exception {
		this.inputTools = inputTools;
		//TODO: ajeitar!!!!!!!!!!!
		GUIUpdater guiUpdater = new GUIUpdater(new TimescaleAudioFrame(500,500),100,100);
		this.controller = new GUIController(guiUpdater);
	}
		
	public void run() {
		try {
			callTimescale();		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("erro no player " + e.getMessage());
		}
	}
	
	public void callTimescale() throws Exception {				
/*		
		JMFDataSourcePlayer player = new JMFDataSourcePlayer();
		player.setInput(inputTools);
		player.prepare();
		player.play();
*/		
		JMFDataSourcePlayer player = new JMFDataSourcePlayer();
		double rate = 1.1;
		this.controller.play(inputTools, rate, null, player);	
	}
	
/*	
	public static void main(String args[]) throws Exception {
		//produtor
		ControllerOutputTools outputTools = new ControllerOutputTools();
		FileInputStream fileInputStream = new FileInputStream ("E:/MediasFiles/Audio/ACasa.mp2");
		ByteOutputTools b = (ByteOutputTools) outputTools.getOutputStream();
		ContentType contentType = new ContentType(ContentType.MPEG_BC_AUDIO_CONTENT_TYPE);		
		b.setContentType(contentType);
		
		//producao de dados
		int total = 0;
		byte bytes[] = new byte[1000];
		while (total!=-1) {
			total = fileInputStream.read(bytes, 0, bytes.length);
			if (total>0) {
				b.output(bytes, total);
			}			
		}
		fileInputStream.close();
		outputTools.finish();
		System.out.println("FIM ESCRITA");

		//consumidor
		//CircularInputTools inputTools = (CircularInputTools) b.getInputTools();
		
		/*
		FileOutputTools fileOutputTools = new FileOutputTools ("E:/MediasFiles/Audio/testttttttttttt.mp2");
		total = 0;
		bytes = new byte[1000];
		while (total!=-1) {
			total = inputTools.read(bytes, 0, bytes.length);
			if (total>0) {
				fileOutputTools.output(bytes, total);
			}
			if (total<1000) {
				System.out.println("TA TERMINANDO");
			}
		}
		fileOutputTools.finish();			

		Test test = new Test(b.getInputTools());		
		test.run();
		
		System.out.println("FIM TOTAL"); 
	}
*/	
}
