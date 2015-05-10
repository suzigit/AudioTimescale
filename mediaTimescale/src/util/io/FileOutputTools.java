/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (01/05/2004)
 */
 
package util.io;

import java.io.*;

import util.data.Bits;

/**
 * Essa classe disponibiliza um conjunto de operacoes de acesso
 * a um conjunto de bytes em um arquivo binario.
 */
public class FileOutputTools implements OutputTools {
	
	private FileOutputStream writer;
	private String name;
	private boolean isFinished=false;
	
	/** 
	 * Controi instancia da classe.
	 * @param name nome do arquivo binário a ser gerado	 
	 */		
	public FileOutputTools (String name) throws IOException {
		this.name = name;
		this.writer = new FileOutputStream(name);
	}
	
	/** 
	 * Retorna nome da fonte de dados de saida.
	 * @return nome da fonte de dados de saida
	 */		
	public String getName() {
		return this.name;
	}
		
	/**
	 * Escreve conjunto de bits no arquivo binário.
	 * @param bits conjunto de bits a ser escrito no arquivo
	 */
	public void output (Bits bits) throws IOException {					
		byte bytes[] = bits.getBytes();
		this.output(bytes);			
	}

	/**
	 * Escreve conjunto de bits no arquivo binário.
	 * @param sBits conjunto de bits a ser escrito no arquivo
	 */
	public void output (String sBits) throws IOException {					
		if (sBits!=null && !sBits.equals("")) {
			Bits bits = new Bits(sBits);
			this.output(bits);
		}
	}
	
	/**
	 * Escreve conjunto de bits no arquivo binário.
	 * @param bytes conjunto de bits a ser escrito no arquivo
	 */
	public void output (byte bytes[]) throws IOException {
		if (bytes!=null) {
			this.writer.write(bytes);
		}
	}
	
	/**
	 * Escreve conjunto de bits no arquivo binário.
	 * @param bytes conjunto de bits a ser escrito no arquivo
	 * @total numero de bytes a ser escrito do buffer
	 */
	public void output (byte bytes[], int total) throws IOException {
		this.output(bytes,0,total);
	}
	
	public void output (byte bytes[], int delta, int total) throws IOException {
		if (bytes!=null) {
			this.writer.write(bytes, delta, total);
		}
	}	
	
	/**
	 * Método para fechar arquivo. 
	 */
	public void finish () throws IOException {		
		this.writer.close();
		this.isFinished=true;
		System.out.println("FileOutputTools: Fechou arquivo " + name);
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
}