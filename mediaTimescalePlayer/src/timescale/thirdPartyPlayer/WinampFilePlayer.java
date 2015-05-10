/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (10/08/2005)
 */

package timescale.thirdPartyPlayer;

import java.io.FileWriter;
import java.io.IOException;


//O Winamp deve estar aberto para esse player funcionar

public class WinampFilePlayer implements IThirdPartyPlayer {
	
	private String url;
	private String winampPath;
	
	public WinampFilePlayer (String winampPath) {
		this.winampPath = winampPath;
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
		writer.write("cd \"" + this.winampPath + "\"\n");
		writer.write("Clamp /plclear\n");
		writer.write("Clamp /pladd " + url + "\n");
		writer.write("Clamp /play");
		writer.close();
		return name;
	}
	

}
