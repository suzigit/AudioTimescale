/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (16/12/2005)
 */

package timescale.metadata;

public interface IMediaMetadata {
	
	public int getIndexMetadata();
	
	public void setIndexMetadata(int indexMetadata);
	
	public void adjustClock(double deltaTime);

}
