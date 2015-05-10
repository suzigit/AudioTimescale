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

public class Main {

	private static String URL="file:/E:/MediasFiles/Audio/MP2/MPEG-1/ACasa.mp2";	
	private static int bufferLength = 2000;
	
	public static void main(String args[]) {
		SenderStream sender = new SenderStream(URL);		
		Thread t1 = new Thread(sender);
		t1.run();
		
		ReceiverStream receiver = new ReceiverStream(bufferLength);
		Thread t2 = new Thread(receiver);
		t2.run();
	}
}
