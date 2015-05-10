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
import timescale.data.Factor;
import timescale.util.constants.Constants;
import util.functions.Functions;

/**
 * Essa classe realiza ajuste elastico em um frame de audio.
 */
public class FrameProcessor {

	private Factor rate;
	
	/** 
	 * Controi instancia da classe.
	 * @param parameters parametros de processamento
	 */	
	public FrameProcessor (Factor rate) {
		this.rate = rate;
	}
	
	/** 
	 * Tenta processar quadro - dependendo das caracteristicas do quadro
	 * (tamanho dos dados fisicos e logicos) e do PAD pode conseguir ou nao.
	 * @param frames conjunto de quadros que estao sendo processados
	 * @param frame quadro a ser processado
	 * @param pad PAD do fluxo de dados
	 */
	public boolean tryProcessFrame (FramesCollection frames, Frame frame,  
			PADData pad) {
		
		boolean isThereProcessment = false;
		int indexInCollection = frames.indexInCollection(frame.getId());
		if (this.rate.getMode() == Constants.TimescaleOperation.CUT) {
	
			if (frames.lengthLogicalDataInBytes(indexInCollection) 
						>= frame.getPhysicalData().lengthInBytes()) {			

					int deltaInBytes = cutLargeFrame(frames, frame.getId());

					pad.setNumberOfBytes(deltaInBytes);								
					isThereProcessment = true;
			}	
		}
		else if (this.rate.getMode() == Constants.TimescaleOperation.INSERT) {
			
			if ((frames.lengthLogicalDataInBytes(indexInCollection) - 2*pad.getNumberOfBytes()) 
					<= frame.getPhysicalData().lengthInBytes()) {
 				
					int deltaInBytes = insertSmallFrame (frames, frame.getId(), pad);

					pad.setNumberOfBytes(deltaInBytes);														
					isThereProcessment = true;							
			}									
		}			 	
		return isThereProcessment;		 	
	}

	/* Remove frame com dado_lógico > dado_físico
	 * Deixa PAD restante no final do frame anterior ao que foi removido
	 * Retorna o número:  dado_lógico - dado_físico
	 */
	private static int cutLargeFrame (FramesCollection frames, 
			ElementID idProcessedFrame) {
		
		AMainDataHandler extractor = AMainDataHandler.getMainDataHandler(frames);
		 
		int indexProcessedFrame = frames.indexInCollection(idProcessedFrame);				
		Frame f = frames.elementAt(indexProcessedFrame);
		
		//***** Passa metadados para proximo frame *********** //
		if (frames.isValidIndex(indexProcessedFrame+1)) {
			Frame nextFrame = frames.elementAt(indexProcessedFrame+1);
			if (f.getMetadata()!=null) {
				if (nextFrame.getMetadata()!=null) {
					if (nextFrame.getMetadata().getIndexMetadata()!=0) {
						System.err.println("FrameProcessor: erro - nao estou tratando isso");	
					}
				}
				else {
					nextFrame.setMetadata(f.getMetadata());
					nextFrame.getMetadata().setIndexMetadata(0);
				}
			}
		}
		
		//*******Determina o index de onde deve 
		//       começar a sobrescrever dado lógico do frame a ser retirado  
		//       e onde começa dado a ser inserido  *****
		AMainDataHandler.TuplaIndex tiLogicalDataF = extractor.findIndexBeginLogicalData (indexProcessedFrame);		

		int lengthLogicalData = frames.lengthLogicalDataInBytes(indexProcessedFrame);
		int deltaInBytes = lengthLogicalData - f.getPhysicalData().lengthInBytes();
		
		//Necessário pois bits restantes serão considerados ancillary data de frame anterior 
		AMainDataHandler.TuplaIndex tiBeginOldData = extractor.incrementTuplaIndex (tiLogicalDataF,deltaInBytes);		
		
		//***** Recupera string do dado a ser inserido ********	
		AMainDataHandler.TuplaIndex tfLogicalDataF = extractor.incrementTuplaIndex (tiLogicalDataF,lengthLogicalData);
		AMainDataHandler.TuplaIndex tfPhysicalDataF = extractor. new TuplaIndex(indexProcessedFrame,f.getPhysicalData().lengthInBytes());
		int lengthInBytes = extractor.lengthInBytesBetweenTuplas (tfLogicalDataF, tfPhysicalDataF);
		byte[] newData = extractor.getData (tfLogicalDataF, lengthInBytes);

		//*********** Modifica bits *******************
		extractor.setData (tiBeginOldData, newData);	

		//*********** Remove frame *******************
		frames.remove(indexProcessedFrame);	
		
		return deltaInBytes;

	}

