/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (01/08/2005)
 */

package timescale.thirdPartyPlayer;

import java.io.FileWriter;
import java.io.IOException;


public class VLCFilePlayer implements IThirdPartyPlayer {
	
	private String url;
	private String vlcPath;
	
	public VLCFilePlayer(String vlcPath) {
		this.vlcPath = vlcPath;
	}
	
	public void stop() {
		
	}
	
	public void prepare() {
		
	}
	
	public void setInput (Object o) {
		this.url = (String) o;
	}
	
	public void play () throws IOException {
		String name = createBatForFile(url);
		Runtime r = Runtime.getRuntime();
		r.exec(name);
	}
	
	//TODO: nao criar arquivo
	private String createBatForFile(String url) throws IOException {
		String name = "vlc.bat";
		FileWriter writer = new FileWriter(name);
		writer.write("C:\n");
		writer.write("cd \"" + this.vlcPath + "\"\n");
		writer.write("vlc file://" + url);
		writer.close();
		return name;
	}

}
