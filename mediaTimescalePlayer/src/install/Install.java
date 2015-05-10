package install;
import javax.media.Codec;
import javax.media.Format;
import javax.media.PlugInManager;



public class Install {

	
	public static void main(String args[]) throws Exception {
		 String className = "net.sourceforge.jffmpeg.AudioDecoder";
		 Class c = Class.forName(className);
		 Object o = c.newInstance();
		 Format [] inputs = ((Codec)o).getSupportedInputFormats();
		 Format [] outputs = ((Codec)o).getSupportedOutputFormats(null);
		 PlugInManager.addPlugIn(className, inputs, outputs, PlugInManager.CODEC);
		 PlugInManager.commit();
		 
	}
}
