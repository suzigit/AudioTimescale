/*
 * Created on 25/08/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.statistics;

/**
 * This strategy considers the information obtained in the whole video stream of file. This strategy 
 * priviledges the information obtained in the past over the new information. So, as the stream or file is
 * read, the influence of a new information diminishes.
 * @author Sergio Cavendish
 */
public final class WholeStreamStrategy extends StatsStrategy{

	public void updateStats(int elementType, Stats stats) {
		stats.incrementsCounter(elementType);
	}
}
