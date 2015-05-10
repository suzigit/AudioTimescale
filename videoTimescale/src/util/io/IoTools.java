/*
 * Created on Nov 4, 2004.
 * 
 * Esta versão do programa (V1.2) altera a representação das estruturas de um fluxo 
 * MPEG-2. Cada elemento do fluxo MPEG-2 é armazenado como um array de bytes. Isso
 * facilita a leitura dos dados, pois essa tarefa passa a ser realizada como uma cópia
 * dos bytes de entrada. Durante o processo de leitura, os atributos necessários
 * presentes no objeto é copiado para atributos individualizados. Essa forma facilita
 * tanto o processo de leitura quanto o de escrita dos objetos, cujas tarefas passam
 * a ser realizadas através da cópia de bytes. Por outro lado, esse mecanismo
 * predudica o processo de alteração de parâmetros.
 */
package util.io;

import java.io.FileInputStream;
import java.io.IOException;

//import javax.swing.JOptionPane;

import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;

/**
 * A leitura do arquivo de entrada é realizada em blocos de bytes. Os dados lidos são
 * armazenados em um buffer temporário. O controle da leitura desse buffer é
 * feito através de um índice que indica a posição em bytes, dentro do buffer.
 * A atualização de dados no buffer é realizada apenas em metade do tamanho do buffer.
 * Isso permite que os bytes posteriores sejam acessados sem que os dados anteriores
 * sejam perdidos. Essa situação é importante para o método lookAhead.
 * 
 * @author Sergio Cavendish
 */
public class IoTools {

	private FileInputStream fis;
	private byte buffer[];
	private int position; // indica a posição do próximo byte a ser lido.
	private final int FIRST_HALF = 0;
	private final int SECOND_HALF = 1;
	private boolean readFlag;

	/**
	 * Inicializa o buffer de leitura do arquivo, seu índice de leitura e preenche com 
	 * dados a primeira metade do buffer.
	 * 
	 * @param fileInputStream
	 * @param bufferLengthInBytes
	 */
	public IoTools(FileInputStream fileInputStream, int bufferLengthInBytes) 
		throws IOException {
		fis = fileInputStream;
		buffer = new byte[bufferLengthInBytes];
		position = 0;
		readInput(FIRST_HALF);
		readFlag = true;
	}
	
	public void close() throws Exception {
		fis.close();
	}


	/**
	 * Lê os dados do arquivo de entrada. Cada chamada a esse método lê uma
	 * quantidade de bytes igual à metade do buffer. Isso facilita o método
	 * lookAhead. A quantidade de bytes lida é igual à metade do tamanho do
	 * buffer.
	 */
	private void readInput(int half) throws IOException {
		if (half == FIRST_HALF)
			fis.read(buffer, 0, (buffer.length / 2));
		else if (half == SECOND_HALF)
			fis.read(buffer, (buffer.length / 2), (buffer.length / 2));
	}

	/**
	 * Retorna um array de bytes contendo todos os dados até que um start_code seja
	 * encontrado.
	 * 
	 * @return
	 */
	public GrowableByteBuffer nextStartCode() throws IOException {
		GrowableByteBuffer gbb = new GrowableByteBuffer(1);
		while (!lookBytesAhead(Constants.START_CODE_PREFIX)) {
			byte b[] = getBytes(1);
			gbb.put(b);
		}
		if(!gbb.isEmpty())
			gbb.trimToSize();
		return gbb;
	}

	/**
	 * Realiza a comparação dos próximos bytes do buffer com os valores dos
	 * bytes contidos no parâmetro b.
	 * 
	 * @param b - contém os bytes a serem comparados com os bytes do buffer.
	 * @return
	 */
	public boolean lookBytesAhead(byte[] b) throws IOException {
		boolean result = true;
		byte a[] = new byte[b.length];

		a = getBytes(b.length);

		/*
		 * Realiza a comparação.
		 */
		int count = 0;
		while ((count < b.length) && result) {
			if (a[count] != b[count])
				result = false;
			count++;
		}
		/*
		 * Retorna o índice bitPos para a posição anterior à comparação.
		 */
		ungetBytes(b.length);

		return result;
	}
	
	public boolean lookExtensionIDAhead(int extID) throws IOException{
		boolean result = true;
		// Lê o próximo byte.
		byte nextByte = (getBytes(1))[0];
		// Máscara para a leitura dos próximos 4 bits.
		byte bitMask = -16;
		//Máscara para a obtenção dos 4 bits em um inteiro.
		int intMask = 15;
		int extensionID = ((nextByte & bitMask) >> 4) & intMask;
		
		if(extensionID != extID)
			result = false;
		
		ungetBytes(1);
		
		return result;
	}
	
	public boolean lookSliceStartCodeAhead() throws IOException {
		boolean result = false;
		byte a[] = new byte[4];
		a = getBytes(4);
		if ((a[0] == 0)
			&& (a[1] == 0)
			&& (a[2] == 1)
			&& ((a[3] >= 1) && (a[3] <= 127) || (a[3] >= -128) && (a[3] <= -81)))
			result = true;
		
		ungetBytes(4);

		return result;
	}

	/**
	 * Lê bytes do buffer. Atualiza o índice position de leitura do buffer.
	 * 
	 * @param size - indica a quantidade de bytes a serem lidos.
	 * @return
	 */
	public byte[] getBytes(int length) throws IOException {
		byte b[] = new byte[length];
		
		for (int count = 0; count < length; count++) {
			
					
			b[count] = buffer[position++];
			
			//int pos = position;
			if ((position == buffer.length / 2) && readFlag) {
				readInput(SECOND_HALF);
			} else if (position == buffer.length){
				if (readFlag)
					readInput(FIRST_HALF);
				position = 0;
			}
//			if(((pos == buffer.length / 2) || (pos == buffer.length)) && !readFlag)
//				readFlag = true;
			if(((position == 5) && !readFlag) || ((position == (buffer.length / 2 + 5)) && !readFlag))
				readFlag = true;
		}
		return b;
	}

	/**
	 * Retrocede o índice de leitura de bytes no buffer. Tem a função de tornar
	 * bytes, já consumidos anteriormente, novamente disponíveis para leitura.
	 * 
	 * @param length - quantidade de bytes a serem retornados ao buffer.
	 */
	private void ungetBytes(int length) {
		/*
		 * Se o índice position estiver em uma das metades do buffer e tiver que ser
		 * atualizado para a outra metade, o flag readFlag recebe o valor false, de forma
		 * a prevenir que, quando o índice position passar novamente pelo limite de uma
		 * das metades do buffer, seja lida nova metade do buffer. O flag readFlag retorna
		 * a true dentro do método getBytes().
		 */
		int newPosition = position - length;
		if(((position >= buffer.length / 2) && (newPosition < buffer.length / 2))
				|| ((position >= 0) && (newPosition < 0)))
			readFlag = false;
		/*
		 * Atualização do índice position.
		 */
		if (position - length >= 0)
			position -= length;
		else {
			position = buffer.length - (length - position);
		}
	}
}
