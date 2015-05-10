package timescale.data;

public class ParametersProcessment {
	private Factor factor;
	private FinalReport report;
	private boolean mustPlay;
	private boolean alive;
	
	public ParametersProcessment (double dFactor, InstantsSet anchors, 
			boolean mustPlay)	{
		this.factor = new Factor(dFactor);
		this.mustPlay = mustPlay;
		this.report = new FinalReport(anchors);
		this.alive = true;
	}
	
	/** 
	 * Verifica se esta ocorrendo ajuste elastico.
	 * @return true sse esta ocorrendo ajuste elastico
	 */		
	public boolean getAlive() {
	    return this.alive;
	}

	/** 
	 * Indica que ajuste esta ocorrendo se true e acabou se false.
	 * @param a valor true/false que indica se ajuste esta ocorrendo
	 */	
	public void setAlive(boolean a) {
	    this.alive = a;
	}	
	
	/** 
	 * Recupera taxa de ajuste.
	 * @return taxa de ajuste
	 */	
	public synchronized Factor getFactor() {
		return this.factor;
	}

	/** 
	 * Modifica valor da taxa de ajuste.
	 * @param r novo valor da taxa de ajuste
	 */	
	public synchronized void setRate(Factor r) {
		//nao pode criar outro objeto, tem que modificar o mesmo.
		this.factor.setFactor(r.getValue());
		//System.out.println("mudou taxa para " + r.getValue());		
	}

	public boolean mustPlay() {
		return this.mustPlay;
	}
	
	public FinalReport getReport() {
		return this.report;
	}
	
	public Object clone() {
		return new ParametersProcessment (this.factor.getValue(), 
				this.report.getAnchors(), this.mustPlay);
	}

}
