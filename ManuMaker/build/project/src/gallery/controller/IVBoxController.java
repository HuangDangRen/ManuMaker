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

import java.util.Optional;

import gallery.abstractclasses.ControllerAbstr;
import gallery.model.GalleryProjectSettings;
import gallery.model.SlideInstructionalPic;
import gallery.model.SlidePic;
import gallery.model.SlideStopMotion;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class IVBoxController extends ControllerAbstr{
	
	@FXML
	AnchorPane AP_IVBoxBase;
	
	@FXML
	AnchorPane AP_ImgFrame;
	
	@FXML
	AnchorPane AP_MCPLabel;

	@FXML
	ImageView IV_Illustration;
	
	@FXML
	Button BT_MCPPlayPause;
	
	@FXML
	Button BT_MCPStop;
	
	@FXML
	Button BT_Abort;
	
	@FXML
	Label LBL_MCPFrame;
	
	private GalleryBrowserController owningController = null;
	private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	private Thread imageCycleThread = null;
	//private Image currentImage = new Image("file:resources/overlays/Blue.png");
	private AnchorPane rootPane = null;
	private SlidePic slidePicture = null;
	private StringProperty mcpLabel = null;
	private volatile boolean cycleImage = false; 
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
		this.invar("setOwningController @ end of method");
 	}
	
	public void setRootPane(AnchorPane nRoot) {
		this.rootPane = nRoot;
	}
	public void fitViewToImage() {
		this.IV_Illustration.preserveRatioProperty();
		this.IV_Illustration.fitHeightProperty().bind(this.AP_ImgFrame.heightProperty().subtract(10.0));
		this.IV_Illustration.fitWidthProperty().bind(this.AP_ImgFrame.widthProperty().subtract(10.0));
		
	}
	
	/**
	 * Enables / disables the controls of the controller.
	 * @param nBool
	 */
	public void disableGUI(boolean nBool) {
		this.AP_IVBoxBase.disableProperty().set(nBool);
	}
	
	public void enableAbortBtn(boolean enable){
		this.BT_Abort.disableProperty().set(!enable);
		this.BT_Abort.setVisible(enable);
	}
	
	
	public void enableMCPButtons(boolean nBool){
		this.BT_MCPPlayPause.setVisible(nBool);
		this.BT_MCPStop.setVisible(nBool);
		this.AP_MCPLabel.setVisible(nBool);
	}
	
	public void setPicture(SlidePic nPic) {
		if(nPic != null){
			this.slidePicture = nPic;
			this.IV_Illustration.setImage(nPic.getImage());
			
			// In case the previous Slide was a StopMotionSlide
			if(this.imageCycleThread != null) {
				this.stopAllThreads();
				this.imageCycleThread = null;
			}
			
			if(this.slidePicture.getClass().equals(SlideStopMotion.class)){
				((SlideStopMotion) this.slidePicture).reset();
				this.threadInit();
				this.imageCycleThread.start();
			} else if(this.slidePicture.getClass().equals(SlideInstructionalPic.class)){
				this.IV_Illustration.setImage(((SlideInstructionalPic)nPic).getFramedImage());
			}

		} else {
			if(this.imageCycleThread != null) {
				this.stopAllThreads();
			}
			this.slidePicture = new SlideInstructionalPic();
			this.IV_Illustration.setImage(null);
		}
		this.updateUI();
	}
	
	@Override
	public void updateUI() {
		this.invar("updateUI @ start of method");
		if(this.slidePicture != null) {
			if(this.slidePicture instanceof SlideStopMotion) {
				((SlideStopMotion) this.slidePicture).reset();
				this.enableMCPButtons(true);
				this.AP_MCPLabel.setVisible(true);
			} else {
				this.enableMCPButtons(false);
				this.AP_MCPLabel.setVisible(false);
			}
			//this.IV_Illustration.setImage(this.slidePicture.getImage());
			
			this.fitViewToImage();
		}
	}

	@Override
	public void initCTRL() {
		this.invar("initCTRL@ start of method");
		this.fitAPToParent(AP_IVBoxBase);
		this.IV_Illustration.setImage(new Image("file:resources/default_files/Blank.png"));
		this.mcpLabel = new SimpleStringProperty();
		this.enableAbortBtn(false);
		this.updateUI();
	}
	
	@FXML
	public void handleEditTitle() {
		if(this.owningController.TAB_View.isSelected() == false){
			TextInputDialog tiDia = new TextInputDialog(this.owningController.currentSlide.getSlideTitle());
			
			tiDia.setTitle("Edit Slide Title");
			tiDia.setHeaderText("Please give this slide a title");
			tiDia.setContentText("The title should be short but concise. \n\rManual numbering of the slides is not necessary,\nthe gallery takes care of all enumeration.");
			tiDia.setGraphic(null);
			Optional<String> res = tiDia.showAndWait();
			if(res.isPresent()) {
				this.owningController.currentSlide.setSlideTitle(res.get());
			} else {
				if(this.owningController.currentSlide.getSlideTitle() == ""){
					this.owningController.currentSlide.setSlideTitle("Click to edit slide title...");
				} 
			}
			this.updateUI();	
			this.owningController.updateCurrentSlide();
		}
	}
	



	@FXML
	public void handleMCPPlayPause() {
		if(this.cycleImage == true) {
			this.stopAllThreads();

		} else {
			this.threadInit();
			this.imageCycleThread.start();
		}
	}
	
	@FXML
	public void handleMCPStop() {
		if(this.cycleImage == true) {
			this.stopAllThreads();
			((SlideStopMotion)this.slidePicture).reset();
		}
	}

	@FXML
	public void handleCamAbort(){
		this.owningController.stopAllThreads();
		this.owningController.updateCurrentSlide();
	}

	@Override
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		check = check & (this.owningController != null);
		
		if(check == false){
			System.out.println("\n\rIVBoxController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
	
	
	private void threadInit() {
		if(this.imageCycleThread == null) {
			System.out.println("\n\rInitializing ImageCycleThread...");
			//((SlideStopMotion)this.slidePicture).reset();
			this.imageCycleThread = new Thread(new SMPRunnable());
	 		this.imageCycleThread.setName("SMPRunnable");
	 		this.imageCycleThread.setDaemon(true);
		} 		
	}
	
	public void stopAllThreads() { 
		if(this.imageCycleThread != null && this.imageCycleThread.isAlive()) {
			try{
				while(this.imageCycleThread.isAlive()){
					this.imageCycleThread.interrupt();
					System.out.println("\n\rIVBoxCTRL.stopAllThreads() in while-loop");
					Thread.sleep(5);
				}
			} catch (InterruptedException e){
		
			}
			if(!this.imageCycleThread.isAlive()){
				this.imageCycleThread = null;
			}
		}
	}
	
	/**
	 * Runnable for cycling through Stop Motion Pictures 
	 */
	public class SMPRunnable implements Runnable {
		Image capturedImage = null;
		long sleeptime = IVBoxController.this.AppSettingsSingleton.getSMPRefreshRate();
		@Override
		public void run() {
			try{
				while(!Thread.currentThread().isInterrupted()) {
					if(IVBoxController.this.owningController.captureCamOn) { //Stop cycling stop-motion image if cam-thread is activated
						Thread.currentThread().interrupt();
					} else {
						synchronized(IVBoxController.this){
							synchronized(IVBoxController.this.slidePicture) {
								synchronized(IVBoxController.this.mcpLabel) {
									IVBoxController.this.cycleImage = true;
									String labelText = new String("Frame: " + new Integer(((SlideStopMotion)IVBoxController.this.slidePicture).getCurrentIndex()+1).toString());
									IVBoxController.this.mcpLabel.set(labelText +"/"+((SlideStopMotion)IVBoxController.this.slidePicture).getSize().toString());
									synchronized(IVBoxController.this.IV_Illustration){
										IVBoxController.this.IV_Illustration.setImage(IVBoxController.this.slidePicture.getImage());
				
									}
									System.out.println("SMP Runnable cycle Started:\n\r" + labelText );
								}
							}
						}
	
						Platform.runLater(new Runnable() {
		
							@Override
							public void run() {
								IVBoxController.this.LBL_MCPFrame.setText(IVBoxController.this.mcpLabel.get());				
							}
						});
					}
					Thread.sleep(sleeptime);					
				}
			} catch (InterruptedException e){ 
				
			}
			IVBoxController.this.cycleImage = false;			
		}
	}
}
