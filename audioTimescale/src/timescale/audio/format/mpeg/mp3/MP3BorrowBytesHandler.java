/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (20/07/2005)
 */

package timescale.audio.format.mpeg.mp3;

import timescale.audio.format.generalFormat.BorrowBytesHandler;

public class MP3BorrowBytesHandler implements BorrowBytesHandler {
	
	private MP3SideInformation sideInfo;
	
	public MP3BorrowBytesHandler(MP3SideInformation sideInfo) {
		this.sideInfo = sideInfo;
	}
	
	/**
	 * Retorna numero de bytes emprestados de frames anteriores.
	 * @return numero de bytes emprestados de frames anteriores.
	 */	
	public int getNumberOfBorrowBytes() {
		return this.sideInfo.getNumberOfBorrowBytes();
	}
	
	/**
	 * Modifica quantidade de bytes emprestados de frames anteriores.
	 * @param deltaInBytes numero de bytes emprestados a ser adicionado
	 */
	public void incrementNumberOfBorrowBytes(int deltaInBytes) {
		this.sideInfo.incrementNumberOfBorrowBytes(deltaInBytes);
	}
	
	/**
	 * Retorna numero maximo de bytes que pode pedir emprestado.
	 * @return numero maximo de bytes que pode pedir emprestado
	 */	
	public int maxNumberOfBorrowedBytes() {
		return this.sideInfo.maxNumberOfBorrowedBytes();
	}
	
	/**
	 * Indica que bytes emprestados de frames anteriores sao invalidos.
	 */		
	public void invalidateBorrowBytes() {
		this.sideInfo.isValidBorrowBytes();
	}
	
	/**
	 * Retorna validade dos bytes emprestados de frames anteriores.
	 * @return validade dos bytes emprestados de frames anteriores
	 */		
	public boolean isValidBorrowBytes() {
		return this.sideInfo.isValidBorrowBytes();
	}
	
	public MP3SideInformation getSideInformation() {
		return this.sideInfo;
	}
	
	public Object clone () {
		MP3SideInformation sideInfo = (MP3SideInformation) this.getSideInformation().clone();
		return new MP3BorrowBytesHandler(sideInfo);
	}
	
}
