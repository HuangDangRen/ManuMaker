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

import java.io.BufferedWriter;
import java.io.File;
import java.util.Optional;

import gallery.abstractclasses.ControllerAbstr;
import gallery.abstractclasses.GalleryItem;
import gallery.model.GalleryChapter;
import gallery.model.GalleryProjectSettings;
import gallery.model.GalleryTitle;
import gallery.utils.ToolTipMaker;
import gallery.utils.TypeUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;

public class ChapterInputFormController extends ControllerAbstr {
		
	@FXML
	TitledPane TP_ChapterInputForm;
	@FXML
	Label LBL_SlideType;
	@FXML
	Button BT_SelectType;
	@FXML
	RadioButton RB_InsertBefore;
	@FXML
	RadioButton RB_InsertAfter;
	@FXML
	TextField TF_Title;
	@FXML
	TextField TF_Author;
	@FXML
	TextArea TA_Audience;
	@FXML
	TextField TF_AdditionalTitle;
	@FXML
	TextArea TA_AdditionalText;
	@FXML
	Button BT_OK;
	@FXML
	Button BT_Cancel;
	@FXML
	Accordion ACC_ChapterInput;
	
	private GalleryBrowserController owningController = null;
	private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	private Node oldChild = null;
	private boolean insertBefore = true;
	private GalleryItem newSlide = null;
	
	private boolean mandatoryFieldsFilled(){
		boolean ret = true;
		// this.TF_ProjectTitle.getText().matches("[^\\s-]")
		ret = ret && (!this.TF_Title.getText().isEmpty());
		ret = ret && (!this.TF_Author.getText().isEmpty());
		ret = ret && (!this.TA_Audience.getText().isEmpty());
		return ret;
	}
	
	private void disableInput(boolean state){
		this.ACC_ChapterInput.disableProperty().set(state);
//		this.TF_Title.disableProperty().set(state);
//		this.TF_Author.disableProperty().set(state);
//		this.TA_Audience.disableProperty().set(state);
//		this.TF_AdditionalTitle.disableProperty().set(state);
//		this.TA_AdditionalText.disableProperty().set(state);
		this.RB_InsertAfter.disableProperty().set(state);
		this.RB_InsertBefore.disableProperty().set(state);
		this.BT_OK.disableProperty().set(state);
	}
		
