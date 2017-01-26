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

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import gallery.abstractclasses.ControllerAbstr;
import gallery.model.GalleryProjectSettings;
import gallery.utils.HTMLMaker;
import gallery.utils.PathUtils;

public class RootLayoutController extends ControllerAbstr{

	@FXML
	private Label LBL_SysMessage;
	
	@FXML
	private MenuItem MI_Export;
	
	@FXML
	private MenuItem MI_BOM;
	
	@FXML
	private MenuItem MI_ToolBox;
	
	@FXML
	private MenuItem MI_Save;
	
	@FXML
	private MenuItem MI_New;
	
	@FXML
	private MenuItem MI_Load;
	
	@FXML
	private MenuItem MI_ProjectSettings;


	// Reference to the main application
	private Stage myPrimaryStage = null;
	private GalleryBrowserController galleryBrowserCTRL = null;
	private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE; 
	public ItemListController toolBoxCTRL = null;
	public ItemListController partsListCTRL = null;

	
	/** 
	 * Handles save / discard changes popup dialog
	 * 
	 * Returns true if gallery should close
	 * Save Button: true
	 * Discard Button: true
	 * Cancel button: false
	 */
	 public boolean saveOrDiscard( )  {
		boolean closeGallery = true;
		if(		this.galleryBrowserCTRL.activeGallery != null &&
				this.galleryBrowserCTRL.activeGallery.getCurrentItem().isInitialized()) {
			if(this.galleryBrowserCTRL.activeGallery.getSlideCount() > 0 && this.galleryBrowserCTRL.activeGallery.galleryInitialized()){
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Save before closing?");
				alert.setHeaderText("You are about to close your current project and discard any unsaved changes.");
				alert.setContentText("Would you like to save before closing?");
				
				ButtonType buttonSave = new ButtonType("Save"); 
				ButtonType buttonContinue = new ButtonType("Discard Changes");
				ButtonType buttonCancel = new ButtonType("Cancel");
				
				alert.getButtonTypes().setAll(buttonSave, buttonContinue, buttonCancel);
				
				Optional<ButtonType> result = alert.showAndWait();
				
				if(result.get() == buttonSave) {
					this.handleSave();
					closeGallery = true;
						
				} else if(result.get() == buttonCancel){
					closeGallery = false;
				} else {
					closeGallery = true;
				}
			}
		}
		return closeGallery;
	}
	

	/**
	 * Initializes the controller. 
	 *
	 */
	@Override
	public void initCTRL() {
		System.out.println("\r\nRootLayoutController.initCTRL() -> Initializing Controller...");
		try {
			System.out.println("\r\nRootLayoutController.initCTRL() -> Trying to load Settings:");
			this.AppSettingsSingleton.loadSettings();
			System.out.println("\r\nRootLayoutController.initCTRL() -> Successfully loaded Settings");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
	    	Alert alert = new Alert(AlertType.WARNING);
	    	alert.setTitle("WARNING: Settings could not be loaded on instantiation!");
	    	alert.setHeaderText("Settings file not readable!");
	    	alert.setContentText(	"Default settings will be loaded and saved to \n"
	    					+ 		"replace the missing or corrupted file.");
	    	alert.showAndWait();
	    	this.AppSettingsSingleton.init();
		}
		
		MI_Save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		MI_New.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		MI_Load.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		MI_BOM.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		MI_ToolBox.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
		
		
		
		this.setSysMessage("Please load an existing documentation project or create a new one.");
		System.out.println("\r\nRootLayoutController.initCTRL() -> Controller initialized.");
	}
	
	
	/**
	 * Used for giving a reference to the stage owning the RootLayout
	 */
	public void setPrimaryStage(Stage nPrimaryStage) {
		this.myPrimaryStage = nPrimaryStage;
	}
	
	/**
	 * Used for updating the gallery window
	 */
	public void setGalleryCtrl(GalleryBrowserController ngbc) {
		this.galleryBrowserCTRL = ngbc;
	}
	
	/**
	 * Set text on the System Message Label on the bottom of the frame
	 */
	public void setSysMessage(String nMsg) {
		if(nMsg != null) {
			this.LBL_SysMessage.setText(nMsg);
		} else {
			this.LBL_SysMessage.setText("");
		}
	}
	
