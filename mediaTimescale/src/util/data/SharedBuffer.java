package util.data;


/**
 * Essa classe representa um buffer de bytes que e acessado por um produtor
 * e um consumidor paralelamente.
 */
public class SharedBuffer {

	private byte[] byteBuffer;
	private int readPosition=0;
	private long readSinceBegin = 0;
	private int writePosition=0;
	private boolean isFinished=false;
	private int reservedLength;
	
	private ContentType contentType;
	
	private int lengthWantedByProducer=0;
	
	private String name;

	/** 
	 * Controi instancia da classe.
	 * @param length tamanho inicial do buffer
	 * @reservedLength 
	 */	
    public SharedBuffer(String name, int length, int reservedLength) {
    	this.name = name;
    	init(length, reservedLength);
    }
   
    public String getName() {
    	return this.name;
    }
    
    private void init(int length, int reservedLength) {
    	this.reservedLength = reservedLength;
    	this.byteBuffer = new byte[length];
    }
    
	public synchronized ContentType getContentType() {
		return this.contentType;
	}	
	
	public synchronized void setContentType (ContentType ct) {
		this.contentType = ct;
	}

	public int getLength() {
		return this.byteBuffer.length;
	}
	
	public synchronized int read(byte[] b, int offset, int length) throws Exception {  
		return read(b,offset,length,true);
	}
	
	/** 
	 * Realiza a leitura de conjunto de dados.
	 * @param b local a ser armazenado os bytes lidos
	 * @param offset index no buffer em que dados devem comecar a ser armazenados
	 * @param length numero de bytes solicitados 
	 * @return numero de bytes lidos ou -1 se não houver mais bytes
	 */    
    public synchronized int read(byte[] b, int offset, int length, boolean updateReader) throws Exception {    	
    	int total = length;
    	try {        	
            if (!this.isAvailableForGet(1) && isFinished) {
            	total = -1;
            }
            else {
    	    	while (!this.isAvailableForGet(1)) {
                	//System.err.println("Consumidor do buffer " + this.name + " vai dormir");
                    wait();
                    //System.err.println("Consumidor do buffer " + this.name + " acordou");
    	        }
    	        if (this.numberOfElements()<total) {
    	        	total = this.numberOfElements();
    	        }
    	        
    	        int readPositionTemp = readPosition;
    	        long readSinceBeginTemp = readSinceBegin;
    	        
    	        //le quantidade desejada
    	        for (int i=0; i<total; i++) {
    	        	b[i+offset] = this.byteBuffer[readPositionTemp];
    	        	readPositionTemp = this.incrementPosition(readPositionTemp);
    	        	readSinceBeginTemp++;
    	        }
    	        
    	        //atualiza ponteiros dentro do buffer
    	        if (updateReader) {
    	        	this.readPosition = readPositionTemp;
    		        this.readSinceBegin = readSinceBeginTemp;
    	        	
    		        if (this.lengthWantedByProducer < this.freeSpace(this.byteBuffer.length - this.reservedLength)) {
    		        	notifyAll();
    		        }        		        	
    	        }
    	        //System.out.println(name + "- Consumer read " + total + " do buffer " + this.name + "- Total no buffer=" + this.numberOfElements());	        
            }        	
        } catch (EOFException e) {
        	total = -1;
        }
        return total;
    }
    
    public synchronized byte[] getBytes(int length) throws Exception {
        byte bytes[] = new byte[length];
        
    	while (!this.isAvailableForGet(length)) {
        	//System.err.println("Consumidor do buffer "+ this.name +" vai dormir!");
            wait();
            //System.err.println("Consumidor do buffer " + this.name + " acordou");
        }
      	        
        for (int i=0; i<length; i++) {
        	bytes[i] = this.byteBuffer[readPosition];
        	readPosition = this.incrementPosition(readPosition);
        	readSinceBegin++;
        }
    	
        if (this.lengthWantedByProducer < this.freeSpace(this.byteBuffer.length - this.reservedLength)) {
        	//System.err.println("Acordou produtores do buffer " + this.name);
	        notifyAll();
        }

        return bytes;
    }

