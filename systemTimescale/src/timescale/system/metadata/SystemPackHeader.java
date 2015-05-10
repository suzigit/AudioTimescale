/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (28/11/2005)
 */

package timescale.system.metadata;

import util.data.Bits;
import util.data.GrowableByteBuffer;
import util.functions.Convert;

public class SystemPackHeader {
	
	private byte[] programUntilStuffingLength;
	
	private byte[] stuffingBytes;
	private SystemHeader systemHeader;
	
	private double timeScrBase;
	private double timeExtension;
	
	public SystemPackHeader(byte[] beginHeader, byte[] stuffingBytes, 
			SystemHeader systemHeader) {
		
		//System.out.println("beginHeader=" + new Bits(beginHeader).toString());
		this.setSCR(beginHeader);
		this.programUntilStuffingLength = new byte[4];
		for (int i=0; i<4; i++) {
			this.programUntilStuffingLength[i] = 
				beginHeader[i+6];
		}
		//System.out.println("beginHeader depois=" + new Bits(this.getStrSCR()).toString());
		this.stuffingBytes = stuffingBytes;
		this.systemHeader = systemHeader;
		
	}
	
	public void adjustClock(double deltaTime) {
	  
		//TODO: teoricamente, precisaria fazer o mesmo calculo para o timeScrBase, mas como o valor maximo dele eh
		//muito grande, nao fiz.
		this.timeScrBase += deltaTime;
		
		double result = (MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE*Math.abs(deltaTime))
							% MPEG2SystemConstants.TIME_FACTOR_CTE;
		double deltaTimeForExtension = result/MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE;
		double maxTimeExtension = MPEG2SystemConstants.MAX_TIME_EXTENSION;
		if (this.timeExtension<deltaTimeForExtension && deltaTime<0) {			
			this.timeExtension += maxTimeExtension;
		}
		int signal = deltaTime>0?1:-1;
		this.timeExtension += (deltaTimeForExtension*signal);
		if (this.timeExtension>maxTimeExtension) {
			this.timeExtension -= maxTimeExtension;
		}
		
	}
	
	private void setSCR(byte[] beginHeader) {
		Bits bitsBeginHeader = new Bits(beginHeader);
		
		String strScrBase = bitsBeginHeader.toString(2,3);
		strScrBase += bitsBeginHeader.toString(6,15);
		strScrBase += bitsBeginHeader.toString(22,15);		
		long scrBase = Convert.bitsToLong(strScrBase);
		
		String strScrExtension = bitsBeginHeader.toString(38,9);
		long scrExtension = Convert.bitsToInt(strScrExtension);
		
		this.setSCR(scrBase, scrExtension);
	}
	
	
	private void setSCR(double scrBase, double scrExtension){		
		this.timeScrBase = scrBase*MPEG2SystemConstants.TIME_FACTOR_CTE/MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE;		
		//System.out.println("TIME BASE: " + timeScrBase);
		
		this.timeExtension = scrExtension/MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE;
		//System.out.println("TIME EXTENSION: " + timeExtension);		
	}
	
	
	public long getScrBase() {
		double scrBase = (((double) MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE) 
				* this.timeScrBase)/MPEG2SystemConstants.TIME_FACTOR_CTE;
		scrBase %= Math.pow(2,33);
		long lScrBase = Math.round(scrBase);
		return lScrBase;
	}
	
	public long getScrExtension() {		
		double scrExtension = (((double) MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE) 
				* this.timeExtension);
		scrExtension %= MPEG2SystemConstants.TIME_FACTOR_CTE;
		long lScrExtension = Math.round(scrExtension);
		return lScrExtension;
	}	
	
	public byte[] getStrSCR() {
		long scrBase = getScrBase();
		String strScrBase = Convert.longToString(scrBase, 33);
		
		long scrExtension = getScrExtension();
		String strExtension = Convert.longToString(scrExtension, 9);
		
		String total = "01" + strScrBase.substring(0,3)+ '1' 
		         + strScrBase.substring(3,18) + '1' + strScrBase.substring(18,33)
		         + '1' + strExtension + '1';
		
		return Convert.bitsToBytes(total);		
	}

	
	public byte[] toBytes() {
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();
		byte[] startCode = (new Bits(MPEG2SystemConstants.PACK_HEADER_START_CODE)).getBytes();
		bytesFrame.put(startCode);				
		bytesFrame.put(this.getStrSCR());
		bytesFrame.put(this.programUntilStuffingLength);				
		bytesFrame.put(this.stuffingBytes);
		if (systemHeader!=null) {
			bytesFrame.put(systemHeader.toBytes());	
		}		
		bytesFrame.trimToSize();
		return bytesFrame.array();	
	}

}
