/*
 * Created on 28/09/2004
 */
package timescale.video.controller;

import timescale.video.mpeg2Elements.*;
import timescale.video.utils.Constants;

/**
 * @author Sergio Cavendish
 */
public class AddDropBFrames extends ControlStrategy {

	public AddDropBFrames(int window) {
		super(1.0);
	}

	public AddDropBFrames() {
		super(1.0);
	}

	/* 
	 * @see videoController.ControlStrategy#isAdded(java.lang.Object)
	 */
	public boolean isAdded(Object element) {
		boolean add = true;

		if (desiredRate < 1.0) {
			if (element instanceof Picture) {

				int picType = ((Picture) element).getPictureCodingType();

				if (inputPicQuantity != 0) {
					double actualRate =
						((double) outputPicQuantity) / inputPicQuantity;

					if ((desiredRate < actualRate) && (picType == Constants.B_PIC)) {
						add = false;
					}
				} else
					outputPicQuantity++;

				inputPicQuantity++;
			}
		}

		return add;
	}

	public boolean isInserted() {
		boolean insert = false;

		if (desiredRate > 1.0) {
			int picType = 0;
				//(mpeg2Processor.getLastPicture()).getPictureCodingType();

			if (inputPicQuantity != 0) {
				double actualRate =
					((double) outputPicQuantity) / inputPicQuantity;

				if ((desiredRate > actualRate) && (picType == Constants.B_PIC)) {
					insert = true;
					outputPicQuantity++; // devido à inserção do quadro duplicado
				}
			}
			outputPicQuantity++; // devido à inserção do quadro original
			inputPicQuantity++;
		}
		return insert;
	}
}