	/** 
	 * Sets the reference to the owning controller
	 * @param nOC
	 */
	public void setOwningController(GalleryBrowserController nOC) {
		try{
			this.owningController = nOC;
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		this.invar("setOwningController()@end of method");
 	}
	

	/**
	 * Make the ChapterInputFormController remember the GUI component it replaced
	 * @param nOC
	 */
	public void setOldChild(Node nOC) {
		this.oldChild = nOC;
	}
	
	/**
	 * Get the GUI component which the ChapterInputFormController temporarily replaced
	 * @return
	 */
	public Node getOldChild() {
		return this.oldChild;
	}
	
	@Override
	public void updateUI() {
		if(this.newSlide instanceof GalleryTitle) {
			this.LBL_SlideType.setText("Title Slide");
			this.disableInput(false);
			this.ACC_ChapterInput.disableProperty().set(true);
			this.RB_InsertAfter.disableProperty().set(true);
			this.RB_InsertBefore.disableProperty().set(true);
		} else if (this.newSlide instanceof GalleryChapter){
			this.LBL_SlideType.setText("Chapter Slide");
			this.disableInput(false);
		} else {
			this.LBL_SlideType.setText("Click button to select...");
			this.disableInput(true);
		}
	}

	@Override
	public void initCTRL() {
		this.newSlide = null;
		this.disableInput(true);
		this.updateUI();
		
		// Make tooltips: 
		this.TF_Title.setTooltip(ToolTipMaker.simpleToolTip(	"The title of a chapter should be short and concise. \n\n"
				+ 	"Examples: \"Safety\", \"Assembly\", \"Bearing Maintenance\" \n\n"
				+ 	"The chapter slide will automatically be completed with the term\n"
				+ 	"\"Chapter\" and a running number, so these should not be written \n"
				+ 	"in the title.\n"
				+ 	"This is a required field."));

		this.TF_Author.setTooltip(ToolTipMaker.simpleToolTip(	"Give the name of the author. This is a required field."));
		this.TA_Audience.setTooltip(ToolTipMaker.simpleToolTip(	"Name the target group(s) of people for whom this chapter\n"
															+ 	"is intended. \n"
															+ 	"Use job descriptions if possible, e.g.\n"
															+ 	"\"Service Technicians\", \"Cleaning Staff\"...\n"
															+ 	"This is a required field."));
		this.TF_AdditionalTitle.setTooltip(ToolTipMaker.simpleToolTip(	"These fields can be used to add extra information."));
		this.TA_AdditionalText.setTooltip(ToolTipMaker.simpleToolTip(	"These fields can be used to add extra information."));
		this.BT_SelectType.setTooltip(ToolTipMaker.simpleToolTip(	"Select whether to insert a Chapter-Slide \n"
																+ "(the form below is used to define the necessary information)\n"
																+ "or a Title-Slide, which gets automatically filled with data \n"
																+ "from the Project Settings. The data is updated everytime the\n"
																+ "Project Settings are changed."));
	}
	
	@FXML
	public void handleSelectType() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Choose Slide Type");
		alert.setHeaderText("Insert Chapter or Title Slide?");
		alert.setContentText("Please choose.");
	
		ButtonType buttonTitle = new ButtonType("Title Slide"); 
		ButtonType buttonChapter = new ButtonType("Chapter Slide");
		ButtonType buttonContinue = new ButtonType("Cancel");
		
		alert.getButtonTypes().setAll(buttonTitle, buttonChapter, buttonContinue);
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == buttonTitle) {
			this.newSlide = new GalleryTitle();
			System.out.println("\n\rCIFCTRL.handleSelectType() -> loading data from project settings!");
			((GalleryTitle)this.newSlide).loadDataFromProjectSettings();
			((GalleryTitle)this.newSlide).updateTemplate();
			this.disableInput(true);
			
		} else if(result.get() == buttonChapter) {
			this.newSlide = new GalleryChapter();
			this.disableInput(false);
			if(this.newSlide != null) {
				this.TF_Title.setText(((GalleryChapter)this.newSlide).getSimpleTitle());
				this.TF_Author.setText(((GalleryChapter)this.newSlide).getAuthor());
				this.TA_Audience.setText(((GalleryChapter)this.newSlide).getAudience());
				this.TF_AdditionalTitle.setText(((GalleryChapter)this.newSlide).getRef1());
				this.TA_AdditionalText.setText(((GalleryChapter)this.newSlide).getData1());
			}
			if(this.owningController.currentSlide.getClass().equals(GalleryTitle.class)){
		     	this.insertBefore = false;
	           	this.RB_InsertBefore.selectedProperty().set(false);
	           	this.RB_InsertAfter.selectedProperty().set(true);
			}
	
		} else {
			this.newSlide = null;
		}
		

