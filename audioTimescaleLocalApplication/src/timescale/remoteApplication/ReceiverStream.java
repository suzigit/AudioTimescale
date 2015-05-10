/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (16/09/2005)
 */

package timescale.remoteApplication;


import timescale.net.IRTPHandlerData;
import timescale.net.RTPClient;
import timescale.util.constants.Constants;
import timescale.util.constants.IOConstants;
import util.data.ContentType;

import javax.media.Manager;
import javax.media.Processor;
import javax.media.ProcessorModel;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;


public class ReceiverStream implements IRTPHandlerData, Runnable {
	
	private int bufferLength;
	
	public ReceiverStream(int bufferLength) {
		this.bufferLength = bufferLength;
	}
	
	public void run() {
		try {
			String rtpMachine = IOConstants.RTP_MACHINE_MULTICAST;
			//TODO: a principio, so uma porta, fazer com outras portas
			int ports[] = {IOConstants.ORIGINAL_STREAM_READER_RTP_PORT};			
			int rtpTtl = IOConstants.RTP_TTL;
			int waitTime = IOConstants.RTP_WAIT_TIME;
			RTPClient rtpClient = new RTPClient(this, waitTime);
			RTPClient.SessionData sessions[] = new RTPClient.SessionData[ports.length];
			for (int i=0; i<ports.length; i++) {
				sessions[i] = rtpClient.new SessionData();
				sessions[i].address = rtpMachine;
				sessions[i].port = ports[i];
				sessions[i].ttl = rtpTtl;			
			}		
			rtpClient.prepare(sessions);
			Thread t = new Thread(rtpClient);
			t.start();
		}
		catch (Exception e) {
			System.out.println("Erro no ReceiverStream: " + e.getMessage());
		}
	}
	
	/**
	 * Metodo chamado pelo cliente RTP quando recebe dados.
	 */
	public void receiveStream (DataSource dataSource) throws Exception {
		//TODO: mudar depois para audio ou video
		ContentDescriptor type = new ContentDescriptor (Constants.ContentType.MPEG_AUDIO_BC);
		ProcessorModel model = new ProcessorModel(dataSource,null,type);
		Processor processor = Manager.createRealizedProcessor(model);
		DataSource dsOutputProcessor = processor.getDataOutput();
		dsOutputProcessor.connect();
		processor.start();
		
		PushDataSource pushDataSource = (PushDataSource) dsOutputProcessor;
		PushSourceStream [] streams = pushDataSource.getStreams();
		for (int i=0; i<streams.length; i++) {
			PushSourceStream stream = streams[i];
			ContentType contentType = ContentType.createByFullFormat(stream.getContentDescriptor().getContentType());			
			HandlerReceivedData sth = new HandlerReceivedData(bufferLength, contentType);
			stream.setTransferHandler(sth);
		}
		pushDataSource.start();	
	}
	
}
