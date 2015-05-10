/*
 * Created on Nov 4, 2004.
 * 
 * Esta vers�o do programa (V1.2) altera a representa��o das estruturas de um fluxo 
 * MPEG-2. Cada elemento do fluxo MPEG-2 � armazenado como um array de bytes. Isso
 * facilita a leitura dos dados, pois essa tarefa passa a ser realizada como uma c�pia
 * dos bytes de entrada. Durante o processo de leitura, os atributos necess�rios
 * presentes no objeto � copiado para atributos individualizados. Essa forma facilita
 * tanto o processo de leitura quanto o de escrita dos objetos, cujas tarefas passam
 * a ser realizadas atrav�s da c�pia de bytes. Por outro lado, esse mecanismo
 * predudica o processo de altera��o de par�metros.
 */
package util.io;

import java.io.FileInputStream;
import java.io.IOException;

//import javax.swing.JOptionPane;

import timescale.video.utils.Constants;
import timescale.video.utils.GrowableByteBuffer;

/**
 * A leitura do arquivo de entrada � realizada em blocos de bytes. Os dados lidos s�o
 * armazenados em um buffer tempor�rio. O controle da leitura desse buffer �
 * feito atrav�s de um �ndice que indica a posi��o em bytes, dentro do buffer.
 * A atualiza��o de dados no buffer � realizada apenas em metade do tamanho do buffer.
 * Isso permite que os bytes posteriores sejam acessados sem que os dados anteriores
 * sejam perdidos. Essa situa��o � importante para o m�todo lookAhead.
 * 
 * @author Sergio Cavendish
 */
public class IoTools {

	private FileInputStream fis;
	private byte buffer[];
	private int position; // indica a posi��o do pr�ximo byte a ser lido.
	private final int FIRST_HALF = 0;
	private final int SECOND_HALF = 1;
	private boolean readFlag;

	/**
	 * Inicializa o buffer de leitura do arquivo, seu �ndice de leitura e preenche com 
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
	 * L� os dados do arquivo de entrada. Cada chamada a esse m�todo l� uma
	 * quantidade de bytes igual � metade do buffer. Isso facilita o m�todo
	 * lookAhead. A quantidade de bytes lida � igual � metade do tamanho do
	 * buffer.
	 */
	private void readInput(int half) throws IOException {
		if (half == FIRST_HALF)
			fis.read(buffer, 0, (buffer.length / 2));
		else if (half == SECOND_HALF)
			fis.read(buffer, (buffer.length / 2), (buffer.length / 2));
	}

	/**
	 * Retorna um array de bytes contendo todos os dados at� que um start_code seja
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
	 * Realiza a compara��o dos pr�ximos bytes do buffer com os valores dos
	 * bytes contidos no par�metro b.
	 * 
	 * @param b - cont�m os bytes a serem comparados com os bytes do buffer.
	 * @return
	 */
	public boolean lookBytesAhead(byte[] b) throws IOException {
		boolean result = true;
		byte a[] = new byte[b.length];

		a = getBytes(b.length);

		/*
		 * Realiza a compara��o.
		 */
		int count = 0;
		while ((count < b.length) && result) {
			if (a[count] != b[count])
				result = false;
			count++;
		}
		/*
		 * Retorna o �ndice bitPos para a posi��o anterior � compara��o.
		 */
		ungetBytes(b.length);

		return result;
	}
	
	public boolean lookExtensionIDAhead(int extID) throws IOException{
		boolean result = true;
		// L� o pr�ximo byte.
		byte nextByte = (getBytes(1))[0];
		// M�scara para a leitura dos pr�ximos 4 bits.
		byte bitMask = -16;
		//M�scara para a obten��o dos 4 bits em um inteiro.
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
	 * L� bytes do buffer. Atualiza o �ndice position de leitura do buffer.
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
	 * Retrocede o �ndice de leitura de bytes no buffer. Tem a fun��o de tornar
	 * bytes, j� consumidos anteriormente, novamente dispon�veis para leitura.
	 * 
	 * @param length - quantidade de bytes a serem retornados ao buffer.
	 */
	private void ungetBytes(int length) {
		/*
		 * Se o �ndice position estiver em uma das metades do buffer e tiver que ser
		 * atualizado para a outra metade, o flag readFlag recebe o valor false, de forma
		 * a prevenir que, quando o �ndice position passar novamente pelo limite de uma
		 * das metades do buffer, seja lida nova metade do buffer. O flag readFlag retorna
		 * a true dentro do m�todo getBytes().
		 */
		int newPosition = position - length;
		if(((position >= buffer.length / 2) && (newPosition < buffer.length / 2))
				|| ((position >= 0) && (newPosition < 0)))
			readFlag = false;
		/*
		 * Atualiza��o do �ndice position.
		 */
		if (position - length >= 0)
			position -= length;
		else {
			position = buffer.length - (length - position);
		}
	}
}
