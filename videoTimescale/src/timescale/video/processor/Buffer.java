package timescale.video.processor;


/*
 * Created on 05/10/2004
 * 
 * This class is based in the information contained in Recommendation H.262
 * Generic coding of moving pictures and associated audio information: Video
 */

/**
 * O armazenamento das estruturas (seqüências) do vídeo é realizado através de um buffer
 * circular.
 * Como teste, e para não consumir muita memória, foi escolhido o tamanho de 2 objetos
 * para este buffer. Os String contendo os códigos de início e término do fluxo são 
 * armazenados como objetos independentes.
 * @author Sergio Cavendish
 */
public class Buffer {
	/*
	 * Como cada seqüência utilizada como teste contém 1 GOP com 15 figuras, SIZE foi escolhido
	 * de forma a prover um atraso de 2 segundos, ou seja, 4 seqüências (considerando-se uma 
	 * taxa de 30 quadros/s).
	 */
	private int size;
	private Object sharedObject[];
	private boolean writeable = true;
	private boolean readable = false;
	private int readLocation, writeLocation;
	//private VideoController controller;// = Controller.instance();

	public Buffer() {
		this(4);
	}
	
	public Buffer(int s) {
		this.size = s;
		sharedObject = new Object[size];
		//this.controller = VideoController.instance();
		readLocation = 0;
		writeLocation = 0;
	}

	public synchronized void setSharedObject(Object object) 
	throws InterruptedException {
		while (!writeable) {
				wait();
		}

		sharedObject[writeLocation] = object;
		readable = true;
		
		//controller.updateGui(VideoGUI.INPUT_PANEL);

		writeLocation = (writeLocation + 1) % size;
		if (writeLocation == readLocation)
			writeable = false;

		notify();
	}

	public synchronized Object getSharedObject() throws InterruptedException {
		Object object;
		while (!readable) {
			wait();
		}
		
		object = sharedObject[readLocation];
		writeable = true;
		
		//controller.updateGui(VideoGUI.OUTPUT_PANEL);
		
		readLocation = (readLocation + 1) % size;
		if(readLocation == writeLocation)
			readable = false;
		
		notify();
		return object;
	}

	/**
	 * 
	 */
	public void clearBuffer() {
		for(int count = 0; count < sharedObject.length; count++){
			sharedObject[count] = null;
		}
		writeable = true;
		readable = false;
		writeLocation = 0;
		readLocation = 0;
	}
}
