/*
 * Essa classe faz parte do projeto de ajuste elastico de media comprimida
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (10/11/2005)
 */
package timescale.facade;

import java.io.IOException;

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.data.InstantsSet;
import timescale.data.ParametersProcessment;
import timescale.facade.MediaTimescaleFacade;
import util.io.InputTools;


/**
 * Fabrica de objetos de ajuste elastico. 
 */
public abstract class AbstractTimescaleFactory {
	
    public abstract IAssembler getAssembler(InputTools inputTools) 
    throws Exception;
    
    public abstract IDisassembler getDisassembler(String outputFile) 
    throws IOException;
    
    public abstract IDisassembler getDisassembler() throws IOException;
    
    public abstract MediaTimescaleFacade getMediaTimescalePlayer ();
    
    public ParametersProcessment getParametersProcessment(double rate, 
    		InstantsSet anchors, boolean mustPlay) {
    	ParametersProcessment parameters = new ParametersProcessment(rate, 
    			anchors, mustPlay);
    	return parameters;
    }
}

