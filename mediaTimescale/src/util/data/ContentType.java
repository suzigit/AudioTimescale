/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (20/09/2004)
 */

package util.data;

import java.util.HashMap;
import java.util.Iterator;

import timescale.util.constants.Constants;


public class ContentType {
	
	private int id;
	
	private static HashMap hashExtension;
	private static HashMap hashFullFormat;

	/*
	 * Cosiderar que audio tem id no intervalo [0,10[
	 * video em [10,20[
	 * sistemas em [20,30[
	 */
	public static int ID_MPEG_AUDIO_BC = 0;
	public static int ID_MP2 = 1;
	public static int ID_MP3 = 2;
	public static int ID_AC3 = 3;
	public static int ID_MPEG_ADTS_AAC = 4;	
	public static int ID_MPEG2_VIDEO = 10;
	public static int ID_MPEG_SYSTEM_A = 20;
	public static int ID_MPEG_SYSTEM_B = 21;
	
	static {
		
		hashFullFormat = new HashMap();
		hashFullFormat.put(ID_MPEG_AUDIO_BC+"", Constants.ContentType.MPEG_AUDIO_BC);
		hashFullFormat.put(ID_MP2+"", Constants.ContentType.MP2);
		hashFullFormat.put(ID_MP3+"", Constants.ContentType.MP3);
		hashFullFormat.put(ID_AC3+"", Constants.ContentType.AC3);
		hashFullFormat.put(ID_MPEG_ADTS_AAC+"", Constants.ContentType.AAC);
		hashFullFormat.put(ID_MPEG2_VIDEO+"", Constants.ContentType.MPEG2_VIDEO);
		hashFullFormat.put(ID_MPEG_SYSTEM_A+"", Constants.ContentType.MPEG_SYSTEM);
		hashFullFormat.put(ID_MPEG_SYSTEM_B+"", Constants.ContentType.MPEG_SYSTEM);
		
		hashExtension = new HashMap();
		
		hashExtension.put(ID_MP2+"", Constants.Extension.MP2);
		hashExtension.put(ID_MP3+"", Constants.Extension.MP3);
		hashExtension.put(ID_AC3+"", Constants.Extension.AC3);
		hashExtension.put(ID_MPEG_ADTS_AAC+"", Constants.Extension.AAC);
		hashExtension.put(ID_MPEG2_VIDEO+"", Constants.Extension.MPEG2_VIDEO);
		hashExtension.put(ID_MPEG_SYSTEM_A+"", Constants.Extension.MPEG_SYSTEM_A);
		hashExtension.put(ID_MPEG_SYSTEM_B+"", Constants.Extension.MPEG_SYSTEM_B);
	}

	public ContentType (int id) {
		this.id = id;
	}
	
	public String getFullFormat() {
		return (String) hashFullFormat.get(this.id + "");
	}
	
	public boolean isMPEGBC() {
		boolean answer = false;
		if (this.id==0 || this.id==1 || this.id==2) {
			answer = true;
		}
		return answer; 
	}
	
	public boolean isAudio () {
		return (this.id >= 0 && this.id < 10);
	}

	public boolean isVideo () {
		return (this.id >= 10 && this.id < 20);
	}	
	
	public boolean isSystem () {
		return (this.id >= 20 && this.id < 30);
	}	
	
	public static ContentType createByFullFormat(String format) {
		Iterator keys = hashFullFormat.keySet().iterator();
		boolean isFound = false;
		int id=-1;
		while (keys.hasNext() && !isFound) {
			String key = (String) keys.next();
			String value = (String) hashFullFormat.get(key);
			if (format.equals(value)) {
				isFound = true;
				id = Integer.parseInt(key);
			}
		}
		return new ContentType(id);
	}
		
	public static ContentType createByExtension(String extension) {
		Iterator keys = hashExtension.keySet().iterator();
		boolean isFound = false;
		int id=-1;
		while (keys.hasNext() && !isFound) {
			String key = (String) keys.next();
			String value = (String) hashExtension.get(key);
			if (extension.equals(value)) {
				isFound = true;
				id = Integer.parseInt(key);
			}
		}
		return new ContentType(id);
	}

	
	public boolean equals(Object o) {
		boolean value = false;
		try {
			ContentType ct = (ContentType) o;
			value = this.id==ct.id;
		}
		catch (Exception e) {}
		return value;
	}

}
