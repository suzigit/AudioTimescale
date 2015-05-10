/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (10/03/2005)
 */


package timescale.audio.timescaleAnalyzer;

import java.io.FileWriter;
import java.io.IOException;

import util.functions.Convert;
import timescale.audio.util.constants.ConfigConstants;
import timescale.data.FinalReport;
import timescale.data.FrameProcessmentInfo;
import timescale.data.ParametersProcessment;

/**
 * Essa classe calcula metricas de qualidade do algoritmo executado.
 */
public class AlgorithmAnalyzer {
	
	private ParametersProcessment parameters;
	private FileWriter writer;
	
	/** 
	 * Controi instancia da classe.
	 * @param parameters dados a serem analizados 
	 */
	public AlgorithmAnalyzer (ParametersProcessment parameters) {
		this.parameters = parameters;
	}
	
	/** 
	 * Analiza dados e escreve em arquivo
	 * @param reportURL URL de arquivo com informacoes gerais
	 * @param reportDataURL URL de arquivo com informacoes dos quadros processados  
	 */	
	public void analyze (String reportURL, String reportDataURL) throws Exception {
		writer = new FileWriter(reportURL);		
		FinalReport report = parameters.getReport();	
		String s = null;
		
		s = "Terminou com sucesso em " + (report.getProcessmentTime()) + " milisegundos";
		print(s);
		
		//tempo por frame
		int numberOfWrittenFrames = report.getNumberOfWrittenFrames();
		double timePerFrame = (double) report.getProcessmentTime()/numberOfWrittenFrames;
		s = "Tempo por frame escrito " + Convert.roundDouble(timePerFrame,2) + " milisegundos";
		print(s);
		
		s = "Taxa final aplicada:" + parameters.getFactor().getScaleFactor()*100 + "%";
		print(s);
		s = "Taxa final para processar:" + Convert.roundDouble(parameters.getFactor().getProcessingRate()*100,2) + "%";
		print(s);
		s = "Taxa real atingida:" + Convert.roundDouble(report.getRealRate()*100,2) + "%";
		print(s);
		
		writer.write("Número de frames lidos:" + report.getNumberOfReadFrames() + "\n");
		writer.write("Número de frames escritos:" + report.getNumberOfWrittenFrames() + "\n");

		FrameProcessmentInfo[] fpi = report.getArrayProcessedFrames();
		this.printCSV(ConfigConstants.REPORT_IDS_DATA_URL, fpi);

		if (fpi.length>1) {

			int[] values = new int[fpi.length-1];		
			for (int i=0; i<values.length; i++) {								
				values[i] = (int) (fpi[i+1].getId() - fpi[i].getId()); 
			}
			
			int length = this.max(values)+1;
			double[] frequencies = new double[length];
			for (int i=0; i<values.length; i++) {
				int index = values[i];
				frequencies[index]++;
			}
			
			this.printCSV(reportDataURL, frequencies);
			
			//Esperança
			double median = extractMedian(frequencies, values.length, 1);
			print("Esperança=" + median);				
	
			//desvio padrão			
			double medianQuad = extractMedian(frequencies, values.length, 2);
			double variance = medianQuad - median*median;
			double stddev = Math.sqrt(variance);
			print("Desvio Padrão=" + stddev);
		}
					
		writer.close();		
	}
	
	private int max(int[] a) {
		int max = a[0];
		for (int i=0; i<a.length; i++) {
			if (max<a[i]) {
				max=a[i];
			}
		}
		return max;
	}
	
	private double extractMedian(double[] frequencies, int size, int pow) {
		double median = 0;
		for (int i=1; i<frequencies.length; i++) {
			median += (Math.pow(i,pow)*frequencies[i]);
		}
		median /= size;
		return median;
	}

	private void print(String s) throws Exception {
		System.out.println(s);
		writer.write(s + "\n");
	}
	
	private void printCSV(String dataURL, double [] data) throws IOException {
		FileWriter writer = new FileWriter(dataURL);
		
		for (int i=0; i<data.length; i++) {
			String textData = Double.toString(data[i]);
			textData = textData.replace('.', ',');
			writer.write(textData + "\n");			
		}		
		writer.close();	
	}	
	
	private void printCSV(String dataURL, FrameProcessmentInfo[] data) throws IOException {
		FileWriter writer = new FileWriter(dataURL);
		
		for (int i=0; i<data.length; i++) {
			String textData = Integer.toString((int) (data[i].getId()));
			writer.write(textData + "\n");			
		}		
		writer.close();	
	}	
}
