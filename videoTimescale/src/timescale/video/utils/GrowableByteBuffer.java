/*
 * Created on Nov 9, 2004
 */
package timescale.video.utils;

/**
 * @author Sergio Cavendish
 */
public class GrowableByteBuffer {

	private final int pageSize;

	private byte[] data;

	private int pos;

	public GrowableByteBuffer(int pageSize) {
		data = new byte[pageSize];
		this.pageSize = pageSize;
		pos = 0;
	}

	public GrowableByteBuffer() {
		this(4096);
	}

	private void growIfNeeded(int size) {
		int newLength = size + pos;
		if (newLength > data.length) {
			int newSize = ((newLength / pageSize) + 1) * pageSize * 2;
			byte[] newData = new byte[newSize];
			arraycopy(newData, 0, data, 0, data.length);
			this.data = newData;
		}
	}

	public void put(GrowableByteBuffer gbb) {
		if (!gbb.isEmpty()) {
			byte[] a = gbb.array();
			int length = gbb.position();
			growIfNeeded(a.length);
			arraycopy(data, pos, a, 0, length);
			pos += a.length;
		}
	}

	public void put(byte[] array) {
		growIfNeeded(array.length);
		arraycopy(data, pos, array, 0, array.length);
		pos += array.length;
	}

	/*
	 * Adequa o tamanho do array de bytes de forma a conter apenas dados v�lidos.
	 * Ao final desse m�todo, o �ndice pos est� sempre apontando para a primeira
	 * posi��o inv�lida posterior ao array, ou seja, seu valor � igual ao tamanho do
	 * array.
	 */
	public void trimToSize() {
		if (pos != 0) {
			if (pos <= data.length - 1) {
				byte[] newData = new byte[pos];
				arraycopy(newData, 0, data, 0, pos);
				this.data = newData;
			}
/*			else if(pos == data.length){
				byte[] newData = new byte[pos + 1];
				arraycopy(newData, 0, data, 0, data.length);
				this.data = newData;
			}
*/
		}
		else{
			/*
			 * Opera��o realizada apenas para diminuir a quantidade de mem�ria utilizada.
			 */
			data = new byte[1];													
		}
	}

	public byte[] array() {
		this.trimToSize();
		return data;
	}

	public int position() {
		return pos;
	}

	public boolean isEmpty() {
		boolean result = true;
		if (pos != 0)
			result = false;
		return result;
	}

	private void arraycopy(byte[] a, int aPos, byte[] b, int bPos, int length) {
		for (int count = 0; count < length; count++) {
			a[aPos++] = b[bPos++];
		}
	}

	/**
	 * Copia bits contidos em um GrowableByteBuffer e os retorna como um inteiro.
	 * 
	 * @param offset -
	 *            �ndice indicando a posi��o inicial, em bits, dos dados solicitados,
	 * 						no array data.
	 * @param sizeInBits -
	 *            n�mero de bits v�lidos solicitados.
	 * @return result - inteiro contendo os bits solicitados.
	 */
	public int getBits(int offset, int sizeInBits) {
		int result = 0;
		int resultLength = 0;
		int bytePos = offset / 8;
		int offsetInByte = offset % 8;
		int partialResult = 0;
		int partialResultLength = 0;
		int intMask = 0;
		int intMaskSize = 0;
		int missingBits = 0;

		/*
		 * Verifica se h� bits v�lidos suficientes no primeiro byte.
		 */
		if (sizeInBits <= (8 - offsetInByte)) {
			/*
			 * Forma��o da m�scara para retirar os bits.
			 */
			intMaskSize = sizeInBits;
			intMask = intMask(offsetInByte, sizeInBits);
			/*
			 * Retirada do primeiro byte.
			 */
			result = data[bytePos++] & intMask;
			result >>>= (8 - offsetInByte - sizeInBits);
			offset += intMaskSize;
			resultLength += intMaskSize;
		} else {
			/*
			 * Retirada dos bits do primeiro byte.
			 */
			intMaskSize = 8 - offsetInByte;
			intMask = intMask(offsetInByte, intMaskSize);
			result = data[bytePos++] & intMask;
			offset += intMaskSize;
			resultLength += intMaskSize;
		}

		/*
		 * Obten��o dos pr�ximos bits de buffer.
		 */
		while (resultLength < sizeInBits) {
			missingBits = sizeInBits - resultLength;
			if (missingBits >= 8)
				intMaskSize = 8;
			else
				intMaskSize = missingBits;

			intMask = intMask(0, intMaskSize);
			/*
			 * Retirada dos bits do buffer.
			 */
			partialResult = data[bytePos++] & intMask;
			partialResult >>>= 8 - intMaskSize;
			result = concatenate(result, resultLength, partialResult,
					intMaskSize);
			resultLength += intMaskSize;
		}

		return result;
	}
	
