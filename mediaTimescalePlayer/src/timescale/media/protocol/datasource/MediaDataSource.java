/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (10/05/2005)
 */

package timescale.media.protocol.datasource;

import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.Time;
import javax.media.Duration;

import timescale.event.PresentationFinishListener;
import timescale.event.PresentationFinishedEvent;
import util.io.InputTools;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Essa classe representa a interface para conexao com uma fonte de dados 
 * que deve ser exibida por um player JMF.
 */
public class MediaDataSource extends PullDataSource
{
	private List listeners = new ArrayList();
	protected MediaSourceStream stream;
    protected String contentType;
	
	public MediaDataSource(InputTools inputTools) throws Exception {
		this.contentType = inputTools.getType().getFullFormat();
		stream = new MediaSourceStream(inputTools, contentType);		
	}
		
	/** 
	 * Estabelece conexao com fonte de dados.
	 */	
	public void connect() throws IOException {
		//System.out.println("MediaDataSource: CONNECT");
	}

	/** 
	 * Finaliza conexao com fonte de dados.
	 */		
	public void disconnect() {
		//System.out.println("MediaDataSource: DISCONNECT");
	}

	/** 
	 * Inicia transferencia da fonte de dados.
	 */		
	public void start() throws IOException {
		System.out.println("MediaDataSource: START");
	}

	/** 
	 * Termina transferencia da fonte de dados.
	 */			
	public void stop() {
		System.out.println("MediaDataSource: STOP");
		this.firePresentationEvent();
	}

	/** 
	 * Retorna tipo de midia da fonte de dados.
	 * @return tipo de midia da fonte de dados
	 */
	public String getContentType() {
//		System.out.println("MediaDataSource: Content Type");
		return this.contentType;
	}

	
	/** 
	 * Retorna fontes de dados.
	 * @return fontes de dados
	 */
	public PullSourceStream[] getStreams() {
//		System.out.println("MediaDataSource: Get Streams");
		PullSourceStream[] streams = new PullSourceStream[1];
		streams[0] = this.stream;
		return streams;
	}
	
	/** 
	 * Retorna duracao da midia a ser exibida.
	 * @return duracao da midia a ser exibida
	 */	
	public Time getDuration() {
		//System.out.println("MediaDataSource: Get Duration");
		return Duration.DURATION_UNKNOWN;
	}

	/** 
	 * Retorna um controle sobre a midia a ser exibida.
	 * @param s nome do controle solicitado
	 * @return controle sobre a midia a ser exibida
	 */		
	public Object getControl (String s) {
//		System.out.println("MediaDataSource: Get Control");
		return null;
	}

	/** 
	 * Retorna controles existentes sobre a midia a ser exibida.
	 * @return controles existentes sobre a midia a ser exibida
	 */			
	public Object[] getControls () {
		//System.out.println("MediaDataSource: Get Controls");
		return new Object[0];
	}
	
	
	/** 
	 * Adiciona novo listener do evento de finalizacao do ajuste.
	 * @param l listener a ser adicionado 
	 */	
	public void addTimescaleListener (PresentationFinishListener  l) {
		//System.out.println("MediaDataSource: Add Listener");
        listeners.add(l);
    }

	/** 
	 * Avisa aos listeneres que o evento de finalizacao do ajuste aconteceu.
	 */	
	private void firePresentationEvent() {
		//System.out.println("MediaDataSource: Fire Presentation");
		try {
			PresentationFinishedEvent event = new PresentationFinishedEvent();
	        Iterator iterator = listeners.iterator();
	        while (iterator.hasNext() ) {
	            ((PresentationFinishListener) iterator.next()).actionPosPresentation(event);
	        }
		} catch (Exception e) {
			System.out.println("Nao conseguiu disparar evento de fim de apresentacao");
		}
    }
	
}