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

package gallery;



import java.io.IOException;



import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import gallery.controller.*;
import gallery.model.ApplicationSettings;




/****
 * 
 *  [...]but also needs to avoid synchronizing invocations of other objects' methods. 
 *  (Invoking other objects' methods from synchronized code can create problems 
 *  that are described in the section on Liveness.)
 * @author Daniel Lachmann
 *
 */
public class Main extends Application {

	private Stage primaryStage; 
	private Stage appSettingsStage = null;
	private Stage projSettingsStage = null;
	private Stage toolBoxStage = null;
	private Stage partsListStage = null; 
	
	private BorderPane 	BP_RootLayout = null;
	private Scene 		rootLayoutScene = null;
	
	
	private String appName = "ManuMaker V1.0";
	
	protected RootLayoutController rootLayoutCTRL = null;
	private GalleryBrowserController galleryBrowserCTRL = null;
	private StartupWindowController startupWindowCTRL = null;
	private AppSettingsWindowController appSettingsWindowCTRL = null;
	private ProjSettingsWindowController projSettingsWindowCTRL = null;
	

	
	private AnchorPane AP_StartupWindow = null;
	private AnchorPane AP_GalleryBrowser = null;
	
	public ApplicationSettings AppSettingsSingleton = ApplicationSettings.INSTANCE;
	public boolean projectSettingsSaved() {
		return this.projSettingsWindowCTRL.savedBeforeClosing();
	}
	
	
	@Override
	public void start(Stage nPrimaryStage) {

		try{
			this.primaryStage = nPrimaryStage;
			this.primaryStage.setTitle(this.appName);
			this.primaryStage.getIcons().setAll(new Image(Main.class.getResourceAsStream("img/ManuMaker.png")));
			
			this.primaryStage.setFullScreen(false);
			this.primaryStage.setMaximized(true);
		    this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			       @Override
			       public void handle(WindowEvent e) {
			    	  if( rootLayoutCTRL != null){
			    		  if(rootLayoutCTRL.saveOrDiscard()){
			    			  Platform.exit();
					          System.exit(0);
			    		  } else {
			    			  e.consume();
			    			  //NOP
			    		  }
			    	  } 		          
			       }
			    });
			initControllers();
			showRootLayout();
			showStartupWindow();;


		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: Exception while loading application!");
			alert.setHeaderText(alert.titleProperty().get());
			alert.setContentText(	"Some files may be missing or corrupted. \n"
								+ 	"If this problem keeps occurring, please reinstall the application.");
			alert.showAndWait();
			e.printStackTrace();
		}
				
	}
	
	
	/**
	 * Closes the Application 
	 */
	@Override
	public void stop() {
		this.rootLayoutCTRL.handleQuit();
	}

	/**
	 * Loads all controllers and initializes them properly
	 */
	public void initControllers(){
		try {
		// INITIALIZE ROOT LAYOUT CONTROLLER: 
			FXMLLoader rootLayoutLoader = new FXMLLoader();
			rootLayoutLoader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			this.BP_RootLayout = (BorderPane) rootLayoutLoader.load();
			this.rootLayoutScene = new Scene(this.BP_RootLayout);
			//this.rootLayoutScene.getStylesheets().add(getClass().getResource("display.css").toExternalForm());

			this.rootLayoutCTRL = rootLayoutLoader.getController();
			this.primaryStage.setScene(this.rootLayoutScene);
			this.rootLayoutCTRL.setMainApp(this);
			this.rootLayoutCTRL.setPrimaryStage(this.primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
		// INITIALIZE STARTUP WINDOW:
			FXMLLoader startupWindowLoader = new FXMLLoader();
			startupWindowLoader.setLocation(Main.class.getResource("view/StartupWindow.fxml"));
			this.AP_StartupWindow = (AnchorPane) startupWindowLoader.load();
			this.startupWindowCTRL = startupWindowLoader.getController();
			this.startupWindowCTRL.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// INITIALIZE Project SETTINGS: 
		try {
			this.projSettingsStage = new Stage();
			FXMLLoader projSettingsLoader = new FXMLLoader();
			projSettingsLoader.setLocation(Main.class.getResource("view/ProjSettingsWindow.fxml"));
			AnchorPane basePane = (AnchorPane) projSettingsLoader.load();
			this.projSettingsStage.setTitle("Project Settings");
			this.projSettingsStage.initModality(Modality.WINDOW_MODAL);
			this.projSettingsStage.initOwner(this.primaryStage);
			Scene scene = new Scene(basePane);
			this.projSettingsStage.setScene(scene);
			// Select the controller
			this.projSettingsWindowCTRL = projSettingsLoader.getController();
			this.projSettingsWindowCTRL.setStage(this.projSettingsStage);
			this.projSettingsWindowCTRL.setMainApp(this);	
		} 		catch (IOException e) {
			e.printStackTrace();
		}
		try {
		// INITIALIZE GALLERY BROWSER: 
			FXMLLoader galleryBrowserLoader = new FXMLLoader();
			galleryBrowserLoader.setLocation(Main.class.getResource("view/GalleryBrowser.fxml"));
			this.AP_GalleryBrowser = (AnchorPane) galleryBrowserLoader.load();
			this.galleryBrowserCTRL = galleryBrowserLoader.getController();
			this.galleryBrowserCTRL.setMainApp(this);
			} catch (IOException e) {					e.printStackTrace();
			}
		// INITIALIZE APP SETTINGS: 
		try {
			this.appSettingsStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/AppSettingsWindow.fxml"));
			AnchorPane basePane = (AnchorPane) loader.load();
			this.appSettingsStage.setTitle("Application Settings");
			this.appSettingsStage.initModality(Modality.WINDOW_MODAL);
			this.appSettingsStage.initOwner(this.primaryStage);
			Scene scene = new Scene(basePane);
			this.appSettingsStage.setScene(scene);
			// Select the controller
			this.appSettingsWindowCTRL = loader.getController();
			this.appSettingsWindowCTRL.setStage(this.appSettingsStage);
			this.appSettingsWindowCTRL.setMainApp(this);
	
		} 		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			// INITIALIZE AudioPlayer CONTROLLER: 
				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing AudioPlayerController...");
				FXMLLoader audioPlayerLoader = new FXMLLoader();
				audioPlayerLoader.setLocation(Main.class.getResource("view/AudioPlayer.fxml"));
				this.galleryBrowserCTRL.AP_Audio = (AnchorPane) audioPlayerLoader.load();
				this.galleryBrowserCTRL.audioPlayerCTRL = audioPlayerLoader.getController();
				this.galleryBrowserCTRL.audioPlayerCTRL.setMainApp(this);
				this.galleryBrowserCTRL.audioPlayerCTRL.setOwningController(this.galleryBrowserCTRL);
			//	System.out.println("\n\r GBC.initCTRL(): AudioPlayer initialized.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
			// INITIALIZE IVBox CONTROLLER: 
				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing IVBoxController...");
				FXMLLoader IVBoxLoader = new FXMLLoader();
				IVBoxLoader.setLocation(Main.class.getResource("view/IVBox.fxml"));
				this.galleryBrowserCTRL.AP_IVBoxBase = (AnchorPane) IVBoxLoader.load();
				this.galleryBrowserCTRL.IVBoxCTRL = IVBoxLoader.getController();
				this.galleryBrowserCTRL.IVBoxCTRL.setMainApp(this);
				this.galleryBrowserCTRL.IVBoxCTRL.setRootPane(this.galleryBrowserCTRL.AP_IVBoxBase);
				

			//	System.out.println("\n\r GBC.initCTRL(): IVBox initialized.");			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
			// INITIALIZE SlideList CONTROLLER: 
				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing SlideListController...");
				FXMLLoader SlideListLoader = new FXMLLoader();
				SlideListLoader.setLocation(Main.class.getResource("view/SlideList.fxml"));
				this.galleryBrowserCTRL.AP_SlideListBase = (AnchorPane) SlideListLoader.load();
				this.galleryBrowserCTRL.slideListCTRL = SlideListLoader.getController();
				this.galleryBrowserCTRL.slideListCTRL.setMainApp(this);
				
			//	System.out.println("\n\r GBC.initCTRL(): SlideList initialized.");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
			// INITIALIZE TagUtils CONTROLLER: 
				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing TagUtilsController...");
				FXMLLoader TagUtilsLoader = new FXMLLoader();
				TagUtilsLoader.setLocation(Main.class.getResource("view/TagUtils.fxml"));		
				this.galleryBrowserCTRL.AP_TagUtilsBase = (AnchorPane) TagUtilsLoader.load();
				this.galleryBrowserCTRL.tagUtilsCTRL = TagUtilsLoader.getController();
				this.galleryBrowserCTRL.tagUtilsCTRL.setMainApp(this);
				this.galleryBrowserCTRL.tagUtilsCTRL.setEditable(true);
				
			//	System.out.println("\n\r GBC.initCTRL(): TagUtils initialized.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
			// INITIALIZE StopMotionTool CONTROLLER: 
				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> StopMotionToolController...");
				FXMLLoader SMTLoader = new FXMLLoader();
				SMTLoader.setLocation(Main.class.getResource("view/StopMotionTool.fxml"));
				this.galleryBrowserCTRL.AP_StopMotionBase = (AnchorPane) SMTLoader.load();
				this.galleryBrowserCTRL.smtCTRL = SMTLoader.getController();
				this.galleryBrowserCTRL.smtCTRL.setMainApp(this);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
			// INITIALIZE ChapterInputForm CONTROLLER: 
				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing ChapterInputController...");
				FXMLLoader CIFLoader = new FXMLLoader();
				CIFLoader.setLocation(Main.class.getResource("view/ChapterInputForm.fxml"));
				this.galleryBrowserCTRL.AP_ChapterInputFormBase = (AnchorPane) CIFLoader.load();
				this.galleryBrowserCTRL.cifCTRL = CIFLoader.getController();
				this.galleryBrowserCTRL.cifCTRL.setMainApp(this);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
			// INITIALIZE TableImportForm CONTROLLER: 
				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing TableImportFormController...");
				FXMLLoader TIFLoader = new FXMLLoader();
				TIFLoader.setLocation(Main.class.getResource("view/TableImportForm.fxml"));
				this.galleryBrowserCTRL.AP_TableImportFormBase = (AnchorPane) TIFLoader.load();
				this.galleryBrowserCTRL.tifCTRL = TIFLoader.getController();
				this.galleryBrowserCTRL.tifCTRL.setMainApp(this);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
			// INITIALIZE WarningLabelMaker CONTROLLER: 
				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing WarningLabelMakerController...");
				FXMLLoader WLMLoader = new FXMLLoader();
				WLMLoader.setLocation(Main.class.getResource("view/WarningLabelMaker.fxml"));		
				this.galleryBrowserCTRL.AP_WarningLabelMakerBase = (AnchorPane) WLMLoader.load();
				this.galleryBrowserCTRL.wlmCTRL = WLMLoader.getController();
				this.galleryBrowserCTRL.wlmCTRL.setMainApp(this);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			// INITIALIZE ToolBox CONTROLLER: 
			try {

				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing ToolBoxController...");
				this.toolBoxStage = new Stage();
				FXMLLoader toolBoxLoader = new FXMLLoader();
				toolBoxLoader.setLocation(Main.class.getResource("view/ItemList.fxml"));
				AnchorPane basePane = (AnchorPane) toolBoxLoader.load();
				this.toolBoxStage.setTitle("Required Toolbox Contents:");
				this.toolBoxStage.initModality(Modality.WINDOW_MODAL);
				this.toolBoxStage.initOwner(this.primaryStage);
				Scene scene = new Scene(basePane);
				this.toolBoxStage.setScene(scene);
				// Select the controller
				this.rootLayoutCTRL.toolBoxCTRL = toolBoxLoader.getController();
				this.rootLayoutCTRL.toolBoxCTRL.setStage(this.toolBoxStage);
				this.rootLayoutCTRL.toolBoxCTRL.setMainApp(this);	
			} 		catch (IOException e) {
				e.printStackTrace();
			}
			
			// INITIALIZE PartsList CONTROLLER: 
			try {

				System.out.println("\r\ngalleryBrowserCTRL.initCTRL() -> Initializing PartsListController...");
				this.partsListStage = new Stage();
				FXMLLoader partsListLoader = new FXMLLoader();
				partsListLoader.setLocation(Main.class.getResource("view/ItemList.fxml"));
				AnchorPane basePane = (AnchorPane) partsListLoader.load();
				this.partsListStage.setTitle("Parts Used:");
				this.partsListStage.initModality(Modality.WINDOW_MODAL);
				this.partsListStage.initOwner(this.primaryStage);
				Scene scene = new Scene(basePane);
				this.partsListStage.setScene(scene);
				// Select the controller
				this.rootLayoutCTRL.partsListCTRL = partsListLoader.getController();
				this.rootLayoutCTRL.partsListCTRL.setStage(this.partsListStage);
				this.rootLayoutCTRL.partsListCTRL.setMainApp(this);	
			} 		catch (IOException e) {
				e.printStackTrace();
			}

		
		// Give references between controllers where needed
	//	System.out.println("\r\nPassing references between Controllers...:");
		this.appSettingsWindowCTRL.setGalleryBrowserCTRL(this.galleryBrowserCTRL);
		this.projSettingsWindowCTRL.setGalleryBrowserCTRL(this.galleryBrowserCTRL);
		this.rootLayoutCTRL.setGalleryCtrl(this.galleryBrowserCTRL);
		this.startupWindowCTRL.setRootCtrl(this.rootLayoutCTRL);

		// Run initialization methods in for controllers: 
		this.rootLayoutCTRL.initCTRL();
		this.startupWindowCTRL.initCTRL();
		this.galleryBrowserCTRL.initCTRL();
		this.appSettingsWindowCTRL.initCTRL();
		this.projSettingsWindowCTRL.initCTRL();
		
		this.galleryBrowserCTRL.audioPlayerCTRL.initCTRL();

		this.galleryBrowserCTRL.IVBoxCTRL.setOwningController(this.galleryBrowserCTRL);
		this.galleryBrowserCTRL.IVBoxCTRL.initCTRL();
		this.galleryBrowserCTRL.slideListCTRL.setOwningController(this.galleryBrowserCTRL);
		this.galleryBrowserCTRL.slideListCTRL.initCTRL();
		this.galleryBrowserCTRL.tagUtilsCTRL.setOwningController(this.galleryBrowserCTRL);
		this.galleryBrowserCTRL.tagUtilsCTRL.initCTRL();
		this.galleryBrowserCTRL.smtCTRL.setOwningController(this.galleryBrowserCTRL);
		this.galleryBrowserCTRL.smtCTRL.setImageBoxController(this.galleryBrowserCTRL.IVBoxCTRL);
		this.galleryBrowserCTRL.smtCTRL.initCTRL();
		this.galleryBrowserCTRL.cifCTRL.setOwningController(this.galleryBrowserCTRL);
		this.galleryBrowserCTRL.cifCTRL.initCTRL();
		this.galleryBrowserCTRL.tifCTRL.setOwningController(this.galleryBrowserCTRL);
		this.galleryBrowserCTRL.tifCTRL.initCTRL();
		this.galleryBrowserCTRL.wlmCTRL.setOwningController(this.galleryBrowserCTRL);
		this.galleryBrowserCTRL.wlmCTRL.initCTRL();
		
		

		
		this.galleryBrowserCTRL.updateCurrentSlide();
		
	//	System.out.println("\r\nAll Controllers initialized.");
	}
	/**
	 * Presents the root layout.
	 * 
	 */
	public void showRootLayout() {
		this.primaryStage.show();
	}
	
	/**
	 * Loads & presents the gallery browser inside the root layout
	 * 
	 * @param args
	 */
	public void showGalleryBrowser() {
		this.BP_RootLayout.setCenter(this.AP_GalleryBrowser);	
		this.setSysMessageLabel("");
	}
	
	/**
	 * Loads & presents the gallery browser inside the root layout
	 * 
	 * @param args
	 */
	public void showStartupWindow() {
		this.BP_RootLayout.setCenter(this.AP_StartupWindow);
		this.rootLayoutCTRL.fitAPToParent(AP_StartupWindow);
		//this.rootLayoutCTRL.fitAPToParent(this.AP_StartupWindow);
	}
	
	/**
	 * Loads & presents the Application Settings
	 * @param args
	 * 
	 */
	public void showProjSettings() {
		this.projSettingsWindowCTRL.clearForm();
		this.projSettingsWindowCTRL.loadFields();
		this.projSettingsStage.showAndWait();
	}
	
	/**
	 * Loads & presents the Application Settings
	 * @param args
	 * 
	 */
	public void showAppSettings() {
		this.projSettingsWindowCTRL.loadFields();
		this.appSettingsStage.show();
	}
		
	/**
	 * Shows the list of parts. 
	 * Needs to be initialized first.
	 */
	public void showPartsList() {
		this.partsListStage.show();
	}
	
	/**
	 * Shows the list of required tools. 
	 * Needs to be initialized first.
	 */
	public void showToolBox() {
		this.toolBoxStage.show();
	}
		
	/**
	 * Sets the text on the System Message Label in RootLayoutController 
	 * @param String nMsg 	String to print on the Label. If nMsg == null it will be treated as empty string.
	 */
	public void setSysMessageLabel(String nMsg){
		this.rootLayoutCTRL.setSysMessage(nMsg);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