	/**
	 * Creates a new Gallery
	 */
	
	@FXML 
	public void handleNewGallery() {
		boolean makeNew = false;
		this.mainApp.showProjSettings();
		if(		this.galleryBrowserCTRL.activeGallery.getSlideCount() >= 1 && 
				this.galleryBrowserCTRL.activeGallery.getCurrentItem().isInitialized()) {
			this.galleryBrowserCTRL.stopAllThreads();
			makeNew = saveOrDiscard();
		} else if(this.mainApp.projectSettingsSaved()){
			makeNew = true;
		}
		
		if(makeNew == true){
			//this.mainApp.showProjSettings();
			this.galleryBrowserCTRL.activeGallery.newGallery(false);
			this.galleryBrowserCTRL.activeGallery.init();
			this.mainApp.showGalleryBrowser();
			this.galleryBrowserCTRL.showTab("draft");
		   
			this.setSysMessage("New Gallery created.");
			this.MI_Export.disableProperty().set(false);
			this.MI_BOM.disableProperty().set(false);
			this.MI_ToolBox.disableProperty().set(false);
			this.MI_ProjectSettings.disableProperty().set(false);
		}	
	}
	
	/**
	 * Opens a FileChooser to let the user select a gallery to load
	 * @throws IOException 
	 */
	@FXML
	public void handleLoadGallery() throws IOException {
		this.galleryBrowserCTRL.stopAllThreads();
		
		boolean close = true;
		if(this.galleryBrowserCTRL.activeGallery.getCurrentItem().isInitialized()){
			close = this.saveOrDiscard();
		}
		
		if(close == true){
			this.LBL_SysMessage.setText("Loading Project... Please be patient, this may take a while...");
			// TODO: Make default dir platform independent in next release!
			System.out.println("\n\rcreating new Gallery()...");
			this.galleryBrowserCTRL.activeGallery.newGallery(false);
			System.out.println("\n\rupdating GPSSingleton...");
			this.GPSSingleton.updateSettings();
			System.out.println("\n\rsetting GBC to safe state...");
			System.out.println("\n\rLoading...");
		
			DirectoryChooser chooser = new DirectoryChooser();
			File directory = null;
			chooser.setTitle("Load from project directory...");
			if(this.GPSSingleton.getDefaultSavePath() == this.GPSSingleton.getSavePath()) {
				directory = new File(this.GPSSingleton.getDefaultSavePath());
			    if (!directory.exists()) {
			        if (directory.mkdirs()) {
			            System.out.println("\r\nRootLayoutController.handleLoadGallery() -> sub directories created successfully");
			        } else {
			            System.out.println("\r\nRootLayoutController.handleLoadGallery() -> failed to create sub directories");
			            this.setSysMessage("ERROR: Could not create default project directory!");
			        }
			    }
			} else {
				directory = new File(this.GPSSingleton.getSavePath());
			}
			chooser.setInitialDirectory(directory);
			File selectedDirectory = chooser.showDialog(this.myPrimaryStage);
			if(!(selectedDirectory == null)) {
				this.GPSSingleton.setSavePath(selectedDirectory.getAbsolutePath()+"\\");
				if(new File(selectedDirectory.toString() + "\\Resources\\manifest.txt").exists()){
					this.GPSSingleton.updateSettings();
					try {
						this.GPSSingleton.loadSettings();
						this.GPSSingleton.setSavePath(selectedDirectory.getAbsolutePath()+"\\");
						this.GPSSingleton.saveSettings();
					} catch (IOException e) {
				    	Alert alert = new Alert(AlertType.WARNING);
				    	alert.setTitle("WARNING: Project settings could not be loaded!");
				    	alert.setHeaderText("Project settings not loaded!");
				    	alert.setContentText(	"Default settings will be loaded and saved to \n"
				    					+ 		"replace the missing or corrupted file. \n\r"
				    					+ 		"Recovery of the critical project data (slide data) will be attempted, \n"
				    					+ 		"The Meta-Information (Project title, product info, etc.) \n"
				    					+ 		"will have to be added again, manually.");
				    	alert.showAndWait();
				    	this.GPSSingleton.init();
				    	this.GPSSingleton.setSavePath(selectedDirectory.getAbsolutePath()+"\\");
				    	List<String> slideCount = PathUtils.textFileLoadLines(new File(this.GPSSingleton.getResourcePath() + "\\manifest.txt"));
				    	this.GPSSingleton.setSlideCount(slideCount.size());
				    	this.GPSSingleton.saveSettings();
				 
					}
					this.galleryBrowserCTRL.activeGallery.loadGallery();
					this.galleryBrowserCTRL.activeGallery.setCurrentIndex(0);
					this.mainApp.showGalleryBrowser();
					this.galleryBrowserCTRL.showTab("view");
					this.galleryBrowserCTRL.updateCurrentSlide();
					this.galleryBrowserCTRL.updateUI();
					this.setSysMessage("Loaded Project at "+selectedDirectory.getAbsolutePath());
					
					try {
						this.galleryBrowserCTRL.updateCurrentSlide();
					} catch (java.lang.IndexOutOfBoundsException e) {
						e.printStackTrace();
					} catch (NullPointerException ne) {
						ne.printStackTrace();
					}
					
					this.MI_Export.disableProperty().set(false);
					this.MI_BOM.disableProperty().set(false);
					this.MI_ToolBox.disableProperty().set(false);
					this.MI_ProjectSettings.disableProperty().set(false);
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("No project found!");
					alert.setHeaderText("No project found in selected directory!");
					alert.setContentText("Please select a directory which contains a valid documentation project.");
					
					alert.showAndWait();
					this.MI_Export.disableProperty().set(true);
					this.setSysMessage("No manifest file found in directory. Could not load project.");
				}
			} else {
				this.mainApp.showStartupWindow();
				this.MI_ProjectSettings.disableProperty().set(true);
				this.MI_Export.disableProperty().set(true);
			}
		}
	}
	
