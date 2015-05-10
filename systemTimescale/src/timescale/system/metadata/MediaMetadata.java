/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (16/12/2005)
 */

package timescale.system.metadata;

import timescale.metadata.IMediaMetadata;



public class MediaMetadata implements IMediaMetadata {
	
	private AVSystemPESPacket pes;
	
	/*
	 * Posicao do pes no fluxo de bytes da estrutura que o contem.
	 */
	private int indexMetadata;
	
	public MediaMetadata(AVSystemPESPacket pes) {
		this.pes = pes;
	}
	
	public AVSystemPESPacket getPES() {
		return this.pes;
	}

	public int getIndexMetadata() {
		return indexMetadata;
	}

	public void setIndexMetadata(int indexMetadata) {
		this.indexMetadata = indexMetadata;
	}
	
	public void adjustClock(double deltaTime) {
		//System.out.println("Ajustar clock em " + deltaTime);
		this.pes.adjustClock(deltaTime);
	}
	
	
}
