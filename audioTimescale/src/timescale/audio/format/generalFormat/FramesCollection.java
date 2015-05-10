/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (25/02/2005)
 */

package timescale.audio.format.generalFormat;

import java.util.*;

import timescale.data.ElementID;

/**
 * Essa classe representa uma colecao de frames de audio.
 */
public class FramesCollection {
	
	private Vector frames;
	
	
	/** 
	 * Controi instancia da classe.
	 */		
	public FramesCollection() {
		this.frames = new Vector();	
	}	
	
	/**
	 * Retorna elemento do index especificado.
	 * Esse index não é necessariamente equivalente ao identificador do frame. 
	 * @param index index do elemento procurado
	 * @return frame procurado
	 */
	public Frame elementAt (int index) {
		return (Frame) this.frames.elementAt(index);
	}

	/**
	 * Retorna elemento com identificador especificado. 
	 * @param idFrame identificador do elemento procurado
	 * @return frame procurado
	 */	
	public Frame elementAtById (ElementID idFrame) {
		Frame f = null;
		int index = this.indexInCollection(idFrame);
		if (index != -1) {
			f = this.elementAt(index); 
		}
		return f;
	}
	
	/**
	 * Retorna tamanho da colecao de frames.
	 * @return tamanho da colecao
	 */
	public int size () {
		return this.frames.size();
	}

	/**
	 * Adiciona novo frame ao final da coleção. 
	 * @param frame novo frame a ser adicionado
	 */
	public void add(Frame frame) {
		frames.add(frame);	
	}

	/**
	 * Adiciona novo frame na colecao no index especificado.
	 * @param index index onde frame deve ser inserido 
	 * @param frame novo frame a ser adicionado
	 */
	public void add(int index, Frame frame) {
		this.frames.add(index,frame);
	}

	/**
	 * Retorna index do frame na colecao a partir de seu identificador
	 * @param idFrame identificador do frame procurado
	 * @return index do frame na colecao ou (-1) caso o elemento nao exista na colecao 
	 */
	public int indexInCollection(ElementID idFrame) {
		int index = 0;
		int length = this.frames.size();
		while (index < length) {
			Frame frame = this.elementAt(index);
			if (frame.getId().equals(idFrame)) {
				break; 
			}
			index++;
		}
		if (index==length) {
			index = -1;
		}
		return index;
	}
	
	/**
	 * Verifica se index e valido.
	 * @param index index a ser testado
	 * @return true sse index for valido 
	 */
	public boolean isValidIndex (int index) {
		return (index >= 0 && index < this.frames.size());
	}

	/**
	 * Remove frame da coleção.
	 * @param index do frame a ser removido
	 */
	public void remove (int index) {
		this.frames.remove(index);
	}
	
	/**
	 * Retorna tamanho do dado logico de um frame.
	 * @param index index do frame no conjunto de frames
	 * @return tamanho do dado logico do frame
	 */
	public int lengthLogicalDataInBytes(int index) {
		Frame f = this.elementAt(index);
		int physicalLengthInBytes = f.getPhysicalData().lengthInBytes();		 
		int actualNumberOfBorrowBytes =  f.getNumberOfBorrowBytes();
		int nextNumberOfReserveBytes = 0;
		
		if (index<this.frames.size()-1) {
			nextNumberOfReserveBytes = this.elementAt(index+1).getNumberOfBorrowBytes();	
		}
	 
		int length = physicalLengthInBytes+actualNumberOfBorrowBytes-nextNumberOfReserveBytes; 	
		return length;
	}
	
	/**
	 * Retorna true se e somente se o frame e o ultimo do conjunto de frames.
	 * @param idFrame identificador do frame cuja posição se deseja descobrir
	 * @return true sse o frame indicado é o ultimo do conjunto de frames
	 */
	public boolean isLast(ElementID idFrame) {
		boolean r = false;
		Frame f = this.last();
		if (f!=null && f.getId().equals(idFrame)){
			r = true;
		}
		return r;	
	}

	private Frame last () {
		return (Frame) this.frames.lastElement();	
	}
	
	/**
	 * Retorna um iterador da colecao.
	 * @return iterador da colecao
	 */
	public Iterator iterator () {
		return this.frames.iterator();
	}
}
