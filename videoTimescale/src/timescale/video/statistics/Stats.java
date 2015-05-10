/*
 * Created on 25/08/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.statistics;

import timescale.video.utils.Constants;

/**
 * @author Sergio Cavendish
 */
public class Stats {

	private String fileName;
	private String fileFormat;

	private String videoFormat;
	private String widhtXHeight;
	private String profileAndLevel;
	private double frameRate;
	private double bitRate;
	
	private int sequenceCounter;
	private int gopCounter;
	private int iPictureCounter;
	private int pPictureCounter;
	private int bPictureCounter;
	private int unknownPictureCounter;

	public Stats() {
		this.fileName = "";
		this.fileFormat = "Unknown";
		this.videoFormat = "Unknown";
		this.widhtXHeight = "";
		this.profileAndLevel = "Not recognized";
		this.frameRate = 0.0;
		this.bitRate = 0.0;
		
		this.sequenceCounter = 0;
		this.gopCounter = 0;
		this.iPictureCounter = 0;
		this.pPictureCounter = 0;
		this.bPictureCounter = 0;
		this.unknownPictureCounter = 0;
	}

	public void update(int elementType) {
		incrementsCounter(elementType);
	}

	/**
	 * @param i
	 */
	public void setBPictureCounter(int i) {
		bPictureCounter = i;
	}

	/**
	 * @param i
	 */
	public void setGopCounter(int i) {
		gopCounter = i;
	}

	/**
	 * @param i
	 */
	public void setIPictureCounter(int i) {
		iPictureCounter = i;
	}

	/**
	 * @param i
	 */
	public void setPPictureCounter(int i) {
		pPictureCounter = i;
	}

	/**
	 * @param i
	 */
	public void setUnknownTypePictureCounter(int i) {
		unknownPictureCounter = i;
	}

	public void clearStats() {
		this.sequenceCounter = 0;
		this.gopCounter = 0;
		this.iPictureCounter = 0;
		this.pPictureCounter = 0;
		this.bPictureCounter = 0;
		this.unknownPictureCounter = 0;
	}

	/**
	 * @param element
	 */
	public void decrementsCounter(int element) {
		switch (element) {
			case Constants.I_PIC :
				iPictureCounter--;
				break;
			case Constants.P_PIC :
				pPictureCounter--;
				break;
			case Constants.B_PIC :
				bPictureCounter--;
				break;
			case Constants.UNKNOWN_PIC :
				unknownPictureCounter--;
				break;
			case Constants.GOP :
				gopCounter--;
				break;
			case Constants.SEQ :
				sequenceCounter--;
				break;
		}
	}

	/**
	 * @param element
	 */
	public void incrementsCounter(int element) {
		switch (element) {
			case Constants.I_PIC :
				iPictureCounter++;
				break;
			case Constants.P_PIC :
				pPictureCounter++;
				break;
			case Constants.B_PIC :
				bPictureCounter++;
				break;
			case Constants.UNKNOWN_PIC :
				unknownPictureCounter++;
				break;
			case Constants.GOP :
				gopCounter++;
				break;
			case Constants.SEQ :
				sequenceCounter++;
				break;
		}
	}
	/**
	 * @return
	 */
	public int getBPictureCounter() {
		return bPictureCounter;
	}

	/**
	 * @return
	 */
	public int getGopCounter() {
		return gopCounter;
	}

	/**
	 * @return
	 */
	public int getIPictureCounter() {
		return iPictureCounter;
	}

	/**
	 * @return
	 */
	public int getPPictureCounter() {
		return pPictureCounter;
	}

	/**
	 * @return
	 */
	public int getSequenceCounter() {
		return sequenceCounter;
	}

	/**
	 * @return
	 */
	public int getUnknownPictureCounter() {
		return unknownPictureCounter;
	}

	public String toString() {
		String report = "";
		report = "\nSequence Counter:\t" + getSequenceCounter();
		report += "\nGOP Counter:\t" + getGopCounter();
		report += "\nI pic Counter:\t" + getIPictureCounter();
		report += "\nP pic Counter:\t" + getPPictureCounter();
		report += "\nB pic Counter:\t" + getBPictureCounter();

		return report;
	}
	/**
	 * @return
	 */
	public double getBitRate() {
		return bitRate;
	}

	/**
	 * @return
	 */
	public String getFileFormat() {
		return fileFormat;
	}

	/**
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return
	 */
	public double getFrameRate() {
		return frameRate;
	}

	/**
	 * @return
	 */
	public String getProfileAndLevel() {
		return profileAndLevel;
	}

	/**
	 * @return
	 */
	public String getVideoFormat() {
		return videoFormat;
	}

	/**
	 * @return
	 */
	public String getWidhtXHeight() {
		return widhtXHeight;
	}

	/**
	 * @param d
	 */
	public void setBitRate(double d) {
		bitRate = d;
	}

	/**
	 * @param string
	 */
	public void setFileFormat(String string) {
		fileFormat = string;
	}

	/**
	 * @param string
	 */
	public void setFileName(String string) {
		fileName = string;
	}

	/**
	 * @param d
	 */
	public void setFrameRate(double d) {
		frameRate = d;
	}

	/**
	 * @param string
	 */
	public void setProfileAndLevel(String string) {
		profileAndLevel = string;
	}

	/**
	 * @param i
	 */
	public void setSequenceCounter(int i) {
		sequenceCounter = i;
	}

	/**
	 * @param i
	 */
	public void setUnknownPictureCounter(int i) {
		unknownPictureCounter = i;
	}

	/**
	 * @param string
	 */
	public void setVideoFormat(String string) {
		videoFormat = string;
	}

	/**
	 * @param string
	 */
	public void setWidhtXHeight(String string) {
		widhtXHeight = string;
	}

}
