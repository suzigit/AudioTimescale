/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (26/12/2005)
 */
package util.functions;

public class Semaphore {
	//Indica se processamento ja pode iniciar
	private boolean initiated = false;
	
	public synchronized void waitForInitialization() {
		if (!this.initiated) {
			try {
				wait();
			}
			catch (Exception e) {
				System.out.println("erro " + e.getMessage());
			}			
		}
	}

	public synchronized void setInitiated(boolean i) {
		if (!this.initiated && i==true) {
			try {
				notifyAll();
			}
			catch (Exception ex) {
				System.out.println("testes");
			}
		}				
		this.initiated = i;
	}
	
}