	/**
	 * Saves the files to the currently opened gallery. If there is no open file, the "save as" dialog is shown.
	 */
	@FXML
	public void handleSave() {
		this.galleryBrowserCTRL.stopAllThreads();
		this.galleryBrowserCTRL.returnCurrentSlideToGallery();
		this.LBL_SysMessage.setText("Saving Project... Please be patient, this may take a while...");
		File directory = null;
		boolean save = true;
		if(this.GPSSingleton.getReqInstr()){
			for (int i = 0; i < this.GPSSingleton.getSlideCount(); i++) {
				save = save && this.galleryBrowserCTRL.activeGallery.getGallerySlides().get(i).isInitialized();
			}
		}

		if(this.GPSSingleton.getReqTitle()){
			for (int i = 0; i < this.GPSSingleton.getSlideCount(); i++) {
				save = save && !(this.galleryBrowserCTRL.activeGallery.getGallerySlides().get(i).getSlideTitle() != null);
			}
		}
		
		if(save) {
			if(this.GPSSingleton.isCustomPathSet()) {
					File resourceFile = new File(this.GPSSingleton.getResourcePath());
					directory = new File(this.GPSSingleton.getSavePath());
		
					if(directory.exists()){
						PathUtils.deleteNumberedFiles(resourceFile,"GalleryItem§.txt" ,"§");
						PathUtils.deleteNumberedFiles(resourceFile,"slide_§.png" ,"§", 0, this.galleryBrowserCTRL.activeGallery.getSlideCount());
						PathUtils.deleteNumberedFiles(resourceFile,"timeStamps§.txt" ,"§", 0, this.galleryBrowserCTRL.activeGallery.getSlideCount());
						PathUtils.deleteNumberedFiles(resourceFile, "slide_§_#.png", "§", "#", 0, this.galleryBrowserCTRL.activeGallery.getSlideCount());
						try {
							Files.delete(new File(resourceFile.toPath() + "\\manifest.txt").toPath());
						} catch(NoSuchFileException nsfe) {
							// ignore
						} catch (IOException e) {
					    	Alert alert = new Alert(AlertType.ERROR);
					    	alert.setTitle("ERROR: File could not be deleted!");
					    	alert.setHeaderText("Manifest file could not be deleted!");
					    	alert.setContentText(	"The project structure may be corrupted. \n\r");
					    	alert.showAndWait();
							e.printStackTrace();
						} 
					}

//					if(FileUtils.deleteQuietly(directory)){
//						System.out.println("\r\nRootLayoutController.handleSave() -> Project directory cleared successfully");
//					}
					// Create empty dir at custom savepath
			        if (directory.exists()){
			        	this.LBL_SysMessage.setText("Saving Project... Please be patient, this may take a while...");
			            System.out.println("\r\nRootLayoutController.handleSave() -> Save directory exists");
			            this.galleryBrowserCTRL.activeGallery.saveGallery();
			            this.setSysMessage("Saved Project at " + directory.getAbsolutePath());
			            this.galleryBrowserCTRL.setSaved();
			        } else if(directory.mkdirs()) {
			        	this.LBL_SysMessage.setText("Saving Project... Please be patient, this may take a while...");
			            System.out.println("\r\nRootLayoutController.handleSave() -> sub directories created successfully");
			            this.galleryBrowserCTRL.activeGallery.saveGallery();
			            this.setSysMessage("Saved Project at " + directory.getAbsolutePath());
			        } else {
			        	this.LBL_SysMessage.setText("Project could not be saved.");
			            System.out.println("\r\nRootLayoutController.handleSave() -> failed to create sub directories");
			        }

			} else {
					this.handleSaveAs();
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Saving Failed!");
			alert.setHeaderText("The Project contains uninitialized Slides.");
			alert.setContentText("Please make sure every slide has an image attached to it.");
			alert.showAndWait();
			this.LBL_SysMessage.setText("Project could not be saved.");
		}	
	}
	
	/**
	 * Opens FileChooser to let the user select a file to save to.
	 */
	@FXML
	public void handleSaveAs() {
		boolean saveNow = false;
		boolean noNulls = true;
		DirectoryChooser chooser = new DirectoryChooser();
		File directory = null;
		File dirbackup = null;
		this.galleryBrowserCTRL.stopAllThreads();
		this.galleryBrowserCTRL.returnCurrentSlideToGallery();
		String oldSafetyFiles = this.GPSSingleton.getSafetyDataPath();
		chooser.setTitle("Save As...");
		// Custom path set?
		if(this.GPSSingleton.isCustomPathSet()) {
			directory = new File(this.GPSSingleton.getSavePath());
		} else {
			directory = new File(this.GPSSingleton.getDefaultSavePath());
		}
		
		if(!directory.exists()) {
			try{
				directory.mkdirs();
			} catch (SecurityException se){
				se.printStackTrace();				
			}
		}
		// Open Chooser
    	chooser.setInitialDirectory(directory);
		File selectedDirectory = chooser.showDialog(this.myPrimaryStage);
		this.setSysMessage("Saving Project... Please be patient, this may take a while...");
		for (int i = 0; i < this.galleryBrowserCTRL.activeGallery.getSlideCount(); i++) {
			noNulls = this.galleryBrowserCTRL.activeGallery.getGallerySlides().get(i).isInitialized();
		}
		
		if(noNulls) {
			// Is selected project dir NOT EMPTY?
			if( !( selectedDirectory == null ) && ( selectedDirectory.listFiles().length > 0 ) ) {
				//Alert: Overwrite?
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Overwrite existing directory?");
				alert.setHeaderText("A directory already exists at this location.");
				alert.setContentText("Do you wish to delete all existing files in the folder and save the current project there?");
				
				ButtonType buttonYes = new ButtonType("Yes"); 
				ButtonType buttonCancel = new ButtonType("Cancel");
				alert.getButtonTypes().setAll(buttonYes, buttonCancel);
				Optional<ButtonType> result = alert.showAndWait();
				dirbackup = selectedDirectory;
				if(result.get() == buttonYes) {
					// Delete folder at chosen path
					if(FileUtils.deleteQuietly(selectedDirectory)){
						System.out.println("\r\nRootLayoutController.handleSaveAs() -> Project directory cleared successfully");
						selectedDirectory = dirbackup;
					}
					
				} else {
					//Alert: Project was not saved
					Alert alertAbort = new Alert(AlertType.INFORMATION);
					alertAbort.setTitle("Save As... Aborted");
					alertAbort.setHeaderText("The saving procedure was aborted.");
					alertAbort.setContentText("No project data was saved.");
					alertAbort.showAndWait();
					return;
				}
				// Create empty dir at chosen path
		        if (selectedDirectory.mkdirs()) {
		            System.out.println("\r\nRootLayoutController.handleSaveAs() -> sub directories created successfully");
		            saveNow = true;
		        } else {
		            System.out.println("\r\nRootLayoutController.handleSaveAs() -> failed to create sub directories");
		            saveNow = false;
		        }
			} else {
				saveNow = true;
			}
		} else {
			saveNow = false;
	

		}
		if(!(selectedDirectory == null) && saveNow == true){
			
			String oldPath = this.GPSSingleton.getResourcePath();
			this.GPSSingleton.setSavePath(selectedDirectory.getAbsolutePath()+"\\");
			//Move audio Files to new path: 
			for(Integer i = 0; i < this.galleryBrowserCTRL.activeGallery.getSlideCount(); i++){
				try {
					File resDir = new File(selectedDirectory.toPath() + "\\Resources\\");
					resDir.mkdirs();
					File tmpSource = new File(oldPath + "audio" + i.toString() + ".wav");
					File tmpTarget = new File(GPSSingleton.getResourcePath() + "audio" + i.toString() + ".wav");
					if(tmpSource.exists() && tmpSource.isFile() && tmpSource.canRead()) {
						Files.copy(	Paths.get(tmpSource.toString()),
									Paths.get(tmpTarget.toString()),
									StandardCopyOption.REPLACE_EXISTING);
					}
							
					
				} catch (IOException e) {
			    	Alert alert = new Alert(AlertType.ERROR);
			    	alert.setTitle("ERROR: Project could not be saved to new location!");
			    	alert.setHeaderText("Saving failed!");
			    	alert.setContentText(	"Some or all of the files could not be saved at the new location. \n"
			    					+ 		"The project structure at the new Location may be corrupted.");
			    	alert.showAndWait();
					e.printStackTrace();
				}
		
			}
			
		    File tmpSource = new File(oldSafetyFiles);
			File tmpTarget = new File(selectedDirectory + "\\Resources\\SafetyData");
			if(tmpSource.exists() && tmpSource.isDirectory() && tmpSource.canRead()) {
				try {
					FileUtils.copyDirectory(tmpSource,
											tmpTarget);
				} catch (IOException e) {
			    	Alert alert = new Alert(AlertType.ERROR);
			    	alert.setTitle("ERROR: Pictograms could not be saved to new location!");
			    	alert.setHeaderText("Saving failed!");
			    	alert.setContentText(	"Some or all of the files could not be saved at the new location. \n"
			    					+ 		"The project structure at the new Location may be corrupted.");
			    	alert.showAndWait();
					e.printStackTrace();
				}
			} else {
				System.out.println("\n\rNo safety data found in current project.");
			}
		
			this.galleryBrowserCTRL.activeGallery.saveGallery();
			this.setSysMessage("Saved Project at " + selectedDirectory.getAbsolutePath());
			this.galleryBrowserCTRL.setSaved();
		} else {
			this.LBL_SysMessage.setText("Project could not be saved.");
		}
	}
	
	/**
	 * Handles the export of a Gallery to e.g. PDF or HTML
	 */
	@FXML
	public void handleExport() {
		boolean saveNow = false;
		String fileName = null;
		DirectoryChooser chooser = new DirectoryChooser();
		File directory = null;
		File newFile = null;
		String fileType = ".html";
		this.galleryBrowserCTRL.stopAllThreads();
		chooser.setTitle("Export to...");
		// Custom path set?
		if(this.GPSSingleton.isCustomPathSet()) {
			directory = new File(this.GPSSingleton.getSavePath());
		} else {
			directory = new File(this.GPSSingleton.getDefaultSavePath());
		}
		
		if(!directory.exists()) {
			try{
				directory.mkdirs();
			} catch (SecurityException se){
				se.printStackTrace();				
			}
		}
		// Open Chooser
    	chooser.setInitialDirectory(directory);
		File selectedDirectory = chooser.showDialog(this.myPrimaryStage);
		if(selectedDirectory != null && selectedDirectory.exists() && selectedDirectory.isDirectory()){
			selectedDirectory = new File(selectedDirectory.toPath() + "\\" + this.GPSSingleton.getProjTitle().trim() + "Printable_HTML");
			newFile = new File(selectedDirectory.getPath() + "\\" + this.GPSSingleton.getProjTitle().trim() + fileType);
			fileName =  this.GPSSingleton.getProjTitle().trim() + fileType;
			// Is selected project dir NOT EMPTY?
			if( !( selectedDirectory == null ) && ( newFile.exists() ) ) {
				
				Alert alert = new Alert(AlertType.CONFIRMATION);		
				alert.setTitle("File already exists!");
				alert.setHeaderText("A file with this name already exists at this location.");
				alert.setContentText("Click OK to overwrite the selected file.");
			
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get().equals(ButtonType.OK)){
					if(newFile.exists()){
						for(	Integer i = 0;																// Iterate until a filename is found which does not yet exist 
								(newFile = new File(selectedDirectory.getPath() + "\\" + this.GPSSingleton.getProjTitle().trim() + "_"+ i + fileType)).exists();
								i++){
							fileName = this.GPSSingleton.getProjTitle().trim() + "_" + i + fileType; 
							selectedDirectory = new File(selectedDirectory.toPath() + "_0" + i);
						};
					}
					
			        if (selectedDirectory.mkdirs() || selectedDirectory.exists()) { 					// Create empty dir at chosen path
			            System.out.println("\r\nRootLayoutController.handleExport() -> sub directories created successfully");
			            saveNow = true;
			        } else {
			            System.out.println("\r\nRootLayoutController.handleExport() -> failed to create sub directories");
			            saveNow = false;
			        }
				} else {
					saveNow = false;
				}
		
			} else {
				saveNow = true;
			}
		}
		if(saveNow == true){
			System.out.println("\r\nRootLayoutController.handleExport: -> Exporting to " + newFile.getAbsolutePath());
			if(fileType.contains(".pdf")){
			
			} else if(fileType.contains(".html")){
				try {
					this.galleryBrowserCTRL.returnCurrentSlideToGallery();
					this.setSysMessage("Project Successfully Exported:" + HTMLMaker.ExportSlidesToHTML_Printable(this.galleryBrowserCTRL.activeGallery, selectedDirectory, fileName));
				} catch (IOException e) {
			    	Alert alert = new Alert(AlertType.ERROR);
			    	alert.setTitle("ERROR: Export failed!");
			    	alert.setHeaderText("Error while Exporting to HTML!");
			    	alert.setContentText(	"Please make sure the application is allowed to write to \n"
			    					+ 		"the directory your have selected. ");
			    	alert.showAndWait();
					e.printStackTrace();
				}
			}
				
			
		}
	}
	
	
	/**
	 * Closes a gallery project, returns to the startscreen
	 */
	@FXML
	public void handleCloseGallery(){
		boolean close = false;
		if(this.galleryBrowserCTRL.activeGallery.getCurrentItem().isInitialized()){
			close = this.saveOrDiscard();
		}
	
		if(close == true){
			this.galleryBrowserCTRL.activeGallery.newGallery(false);
			//this.galleryBrowserCTRL.initCTRL();
			this.galleryBrowserCTRL.updateCurrentSlide();
			this.mainApp.showStartupWindow();
			this.MI_Export.disableProperty().set(true);
			this.MI_BOM.disableProperty().set(true);
			this.MI_ToolBox.disableProperty().set(true);
			this.MI_ProjectSettings.disableProperty().set(true);
			this.setSysMessage("Gallery Closed.");
		}
	}
	
	
	/**
	 * Closes the application.
	 */
	@FXML
	public void handleQuit() {
			System.exit(0);

	}
	
