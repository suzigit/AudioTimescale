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

import timescale.audio.format.generalFormat.DataField;
import timescale.data.ElementID;
import timescale.metadata.IMediaMetadata;
import util.data.GrowableByteBuffer;


/**
 * Essa classe representa um frame de audio.
 */
public class Frame implements Cloneable {
	
	private ElementID id;
	private Header header;
	private DataField physicalData;
	
	private IMediaMetadata metadata;

	/** 
	 * Controi instancia da classe.
	 * @param header header do frame
	 * @param physicalData dado que pertence ao frame 
	 */	
	public Frame (Header header, DataField physicalData) {
		this.header = header;
		this.physicalData = physicalData; 
	}

	/** 
	 * Controi instancia da classe.
	 */	
	private Frame () {
		
	}

	/**
	 * Retorna identificador do frame. 
	 * @return identificador do frame
	 */
	public ElementID getId() {
		return id;
	}
	
	/**
	 * Modifica identificador do frame. 
	 * @param id novo identificador do frame
	 */	
	public void setId(ElementID id) {
		this.id = id;				
	}
	
	/**
	 * Retorna dados que pertencem fisicamente a frame.
	 * @return dados que pertencem fisicamente a frame
	 */
	public DataField getPhysicalData() {
		return this.physicalData;
	}

	/**
	 * Retorna header do frame.
	 * @return header do frame
	 */
	public Header getHeader () {
		return this.header;
	}

	/**
	 * Retorna numero de bytes emprestados de frames anteriores.
	 * @return numero de bytes emprestados de frames anteriores.
	 */	
	public int getNumberOfBorrowBytes () {
		int number = 0;
		number = this.getHeader().getBorrowBytesHandler().getNumberOfBorrowBytes();
		return number;	
	}	

	/**
	 * Modifica quantidade de bytes emprestados de frames anteriores.
	 * @param deltaInBytes numero de bytes emprestados a ser adicionado
	 */		
	public void incrementNumberOfBorrowBytes(int deltaInBytes) {
		this.getHeader().getBorrowBytesHandler().incrementNumberOfBorrowBytes(deltaInBytes);						
	}

	/**
	 * Retorna numero maximo de bytes que pode pedir emprestado.
	 * @return numero maximo de bytes que pode pedir emprestado
	 */		
	public int maxNumberOfBorrowedBytes () {		
		return this.getHeader().getBorrowBytesHandler().maxNumberOfBorrowedBytes();
	}	
	
	/**
	 * Indica que bytes emprestados de frames anteriores sao invalidos.
	 */	
	public void invalidateBorrowBytes() {
		this.getHeader().getBorrowBytesHandler().invalidateBorrowBytes();
	}

	/**
	 * Retorna validade dos bytes emprestados de frames anteriores.
	 * @return validade dos bytes emprestados de frames anteriores
	 */		
	public boolean isValidBorrowBytes() {
		return this.getHeader().getBorrowBytesHandler().isValidBorrowBytes();
	}	
	
	public byte[] toBytesWithoutMetadata() {
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();
		
		byte[] bytesHeader = this.getHeader().toBytes();
		bytesFrame.put(bytesHeader);				
		
		byte[] bytesPhysicalData = this.getPhysicalData().toBytes();
		
		/* Esse teste é necessário porque encontrei alguns arquivos MP3 com
		 * último frame sem dados
		 */
		if (bytesPhysicalData!=null) {
			bytesFrame.put(bytesPhysicalData);		
		}
		
		bytesFrame.trimToSize();
		
		byte[] bytes = bytesFrame.array();
		
		return bytes;
	}	
	
	/**
	 * Retorna bytes que representam objeto.
	 * @return bytes que representam objeto
	 *//*
	public byte[] toBytes() {
		
		byte[] bytes = this.toBytesWithoutMetadata();
		
		//Adiciona metadados
		if (this.metadata!=null) {
			byte[] bMetadata = this.metadata.toBytes();
			int indexBeginMetadata = this.metadata.getIndexMetadata();
			byte[] newBytesFrame = new byte[bytes.length + bMetadata.length];
			for (int i=0; i<indexBeginMetadata; i++) {
				newBytesFrame[i] = bytes[i];
			}
			for(int i=indexBeginMetadata; i<indexBeginMetadata+bMetadata.length; i++) {
				newBytesFrame[i] = bMetadata[i-indexBeginMetadata];
			}
			for (int i=indexBeginMetadata+bMetadata.length; i<newBytesFrame.length; i++) {
				newBytesFrame[i] = bytes[i-bMetadata.length];
			}
			bytes = newBytesFrame;
		}	
		
		return bytes;
	}*/
		
	/**
	 * Retorna clone do objeto.
	 * @return clone do objeto
	 */		
	public Frame cloneWithoutMetadata() {
		Frame f = new Frame();
		f.setId(new ElementID(-1));
		f.header = (Header) this.getHeader().clone();		
		f.physicalData = (DataField) this.physicalData.clone();
		return f;
	}
 	
	public IMediaMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(IMediaMetadata metadata) {
		this.metadata = metadata;
	}
}
