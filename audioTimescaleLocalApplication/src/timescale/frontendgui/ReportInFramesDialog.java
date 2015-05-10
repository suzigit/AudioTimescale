/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (21/07/2004)
 */
 
package timescale.frontendgui;

import java.awt.*;
import java.awt.event.*;

import util.functions.Convert;
import timescale.backendgui.GUIController;
import timescale.data.FinalReport;


/**
 * Essa classe exibe um relatorio de frames lidos/escritos/processados.
 */
public class ReportInFramesDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;
	
	private Panel resultsPanel = new Panel();
	private Panel resultsPanelLine1 = new Panel();	
	private Panel resultsPanelLine2 = new Panel();
	private Panel resultsPanelLine3 = new Panel();
	private Label lblRealRateTittle = new Label();
	private Label lblRealRateData = new Label();
	private Label lblTotalFramesTittle = new Label();
	private Label lblTotalFramesData = new Label();	
	private Label lblProcessedFramesTittle = new Label();
	private Label lblProcessedFramesData = new Label();
	
	private GUIController controller;
	private double realRate=0;
	private double totalOfFrames=0;
	private double totalOfProcessedFrames=0;
	

	/** 
	 * Controi instancia da classe.
	 * @param f janela de exibicao do dialog
	 * @param controller controlador que oferece realizacao de servicos
	 */		
	public ReportInFramesDialog(Frame f, GUIController controller) {		
		super (f, "Relatório em frames", true);
		
		this.controller = controller;
		this.setLocation(f.getLocation());
		this.setSize(350, 100);
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.resultsPanel.setLayout(new GridLayout(3,1));
		this.setReport();
		
		this.lblRealRateTittle.setText("Ajuste realizado:");
		this.resultsPanelLine1.add(this.lblRealRateTittle);	
		this.lblRealRateData.setText(""+Convert.roundDouble(realRate,2));
		this.resultsPanelLine1.add(this.lblRealRateData);
		this.lblTotalFramesTittle.setText("Número de frames do arquivo:");
		this.resultsPanelLine2.add(this.lblTotalFramesTittle);
		this.lblTotalFramesData.setText(""+totalOfFrames);
		this.resultsPanelLine2.add(this.lblTotalFramesData);	
		this.lblProcessedFramesTittle.setText("Número de frames inseridos ou retirados:");
		this.resultsPanelLine3.add(this.lblProcessedFramesTittle);
		this.lblProcessedFramesData.setText(""+totalOfProcessedFrames);	
		this.resultsPanelLine3.add(this.lblProcessedFramesData);
		this.resultsPanelLine1.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.resultsPanel.add(this.resultsPanelLine1);
		this.resultsPanelLine2.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.resultsPanel.add(this.resultsPanelLine2);
		this.resultsPanelLine3.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.resultsPanel.add(this.resultsPanelLine3);
		this.add(this.resultsPanel);		
	}
	
	private void setReport() {
		FinalReport report = this.controller.getReport();
		if (report!=null) {
			this.realRate=report.getRealRate();
			this.totalOfFrames=report.getNumberOfReadFrames();
			this.totalOfProcessedFrames=report.getNumberProcessedFrames();
		}
	}
	
	protected void processWindowEvent(WindowEvent e) {
	  if (e.getID() == WindowEvent.WINDOW_CLOSING) {
		dispose();
	  }
	  super.processWindowEvent(e);
	}
	

	
}
