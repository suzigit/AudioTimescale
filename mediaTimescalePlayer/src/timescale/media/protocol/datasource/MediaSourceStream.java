/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (10/05/2005)
 */

package timescale.media.protocol.datasource;

import java.io.*;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.Seekable;
import javax.media.protocol.SourceStream;

import util.io.CircularInputTools;
import util.io.InputTools;

/**
 * Essa classe representa uma fonte de dados a ser exibida por um player JMF.
 */
public class MediaSourceStream implements PullSourceStream, Seekable
{
	protected ContentDescriptor contentDescriptor;
	private CircularInputTools inputTools;

	/** 
	 * Controi instancia da classe.
	 * @param iOutputTools fonte de dados de saida
	 * @param contentDescriptor tipo de dados do source stream
	 */	
	public MediaSourceStream(InputTools iInputTools, String contentType) 
				throws Exception {
		inputTools = (CircularInputTools) iInputTools;
		this.contentDescriptor = new ContentDescriptor(contentType);
	}
	
	/** 
	 * Realiza a leitura de conjunto de dados.
	 * @param buffer local a ser armazenado os bytes lidos
	 * @param offset index no buffer em que dados devem comecar a ser armazenados
	 * @param length numero de bytes solicitados 
	 * @return numero de bytes lidos
	 */		
	public int read(byte[] buffer, int offset, int length) throws IOException {	
//		System.out.println("MediaDataSource: READ");
		int total = 0;
		try {
			total = this.inputTools.read(buffer,offset,length);
		}
		catch (Exception e) {
			String msg = "MediaDataSource: READ Exception " + e.getMessage();
			System.out.println(msg);
			throw new IOException(msg);
		}
		return total;		 
    }
		
	/** 
	 * Verifica se leitura ira bloquear consumidor.
	 * @return true sse leitura ira bloquear consumidor
	 */
	public boolean willReadBlock() {
		//System.out.println("WILLREADBLOCK STREAM");
		return false;
	}
	
	/** 
	 * Retorna tipo de midia da fonte de dados.
	 * @return tipo de midia da fonte de dados
	 */	
	public ContentDescriptor getContentDescriptor() {
		//System.out.println("MediaDataSource: Get Content Descriptor");
		return this.contentDescriptor;
	}
	
	
	/** 
	 * Finaliza fonte de dados.
	 */	
	public void close() throws IOException {
		//System.out.println("CLOSE STREAM");
	}
	
	/** 
	 * Verifica se os dados da fonte terminaram.
	 * @return true sse os dados da fonte terminaram
	 */
	public boolean endOfStream() {
		//System.out.println("ENDOFSTREAM STREAM");
		return this.inputTools.isEOF();
	}	

	/** 
	 * Retorna tamanho da fonte de dados.
	 * @return tamanho da fonte de dados
	 */	
	public long getContentLength() {
//		System.out.println("MediaDataSource: Get Length");
		return SourceStream.LENGTH_UNKNOWN;
	}

	/** 
	 * Retorna um controle sobre a midia a ser exibida.
	 * @param s nome do controle solicitado
	 * @return controle sobre a midia a ser exibida
	 */		
	public Object getControl (String s) {
//		System.out.println("MediaDataSource: Get Control");
		return null;
	}

	/** 
	 * Retorna controles existentes sobre a midia a ser exibida.
	 * @return controles existentes sobre a midia a ser exibida
	 */		
	public Object[] getControls () {
		//System.out.println("MediaDataSource: Get Controls");
		return new Object[0];
	}

	/** 
	 * Verifica se midia pode ser acessada randomicamente.
	 * @return true sse midia pode ser acessada randomicamente
	 * Deve ser false, porque esta exibindo um stream
	 */		
    public boolean isRandomAccess() {
//    	System.out.println("MediaDataSource: Is Random Access");
    	return false;
    }
    
	/** 
	 * Retorna localizacao do ponteiro de leitura na fonte de dados.
	 * @return localizacao do ponteiro de leitura na fonte de dados
	 */		
    public long tell() {
//    	System.out.println("MediaDataSource: Tell");
    	return this.inputTools.tell();
    }
    
	/** 
	 * Modifica localizacao do ponteiro de leitura na fonte de dados.
	 * @param where localizacao solicitada para ponteiro de leitura na fonte de dados
	 * @return nova localizacao solicitada do ponteiro de leitura na fonte de dados
	 */	
    public long seek(long where) {
    	//System.out.println("MediaDataSource: Seek " + where);
    	long position=0;
		try {
			position = this.inputTools.seek(where);
		}
		catch (Exception e) {
			String msg = "MediaDataSource: SEEK Exception " + e.getMessage();
			System.out.println(msg);
			throw new IllegalArgumentException(msg);
		}
		return position;		 
    }
	
}