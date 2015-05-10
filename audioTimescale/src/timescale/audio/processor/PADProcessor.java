/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (17/10/2004)
 */
 
package timescale.audio.processor;

import timescale.audio.format.generalFormat.AMainDataHandler;
import timescale.audio.format.generalFormat.Frame;
import timescale.audio.format.generalFormat.FramesCollection;
import timescale.data.ElementID;


/**
 * Essa classe é responsável por fazer o deslocamento do PAD.
 */
public class PADProcessor {


	/** Ajusta o PAD de um quadro. Ou seja, passa PAD para final do quadro.
	 * @param frames conjunto de quadros que estao sendo processados
	 * @param pad PAD do fluxo de dados
	 * @param idFrame identificador do quadro cujo PAD se deseja ajustar 
	 */
	public static void adjustPAD (FramesCollection frames, PADData pad, 
			ElementID idFrame) {

		int index = frames.indexInCollection(idFrame);
		Frame f = frames.elementAt(index);		

		AMainDataHandler extractor = AMainDataHandler.getMainDataHandler(frames);
		AMainDataHandler.TuplaIndex ti = extractor.findIndexBeginLogicalData(index);
		modifyPADToMax(f, pad);						
		AMainDataHandler.TuplaIndex tBeginUtilData = extractor.incrementTuplaIndex(ti,-pad.getNumberOfBytes());						
		byte[] newData = extractor.getData(ti, frames.lengthLogicalDataInBytes(index));
		extractor.setData (tBeginUtilData, newData);
		f.incrementNumberOfBorrowBytes(pad.getNumberOfBytes());
		
//System.out.println("- Ajeitou PAD de " + f.getId() + " do index " + index + " com delta de " + pad.getNumberOfBits() + " - lógico=" + frames.lengthLogicalData(index) + " - bytes emprestados = " + f.getNumberOfBorrowBytes());

	}

	/*
	 * Modifica, se necessário, o número de bytes do PAD para o máximo permitido pelo frame
	 * considerando restrições do número de bits emprestados máximo
	 * que cada frame pode conter
	 */
	private static void modifyPADToMax(Frame fPhysicalOfLogical, PADData pad) {
		int maxBytesAllocation = fPhysicalOfLogical.maxNumberOfBorrowedBytes();
		int actualBytesAllocation = fPhysicalOfLogical.getNumberOfBorrowBytes();
		int maxIncrementBitAllocation = maxBytesAllocation - actualBytesAllocation;
					
		if (maxIncrementBitAllocation <= pad.getNumberOfBytes()) {
				pad.setNumberOfBytes(maxIncrementBitAllocation);	
		} 
	}

}
