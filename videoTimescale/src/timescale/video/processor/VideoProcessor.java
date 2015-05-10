/*
 * Created on 26/08/2004
 */
package timescale.video.processor;

import java.util.Vector;

import timescale.data.ElementID;
import timescale.data.FinalReport;
import timescale.event.TimescaleInstantListener;
import timescale.instantsRetrieval.InstantsRetrieval;
import timescale.video.mpeg2Elements.Picture;
import timescale.video.mpeg2Elements.Sequence;
import timescale.video.mpeg2Elements.SequenceInformation;
import timescale.video.mpeg2Elements.VideoObject;
import timescale.video.statistics.StatsCollector;
import timescale.video.utils.Constants;

/**
 * @author Sergio Cavendish
 */
public class VideoProcessor extends Thread{
	
	private Buffer inputBuffer;
	private Buffer outputBuffer;
	StatsCollector statCollector = StatsCollector.instance();
	private Object object;
	private boolean end = false;
	private double desiredRate;
	
	private InstantsRetrieval anchorsRetrieval;
	private FinalReport finalReport;
	
	public VideoProcessor(Buffer input, Buffer output, FinalReport report){
		this.inputBuffer = input;
		this.outputBuffer = output;
		this.desiredRate = 1.0;
		this.anchorsRetrieval = new InstantsRetrieval (report);
		this.finalReport = report;
	}
	
	public void addAnchorListener(TimescaleInstantListener listener) {
		this.anchorsRetrieval.addAnchorListener(listener);
	}
	
