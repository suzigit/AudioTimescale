/*
 * Created on 26/08/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.controller;

import timescale.video.statistics.*;

/**
 * This politics can be adapted according diferent algorithms. This politics considers only 
 * dropping or insertion of B pictures. In order to do that, it provides methods to argue about
 * the possibility of doing the requested drop.
 * Algorithm:
 * 1) Verify the possibility to perform the requested operation, through statistics information
 * 2) 
 * @author Sergio Cavendish
 */
public abstract class ControlStrategy {

	StatsCollector collector = StatsCollector.instance();
	Stats stats;

	protected static double desiredRate;
	protected static int inputPicQuantity;
	protected static int outputPicQuantity;

	protected ControlStrategy(double rate) {
		desiredRate = rate;
		inputPicQuantity = 0;
		outputPicQuantity = 0;
	}

	public abstract boolean isAdded(Object element);

	public abstract boolean isInserted();

	public void setRate(double rate) {
		desiredRate = rate;
		inputPicQuantity = 0;
		outputPicQuantity = 0;
	}

	public double getRate() {
		return desiredRate;
	}
}
