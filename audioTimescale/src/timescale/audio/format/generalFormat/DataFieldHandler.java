/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (08/06/2004)
 */

package timescale.audio.format.generalFormat;

import util.functions.Functions;

/**
 * Essa classe extrai informações de dados dentro de frames.
 */
public class DataFieldHandler extends AMainDataHandler {
	

	/** 
	 * Controi instancia da classe.
	 * @param frames frames cujos main data se deseja manipular
	 */    
	public DataFieldHandler (FramesCollection frames) {
		super (frames);	
	}


	/**
	 * Recupera string formada dos caracteres encontrados partindo da tuplaindex. 
	 * Formara string de tamanho, NO MÁXIMO, igual a length. Pode ser diferente no
	 * caso de nao existir mais bits a serem recuperados.
	 * @param ti index de inicio de onde dados serão buscados, inclusive
	 * @param lengthInBytes tamanho MAXIMO da string a ser retornada
	 * @return string de tamanho length formada dos caracteres encontrados 
	 * partindo da tuplaindex.   
	 */
	public byte[] getData (AMainDataHandler.TuplaIndex ti, int lengthInBytes) {
		int indexFrame = ti.getIndexFrameInCollection();
		Frame f = this.frames.elementAt(indexFrame);
				
		byte[] collectedData = new byte[lengthInBytes];
		int indexInCollectedData = 0;
		
		byte[] data = f.getPhysicalData().substring(ti.getIndexPhysicalDataInBytes());
		boolean hasNext = true;
		
		while (lengthInBytes > data.length) {
			lengthInBytes -= data.length;
			indexFrame++;
			
			Functions.appendArray (collectedData, data, indexInCollectedData);
			indexInCollectedData += data.length;
			
			if (this.frames.isValidIndex(indexFrame)) {
				f = this.frames.elementAt(indexFrame);
				data = f.getPhysicalData().getData();
			}
			else {
				hasNext = false;
				break;							
			}
		}

		if (hasNext) {
			Functions.appendArray (collectedData, data, indexInCollectedData, lengthInBytes);
			indexInCollectedData += lengthInBytes;
		}
		
		if (indexInCollectedData!= collectedData.length) {
			byte[] temp = new byte[indexInCollectedData];
			Functions.appendArray (temp, collectedData, 0, indexInCollectedData);
			collectedData = temp;
		}
		
		return collectedData;		
	}


	/**
	 * Modifica dados fisicos dos frames a partir do index especificado 
	 * utilizando dado tambem passado como argumento.
	 * Caso acabem os bits dos frames, os dados que sobrarem do newData serao ignorados.
	 * @param ti index inicial a ser modificado
	 * @param byteNewData dado a ser utilizado para modificar os bits 
	 */
	public void setData (AMainDataHandler.TuplaIndex ti, byte[] byteNewData) {	
		int length = 0;
		int indexFrame = ti.getIndexFrameInCollection();
		int indexPhysicalDataInBytes = ti.getIndexPhysicalDataInBytes();
		
		Frame frame = this.frames.elementAt(indexFrame);
		byte[] physicalData = frame.getPhysicalData().substring(indexPhysicalDataInBytes);
				
		while (byteNewData.length > 0) {
			if (physicalData.length < byteNewData.length) {
				length = physicalData.length; 
			}
			else {
				length = byteNewData.length;
			}
			
			frame.getPhysicalData().setData(byteNewData, indexPhysicalDataInBytes, length);
			
			//Atualiza bytes, retirando alguns bytes
			int lengthTemp = byteNewData.length - length;
			byte[] temp = new byte[lengthTemp];
			for (int i=0; i<lengthTemp; i++) {
				temp[i] = byteNewData[i+length]; 
			}
			byteNewData = temp;
						
			indexFrame++;
			if (this.frames.isValidIndex(indexFrame)) {
				frame = this.frames.elementAt(indexFrame);				
				physicalData = frame.getPhysicalData().getData();					
			}
			else {
				break;
			}
			indexPhysicalDataInBytes = 0; 
		}				
	}


	
	/**
	 * Retorna novo index considerando um incremento de delta na TuplaIndex
	 * passada como argumento.
	 * @param ti index que deve ser incrementado
	 * @param delta incremento dado a tupla em bytes
	 * @return index incrementado por delta  
	 */
	public AMainDataHandler.TuplaIndex incrementTuplaIndex (AMainDataHandler.TuplaIndex ti, int delta) {
	    AMainDataHandler.TuplaIndex newTi = ti;
		if (delta > 0) {
			newTi = this.plusIncrementTuplaIndex(ti, delta);
		}
		else if (delta < 0) {
			newTi = this.minusIncrementTuplaIndex(ti, -delta);
		}
		return newTi;			
	}

