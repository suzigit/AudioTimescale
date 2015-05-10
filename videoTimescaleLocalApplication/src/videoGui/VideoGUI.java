/*
 * Created on Sep 29, 2004
 */
package videoGui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import timescale.data.ParametersProcessment;
import timescale.video.assembler.VideoAssembler;
import timescale.video.assembler.VideoDisassembler;
import timescale.video.controller.VideoController;
import timescale.video.processor.*;
import timescale.video.statistics.*;
import timescale.video.utils.Constants;
import util.io.FileOutputTools;
import util.io.IoTools;
import util.io.OutputTools;

/**
 * @author Sergio Cavendish
 */
public class VideoGUI extends JFrame {
	
	public final static int INPUT_PANEL = 0;
	public final static int OUTPUT_PANEL = 1;
	public final static int GENERAL_PANEL = 2;
	
	VideoController controller;
	StatsCollector collector = StatsCollector.instance();
	
	private Container container;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	
	private final int TEXT_SIZE1 = 10;
	private final int TEXT_SIZE2 = 20;
	
	JCheckBox controllerCheckBox, statsCheckBox, otherCheckBox;
	JSlider stretchSlider;
	JLabel statusBar,
	inputFileLabel,
	outputFileLabel,
	outputStretchRateLabel,
	pictureProcessingRateLabel;
	JComboBox controlStrategyComboBox, statsStrategyComboBox;
	ProcessorStrategy controlStrategy[] = { new AddDropBFrames()};
	JTextField stretchRateField,
	frameRateField,
	controlBitrateField,
	inputBufferSizeField,
	inputSharedBufferSizeField,
	outputSharedBufferSizeField,
	sliderRangeField,
	inputFileField,
	outputFileField,
	inputBitrateField,
	outputBitrateField,
	inputFrameRateField,
	outputFrameRateField,
	inputSequencesField,
	inputGOPField,
	inputIFramesField,
	inputPFramesField,
	inputBFramesField,
	inputTotalField,
	outputSequencesField,
	outputGOPField,
	outputIFramesField,
	outputPFramesField,
	outputBFramesField,
	outputTotalField,
	generalProcessingTimeField,
	generalStretchRateField,
	pictureProcessingRateField;
	JButton startButton, stopButton, selectInputButton, selectOutputButton;
	StatsStrategy statsStrategy[] =
	{
			new LastPicturesStrategy(),
			new LastSequenceStrategy(),
			new WholeStreamStrategy()};
	
	private JPanel controlPanel,
	stretchPanel,
	inputPanel,
	outputPanel,
	generalPanel,
	buttonPanel,
	statusPanel;
	
	JSliderHandler sliderHandler;
	GridBagLayout stretchLayout;
	GridBagConstraints stretchConstraints;
	
	private UIManager.LookAndFeelInfo looks[];
	
	private int type, inputBufferSize, inputSharedBufferSize,
	outputSharedBufferSize, sliderRange, sliderSpacing;
	private Stats stats;
	private double rate;
	private DecimalFormat formatNumber1, formatNumber2, formatNumber3;
	
	private static VideoGUI myInstance = null;
	
	public static VideoGUI instance(String name, VideoController control) {
		return myInstance = new VideoGUI(name, control);
	}
	
	public static VideoGUI instance() {
		return myInstance;
	}
	
	protected VideoGUI(String name, VideoController control) {
		super(name);
		
		formatNumber1 = new DecimalFormat("0.00");
		formatNumber2 = new DecimalFormat("0.00000000");
		formatNumber3 = new DecimalFormat("0.000");
		
		controller = control;
		
		JCheckBoxHandler checkBoxHandler = new JCheckBoxHandler();
		JComboBoxHandler comboBoxHandler = new JComboBoxHandler();
		JTextFieldHandler textFieldHandler = new JTextFieldHandler();
		JButtonHandler buttonHandler = new JButtonHandler();
		sliderHandler = new JSliderHandler();
		
		Font panelHeader = new Font("SansSerif", Font.BOLD, 18);
		Font innerPanelHeader = new Font("SansSerif", Font.ITALIC, 14);
		
		TitledBorder controlTitle = BorderFactory.createTitledBorder("Control");
		controlTitle.setTitleFont(panelHeader);
		controlTitle.setTitleColor(Color.BLUE);
		
		/*
		 * Control Panel
		 */
		
		controlPanel = new JPanel();
		controlPanel.setBorder(controlTitle);
		
		GridBagLayout controlLayout = new GridBagLayout();
		controlPanel.setLayout(controlLayout);
		GridBagConstraints controlConstraints = new GridBagConstraints();
		
		controlConstraints.anchor = GridBagConstraints.WEST;
		
		controllerCheckBox = new JCheckBox("Processor");
		controllerCheckBox.addItemListener(checkBoxHandler);
		controllerCheckBox.setFont(innerPanelHeader);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				controllerCheckBox,
				0,
				0,
				1,
				1);
		
