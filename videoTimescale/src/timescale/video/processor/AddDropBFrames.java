/*
 * Created on 28/09/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.processor;

import timescale.video.mpeg2Elements.Picture;
import timescale.video.utils.Constants;

/**
 * @author Sergio Cavendish
 */
public class AddDropBFrames extends ProcessorStrategy {

	/*
	 * Tarefas que devem ser realizadas:
	 * 1- Solicitar dados estat�sticos.
	 * 2- Calcular o n�mero de quadros que devem ser retirados/inseridos.
	 * 3- Controlar o momento em que o ajuste (retirada/inser��o) deve acontecer.
	 * 4- Atualizar os dados estat�sticos para continuar a opera��o.
	 * 
	 * Para tanto, � necess�rio que esta classe guarde a taxa obtida ap�s o ajuste.
	 */
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
					double actualRate =	((double) outputPicQuantity) / inputPicQuantity;

					if ((actualRate > desiredRate) && (picType == Constants.B_PIC)) {
						add = false;
					} else{
						outputPicQuantity++;
					}
				} else{
					outputPicQuantity++;
				}
				
				inputPicQuantity++;
			}
		}
		
		return add;
	}

	public boolean isInserted(int type) {
		int picType = type;
		boolean insert = false;

		if (desiredRate > 1.0) {

			if (inputPicQuantity != 0) {
				double actualRate = ((double) outputPicQuantity) / inputPicQuantity;

				if ((actualRate < desiredRate) && (picType == Constants.B_PIC)) {
					insert = true;
					outputPicQuantity++;
					// devido � inser��o do quadro duplicado
				}
			}
			outputPicQuantity++; // devido � inser��o do quadro original
			inputPicQuantity++;
		}
		
		return insert;
	}
	
	public double getActualStretchRate(){
		return (double)outputPicQuantity / inputPicQuantity;
	}
}