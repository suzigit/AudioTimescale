/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:cavendi@telemidia.puc-rio.br">Sergio Cavendish</a>
 * Creation date: (25/02/2005)
 */

package util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import util.data.SharedBuffer;

/**
 * Esta classe implementa os metodos para a leitura de dados a partir 
 * de um arquivo.
 */
public class FileInputTools extends CircularInputTools
							implements Runnable {
	
	protected InputStream stream;	
	private int bufferInputFileLength;

	/** 
	 * Controi instancia da classe.
	 * @param inputTools fonte de dados de entrada
	 * @param bufferLengthInBytes tamanho do buffer de entrada
	 */	
	public FileInputTools(SharedBuffer buffer, int bufferInputFileLength) throws Exception {
		super (buffer);
		this.reset();
		this.bufferInputFileLength = bufferInputFileLength;
		this.initializeStream();
	}
	
	protected void initializeStream() throws Exception {
		this.stream = new FileInputStream(new File(this.getInputName()));
	}
	
	public void run()  {
		try {			
			ByteOutputTools bot = new ByteOutputTools(this.getBuffer()); 
			byte array[] = new byte[bufferInputFileLength];
			int total = array.length;
			while (total!=-1) {
				total = this.stream.read(array,0,array.length);
				if (total>0) {
					bot.output(array,total);
				}
			}												
			System.out.println("############# Fim da leitura do arquivo ################");
			stream.close();
			bot.finish();
		} 
		catch (Exception e) {
			System.out.println("Erro na leitura do arquivo - " + e.getMessage());
		}
	}
	
	public synchronized void close() throws IOException {
		
	}
}