	public void run() {
		try {
			
			do {
				/*
				 * Leitura de objetos (stuffing ou sequences) do inputBuffer.
				 */
				object = inputBuffer.getSharedObject();
				if (object instanceof VideoObject) {
					object = processVideo((Sequence)object, false, desiredRate);
				}
				
				/*
				 * Escrita do object processado. Caso object não seja um VideoObject, 
				 * os dados tambem sao transferidos para a saida.
				 */
				outputBuffer.setSharedObject(object);
				object = null;
			} while (end == false);
		} catch (Exception e) {
			System.out.println("Processor Error: " + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	public void setEnd() {
		end = true;
	}

	
	//TODO: Sergio, falta somente adicionar ao report quando duplicar ou remover um 
	// quadro para as ancoras funcionarem 
	
	/*
	 * Solicita o processamento de uma sequencia. O parametro repeat indica apenas 
	 * se as operacoes de repetir quadros (atraves do parametro repeatFirstField, entre
	 * outros) devem ser utilizadas.
	 */
	private Sequence processVideo(Sequence seq, boolean repeat, double desiredRate){
		Sequence result = seq;
		
		if(repeat)
			result = processVideoWithRepeat(seq, desiredRate);
		else
			result = processVideoWithoutRepeat(seq, desiredRate);
		return result;
	}
	
	private Sequence processVideoWithRepeat(Sequence seq, double desiredRate){
		Sequence result = seq;
		SequenceInformation seqInf = seq.getSeqInformation();
		//		SequenceInformation newSeqInf = new SequenceInformation();
		double stretchRate = desiredRate;
		
		if(stretchRate > 1.0){
			int framesToAdjust = (int)(stretchRate * seqInf.getNumberOfPics());
			
			/* Quantidade máxima de quadros que podem ser repetidos na saida do 
			 * decodificador ou cuja repeticao pode ser aumentada.
			 */
			int repeatNumber = 2 * seqInf.getReplicableList().size() + 
			seqInf.getDuplicatedList().size();
			
			/*
			 * framesToReplicate indica o número de quadros que devem ser replicados 
			 * através do processo de add/drop.
			 */
			int framesToReplicate = framesToAdjust- repeatNumber;
			
			//			int framesReplicated = replicateFrames(framesToReplicate, seq);
			
			int newRepeatNumber = seqInf.getReplicableList().size() + 
			seqInf.getDuplicatedList().size();
			
			int framesRepeated = 0;
			//			if((framesToAjdust - framesReplicated) <= newRepeatNumber)
			//				framesRepeated = repeatFrames(framesToAjdust - framesReplicated);
			//			else
			//				framesRepeated = repeatFrames(newRepeatNumber);
			
			//			framesAdjusted = framesReplicated + framesRepeated;
			
		}
		return result;
	}
	
	/**
	 * Faz o ajuste sem utilizar os valores do parametro repeatFirstField, entre outros.
	 */
	private Sequence processVideoWithoutRepeat(Sequence seq, double desiredRate){
		Sequence result = seq;
		
		SequenceInformation seqInf = seq.getSeqInformation();
		
		double stretchRate = desiredRate;
		
		int inputPicQuantity = seqInf.getNumberOfPics();
		//int inputPicQuantity = seqInf.getNumberOfPresentationPics();
		int finalOutputPicQuantity = 0;
		int outputPicQuantity = (int)(stretchRate * inputPicQuantity);
		
		if(outputPicQuantity > inputPicQuantity){
			finalOutputPicQuantity = 
				addFrames(seq, (outputPicQuantity - inputPicQuantity)) + inputPicQuantity;
		}
		else if(outputPicQuantity < inputPicQuantity){
			finalOutputPicQuantity = 
				inputPicQuantity - dropFrames(seq, (inputPicQuantity - outputPicQuantity));
		}
		
		return result;
	}
	
	/**
	 * Retira quadros de uma seqüência. Retorna o número de quadros retirados.
	 * @param seq
	 * @param quantity
	 * @return
	 */
	private int dropFrames(Sequence seq, int quantity){
		int result = 0;
		SequenceInformation seqInf = seq.getSeqInformation();
		int numberOfBFrames = seqInf.getBList().size();
		
		/*
		 * Faz descarte com prioridade: primeiro quadros B, depois P e, por último, I. 
		 */
		result = dropTypeFrames(seq, quantity, Constants.B_PIC);
		
		if(result < quantity)
			result += dropTypeFrames(seq, (quantity - result), Constants.P_PIC);
		if(result < quantity)
			result += dropTypeFrames(seq, (quantity - result), Constants.I_PIC);
		
		return result;
	}
	
	/**
	 * Retira quadros B de uma seqüência. Retorna o número de quadros retirados. 
	 */
	private int dropTypeFrames(Sequence seq, int quantity, int type){
		int result = 0;
		SequenceInformation seqInf = seq.getSeqInformation();
		Vector sequenceElementVector = seq.getSequenceElementVector();
		Vector list = new Vector();
		boolean directOrder = true;
		
		switch(type){
		case Constants.B_PIC:
			list = seqInf.getBList();
		break;
		case Constants.P_PIC:
			list = seqInf.getPList();
		directOrder = false;
		break;
		case Constants.I_PIC:
			list = seqInf.getIList();
		break;
		default:
		}
		
		int listSize = list.size();
		/*
		 * Garante a compatibilidade entre a quantidade solicitada e o número de 
		 * quadros existentes.
		 */
		if (quantity > listSize)
			quantity = listSize;
		
		/*
		 * Calcula como o descarte deve ser realizado, visando um descarte uniforme na 
		 * seqüência.
		 */
		int intervalo = listSize / quantity;
		
		/*
		 * O descarte de quadros B e I ocorre na ordem direta em que eles aparecem
		 * na seqüência. O descarte de quadros P ocorre na ordem inversa em que eles
		 * aparecem na seqüência. Para quadros P, o descarte tem que ser de quadros
		 * consecutivos.
		 */
		if(directOrder){
			int picListIndex = intervalo - 1;
			
			while(result < quantity){
				Picture pic = (Picture)list.get(picListIndex);
				sequenceElementVector.remove(pic);
				
				//TODO: Sergio, coloca essa linha em um lugar para executar uma vez em todo programa
				this.finalReport.setFrameDuration(seq.getSequenceHeader().getDuration());
				
				System.out.println("VideoProcessor: Removeu picture " + pic.getID().getValue());
				this.finalReport.putIdProcessedFrame(pic.getID().getValue(), timescale.util.constants.Constants.TimescaleOperation.CUT);
				list.remove(picListIndex);
				
				picListIndex += intervalo - 1;
				result++;
			}
		}
		else{
			int picListIndex = listSize - 1;
			
			while(result < quantity){
				Picture pic = (Picture)list.get(picListIndex--);
				sequenceElementVector.remove(pic);
				list.remove(picListIndex);
				
				result++;
			}
		}
		
		return result;
	}
	
	/**
	 * Adiciona quadros a uma seqüência. Retorna o número de quadros adicionados.
	 * @param seq
	 * @param quantity
	 * @return
	 */
	private int addFrames(Sequence seq, int quantity){
		int result = 0;
		SequenceInformation seqInf = seq.getSeqInformation();
		/*
		 * Se existir quadros B, apenas estes serão repetidos.
		 */
		if(!seqInf.getBList().isEmpty())
			result = addTypeFrames(seq, quantity, Constants.B_PIC);
		else{
			/*
			 * Repetição de quadros P.
			 */
			int numberOfRepeat = quantity / seqInf.getPList().size();
			if((quantity % seqInf.getPList().size()) != 0)
				numberOfRepeat++;
			/*
			 * Limita o número de repetições de cada quadro P em 3.
			 */
			if(numberOfRepeat <= 3){
				result = addTypeFrames(seq, quantity, Constants.P_PIC);
			}
			else{
				/*
				 * Caso em que a quantidade de quadros a serem inseridos será 
				 * distribuída entre quadros P e I. Cálcula o número de repetições de cada
				 * quadro P e I. 
				 */
				int nPics = seqInf.getPList().size() + seqInf.getIList().size();
				numberOfRepeat = quantity / nPics;
				if((quantity % nPics) != 0)
					numberOfRepeat++;
				
				int numberOfPFrames = seqInf.getPList().size() * numberOfRepeat;
				int numberOfIFrames = quantity - numberOfPFrames;
				
				result = addTypeFrames(seq, numberOfPFrames, Constants.P_PIC);
				result += addTypeFrames(seq, numberOfIFrames, Constants.I_PIC);
			}
		}
		return result;
	}
	
	/**
	 * Adiciona quadros, do tipo especificado, a uma seqüência. Retorna o número de 
	 * quadros inseridos.
	 * @param seq
	 * @param quantity
	 * @return
	 */
	private int addTypeFrames(Sequence seq, int quantity, int type){
		int result = 0;
		
		SequenceInformation seqInf = seq.getSeqInformation();
		Vector sequenceElementVector = seq.getSequenceElementVector();
		Vector list = new Vector();
		
		switch(type){
		case Constants.B_PIC:
			list = seqInf.getBList();
		break;
		case Constants.P_PIC:
			list = seqInf.getPList();
		break;
		case Constants.I_PIC:
			list = seqInf.getIList();
		break;
		default:
		}
		
		/*
		 * Verifica se existe figuras do tipo especificado.
		 */
		if(!list.isEmpty()){
			/*
			 * Cálculo do número de vezes que cada quadro deve ser repetido. Utilizado
			 * para distribuir a quantidade de quadros a serem repetidos pelos vários 
			 * quadros I existentes.
			 */
			int numberOfRepeat = quantity / list.size();
			if((quantity % list.size()) != 0)
				numberOfRepeat++;
			
			/*
			 * Repetição de cada quadro de acordo com o número de vezes determinado em 
			 * numberOfRepeat.
			 */
			int picListIndex = 0;
			while (result < quantity){
				Picture pic = (Picture)list.elementAt(picListIndex++);
				int picSeqElemVectorIndex = sequenceElementVector.indexOf(pic);
				
				int count = 0;
				while ((count < numberOfRepeat) && (result < quantity)){
					Picture clonePic = (Picture)pic.clone();
					
					/*
					 * Inserção da cópia da figura na lista iList e em sequenceElementVector.
					 */
					list.add(picListIndex++, clonePic);
					clonePic.setID(ElementID.nextIDToNewFrame(pic.getID()));
										
					System.out.println("VideoProcessor: Clonou picture " + pic.getID().getValue());
					
					//TODO: Sergio, coloca essa linha em um lugar para executar uma vez em todo programa
					this.finalReport.setFrameDuration(seq.getSequenceHeader().getDuration());
					
					this.finalReport.putIdProcessedFrame(pic.getID().getValue(), timescale.util.constants.Constants.TimescaleOperation.INSERT);
					sequenceElementVector.add(picSeqElemVectorIndex++, clonePic);
					
					result++;
					count++;
				}
			}
		}
		
		return result;
	}
	

/**
 * Adiciona quadros B a uma seqüência. Retorna o número de quadros adicionado.
 */
private int addBFrames(Sequence seq, int quantity){
	SequenceInformation seqInf = seq.getSeqInformation();
	Vector sequenceElementVector = seq.getSequenceElementVector();
	int result = 0;
	
	/*
	 * Verifica se existe figuras B.
	 */
	if(!seqInf.getBList().isEmpty()){
		/*
		 * Cálculo do número de vezes que cada quadro B deve ser repetido. Utilizado
		 * para distribuir a quantidade de quadros a serem repetidos pelos vários 
		 * quadros B existentes.
		 */
		int numberOfRepeat = quantity / seqInf.getBList().size();
		if((quantity % seqInf.getBList().size()) != 0)
			numberOfRepeat++;
		
		/*
		 * Repetição de cada quadro B de acordo com o número de vezes determinado em 
		 * numberOfRepeat.
		 */
		int picBListIndex = 0;
		while (result < quantity){
			Picture pic = (Picture)seqInf.getBList().elementAt(picBListIndex++);
			int picSeqElemVectorIndex = sequenceElementVector.indexOf(pic);
			
			int count = 0;
			while ((count < numberOfRepeat) && (result < quantity)){
				Picture clonePic = (Picture)pic.clone();
				
				/*
				 * Inserção da cópia da figura na lista bList e em sequenceElementVector.
				 */
				seqInf.getBList().add(picBListIndex++, clonePic);
				sequenceElementVector.add(picSeqElemVectorIndex++, clonePic);
				
				result++;
				count++;
			}
		}
	}
	return result;
}

	public void setDesiredRate(double rate){
		this.desiredRate = rate;
	}
	
	public double getDesiredRate(){
		return this.desiredRate;
	}

	/**
	 * @return
	 */
	public double getActualStretchRate() {
		
		return this.desiredRate;
	}
}
