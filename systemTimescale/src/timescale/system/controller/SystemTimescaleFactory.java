/*
 * Essa classe faz parte do projeto de ajuste elastico de media comprimida
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (25/11/2005)
 */

package timescale.system.controller;

import java.io.IOException;

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.facade.AbstractTimescaleFactory;
import timescale.facade.MediaTimescaleFacade;
import timescale.system.assembler.SystemAssembler;
import timescale.system.assembler.SystemDisassembler;
import timescale.util.constants.Constants;
import util.data.SharedBuffer;
import util.io.ByteOutputTools;
import util.io.FileOutputTools;
import util.io.InputTools;
import util.io.OutputTools;

public class SystemTimescaleFactory extends AbstractTimescaleFactory {

    public IAssembler getAssembler(InputTools inputTools) throws Exception {
    	IAssembler assembler = new SystemAssembler(inputTools);
		return assembler;
    }
    
    public IDisassembler getDisassembler(String outputFile) throws IOException {
		OutputTools outputTools = new FileOutputTools(outputFile); 
		IDisassembler disassembler = new SystemDisassembler(outputTools);
    	return disassembler;
    }
    
    public IDisassembler getDisassembler() throws IOException {
		SharedBuffer buffer = new SharedBuffer("systembuffer", 
				Constants.SHARED_BUFFER_LENGTH, Constants.RESERVED_BUFFER_LENGTH);
		ByteOutputTools writer = new ByteOutputTools(buffer);
		IDisassembler disassembler = new SystemDisassembler(writer);
		return disassembler;
    }
    
    public MediaTimescaleFacade getMediaTimescalePlayer () {
		MediaTimescaleFacade mediaPlayer = new SystemTimescaleFacade();		
		return mediaPlayer;
    }
    
}
