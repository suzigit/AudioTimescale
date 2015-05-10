/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (23/09/2004)
 */

package timescale.backendgui;

import javax.swing.JPanel;

public interface PresentationFrame {
	public JPanel getPanelPlayer ();
	public void showResult(String s);
}