		JLabel controlStrategyLabel = new JLabel("Processor Strategy:");
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				controlStrategyLabel,
				1,
				0,
				1,
				1);
		
		String controlStrategyNames[] = { "AddDropBframes" };
		
		controlStrategyComboBox = new JComboBox(controlStrategyNames);
		controlStrategyComboBox.setEnabled(false);
		controlStrategyComboBox.setMaximumRowCount(3);
		controlStrategyComboBox.addItemListener(comboBoxHandler);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				controlStrategyComboBox,
				1,
				1,
				1,
				1);
		
		statsCheckBox = new JCheckBox("Statistics");
		statsCheckBox.addItemListener(checkBoxHandler);
		statsCheckBox.setFont(innerPanelHeader);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				statsCheckBox,
				2,
				0,
				1,
				1);
		
		JLabel statsStrategyLabel = new JLabel("Stats Strategy:");
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				statsStrategyLabel,
				3,
				0,
				1,
				1);
		
		String statsStrategyNames[] =
		{ "LastPictures", "LastSequence", "WholeStream" };
		
		statsStrategyComboBox = new JComboBox(statsStrategyNames);
		statsStrategyComboBox.setEnabled(false);
		statsStrategyComboBox.setMaximumRowCount(3);
		statsStrategyComboBox.addItemListener(comboBoxHandler);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				statsStrategyComboBox,
				3,
				1,
				1,
				1);
		
		otherCheckBox = new JCheckBox("Other Adjustments");
		otherCheckBox.addItemListener(checkBoxHandler);
		otherCheckBox.setFont(innerPanelHeader);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				otherCheckBox,
				4,
				0,
				1,
				1);
		
		JLabel controlFrameRateLabel = new JLabel("Frame Rate:");
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				controlFrameRateLabel,
				5,
				0,
				1,
				1);
		
		frameRateField = new JTextField(TEXT_SIZE1);
		frameRateField.setEnabled(false);
		frameRateField.addActionListener(textFieldHandler);
		frameRateField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				frameRateField,
				5,
				1,
				1,
				1);
		
		JLabel controlBitrateLabel = new JLabel("Bit Rate:");
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				controlBitrateLabel,
				6,
				0,
				1,
				1);
		
		controlBitrateField = new JTextField(TEXT_SIZE1);
		controlBitrateField.setEnabled(false);
		controlBitrateField.addActionListener(textFieldHandler);
		controlBitrateField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				controlBitrateField,
				6,
				1,
				1,
				1);
		
		JLabel inputBufferSizeLabel = new JLabel("Input Buffer Size (bytes):");
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				inputBufferSizeLabel,
				7,
				0,
				1,
				1);
		
		inputBufferSizeField = new JTextField("32000", TEXT_SIZE1);
		inputBufferSizeField.setEnabled(false);
		inputBufferSizeField.addActionListener(textFieldHandler);
		inputBufferSizeField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				inputBufferSizeField,
				7,
				1,
				1,
				1);
		
		JLabel inputSharedBufferSizeLabel = new JLabel("Input Shared Buffer Size (Obj):");
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				inputSharedBufferSizeLabel,
				8,
				0,
				1,
				1);
		
		inputSharedBufferSizeField = new JTextField("2", TEXT_SIZE1);
		inputSharedBufferSizeField.setEnabled(false);
		inputSharedBufferSizeField.addActionListener(textFieldHandler);
		inputSharedBufferSizeField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				inputSharedBufferSizeField,
				8,
				1,
				1,
				1);
		
		JLabel outputSharedBufferSizeLabel = new JLabel("Output Shared Buffer Size (Obj):");
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				outputSharedBufferSizeLabel,
				9,
				0,
				1,
				1);
		
		outputSharedBufferSizeField = new JTextField("2", TEXT_SIZE1);
		outputSharedBufferSizeField.setEnabled(false);
		outputSharedBufferSizeField.addActionListener(textFieldHandler);
		outputSharedBufferSizeField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				controlPanel,
				controlLayout,
				controlConstraints,
				outputSharedBufferSizeField,
				9,
				1,
				1,
				1);
		
		
		/*
		 * Stretch Control Panel
		 */
		TitledBorder stretchTitle = BorderFactory.createTitledBorder("Stretch Adjustment");
		stretchTitle.setTitleFont(panelHeader);
		stretchTitle.setTitleColor(Color.BLUE);
		
		stretchPanel = new JPanel();
		stretchPanel.setBorder(stretchTitle);
		
		stretchLayout = new GridBagLayout();
		stretchPanel.setLayout(stretchLayout);
		stretchConstraints = new GridBagConstraints();
		
		stretchConstraints.anchor = GridBagConstraints.WEST;
		
		JLabel sliderRangeLabel = new JLabel("Slider Range (%):");
		addComponent(
				stretchPanel,
				stretchLayout,
				stretchConstraints,
				sliderRangeLabel,
				0,
				0,
				1,
				1);
		
		
		sliderRange = 100;
		sliderRangeField = new JTextField("" + sliderRange, TEXT_SIZE1);
		sliderRangeField.setHorizontalAlignment(JTextField.RIGHT);
		sliderRangeField.addActionListener(textFieldHandler);
		addComponent(
				stretchPanel,
				stretchLayout,
				stretchConstraints,
				sliderRangeField,
				0,
				1,
				1,
				1);
		
		JLabel stretchRateLabel = new JLabel("Stretch Rate (%):");
		addComponent(
				stretchPanel,
				stretchLayout,
				stretchConstraints,
				stretchRateLabel,
				1,
				0,
				1,
				1);
		
		stretchRateField = new JTextField("0", TEXT_SIZE1);
		stretchRateField.setHorizontalAlignment(JTextField.RIGHT);
		stretchRateField.addActionListener(textFieldHandler);
		addComponent(
				stretchPanel,
				stretchLayout,
				stretchConstraints,
				stretchRateField,
				1,
				1,
				1,
				1);
		
		sliderSpacing = sliderRange;
		stretchSlider = new JSlider(SwingConstants.HORIZONTAL, (-1) * sliderRange,
				sliderRange, 0);
		stretchSlider.setPaintLabels(true);
		stretchSlider.setPaintTicks(true);
		stretchSlider.setMajorTickSpacing(sliderSpacing);
		stretchSlider.setMinorTickSpacing(sliderSpacing / 4);
		stretchSlider.addChangeListener(sliderHandler);
		addComponent(
				stretchPanel,
				stretchLayout,
				stretchConstraints,
				stretchSlider,
				3,
				0,
				3,
				3);
		
		/*
		 * Input Panel
		 */
		
		TitledBorder inputTitle = BorderFactory.createTitledBorder("Input");
		inputTitle.setTitleFont(panelHeader);
		inputTitle.setTitleColor(Color.BLUE);
		
		inputPanel = new JPanel();
		inputPanel.setBorder(inputTitle);
		GridBagLayout inputLayout = new GridBagLayout();
		inputPanel.setLayout(inputLayout);
		GridBagConstraints inputConstraints = new GridBagConstraints();
		
		inputConstraints.anchor = GridBagConstraints.WEST;
		
		inputFileLabel = new JLabel("Input File:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputFileLabel,
				0,
				0,
				1,
				1);
		
		inputFileField = new JTextField(TEXT_SIZE2);
		inputFileField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputFileField,
				0,
				1,
				1,
				1);
		
		selectInputButton = new JButton("Browse");
		selectInputButton.addActionListener(buttonHandler);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				selectInputButton,
				0,
				2,
				1,
				1);
		
		inputConstraints.anchor = GridBagConstraints.CENTER;
		
		JLabel inputVideoDataLabel = new JLabel("Video Data");
		inputVideoDataLabel.setFont(innerPanelHeader);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputVideoDataLabel,
				1,
				0,
				3,
				1);
		
		inputConstraints.anchor = GridBagConstraints.WEST;
		
		JLabel inputBitrateLabel = new JLabel("Bit Rate:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputBitrateLabel,
				2,
				0,
				1,
				1);
		
		inputBitrateField = new JTextField(TEXT_SIZE1);
		inputBitrateField.setEditable(false);
		inputBitrateField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputBitrateField,
				2,
				1,
				1,
				1);
		
		JLabel inputFrameRateLabel = new JLabel("Frame Rate:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputFrameRateLabel,
				3,
				0,
				1,
				1);
		
		inputFrameRateField = new JTextField(TEXT_SIZE1);
		inputFrameRateField.setEditable(false);
		inputFrameRateField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputFrameRateField,
				3,
				1,
				1,
				1);
		
		inputConstraints.anchor = GridBagConstraints.CENTER;
		
		JLabel inputStatsLabel = new JLabel("Statistics");
		inputStatsLabel.setFont(innerPanelHeader);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputStatsLabel,
				4,
				0,
				3,
				1);
		
		inputConstraints.anchor = GridBagConstraints.WEST;
		
		JLabel inputSequenceLabel = new JLabel("Sequences:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputSequenceLabel,
				5,
				0,
				1,
				1);
		
		inputSequencesField = new JTextField(TEXT_SIZE1);
		inputSequencesField.setEditable(false);
		inputSequencesField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputSequencesField,
				5,
				1,
				1,
				1);
		
		JLabel inputGopLabel = new JLabel("GOPs:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputGopLabel,
				6,
				0,
				1,
				1);
		
		inputGOPField = new JTextField(TEXT_SIZE1);
		inputGOPField.setEditable(false);
		inputGOPField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputGOPField,
				6,
				1,
				1,
				1);
		
		JLabel inputPictureLabel = new JLabel("Picture");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputPictureLabel,
				7,
				0,
				1,
				1);
		
		JLabel inputILabel = new JLabel("I Frames:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputILabel,
				8,
				0,
				1,
				1);
		
		inputIFramesField = new JTextField(TEXT_SIZE1);
		inputIFramesField.setEditable(false);
		inputIFramesField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputIFramesField,
				8,
				1,
				1,
				1);
		
		JLabel inputPLabel = new JLabel("P Frames:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputPLabel,
				9,
				0,
				1,
				1);
		
		inputPFramesField = new JTextField(TEXT_SIZE1);
		inputPFramesField.setEditable(false);
		inputPFramesField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputPFramesField,
				9,
				1,
				1,
				1);
		
		JLabel inputBLabel = new JLabel("B Frames:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputBLabel,
				10,
				0,
				1,
				1);
		
		inputBFramesField = new JTextField(TEXT_SIZE1);
		inputBFramesField.setEditable(false);
		inputBFramesField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputBFramesField,
				10,
				1,
				1,
				1);
		
		JLabel inputTotalLabel = new JLabel("Total:");
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputTotalLabel,
				11,
				0,
				1,
				1);
		
		inputTotalField = new JTextField(TEXT_SIZE1);
		inputTotalField.setEditable(false);
		inputTotalField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				inputPanel,
				inputLayout,
				inputConstraints,
				inputTotalField,
				11,
				1,
				1,
				1);
		
		/*
		 * Output Panel
		 */
		
		TitledBorder outputTitle = BorderFactory.createTitledBorder("Output");
		outputTitle.setTitleFont(panelHeader);
		outputTitle.setTitleColor(Color.BLUE);
		
		outputPanel = new JPanel();
		outputPanel.setBorder(outputTitle);
		GridBagLayout outputLayout = new GridBagLayout();
		outputPanel.setLayout(outputLayout);
		GridBagConstraints outputConstraints = new GridBagConstraints();
		
		outputConstraints.anchor = GridBagConstraints.WEST;
		
		outputFileLabel = new JLabel("Output File:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputFileLabel,
				0,
				0,
				1,
				1);
		
		outputFileField = new JTextField(TEXT_SIZE2);
		outputFileField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputFileField,
				0,
				1,
				1,
				1);
		
		selectOutputButton = new JButton("Browse");
		selectOutputButton.addActionListener(buttonHandler);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				selectOutputButton,
				0,
				2,
				1,
				1);
		
		outputConstraints.anchor = GridBagConstraints.CENTER;
		
		JLabel outputVideoDataLabel = new JLabel("Video Data");
		outputVideoDataLabel.setFont(innerPanelHeader);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputVideoDataLabel,
				1,
				0,
				3,
				1);
		
		outputConstraints.anchor = GridBagConstraints.WEST;
		
		JLabel outputBitrateLabel = new JLabel("Bit Rate:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputBitrateLabel,
				2,
				0,
				1,
				1);
		
		outputBitrateField = new JTextField(TEXT_SIZE1);
		outputBitrateField.setEditable(false);
		outputBitrateField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputBitrateField,
				2,
				1,
				1,
				1);
		
		JLabel outputFrameRateLabel = new JLabel("Frame Rate:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputFrameRateLabel,
				3,
				0,
				1,
				1);
		
		outputFrameRateField = new JTextField(TEXT_SIZE1);
		outputFrameRateField.setEditable(false);
		outputFrameRateField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputFrameRateField,
				3,
				1,
				1,
				1);
		
		outputConstraints.anchor = GridBagConstraints.CENTER;
		
		JLabel outputStatsLabel = new JLabel("Statistics");
		outputStatsLabel.setFont(innerPanelHeader);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputStatsLabel,
				4,
				0,
				3,
				1);
		
		outputConstraints.anchor = GridBagConstraints.WEST;
		
		JLabel outputSequenceLabel = new JLabel("Sequences:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputSequenceLabel,
				5,
				0,
				1,
				1);
		
		outputSequencesField = new JTextField(TEXT_SIZE1);
		outputSequencesField.setEditable(false);
		outputSequencesField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputSequencesField,
				5,
				1,
				1,
				1);
		
		JLabel outputGopLabel = new JLabel("GOPs:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputGopLabel,
				6,
				0,
				1,
				1);
		
		outputGOPField = new JTextField(TEXT_SIZE1);
		outputGOPField.setEditable(false);
		outputGOPField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputGOPField,
				6,
				1,
				1,
				1);
		
		JLabel outputPictureLabel = new JLabel("Picture");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputPictureLabel,
				7,
				0,
				1,
				1);
		
		JLabel outputILabel = new JLabel("I Frames:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputILabel,
				8,
				0,
				1,
				1);
		
		outputIFramesField = new JTextField(TEXT_SIZE1);
		outputIFramesField.setEditable(false);
		outputIFramesField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputIFramesField,
				8,
				1,
				1,
				1);
		
		JLabel outputPLabel = new JLabel("P Frames:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputPLabel,
				9,
				0,
				1,
				1);
		
		outputPFramesField = new JTextField(TEXT_SIZE1);
		outputPFramesField.setEditable(false);
		outputPFramesField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputPFramesField,
				9,
				1,
				1,
				1);
		
		JLabel outputBLabel = new JLabel("B Frames:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputBLabel,
				10,
				0,
				1,
				1);
		
		outputBFramesField = new JTextField(TEXT_SIZE1);
		outputBFramesField.setEditable(false);
		outputBFramesField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputBFramesField,
				10,
				1,
				1,
				1);
		
		JLabel outputTotalLabel = new JLabel("Total:");
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputTotalLabel,
				11,
				0,
				1,
				1);
		
		outputTotalField = new JTextField(TEXT_SIZE1);
		outputTotalField.setEditable(false);
		outputTotalField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				outputPanel,
				outputLayout,
				outputConstraints,
				outputTotalField,
				11,
				1,
				1,
				1);
		
		/*
		 * General Panel
		 */
		
		TitledBorder generalTitle = BorderFactory.createTitledBorder("General");
		generalTitle.setTitleFont(panelHeader);
		generalTitle.setTitleColor(Color.BLUE);
		
		generalPanel = new JPanel();
		generalPanel.setBorder(generalTitle);
		GridBagLayout generalLayout = new GridBagLayout();
		generalPanel.setLayout(generalLayout);
		GridBagConstraints generalConstraints = new GridBagConstraints();
		
		generalConstraints.anchor = GridBagConstraints.WEST;
		
		JLabel processingTimeLabel = new JLabel("Elapsed Time (seconds):");
		addComponent(
				generalPanel,
				generalLayout,
				generalConstraints,
				processingTimeLabel,
				0,
				0,
				1,
				1);
		
		generalProcessingTimeField = new JTextField(TEXT_SIZE2);
		generalProcessingTimeField.setEditable(false);
		generalProcessingTimeField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				generalPanel,
				generalLayout,
				generalConstraints,
				generalProcessingTimeField,
				0,
				1,
				1,
				1);
		
		outputStretchRateLabel = new JLabel("Output Stretch Rate:");
		addComponent(
				generalPanel,
				generalLayout,
				generalConstraints,
				outputStretchRateLabel,
				1,
				0,
				1,
				1);
		
		generalStretchRateField = new JTextField(TEXT_SIZE2);
		generalStretchRateField.setEditable(false);
		generalStretchRateField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				generalPanel,
				generalLayout,
				generalConstraints,
				generalStretchRateField,
				1,
				1,
				1,
				1);
		
		pictureProcessingRateLabel = new JLabel("Picture Processing Rate:");
		addComponent(
				generalPanel,
				generalLayout,
				generalConstraints,
				pictureProcessingRateLabel,
				2,
				0,
				1,
				1);
		
		pictureProcessingRateField = new JTextField(TEXT_SIZE2);
		pictureProcessingRateField.setEditable(false);
		pictureProcessingRateField.setHorizontalAlignment(JTextField.RIGHT);
		addComponent(
				generalPanel,
				generalLayout,
				generalConstraints,
				pictureProcessingRateField,
				2,
				1,
				1,
				1);
		
		/*
		 * Button Panel
		 */
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		
		startButton = new JButton("Start");
		startButton.addActionListener(buttonHandler);
		buttonPanel.add(startButton);
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(buttonHandler);
		stopButton.setEnabled(false);
		buttonPanel.add(stopButton);
		
		/*
		 * Layout de todo o JFrame
		 */
		container = getContentPane();
		layout = new GridBagLayout();
		container.setLayout(layout);
		
		Insets insets = new Insets(5, 5, 5, 5);
		constraints =
			new GridBagConstraints(
					0,
					0,
					1,
					1,
					0,
					0,
					GridBagConstraints.CENTER,
					GridBagConstraints.NONE,
					insets,
					5,
					5);
		
		constraints.anchor = GridBagConstraints.NORTHWEST;
		
		addComponent(container, layout, constraints, controlPanel, 0, 0, 1, 1);
		addComponent(container, layout, constraints, inputPanel, 0, 1, 1, 1);
		addComponent(container, layout, constraints, outputPanel, 0, 2, 1, 1);
		
		constraints.anchor = GridBagConstraints.NORTHWEST;
		addComponent(container, layout, constraints, stretchPanel, 1, 1, 1, 1);
		
		constraints.anchor = GridBagConstraints.SOUTH;
		addComponent(container, layout, constraints, buttonPanel, 1, 0, 1, 1);
		
		constraints.anchor = GridBagConstraints.NORTHWEST;
		addComponent(container, layout, constraints, generalPanel, 1, 2, 2, 1);
		
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		statusBar = new JLabel("  ");
		addComponent(container, layout, constraints, statusBar, 2, 0, 3, 1);
		
		looks = UIManager.getInstalledLookAndFeels();
		try {
			UIManager.setLookAndFeel(looks[2].getClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(
					null,
					exception.toString(),
					"Look and Feel Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
		pack();
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void addComponent(
			JPanel panel,
			GridBagLayout layout,
			GridBagConstraints constraints,
			Component component,
			int row,
			int column,
			int width,
			int height) {
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		
		layout.setConstraints(component, constraints);
		panel.add(component);
	}
	
	private void addComponent(
			Container container,
			GridBagLayout layout,
			GridBagConstraints constraints,
			Component component,
			int row,
			int column,
			int width,
			int height) {
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		
		layout.setConstraints(component, constraints);
		container.add(component);
	}
	
	private class JCheckBoxHandler implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			if (event.getSource() == controllerCheckBox) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					controlStrategyComboBox.setEnabled(true);
					stretchRateField.setEnabled(true);
				} else {
					controlStrategyComboBox.setEnabled(false);
					stretchRateField.setEnabled(false);
				}
			} else if (event.getSource() == statsCheckBox) {
				if (event.getStateChange() == ItemEvent.SELECTED)
					statsStrategyComboBox.setEnabled(true);
				else
					statsStrategyComboBox.setEnabled(false);
			} else if (event.getSource() == otherCheckBox) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					frameRateField.setEnabled(true);
					controlBitrateField.setEnabled(true);
					inputBufferSizeField.setEnabled(true);
					inputSharedBufferSizeField.setEnabled(true);
					outputSharedBufferSizeField.setEnabled(true);
				} else {
					frameRateField.setEnabled(false);
					controlBitrateField.setEnabled(false);
					inputBufferSizeField.setEnabled(false);
					inputSharedBufferSizeField.setEnabled(false);
					outputSharedBufferSizeField.setEnabled(false);
				}
			}
		}
	}
	
	private class JComboBoxHandler implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			if (event.getSource() == controlStrategyComboBox)
				if (event.getStateChange() == ItemEvent.SELECTED) {
					/*
					 controller.setControlStrategy(
					 controlStrategy[controlStrategyComboBox
					 .getSelectedIndex()]);*/
				} else if (event.getSource() == statsStrategyComboBox) {
					if (event.getStateChange() == ItemEvent.SELECTED) {
						controller.setStatsStrategy(
								statsStrategy[statsStrategyComboBox
											  .getSelectedIndex()]);
					}
				}
		}
	}
	
	private class JTextFieldHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == frameRateField) {
				// falta transferir o valor colocado aqui para o programa
			} else if (event.getSource() == controlBitrateField) {
				// falta tranferir o valor colocado aqui para o programa
			}	else if (event.getSource() == stretchRateField){
				int stretchRateValue = Integer.parseInt(stretchRateField.getText());
				int absStretchRateValue = Math.abs(stretchRateValue);
				int sliderRangeValue = Integer.parseInt(sliderRangeField.getText());
				if(absStretchRateValue > sliderRangeValue){
					sliderRangeValue = absStretchRateValue;
					sliderRangeField.setText("" + sliderRangeValue);									
					stretchSlider.setMinimum((-1) * sliderRangeValue);
					stretchSlider.setMaximum(sliderRangeValue);
					stretchSlider.setMajorTickSpacing(sliderRangeValue);
					stretchSlider.setMinorTickSpacing(sliderRangeValue / 4);
				}
				stretchSlider.setValue(stretchRateValue);
				//controller.setStretchRate((100 + stretchRateValue) / 100);
			}	else if (event.getSource() == sliderRangeField){
				int stretchRangeValue = Integer.parseInt(sliderRangeField.getText());
				if(stretchRangeValue > 0){
					int stretchRateValue = stretchSlider.getValue();
					stretchSlider.setMaximum(stretchRangeValue);
					stretchSlider.setMinimum((-1) * stretchRangeValue);
					stretchSlider.setMajorTickSpacing(stretchRangeValue);
					stretchSlider.setMinorTickSpacing(stretchRangeValue / 4);
					stretchSlider.setValue(stretchRateValue);
				}
			}
		}
	}
	
	private class JButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			if (event.getSource() == startButton) {
				
				try { 
					
					String inputFile = inputFileField.getText();
					String outputFile = outputFileField.getText();
					if (!inputFile.equals("") && !outputFile.equals("")) {
						
						String rateString = stretchRateField.getText();
						rate = 1.0;
						if (!rateString.equals(""))
							rate = 1.0 + Double.parseDouble(rateString)/100;
						
						inputBufferSize = 0;
						String inputBufferSizeString = inputBufferSizeField.getText();
						if(!inputBufferSizeString.equals(""))
							inputBufferSize = Integer.parseInt(inputBufferSizeString); 
						inputSharedBufferSize = outputSharedBufferSize = 2;
						String inputSharedBufferSizeString = inputSharedBufferSizeField.getText();
						if(!inputSharedBufferSizeString.equals(""))
							inputSharedBufferSize = Integer.parseInt(inputSharedBufferSizeString);
						String outputSharedBufferSizeString = outputSharedBufferSizeField.getText();
						if(!outputSharedBufferSizeString.equals(""))
							outputSharedBufferSize = Integer.parseInt(outputSharedBufferSizeString);
	
//Sergio, acrescentei o codigo abaixo para tua GUI continuar funcionando						
//						controller.startProcessing(inputFile, outputFile, rate, inputBufferSize,
//								inputSharedBufferSize, outputSharedBufferSize);
						
						FileInputStream fis = new FileInputStream(inputFile); 
						IoTools io = new IoTools(fis, 32000);
						VideoAssembler assembler = new VideoAssembler(io);
						OutputTools outputTools = new FileOutputTools(outputFile); 
						VideoDisassembler disassembler = new VideoDisassembler(outputTools);
				    	ParametersProcessment parameters = new ParametersProcessment(rate, 
				    			null, false);
				    	
						controller.setSizes(inputBufferSize,inputSharedBufferSize, outputSharedBufferSize);
						controller.config(assembler, disassembler, parameters);
						controller.run();

						
					
						
						startButton.setEnabled(false);
						stopButton.setEnabled(true);
						statusBar.setText("Executing ...");
						selectInputButton.setEnabled(false);
						selectOutputButton.setEnabled(false);
						inputFileField.setEnabled(false);
						outputFileField.setEnabled(false);
						
					} else if (inputFileField.getText().equals("")) {
						JOptionPane.showMessageDialog(
								null,
								"Choose an input file.",
								"Warning",
								JOptionPane.WARNING_MESSAGE);
					} else if (outputFileField.getText().equals("")) {
						JOptionPane.showMessageDialog(
								null,
								"Choose an output file.",
								"Warning",
								JOptionPane.WARNING_MESSAGE);
					}					
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					e.printStackTrace();
				}
			}
			/*
			 * Stop Button
			 */
			else if (event.getSource() == stopButton) {
				controller.stopProcessing();
				
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
				selectInputButton.setEnabled(true);
				selectOutputButton.setEnabled(true);
				inputFileField.setEnabled(true);
				outputFileField.setEnabled(true);
				statusBar.setText("Processing Stopped.");
			}
			/*
			 * Select Input Button
			 */
			else if (event.getSource() == selectInputButton) {
				JFileChooser fileChooser = new JFileChooser("E:\\Exemplos");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.CANCEL_OPTION)
					return;
				
				File fileName = fileChooser.getSelectedFile();
				
				if (fileName == null || fileName.getName().equals(""))
					JOptionPane.showMessageDialog(
							null,
							"Invalid File Name",
							"Invalid File Name",
							JOptionPane.ERROR_MESSAGE);
				else
					inputFileField.setText(fileName.toString());
			}
			/*
			 * Select Output Button
			 */
			else if (event.getSource() == selectOutputButton) {
				JFileChooser fileChooser = new JFileChooser("E:\\Exemplos");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				int result = fileChooser.showSaveDialog(null);
				if (result == JFileChooser.CANCEL_OPTION)
					return;
				
				File fileName = fileChooser.getSelectedFile();
				
				if (fileName == null || fileName.getName().equals(""))
					JOptionPane.showMessageDialog(
							null,
							"Invalid File Name",
							"Invalid File Name",
							JOptionPane.ERROR_MESSAGE);
				else
					outputFileField.setText(fileName.toString());
			}
			
		}
	}
	
	private class JSliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			int value = stretchSlider.getValue();
			stretchRateField.setText("" + value);
			//controller.setStretchRate((100 + value) / 100);
		}
	}
	
	public void updateGUI(long elapsedTime) {
		int iFrames, pFrames, bFrames;
		
		/*
		 * Piscar mensagem do statusBar. Primeira parte: apagar
		 */
		String msg = statusBar.getText();
		statusBar.setText("   ");
		
		/*
		 * Atualização do Input Panel
		 */
		stats = controller.getStats(Constants.INPUT);
		inputBitrateField.setText(formatNumber1.format(stats.getBitRate()));
		inputFrameRateField.setText(formatNumber1.format(stats.getFrameRate()));
		inputSequencesField.setText("" + stats.getSequenceCounter());
		inputGOPField.setText("" + stats.getGopCounter());
		iFrames = stats.getIPictureCounter();
		inputIFramesField.setText("" + iFrames);
		pFrames = stats.getPPictureCounter();
		inputPFramesField.setText("" + pFrames);
		bFrames = stats.getBPictureCounter();
		inputBFramesField.setText("" + bFrames);
		
		int inputTotal = iFrames + pFrames + bFrames;
		inputTotalField.setText("" + inputTotal);
		
		/*
		 * Atualização do Output Panel
		 */
		stats = controller.getStats(Constants.OUTPUT);
		outputBitrateField.setText(formatNumber1.format(stats.getBitRate()));
		outputFrameRateField.setText(
				formatNumber1.format(stats.getFrameRate()));
		outputSequencesField.setText("" + stats.getSequenceCounter());
		outputGOPField.setText("" + stats.getGopCounter());
		iFrames = stats.getIPictureCounter();
		outputIFramesField.setText("" + iFrames);
		pFrames = stats.getPPictureCounter();
		outputPFramesField.setText("" + pFrames);
		bFrames = stats.getBPictureCounter();
		outputBFramesField.setText("" + bFrames);
		
		int outputTotal = iFrames + pFrames + bFrames;
		outputTotalField.setText("" + outputTotal);
		
		/*
		 * Atualização do General Panel
		 */
		
		double generalStretchRate = controller.getActualStretchRate();
		generalStretchRateField.setText(
				formatNumber2.format(generalStretchRate));
		
		generalProcessingTimeField.setText(
				formatNumber3.format((double) elapsedTime / 1000));
		
		pictureProcessingRateField.setText(
				formatNumber3.format((double) inputTotal * 1000 / elapsedTime));
		
		/*
		 * Piscar mensagem no statusBar. Segunda parte: recolocar mensagem
		 */
		statusBar.setText(msg);
	}
	
	/**
	 * 
	 */
	public void finished() {
		stopButton.setEnabled(false);
		startButton.setEnabled(true);
		selectInputButton.setEnabled(true);
		selectOutputButton.setEnabled(true);
		inputFileField.setEnabled(true);
		outputFileField.setEnabled(true);
		statusBar.setText("Task Finished.");
	}
	
	/**
	 * @param l
	 */
	public void updateElapsedTime(long elapsedTime) {
		
		generalProcessingTimeField.setText(
				formatNumber3.format((double) elapsedTime / 1000));
	}
}