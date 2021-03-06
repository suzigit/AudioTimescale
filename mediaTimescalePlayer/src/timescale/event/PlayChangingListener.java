/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (14/09/2005)
 */
 
package timescale.event;

/**
 * Essa classe define a interface de classes que desejam observar a 
 * ocorrencia do evento de finalizacao de processamento do ajuste 
 * elastico de uma midia.
 */
public interface PlayChangingListener {
	
    public void actionPosCreateNewPlayer (PlayChangingEvent event);
    
    public void actionPosRealizeNewPlayer (PlayChangingEvent event);    
}
