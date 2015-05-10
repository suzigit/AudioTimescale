package timescale.system.metadata;

import util.data.Bits;
import util.data.GrowableByteBuffer;
import util.functions.Convert;

public class DataSystemPESPacket extends SystemPESPacket {

	public DataSystemPESPacket(byte streamIdentifier, byte[] data) {
		super(streamIdentifier, data);
	}
	
	public static boolean isPaddingStream(String streamID) {
		return streamID.equals("10111110");
	}
	
	public byte[] toOriginalBytes() {		
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();
		bytesFrame.put(this.toHeaderBytes());
		bytesFrame.put(this.data);
		bytesFrame.trimToSize();
		return bytesFrame.array();
	}
	
	public byte[] toHeaderBytes() {		
		GrowableByteBuffer bytesFrame = new GrowableByteBuffer();		
		if (this.pack!=null) {
			bytesFrame.put(this.pack.toBytes());
		}
		
		byte[] startCode = (new Bits(MPEG2SystemConstants.PES_HEADER_START_CODE)).getBytes();
		bytesFrame.put(startCode);
		bytesFrame.put(this.streamIdentifier);
		bytesFrame.put(this.getPESLength());
		bytesFrame.trimToSize();
		return bytesFrame.array();
	}
	
	private byte[] getPESLength() {
		int length = this.data.length;
		return Convert.intToBytes(length,2);
	}

	public boolean wasProcessed() {
		return false;
	}

}
