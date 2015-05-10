package timescale.audio.format.mpeg;

import timescale.audio.assembler.GenericAudioAssembler;
import util.data.ContentType;
import util.io.InputTools;

/**
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (13/07/2005)
 */
public abstract class MPEGBCAssembler extends GenericAudioAssembler {

	/** 
	 * Controi instancia da classe.
	 * @param inputTools fonte de dados de entrada
	 */
	public MPEGBCAssembler (InputTools inputTools) {
	    super (inputTools);		
	}
	
	/** 
	 * Retorna formato de dados do fluxo.
	 * @return formato de dados do fluxo
	 */		
	public static ContentType getStaticType() {
		return new ContentType(ContentType.ID_MPEG_AUDIO_BC);
	}
		    
}
