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

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import gallery.abstractclasses.ControllerAbstr;
import gallery.utils.ToolTipMaker;

public class AppSettingsWindowController extends ControllerAbstr{
	// Reference to the main application
	private Stage dialogStage;
	private Map<String, Image> thumbNails = new HashMap<String, Image>();
	private List<String> camList = new ArrayList<String>();
	private GalleryBrowserController galleryBrowserCTRL = null;
	
	@FXML
	private Tab TAB_Camera;
	
	@FXML 
	private Label LBL_CamName;
	
	@FXML 
	private Label LBL_Resolution;
	
	@FXML
	private ImageView IV_CamPreview;
	
	@FXML 
	private AnchorPane AP_CamPreview;
	
	@FXML
	private Button BT_SelectCam;
	
	@FXML
	private Button BT_SetResolution;
	
	@FXML 
	private Button BT_RotateImage;
	
	@FXML
	private TextField TF_SMPRefreshTime;

	/**
	 * The Application settings use their own stage.
	 *
	 */
	public void setStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setGalleryBrowserCTRL(GalleryBrowserController nGBC){
		this.galleryBrowserCTRL = nGBC; 
	}
	
	/**
	 * Initializes the controller
	 */
	@Override
	public void initCTRL() {
		// Set tooltips:
		this.TAB_Camera.setTooltip(ToolTipMaker.simpleToolTip("This tab contains all project-independent camera related settings"));
		this.BT_SelectCam.setTooltip(ToolTipMaker.simpleToolTip("Select a USB webcam to take pictures with"));
		this.BT_SetResolution.setTooltip(ToolTipMaker.simpleToolTip("Select the Image resolution for webcam snapshots.\nWARNING: Not all resolutions listed are necessarily supported by your webcam. \nSee Help->Compatibility whether your cam \nhas been tested with this application."));
		this.BT_RotateImage.setTooltip(ToolTipMaker.simpleToolTip("Rotate the Image by multiples of 90°"));
		this.LBL_Resolution.setTooltip(ToolTipMaker.simpleToolTip("Currently selected webcam resolution. \nNOTE: If this resolution is not supported by your camera, \nthis label will still show the desired, not the actual resolution."));
		this.TF_SMPRefreshTime.setTooltip(ToolTipMaker.simpleToolTip("Set the diplay time (in milliseconds) of Stop Motion Picture frames. Only integer numbers between 250 (0.25s) and 25000 (25s) are accepted."));
		this.TF_SMPRefreshTime.setText(this.AppSettingsSingleton.getSMPRefreshRate().toString());
		this.LBL_CamName.setText(this.AppSettingsSingleton.getCamName());
		this.LBL_Resolution.setText(this.AppSettingsSingleton.getCamResolution().toString());
		this.updateUI();
	}
	
	/**
	 * Updates the UI 
	 */
	@Override
	public void updateUI( ) {
		Dimension dim = this.AppSettingsSingleton.getCamResolution();
		this.camList = this.galleryBrowserCTRL.camera.getWebcamNames();		
		this.galleryBrowserCTRL.camera.selectCam(this.AppSettingsSingleton.getCamName());
		this.IV_CamPreview.setImage(this.thumbNails.get(this.AppSettingsSingleton.getCamName()));
		this.IV_CamPreview.preserveRatioProperty();
		this.IV_CamPreview.fitWidthProperty().bind(this.AP_CamPreview.widthProperty());
		this.LBL_CamName.setText(this.AppSettingsSingleton.getCamName());
		this.LBL_Resolution.setText(String.valueOf(dim.width) + "x" + String.valueOf(dim.height));
		this.galleryBrowserCTRL.camera.init(this.AppSettingsSingleton.getCamName(),this.AppSettingsSingleton.getCamResolution());
	}
	
