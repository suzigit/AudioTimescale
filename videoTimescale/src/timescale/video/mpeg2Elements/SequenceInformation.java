/*
 * Created on Oct 8, 2005
 */
package timescale.video.mpeg2Elements;

import java.util.Vector;

import timescale.video.utils.Constants;

/**
 * @author Sergio Cavendish
 */
public class SequenceInformation{
	private int progressiveSequence;
	private int lowDelay;
	
	private Vector iList, pList, bList;
	private Vector replicableList;
	private Vector duplicatedList, triplicatedList;
	
	public SequenceInformation(){
		iList = new Vector();
		pList = new Vector();
		bList = new Vector();
		
		replicableList = new Vector();
		duplicatedList = new Vector();
		triplicatedList = new Vector();
	}
	
	/**
	 * @param lowDelay The lowDelay to set.
	 */
	public void setLowDelay(int lowDelay) {
		this.lowDelay = lowDelay;
	}
	/**
	 * @param progressiveSequence The progressiveSequence to set.
	 */
	public void setProgressiveSequence(int progressiveSequence) {
		this.progressiveSequence = progressiveSequence;
	}
	
	public void atualizeLists(Picture p){
		
		// Atualizacao das listas de figuras
		int picType = p.getPictureCodingType();
		switch(picType){
			case Constants.I_PIC:
				iList.add(p);
				break;
			case Constants.P_PIC:
				pList.add(p);
				break;
			case Constants.B_PIC:
				bList.add(p);
			default:
		}
		
		// Atualizacao das listas de replicacao.
		int repeatFirstField = p.getRepeatFirstField();
		int progressiveFrame = p.getProgressiveFrame();
		int topFieldFirst = p.getTopFieldFirst();
		int pictureStructure = p.getPictureStructure();
		
		if(pictureStructure == Constants.FRAME_PIC){
			if(progressiveSequence == 1){
				if(repeatFirstField == 1)
						if(topFieldFirst == 0)
							duplicatedList.add(p);
						else
							triplicatedList.add(p);
				else
					replicableList.add(p);
			}
		}		 
	}
	
	public int getNumberOfPics(){
		return (iList.size() + pList.size() + bList.size());
	}
	
	/**
	 * @return Returns the replicableList.
	 */
	public Vector getReplicableList() {
		return replicableList;
	}
	
	/**
	 * @return Returns the duplicatedList.
	 */
	public Vector getDuplicatedList() {
		return duplicatedList;
	}
	
	/**
	 * @return Returns the triplicatedList.
	 */
	public Vector getTriplicatedList() {
		return triplicatedList;
	}
	
	/**
	 * @return Returns the bList.
	 */
	public Vector getBList() {
		return bList;
	}

	/* (non-Javadoc)
	 * @see videoData.VideoObject#getObject()
	 */
	public byte[] getObject() {
		return null;
	}

	/**
	 * @return
	 */
	public Vector getPList() {
		return pList;
	}
	/**
	 * @return Returns the iList.
	 */
	public Vector getIList() {
		return iList;
	}
	
	/**
	 * Retorna o número de quadros considerando a replicação.
	 * @author Sergio Cavendish
	 */
	public int getNumberOfPresentationPics(){
		return (replicableList.size() + 2 * duplicatedList.size()
				+ 3 * triplicatedList.size());
	}
}