		updateUI();
	}
	
	@FXML
	public void handleInsertBefore() {
		if(this.owningController.currentSlide.getClass().equals(GalleryTitle.class)){
		 	Alert error = new Alert(AlertType.ERROR);
           	error.setTitle("Cannot insert before title slide");
           	error.setHeaderText("Inserting before Title not possible");
           	error.setContentText( "You can only insert new slides AFTER the title slide.");
           	error.showAndWait(); 
           	this.RB_InsertBefore.selectedProperty().set(false);
           	this.RB_InsertAfter.selectedProperty().set(true);
			this.insertBefore = false;
		} else {
			this.RB_InsertAfter.selectedProperty().set(false);
			this.insertBefore = true;
		}
		
	}
	
	@FXML
	public void handleInsertAfter() {
		this.RB_InsertBefore.selectedProperty().set(false);
		this.insertBefore = false;		
	}
	
	
	
	
	@FXML
	public void handleTitleText() {
		if(this.newSlide != null && this.TF_Title.getText() != null) {
			String text = this.TF_Title.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				((GalleryChapter)this.newSlide).setSlideTitle(text);
			} else {
				((GalleryChapter)this.newSlide).setSlideTitle(text + "\n");
			}
			updateUI();
		}
		
	}
	
	@FXML
	public void handleAuthorText() {
		if(this.newSlide != null && this.TF_Author.getText() != null) {
			String text = this.TF_Author.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				((GalleryChapter)this.newSlide).setAuthor(text);
			} else {
				((GalleryChapter)this.newSlide).setAuthor(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleAudienceText() {
		if(this.newSlide != null && this.TA_Audience.getText() != null) {
			String text = new String(this.TA_Audience.getText());
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				((GalleryChapter)this.newSlide).setAudience(text);
			} else {
				((GalleryChapter)this.newSlide).setAudience(text + "\n");
			}

			updateUI();
		}
	}
	
	@FXML
	public void handleAdditionalTitle() {
		if(this.newSlide != null && this.TF_AdditionalTitle.getText() != null) {
			String text = this.TF_AdditionalTitle.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				((GalleryChapter)this.newSlide).setRef1(text);
			} else {
				((GalleryChapter)this.newSlide).setRef1(text + "\n");
			}
			updateUI();
		}
	}
	
	@FXML
	public void handleAdditionalText() {
		if(this.newSlide != null && this.TA_AdditionalText.getText() != null) {
			String text = this.TA_AdditionalText.getText();
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				((GalleryChapter)this.newSlide).setData1(text);
			} else {
				((GalleryChapter)this.newSlide).setData1(text + "\n");
			}
			updateUI();
		}
	}	
	
	@FXML
	public void handleOK() {
		if(		(this.newSlide.getClass().equals(GalleryTitle.class)) 
				|| 
				(this.newSlide.getClass().equals(GalleryChapter.class) && this.mandatoryFieldsFilled() == true)) {
			File projectPath = new File(this.GPSSingleton.getSavePath() + "\\Resources\\");
			BufferedWriter textWriter = null;
		    if (!projectPath.exists()) {
		        if (projectPath.mkdirs()) {
		            System.out.println("sub directories created successfully");
		        } else {
		            System.out.println("failed to create sub directories");
		        }
		    }
		    if(this.newSlide.getClass().equals(GalleryChapter.class)){
			    ((GalleryChapter)this.newSlide).setAudience(TypeUtils.newlinesToHtml(((GalleryChapter)this.newSlide).getAudience()));
			    ((GalleryChapter)this.newSlide).setAuthor(TypeUtils.newlinesToHtml(((GalleryChapter)this.newSlide).getAuthor()));
			    ((GalleryChapter)this.newSlide).setData1(TypeUtils.newlinesToHtml(((GalleryChapter)this.newSlide).getData1()));
			    ((GalleryChapter)this.newSlide).setRef1(TypeUtils.newlinesToHtml(((GalleryChapter)this.newSlide).getRef1()));
		    }

			this.owningController.chapterImportFormControllerDone(this.newSlide, this.insertBefore);
		} else {
		 	Alert error = new Alert(AlertType.ERROR);
           	error.setTitle("Mandatory fields not filled!");
           	error.setHeaderText("One or more mandatory fields are not filled out!");
           	error.setContentText( "Please fill out all text fields labeled in bold.");
           	error.showAndWait();
		}
	}
	
	@FXML
	public void handleCancel() {
		this.owningController.chapterImportFormControllerDone(null, this.insertBefore);
	}


	@Override
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		check = check & (this.owningController != null);
		
		if(check == false){
			System.out.println("\n\rChapterInputFormController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
	
	
}
