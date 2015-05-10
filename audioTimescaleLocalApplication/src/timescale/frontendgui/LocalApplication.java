/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (17/08/2004)
 */

package timescale.frontendgui;

/**
 * Essa classe instancia a aplicacao de ajuste elastico em um frame local.
 */
public class LocalApplication {

    /** 
     * Ponto de entrada da aplicacao
     */
	public static void main (String args[]) {
		int width = 1000;
		int height = 600;
		TimescaleAudioFrame frame = new TimescaleAudioFrame(width,height);
		frame.setVisible(true);	
	}	

}
