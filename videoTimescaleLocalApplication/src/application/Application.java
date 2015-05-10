/*
 * Created on 05/10/2004
 * A versão 3.0 implementa o ajuste através da análise de cada seqüência em separado, 
 * decidindo quais os quadros devem ser replicados ou descartados antes de entregá-los
 * à thread de saída. Assim, o processador foi implementado como uma thread.
 */
package application;

import timescale.video.controller.*;
import videoGui.VideoGUI;

/**
 * @author Sergio Cavendish
 */
public class Application {
	
	public static void main(String[] args) {
		VideoController controller = new VideoController();
		VideoGUI videoGui = VideoGUI.instance("MPEG-2 TimeScale Processor", controller);
	}
}
