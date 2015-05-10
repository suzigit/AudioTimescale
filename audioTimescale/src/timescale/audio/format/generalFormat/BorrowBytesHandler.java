/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (20/07/2005)
 */

package timescale.audio.format.generalFormat;

public interface BorrowBytesHandler {

	/**
	 * Retorna numero de bytes emprestados de frames anteriores.
	 * @return numero de bytes emprestados de frames anteriores.
	 */	
	public int getNumberOfBorrowBytes();	
	
	/**
	 * Modifica quantidade de bytes emprestados de frames anteriores.
	 * @param deltaInBytes numero de bytes emprestados a ser adicionado
	 */
	public void incrementNumberOfBorrowBytes(int deltaInBytes);
	
	/**
	 * Retorna numero maximo de bytes que pode pedir emprestado.
	 * @return numero maximo de bytes que pode pedir emprestado
	 */	
	public int maxNumberOfBorrowedBytes();
	
	/**
	 * Indica que bytes emprestados de frames anteriores sao invalidos.
	 */		
	public void invalidateBorrowBytes();
	
	/**
	 * Retorna validade dos bytes emprestados de frames anteriores.
	 * @return validade dos bytes emprestados de frames anteriores
	 */		
	public boolean isValidBorrowBytes();
	
	public Object clone();

}
