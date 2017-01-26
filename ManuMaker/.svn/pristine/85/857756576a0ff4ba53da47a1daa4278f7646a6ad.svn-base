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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.sarxos.webcam.WebcamLockException;

import gallery.abstractclasses.CamAbstr;
import gallery.abstractclasses.ControllerAbstr;
import gallery.abstractclasses.GalleryItem;
import gallery.camera.SarxosCam;
import gallery.model.Gallery;
import gallery.model.GalleryChapter;
import gallery.model.GalleryProjectSettings;
import gallery.model.GallerySafetySlide;
import gallery.model.GallerySlide;
import gallery.model.GalleryTitle;
import gallery.model.ScreenCaptureTool;
import gallery.model.SlideInstructionalPic;
import gallery.model.SlidePic;
import gallery.model.SlideStopMotion;
import gallery.utils.ToolTipMaker;
import gallery.utils.TypeUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class GalleryBrowserController extends ControllerAbstr{

	@FXML
	TabPane TP_GalleryBrowser;
	@FXML
	Tab TAB_Draft;
	@FXML
	TitledPane TP_Slide_Draft;
	@FXML
	AnchorPane AP_SlideList_Draft;
	@FXML
	AnchorPane AP_IVBox_Draft;
	@FXML
	TextArea TA_Annotation_Draft;
	@FXML
	AnchorPane	AP_TagUtils_Draft;
	@FXML
	AnchorPane AP_MediaPlayer_Draft;
	@FXML
	CheckBox CB_AutoAdd_Draft;
	@FXML
	Label LBL_CurrentIndex_Draft;
	@FXML
	Button BT_BackEdit_Draft;
	@FXML
	Button BT_RemoveComponent_Draft;
	@FXML
	Button BT_InsertSlide_Draft;
	@FXML
	Button BT_RecStopMotion_Draft;
	@FXML
	Button BT_RecordAudio_Draft;
	@FXML
	Button BT_OpenImgEditor_Draft;
	@FXML
	Button BT_SnapShot_Draft; 
	@FXML
	Button BT_PasteClipboard_Draft;
	@FXML
	Button BT_ForwardEdit_Draft;
	@FXML
	Tab TAB_Refine;
	@FXML
	TitledPane TP_Slide_Refine;
	@FXML
	AnchorPane AP_SlideList_Refine;
	@FXML
	AnchorPane AP_IVBox_Refine;
	@FXML
	HTMLEditor HTMLE_Annotation_Refine;
	@FXML
	AnchorPane 	AP_TagUtils_Refine;
	@FXML
	AnchorPane AP_MediaPlayer_Refine;
	@FXML
	CheckBox CB_AutoAdd_Refine;
	@FXML
	Label LBL_CurrentIndex_Refine;
	@FXML
	Button BT_BackEdit_Refine;
	@FXML
	Button BT_ForwardEdit_Refine;
	@FXML
	Button BT_ReloadText_Refine;
	@FXML
	Button BT_RemoveSlide_Refine;
	@FXML
	Button BT_InsertSlide_Refine;
	@FXML
	Button BT_InsertWarning_Refine;
	@FXML
	Button BT_InsertTable_Refine;
	@FXML
	Tab	TAB_View;
	@FXML
	TitledPane TP_Slide_View;
	@FXML
	AnchorPane AP_SlideList_View;
	@FXML
	AnchorPane AP_IVBox_View;
	@FXML
	AnchorPane AP_ViewUtils;
	@FXML
	WebView WV_Annotation_View;
	@FXML
	AnchorPane AP_TagUtils_View;
	@FXML
	Label LBL_CurrentIndex_View;
	@FXML
	Button BT_Back_View;
	@FXML
	Button BT_Forward_View;
	@FXML
	SplitPane SP_Utilities_Draft;
	@FXML
	SplitPane SP_Utilities_Refine;
	@FXML
	SplitPane SP_Utilities_View;
	@FXML
	SplitPane SP_MainBrowser_Draft;
	@FXML
	SplitPane SP_MainBrowser_Refine;
	@FXML
	SplitPane SP_MainBrowser_View;
	@FXML
	SplitPane SP_Browser_Draft;
	@FXML
	SplitPane SP_Browser_Refine;
	@FXML
	SplitPane SP_Browser_View;
	

	
	
	public 	Gallery activeGallery = null;
	public  GalleryItem currentSlide = null;
	public  CamAbstr camera = null;
	public 	ScreenCaptureTool screenCam = null;
	public volatile boolean captureCamOn = false;
	public volatile boolean markerInsert = false;
	private Thread imageCaptureThread = null;
	
	public AnchorPane AP_Audio = null;
	public AnchorPane AP_IVBoxBase = null;
	public AnchorPane AP_SlideListBase = null;
	public AnchorPane AP_TagUtilsBase = null;
	public AnchorPane AP_StopMotionBase = null;
	public AnchorPane AP_ChapterInputFormBase = null;
	public AnchorPane AP_TableImportFormBase = null;
	public AnchorPane AP_WarningLabelMakerBase = null;
	public AudioPlayerController audioPlayerCTRL = null;
	public IVBoxController IVBoxCTRL = null;
	public SlideListController slideListCTRL = null;
	public TagUtilsController tagUtilsCTRL = null;
	public StopMotionToolController smtCTRL = null;
	public ChapterInputFormController cifCTRL = null;
	public TableImportFormController tifCTRL = null;
	public WarningLabelMakerController wlmCTRL = null;
	private String safetyPlaceholder = null;
	private String resPlaceholder = null;
	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	private boolean saved = false; 
	
	private String examplepath = "<img style=\"display:block;\" src=\"plpictogrampathpl/WarningPictogram.png\" alt=\"image not found\" width=\"100%\">";
	private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	
	private WebEngine inlineSix;
	
	private void setDividers(GalleryItem cgi){
		if(cgi.getClass().equals(GalleryTitle.class)) {
			//Max out top part of the SplitPanes
			SP_Utilities_View.getDividers().get(0).setPosition(0.85);
			SP_Utilities_Refine.getDividers().get(0).setPosition(0.70);
			SP_Utilities_Refine.getDividers().get(1).setPosition(0.81);
			
			//Max out Width of utilities & Annotation
			SP_Browser_View.getDividers().get(0).setPosition(0.01);
			SP_Browser_Refine.getDividers().get(0).setPosition(0.01);
			SP_Browser_Draft.getDividers().get(0).setPosition(0.01);
			
		} else if(cgi.getClass().equals(GalleryChapter.class)) {
			//Max out top part of the SplitPanes
			SP_Utilities_View.getDividers().get(0).setPosition(0.85);
			SP_Utilities_Refine.getDividers().get(0).setPosition(0.70);
			SP_Utilities_Refine.getDividers().get(1).setPosition(0.81);
			
			//Max out Width of utilities & Annotation
			SP_Browser_View.getDividers().get(0).setPosition(0.01);
			SP_Browser_Refine.getDividers().get(0).setPosition(0.01);
			
		} else if(cgi.getClass().equals(GallerySlide.class)) {
				//Balance Utilities
				SP_Utilities_View.getDividers().get(0).setPosition(0.85);
				SP_Utilities_Refine.getDividers().get(0).setPosition(0.52);
				SP_Utilities_Refine.getDividers().get(1).setPosition(0.78);
			
			if(((GallerySlide)cgi).getPicture().getImage() != null) {//Balance Image Space and Annotation Space
				SP_Browser_View.getDividers().get(0).setPosition(0.66);
				SP_Browser_Refine.getDividers().get(0).setPosition(0.66);
			} else {//Maximize Text Space
				SP_Browser_View.getDividers().get(0).setPosition(0.01);
				SP_Browser_Refine.getDividers().get(0).setPosition(0.01);
			}
			
		} else if(cgi.getClass().equals(GallerySafetySlide.class)) {
			//Balance Utilities in favour of Text
			SP_Utilities_View.getDividers().get(0).setPosition(0.70);
			SP_Utilities_Refine.getDividers().get(0).setPosition(0.64);
			SP_Utilities_Refine.getDividers().get(1).setPosition(0.80);
		
			if(((GallerySlide)cgi).getPicture().isImageSet()) {//Balance Image Space and Annotation Space
				SP_Browser_View.getDividers().get(0).setPosition(0.55);
				SP_Browser_Refine.getDividers().get(0).setPosition(0.55);
			} else {//Balance Image/Text in favour of Text
				SP_Browser_View.getDividers().get(0).setPosition(0.01);
				SP_Browser_Refine.getDividers().get(0).setPosition(0.01);
			}
		}
		//Reset Slide List width:
		SP_MainBrowser_View.getDividers().get(0).setPosition(0.2);
		SP_MainBrowser_Refine.getDividers().get(0).setPosition(0.2);
		SP_MainBrowser_Draft.getDividers().get(0).setPosition(0.2);
	}
	
	private void updateSlideCountLabel() {
		if(this.activeGallery.getSlideCount() > 0) {
			this.LBL_CurrentIndex_Draft.setText("Slide: " + (this.activeGallery.getCurrentIndex()+1) + "/" + this.activeGallery.getSlideCount());
		} else if(this.currentSlide != null) {
			this.LBL_CurrentIndex_Draft.setText("Slide: " + (this.activeGallery.getCurrentIndex()+1) + "/1");
		} else {
			this.LBL_CurrentIndex_Draft.setText("Empty Gallery");
		}
	}
	
	private void blockNavigation(boolean block){
		this.BT_Back_View.disableProperty().set(block);
		this.BT_BackEdit_Draft.disableProperty().set(block);
		this.BT_BackEdit_Refine.disableProperty().set(block);
		this.BT_Forward_View.disableProperty().set(block);
		this.BT_ForwardEdit_Draft.disableProperty().set(block);
		this.BT_ForwardEdit_Refine.disableProperty().set(block);
	}
	
	
	private void disableAll(boolean nState) {
		//Buttons:
		this.BT_Back_View.disableProperty().set(nState);
		this.BT_BackEdit_Draft.disableProperty().set(nState);
		this.BT_BackEdit_Refine.disableProperty().set(nState);
		this.BT_Forward_View.disableProperty().set(nState);
		this.BT_ForwardEdit_Draft.disableProperty().set(nState);
		this.BT_InsertSlide_Draft.disableProperty().set(nState);
		this.BT_InsertSlide_Refine.disableProperty().set(nState);
		this.BT_InsertTable_Refine.disableProperty().set(nState);
		this.BT_InsertWarning_Refine.disableProperty().set(nState);
		this.BT_RecordAudio_Draft.disableProperty().set(nState);
		this.BT_OpenImgEditor_Draft.disableProperty().set(nState);
		this.BT_RecStopMotion_Draft.disableProperty().set(nState);
		this.BT_ReloadText_Refine.disableProperty().set(nState);
		this.BT_RemoveComponent_Draft.disableProperty().set(nState);
		this.BT_RemoveSlide_Refine.disableProperty().set(nState);
		this.BT_SnapShot_Draft.disableProperty().set(nState);
		this.BT_PasteClipboard_Draft.disableProperty().set(nState);
		this.HTMLE_Annotation_Refine.disableProperty().set(nState);
		
		//Sub-Controllers:
		this.audioPlayerCTRL.disableGUI(nState);
		this.IVBoxCTRL.disableGUI(nState);
		this.tagUtilsCTRL.disableGUI(nState);
	}
 	
	private String getClipboardImg() {
		String outstring = "";
		Image nImage = null;
		if(this.currentSlide != null && this.currentSlide instanceof GallerySafetySlide){
			try {
				nImage = this.screenCam.getClipboardImage();
				if(nImage != null){
					outstring = "Pasted image from Clipboard.";
				} else {
					outstring = "No image found on Clipboard!";
				}
			} catch (NullPointerException ne) {
				ne.printStackTrace();
			}
			if(nImage != null) {
				((GallerySafetySlide)this.currentSlide).getPicture().setImage(nImage);
				this.returnCurrentSlideToGallery();
			} else {
				outstring = "\n\rNo valid image found in clipboard!";
			}
		}
		return outstring;
	}
	
	/**
	 * Modify image paths in html annotation to absolute, to enable viewing of filesystem resoources in-app
	 */
	private void getCurrentSlideFromGallery(){
		safetyPlaceholder = "file:\\" + new File(this.GPSSingleton.getSafetyDataPath()).getAbsolutePath();
		resPlaceholder = "file:\\" + new File(this.GPSSingleton.getResourcePath()).getAbsolutePath() + "\\Logo";
		
		if(this.activeGallery.getSlideCount() > 0) {
			this.currentSlide = this.activeGallery.getCurrentItem();
	
			if(this.currentSlide.getClass().equals(GalleryTitle.class)){
				((GalleryTitle)this.currentSlide).loadDataFromProjectSettings();
		    } else if(this.currentSlide.getClass().equals(GalleryChapter.class)){
		    	((GalleryChapter)this.currentSlide).updateTemplate();
		    }
			String finalText = this.currentSlide.annotation.finalTextGet().replace("plpictogrampathpl", safetyPlaceholder);
			finalText = finalText.replace("pllogobasepathpl/Logo", resPlaceholder);	
			this.currentSlide.annotation.finalTextSet(finalText);
		}
	}
	
	/**
	 * Modify image paths in html annotation to relative, to enable export
	 */
	public void returnCurrentSlideToGallery(){
		if(this.safetyPlaceholder != null){
			//String absPlaceholder = "file:\\" + this.GPSSingleton.getSafetyDataPath();
			this.handleDraftText();
			this.handleRefineText();
			if(this.currentSlide.getClass().equals(GalleryTitle.class)){
				((GalleryTitle)this.currentSlide).loadDataFromProjectSettings();
		    } else if(this.currentSlide.getClass().equals(GalleryChapter.class)){
		    	((GalleryChapter)this.currentSlide).updateTemplate();
		    }
			String finalText = this.currentSlide.annotation.finalTextGet().replace(safetyPlaceholder, "plpictogrampathpl");
			finalText = finalText.replace(resPlaceholder, "pllogobasepathpl/Logo");
			finalText = TypeUtils.newlinesToNbsp(finalText);
			
			this.currentSlide.annotation.finalTextSet(finalText);		
		}
	}
	
	public void setSaved(){
		saved = true;
	}
	
	public void showTab(String tabname) {
		switch (tabname.toLowerCase()){
		case "draft":
			this.TP_GalleryBrowser.getSelectionModel().select(TAB_Draft);
			this.setDividers(currentSlide);
			break;
			
		case "refine": 
			this.TP_GalleryBrowser.getSelectionModel().select(TAB_Refine);
			this.setDividers(currentSlide);
			break;
			
		default:
			this.TP_GalleryBrowser.getSelectionModel().select(TAB_View);
			this.setDividers(currentSlide);
			break;
		}
		
		
	}
	
	
	public void stopAllThreads() { 
		if(this.imageCaptureThread != null && this.imageCaptureThread.isAlive()) {
			try {
				while(this.imageCaptureThread.isAlive()){
					synchronized(this){
						synchronized(this.IVBoxCTRL.IV_Illustration){
							synchronized(this.camera){
								this.imageCaptureThread.interrupt();
							}
						}
					}
		
//					System.out.println("\n\rGBC.stopAllThreads() in while-loop");
					Thread.sleep(1);
					
				}
			} catch (InterruptedException e) {
				//NOP
			}
			if(!this.imageCaptureThread.isAlive()){
				this.imageCaptureThread = null;
			}
		}
		if(this.IVBoxCTRL != null){
			this.IVBoxCTRL.stopAllThreads();
		}

		if(this.activeGallery != null){
			this.blockNavigation(false);
		}
	}
	
	/**
	 * Sets the gallery (can be set to null!)
	 * @param nGallery
	 */
	public void setGallery(Gallery nGallery) {
		this.activeGallery = nGallery;
	}
	
	public Image getDisplayedImage() {
		synchronized(this.IVBoxCTRL.IV_Illustration){
			return this.IVBoxCTRL.IV_Illustration.getImage();
		}
	}

	
	@Override
	public void initCTRL() {
		// ------ Define tooltips
		// Draft
		this.CB_AutoAdd_Draft.setTooltip(ToolTipMaker.simpleToolTip(	"If this box is checked, the forward (\" > \") button in the draft and refine tabs \n"
				+ 	"adds a new blank slide when clicked at the end of the gallery."));
		this.BT_RemoveComponent_Draft.setTooltip(ToolTipMaker.simpleToolTip("Opens a dialog to remove selected components from this slide: \n"
				+ "The Audio Draft\n"
				+ "The Image\n"
				+ "The slide itself\n\n"
				+ "Note that removed items are permanently deleted."));
		
		this.TA_Annotation_Draft.setTooltip(ToolTipMaker.simpleToolTip(	"This textbox is for quickly writing down important information. \n"
				+ "Styling or formatting is not important, and can be done at a later stage in the \"Refine\" Tab.\n\n"
				+ "If a process that requires the documenters hands needs to be documented on the fly, \n"
				+ "the \"Record Audio\" function may be a suitable tool."));
		this.BT_InsertSlide_Draft.setTooltip(ToolTipMaker.simpleToolTip(	"Insert a new instructional slide before or after the current one."));
		this.BT_RecStopMotion_Draft.setTooltip(ToolTipMaker.simpleToolTip(	"Record a series of images to indicate an action in progress. \n"
				+ "The images can be inserted via webcam or via clipboard.\n"
				+ "If the slide already contains a Stop Motion Picture, \n"
				+ "A dialog will offer to edit the existing one \n"
				+ "or discard it and create a new Stop Motion Picture."));
		this.BT_RecordAudio_Draft.setTooltip(ToolTipMaker.simpleToolTip(	"Records an audio clip from the default microphone. \n"
				+ "This feature is intended for the drafting and refinement phase, "
				+ "specifically for recording complex information during a task, without occupying the documenters hands.\n"
				+ "The recorded audio clip is not available in View Mode."));
		this.BT_OpenImgEditor_Draft.setTooltip(ToolTipMaker.simpleToolTip(	"Opens the slide image (last version to have been saved) \n"
				+ "for editing in the systems default editor. "
				+ "After editing the image, the easiest way to re-insert it is: \n\n"
				+ "->Copy modified image from editor to clipboard \n"
				+ "->Click \"Insert from Clipboard\" in ManuMaker's Draft Tab\n"
				+ "->Save the project"));
		this.BT_PasteClipboard_Draft.setTooltip(ToolTipMaker.simpleToolTip(	"Inserts an image from the System Clipboard if one is available.\n"
				+ "Existing images in the slide are replaced without prompt.\n\n"
				+ "Use of a snipping tool is recommended to make the visual information more concise\n"
				+ "when adding screenshots."));
		this.BT_SnapShot_Draft.setTooltip(ToolTipMaker.simpleToolTip(	"Adds a snapshot from the currently selected Camera to the current gallery slide.\n"
				+ "The first click starts the camera stream, the second captures the image. \n"
				+ "If the red \"Cancel\" button in the lower right corner of the image is clicked,\n "
				+ "the camera stream is disabled and no image is captured. \n"
				+ "Existing images are overwritten without prompt. \n\n"
				+ "To set a different Webcam or resolution, go to the top menu bar \n"
				+ "and select Settings -> Application Settings"));

		//Refine
		this.CB_AutoAdd_Refine.setTooltip(ToolTipMaker.simpleToolTip(	"If this box is checked, the forward (\" > \") button in the draft and refine tabs \n"
				+ 	"adds a new blank slide when clicked at the end of the gallery."));
		
		this.BT_ReloadText_Refine.setTooltip(ToolTipMaker.simpleToolTip(	"Loads the text from draft to refine text-editor. \n"
				+ "Existing text in the refine editor is discarded."));
		this.BT_RemoveSlide_Refine.setTooltip(ToolTipMaker.simpleToolTip(	"Removes the current slide from the gallery. \n"
				+ "This action cannot be undone."));
		this.BT_InsertSlide_Refine.setTooltip(ToolTipMaker.simpleToolTip(	"Opens a form which presents two possible actions: \n"
				+ "-> Insert a title slide, whose data is loaded from the Project Settings \n"
				+ "-> Insert a chapter slide, whose title and additional data can be defined in a form."));
		
		this.BT_InsertWarning_Refine.setTooltip(ToolTipMaker.simpleToolTip(	"Opens a template-form for notices or full-fledged warning labels.\n"
				+ "The content and pictogram can be set, and the result is copied to the clipboard. \n"
				+ "To insert the label, rightclick on the desired location in the Refine Tab's text\n"
				+ "and select \"Paste\". The labels can be edited (carefully!) in Refine mode."));

		this.BT_InsertTable_Refine.setTooltip(ToolTipMaker.simpleToolTip(	"Allows importing a table saved in simple html form\n"
				+ "In OpenOffice Calc, use the \"save As\" function and select \".html\".\n"
				+ "A caption and an identifier for the table have to be provided.\n"
				+ "Large tables may make the export-to-printable-html function useless. \n\n"
				+ "A very tall table should be inserted on a blank slide without an image, \n"
				+ "so it will be printed on its own page. If printing is a concern."));
		
		this.HTMLE_Annotation_Refine.setTooltip(ToolTipMaker.simpleToolTip(	"The annotation text can be finalized in this editor. \n"
				+ "The draft text can be loaded (button at the lower left) and worked into a presentable form. \n"
				+ "The Audio draft can be played, and its information written down."
				+ "Tables and Warning Labels can be inserted (buttons at the bottom right)\n\n"));

		// ------ end of tooltips
		System.out.println("\r\nGalleryBrowserController.initCTRL() -> Initializing Controller...");
		
		this.HTMLE_Annotation_Refine.setStyle("-fx-font-family:\"Arial\"");
		this.HTMLE_Annotation_Refine.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mev){
				if(mev.getButton() == MouseButton.SECONDARY && mev.getButton() == MouseButton.PRIMARY){
					GalleryBrowserController.this.handleRefineText();
				}
			}
		});

	
		this.TP_Slide_Refine.textProperty().bind(this.TP_Slide_Draft.textProperty());
		this.TP_Slide_View.textProperty().bind(this.TP_Slide_Draft.textProperty());

	
		this.CB_AutoAdd_Draft.selectedProperty().set(true);
		this.CB_AutoAdd_Refine.selectedProperty().set(true);
		

	//	System.out.println("\r\nGalleryBrowserController.initCTRL() -> Initializing Controller...");
		this.camera = new SarxosCam();
		this.screenCam = new ScreenCaptureTool();
	//	System.out.println("\r\nGalleryBrowserController.initCTRL() -> Creating a new Gallery...");
		this.activeGallery = new Gallery(false);
		this.getCurrentSlideFromGallery();
		if(this.currentSlide == null) {
		//	System.out.println("\n\r GBC.initCTRL(): CurrentSlide == null");
		} else {
		//	System.out.println("\n\r GBC.initCTRL(): CurrentSlide retrieved...");
		}
		this.LBL_CurrentIndex_Refine.textProperty().bind(this.LBL_CurrentIndex_Draft.textProperty());
		this.LBL_CurrentIndex_View.textProperty().bind(this.LBL_CurrentIndex_Draft.textProperty());

		this.AP_MediaPlayer_Draft.getChildren().add(AP_Audio);
		this.fitAPToParent(AP_Audio);
		this.AP_IVBox_Draft.getChildren().add(AP_IVBoxBase);
		this.fitAPToParent(AP_IVBoxBase);
		this.AP_SlideList_Draft.getChildren().add(AP_SlideListBase);
		this.fitAPToParent(AP_SlideListBase);
		this.AP_TagUtils_Draft.getChildren().add(AP_TagUtilsBase);
		this.fitAPToParent(AP_TagUtilsBase);
	
		
			
		this.WV_Annotation_View.setContextMenuEnabled(false);
		this.WV_Annotation_View.setCache(false);
		this.inlineSix = this.WV_Annotation_View.getEngine();