	/** 
	 * Insere conjunto de dados no buffer.
	 * @param b dados a serem inseridos
	 */    
    public synchronized void put(byte[] b) throws Exception {
        this.lengthWantedByProducer = b.length;
    	
    	while (!this.isAvailableForPut(b.length)) {  	
        	//System.err.println("Produtor vai dormir - queria colocar " + b.length);        	
            wait();
            //System.err.println("Produtor " + name + " acordou");
        }
        for (int i=0; i<b.length; i++) {
        	this.byteBuffer[this.writePosition] = b[i];
        	writePosition = this.incrementPosition(writePosition);
        }        
        //System.out.println(name + "- Producer put: " + b.length + " no buffer " + this.name + " -Total no buffer=" + this.numberOfElements());
        notifyAll();
    }

    //Retorna espaco livre para escrever novos dados
    private synchronized int freeSpace(int totalLength) {
    	int total;
    	if (this.writePosition >= this.readPosition) {
    		total = totalLength - (this.writePosition - this.readPosition);
    	}
    	else { 
    		total = this.readPosition - this.writePosition;
    	}
    	return total;
    }
    
 
    private synchronized int numberOfElements() {
    	return this.byteBuffer.length - this.freeSpace(this.byteBuffer.length);
    }
    
    /**
     * Verifica se e possivel realizar a leitura de bytes.
     * @return true sse e possivel realizar a leitura de bytes
     */
    public synchronized boolean isAvailableForGet(int total) throws Exception {    	    	
    	boolean flag = false;
   		if (numberOfElements() >= total) {
   			flag = true;
    	}
   		else if (isFinished()) { 
   			//System.out.println("TERMINOU LEITURA DO ARQUIVO!!");
   			throw new EOFException();
   		}
    	return flag;
    }
    
    /**
     * Verifica se e possivel inserir novos bytes no buffer.
     * @param length quantidade de bytes que se deseja inserir
     */
    public synchronized boolean isAvailableForPut(int length) {    	
    	return this.freeSpace(this.byteBuffer.length - this.reservedLength) > length;
    }
    
    private int incrementPosition (int position) {
    	position++;
    	position %= this.byteBuffer.length;
    	return position;
    }

    /**
     * Indica que ja terminou a insercao de bytes no buffer.
     */    
	public synchronized void finish() {
		this.isFinished = true;
		notifyAll();
	}
	
    /**
     * Indica se ja terminou a insercao de bytes no buffer.
     * @return true sse ja terminou a insercao de bytes no buffer
     */    
	public boolean isFinished() {		
		return this.isFinished;
	}
	
	public synchronized long tell () {
		long tellPosition = this.readSinceBegin;
//		System.out.println("Tell: " + tellPosition);
		return tellPosition;
	}
	
	public synchronized long seek (long where) throws Exception {
		//System.out.println("### Seek para " + where);
		
        // Adianta a exibicao da midia
		if (where > this.readSinceBegin)
		{
			long delta = where-this.readPosition;
			
			if (this.numberOfElements()>delta) {
				for (int i=0; i<delta; i++) {
					this.readPosition = this.incrementPosition(this.readPosition);					
				}
			}
			
			else {
				//Deve ler tudo que ja tem disponivel no buffer
				while (this.readPosition + 1< this.writePosition) {
					this.readPosition = this.incrementPosition(this.readPosition);
				}
				
				long skipLength = where - this.readPosition;
				int nRead = 0;
				for (long i = skipLength; i > 0 && nRead!=-1; i -= nRead) {
					int length = i < Integer.MAX_VALUE ? (int)i : Integer.MAX_VALUE;
					byte b[] = new byte[length];  
					nRead = this.read(b, 0, length);
				}
			}
			//Libera para produtor
			notifyAll();
		} 
		// Volta a exibicao da midia, ainda dentro do buffer
		// Volta para dados anteriormente lidos e que ja estavam disponiveis para escrita 
		else if (this.readSinceBegin - where <= this.freeSpace(this.byteBuffer.length)) {
			int backLength = (int)(this.readSinceBegin - where);
			
			int nDelta = this.readPosition - backLength;
			
			if (nDelta < 0) {
				this.readPosition = this.byteBuffer.length + nDelta;				
			}
			else {
				this.readPosition = nDelta;
			}
			//Libera para consumidor
			notifyAll();
		} 
		else {
			throw new IllegalArgumentException("Nao conseguiu dar seek para local desejado.");
		}

		this.readSinceBegin = where;
		return where;
	}
}