	public void setBits(int offset, int value, int sizeInBits){
		int nextValue = 0;
		int totalSize = sizeInBits;
		int bytePos = offset / 8;
		int offsetInByte = offset % 8;
		int usedBits = 0;
		int nextSizeInBits = 0;
		int missingBits = 0;
		int availableBitsInByte = 8 - offsetInByte;
		
		if(sizeInBits <= availableBitsInByte){
			data[bytePos] = setBitsInByte(data[bytePos], offsetInByte, value, sizeInBits);
			usedBits += sizeInBits;
		}
		else{
			nextValue = nextValue(value, sizeInBits, availableBitsInByte);
							
			value >>>= (sizeInBits - availableBitsInByte);
			data[bytePos] = setBitsInByte(data[bytePos], offsetInByte, value,
				availableBitsInByte);
				
			usedBits += availableBitsInByte;
			
			sizeInBits -= availableBitsInByte;
			availableBitsInByte = 8;
			bytePos++;
			offsetInByte = 0;
			value = nextValue;
			
			while(usedBits < totalSize){
				missingBits = totalSize - usedBits;
				if(missingBits >= 8)
					nextSizeInBits = 8;
				else
					nextSizeInBits = missingBits;
				
				nextValue = nextValue(value, sizeInBits, nextSizeInBits);
				
				value >>>= (sizeInBits - nextSizeInBits);
				data[bytePos] = setBitsInByte(data[bytePos], offsetInByte, value,
						nextSizeInBits);
				
				bytePos++;
				value = nextValue;
				sizeInBits -= nextSizeInBits;
				usedBits += nextSizeInBits;
			}
		}
	}
	
	private int nextValue(int value, int sizeInBits, int discardedLength){
		int result = 0;
		int intMask = ~intMask((8 - sizeInBits), discardedLength);
		return result = value & intMask;
	}
	
	private byte setBitsInByte(byte b, int offsetInByte, int value, int length){
		byte result;
		int intMaskSize = length;
		int	intMask;
		int intByte;
		
		/*
		 * Forma��o da m�scara para limitar os bits v�lidos em value.
		 */
		intMask = intMask((8 - intMaskSize), intMaskSize);
		value &= intMask;
		
		/*
		 * Posicionamento dos bits v�lidos de value.
		 */
		value <<= (8 - (offsetInByte + length));
		
		/*
		 * Forma��o da m�scara invertida.
		 */
		intMask = intMask(offsetInByte, intMaskSize);
		intMask = ~intMask;
		
		/*
		 * Obten��o do byte a ser alterado e exclus�o dos dados das posi��es em bits
		 * a serem substitu�dos
		 */
		intByte = b & intMask;
		/*
		 *  Grava��o dos novos valores em result.
		 */
		result = (byte)(intByte | value);
		return result;
	}

	/**
	 * Forma��o de uma m�scara para um byte contendo tamanho especificado pelo
	 * par�metro length. Retorna a m�scara como um inteiro.
	 */
	private int intMask(int initialPos, int length) {
		int basicMask = 1;
		int mask = 0;

		if (length != 0) {

			/*
			 * Forma��o da m�scara contendo a quantidade de bits iguais a 1
			 * especificada no par�metro length nos bits menos significativos do
			 * byte.
			 */
			mask = 1;
			for (int count = 0; count < length - 1; count++) {
				mask <<= 1;
				mask |= basicMask;
			}
			/*
			 * Posicionamento da m�scara no bit indicado em initialPos.
			 */
			int shift = 8 - (length + initialPos);
			mask <<= shift;
		}
		return mask;
	}

	/**
	 * Recebe dois inteiros contendo alguns bits v�lidos. Concatena os bits
	 * v�lidos em um novo inteiro, que � retornado. A quantidade de bits v�lidos
	 * em cada um dos inteiros de entrada � especificada pelos par�metros
	 * firstLength e secondLength.
	 */
	private int concatenate(int firstPiece, int firstLength, int secondPiece,
			int secondLength) {

		int result = 0;

		result = firstPiece;
		result <<= secondLength;
		result |= secondPiece;

		return result;
	}
	
	public Object clone(){
		GrowableByteBuffer gbb = new GrowableByteBuffer();
		gbb.put(this);
		gbb.trimToSize();
		return (Object)gbb;
	}
}