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

import timescale.assembler.IDisassembler;
import timescale.system.metadata.MPEG2SystemConstants;
import util.functions.Convert;
import util.functions.Functions;
import util.io.OutputTools;

public class SystemDisassembler implements IDisassembler  {

	private OutputTools outputTools;
	
	public SystemDisassembler (OutputTools outputTools) {
		this.outputTools = outputTools;
	}
	
	public OutputTools getOutputTools () {
		return this.outputTools;
	}
	
	public void output(byte[] b) throws Exception {
		this.outputTools.output(b);
	}
	
	public void output(byte[] b, int total) throws Exception {
		if (total!=-1) {
			if (total!=b.length) {
				b = Functions.copyParameters(b,total);
			}		
			this.outputTools.output(b);			
		}
	}	
	
	public void finish () throws Exception {
		String finishCode = MPEG2SystemConstants.FINISH_CODE;
		byte[] bytesEOF = Convert.bitsToBytes(finishCode);
		this.outputTools.output(bytesEOF);
		this.outputTools.finish();
	}
	
}