	/**
	 *  Opens the project-specific settings window
	 */
	@FXML 
	public void handleProjectSettings() {
		this.mainApp.showProjSettings();
	}
	
	
	/**
	 * Opens the application-specific settings window
	 */
	@FXML
	public void handleApplicationSettings() {
		this.mainApp.showAppSettings();
		this.galleryBrowserCTRL.stopAllThreads();
	}
	
	/**
	 * Compiles the Toolbox and opens it in a new window
	 * 
	 * Non Blocking.
	 */
	@FXML
	public void handleToolBox() {
		this.toolBoxCTRL.setItemList(this.galleryBrowserCTRL.activeGallery.getAllTools(), "Tools required for this project:");
		this.mainApp.showToolBox();
	}
	
	/**
	 * Compiles the Parts List and opens it in a new window
	 * 
	 * Non Blocking.
	 */
	@FXML
	public void handleBOM() {
		this.partsListCTRL.setItemList(this.galleryBrowserCTRL.activeGallery.getAllParts(), "Parts used / required for this project:");
		this.mainApp.showPartsList();
	}
	
	/**
	 * Opens an about dialog.
	 */
	@FXML
	public void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ManuMaker V1.0 - The Open Source Documentation Tool");
		alert.setHeaderText("About");
		alert.setContentText("Author: Daniel Lachmann \n"
				+ "ManuMaker V1.0 is an open-source tool for creating step-by-step manuals. \n\n\n"
				+ "Copyright (C) 2016 Daniel M. Lachmann\n\n"
				+ "To contact me via email, write to: ManuMaker.Dev@gmail.com \n\n"
				+ "This program is free software: you can redistribute it and/or modify "
				+ "it under the terms of the GNU General Public License as published by "
				+ "the Free Software Foundation, either version 3 of the License, or "
				+ "(at your option) any later version.\n\n"
				+ "This program is distributed in the hope that it will be useful, "
				+ "but WITHOUT ANY WARRANTY; without even the implied warranty of "
				+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  \n\nSee the\n"
				+ "GNU General Public License \nfor more details\n\n"
				+ "You should have received a copy of the GNU General Public License "
				+ "along with this program.  If not, see \"http://www.gnu.org/licenses/\".");
	
