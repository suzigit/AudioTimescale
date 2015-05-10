/*
 * Created on 25/08/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */
package timescale.video.statistics;

import timescale.video.utils.Constants;

/**
 * Esta classe Singleton contém todas as informações estatísticas. Durante o processo de leitura do arquivo
 * de entrada, para cada estrutura existente, é enviada uma mensagem de atualização dos referidos parâmetros.
 * 
 * @author Sergio Cavendish
 */
public class StatsCollector {

	private Stats inputStats;
	private Stats actualStats;
	private Stats outputStats;

	private static StatsCollector myInstance = null;
	private StatsStrategy strategy;
	
	/*
	 * Inicio do estudo sobre progressive_sequence, low_delay, repeat_first_field e 
	 * top_field_first.
	 */ 
	private int progressiveSequenceCounter0, progressiveSequenceCounter1;
	private int lowDelayCounter0, lowDelayCounter1;
	private int repeatFirstFieldCounter0, repeatFirstFieldCounter1;
	private int pictureStructure0, pictureStructure1, pictureStructure2,
		pictureStructure3;
	private int progressiveFrameCounter0, progressiveFrameCounter1;
	private int topFieldFirst0, topFieldFirst1;
	// fim do estudo

	/**
	 * Contrutor do coletor de estatísticas com uma política de armazenamento específica determinada pelo
	 * strategyType. Foi projetada como um Singleton, uma vez que que todas as estruturas de vídeo devem 
	 * atualizar suas informações e apenas haverá um objeto dessa classe.
	 * 
	 * Constructor of the collector of statistics with a specified politics of storing determined
	 * by strategyType. It is designed as a Singleton, once all video structures will update its information
	 * and there is going to be only one object.
	 * @param fileName
	 * @param strategyType
	 */
	protected StatsCollector(StatsStrategy strat) {

		inputStats = new Stats();
		actualStats = new Stats();
		outputStats = new Stats();

		/**
		 * Determina a estratégia a ser utilizada. Outros tipos podem ser definidos depois. Significa um 
		 * ponto de adaptação no programa.
		 * 
		 * It determines the strategy to be used. Other types may be additionated later. This provides an 
		 * adaptation point.
		 */
		this.strategy = strat;
		
		progressiveSequenceCounter0 = 0;
		progressiveSequenceCounter1 = 0;
		lowDelayCounter0 = 0;
		lowDelayCounter1 = 0;
		repeatFirstFieldCounter0 = 0;
		repeatFirstFieldCounter1 = 0;
	}

	/*
	 * Método para criação do Singleton, recebendo o nome do arquivo e a estratégia de 
	 * armazenamento dos dados.
	 */
	public static StatsCollector instance(StatsStrategy strat) {
		return myInstance = new StatsCollector(strat);
	}

	/*
	 * Caso o usuário não defina a estratégia a ser utilizada, é adotada a LastSequenceStrtegy
	 */
	public static StatsCollector instance(String fileName) {
		return myInstance = new StatsCollector(new LastSequenceStrategy());
	}

	public static StatsCollector instance() {
		return myInstance;
	}

	/**
	 * Atualiza os contadores de acordo com os dados originais, finais e a política 
	 * definida na estratégia.
	 * 
	 * Update the counters according with the politics defined in strategy.
	 * @param element
	 */
	public void updateStats(int elementType, int statistics) {
		switch (statistics) {
			case Constants.INPUT :
				inputStats.update(elementType);
				strategy.updateStats(elementType, actualStats);
				break;
			case Constants.OUTPUT :
				outputStats.update(elementType);
				break;
		}
	}

	/**
	 * Retorna as informações estatísticas de acordo com o tipo específico (original, atual ou final).
	 * 
	 * Returns the stats information required according to type.
	 * @param type
	 * @return
	 */
	public Stats getStats(int type) {
		Stats temp = new Stats();

		switch (type) {
			case Constants.ACTUAL :
				temp = actualStats;
				break;
			case Constants.INPUT :
				temp = inputStats;
				break;
			case Constants.OUTPUT :
				temp = outputStats;
				break;
		}

		return temp;
	}
	/**
	 * @return
	 */
	public StatsStrategy getStatsStrategy() {
		return strategy;
	}

	/**
	 * Permite que a estratégia seja alterada durante a execução do programa.
	 * @param strategy
	 */
	public void setStatsStrategy(StatsStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return
	 */
	public String getInputFile() {
		return inputStats.getFileName();
	}

	/**
	 * @param string
	 */
	public void setInputFile(String string) {
		inputStats.setFileName(string);
	}

	/**
		 * @return
		 */
	public String getOutputFile() {
		return outputStats.getFileName();
	}

	/**
	 * @param string
	 */
	public void setOutputFile(String string) {
		outputStats.setFileName(string);
	}

	public void clearStats(int type) {
		switch(type){
			case Constants.INPUT:
				inputStats.clearStats();
				break;
			case Constants.OUTPUT:
				outputStats.clearStats();
				break;
			case Constants.ACTUAL:
				actualStats.clearStats();
				break;
		}
	}
	
	public void clearAllStats() {
		inputStats.clearStats();
		outputStats.clearStats();
		actualStats.clearStats();
	}
	
	
	/**
	 * 
	 */
	public void incLowDelayCounter(int num) {
		if(num == 0)
			this.lowDelayCounter0++;
		else
			this.lowDelayCounter1++;
	}
	
	/**
	 * 
	 */
	public void incProgressiveSequenceCounter(int num) {
		if(num == 0)
			this.progressiveSequenceCounter0++;
		else
			this.progressiveSequenceCounter1++;
	}
	
	/**
	 * 
	 */
	public void incRepeatFirstFieldCounter(int num) {
		if(num == 0)
			this.repeatFirstFieldCounter0++;
		else
			this.repeatFirstFieldCounter1++;
	}
	
	/**
	 * 
	 */
	public void incPictureStructureCounter(int num) {
		switch(num){
			case 0:
				pictureStructure0++;
				break;
			case 1:
				pictureStructure1++;
				break;
			case 2:
				pictureStructure2++;
				break;
			case 3:
				pictureStructure3++;
			default:
		}
	}
	
	/**
	 * 
	 */
	public void incProgressiveFrameCounter(int num) {
		if(num == 0)
			this.progressiveFrameCounter0++;
		else
			this.progressiveFrameCounter1++;
	}
	
	/**
	 * 
	 */
	public void incTopFieldFirstCounter(int num) {
		if(num == 0)
			this.topFieldFirst0++;
		else
			this.topFieldFirst1++;
	}
}