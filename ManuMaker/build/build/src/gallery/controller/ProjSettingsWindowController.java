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

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import gallery.abstractclasses.ControllerAbstr;
import gallery.abstractclasses.GalleryItem;
import gallery.model.GalleryProjectSettings;
import gallery.model.GalleryTitle;


public class ProjSettingsWindowController extends ControllerAbstr {
	// Reference to the main application
	private GalleryProjectSettings PSSingleton = GalleryProjectSettings.INSTANCE;
	private Stage dialogStage;
	private boolean savedBeforeClosing = false;
	
	@FXML
	private TextField TF_ProjectTitle;
	@FXML
	private TextField TF_ProductName;
	@FXML
	private TextField TF_ProductVersion;
	@FXML
	private TextField TF_DocType;
	@FXML
	private TextField TF_DocRevNumber;
	@FXML
	private TextField TF_Audience;
	@FXML
	private TextField TF_Author;
	@FXML
	private TextField TF_CompanyName;
	@FXML
	private TextArea TA_CompanyAddress;
	@FXML
	private TextArea TA_ContactInfo;
	@FXML
	private Label LBL_LogoPath;
	@FXML
	private Button BT_LoadLogo;
	@FXML
	private Button BT_DeleteLogo;
	@FXML
	private TextField TF_AdditionalTitle;
	@FXML
	private TextArea TA_AdditionalData;
	@FXML
	private TextField TF_ProjectPath;
	@FXML
	private Button BT_ProjectPath;
	@FXML
	private CheckBox CB_ReqTitle;
	@FXML
	private CheckBox CB_ReqInstr;
	@FXML
	private Button BT_Save;
	@FXML
	private ImageView IV_Logo;



	private GalleryBrowserController galleryBrowserCTRL = null;
	
	private boolean mandatoryFieldsFilled(){
		boolean ret = true;
		// this.TF_ProjectTitle.getText().matches("[^\\s-]")
		ret = ret && (!this.TF_ProjectTitle.getText().isEmpty());
		ret = ret && (!this.TF_ProductName.getText().isEmpty());
		ret = ret && (!this.TF_DocType.getText().isEmpty());
		ret = ret && (!this.TF_Author.getText().isEmpty());		
		ret = ret && (!this.TF_ProjectPath.getText().isEmpty());	
		return ret;
	}
	
	public void clearForm(){
		this.TF_ProjectTitle.setText(null);
		this.TF_ProductName.setText(null);
		this.TF_ProductVersion.setText(null);
		this.TF_DocType.setText(null);
		this.TF_DocRevNumber.setText(null);
		this.TF_Audience.setText(null);
		this.TF_Author.setText(null);
		this.TF_CompanyName.setText(null);
		this.TA_CompanyAddress.setText(null);
		this.TA_ContactInfo.setText(null);
		this.LBL_LogoPath.setText(null);
		this.TF_AdditionalTitle.setText(null);
		this.TA_AdditionalData.setText(null);
		this.TF_ProjectPath.setText(null);
		this.IV_Logo.setImage(null);
				
		this.CB_ReqInstr.selectedProperty().set(false);
		this.CB_ReqTitle.selectedProperty().set(false);
//		this.savedBeforeClosing = false;
	}
	public boolean savedBeforeClosing(){
		return this.savedBeforeClosing;
	}
	/**
	 * The Application settings use their own stage.
	 *
	 */
	public void setStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	/**
	 * Initializes the controller
	 */
	@Override
	public void initCTRL() {
		this.dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
	    	@Override
	    	public void handle(WindowEvent e2){
	    		System.out.println("\n\rEvent Type on ProjectSettingsWindow.closed() -> " + e2.getEventType().toString());
	    		ProjSettingsWindowController.this.savedBeforeClosing = false;
	    		e2.consume();
	    		dialogStage.close();
	    	}
		});
		