	/** 
	 * Handles the selection of a new camera
	 * 
	 */
	@FXML
	public void handleSelectCam()  {
		ChoiceDialog<String> dialog = null;
		

		if(! this.camList.equals(this.galleryBrowserCTRL.camera.getWebcamNames())){				// Check if CamList  / Thumbnails need to be updated: 
			this.camList = this.galleryBrowserCTRL.camera.getWebcamNames();
			this.thumbNails = this.galleryBrowserCTRL.camera.getThumbnails();
		} 
		if(this.camList.contains(this.AppSettingsSingleton.getCamName())) {							// If Cam in saved settings still exists:
			dialog = new ChoiceDialog<>(this.AppSettingsSingleton.getCamName(), this.camList);
		} 
		else {																						// Else use Cam No. 0
			dialog = new ChoiceDialog<>(this.camList.get(0), this.camList);
		}
		
		dialog.setTitle("Cam Selection");
		dialog.setHeaderText("Please select a webcam from the dropdown menu.");
		dialog.setContentText("Selected Cam:");

		Optional<String> result = dialog.showAndWait();

		if(result.isPresent()) {
			System.out.println("Your choice: " + result.get());
			this.AppSettingsSingleton.storeCamName(result.get());			
		}
		updateUI();
	}
	
	/**
	 * Handles picking a resolution for the camera (Presents all available solutions)	 * 
	 */
	@FXML
	public void handleSetResolution() {
		ChoiceDialog<Dimension> dialog = null;
		
		List<Dimension> dimList = new ArrayList<Dimension>();
		
		dimList = this.galleryBrowserCTRL.camera.getResolutions();
		
		
		dialog = new ChoiceDialog<>(dimList.get(0), dimList);

		
		dialog.setTitle("Cam Selection");
		dialog.setHeaderText("Please select a webcam from the dropdown menu.");
		dialog.setContentText("Selected Cam:");

		Optional<Dimension> result = dialog.showAndWait();

		if(result.isPresent()) {
			System.out.println("Your choice: " + result.get().toString());
			this.AppSettingsSingleton.setCamResolution(result.get());
			this.galleryBrowserCTRL.camera.setResolution(result.get());			
		}
		updateUI();
	}
	
	/**
	 * Handles rotation of the image to compensate for camera installation angle
	 * 
	 */
	@FXML
	public void handleRotateImage() {
		ChoiceDialog<Integer> dialog = null;
		List<Integer> rotationSelection = new ArrayList<Integer>();
		
		rotationSelection.add(0);
		rotationSelection.add(90);
		rotationSelection.add(180);
		rotationSelection.add(270);
		
		
		dialog = new ChoiceDialog<>(rotationSelection.get(0), rotationSelection);

		
		dialog.setTitle("Cam Selection");
		dialog.setHeaderText("Please select a webcam from the dropdown menu.");
		dialog.setContentText("Selected Cam:");

		Optional<Integer> result = dialog.showAndWait();

		if(result.isPresent()) {
			System.out.println("Your choice: " + result.get().toString());
			this.LBL_Resolution.setText(result.get().toString());
			this.mainApp.AppSettingsSingleton.storeCamAngle(result.get());
		}
		updateUI();
	}
	
	/**
	 * Adds a Filter to all images before presenting them (original file stays untouched!)
	 * 
	 */
	@FXML
	public void handleApplyFilter() {
		// TODO: Implement in next release.
	}

	
	
	/** 
	 * Saves the settings
	 */
	@FXML
	public void handleSave() {
		try{
			String newTime = AppSettingsWindowController.this.TF_SMPRefreshTime.getText();
			Integer time = Integer.parseUnsignedInt(newTime);
			if(time > 250 && time <= 25000){
				AppSettingsWindowController.this.AppSettingsSingleton.setSMPRefreshRate(time);
				
				this.AppSettingsSingleton.saveSettings();
				this.dialogStage.close();
				
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Stop Motion Refresh Time: Number not within safe range!");
				alert.setHeaderText("Stop Motion Display Time: Given number is outside of designated safe range!");
				alert.setTitle("Please enter a positive, integer number between 250 and 25000.");
				
				alert.showAndWait();
			}
		}catch(NumberFormatException e){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Stop Motion Refresh Time: Input is not a valid number!");
			alert.setHeaderText("Stop Motion Display Time: Given input is not a valid number!");
			alert.setTitle("Please enter a positive number between 250 and 25000.");
			
			alert.showAndWait();
		}
	}
	
	/**
	 * Closes the settings window without saving changes (prompts user to discard changes)
	 * 
	 */
	@FXML
	public void handleCancel() {
		this.dialogStage.close();
	}

	@Override
	protected boolean invar(String text) {
		boolean check = true;
		check = check & (this.mainApp != null);
			
		if(check == false){
			System.out.println("\n\rAppSettingsController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
}
