/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (09/09/2005)
 */

package timescale.event;

public interface TimescaleInstantListener {

	public void receiveUpdatedValueAnchor(TimescaleInstantEvent event);
}