//		Dimension dim = this.AppSettingsSingleton.getCamResolution();
//		this.camList = this.mainApp.activeGallery.camera.getWebcamNames();
//		this.thumbNails = this.mainApp.activeGallery.camera.getThumbnails();
		}
	
	/**
	 * Updates the UI 
	 */
	@Override
	public void updateUI( ) {
//		Dimension dim = this.AppSettingsSingleton.getCamResolution();
//		this.mainApp.activeGallery.camera.selectCam(this.AppSettingsSingleton.getCamName());

		
		
		this.savedBeforeClosing = false;
	}
	
	public void loadFields(){
		this.TF_ProjectTitle.setText(this.PSSingleton.getProjTitle());
		this.TF_ProductName.setText(this.PSSingleton.getProductName());
		this.TF_ProductVersion.setText(this.PSSingleton.getProductVersion());
		this.TF_DocType.setText(this.PSSingleton.getDocType());
		this.TF_DocRevNumber.setText(this.PSSingleton.getDocRevision());
		this.TF_Audience.setText(this.PSSingleton.getAudience());
		this.TF_Author.setText(this.PSSingleton.getAuthor());
		this.TF_CompanyName.setText(this.PSSingleton.getCompanyName());
		this.TA_CompanyAddress.setText(this.PSSingleton.getCompanyAddress());
		this.TA_ContactInfo.setText(this.PSSingleton.getContact());
		if(this.PSSingleton.getLogoPic() != null &&	new File(this.PSSingleton.getSavePath() + "\\Resources\\Logo.png").exists()){			
			this.LBL_LogoPath.setText(this.PSSingleton.getSavePath() + "\\Resources\\Logo.png");
		} else {
			this.LBL_LogoPath.setText("No logo selected");
		}
		this.TF_AdditionalTitle.setText(this.PSSingleton.getRef1());
		this.TA_AdditionalData.setText(this.PSSingleton.getData1());
		this.TF_ProjectPath.setText(this.PSSingleton.getSavePath());
		this.IV_Logo.setImage(this.PSSingleton.getLogoPic());
		
		this.CB_ReqInstr.selectedProperty().set(this.PSSingleton.getReqInstr());
		this.CB_ReqTitle.selectedProperty().set(this.PSSingleton.getReqTitle());
		this.savedBeforeClosing = false;
	}

	@FXML
	public void handleProjectTitleText() {
		if(this.TF_ProjectTitle.getText() != null) {
			String text = this.TF_ProjectTitle.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setProjTitle(text);
			} else {
				this.PSSingleton.setProjTitle(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleProductNameText() {
		if(this.TF_ProductName.getText() != null) {
			String text = this.TF_ProductName.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setProductName(text);
			} else {
				this.PSSingleton.setProductName(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleProductVersionText() {
		if(this.TF_ProductVersion.getText() != null) {
			String text = this.TF_ProductVersion.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setProductVersion(text);
			} else {
				this.PSSingleton.setProductVersion(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleDocTypeText() {
		if(this.TF_DocType.getText() != null) {
			String text = this.TF_DocType.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setDocType(text);
			} else {
				this.PSSingleton.setDocType(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleRevNumberText() {
		if(this.TF_DocRevNumber.getText() != null) {
			String text = this.TF_DocRevNumber.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setDocRevision(text);
			} else {
				this.PSSingleton.setDocRevision(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleAudienceText() {
		if(this.TF_Audience.getText() != null) {
			String text = this.TF_Audience.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setAudience(text);
			} else {
				this.PSSingleton.setAudience(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleAuthorText() {
		if(this.TF_Author.getText() != null) {
			String text = this.TF_Author.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setAuthor(text);
			} else {
				this.PSSingleton.setAuthor(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleCompanyNameText() {
		if(this.TF_CompanyName.getText() != null) {
			String text = this.TF_CompanyName.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setCompanyName(text);
			} else {
				this.PSSingleton.setCompanyName(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleCompanyAddressText() {
		if(this.TA_CompanyAddress.getText() != null) {
			String text = this.TA_CompanyAddress.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setCompanyAddress(text);
			} else {
				this.PSSingleton.setCompanyAddress(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleContactInfoText() {
		if(this.TA_ContactInfo.getText() != null) {
			String text = this.TA_ContactInfo.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setContact(text);
			} else {
				this.PSSingleton.setContact(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleLoadLogo() {
		FileChooser chooser = new FileChooser();
		File file = null;
		BufferedImage logo; 
		chooser.setTitle("Choose Logo image file...");
		if(this.PSSingleton.getDefaultSavePath() == this.PSSingleton.getSavePath()) {
			file = new File(this.PSSingleton.getDefaultSavePath());
		    if (!file.exists()) {
		       file = new File(System.getProperty("user.home"));
		    }
		} else {
			file = new File(this.PSSingleton.getSavePath());
		    if (!file.exists()) {
			       file = new File(System.getProperty("user.home"));
			 }
		}
		chooser.setInitialDirectory(file);
		File selectedFile = chooser.showOpenDialog(this.dialogStage);
		
		if(selectedFile != null) {
			File stdLogo = new File(this.PSSingleton.getSavePath() + "\\Resources\\Logo.png");
			if(selectedFile.exists()){
				if(stdLogo.exists()){
					this.handleDeleteLogo();
				} 
				file = new File(selectedFile.getAbsolutePath());
				try {
					logo = ImageIO.read(file);
					this.PSSingleton.setLogoPic(null);
					this.PSSingleton.setLogoPic(SwingFXUtils.toFXImage(logo, null));
					this.IV_Logo.setImage(this.PSSingleton.getLogoPic());
					this.LBL_LogoPath.setText(selectedFile.getAbsolutePath());
					GalleryItem first = this.galleryBrowserCTRL.activeGallery.getGallerySlides().get(0);
					if(first.getClass().equals(GalleryTitle.class)){
						((GalleryTitle)first).loadDataFromProjectSettings();
						this.galleryBrowserCTRL.updateCurrentSlide();
					}
				} catch (IOException e) {
			    	Alert alert = new Alert(AlertType.INFORMATION);
			    	alert.setTitle("Logo file not found.!");
			    	alert.setHeaderText("Logo file not found.");
			    	alert.setContentText(	"Please manually place the logo image-file at: \n"
			    					+ 		"<project-root>/Resources/Logo.png  \n\r"
			    					+ 		"or load it from the file system via the \n"
			    					+ 		"\"Project Settings\" window.");
			    	alert.showAndWait();
					e.printStackTrace();
				}
			}
	
			
		} else {
			if(this.galleryBrowserCTRL.activeGallery.getSlideCount() == 0){
				this.mainApp.showStartupWindow();
			}
		}
	}
	
	@FXML
	public void handleDeleteLogo() {
		if(this.PSSingleton.getLogoPic() != null){
			Alert conf = new Alert(AlertType.CONFIRMATION);
			conf.setTitle("Confirm: Delete the chosen logo?");
			conf.setHeaderText(conf.getTitle());
			conf.setContentText("This action cannot be reversed. \n\rPlease confirm:");
			
			ButtonType BTT_OK = new ButtonType("OK");
			ButtonType BTT_Cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			
			conf.getButtonTypes().setAll(BTT_OK, BTT_Cancel);
			
			Optional<ButtonType> res = conf.showAndWait();
			if(res.get() == BTT_OK) {
				this.IV_Logo.setImage(null);
				this.PSSingleton.setLogoPic(null);
				GalleryItem first = this.galleryBrowserCTRL.activeGallery.getGallerySlides().get(0);
				if(first.getClass().equals(GalleryTitle.class)){
					((GalleryTitle)first).loadDataFromProjectSettings();
				}
				this.PSSingleton.deleteLogoPic();
			
			} else {
				//NOP
			}
		}	
	}
	
	
	@FXML
	public void handleAdditionalTitleText(){
		if(this.TF_AdditionalTitle.getText() != null) {
			String text = this.TF_AdditionalTitle.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setRef1(text);
			} else {
				this.PSSingleton.setRef1(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleAdditionalDataText(){
		if(this.TA_AdditionalData.getText() != null) {
			String text = this.TA_AdditionalData.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.PSSingleton.setData1(text);
			} else {
				this.PSSingleton.setData1(text + "\n");
			}
			
			updateUI();
		}
	}
	
	
	@FXML
	public void handleReqTitle() {
		this.PSSingleton.setReqTitle(this.CB_ReqTitle.selectedProperty().getValue());
	}
	
	@FXML
	public void handleReqInstr() {
		this.PSSingleton.setReqInstr(this.CB_ReqInstr.selectedProperty().getValue());
	}
	
	
	@FXML
	public void handleProjectPath() {
		DirectoryChooser chooser = new DirectoryChooser();
		File directory = null;
		chooser.setTitle("Choose Project Directory...");
		directory = new File(this.PSSingleton.getSavePath());
		chooser.setInitialDirectory(directory);
		File selectedDirectory = chooser.showDialog(this.dialogStage);
//		if(this.galleryBrowserCTRL.activeGallery.GPSSingleton.getDefaultSavePath() == this.galleryBrowserCTRL.activeGallery.GPSSingleton.getSavePath()) {
//			directory = new File(this.galleryBrowserCTRL.activeGallery.GPSSingleton.getDefaultSavePath());
//		    if (!directory.exists()) {
//		        if (directory.mkdirs()) {
//		            System.out.println("sub directories created successfully");
//		        } else {
//		            System.out.println("failed to create sub directories");
//		        }
//		    }
//		} else {
		
//		}
		
		if(!(selectedDirectory == null)) {
			this.TF_ProjectPath.setText(selectedDirectory.getAbsolutePath()+"\\"+this.PSSingleton.getProjTitle());
		}
	}
	
	/** 
	 * Saves the settings
	 */
	@FXML
	public void handleSave() {
		boolean save = true;
		if(mandatoryFieldsFilled()) {
			File directory = new File(this.TF_ProjectPath.getText());
		    if (!directory.exists()) {
		    	Alert createPath = new Alert(AlertType.CONFIRMATION);
		    	createPath.setTitle("Create specified project path?");
		    	createPath.setHeaderText("Please Confirm:");
		    	createPath.setContentText("The specified path could not be found,\ncreate project folders as specified?");
		    		    	
				ButtonType BTT_OK = new ButtonType("OK");
				ButtonType BTT_Cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
				
				createPath.getButtonTypes().setAll(BTT_OK, BTT_Cancel);
				
				Optional<ButtonType> res = createPath.showAndWait();
				if(res.get() == BTT_OK) {
					if(directory.mkdirs()){
						save = true;
			        } else {
			           	Alert error = new Alert(AlertType.ERROR);
			           	error.setTitle("ERROR: Directory could not be created.");
			           	error.setHeaderText("Could not create directories...");
			           	error.setContentText( "It is possible the application does not \n"
			           						+ "have sufficient privileges to write in the\n"
			           						+ "specified location.\n\r"
			           						+ "Please select a different project directory.");
			           	error.showAndWait();
			           	save = false;
			        }
				} else {
					save = false;
				}
			
		    }
		  
		    if(directory.canWrite() && save == true) {
	    		if( this.galleryBrowserCTRL != null && 
	    				this.galleryBrowserCTRL.activeGallery != null && 
	    				this.galleryBrowserCTRL.activeGallery.getGallerySlides().size() > 0){
	    				this.PSSingleton.setSavePath(directory.getPath());
	    				this.PSSingleton.saveSettings();
	    				GalleryItem gi = this.galleryBrowserCTRL.activeGallery.getGallerySlides().get(0);
	    				if(gi.getClass().equals(GalleryTitle.class)){
	    					((GalleryTitle)gi).loadDataFromProjectSettings();
	    					((GalleryTitle)gi).updateTemplate();
	    					this.galleryBrowserCTRL.activeGallery.getGallerySlides().set(0,gi);
	    					this.galleryBrowserCTRL.updateCurrentSlide();
	    					this.galleryBrowserCTRL.updateUI();
	    				}
	    			}
	    		this.savedBeforeClosing = true;
	    		this.dialogStage.close();
	        }
		} else {
		 	Alert error = new Alert(AlertType.ERROR);
           	error.setTitle("Mandatory fields not filled!");
           	error.setHeaderText("One or more mandatory fields are not filled out!");
           	error.setContentText( "Please fill out all text fields labeled in bold.");
           	error.showAndWait();
           	save = false;
		}
		
		this.clearForm();
	}

	
	
	/**
	 * Closes the settings window without saving changes (prompts user to discard changes)
	 * 
	 */
	@FXML
	public void handleCancel() {
		this.savedBeforeClosing = false;
		if(this.PSSingleton.getSlideCount() == 0){
			this.PSSingleton.init();
		}
		this.clearForm();
		this.dialogStage.close();
	}

	@Override
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		
		if(check == false){
			System.out.println("\n\rProjSettingsWindowController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}

	public void setGalleryBrowserCTRL(GalleryBrowserController nGBC) {
		this.galleryBrowserCTRL = nGBC; 
	}
	
}
