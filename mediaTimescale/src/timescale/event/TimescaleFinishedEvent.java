/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (15/05/2005)
 */
 
package timescale.event;

import java.util.EventObject;

import timescale.data.ParametersProcessment;
import timescale.data.RunResult;


/**
 * Essa classe e um evento que representa o final do processamento do 
 * ajuste elastico de uma midia.
 *
 * @author Suzana Mesquita de Borba Maranhão
 */
public class TimescaleFinishedEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	private ParametersProcessment parameters;
	private RunResult result;
	
	/** 
	 * Controi instancia da classe.
	 * @param parameters parametros de processamento
	 */
	public TimescaleFinishedEvent (ParametersProcessment parameters) {
		super(new Object());
		this.parameters = parameters;
	}
	
	public TimescaleFinishedEvent (RunResult result) {
		super(new Object());
		this.result = result;
	}
	
	/** 
	 * Retorna string que representa objeto.
	 * @return string que representa objeto
	 */
	public String toString() {
		return "Operação de ajuste realizada com sucesso";
	}

	/** 
	 * Retorna parametros de processamento do ajuste.
	 * @return parametros de processamento do ajuste
	 */	
	public ParametersProcessment getParameters() {
		return this.parameters;
	}
	
	public RunResult getResult() {
		return this.result;
	}
}
