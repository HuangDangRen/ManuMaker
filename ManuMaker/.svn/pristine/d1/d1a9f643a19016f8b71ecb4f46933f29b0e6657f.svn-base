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

import gallery.abstractclasses.ControllerAbstr;
import gallery.model.GallerySlide;
import gallery.model.SMPFrame;
import gallery.model.ScreenCaptureTool;
import gallery.model.SlidePic;
import gallery.model.SlideStopMotion;
import gallery.utils.ToolTipMaker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class StopMotionToolController extends ControllerAbstr {
	
	@FXML
	AnchorPane AP_StopMotionTool;
	@FXML
	TableView<SMPFrame> TV_ThumbList;
	@FXML
	TableColumn<SMPFrame, Integer> TC_Ordinal;
	@FXML
	TableColumn<SMPFrame, String> TC_Timestamp;
	@FXML
	Button BT_AddFrameCam;
	@FXML
	Button BT_AddFramePaste;
	@FXML
	Button BT_DeleteFrame;
	@FXML
	Button BT_OK;
	@FXML
	Button BT_Cancel;
	@FXML
	Button BT_MoveUp;
	@FXML
	Button BT_MoveDown;	
	
	private GalleryBrowserController owningController = null;
	private IVBoxController IVBoxCTRL = null;
	private Node oldChild = null;
	private ObservableList<SMPFrame> imgList = FXCollections.observableArrayList();
	private Integer currentPos = 0;
	private ScreenCaptureTool screenCam = new ScreenCaptureTool();
	private void refreshOrdinals() {
		for(int i = 0; i < this.imgList.size(); i++){
			this.imgList.get(i).setOrdinal(i);
		}
	}
	/** 
	 * Sets the reference to the owning controller
	 * @param nOC
	 */
	public void setOwningController(GalleryBrowserController nOC) {
		this.owningController = nOC;
		

		this.invar("setOwningController() @ end of method");
 	}
	
	public void setImageBoxController(IVBoxController ivBox) {
		this.IVBoxCTRL = ivBox;
		
		this.invar("setImageBoxController() @ end of method");
	}
	
	/**
	 * Make the StopMotionController remember the GUI component it replaced
	 * @param nOC
	 */
	public void setOldChild(Node nOC) {
		this.oldChild = nOC;
	}
	
	/**
	 * Get the GUI component which the StopMotionController temporarily replaced
	 * @return
	 */
	public Node getOldChild() {
		return this.oldChild;
	}
	

	@Override
	public void updateUI() {
		if(this.currentPos >= 0 && this.currentPos < this.imgList.size()) {

					this.IVBoxCTRL.setPicture(new SlidePic(this.imgList.get(this.currentPos).getImage()));	
		}
		
		if(this.imgList.size() > 0){

					this.TV_ThumbList.selectionModelProperty().get().clearAndSelect(this.currentPos);

		}
	}
	

	@Override
	public void initCTRL() {
		
		this.BT_AddFrameCam.setTooltip(ToolTipMaker.simpleToolTip(	"First click starts a the camera stream, second click adds the captured image to the frame list above.\n"
				+ "Click the red cancel button at the bottom right of the image to stop the camera stream without capturing an image."));
		this.BT_AddFramePaste.setTooltip(ToolTipMaker.simpleToolTip("Inserts a frame from System Clipboard. \n"
				+ "Camera- and Clipboard frames can be mixed and matched in a Stop-Motion Series, \n"
				+ "although then it is recommended to paste images with similar aspect ratio as the camera, \n"
				+ "to make viewing more comfortable. \n"
				+ "The GUI automatically adjusts to image size, meaning in a Stop-Motion \n"
				+ "Series with different image formats the GUI will resize every second."));
		
		this.BT_DeleteFrame.setTooltip(ToolTipMaker.simpleToolTip(	"Deletes the selected frame from the list. \n"
				+ "Recovery of a deleted frame is not possible."));
		
		
		this.BT_MoveDown.setTooltip(ToolTipMaker.simpleToolTip(	"Move the selected item down one step in the frame list."));
		
		this.BT_MoveUp.setTooltip(ToolTipMaker.simpleToolTip(	"Move the selected item up one step in the frame list."));
		
		this.BT_OK.setTooltip(ToolTipMaker.simpleToolTip(	"Compile the captured frames into a Stop Motion Picture and add it to the current slide. \n"
				+ "The image series can be reordered, appended or shortend by reopening \n"
				+ "the Stop Motion Tool on a frame containing a Stop Motion Picture."));
		
		
		this.TC_Ordinal  = new TableColumn<SMPFrame, Integer>("Frame Number");
		
		
		this.TC_Ordinal.setCellValueFactory( 
				new PropertyValueFactory<SMPFrame, Integer>("ordinal"));

		this.TC_Ordinal.setCellFactory(new Callback<TableColumn<SMPFrame, Integer>, TableCell<SMPFrame, Integer>>() {
			@Override
			public TableCell<SMPFrame, Integer> call(TableColumn<SMPFrame, Integer> param){
				return new TableCell<SMPFrame, Integer>() {
					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						if(!empty) {
							setText(new Integer(item + 1).toString());
						} else { 
							setText("");
						}
					}
				};				
			}
		});	
		
		
		this.TC_Timestamp  = new TableColumn<SMPFrame, String>("Timestamp");
		
		this.TC_Timestamp.setCellValueFactory( 
				new PropertyValueFactory<SMPFrame, String>("timestamp"));

		this.TC_Timestamp.setCellFactory(new Callback<TableColumn<SMPFrame, String>, TableCell<SMPFrame, String>>() {
			@Override
			public TableCell<SMPFrame, String> call(TableColumn<SMPFrame, String> param){
				return new TableCell<SMPFrame, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if(!empty) {
							setText(item);
						} else { 
							setText("");
						}
					}
				};				
			}
		});	
		

		this.TC_Ordinal.setMinWidth(50.0);
		this.TC_Ordinal.setMaxWidth(50.0);	
		this.TC_Ordinal.setPrefWidth(50.0);
		this.TV_ThumbList.setItems(this.imgList);
		this.TV_ThumbList.getColumns().set(0, this.TC_Ordinal);	
		this.TV_ThumbList.getColumns().set(1, this.TC_Timestamp);	
	}
	
	@FXML
	public void handleListSelection() {
		if(this.imgList.size() > 0){
			this.currentPos =  this.TV_ThumbList.getSelectionModel().getSelectedIndex();
			if(this.currentPos < this.imgList.size()){
				this.updateUI();
			}
		}
	}
	
	@FXML
	public void handleMoveUp() {
		if(this.currentPos > 0){
			SMPFrame tempFrame = this.imgList.get(this.currentPos);
			
			this.imgList.remove(tempFrame);
			tempFrame.setOrdinal(currentPos-1);
			this.imgList.add(this.currentPos-1, tempFrame);
			this.currentPos--;
			
			this.refreshOrdinals();
			this.updateUI();
		}
	}

	@FXML
	public void handleMoveDown() {
		if(this.currentPos < this.imgList.size()-1){
			SMPFrame tempFrame = this.imgList.get(this.currentPos);
			
			this.imgList.remove(tempFrame);
			tempFrame.setOrdinal(currentPos+1);
			this.imgList.add(this.currentPos+1, tempFrame);
			this.TV_ThumbList.selectionModelProperty().get().clearAndSelect(this.currentPos+1);
			this.currentPos++;
			
			this.refreshOrdinals();
			this.updateUI();
		}
	}
	
	@FXML
	public void handleAddCam() {
		boolean started = false;
		if(this.owningController.captureCamOn == false){
			this.owningController.stopAllThreads();
			this.owningController.threadInit();
			started = true;
		} else {
			this.owningController.stopAllThreads();
			SMPFrame nFrame = new SMPFrame();
			Image tmp = this.owningController.getDisplayedImage();
			if(tmp != null){
				nFrame.setImage(tmp);
				nFrame.setOrdinal(this.currentPos);
				this.invar("handleAddCam() @ start of method");
				try{
					if(this.imgList.size() == 0){
						this.imgList.add(this.currentPos, nFrame);
					} else {
						this.imgList.add(++this.currentPos, nFrame);
					}
				
				} catch (ClassCastException cce) {
					cce.printStackTrace();
				} catch (RuntimeException re) {
					re.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.refreshOrdinals();
			}
		}
		
		if(started == false){
			this.owningController.stopAllThreads();
		}
		this.updateUI();
		this.invar("handleAddCam() @ end of method");
	}
	
	@FXML
	public void handleAddPaste() {
		this.owningController.stopAllThreads();
		SMPFrame nFrame = new SMPFrame();
		nFrame.setImage(this.screenCam.getClipboardImage());
		if(nFrame.getImage() != null){
			nFrame.setOrdinal(this.currentPos);
			try{
				if(this.imgList.size() == 0){
					this.imgList.add(this.currentPos, nFrame);
				} else {
					this.imgList.add(++this.currentPos, nFrame);
				}
			
			} catch (ClassCastException cce) {
				cce.printStackTrace();
			} catch (RuntimeException re) {
				re.printStackTrace();
			}
			this.refreshOrdinals();
			this.updateUI();
			this.invar("handleAddPaste() @ end of method");
		}
	}
	
	@FXML
	public void handleDeleteFrame() {
		if(this.owningController.threadInterrupted()){
			this.owningController.stopAllThreads();
			this.refreshOrdinals();
			if(this.imgList.size() > 0) {
				if(this.TV_ThumbList.getSelectionModel().getSelectedIndex() == -1){
					this.TV_ThumbList.getSelectionModel().select(this.imgList.size()-1);
				} 
				this.imgList.remove(this.TV_ThumbList.getSelectionModel().getSelectedIndex());
				
			}
			if(this.currentPos > 0) {
				this.currentPos--;
			}
		} else {
		}
		this.updateUI();
	}
	
	@FXML
	public void handleCancel() {
		this.owningController.stopAllThreads();
		this.imgList.clear();
		this.currentPos = 0;
		this.owningController.stopMotionDone(false);
	}

	@FXML
	public void handleOK() {
		this.owningController.stopAllThreads();
		this.refreshOrdinals();
		if(this.imgList.size() == 0) {
			this.owningController.stopMotionDone(false);
		} else {
			SlideStopMotion newSSM = new SlideStopMotion();
			
			this.currentPos = 0;
			for(SMPFrame i:this.imgList) {
				newSSM.addImage(i);
				this.currentPos++;
			}
			newSSM.reset();
			if(this.owningController.currentSlide instanceof GallerySlide) {
				newSSM.reset();
				((GallerySlide)this.owningController.currentSlide).setPicture(newSSM);	
			}
			
			newSSM = null;
			this.imgList.clear();
			this.currentPos = 0;
			this.owningController.stopMotionDone(true);
		}
	}


	public void loadSMP(SlideStopMotion nSMP){
		this.imgList.clear();
		nSMP.reset();
		for(int i = 0; i < nSMP.getSize(); i++){
			this.imgList.add(nSMP.getSMPFrame());
		}
	}
	
	public void resetController(){
		this.imgList.clear();
		this.currentPos = 0;
		this.updateUI();
	}
	
	@Override
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		check = check & (this.owningController != null);
		if(this.imgList != null) {
			check = check & (this.currentPos <= this.imgList.size() && this.currentPos >= 0);
		}
		
		if(check == false){
			System.out.println("\n\rStopMotionToolController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
	

}
