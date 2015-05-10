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
import util.functions.Functions;

//TODO: generalizar para outros fluxos que nao sao audio (considerar DTS)
public class AVSystemPESPacket extends SystemPESPacket {
	
	private byte[] flags;
	private byte[] extensionHeader;
	
	//TODO: So esta tratando esse relogio por enquanto
	private double timePts;
	
	private byte[] processedData;
		
	
	public AVSystemPESPacket(byte streamIdentifier, byte[] flags, byte[] extensionHeader, byte[] data) {
		super(streamIdentifier, data);
		this.flags = flags;		
		this.extensionHeader = extensionHeader;
		
		if (this.hasOnlyPTS()) {
			this.setClock(extensionHeader);
		}				
	}
	
	private boolean hasOnlyPTS() {
		boolean value = false;
		String strFlags = Convert.bytesToString(this.flags);		
		String ptsDtsFlags = strFlags.substring(8,10);
		if (ptsDtsFlags.equals("10")) {
			value = true;
		}
		return value;
	}
	
	private byte[] toHeaderBytes(boolean processed) {		
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();		
		if (this.pack!=null) {
			bytesFrame.put(this.pack.toBytes());
		}
		
		byte[] startCode = (new Bits(MPEG2SystemConstants.PES_HEADER_START_CODE)).getBytes();
		bytesFrame.put(startCode);
		bytesFrame.put(this.streamIdentifier);
		bytesFrame.put(this.getPESLength(processed));
		bytesFrame.put(this.flags);
		bytesFrame.put((byte)this.extensionHeader.length);
		if (this.hasOnlyPTS()) {
			byte[] strPts = this.getStrPTS(); 
			bytesFrame.put(strPts);
			
			int total = this.extensionHeader.length-strPts.length;
			byte[] endExtension = Functions.copyParameters(this.extensionHeader,strPts.length-1,total);
			bytesFrame.put(endExtension);			
		}
		else {
			bytesFrame.put(this.extensionHeader);
		}
		bytesFrame.trimToSize();
		return bytesFrame.array();
	}
	
	public byte[] getPESLength(boolean processed) {
		int length = this.flags.length+1+this.extensionHeader.length;
		if (processed) {
			length+=this.processedData.length;
		}
		else {
			length+=this.data.length;
		}
		return Convert.intToBytes(length,2);
	}

	
	public byte[] toOriginalBytes() {		
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();
		bytesFrame.put(this.toHeaderBytes(false));
		bytesFrame.put(this.data);
		bytesFrame.trimToSize();
		return bytesFrame.array();
	}
	
	public byte[] toProcessedBytes() {		
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();
		bytesFrame.put(this.toHeaderBytes(true));
		bytesFrame.put(this.processedData);
		bytesFrame.trimToSize();
		return bytesFrame.array();
	}
	
	
	public void addProcessedData(byte[] newData) {
		if (this.processedData==null) {
			this.processedData = newData;
		}
		else {
			this.processedData = Functions.appendArrayInAnotherArray(this.processedData,newData);
		}
	}

	public boolean wasProcessed() {
		return (this.processedData!=null);
	}

	//******* Metodos de ajuste de relogio PTS ******************* //
	
	public void adjustClock(double deltaTime) {
		if (this.hasOnlyPTS()) {
			this.timePts += deltaTime;
			if (this.pack!=null) {
				this.pack.adjustClock(deltaTime);
			}
		}
	}
	
	private void setClock(byte[] extensionHeader) {
		Bits bitsExtensionHeader = new Bits(extensionHeader);		
		String strPts = bitsExtensionHeader.toString(4,3);
		strPts += bitsExtensionHeader.toString(8,15);
		strPts += bitsExtensionHeader.toString(24,15);
		long pts = Convert.bitsToLong(strPts);
		this.setPts(pts);
	}
	
	private void setPts(double pts) {
		this.timePts = pts*MPEG2SystemConstants.TIME_FACTOR_CTE
						/MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE;		
	}
	
	private long getPts() {
		double pts = (MPEG2SystemConstants.SYSTEM_CLOCK_FREQUENCE * this.timePts)
						/(double) MPEG2SystemConstants.TIME_FACTOR_CTE;
		pts %= Math.pow(2,33);	
		long lpts = Math.round(pts);
		return lpts;
	}
		
	private byte[] getStrPTS() {
		long pts = getPts();
		String strScrBase = Convert.longToString(pts, 33);
		
		String total = "0010" + strScrBase.substring(0,3)+ '1' 
		         + strScrBase.substring(3,18) + '1' + strScrBase.substring(18,33)
		         + '1';
		
		return Convert.bitsToBytes(total);		
	}
	


}
