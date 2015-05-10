package timescale.system.metadata;

import java.util.Collection;
import java.util.Vector;

import util.data.Bits;
import util.data.ContentType;
import util.data.EOFException;
import util.data.GrowableByteBuffer;
import util.functions.Functions;
import util.io.InputTools;

public class SystemInputTools implements InputTools {

	private CollectionPESPacket collectionPES;
	
	//index do byte do PES atual (dado pelo indexPES)
	private int indexByte = 0;
	
	//Hash com (index no frame em que cabecalho comeca, pes)
	private Collection collectedPESCollection;
	
	private String remainingBits = "";
	
	public SystemInputTools(CollectionPESPacket collectionPES) {
		this.collectionPES = collectionPES;
	}
	
	public byte[] getBytes(int length) throws Exception {
		return this.getBytes(length, true);
	}
	
	public String getBits(int length) throws Exception {
		return this.getBits(length, true);
	}

	
	public int read(byte[] buffer, int offset, int lengthInBytes) throws Exception {
		int indexInCollectedData = 0;
		try {
			SystemPESPacket pes = this.collectionPES.getFirstElement();			
			
			byte[] data = pes.getSubsetData(this.indexByte);
			
			while (lengthInBytes > 0 && lengthInBytes > data.length) {			
				lengthInBytes -= data.length;
				this.collectionPES.removeFirstPES();
				this.indexByte=0;
							
				Functions.appendArray (buffer, data, indexInCollectedData+offset);
				indexInCollectedData += data.length;
				
				if (lengthInBytes > 0) {
					pes = this.collectionPES.getFirstElement();	
					data = pes.getData();
				}
				
				this.collectedPESCollection.add(pes);
			}

			if (lengthInBytes > 0) {
				Functions.appendArray (buffer, data, indexInCollectedData+offset, lengthInBytes);
				this.indexByte += lengthInBytes;
				indexInCollectedData += lengthInBytes;
				if (this.indexByte==pes.getOriginalElementaryStreamLength()) {
					this.indexByte = 0;
					this.collectionPES.removeFirstPES();
				}
			}			
		}
		catch (EOFException e) {
			
		}
				
		return indexInCollectedData;
 		
	}	
	
	private String getBits(int length, boolean updateReader) throws Exception {
		String findStr = "";
		String localRemainingBits = this.remainingBits;
		
		//Recupera de dados ja lido
		if (localRemainingBits.length() > length) {
			findStr = localRemainingBits.substring(0,length);
			localRemainingBits = localRemainingBits.substring(length); 
		}
		else {
			findStr = localRemainingBits;
			localRemainingBits="";
			
			//Recupera de dados de bytes inteiros
			int remainingLengthInBytes = (length-findStr.length())/8;
			if ( (length-findStr.length()) % 8 !=0) {
				remainingLengthInBytes++; 
			}
			byte[] fullBytes = getBytes(remainingLengthInBytes, updateReader);		
			String strFullBytes = (new Bits(fullBytes)).toString();
			
			String totalStr = findStr + strFullBytes;
			findStr = totalStr.substring(0,length);
			localRemainingBits = totalStr.substring(length);
			
			if (updateReader) {
				this.remainingBits = localRemainingBits;
			}	
			
		}
				
		return findStr;
	}
		
	private byte[] getBytes (int lengthInBytes, boolean updateReader) throws Exception {
		
		int localIndexPes = 0;
		int localIndexByte = this.indexByte;
		
		SystemPESPacket pes = this.collectionPES.getFirstElement();
		
		byte[] collectedData = new byte[lengthInBytes];
		int indexInCollectedData = 0;
		
		byte[] data = pes.getSubsetData(localIndexByte);
		
		//Primeiro frame do fluxo
		if (this.indexByte==0 && updateReader) {
			this.collectedPESCollection.add(pes);
		}
		
		while (lengthInBytes > 0 && lengthInBytes > data.length) {			
			lengthInBytes -= data.length;
			localIndexPes++;
			localIndexByte=0;
						
			Functions.appendArray (collectedData, data, indexInCollectedData);
			indexInCollectedData += data.length;
			
			if (lengthInBytes > 0) {
				pes = this.collectionPES.elementAt(localIndexPes);
				data = pes.getData();
			}
			
			if (updateReader) {
				this.collectedPESCollection.add(pes);
			}				
		}

		if (lengthInBytes > 0) {
			Functions.appendArray (collectedData, data, indexInCollectedData, lengthInBytes);
			localIndexByte += lengthInBytes;
			if (localIndexByte==pes.getOriginalElementaryStreamLength()) {
				localIndexByte = 0;
				localIndexPes++;
			}
		}
		
		if (updateReader) {
			for (int i=0; i<localIndexPes; i++) {
				this.collectionPES.removeFirstPES();
			}			
			this.indexByte = localIndexByte;
		}
		
		return collectedData;
	}

	public int getActualByteIndex () {
		return this.indexByte;
	}

	public void initCollectedPES() {
		this.collectedPESCollection = new Vector();
	}	
	
	public Collection getCollectedPES() {
		return this.collectedPESCollection;
	}
	
	public boolean lookBitsAhead(String s) throws Exception {
		String bitsAhead = this.getBits(s.length(), false);
		return s.equals(bitsAhead);
	}
	
	
	public GrowableByteBuffer nextStartCode(String startCode) throws Exception {
		throw new UnsupportedOperationException("LimitedSimpleIO nao suporta o metodo nextStartCode");
	}

	public GrowableByteBuffer getTail() throws Exception {
		throw new UnsupportedOperationException("LimitedSimpleIO nao suporta o metodo getTail");
	}
	
	public void close() throws Exception {
		
	}
	
	public ContentType getType() {
		throw new UnsupportedOperationException("LimitedSimpleIO nao suporta o metodo getType");
	}
	
	public void reset() throws Exception {
		throw new UnsupportedOperationException("LimitedSimpleIO nao suporta o metodo reset");
	}
	
	public String getInputName() {
		throw new UnsupportedOperationException("LimitedSimpleIO nao suporta o metodo getInputName");
	}
	
	public SystemPESPacket getActualPES() {
		SystemPESPacket pes = null;
		try {
			pes = this.collectionPES.getFirstElement();
		}
		catch (EOFException e) {}
		return pes;
	}
	
}
