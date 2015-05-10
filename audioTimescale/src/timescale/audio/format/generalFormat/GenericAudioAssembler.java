/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/03/2005)
 */

package timescale.audio.format.generalFormat;

import timescale.assembler.IAssembler;
import timescale.audio.format.generalFormat.Frame;
import timescale.audio.format.generalFormat.FrameInvalidException;
import timescale.audio.util.constants.GeneralFormatConstants;
import timescale.data.ElementID;
import util.data.ContentType;
import util.data.EOFException;
import util.data.GrowableByteBuffer;
import util.io.InputTools;

/**
 * Essa classe extrai frames a partir de um subsistema de entrada.
 */
public abstract class GenericAudioAssembler implements IAssembler {
	
	private int indexOfExtractedFrame;
    private byte[] initialData;
	private byte[] tailData;	
	protected int indexInData;

	protected InputTools inputTools;

	protected GeneralFormatConstants constants;
	
	/** 
	 * Controi instancia da classe.
	 * @param inputTools fonte de dados de entrada
	 */
	public GenericAudioAssembler (InputTools inputTools) {
		this.inputTools = inputTools;
		this.indexInData = 0;
		this.indexOfExtractedFrame = 0;
	}
	
	public GeneralFormatConstants getConstants() {
	    return this.constants;
	}
	
	/** 
	 * Retorna bytes existentes antes do inicio dos frames.
	 * @return bytes existentes antes do inicio dos frames
	 */	
	public byte[] getInitialBitsWithoutFrames() throws Exception {
		// Le bytes iniciais do stream (antes de iniciarem os frames)
		GrowableByteBuffer gbbuffer = inputTools.nextStartCode(constants.SYNC_WORD);
		this.initialData = gbbuffer.array();
		return this.initialData;
	}

	/**
	 * Cria um frame de audio. 
	 * @return frame criado. 
	 */	
	public Frame createCompleteFrame() throws Exception {
		Frame frame = null;
		
		try {		
			// Percorre string com os bytes do fluxo coletando frames
			if (inputTools.lookBitsAhead(constants.SYNC_WORD)) {
				frame = this.createFrame();
				ElementID id = new ElementID(this.indexOfExtractedFrame);
				frame.setId(id);
				this.indexOfExtractedFrame++;
			}
		}
		catch (EOFException e) {
//			System.out.println("EOF do input tools");
		}
		catch (FrameInvalidException e) {
			//Ocorreu um erro!!
			//TODO: Procurar próximo Synch... pode dar um erro no meio...			
			//Precisa percorrer até o final se der erro ou não
			System.out.println("Erro no meio da leitura do arquivo - Frame inválido");
			System.out.println("******* indexInData=" + indexInData);			
		}
		
		return frame;
	}

	/**
	 * Extrai e retorna tag do final do fluxo.
	 * @return tag do final do stream.
	 */
	public byte[] extractTail() throws Exception {
		//Recupera dado do final do stream
		GrowableByteBuffer gbbuffer = inputTools.getTail();
		this.tailData = gbbuffer.array();
		return this.tailData;
	}
	
	/**
	 * Cria e retorna um novo quadro. 
	 * @return um novo quadro
	 */	
	protected abstract Frame createFrame() throws Exception;

	
	public ContentType getType () {
		return this.inputTools.getType();
	}
	
	public InputTools getInputTools() {
		return this.inputTools;
	}
}
