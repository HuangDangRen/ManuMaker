/**
 *  ManuMaker V1.0 is an open-source tool for creating step-by-step manuals.   
 *  Copyright (C) 2016 Daniel M. Lachmann
 *  
 *  To contact me via email, write to: ManuMaker.Dev@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gallery.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import gallery.abstractclasses.ControllerAbstr;
import gallery.model.GalleryProjectSettings;
import gallery.utils.PathUtils;
import gallery.utils.ToolTipMaker;
import gallery.utils.TypeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WarningLabelMakerController extends ControllerAbstr{

    @FXML
    private TitledPane TP_WarningLabelMaker;
    
    @FXML
    private TitledPane TP_Consequences;
    
    @FXML
    private TitledPane TP_AvoidDanger;
   
    @FXML
    private ImageView IV_Pictogram;

    @FXML
    private Button BT_SelectPictogram;

    @FXML
    private Label LBL_Path;

    @FXML
    private TextField TF_SignalWord;

    @FXML
    private ComboBox<String> CB_SignalWord = new ComboBox<String>();

    @FXML
    private TextArea TA_SourceOfDanger;

    @FXML
    private TextArea TA_Consequences;

    @FXML
    private TextArea TA_AvoidTheDanger;

    @FXML
    private RadioButton RB_Warning;
    
    @FXML
    private RadioButton RB_Notice;
    
    @FXML
    private CheckBox CB_ShowInSafetyChapter;

    @FXML
    private Button BT_OK;

    @FXML
    private Button BT_Cancel;
    
    private GalleryBrowserController owningController = null;
    private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private ObservableList<String> signalWords = FXCollections.observableArrayList();
    private String warningData = null;
    private String noticeTMP = null;
    private String warningTMP = null;
    private Stage  dialogStage;
    private Node oldChild = null;
    private Image pictogram = null;
    private String notice = null;
    private String consequence = null;
    private String avoid = null;
    private File labelSaveDir = null;
    private File selectedPic = null;
    
    
	private boolean mandatoryFieldsFilled(){
		boolean ret = true;
		// this.TF_ProjectTitle.getText().matches("[^\\s-]"
		if(this.RB_Warning.selectedProperty().get()){
			if(this.pictogram != null && this.notice != null && this.consequence != null && this.avoid != null){
				ret = ret && (!this.CB_SignalWord.getValue().isEmpty());
				ret = ret && (!this.notice.isEmpty());
				ret = ret && (!this.consequence.isEmpty());
				ret = ret && (!this.avoid.isEmpty());
			} else {
				ret = false;
			}
		} else if (this.RB_Notice.selectedProperty().get()){
			if(this.pictogram != null && this.notice != null){
				ret = ret && (!this.CB_SignalWord.getValue().isEmpty());
				ret = ret && (!this.notice.isEmpty());
			} else {
				ret = false;
			}
		} else {
			ret = false;
		}
	

		return ret;
	}
    
	/**
	 * Make the WarningLabelMakerController remember the GUI component it replaced
	 * @param nOC
	 */
	public void setOldChild(Node nOC) {
		this.oldChild = nOC;
	}
	
	/**
	 * Get the GUI component which the WarningLabelMakerController temporarily replaced
	 * @return
	 */
	public Node getOldChild() {
		return this.oldChild;
	}
    
    public void setOwningController(GalleryBrowserController nGBC){
    	this.owningController = nGBC;
    }
    
    

    @FXML
    public void handleWarning() {
    	this.RB_Notice.selectedProperty().set(false);
    	this.RB_Warning.selectedProperty().set(true);
    	this.TP_AvoidDanger.disableProperty().set(false);
    	this.TP_Consequences.disableProperty().set(false);
		
    	this.warningData = this.warningTMP;
    }
    
    @FXML
    public void handleNotice() {
    	this.RB_Warning.selectedProperty().set(false);
    	this.RB_Notice.selectedProperty().set(true);
    	this.TP_AvoidDanger.disableProperty().set(true);
    	this.TP_Consequences.disableProperty().set(true);
     
		this.warningData = this.noticeTMP;
    }
    
    @FXML
    public void handleSelectPictogram() {
		FileChooser chooser = new FileChooser();
		File file = null;
		
		chooser.setTitle("Choose a HTML Table:");

		file = new File("resources\\");//System.getProperty("user.home"));
		    
		chooser.setInitialDirectory(file);
		List<String> extensions = new ArrayList<String>();
		extensions.add(".png");
		extensions.add(".jpg");
		extensions.add(".bmp");
		extensions.add(".jpeg");
		extensions.add(".gif");
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".png", extensions);
		chooser.setSelectedExtensionFilter(extFilter);
		selectedPic = chooser.showOpenDialog(this.dialogStage);
		
		if(selectedPic != null && selectedPic.exists()) {
			try {
				this.pictogram = SwingFXUtils.toFXImage(ImageIO.read(selectedPic), null);
				this.IV_Pictogram.setImage(this.pictogram);
				this.LBL_Path.setText(selectedPic.getAbsolutePath());
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR: Pictogram could not be loaded!");
				alert.setHeaderText("ERROR!");
				alert.setContentText("The selected file could not be loaded.");
				alert.showAndWait();
				e.printStackTrace();
			}
		} else {
			//NOP
		}
    }

    @FXML
    public void handleSignalWordText() {

    }
    
    
    @FXML
	public void handleSODText() {
		if(this.TA_SourceOfDanger.getText() != null) {
			String text = this.TA_SourceOfDanger.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.notice = text;
			} else {
				this.notice = text + "\n";
			}
			updateUI();
		}
	}

	@FXML
	public void handleConsequencesText() {
		if(this.TA_Consequences.getText() != null) {
			String text = this.TA_Consequences.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.consequence = text;
			} else {
				this.consequence = text + "\n";
			}
			updateUI();
		}
	}

	@FXML
	public void handleAvoidDangerText() {
		if(this.TA_AvoidTheDanger.getText() != null) {
			String text = this.TA_AvoidTheDanger.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.avoid = text;
			} else {
				this.avoid = text + "\n";
			}
			updateUI();
		}
	}

	@FXML
    public void handleOK() {
		if(this.mandatoryFieldsFilled()){
			boolean saved = true;
			boolean error = false;
			String warning = "";
			String imageLink = "<img style=\"display:block;\" src=\"plpictogrampathpl/plpictogrampl\" alt=\"image not found\" width=\"40px\">";
			this.labelSaveDir = new File(this.GPSSingleton.getSafetyDataPath());
			// 1. Save image:
			
			if(!this.labelSaveDir.exists()){							//->make dirs if not existing: 
				if(this.labelSaveDir.mkdirs()){
					System.out.println("\n\rWLMCTRL.handleOK -> Directories created successfully.");
				} else {
					error = true;
					System.out.println("\n\rWLMCTRL.handleOK -> Could not create directory. Aborting.");
				}
			}
			if(error == false){
	
				String filename = Paths.get(this.selectedPic.toURI()).getFileName().toString();
				File pictogramSaveFile = new File(this.labelSaveDir.toString() + "\\" + filename);
				if(!pictogramSaveFile.exists()){
					saved = PathUtils.saveImagePng(labelSaveDir, this.pictogram, filename);
				}
				
				// 2. Fill Template: 
				warning = this.warningData.replace("plpictpl", imageLink.replace("plpictogrampl", filename));

				warning = warning.replace("plsigwordpl", this.CB_SignalWord.getValue());
				
				if(this.RB_Notice.selectedProperty().get()) { 	// Notice template selected
					warning = warning.replace("plnoticepl", this.notice);				
				} else {
					warning = warning.replace("plcausepl", this.notice);
					warning = warning.replace("plconsequencespl", this.consequence);
					warning = warning.replace("plavoidpl", this.avoid);
				}
				
				
				Map<DataFormat, Object> cb = new HashMap<DataFormat, Object>();
				cb.put(DataFormat.HTML, warning);
				this.clipboard.setContent(cb);

				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning Label successfully created");
				alert.setHeaderText("Warning Label prepared!");
				alert.setContentText(	"Please insert the label into the annotation text\n"
									+ 	"by right-clicking at the desired location and \n"
									+ 	"choosing \"Paste\"");
				alert.showAndWait();
		    	this.selectedPic = null;
		    	this.pictogram = null;
		    	this.IV_Pictogram.setImage(null);
		    	this.LBL_Path.setText(null);
		    	this.CB_SignalWord.setValue("");
		    	this.TF_SignalWord.setText(null);
		    	this.notice = null;
		    	this.TA_SourceOfDanger.setText(null);
		    	this.consequence = null;
		    	this.TA_Consequences.setText(null);
		    	this.avoid = null;
		    	this.TA_AvoidTheDanger.setText(null);
				this.owningController.warningLabelMakerControllerDone();
		
		    	
			}
						
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Form not filled out!");
			alert.setHeaderText("Some fields are empty!");
			alert.setContentText(	"Please fill all the fields in this form \n"
								+ 	"and load a pictogram file.");
			alert.showAndWait();
		}
    }

    @FXML
    public void handleCancel() {
    	this.selectedPic = null;
    	this.pictogram = null;
    	this.IV_Pictogram.setImage(null);
    	this.LBL_Path.setText(null);
    	this.CB_SignalWord.setValue("");
    	this.TF_SignalWord.setText(null);
    	this.notice = null;
    	this.TA_SourceOfDanger.setText(null);
    	this.consequence = null;
    	this.TA_Consequences.setText(null);
    	this.avoid = null;
    	this.TA_AvoidTheDanger.setText(null);
    	this.owningController.warningLabelMakerControllerDone();
    }
    
    
    public void primeWLM(){

    }
    
	@Override
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		check = check & (this.owningController != null);
		
		if(check == false){
			System.out.println("\n\rWarningLabelMakerController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}

	/**
	 * Does nothing here...
	 */
	@Override
	public void updateUI() {

		
	}
	
	@Override
	public void initCTRL() {
		
		this.RB_Notice.setTooltip(ToolTipMaker.simpleToolTip(	"Load the template for inserting a notice. \n"
				+ "A Notice contains: \n"
				+ "-> a Pictogram\n"
				+ "-> a Signal Word (e.g.\"NOTICE\") \n"
				+ "-> a text-paragraph which explains the situation in question to the reader."));
		
		this.RB_Warning.setTooltip(ToolTipMaker.simpleToolTip(	"Load the template for inserting a warning. \n"
				+ "A Warning contains: \n"
				+ "-> a Pictogram\n"
				+ "-> a Signal Word (e.g.\"DANGER\") \n"
				+ "-> a text-paragraph which outlines the source of the danger, e.g. \n"
				+ "	 \"Hot machine parts!\" \n"
				+ "-> a text-paragraph which outlines the possible (worst-case) consequences, e.g. \n"
				+ "	 \"May cause severe burns or death on contact.\" \n"
				+ "-> a text-paragraph which outlines how to avoid the danger, e.g. \n"
				+ "  \"Wear heat-resistant and insulated clothing when operating this machine.\n"
				+ "    Do not touch hot surfaces without protective clothing."));
		
		
		
		this.TA_AvoidTheDanger.setTooltip(ToolTipMaker.simpleToolTip("Short and concise paragraph on how to avoid the adverse effects stated above.\n"
				+ "Give clear instructions in command form. \n"
				+ "Do not use imprecise modal verbs, such as \"could\", \"may\", \"might\"."));
		
		this.TA_Consequences.setTooltip(ToolTipMaker.simpleToolTip("Short and concise summary of the worst-case consequences of exposure to above hazard.\n"
				+ "Use blunt formulations, e.g. \"severe burns\" \"loss of life\". \n"
				+ "Do not use colourful formulations.\n"
				+ "Do not use imprecise modal verbs, such as \"could\", \"may\", \"might\"."));
		
		this.TA_SourceOfDanger.setTooltip(ToolTipMaker.simpleToolTip("Short and concise description of the cause for this warning, e.g.\n"
				+ "\"hot surfaces\", \"rotating blades\". \n"
				+ "Use direct formulations, e.g. \"Exhaust pipes <b>are</b> hot during operation of the vehicle.\"\n"
				+ "Do not use imprecise modal verbs, such as \"could\", \"may\", \"might\"."));
		
		this.CB_SignalWord.setTooltip(ToolTipMaker.simpleToolTip("Select a signal word to headline the label.\n"
				+ "Depending on the country, there may be different requirements. \n"
				+ "The default ones for English and German can be selected \n"
				+ "from the drop-down menu on the right, or a custom one can be specified."));
		
		this.BT_SelectPictogram.setTooltip(ToolTipMaker.simpleToolTip("Select an image file for the pictogram. \n"
				+ "A selection of EN ISO 7010 pictograms was bundled with this application \n"
				+ "and can be found in the install-directory/resources/pictograms. \n\n"
				+ "Any .png files can be loaded."));
		
		List<String>  tmp = null;
		try {
			tmp = Files.readAllLines(Paths.get("resources/default_values/signalwords.txt"),Charset.forName("UTF8"));

		} catch (Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("ERROR! Could not load default signal words.");
			error.setHeaderText("Signal words file not found!");
			error.setContentText(	"Check if the file WL_SignalWords.txt is located in \n"
								+ 	"application directory/resources/default_values/....");
			error.showAndWait();
			e.printStackTrace();
		}		
		
		ObservableList<String> sigwrds = FXCollections.observableArrayList(tmp);
		
		this.CB_SignalWord.setItems(sigwrds);
		this.CB_SignalWord.getSelectionModel().select(0);
		this.CB_SignalWord.setEditable(true);
		
	
		

		try {
			this.noticeTMP = TypeUtils.makeListToString(Files.readAllLines(Paths.get("resources/html_templates/NoticeLabel_A.html"),Charset.forName("UTF8")));
			this.warningTMP = TypeUtils.makeListToString(Files.readAllLines(Paths.get("resources/html_templates/WarningLabel_A.html"),Charset.forName("UTF8")));		
		} catch (IOException e) {
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("ERROR: Templates for warning labels could not be loaded!");
	    	alert.setHeaderText("Warning label templates not loaded!");
	    	alert.setContentText("Please ensure the templates are available in the application directory.");
	    	alert.showAndWait();
			e.printStackTrace();
		}
		
		
		this.LBL_Path.setText("Please choose a pictogram from filesystem.");
		this.warningData = this.warningTMP;
		
//		this.CB_SignalWord.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
//
//			@Override
//			public void changed(ObservableValue<? extends String> item, String arg1, String arg2) {
//				t			
//			}
//			
//		});
		
		
		
	}
}



	