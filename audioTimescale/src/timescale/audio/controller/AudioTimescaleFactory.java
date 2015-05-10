/*
 * Essa classe faz parte do projeto de ajuste elastico de media comprimida
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (11/11/2005)
 */

package timescale.audio.controller;

import java.io.IOException;

import timescale.assembler.IAssembler;
import timescale.assembler.IDisassembler;
import timescale.audio.assembler.AudioAssembler;
import timescale.audio.assembler.IAudioDisassembler;
import timescale.audio.assembler.GenericAudioAssembler;
import timescale.audio.assembler.AudioDisassembler;
import timescale.audio.format.ac3.AC3Assembler;
import timescale.audio.format.mpeg.aac.AACAssembler;
import timescale.audio.format.mpeg.mp2.MP2Assembler;
import timescale.audio.format.mpeg.mp3.MP3Assembler;
import timescale.audio.util.constants.MPEGBCConstants;
import timescale.facade.AbstractTimescaleFactory;
import timescale.facade.MediaTimescaleFacade;
import timescale.util.constants.Constants;
import util.data.ContentType;
import util.data.SharedBuffer;
import util.io.ByteOutputTools;
import util.io.FileOutputTools;
import util.io.InputTools;

/**
 * Fabrica de objetos de ajuste elastico de audio.
 */
public class AudioTimescaleFactory extends AbstractTimescaleFactory {
		
    public IAssembler getAssembler(InputTools inputTools) throws Exception { 
    	GenericAudioAssembler genericAudioAssembler = getElementaryAssembler(inputTools, inputTools.getType());
		AudioAssembler audioAssembler = new AudioAssembler(genericAudioAssembler);
		return audioAssembler;
	}

	public GenericAudioAssembler getElementaryAssembler(InputTools inputTools, ContentType inputType) throws Exception {
    	GenericAudioAssembler genericAudioAssembler;
		if (inputType.isMPEGBC()) {			
			
			if (inputTools!=null) {
				MPEGBCConstants constants = new MPEGBCConstants();	
				inputTools.nextStartCode(constants.SYNC_WORD);
				String bits = inputTools.getBits(MPEGBCConstants.INDEX_LAYER_END);		
				String layer = bits.substring(MPEGBCConstants.INDEX_LAYER_BEGGIN);
				inputTools.reset();
				if (layer.equals(MPEGBCConstants.LAYER2)) {
					genericAudioAssembler = new MP2Assembler(inputTools);
				}
				else if (layer.equals(MPEGBCConstants.LAYER3)) {
					genericAudioAssembler = new MP3Assembler(inputTools);
				}
				else {
					throw new IllegalArgumentException();
				}				
			}
			else {
				genericAudioAssembler = new MP2Assembler(inputTools);
			}
		}
		else if (AC3Assembler.getStaticType().equals(inputType)) {
			genericAudioAssembler = new AC3Assembler (inputTools);
		}
		else if (AACAssembler.getStaticType().equals(inputType)) {
			genericAudioAssembler = new AACAssembler (inputTools);
		}
		else {
			throw new IllegalArgumentException();
		}
		return genericAudioAssembler;
	}
    
    public IDisassembler getDisassembler(String outputFile) throws IOException {
   		FileOutputTools writer = new FileOutputTools(outputFile);
   		IAudioDisassembler disassembler = new AudioDisassembler(writer);
    	return disassembler;
    }
    
    public IDisassembler getDisassembler() throws IOException {
		SharedBuffer buffer = new SharedBuffer("bufferaudio", 
				Constants.SHARED_BUFFER_LENGTH, Constants.RESERVED_BUFFER_LENGTH);
		ByteOutputTools writer = new ByteOutputTools(buffer);
		IAudioDisassembler gad = new AudioDisassembler(writer);		
    	return gad;
    }

    
	public MediaTimescaleFacade getMediaTimescalePlayer () {
		MediaTimescaleFacade mediaPlayer = new AudioTimescaleFacade();
		return mediaPlayer;
	}
	
	
}
