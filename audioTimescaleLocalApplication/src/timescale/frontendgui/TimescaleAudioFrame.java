/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (14/06/2004)
 */

package timescale.frontendgui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import timescale.backendgui.GUIController;
import timescale.backendgui.GUIUpdater;
import timescale.backendgui.PresentationFrame;
import timescale.factory.InputToolsBuilder;
import timescale.thirdPartyPlayer.IThirdPartyPlayer;
import timescale.thirdPartyPlayer.JMFDataSourcePlayer;
import timescale.thirdPartyPlayer.JMFFilePlayer;
import timescale.thirdPartyPlayer.JMFRTPTransmissionPlayer;
import timescale.thirdPartyPlayer.VLCFilePlayer;
import timescale.thirdPartyPlayer.VLCRTPPlayer;
import timescale.thirdPartyPlayer.WinampFilePlayer;
import timescale.util.constants.IOConstants;
import util.functions.Functions;
import util.io.InputTools;


/**
 * Essa classe contem a interface apresentada ao usuario para escolha do 
 * arquivo e da selecao ajuste elastico desejado.
 */
public class TimescaleAudioFrame extends Frame implements PresentationFrame {

	private static final long serialVersionUID = 1L;
	private GUIController controller;
	private MenuBarAudio menuBar; 
	
	private Label lblInputFile = new Label(); 
	private TextField txtInputFile  = new TextField();
	private Button btnInputFile = new Button();

	private Label lblRemoteInputFile = new Label(); 
	private TextField txtRemoteInputFile  = new TextField();

	
	private Label lblOutputFile = new Label(); 
	private TextField txtOutputFile  = new TextField();
	private Button btnOutputFile = new Button();
	
	private Button btnProcess = new Button();
	private Button btnProcessAndPlayJMF = new Button();
	private Button btnProcessAndPlayVLC = new Button();	
	private Button btnProcessAndPlayWinamp = new Button();	
	private Button btnPlayJMF = new Button();
	private Button btnPlayRTPJMF = new Button();	
	private Button btnPlayVLC = new Button();	
	private Button btnRateChanging = new Button();	
	private Button btnStop = new Button();
	
	private Label lblTittleTimescale = new Label();
	private Label lblTimescale = new Label();
	private JSlider sliderTimescale  = new JSlider(90,110,100);
	
	private Label lblResult  = new Label();
	private TextArea textAreaResult  = new TextArea(50,20);

	private Label lblTimescaleInstant = new Label(); 
	private TextField txtTimescaleInstant  = new TextField();
	private Button btnTimescaleInstant = new Button();
	private Label lblTimescaleInstantResult = new Label();
	
	private JPanel panelPlayer;
	
	private int X_BEGIN_PARAMETERS = 20;
	private String MSG_CHOOSE_FILE = "Escolha arquivo de áudio";
	