	//Incrementa, no máximo, delta
	//Pode ser menos se não houver dados para isso.
	private AMainDataHandler.TuplaIndex 
			plusIncrementTuplaIndex (AMainDataHandler.TuplaIndex ti, int delta) {
	
		int indexFrame = ti.getIndexFrameInCollection();
		int indexPhysicalDataInBytes = ti.getIndexPhysicalDataInBytes();
		
		Frame f = this.frames.elementAt(indexFrame);
		byte[] data = f.getPhysicalData().substring(indexPhysicalDataInBytes);

		while (delta > data.length) {
			delta -= data.length;
			indexFrame++;
			indexPhysicalDataInBytes = 0;
			if (this.frames.isValidIndex(indexFrame)) {
				f = this.frames.elementAt(indexFrame);
				data = f.getPhysicalData().getData();
			}
			else {
				indexFrame--;
				f = this.frames.elementAt(indexFrame);
				delta = f.getPhysicalData().lengthInBytes();
				break; 
			}						
		}
		
		indexPhysicalDataInBytes += delta;
		AMainDataHandler.TuplaIndex newTi = new AMainDataHandler.TuplaIndex (indexFrame, indexPhysicalDataInBytes);
		return newTi;			
	}

	
	// Esse método não preve a situação de não ter dado suficiente de modo a 
	// não conseguir satisfazer decrementar o ti
	private AMainDataHandler.TuplaIndex 
			minusIncrementTuplaIndex (AMainDataHandler.TuplaIndex ti, int delta) {		
	
		Frame f = null;
		int indexFrame = ti.getIndexFrameInCollection();
		int indexPhysicalDataInBytes = ti.getIndexPhysicalDataInBytes();
		
		//O último index é excluive
		if (indexPhysicalDataInBytes == 0) {			 
			indexFrame--;
			f = this.frames.elementAt(indexFrame);
			indexPhysicalDataInBytes = f.getPhysicalData().lengthInBytes();
		}
		
		if (f==null) {
			f = this.frames.elementAt(indexFrame);
		}
		byte[] data = f.getPhysicalData().substring(0,indexPhysicalDataInBytes);

		while (delta > data.length) {
			delta -= data.length;
			indexFrame--;
			if (this.frames.isValidIndex(indexFrame)) {
				f = this.frames.elementAt(indexFrame);	
				data = f.getPhysicalData().getData();
			}			
			indexPhysicalDataInBytes = f.getPhysicalData().lengthInBytes();			
		}
		
		indexPhysicalDataInBytes -= delta;
		AMainDataHandler.TuplaIndex newTi = new AMainDataHandler.TuplaIndex (indexFrame, indexPhysicalDataInBytes);
		return newTi;					
	}


	/**
	 * Retorna o numero de bytes contido entre as duas tuplas.
	 * @param begin tupla de inicio do intervalo, inclusive
	 * @param end tupla de final do intervalo, exclusive
	 * @return numero de bytes contido entre as duas tuplas.
	 */
	public int lengthInBytesBetweenTuplas (AMainDataHandler.TuplaIndex begin, 
	        AMainDataHandler.TuplaIndex end) {
		int lengthInBytes = 0;
		if (begin.getIndexFrameInCollection()==end.getIndexFrameInCollection()) {
			lengthInBytes = end.getIndexPhysicalDataInBytes() - begin.getIndexPhysicalDataInBytes();			
		}
		else {
			int indexFrame = begin.getIndexFrameInCollection();
			Frame f = this.frames.elementAt(indexFrame);
			lengthInBytes += f.getPhysicalData().lengthInBytes() - begin.getIndexPhysicalDataInBytes();
			indexFrame++;
			while (indexFrame != end.getIndexFrameInCollection()) {
				f = this.frames.elementAt(indexFrame);
				lengthInBytes += f.getPhysicalData().lengthInBytes();
				indexFrame++; 
			}
			lengthInBytes += end.getIndexPhysicalDataInBytes();
		}
		return lengthInBytes;
	}


	
	/**
	 * Recupera TuplaIndex equivalente ao inicio do dado lógico 
	 * do frame cujo index e passado como argumento.
	 * @param index index do frame que se deseja analisar
	 * @return TuplaIndex equivalente ao inicio do dado logico do frame cujo 
	 * index e passado como argumento.
	 */	
	public AMainDataHandler.TuplaIndex findIndexBeginLogicalData (int index) {	
		Frame f = this.frames.elementAt(index);
		AMainDataHandler.TuplaIndex ti;
		try {
			 ti = findIndexBeginLogicalDataWithException(index);
		}
		catch (FrameInvalidException e) {
			ti = new AMainDataHandler.TuplaIndex (index,0);
			f.invalidateBorrowBytes();			
		}
		return ti;
	}
	
	private AMainDataHandler.TuplaIndex findIndexBeginLogicalDataWithException (int index) throws FrameInvalidException {

		Frame f = this.frames.elementAt(index);
		int numberOfBorrowBytes = f.getNumberOfBorrowBytes();
		int initialIndexInPreviusFrameInBytes = 0;
		
		try {
			while (numberOfBorrowBytes > 0) {
				index--;
				Frame previusFrame = this.frames.elementAt(index);
				int lengthPhysicalDataPreviusFrameInBytes = previusFrame.getPhysicalData().lengthInBytes(); 
				if (lengthPhysicalDataPreviusFrameInBytes < numberOfBorrowBytes) {
					numberOfBorrowBytes -= lengthPhysicalDataPreviusFrameInBytes; 				 			
				}
				else {				
					initialIndexInPreviusFrameInBytes = lengthPhysicalDataPreviusFrameInBytes - numberOfBorrowBytes; 
					numberOfBorrowBytes = 0;
				}
			}
		}
		catch (Exception e) {
			throw new FrameInvalidException(FrameInvalidException.INVALID_BORROW_BYTES);
		}
		
		AMainDataHandler.TuplaIndex ti = new AMainDataHandler.TuplaIndex (index, initialIndexInPreviusFrameInBytes);
		return ti;
	}
	

}
