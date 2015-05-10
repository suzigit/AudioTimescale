/*
 * Created on 25/08/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.statistics;

/**
 * @author Sergio Cavendish
 */
public abstract class StatsStrategy {

	public abstract void updateStats(int elementType, Stats stats);
}