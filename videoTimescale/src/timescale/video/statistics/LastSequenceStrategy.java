/*
 * Created on 27/09/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.statistics;

import java.util.Vector;

import timescale.video.utils.Constants;

/**
 * @author Sergio Cavendish
 */
public final class LastSequenceStrategy extends StatsStrategy {

	private Vector queue;

	public LastSequenceStrategy() {
	}

	public void updateStats(int elementType, Stats stats) {

		if (elementType == Constants.SEQ)
			stats.clearStats();
		else
			stats.incrementsCounter(elementType);
	}
}
