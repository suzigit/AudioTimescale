package timescale.system.metadata;

import util.data.Bits;

public abstract class SystemPESPacket {
	
	protected byte streamIdentifier;	
	private long id;
	
	//So sera diferente de null se for o primeiro pes dentro de um pack
	protected SystemPackHeader pack;
	
	//Dados de midia do PES
	protected byte[] data;
	private int originalDataLength;
	
	public SystemPESPacket (byte streamIdentifier, byte[] data) {
		this.streamIdentifier = streamIdentifier;
		this.data = data;
		this.originalDataLength = this.data.length;
	}
	
	public int getOriginalElementaryStreamLength() {		
		return originalDataLength;
	}
	
	public String getStreamIdentifier() {
		return new Bits(this.streamIdentifier).toString();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public boolean isAudio() {
		return this.getStreamIdentifier().startsWith("110");
	}
	
	public boolean isVideo() {
		return this.getStreamIdentifier().startsWith("1110");
	}	

	public boolean isAudioOrVideo() {
		return isAudioOrVideo(this.getStreamIdentifier());
	}
	
	public static boolean isAudioOrVideo(String streamID) {
		return (streamID.startsWith("110") || streamID.startsWith("1110"));
	}
	
	public SystemPackHeader getPack() {
		return this.pack;
	}
	
	public void setPack(SystemPackHeader pack) {
		this.pack = pack;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public byte[] getSubsetData(int indexBegin) {
		int length = this.data.length - indexBegin;
		byte [] findData = new byte[length];
		for (int i=0; i<length; i++) {
			findData[i] = this.data[i+indexBegin];
		}
		return findData;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public abstract byte[] toOriginalBytes();
	public abstract boolean wasProcessed();
	
}