//		this.updateCurrentSlide();
		System.out.println("\r\nGalleryBrowserController.initCTRL() -> Controller Initialized...");
	}
	
	@Override
	public void updateUI() {
		// Disable features not applicable to current slide type!
		if(this.currentSlide != null){
			if(this.currentSlide.getClass().equals(GalleryTitle.class)) {
				// same as GalleryChapter
				this.disableAll(true);
				this.tagUtilsCTRL.disableGUI(true);
				this.blockNavigation(false);
				this.AP_Audio.disableProperty().set(true);
				
				//Enable: 			
				this.BT_InsertSlide_Refine.disableProperty().set(false);
				this.BT_RemoveSlide_Refine.disableProperty().set(false);
				this.BT_InsertSlide_Draft.disableProperty().set(false);
				this.IVBoxCTRL.setPicture(null);
			//	System.out.println("\r\n\n\nGBC.updateUI() -> CurrentSlide instanceof GalleryTitle");
				
			} else if(this.currentSlide.getClass().equals(GalleryChapter.class)) {
				// same as GalleryChapter
				this.disableAll(true);
				this.tagUtilsCTRL.disableGUI(true);
				this.blockNavigation(false);
				this.AP_Audio.disableProperty().set(true);
				
				//Enable: 			
				this.BT_InsertSlide_Refine.disableProperty().set(false);
				this.BT_RemoveSlide_Refine.disableProperty().set(false);
				this.BT_InsertSlide_Draft.disableProperty().set(false);
			//	System.out.println("\r\n\n\nGBC.updateUI() -> CurrentSlide intanceof GalleryChapter");
			
			} else if(this.currentSlide.getClass().equals(GallerySlide.class)) {
				// Enable ALL
				this.disableAll(false);
				this.AP_Audio.disableProperty().set(false);
				this.audioPlayerCTRL.disableGUI(false);
				
				if(((GallerySlide)this.currentSlide).getPicture().getClass().equals(SlideStopMotion.class)){
					this.BT_OpenImgEditor_Draft.disableProperty().set(true);
				} else {
					this.BT_OpenImgEditor_Draft.disableProperty().set(false);
				}
				//Disable: None
			//	System.out.println("\r\n\n\nGBC.updateUI() -> CurrentSlide intanceof GallerySlide");
			} else if(this.currentSlide.getClass().equals(GallerySafetySlide.class)) {
				//Enable ALL
				this.disableAll(false);
				//Disable:
				this.tagUtilsCTRL.disableGUI(true);
				this.AP_Audio.disableProperty().set(true);
				this.audioPlayerCTRL.disableGUI(true);
			//	System.out.println("\r\n\n\nGBC.updateUI() -> CurrentSlide intanceof GallerySafetySlide");
			} else {
				this.disableAll(false);
			//	System.out.println("\r\n\n\nGBC.updateUI() -> CurrentSlide instanceof UNKNOWN CLASS... This shouldn't happen....");
			}
			
			if(!this.tagUtilsCTRL.TP_Parts.isExpanded() && !this.tagUtilsCTRL.TP_Tools.isExpanded()){
				this.tagUtilsCTRL.TP_Tags.setExpanded(true);
			}
//			this.IVBoxCTRL.updateUI();
			this.slideListCTRL.updateUI();
			this.IVBoxCTRL.updateUI();
			this.setDividers(this.currentSlide);
		}
	}
	
	@FXML 
	public void handleDraftTab() {
		if(this.TAB_Draft.isSelected()){
			if(this.AP_Audio != null) {
				this.AP_MediaPlayer_Draft.getChildren().add(AP_Audio);
			}
			if(this.AP_IVBoxBase != null) {
				this.AP_IVBox_Draft.getChildren().add(AP_IVBoxBase);
				this.fitAPToParent(AP_IVBoxBase);
			}
			if(this.AP_SlideListBase != null) {
				this.AP_SlideList_Draft.getChildren().add(AP_SlideListBase);
				this.fitAPToParent(AP_SlideListBase);
			}
			if(this.AP_TagUtilsBase != null) {
				this.AP_TagUtils_Draft.getChildren().add(AP_TagUtilsBase);
				this.fitAPToParent(AP_TagUtilsBase);
				this.tagUtilsCTRL.setEditable(true);
			}
			if(this.imageCaptureThread != null){
				this.imageCaptureThread.interrupt();
			}
			this.updateCurrentSlide();
			this.updateUI();
			SP_Browser_Draft.getDividers().get(0).setPosition(0.66);
			//this.blockNavigation(false);
			stopAllThreads();
			
		}
	}
	
	@FXML 
	public void handleRefineTab() {
		if(this.TAB_Refine.isSelected()){
			if(this.AP_Audio != null) {
				this.AP_MediaPlayer_Refine.getChildren().add(AP_Audio);
			}
			if(this.AP_IVBoxBase != null) {
				this.AP_IVBox_Refine.getChildren().add(AP_IVBoxBase);
				this.fitAPToParent(AP_IVBoxBase);
			}			
			if(this.AP_SlideListBase != null) {
				this.AP_SlideList_Refine.getChildren().add(AP_SlideListBase);
				this.fitAPToParent(AP_SlideListBase);
			}
			if(this.AP_TagUtilsBase != null) {
				this.AP_TagUtils_Refine.getChildren().add(AP_TagUtilsBase);
				this.fitAPToParent(AP_TagUtilsBase);
				this.tagUtilsCTRL.setEditable(true);
			}
			if(this.imageCaptureThread != null){
				this.imageCaptureThread.interrupt();
			}
			this.updateCurrentSlide();
			this.updateUI();
			stopAllThreads();
		}
	}
	
	@FXML
	public void handleViewTab() {
		if(this.TAB_View.isSelected()){
			if(this.AP_Audio != null) {
				this.AP_MediaPlayer_Draft.getChildren().clear();
				this.AP_MediaPlayer_Refine.getChildren().clear();
				this.audioPlayerCTRL.updateUI();
			}
			if(this.AP_IVBoxBase != null) {
				this.AP_IVBox_View.getChildren().add(AP_IVBoxBase);	
				this.fitAPToParent(AP_IVBoxBase);
				this.IVBoxCTRL.updateUI();
			}
			if(this.AP_SlideListBase != null) {
				this.AP_SlideList_View.getChildren().add(AP_SlideListBase);
				this.fitAPToParent(AP_SlideListBase);
				this.slideListCTRL.updateUI();
			}
			if(this.AP_TagUtilsBase != null) {
				this.AP_TagUtils_View.getChildren().add(AP_TagUtilsBase);
				this.fitAPToParent(AP_TagUtilsBase);
				this.tagUtilsCTRL.setEditable(false);
				this.tagUtilsCTRL.updateUI();
			}
			if(this.imageCaptureThread != null){
				this.imageCaptureThread.interrupt();
			}
			
			this.updateCurrentSlide();
			this.updateUI();
			stopAllThreads();
		}
	}
	@FXML
	public void handleLockGallery_Draft() {
		stopAllThreads();
		this.CB_AutoAdd_Refine.selectedProperty().set(this.CB_AutoAdd_Draft.selectedProperty().get());
		
	}
	
	@FXML
	public void handleLockGallery_Refine() {
		stopAllThreads();
		this.CB_AutoAdd_Draft.selectedProperty().set(this.CB_AutoAdd_Refine.selectedProperty().get());
		
	}
	
	@FXML
	public void handleSlideTitle(){
		this.IVBoxCTRL.handleEditTitle();
	}
	
	@FXML
	public void handleForward_Edit() {
		stopAllThreads();
		if(this.imageCaptureThread != null){
			this.imageCaptureThread.interrupt();
		}
		String outString = "";
		this.returnCurrentSlideToGallery();
		if(!this.CB_AutoAdd_Draft.isSelected()) {
			outString = this.activeGallery.nextSlide(false);
		} else {
			outString = this.activeGallery.nextSlide(true);
		}
		this.updateCurrentSlide(); // Keep at the end of the method!
		this.mainApp.setSysMessageLabel(outString);
		this.updateUI();
	}
	
	@FXML
	public void handleSnapShot() {
		boolean started = false;
		if(this.captureCamOn == false) {						// If thread inactive, Start Thread
			this.stopAllThreads();
			this.blockNavigation(true);	
			this.threadInit();
			started = true;
		} else {					// If Thread active, stop thread
			this.stopAllThreads();
			this.IVBoxCTRL.enableAbortBtn(false);
			Image tmp = this.getDisplayedImage();
			if(tmp != null){
				try{
					if(this.currentSlide.getClass().equals(GallerySlide.class)){
								((GallerySlide) this.currentSlide).setPicture(new SlideInstructionalPic(tmp));
					} else if(this.currentSlide.getClass().equals(GallerySafetySlide.class)){
						((GallerySafetySlide) this.currentSlide).setPicture(new SlidePic(this.IVBoxCTRL.IV_Illustration.getImage()));							
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		if(started == false){
			this.stopAllThreads();
		}
		
		this.updateCurrentSlide();
		this.blockNavigation(false);
	}
	
	@FXML
	public void handlePasteClipboard(){
		this.stopAllThreads();
		if(this.imageCaptureThread == null || this.imageCaptureThread.isAlive() == false){
			this.mainApp.setSysMessageLabel(this.getClipboardImg());
			this.updateCurrentSlide();
		}
	}

	
	@FXML
	public void handleOpenImgEditor() {
		stopAllThreads();
		File currentImg = new File(this.GPSSingleton.getSavePath() + "Resources\\slide_" + this.activeGallery.getCurrentIndex() + ".png");
		System.out.println("\n\rOpening image in external editor: " + currentImg.getPath());
		if(this.currentSlide.getClass().equals(GallerySlide.class)){
			if(currentImg.exists()) {
				try {
					Desktop.getDesktop().edit(currentImg);
				} catch (IOException e) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("!");
					alert.setHeaderText("No image attached to slide!");
					alert.setContentText( "This slide does not contain an image.");
					alert.showAndWait();
					e.printStackTrace();
				} catch(NullPointerException npe) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("!");
					alert.setHeaderText("Please save before editing!");
					alert.setContentText( 	"This slide has not been saved to the filesystem! \n"
									+ 		"Please save the project before editing the image in an \n"
									+ 		"External editor.");
									
					alert.showAndWait();
				}
			} else if(((GallerySafetySlide)this.activeGallery.currentSlide).getPicture() != null &&
					((GallerySafetySlide)this.activeGallery.currentSlide).getPicture().getImage() != null ){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Slide does not contain an image!");
				alert.setHeaderText("No image attached to slide!");
				alert.setContentText( "This slide does not contain an image.");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Image not found!");
				alert.setHeaderText("Image file not found!");
				alert.setContentText( "The image file associated with this slide"
									+ "\ncould not be found. "
									+ "\nPlease search for it manually. ");
				alert.showAndWait();
			}
		}
	}
	
	@FXML
	public void handleRecordAudio() {
		this.stopAllThreads();
		if(this.currentSlide.getClass().equals(GallerySlide.class)){
			if(((GallerySlide)this.currentSlide).getAudioDraft() != null){
				Alert alert =  new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Discard previously recorded audio draft?");
				alert.setHeaderText("Discard existing audio clip?");
				alert.setContentText("The existing clip will be deleted permanently.");
				
				ButtonType buttonOK = new ButtonType("OK");
				ButtonType buttonCancel = new ButtonType("Cancel");
				alert.getButtonTypes().setAll(buttonOK, buttonCancel);
				
				Optional<ButtonType> result = alert.showAndWait();
				
				if(result.get() == buttonOK) {				
					this.audioPlayerCTRL.deleteAudio();
					((GallerySlide)this.currentSlide).deleteAudioDraft();
					stopAllThreads();
					this.disableAll(true);
					this.BT_RecordAudio_Draft.disableProperty().set(true);
					this.audioPlayerCTRL.handleRecord();
					

				} else {
					//NOP
				}
			} else {
				stopAllThreads();
				this.disableAll(false);
				this.BT_RecordAudio_Draft.disableProperty().set(true);
				this.audioPlayerCTRL.handleRecord();
				this.updateCurrentSlide();
			}
			this.audioPlayerCTRL.disableGUI(false);
			
			
		}
	}
	
	public void audioRecorderDone() {
		stopAllThreads();
		this.disableAll(false);
		this.updateCurrentSlide();
	}
	
	@FXML
	public void handleRecStopMotion() {
		stopAllThreads();
		// Disable other tabs & nav buttons
		this.TAB_Refine.disableProperty().set(true);
		this.TAB_View.disableProperty().set(true);
		this.blockNavigation(true);
		
		this.smtCTRL.setOldChild(this.AP_TagUtils_Draft.getChildren().get(0));
		this.AP_TagUtils_Draft.getChildren().clear();
		this.AP_TagUtils_Draft.getChildren().add(this.AP_StopMotionBase);		
		this.fitAPToParent(this.AP_StopMotionBase);
		this.disableAll(true);
		this.IVBoxCTRL.disableGUI(false);
		
		// If current Slide contains a stop-motion image, offer: opening it for edit OR discard and make a new one 
		if(this.currentSlide instanceof GallerySlide) {
			if(((GallerySlide) this.currentSlide).getPicture() instanceof SlideStopMotion){
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Open Existing Stop Motion Picture?");
				alert.setHeaderText("Open or Overwrite?");
				alert.setContentText("This slide already contains a stop-motion picture."
						+ "\n\rOpen the existing one for editing, "
						+ "\nor replace it with a new one?");
				
				ButtonType buttonEdit = new ButtonType("Edit"); 
				ButtonType buttonNew = new ButtonType("New");
				ButtonType buttonCancel = new ButtonType("Cancel");
				
				alert.getButtonTypes().setAll(buttonEdit, buttonNew, buttonCancel);
				
				Optional<ButtonType> result = alert.showAndWait();
				
				if(result.get() == buttonEdit) {
					this.smtCTRL.loadSMP(((SlideStopMotion)((GallerySlide)this.currentSlide).getPicture()));
				} else if(result.get() == buttonNew) {
					this.smtCTRL.resetController();
					this.stopAllThreads();
					this.activeGallery.setCurrentItem(new GallerySlide());
				} else {
					stopMotionDone(false);
				}
			}
		}
		
		this.updateCurrentSlide();
		
	}
	
	public void stopMotionDone(boolean ok) {
		if(ok == true) {
			if(this.captureCamOn == true){
			//If Thread active, stop thread
				if(this.imageCaptureThread != null){
					this.imageCaptureThread.interrupt();
				}
				this.blockNavigation(false);
				
			}
			
		} 
		this.AP_TagUtils_Draft.getChildren().clear();
		this.AP_TagUtils_Draft.getChildren().add(this.smtCTRL.getOldChild());
		this.fitAPToParent(this.AP_TagUtilsBase);
		// Re-Enable other tabs & nav buttons
		this.TAB_Refine.disableProperty().set(false);
		this.TAB_View.disableProperty().set(false);
		this.blockNavigation(false);
		this.stopAllThreads();
		this.disableAll(false);
		this.updateCurrentSlide();
	}
	
	@FXML
	public void handleDraftText() {
		stopAllThreads();
		if(this.TA_Annotation_Draft.getText() != null){
			String text = new String(this.TA_Annotation_Draft.getText());
			char terminator = ' ';
			
			if(text.length() > 1){
				terminator = text.charAt(text.lastIndexOf("")-1);
			} else {
				terminator = '\n';
			}
			
			if(terminator == '\r' || terminator == '\n'){
				this.currentSlide.annotation.draftTextSet(text);
			} else {
				this.currentSlide.annotation.draftTextSet(text + "\r\n");
			}
		}
	}
	
	@FXML
	public void handleInsertSlide() {
		ButtonType buttonBefore = new ButtonType("Insert Before"); 
		ButtonType buttonAfter = new ButtonType("Insert After");
		ButtonType buttonOK = new ButtonType("OK");
		ButtonType buttonContinue = new ButtonType("Cancel");
		stopAllThreads();
		if(this.TAB_Draft.selectedProperty().get()) { 
			Alert alert = new Alert(AlertType.CONFIRMATION);
			if(this.currentSlide.getClass().equals(GalleryTitle.class)){
				alert.setTitle("Insert Slide");
				alert.setHeaderText("Insert after current slide?");
				alert.setContentText("");
				alert.getButtonTypes().setAll(buttonOK, buttonContinue);
			} else {
				alert.setTitle("Insert Slide");
				alert.setHeaderText("Insert before or after current slide?");
				alert.setContentText("Please choose.");
				alert.getButtonTypes().setAll(buttonBefore, buttonAfter, buttonContinue);
			}			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == buttonBefore) {
				this.activeGallery.newSlide(this.activeGallery.getCurrentIndex());
				this.updateCurrentSlide(); // Keep at the end of the method!
				this.updateUI();
			} else if(result.get() == buttonAfter || result.get() == buttonOK) {
				this.activeGallery.newSlide(this.activeGallery.getCurrentIndex()+1);
				handleForward_View();
			} else {
				//NOP
			}
		} else {
			this.TAB_Draft.disableProperty().set(true);
			this.TAB_View.disableProperty().set(true);
			this.blockNavigation(true);
			
			this.cifCTRL.setOldChild(this.AP_TagUtils_Refine.getChildren().get(0));
			this.AP_TagUtils_Refine.getChildren().clear();
			this.AP_TagUtils_Refine.getChildren().add(this.AP_ChapterInputFormBase);		
			this.fitAPToParent(this.AP_ChapterInputFormBase);
			this.SP_Utilities_Refine.getDividers().get(0).setPosition(0.20);
			this.SP_Utilities_Refine.getDividers().get(1).setPosition(0.81);
			this.disableAll(true);
		}
		this.updateCurrentSlide();
		this.stopAllThreads();
	}
	
	public void chapterImportFormControllerDone(GalleryItem newSlide, boolean before) {
		if(newSlide == null) {
		//	System.out.println("\n\rGBC.chapterImportFormControllerDone() -> new Slide == null!");
		} else if (newSlide.getClass().equals(GalleryTitle.class)){
			this.activeGallery.newSlide(0, newSlide);
		} else if (newSlide.getClass().equals(GalleryChapter.class)){
			if(before == true && !this.currentSlide.getClass().equals(GalleryTitle.class)){
				System.out.println(	"\n\rGBC.chapterImportFormControllerDone:             before == " + before + 
									"\n\rthis.currentSlide.getClass().equals(GalleryTitle.class) == "+this.currentSlide.getClass().equals(GalleryTitle.class));
				this.activeGallery.newSlide(this.activeGallery.getCurrentIndex(), newSlide);
			} else {
				System.out.println(	"\n\rGBC.chapterImportFormControllerDone:             before == " + before + 
						"\n\rthis.currentSlide.getClass().equals(GalleryTitle.class) == "+this.currentSlide.getClass().equals(GalleryTitle.class));

				this.activeGallery.newSlide(this.activeGallery.getCurrentIndex()+1, newSlide);
			}
		}
		
		this.cifCTRL.initCTRL();
		
		this.AP_TagUtils_Refine.getChildren().clear();
		this.AP_TagUtils_Refine.getChildren().add(this.cifCTRL.getOldChild());		
		this.fitAPToParent(this.AP_TagUtils_Refine);
		this.HTMLE_Annotation_Refine.prefHeightProperty().set(250);
		this.TAB_Draft.disableProperty().set(false);
		this.TAB_View.disableProperty().set(false);
		this.blockNavigation(false);
		this.disableAll(false);
		this.updateCurrentSlide();
		this.updateUI();
		
		
	}
	
	@FXML
	public void handleRemove() {
		stopAllThreads();
		if(this.TAB_Draft.selectedProperty().get()) { 
			List<String> selection = new ArrayList<>();
			selection.add("Picture");
			selection.add("Audio Draft");
			selection.add("Current Slide");
			
			ChoiceDialog<String> deleteDialog = new ChoiceDialog<>("Picture", selection);
			deleteDialog.setTitle("Remove...");
			deleteDialog.setHeaderText("Remove the current slide or one of its components.");
			deleteDialog.setContentText("Please choose a component to remove:");
			
			Optional<String> dialogRes = deleteDialog.showAndWait();
			if(dialogRes.isPresent()) {
				switch(dialogRes.get()) {
				case "Picture":
					if(this.currentSlide instanceof GallerySafetySlide){
						Alert conf = new Alert(AlertType.CONFIRMATION);
						conf.setTitle("Confirm: Delete picture from current slide?");
						conf.setHeaderText(conf.getTitle());
						conf.setContentText("This action cannot be reversed. \n\rPlease confirm:");
						
						ButtonType BTT_OK = new ButtonType("OK");
						ButtonType BTT_Cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
						
						conf.getButtonTypes().setAll(BTT_OK, BTT_Cancel);
						
						Optional<ButtonType> res = conf.showAndWait();
						if(res.get() == BTT_OK) {
							if(this.currentSlide instanceof GallerySlide) {
								((GallerySlide) this.currentSlide).deletePicture();
								this.returnCurrentSlideToGallery();
								
							} else {
								((GallerySafetySlide) this.currentSlide).deletePicture();
								this.returnCurrentSlideToGallery();
							}
							this.returnCurrentSlideToGallery();
						} else {
							//NOP
						}	
					}
					break;
					
					
				case "Audio Draft":
					if(this.currentSlide instanceof GallerySlide){
						Alert conf = new Alert(AlertType.CONFIRMATION);
						conf.setTitle("Confirm: Delete audio draft from current slide?");
						conf.setHeaderText(conf.getTitle());
						conf.setContentText("This action cannot be reversed. \n\rPlease confirm:");
						
						ButtonType BTT_OK = new ButtonType("OK");
						ButtonType BTT_Cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
						
						conf.getButtonTypes().setAll(BTT_OK, BTT_Cancel);
						
						Optional<ButtonType> res = conf.showAndWait();
						if(res.get() == BTT_OK) {
							this.audioPlayerCTRL.deleteAudio();
							((GallerySlide) this.currentSlide).deleteAudioDraft();
							this.currentSlide.galleryItemSave(this.activeGallery.getCurrentIndex(), null);
						} else {
							//NOP
						}						
					}
					break;
					
				case "Current Slide":
					Alert conf = new Alert(AlertType.CONFIRMATION);
					conf.setTitle("Confirm: Delete current slide?");
					conf.setHeaderText(conf.getTitle());
					conf.setContentText("This action cannot be reversed. \n\rPlease confirm:");
					
					ButtonType BTT_OK = new ButtonType("OK");
					ButtonType BTT_Cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
					
					conf.getButtonTypes().setAll(BTT_OK, BTT_Cancel);
					
					Optional<ButtonType> res = conf.showAndWait();
					if(res.get() == BTT_OK) {
						if(this.currentSlide.getClass().equals(GallerySlide.class)){
							this.audioPlayerCTRL.deleteAudio();
							((GallerySlide) this.currentSlide).deleteAudioDraft();
						}
						this.handleRemoveSlide();
					} else {
						//NOP
					}						
					break;
				}
				this.updateCurrentSlide();
			}
			
		} else if(this.TAB_Refine.selectedProperty().get()){
			Alert conf = new Alert(AlertType.CONFIRMATION);
			conf.setTitle("Confirm: Delete current slide?");
			conf.setHeaderText(conf.getTitle());
			conf.setContentText("This action cannot be reversed. \n\rPlease confirm:");
			
			ButtonType BTT_OK = new ButtonType("OK");
			ButtonType BTT_Cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			
			conf.getButtonTypes().setAll(BTT_OK, BTT_Cancel);
			
			Optional<ButtonType> res = conf.showAndWait();
			if(res.get() == BTT_OK) {
				if(this.currentSlide.getClass().equals(GallerySlide.class)){
					this.audioPlayerCTRL.deleteAudio();
					((GallerySlide) this.currentSlide).deleteAudioDraft();
				}
				this.handleRemoveSlide();
			} else {
				//NOP
			}				
		}
		this.updateCurrentSlide();
	}
	

	
	@FXML
	public void handleInsertTable() {
		stopAllThreads();
		this.TAB_Draft.disableProperty().set(true);
		this.TAB_View.disableProperty().set(true);
		this.blockNavigation(true);
		
		this.tifCTRL.setOldChild(this.AP_TagUtils_Refine.getChildren().get(0));
		this.AP_TagUtils_Refine.getChildren().clear();
		this.AP_TagUtils_Refine.getChildren().add(this.AP_TableImportFormBase);		
		this.fitAPToParent(this.AP_TableImportFormBase);
		this.SP_Utilities_Refine.getDividers().get(0).setPosition(0.20);
		this.SP_Utilities_Refine.getDividers().get(1).setPosition(0.81);
		this.disableAll(true);	
	}
	
	public void tableImportFormControllerDone(){
		this.AP_TagUtils_Refine.getChildren().clear();
		this.AP_TagUtils_Refine.getChildren().add(this.tifCTRL.getOldChild());		
		this.fitAPToParent(this.AP_TagUtils_Refine);
		this.HTMLE_Annotation_Refine.prefHeightProperty().set(250);
		this.TAB_Draft.disableProperty().set(false);
		this.TAB_View.disableProperty().set(false);
		this.blockNavigation(false);
		this.disableAll(false);
		this.updateCurrentSlide();
		this.updateUI();
	}
	
	@FXML
	public void handleInsertWarning() {
		stopAllThreads();
		this.wlmCTRL.primeWLM();
		this.TAB_Draft.disableProperty().set(true);
		this.TAB_View.disableProperty().set(true);
		this.blockNavigation(true);
		
		this.wlmCTRL.setOldChild(this.AP_TagUtils_Refine.getChildren().get(0));
		this.AP_TagUtils_Refine.getChildren().clear();
		this.AP_TagUtils_Refine.getChildren().add(this.AP_WarningLabelMakerBase);		
		this.fitAPToParent(this.AP_WarningLabelMakerBase);
		
		this.SP_Utilities_Refine.getDividers().get(0).setPosition(0.20);
		this.SP_Utilities_Refine.getDividers().get(1).setPosition(0.81);
		
		this.disableAll(true);	
	}
	
	public void warningLabelMakerControllerDone(){
		this.AP_TagUtils_Refine.getChildren().clear();
		this.AP_TagUtils_Refine.getChildren().add(this.wlmCTRL.getOldChild());		
		this.fitAPToParent(this.AP_TagUtils_Refine);
		this.HTMLE_Annotation_Refine.prefHeightProperty().set(250);
		this.TAB_Draft.disableProperty().set(false);
		this.TAB_View.disableProperty().set(false);
		this.blockNavigation(false);
		this.disableAll(false);
		this.updateCurrentSlide();
		this.updateUI();
	}
	
	
	@FXML
	public void handleRemoveSlide() {
		stopAllThreads();
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setTitle("Confirm: Delete current slide from gallery?");
		conf.setHeaderText(conf.getTitle());
		conf.setContentText("This action cannot be reversed. \n\rPlease confirm:");
		
		ButtonType BTT_OK = new ButtonType("OK");
		ButtonType BTT_Cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		conf.getButtonTypes().setAll(BTT_OK, BTT_Cancel);
		
		Optional<ButtonType> res = conf.showAndWait();
		if(res.get() == BTT_OK) {
			this.activeGallery.deleteSlide();
			this.getCurrentSlideFromGallery();
		} else {
			//NOP
		}
		this.updateCurrentSlide();
	}	
	
	@FXML
	public void handleReloadFromDraft() {
		stopAllThreads();
		this.currentSlide.annotation.reloadFinal();
		this.updateCurrentSlide();
		
	}
	
	@FXML
	public void handleRefineText() {
		stopAllThreads();
		String text = new String(this.HTMLE_Annotation_Refine.getHtmlText());
			
		char terminator = ' ';
		
		if(text.length() > 1){
			terminator = text.charAt(text.lastIndexOf("")-1);
		} else {
			terminator = '\n';
		}
		
		if(terminator == '\r' || terminator == '\n'){
			this.currentSlide.annotation.finalTextSet(text);
		} else {
			this.currentSlide.annotation.finalTextSet(text + "\r\n");
		}
	}
	
	@FXML
	public void handleForward_View() {
		stopAllThreads();
		if(this.imageCaptureThread != null){
			this.imageCaptureThread.interrupt();
		}
		this.returnCurrentSlideToGallery();
		this.mainApp.setSysMessageLabel(this.activeGallery.nextSlide(false));
		this.updateCurrentSlide(); // Keep at the end of the method!#
		this.updateUI();
	}
	
	@FXML
	public void handleBack_View() {
		stopAllThreads();
		if(this.imageCaptureThread != null){
			this.imageCaptureThread.interrupt();
		}
		this.returnCurrentSlideToGallery();
		this.mainApp.setSysMessageLabel(this.activeGallery.prevSlide());
	//	System.out.println("\n\rGBC.handleBackView()->Calling updateCurrentSlide()...");
		this.updateCurrentSlide(); // Keep at the end of the method!
	//	System.out.println("\n\rupdateCurrentSlide() has returned");
		this.updateUI();
	}

	public void updateCurrentSlide() {

		if(this.activeGallery != null){
			this.inlineSix.getHistory().getEntries().clear();
			this.inlineSix.loadContent("");
			this.inlineSix.reload();
			this.audioPlayerCTRL.disableGUI(true);
		//	System.out.println("\n\r GBC.updateCurrentSlide() -> Method Start.");
			this.getCurrentSlideFromGallery();
			
			// Update acc. to current slide class:
			if(this.currentSlide.getClass().equals(GallerySlide.class)){
				this.IVBoxCTRL.setPicture(((GallerySlide)this.currentSlide).getPicture());
				if(((GallerySlide)this.currentSlide).getAudioDraft() != null){
					this.audioPlayerCTRL.disableGUI(false);
					this.audioPlayerCTRL.setAudioClip(((GallerySlide)this.currentSlide).getAudioDraft());
				} else {
					this.audioPlayerCTRL.deleteAudio();
				}
			//	System.out.println("\n\r GBC.updateCurrentSlide() -> Got data for IVBox.");
				this.tagUtilsCTRL.TA_ToolTags.setText(((GallerySlide) this.currentSlide).getTools());
				this.tagUtilsCTRL.TA_PartTags.setText(((GallerySlide) this.currentSlide).getParts());
				this.tagUtilsCTRL.TA_InstructionTags.setText(((GallerySlide) this.currentSlide).getTags());
			//	System.out.println("\n\r GBC.updateCurrentItem() -> Got data for TagUtils.");
				
			} else if(this.currentSlide.getClass().equals(GallerySafetySlide.class)){
			//	System.out.println("\n\r GBC.updateCurrentItem() -> currentSlide == GallerySafetySlide");
				this.IVBoxCTRL.setPicture(((GallerySafetySlide) this.currentSlide).getPicture());
				this.tagUtilsCTRL.TA_InstructionTags.setText(((GallerySlide) this.currentSlide).getTags());
				this.audioPlayerCTRL.setAudioClip(null);
				this.audioPlayerCTRL.setAudioClip(null);
				
			} else if (this.currentSlide instanceof GalleryChapter) {
			    System.out.println("\n\r GBC.updateCurrentItem() -> currentSlide instanceof GalleryChapter");
				//GalleryTitle and GalleryChapter are handled the same 
				//from the GalleryBrowserController's perspective,
				//because their info is wrapped in the annotation

				this.IVBoxCTRL.setPicture(null);
				this.audioPlayerCTRL.setAudioClip(null);
				this.tagUtilsCTRL.TA_InstructionTags.setText(this.currentSlide.getTags());
				this.audioPlayerCTRL.setAudioClip(null);
			} else {
				this.IVBoxCTRL.setPicture(null);
				this.audioPlayerCTRL.setAudioClip(null);
				this.tagUtilsCTRL.TA_InstructionTags.setText(this.currentSlide.getTags());
				this.audioPlayerCTRL.setAudioClip(null);
			}
			
			this.TA_Annotation_Draft.setText(this.currentSlide.annotation.draftTextGet());
			this.HTMLE_Annotation_Refine.cacheProperty().set(false);
			

			this.HTMLE_Annotation_Refine.setHtmlText(this.currentSlide.annotation.finalTextGet());
			if(this.currentSlide.getSlideTitle() == null || this.currentSlide.getSlideTitle().isEmpty()){
				this.TP_Slide_Draft.setText("Click to edit title...");
			} else {
				this.TP_Slide_Draft.setText(this.currentSlide.getSlideTitle());			
			}

			this.WV_Annotation_View.getEngine().loadContent(this.currentSlide.annotation.finalTextGet().replace("contenteditable=\"true\"", "contenteditable=\"false\""));
			this.activeGallery.updateOrdinals();
			this.updateSlideCountLabel();
			this.setDividers(currentSlide);
		}
	}



	public void threadInit() {
		if(this.imageCaptureThread == null) {
		//	System.out.println("\n\rInitializing ImageCaptureThread...");
			this.imageCaptureThread = new Thread(new CamRunnable());
	 		this.imageCaptureThread.setName("CamRunnable");
	 		this.imageCaptureThread.setDaemon(true);
	 		this.imageCaptureThread.start();
		} 		
	}

	public boolean threadInterrupted() {
		if(this.imageCaptureThread != null) {
			return imageCaptureThread.isInterrupted();
		} else {
			return true;
		}
	}
	@Override
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		
		if(check == false){
		//	System.out.println("\n\rGalleryBrowserController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
	
//	/**
//	 * Waits until the CamRunnable finished its cycle and returns.
//	 */
//	public void waitUntilSafe(){
//		GalleryBrowserController.this.captureCamOn = false;
//		while(this.captureThreadSafe) {
//			
//		}
//	}
	
	/**
	 * Runnable for capturing images in separate thread: 
	 */
	public class CamRunnable implements Runnable {
		Image capturedImage = null;
		long sleeptime = GalleryBrowserController.this.AppSettingsSingleton.getCamRefreshRate();

		
		@Override
		public void run() {
			synchronized(GalleryBrowserController.this.IVBoxCTRL){
				GalleryBrowserController.this.IVBoxCTRL.enableAbortBtn(true);		
			}
			try {
				while(!Thread.currentThread().isInterrupted()) {
					GalleryBrowserController.this.captureCamOn = true;
//						System.out.println("Cam Runnable: synced on camera");
					synchronized(GalleryBrowserController.this){ 
						synchronized(GalleryBrowserController.this.IVBoxCTRL.IV_Illustration){
							synchronized(GalleryBrowserController.this.camera) {
//								System.out.println("Cam Runnable: synced on IVboxCTRL.IV_Illustration");
								GalleryBrowserController.this.IVBoxCTRL.IV_Illustration.setImage(GalleryBrowserController.this.camera.captureCam());
//								System.out.printf("n\rCam.setResolution(): The new Camera Resolution is: " + GalleryBrowserController.this.camera.getResolution().width +"x" + GalleryBrowserController.this.camera.getResolution().height + "\n\r");						
							}
						}
					}
//					System.out.println("Cam Runnable cycle done. Sleeping...");
					Thread.sleep(sleeptime);
				}
			} catch (InterruptedException e) {
//				// NOP 
			}
			synchronized(GalleryBrowserController.this.IVBoxCTRL){
				GalleryBrowserController.this.IVBoxCTRL.enableAbortBtn(false);		
			}
			GalleryBrowserController.this.captureCamOn = false;
			
		}
	}


}