		alert.setWidth(800);
		alert.showAndWait();
	}
	
	
	@FXML
	public void handleManual(){
		try {
			Alert alert = new Alert(AlertType.NONE);
			alert.setTitle("ManuMaker V1.0 - The Open Source Documentation Tool");
			alert.setHeaderText("Using the User Manual");
			alert.setContentText("Please choose which format the User Manual should be loaded. "
					+ "Click \"PDF\" to opan the PDF version (with bookmarks) of the ManuMaker user manual in your default PDF-viewer.\n\n"
					+ "Click \"HTML to view the exported HTML Manual which can only be searched via your browser's CTRL+F text search function.\n\n"
					+ " "
					+ "Click \"ManuMaker Format\" to open the native help in this instance of ManuMaker.\n"
					+ "The current state of any open project will be saved, but it will not be available in parallel with the manual.\n\n"
					+ "If you wish to do so while keeping your current project open, please start a new instance of the ManuMaker application and open the native manual from there.");
		
			ButtonType btPDF = new ButtonType("PDF");
			ButtonType btHTML = new ButtonType("HTML");
			ButtonType btMM = new ButtonType("ManuMaker Format");

			alert.getButtonTypes().setAll(btPDF, btHTML, btMM);
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.isPresent()) {
				if(result.get().equals(btPDF)){
					Desktop.getDesktop().open(new File("README\\ManuMaker_User_Manual.pdf").getCanonicalFile());
				} else if(result.get().equals(btHTML)){
					Desktop.getDesktop().open(new File("README\\ManuMakerUserManualHTML\\ManuMaker User Manual.html").getCanonicalFile());										
				} else if(result.get().equals(btMM)) {
					if(this.GPSSingleton.isCustomPathSet()) {
						this.handleSave();
					}
					this.openNativeHelp();
				}
			}
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: Help file not found!");
			alert.setHeaderText("ERROR");
			alert.setContentText("The help file could not be opened. \n"
					+ "It should be located in ManuMaker's installation directory in the README folder.\n"
					+ "Alternatively, open the README folder with the ManuMaker Application to \n"
					+ "view the full User Manual.");
		
			alert.setWidth(800);
			alert.showAndWait();
			e.printStackTrace();
		}
	}

	
	
	private void openNativeHelp() throws IOException  {
		this.galleryBrowserCTRL.stopAllThreads();
		
		boolean close = true;
		
		if(close == true){
			this.LBL_SysMessage.setText("Loading Project... Please be patient, this may take a while...");
			// TODO: Make default dir platform independent in next release!
			System.out.println("\n\rcreating new Gallery()...");
			this.galleryBrowserCTRL.activeGallery.newGallery(false);
			System.out.println("\n\rupdating GPSSingleton...");
			this.GPSSingleton.updateSettings();
			System.out.println("\n\rsetting GBC to safe state...");
			System.out.println("\n\rLoading...");
			File directory = new File("README\\").getCanonicalFile();
			if(!(directory == null)) {
				this.GPSSingleton.setSavePath(directory.toString()+"\\");
				if(new File(this.GPSSingleton.getSavePath() + "\\Resources\\manifest.txt").exists()){
					this.GPSSingleton.updateSettings();
					try {
						this.GPSSingleton.loadSettings();
					} catch (IOException e) {
				    	Alert alert = new Alert(AlertType.WARNING);
				    	alert.setTitle("WARNING: Project settings for the User Manual could not be loaded!");
				    	alert.setHeaderText("Project settings not loaded!");
				    	alert.setContentText(	"Default settings will be loaded and saved to \n"
				    					+ 		"replace the missing or corrupted file. \n\r"
				    					+ 		"Recovery of the critical project data (slide data) will be attempted, \n"
				    					+ 		"The Meta-Information (Project title, product info, etc.) \n"
				    					+ 		"will have to be added again, manually.\n\n"
				    					+ "If the problem persists, reinstall ManuMaker to get a fresh copy of the UserManual project.");
				    	alert.showAndWait();
				    	this.GPSSingleton.init();
				    	this.GPSSingleton.setSavePath("README\\");
				    	List<String> slideCount = PathUtils.textFileLoadLines(new File(this.GPSSingleton.getResourcePath() + "\\manifest.txt"));
				    	this.GPSSingleton.setSlideCount(slideCount.size());
				    	this.GPSSingleton.saveSettings();
				 
					}
					this.galleryBrowserCTRL.activeGallery.loadGallery();
					this.galleryBrowserCTRL.activeGallery.setCurrentIndex(0);
					this.mainApp.showGalleryBrowser();
					this.galleryBrowserCTRL.showTab("view");
					this.setSysMessage("Loaded Project at "+directory.getAbsolutePath());
					
					try {
						this.galleryBrowserCTRL.updateCurrentSlide();
					} catch (java.lang.IndexOutOfBoundsException e) {
						e.printStackTrace();
					} catch (NullPointerException ne) {
						ne.printStackTrace();
					}
					
					this.MI_Export.disableProperty().set(false);
					this.MI_BOM.disableProperty().set(false);
					this.MI_ToolBox.disableProperty().set(false);
					this.MI_ProjectSettings.disableProperty().set(true);
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("No project found!");
					alert.setHeaderText("No project found in selected directory!");
					alert.setContentText("Please select a directory which contains a valid documentation project.");
					
					alert.showAndWait();
					this.MI_Export.disableProperty().set(true);
					this.setSysMessage("No manifest file found in directory. Could not load project.");
				}
			} else {
				this.mainApp.showStartupWindow();
				this.MI_ProjectSettings.disableProperty().set(true);
				this.MI_Export.disableProperty().set(true);
			}
		}
	}
	
	/**
	 * Does nothing here...
	 * 
	 */
	@Override
	public void updateUI() {

		
	}


	@Override
	protected boolean invar(String text) {
		{
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		check = check & (this.galleryBrowserCTRL != null);
		
		if(check == false){
			System.out.println("\n\rRootLayoutController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
		}
	}
	


}
