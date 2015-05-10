/*
 * Essa classe faz parte do projeto de ajuste elastico de media comprimida
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (10/11/2005)
 */
package timescale.video.controller;

import java.io.FileInputStream;
import java.io.IOException;

import timescale.adapter.VideoTimescaleAdapter;
import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.facade.AbstractTimescaleFactory;
import timescale.facade.MediaTimescaleFacade;
import timescale.util.constants.Constants;
import timescale.video.assembler.VideoAssembler;
import timescale.video.assembler.VideoDisassembler;
import util.data.SharedBuffer;
import util.io.ByteOutputTools;
import util.io.FileOutputTools;
import util.io.InputTools;
import util.io.IoTools;
import util.io.OutputTools;

/**
 * Fabrica de objetos de ajuste elastico de video.
 */
public class VideoTimescaleFactory extends AbstractTimescaleFactory {
	
	//Esse metodo so eh chamado no comeco do programa!
	//TODO: Sergio, unificar inputTools? Verficar desempenho!
    public IAssembler getAssembler(InputTools inputTools) throws Exception { 				
		String url = inputTools.getInputName();
		inputTools = null;
		FileInputStream fis = new FileInputStream(url); 
		IoTools io = new IoTools(fis, 32000);
		IAssembler assembler = new VideoAssembler(io);
		return assembler;
    }
           
    public IDisassembler getDisassembler(String outputFile) throws IOException {
		OutputTools outputTools = new FileOutputTools(outputFile); 
		IDisassembler disassembler = new VideoDisassembler(outputTools);
    	return disassembler;
    }
    
    public IDisassembler getDisassembler() throws IOException {
		SharedBuffer buffer = new SharedBuffer("videobuffer", 
				Constants.SHARED_BUFFER_LENGTH, Constants.RESERVED_BUFFER_LENGTH);
		ByteOutputTools writer = new ByteOutputTools(buffer);
		IDisassembler disassembler = new VideoDisassembler(writer);
		return disassembler;
    }  
    
	public MediaTimescaleFacade getMediaTimescalePlayer () {
		MediaTimescaleFacade mediaPlayer = new VideoTimescaleAdapter();		
		return mediaPlayer;
	}
   
}


