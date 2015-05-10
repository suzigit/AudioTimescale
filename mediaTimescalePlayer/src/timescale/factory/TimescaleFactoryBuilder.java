package timescale.factory;

import timescale.audio.controller.AudioTimescaleFactory;
import timescale.facade.AbstractTimescaleFactory;
//import timescale.system.controller.SystemTimescaleFactory;
//import timescale.video.controller.VideoTimescaleFactory;
import util.data.ContentType;

public class TimescaleFactoryBuilder {
	
	public static AbstractTimescaleFactory getFactory(ContentType inputType) {
		AbstractTimescaleFactory factory;
		if (inputType.isAudio()) {
			factory = new AudioTimescaleFactory();
		}
/*		else if (inputType.isVideo()) {
			factory = new VideoTimescaleFactory();	
		}
		else if (inputType.isSystem()) {
			factory = new SystemTimescaleFactory();
		}*/
		else {
			throw new IllegalArgumentException("Formato nao reconhecido");
		}
		return factory;
	}
}
