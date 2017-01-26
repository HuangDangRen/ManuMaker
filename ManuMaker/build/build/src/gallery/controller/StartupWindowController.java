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
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

import gallery.abstractclasses.ControllerAbstr;
import gallery.utils.ToolTipMaker;


public class StartupWindowController extends ControllerAbstr {
	// Reference to the main application
	private RootLayoutController rootLayoutCTRL = null;

	@FXML
	private Button BT_LoadGallery;
	

	@FXML
	private Button BT_NewGallery;
	
	
	@FXML
	ImageView IV_Logo;
	
	@FXML
	AnchorPane AP_StartupBase;

	@Override 
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null);
		check = check & (this.rootLayoutCTRL != null);
		
		if(check == false){
			System.out.println(text);
		}
		return check;	
	}
	
	/**
	 * Initializes the Controller & GUI Tooltips
	 */
	@Override
	public void initCTRL() {
		System.out.println("\r\nStartupWindowController.initCTRL() -> Initializing Controller...");
		this.invar("StartupWindowController.initCTRL @ start of method");
		this.BT_LoadGallery.setTooltip(ToolTipMaker.simpleToolTip("Select an existing Documentation Project to load\n\n"));
		this.BT_NewGallery.setTooltip(ToolTipMaker.simpleToolTip("Create a new, empty Documentation Project\n\n"));
		this.invar("StartupWindowController.initCTRL @ end of method");
		System.out.println("\r\nStartupWindowController.initCTRL() -> Controller initialized.");
		

		
	}

	
	public void setRootCtrl(RootLayoutController nRootCtrl)  {
		this.rootLayoutCTRL = nRootCtrl;
		this.invar("StartupWindowController.setRootCTRL @end of method");
	}
	/** 
	 * Creates a new Gallery
	 */
	@FXML
	public void handleNewGallery() {
		if(invar("StartupWindowController.handleNewGallery()")){
			this.rootLayoutCTRL.handleNewGallery();
		}
	}
	
	/**
	 * Opens an existing Gallery
	 */
	@FXML
	public void handleOpenGallery() {
		if(invar("StartupWindowController.handleOpenGallery()")){
			try {
				this.rootLayoutCTRL.handleLoadGallery();
			} catch (IOException e) {
		    	Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("ERROR: Project could not be loaded!");
		    	alert.setHeaderText("Project could not be loaded!");
		    	alert.setContentText(	"The project structure may be corrupted. \n\r"
		    					+ 		"Recovery operations have failed. \n"
		    					+ 		"Some data may be salvageable through manual intervention.");
		    	alert.showAndWait();
			}
		} 
	}

	@Override
	public void updateUI() {
		if(invar("StartupWindowController.updateUI()")){
			//NOP
		} 
	}
	
}
