/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:cavendi@telemidia.puc-rio.br">Sergio Cavendish</a>
 * Creation date: (09/11/2004)
 */

package util.data;

/**
 * Essa classe implementa um buffer de bytes de tamanho variável.
 * Ao serem inseridos dados, esse buffer ter seu tamanho aumentado para
 * comportar novos dados.
 */
public class GrowableByteBuffer {

	private final int pageSize;

	private byte[] data;

	private int pos;


	/** 
	 * Controi instancia da classe.
	 * @param pageSize tamanho inicial do buffer
	 */
	public GrowableByteBuffer(int pageSize) {
		data = new byte[pageSize];
		this.pageSize = pageSize;
		pos = 0;
	}

	/** 
	 * Controi instancia da classe.
	 */	
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

	/** 
	 * Adiciona elementos no buffer.
	 * @param gbb elementos a serem adicionados no buffer 
	 */
	public void put(GrowableByteBuffer gbb) {
		if (!gbb.isEmpty()) {
			byte[] a = gbb.array();
			int length = gbb.position();
			growIfNeeded(a.length);
			arraycopy(data, pos, a, 0, length);
			pos += a.length;
		}
	}

	/** 
	 * Adiciona elementos no buffer.
	 * @param array elementos a serem adicionados no buffer 
	 */
	public void put(byte[] array) {
		put(array,array.length);
	}
	
	public void put(byte value) {
		byte[] array = new byte[1];
		array[0] = value;
		put(array,array.length);
	}
	
	public void put(byte[] array, int length) {
		growIfNeeded(length);
		arraycopy(data, pos, array, 0, length);
		pos += length;
	}

	/*
	 * Ao final desse metodo, o indice pos está sempre apontando para a primeira
	 * posição inválida posterior ao array, ou seja, seu valor é igual ao tamanho do
	 * array.
	 */
	
	/**
	 * Adequa o tamanho do array de bytes de forma a conter apenas dados validos.
	 */
	public void trimToSize() {
		if (pos != 0) {
			if (pos <= data.length - 1) {
				byte[] newData = new byte[pos];
				arraycopy(newData, 0, data, 0, pos);
				this.data = newData;
			}
		}
		else{
			/*
			 * Operação realizada apenas para diminuir a quantidade de memória utilizada.
			 */
			data = new byte[1];													
		}
	}

	/**
	 * Retorna array com dados do buffer.
	 * @return array com dados do buffer
	 */
	public byte[] array() {
		byte b[] = null;
		if (pos > 0) {
			this.trimToSize();
			b = data;
		}
		return b;
	}

	/**
	 * Retorna posicao de leitura dos no buffer.
	 * @return posicao de leitura dos no buffer
	 */
	public int position() {
		return pos;
	}

	/**
	 * Verifica se buffer esta vazio.
	 * @return true sse buffer esta vazio
	 */	
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
	 * @param offset indice indicando a posição inicial, em bits, dos dados solicitados,
	 * no array data
	 * @param sizeInBits numero de bits validos solicitados
	 * @return result inteiro contendo os bits solicitados
	 */
	public int getBits(int offset, int sizeInBits) {
		int result = 0;
		int resultLength = 0;
		int bytePos = offset / 8;
		int offsetInByte = offset % 8;
		int partialResult = 0;
		int intMask = 0;
		int intMaskSize = 0;
		int missingBits = 0;

		/*
		 * Verifica se há bits válidos suficientes no primeiro byte.
		 */
		if (sizeInBits <= (8 - offsetInByte)) {
			/*
			 * Formação da máscara para retirar os bits.
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
		 * Obtenção dos próximos bits de buffer.
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

	/*
	 * Formação de uma máscara para um byte contendo tamanho especificado pelo
	 * parâmetro length. Retorna a máscara como um inteiro.
	 */
	private int intMask(int initialPos, int length) {
		int basicMask = 1;
		int mask = 0;

		if (length != 0) {

			/*
			 * Formação da máscara contendo a quantidade de bits iguais a 1
			 * especificada no parâmetro length nos bits menos significantes do
			 * byte.
			 */
			mask = 1;
			for (int count = 0; count < length - 1; count++) {
				mask <<= 1;
				mask |= basicMask;
			}
			/*
			 * Posicionamento da máscara no bit indicado em initialPos.
			 */
			int shift = 8 - (length + initialPos);
			mask <<= shift;
		}
		return mask;
	}

	/*
	 * Recebe dois inteiros contendo alguns bits válidos. Concatena os bits
	 * válidos em um novo inteiro, que é retornado. A quantidade de bits válidos
	 * em cada um dos inteiros de entrada é especificada pelos parâmetros
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
	
	/**
	 * Modifica dados do buffer
	 * @param array novos dados do buffer
	 * @param numberOfValidBytes numero de dados validos no array 
	 */
	public void setDataArray(byte[] array, int numberOfValidBytes){
		data = array;
		pos = numberOfValidBytes;
	}
	
}