	/** 
	 * Controi instancia da classe.
	 * @param widthFrame largura da janela
	 * @param heightFrame altura da janela 
	 */			
	public TimescaleAudioFrame(int widthFrame, int heightFrame) {	
		
		this.setSize(widthFrame,heightFrame);
        this.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e ) {
                System.exit(0);
            }
        });		
		
		this.setTitle("Ajuste Elástico em áudio");
		this.setResizable(false);				
		this.setLayout(null);
		this.setFont(new java.awt.Font("verdana", Font.BOLD, 12));
		int x;
		int y = 20;
		int height;
		int space = 5;
		int width;
		
		/*
		 * Monta linha de arquivo de entrada locais
		 */
		x = X_BEGIN_PARAMETERS;
		y = y + 40;
		height = 30;		
		this.lblInputFile.setText("Entrada local:");
		width = 100;
		this.lblInputFile.setBounds(x,y,width,height);		
		x += width + space; 
		this.add(this.lblInputFile);

		width = 240;
		this.txtInputFile.setBounds(x,y,width,height);
		this.add(this.txtInputFile);

		this.btnInputFile.setLabel("Buscar...");
		this.btnInputFile.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputFile_actionPerformed(e);
			}
		});
		x += width + space;
		width = 60;
		this.btnInputFile.setBounds(x,y,width,height);				
		this.add(this.btnInputFile);		


		/*
		 * Monta linha de arquivo de entrada remotos
		 */
		x = X_BEGIN_PARAMETERS;
		y = y + 40;
		height = 30;		
		this.lblRemoteInputFile.setText("Entrada remota:");
		width = 100;
		this.lblRemoteInputFile.setBounds(x,y,width,height);		
		x += width + space; 
		this.add(this.lblRemoteInputFile);

		width = 240;
		this.txtRemoteInputFile.setBounds(x,y,width,height);
		this.add(this.txtRemoteInputFile);

		
		/*
		 * Monta linha de arquivo de saida
		 */
		x = X_BEGIN_PARAMETERS;
		y = y + 40;
		height = 30;		
		this.lblOutputFile.setText("Saída local:");
		width = 100;
		this.lblOutputFile.setBounds(x,y,width,height);		
		x += width + space; 
		this.add(this.lblOutputFile);

		width = 240;
		this.txtOutputFile.setBounds(x,y,width,height);
		this.add(this.txtOutputFile);
		
		this.btnOutputFile.setLabel("Buscar...");
		this.btnOutputFile.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getOutputFile_actionPerformed(e);
			}
		});
		x += width + space;
		width = 60;
		this.btnOutputFile.setBounds(x,y,width,height);				
		this.add(this.btnOutputFile);
		
		
		
		/*
		 * Monta linha de fator de ajuste
		 */
		x = X_BEGIN_PARAMETERS;
		y = y + 40;
		height = 17;		
		this.lblTittleTimescale.setText("Fator de ajuste (em %):");
		width = 150;
		this.lblTittleTimescale.setBounds(x,y,width,height);		
		this.add(this.lblTittleTimescale);
		
		x += width + space;
		width = 30;
		this.lblTimescale.setText("100");
		this.lblTimescale.setBounds(x,y,width,height);
		this.add(this.lblTimescale);
		
		x += width + space;
		width = 100;	
		this.sliderTimescale.setBounds(x,y,width,height);
		this.add(this.sliderTimescale);
		sliderTimescale.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e)
            {
                sliderTimescale_changeEvent(e);
            }
		});
		

		/*
		 * Monta linha de botao de processamento (sem tocar)
		 */
		width = 50;
		height = 20;
		y = y + 30;
		x = 20;
		this.btnProcess.setLabel("Gerar");
		this.btnProcess.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				create_actionPerformed(e);
			}
		});		
		this.btnProcess.setBounds(x,y,width,height);				
		this.add(this.btnProcess);

		x += width + space;
		width=100;
		this.btnRateChanging.setLabel("Mudar taxa");
		this.btnRateChanging.setBounds(x,y,width,height);		
		this.add(this.btnRateChanging);
		this.btnRateChanging.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeRating_actionPerformed(e);
			}
		});		
		
		
		this.btnStop.setLabel("Parar");
		x += width + space;
		width=50;
		this.btnStop.setBounds(x,y,width,height);				
		this.add(this.btnStop);	
		this.btnStop.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop_actionPerformed(e);
			}
		});
		

		/*
		 * Monta linha para tocar JMF
		 */
		y = y + 30;
		x = 20;
		width=160;
		this.btnProcessAndPlayJMF.setLabel("Gerar e tocar em JMF");
		this.btnProcessAndPlayJMF.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createAndPlayJMF_actionPerformed(e);
			}			
		});
		this.btnProcessAndPlayJMF.setBounds(x,y,width,height);				
		this.add(this.btnProcessAndPlayJMF);
		
		x += width + space;
		width=120;
		this.btnPlayJMF.setLabel("Tocar em JMF");
		this.btnPlayJMF.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playJMF_actionPerformed(e);
			}			
		});
		this.btnPlayJMF.setBounds(x,y,width,height);				
		this.add(this.btnPlayJMF);

		x += width + space;
		width=200;
		this.btnPlayRTPJMF.setLabel("Tocar usando RTP em JMF");
		this.btnPlayRTPJMF.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playRTPJMF_actionPerformed(e);
			}			
		});
		this.btnPlayRTPJMF.setBounds(x,y,width,height);				
		this.add(this.btnPlayRTPJMF);

		
		/*
		 * Monta linha para tocar VideoLan
		 */
		y = y + 30;
		x = 20;
		width=160;

		this.btnProcessAndPlayVLC.setLabel("Gerar e tocar em VLC");
		this.btnProcessAndPlayVLC.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createAndPlayVLC_actionPerformed(e);
			}			
		});
		this.btnProcessAndPlayVLC.setBounds(x,y,width,height);				
		this.add(this.btnProcessAndPlayVLC);
		
	
		x += width + space;
		width=120;
		this.btnPlayVLC.setLabel("Tocar em VLC");
		this.btnPlayVLC.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playVLC_actionPerformed(e);
			}			
		});
		this.btnPlayVLC.setBounds(x,y,width,height);				
		this.add(this.btnPlayVLC);
		

		x += width + space;
		width=100;
		this.btnProcessAndPlayWinamp.setLabel("Winamp");
		this.btnProcessAndPlayWinamp.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createAndPlayWinamp_actionPerformed(e);
			}			
		});
		this.btnProcessAndPlayWinamp.setBounds(x,y,width,height);				
		this.add(this.btnProcessAndPlayWinamp);


		/*
		 * Monta linha de recuperacao de instante
		 */		
		x = X_BEGIN_PARAMETERS;
		y = y + 40;
		height = 17;		
		this.lblTimescaleInstant.setText("Instante Inicial:");
		width = 100;
		this.lblTimescaleInstant.setBounds(x,y,width,height);		
		this.add(this.lblTimescaleInstant);
		
		x += width + space;
		width = 100;
		this.txtTimescaleInstant.setText("100");
		this.txtTimescaleInstant.setBounds(x,y,width,height);
		this.add(this.txtTimescaleInstant);

		x += width + space;
		width=80;
		this.btnTimescaleInstant.setLabel("Buscar");
		this.btnTimescaleInstant.addActionListener (new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculateInstant_actionPerformed(e);
			}			
		});
		this.btnTimescaleInstant.setBounds(x,y,width,height);				
		this.add(this.btnTimescaleInstant);

		x += width + space;
		height = 17;		
		this.lblTimescaleInstantResult.setText("");
		width = 500;
		this.lblTimescaleInstantResult.setBounds(x,y,width,height);		
		this.add(this.lblTimescaleInstantResult);
		
		/*
		 * Monta linha de apresentacao de resultados
		 */
        width = 140;
        x = widthFrame/2-width/2;
		y += height + 2*space;
		height=20;
		this.lblResult.setText("-- Resultados --");
		this.lblResult.setBounds(x,y,width,height);				
		this.add(this.lblResult);

		y += height + space;
		x = this.X_BEGIN_PARAMETERS;
		width = widthFrame-2*x;
		height = 120;
		this.textAreaResult.setBounds(x,y,width,height);
		this.add(this.textAreaResult);


		y += height + 2*space;		
		height = 25;
        this.panelPlayer = new JPanel();
        this.panelPlayer.setLayout(new BorderLayout());
        this.panelPlayer.setBackground(Color.WHITE);
        this.panelPlayer.setBounds(0,y,widthFrame,heightFrame-y);
    	GUIUpdater guiUpdater = new GUIUpdater(this, widthFrame, heightFrame-y);
    	this.controller = new GUIController(guiUpdater);
        
        this.add(this.panelPlayer);

        this.menuBar = new MenuBarAudio(this, controller);
		this.setMenuBar(menuBar);
	}

	private void calculateInstant_actionPerformed(ActionEvent e) {
		try {
			double d = Double.parseDouble(this.txtTimescaleInstant.getText());
			d = this.controller.getTimescaleInstant(d);
			this.lblTimescaleInstantResult.setText(d+"");
			repaint();
		}
		catch (Exception ex) {
			this.showResult(ex.getMessage());
		    showErrorMessage(ex);
		}
	}

	private void create_actionPerformed(ActionEvent e) {
		try {			
			if (!this.showErrors(true)) {
				int inputType=this.isFileOrStream();
				double rate = this.getRate();
				String inputFile = this.getInput();
				String outputFile = this.txtOutputFile.getText();
				InputTools inputTools = InputToolsBuilder.getInputTools(inputFile, inputType);
				outputFile = Functions.getOutputFileName(inputFile,outputFile,rate);
				this.controller.create(inputTools, outputFile, rate);									
			}
		}
		catch (Exception ex) {
			this.showResult(ex.getMessage());
		    showErrorMessage(ex);
		}
	}

	
	private void createAndPlayJMF_actionPerformed(ActionEvent e) {
		try {			
			if (!this.showErrors(true)) {
				int inputType=this.isFileOrStream();
				double rate = this.getRate();
				String inputFile = this.getInput();
				String outputFile = this.txtOutputFile.getText();
				IThirdPartyPlayer ap = new JMFFilePlayer();
				InputTools inputTools = InputToolsBuilder.getInputTools(inputFile, inputType);
				outputFile = Functions.getOutputFileName(inputFile,outputFile,rate);
				this.controller.createAndPlay(inputTools, outputFile, rate, ap);
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
		    showErrorMessage(ex);			
		}
	}
	
	private void createAndPlayVLC_actionPerformed(ActionEvent e) {
		try {	
			if (!this.showErrors(true)) {
				int inputType=this.isFileOrStream();
				double rate = this.getRate();
				String inputFile = this.getInput();
				String outputFile = this.txtOutputFile.getText();
				String vlcPath = IOConstants.VLC_PATH;
				IThirdPartyPlayer ap = new VLCFilePlayer(vlcPath);
				InputTools inputTools = InputToolsBuilder.getInputTools(inputFile, inputType);
				outputFile = Functions.getOutputFileName(inputFile,outputFile,rate);
				this.controller.createAndPlay(inputTools, outputFile, rate, ap);
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
		    showErrorMessage(ex);			
		}
	}

	private void createAndPlayWinamp_actionPerformed(ActionEvent e) {
		try {			
			if (!this.showErrors(true)) {
				int inputType=this.isFileOrStream();
				double rate = this.getRate();
				String inputFile = this.getInput();
				String outputFile = this.txtOutputFile.getText();
				String winampPath = IOConstants.WINAMP_PATH;
				IThirdPartyPlayer ap = new WinampFilePlayer(winampPath);
				InputTools inputTools = InputToolsBuilder.getInputTools(inputFile, inputType);
				outputFile = Functions.getOutputFileName(inputFile,outputFile,rate);
				this.controller.createAndPlay(inputTools, outputFile, rate, ap);
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
		    showErrorMessage(ex);			
		}
	}
	
	private void playJMF_actionPerformed(ActionEvent e) {
		try {			
			if (!this.showErrors(false)) {
				int inputType=this.isFileOrStream();			
				double rate = this.getRate();
				String inputFile = this.getInput();
				IThirdPartyPlayer ap = new JMFDataSourcePlayer();
				double anchors[] = this.getAnchors();
				InputTools inputTools = InputToolsBuilder.getInputTools(inputFile, inputType);
				this.controller.play(inputTools, rate, anchors, ap);
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
			showErrorMessage(ex);
		}
	}	
	
	
	private void playRTPJMF_actionPerformed(ActionEvent e) {
		try {		
			if (!this.showErrors(false)) {
				int inputType=this.isFileOrStream();
				double rate = this.getRate();
				String inputFile = this.getInput();
				String rtpMachine = IOConstants.RTP_MACHINE_MULTICAST;
				int rtpPortBase = IOConstants.THIRD_PART_PLAYER_RTP_PORT;
				int rtpTtl = IOConstants.RTP_TTL;
				int waitingTime = IOConstants.RTP_WAIT_TIME;
				JMFRTPTransmissionPlayer ap = new JMFRTPTransmissionPlayer(rtpMachine, rtpPortBase, rtpTtl, waitingTime);
				ap.addPlayChangingListener(new PlayWindowManager());
				double anchors[] = this.getAnchors();
				InputTools inputTools = InputToolsBuilder.getInputTools(inputFile, inputType);
				this.controller.play(inputTools, rate, anchors, ap);
			}
			else {
				showOpenFileMessage();						
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
			showErrorMessage(ex);
		}
	}	
	
	
	private void playVLC_actionPerformed(ActionEvent e) {
		try {			
			if (!this.showErrors(false)) {
				int inputType=this.isFileOrStream();
				double rate = this.getRate();
				String inputFile = this.getInput();
				String vlcPath = IOConstants.VLC_PATH;
				String rtpMachine = IOConstants.RTP_MACHINE;
				int rtpPortBase = IOConstants.THIRD_PART_PLAYER_RTP_PORT;
				int waitingTime = IOConstants.RTP_WAIT_TIME;
				IThirdPartyPlayer ap = new VLCRTPPlayer(vlcPath, rtpMachine, rtpPortBase, waitingTime);
				double anchors[] = this.getAnchors();
				InputTools inputTools = InputToolsBuilder.getInputTools(inputFile, inputType);
				this.controller.play(inputTools, rate, anchors, ap);
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
			showErrorMessage(ex);
		}
	}
	
	private void changeRating_actionPerformed(ActionEvent e) {
		try {			
			if (!this.showErrors(false)) {
				double rate = getRate();
				this.controller.setRate(rate);
				this.showResult("Mudou taxa para " + rate + "\n");
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
			showErrorMessage(ex);
  		}		
	}	
	
	private void stop_actionPerformed(ActionEvent e) {
		this.controller.stop();	
	    this.showResult("Parou processamento com sucesso" + "\n");
	}
	
	private double[] getAnchors() {
		String text = this.txtTimescaleInstant.getText();
		StringTokenizer tokenizer = new StringTokenizer(text,",");
		double args[] = new double[tokenizer.countTokens()];
		for (int i=0; i<args.length; i++) {
			args[i] = Double.parseDouble((tokenizer.nextToken()));
		}
		this.lblTimescaleInstantResult.setText("");
		return args;
	}		
	
	private double getRate() {
		return ((double)this.sliderTimescale.getValue())/100;
	}
	
	public JPanel getPanelPlayer () {
		return this.panelPlayer;
	}
	
	public void setFileName(TextField field, File f) {		
	    field.setText(f.getAbsolutePath());		
	}
	
	private void getInputFile_actionPerformed(ActionEvent e) {
		try {			
			FileDialog fd = new FileDialog(this, MSG_CHOOSE_FILE);
			fd.setVisible(true);
			String fileName = fd.getFile();
			String directory = fd.getDirectory();
			if (fileName!=null && directory!=null) {
			    File f = new File(directory+fileName);
			    this.setFileName(this.txtInputFile, f);
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
  		}		
	}
	
	private void getOutputFile_actionPerformed(ActionEvent e) {
		try {			
			FileDialog fd = new FileDialog(this, MSG_CHOOSE_FILE);
			fd.setVisible(true);
			String fileName = fd.getFile();
			String directory = fd.getDirectory();
			if (fileName!=null && directory!=null) {
			    File f = new File(directory+fileName);
			    this.setFileName(this.txtOutputFile, f);
			}
		}
		catch (Exception ex) {
		    this.showResult(ex.getMessage());
  		}
	}
	
	private void sliderTimescale_changeEvent(ChangeEvent e) {
	    this.lblTimescale.setText(this.sliderTimescale.getValue()+"");
	}

	private boolean showErrors(boolean considerOutput) {
		boolean showAny = false;
		int inputType=this.isFileOrStream();
		if (inputType==IOConstants.InputSource.NO_INPUT) {
			showOpenFileMessage();
			showAny = true;
		}
		else if (considerOutput && !isOutputValid()) { 
			showOutputMessage();
			showAny = true;
		}
		
		if (inputType==IOConstants.InputSource.REMOTE && considerOutput) {
			if (this.getInput().endsWith("mpeg") || this.getInput().endsWith("mpg")) {
				this.showOtherMessage("Nao pode gerar arquivo de video ainda");
				showAny = true;
			}			
		}
		return showAny;
	}
	
	private int isFileOrStream() {
		int type;
	    if (!this.txtInputFile.getText().trim().equals("")) {
	        type=IOConstants.InputSource.LOCAL;
	    }
	    else if (!this.txtRemoteInputFile.getText().trim().equals("")) {
	    	type=IOConstants.InputSource.REMOTE;
	    }
	    else {
	    	type=IOConstants.InputSource.NO_INPUT;
	    }
	    return type;
	}
	
	private boolean isOutputValid() {
		boolean isValid=false;
		int type = this.isFileOrStream();
		if (type==IOConstants.InputSource.LOCAL ||
			(type==IOConstants.InputSource.REMOTE && !this.txtOutputFile.getText().equals(""))) {
			isValid=true;
		}
		return isValid;
	}
	
	private String getInput() {
		String s;
		if (this.isFileOrStream()==IOConstants.InputSource.LOCAL) {
			s=this.txtInputFile.getText();
		}
		else {
			s=this.txtRemoteInputFile.getText();
		}
		return s;
	}
	
	private void showOpenFileMessage() {
		String msg = "Indique um áudio de entrada";
		JOptionPane.showMessageDialog(this,msg,"Aviso",JOptionPane.INFORMATION_MESSAGE);		
	}
	
	private void showOutputMessage() {
		String msg = "Indique um áudio de saída";
		JOptionPane.showMessageDialog(this,msg,"Aviso",JOptionPane.INFORMATION_MESSAGE);		
	}	
	
	private void showOtherMessage(String msg) {
		JOptionPane.showMessageDialog(this,msg,"Aviso",JOptionPane.INFORMATION_MESSAGE);				
	}
		
	private void showErrorMessage (Exception ex) {
		ex.printStackTrace();
		String msg = "Erro: " + ex.getMessage();
		JOptionPane.showMessageDialog(this,msg,"Erro",JOptionPane.ERROR_MESSAGE);				
	}

	public void showResult(String s) {
        String actualMsg = this.textAreaResult.getText(); 
	    this.textAreaResult.setText(actualMsg+s);                
	}

}
