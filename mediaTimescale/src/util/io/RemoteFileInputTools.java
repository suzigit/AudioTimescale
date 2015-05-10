/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (14/09/2004)
 */

package util.io;

import java.net.URL;
import java.net.URLConnection;

import util.data.SharedBuffer;

public class RemoteFileInputTools extends FileInputTools {
	
	public RemoteFileInputTools (SharedBuffer buffer, int bufferInputFileLength) throws Exception {
		super (buffer, bufferInputFileLength);
	}
	
	protected void initializeStream() throws Exception {
    	URL url = new URL(this.getInputName());
		URLConnection urlConnection = url.openConnection();
		this.stream=urlConnection.getInputStream();
	}
		
}
