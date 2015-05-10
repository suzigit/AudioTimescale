/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (13/09/2004)
 */

package timescale.net;

import javax.media.protocol.DataSource;

public interface IRTPHandlerData {
	
    public void receiveStream (DataSource dataSouce) throws Exception;

}
