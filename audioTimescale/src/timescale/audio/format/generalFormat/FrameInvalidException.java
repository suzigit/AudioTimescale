/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/07/2004)
 */
 
package timescale.audio.format.generalFormat;

/**
 * Essa classe indica que existe algo errado no stream de audio.
 */
public class FrameInvalidException extends Exception {

	private static final long serialVersionUID = 1L;
	public static final String MSG_INVALID_HEADER_EXCEPTION = "Header inválido";
	public static final String MSG_INVALID_BORROW_BYTES = "Número de bytes emprestados inválido";

	public static int INVALID_HEADER_EXCEPTION = 0;
	public static int INVALID_BORROW_BYTES = 1;
	
	private int type;
	
	//indica ultimo frame que estava correto
	private int lastValidFrame;
	
	/** 
	 * Controi instancia da classe.
	 * @param type tipo de excecao 
	 */		
	public FrameInvalidException (int type) {
		this.type = type;
	}
	
	/**
	 * Modifica valor do atributo lastValidFrame.
	 * @param lastValidFrame novo valor do atributo lastValidFrame
	 */
	public void setLastValidFrame (int lastValidFrame) {
		this.lastValidFrame = lastValidFrame;
	}
	
	/**
	 * Retorna atributo lastValidFrame.
	 * @return valor do atributo lastValidFrame.
	 */
	public int getLastValidFrame() {
		return this.lastValidFrame;
	}
	
	/** 
	 * Retorna tipo da excecao ocorrida.
	 * @return tipo da excecao ocorrida
	 */		
	public int getType () {
		return this.type;
	}
	
	/**
	 * Retorna informacoes sobre a excecao.
	 * @return descricao da excecao e ultimo quadro corretamente criado 
	 */
	public String getMessage () {
		String msg = "";
		switch (type) {
			case 0: 
				msg = MSG_INVALID_HEADER_EXCEPTION;
				break;
			case 1:
				msg = MSG_INVALID_BORROW_BYTES;
		}
		msg += "\nUltimo frame válido-> " + this.getLastValidFrame();
		return msg;
	}
	
}
