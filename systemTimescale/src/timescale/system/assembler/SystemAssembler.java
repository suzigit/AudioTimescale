/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (25/11/2005)
 */

package timescale.system.assembler;

import timescale.assembler.IAssembler;
import timescale.system.metadata.DataSystemPESPacket;
import timescale.system.metadata.MPEG2SystemConstants;
import timescale.system.metadata.SystemHeader;
import timescale.system.metadata.AVSystemPESPacket;
import timescale.system.metadata.SystemPESPacket;
import timescale.system.metadata.SystemPackHeader;
import util.data.Bits;
import util.data.ContentType;
import util.functions.Convert;
import util.functions.Functions;
import util.io.InputTools;

public class SystemAssembler implements IAssembler {

	private InputTools inputTools;
	
	/** 
	 * Controi instancia da classe.
	 * @param inputTools fonte de dados de entrada
	 */
	public SystemAssembler (InputTools inputTools) {
		this.inputTools = inputTools;
	}
	
	public ContentType getType () {
		return this.inputTools.getType();
	}

	public boolean hasMorePacks() throws Exception {
		String packHeaderStartCode = MPEG2SystemConstants.PACK_HEADER_START_CODE;		
		return this.inputTools.lookBitsAhead(packHeaderStartCode);
	}	
	
	public SystemPackHeader getPack() throws Exception {
		return getPack(this.inputTools);
	}
	
	public boolean hasMorePES() throws Exception {
		String pesHeaderStartCode = MPEG2SystemConstants.PES_HEADER_START_CODE;		
		return this.inputTools.lookBitsAhead(pesHeaderStartCode);
	}	
	
	public SystemPESPacket getPES() throws Exception {
		return getPES(this.inputTools);
	}
	
	public boolean isFinished() throws Exception {
		String finishCode = MPEG2SystemConstants.FINISH_CODE;
		return this.inputTools.lookBitsAhead(finishCode);
	}	
	
	public void finish() throws Exception {
		this.inputTools.close();
	}
		
	public static SystemPackHeader getPack(InputTools inputTools) throws Exception {
		inputTools.getBytes(MPEG2SystemConstants.PACK_HEADER_START_CODE.length()/8);
		byte[] packBegin = inputTools.getBytes(MPEG2SystemConstants.PACK_BEGIN_HEADER_BYTES_LENGTH);
		int lastIndex = packBegin.length-1;
		Bits bits = new Bits(packBegin[lastIndex]);
		String sStuffingBitsLength = bits.toString(5,3);
		int stuffingBytesLength = Convert.bitsToInt(sStuffingBitsLength);
		byte[] stuffingBytes = inputTools.getBytes(stuffingBytesLength);
		
		//Pegar cabecalho system
		String systemHeaderStartCode = MPEG2SystemConstants.SYSTEM_HEADER_START_CODE;
		SystemHeader systemHeader = null;
		if (inputTools.lookBitsAhead(systemHeaderStartCode)) {
			systemHeader = getSystemHeader(inputTools);
		}
		
		SystemPackHeader pack = new SystemPackHeader(packBegin, stuffingBytes, systemHeader);
		return pack;	
	}
	
	private static SystemHeader getSystemHeader(InputTools inputTools) throws Exception {
		inputTools.getBytes(MPEG2SystemConstants.SYSTEM_HEADER_START_CODE.length()/8);
		byte[] bSystemLength = inputTools.getBytes(2);
		int systemHeaderLength = Convert.bytesToInt(bSystemLength);
		byte[] bSystemHeaderParameters = inputTools.getBytes(systemHeaderLength);
		SystemHeader header = new SystemHeader(bSystemLength, bSystemHeaderParameters);
		return header;
	}
	
	public static SystemPESPacket getPES(InputTools inputTools) throws Exception {
		inputTools.getBytes(MPEG2SystemConstants.PES_HEADER_START_CODE.length()/8);
		
		byte streamIdentifier = inputTools.getBytes(1)[0];
		String strStreamIdentifier = Convert.byteToString(streamIdentifier);
		
		byte[] bPESLength = inputTools.getBytes(2);
		int pesLength = Convert.bytesToInt(bPESLength);
		
		SystemPESPacket pes = null;
		if (AVSystemPESPacket.isAudioOrVideo(strStreamIdentifier)) {				
			byte[] flags = inputTools.getBytes(2);
			byte[] bExtensionLength = inputTools.getBytes(1);
			int extensionLength = Convert.bytesToInt(bExtensionLength);
			byte[] extensionHeader = inputTools.getBytes(extensionLength);
			
			//Calcula tamanho elementary stream			
			int elementaryStreamLength = pesLength-
						(flags.length + bExtensionLength.length + extensionHeader.length);
					
			//Pega dados do PES - nao necessariamente tem tamanho que deveria
			byte[] data = new byte[elementaryStreamLength];
			int length = inputTools.read(data,0,data.length);
			data = Functions.copyParameters(data,length);
			
			pes = new AVSystemPESPacket(streamIdentifier, flags, extensionHeader, data);
			
		}
		
		//TODO: tratar direito
		else if (DataSystemPESPacket.isPaddingStream(strStreamIdentifier)) {
			byte[] data = inputTools.getBytes(pesLength);
			pes = new DataSystemPESPacket(streamIdentifier, data);
		}
		
		else {
			System.err.println("SystemAssembler:: veio um tipo de PES que ainda nao estou tratando");
		}
				
		return pes;		
	}
}