	private static int insertSmallFrame (FramesCollection frames, 
			ElementID idFrameToProcess, PADData pad) {
		
		// ******** Recupera frame a processar e calcula parâmetros ********** //
		int indexFrameToProcess = frames.indexInCollection(idFrameToProcess);	
		int lengthLogicalData = frames.lengthLogicalDataInBytes(indexFrameToProcess);								
		Frame f = frames.elementAt(indexFrameToProcess);
		int deltaInBytes = f.getPhysicalData().lengthInBytes() - lengthLogicalData + 2*pad.getNumberOfBytes();

		AMainDataHandler extractor = AMainDataHandler.getMainDataHandler(frames);		
		
		//***** Procuramos o index em que o dado lógico de f inicia e 
		//  recuperamos a string newData que é composto por duas partes concatenadas 
		//  A primeira contém o dado lógico de f (sem pad) 
		//  A segunda contém os dados entre o final do pad e o final do dado físico de f, 
		// que equivalente a dizer (se existir um próximo frame = pf) os dados entre 
		// o início do dado lógico do pf e o ínicio do dado físico do pf 
		// ********
		AMainDataHandler.TuplaIndex tiLogicalDataFrame = extractor.findIndexBeginLogicalData (indexFrameToProcess);
		int lengthInBytes = lengthLogicalData - pad.getNumberOfBytes();
		byte[] newDataPart1 = extractor.getData (tiLogicalDataFrame, lengthInBytes);
		
		byte[] newDataPart2 = null;
		if (frames.isValidIndex(indexFrameToProcess + 1)) {
			
			AMainDataHandler.TuplaIndex tiLogicalDataNextFrame = extractor.findIndexBeginLogicalData (indexFrameToProcess+1);
			AMainDataHandler.TuplaIndex tiPhysicalDataNextFrame = extractor.new TuplaIndex (indexFrameToProcess+1,0);
			
			lengthInBytes = extractor.lengthInBytesBetweenTuplas (tiLogicalDataNextFrame, tiPhysicalDataNextFrame);		
			newDataPart2 = extractor.getData (tiLogicalDataNextFrame, lengthInBytes);
		}
		
		byte[] newData;
		if (newDataPart2!=null) {
			newData = Functions.appendArrayInAnotherArray(newDataPart1,newDataPart2);
		}
		else {
			newData = newDataPart1;
		}
		lengthInBytes = newData.length;   			
		
		//******** Cria e insere novo frame no conjunto ********** //	
		Frame newFrame = f.cloneWithoutMetadata();
		frames.add(indexFrameToProcess+1,newFrame);
		int indexNewFrame = indexFrameToProcess + 1;
		ElementID newId = createIdFrame(frames, indexNewFrame);
		newFrame.setId(newId);
		//Atualiza main_data_end, pois não contém PAD
		newFrame.incrementNumberOfBorrowBytes(-pad.getNumberOfBytes());
				
		
		//******* Determina o index de onde deve começar a sobrescrever 
		//        os dados e sobrescreve 
		AMainDataHandler.TuplaIndex tfPhysicalDataNewFrame = extractor.new TuplaIndex (indexNewFrame,newFrame.getPhysicalData().lengthInBytes()); 
		AMainDataHandler.TuplaIndex tiOldData = extractor.incrementTuplaIndex (tfPhysicalDataNewFrame, -lengthInBytes);
		extractor.setData (tiOldData, newData);
		
		return deltaInBytes;
	}
	
	private static ElementID createIdFrame(FramesCollection frames, int index) {		
		ElementID value;
		Frame previusFrame = frames.elementAt(index-1);
		
		//Verifica se existe frame depois
		if (frames.isValidIndex(index + 1)) {
			Frame nextFrame = frames.elementAt(index+1);
			value = ElementID.createIDBetweenElements(previusFrame.getId(), nextFrame.getId());				  	
		}
		else {
			value = ElementID.nextIDToNewFrame(previusFrame.getId());
		}			  
		return value;
	}

}
