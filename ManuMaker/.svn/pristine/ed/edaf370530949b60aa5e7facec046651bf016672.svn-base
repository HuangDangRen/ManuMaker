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
package gallery.camera;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamLockException;
import com.github.sarxos.webcam.WebcamResolution;
import gallery.abstractclasses.CamAbstr;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class SarxosCam extends CamAbstr {

	// Private Fields
	private Webcam webcam; 
	private Map<String, Webcam> webcams = new HashMap<String, Webcam>();
	private Map<String, Image> thumbnails = new HashMap<String, Image>();

	
	/**
	 * Constructor selects default device at creation
	 */
	public SarxosCam() {
	
		updateWebcamList();
		selectCam("DEFAULT");

	}

	@Override 
	public void init(String camName, Dimension nDim) {
		this.getResolutions();
		if(camName == null) {
			this.selectCam(this.AppSettingsSingleton.getCamName());
		} else {
			this.selectCam(camName);
		}
		
		if(nDim == null) {
			this.setResolution(this.AppSettingsSingleton.getCamResolution());
		} else {
			this.setResolution(nDim);
		}
	}
	@Override
	public void updateWebcamList() {
		this.webcams.clear();
		for(Webcam iCam : Webcam.getWebcams()) {
			this.webcams.put(iCam.getName(), iCam);
			
		}
	}
	

	@Override
	public List<String> getWebcamNames() {
		List<String> camNames = new ArrayList<String>();
		
		camNames.add("DEFAULT");
		for(Webcam iCam : Webcam.getWebcams()) {
			camNames.add(iCam.getName());
		}
		return camNames;
	}
	
	@Override
	public List<Dimension> getResolutions() {
		List<Dimension> resoList = new ArrayList<Dimension>();
		Dimension[] resoArray = this.webcam.getViewSizes();
		
		for(Dimension i:resoArray) {
			resoList.add(i);		
		}
		resoList.add(WebcamResolution.SVGA.getSize());
		resoList.add(WebcamResolution.HD720.getSize());
		resoList.add(new Dimension(1920,1080));
		resoList.add(WebcamResolution.UXGA.getSize());
		resoList.add(WebcamResolution.QXGA.getSize());
		
		resoArray = resoList.toArray(new Dimension[0]);
		
		this.webcam.setCustomViewSizes(resoArray);
		
		return resoList; 
	}
	
	
	@Override
	public void setResolution(Dimension nSize) {
		try {
			this.webcam.close();
			this.webcam.setViewSize(nSize);
			
			System.out.printf("n\rCam.setResolution(): The new Camera Resolution is: " + this.webcam.getViewSize().width +"x" + this.webcam.getViewSize().height + "\n\r");
			
			this.webcam.open();
		} catch (WebcamLockException wle){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Webcam already in use!");
			alert.setHeaderText("Webcam already in use!");
			alert.setContentText(	"The webcam \n\r"
								+ 	this.AppSettingsSingleton.getCamName() + "\n\r"
								+ 	"is being used by another Application. \n"
								+ 	"It will not be available for use in ManuMaker. \n"
								+ 	"If you need to use this cam, close the other Application \n"
								+ 	"or change its settngs to use a different webcam. \n\n"
								+ 	"Please select a different webcam.");
			alert.showAndWait();
		}

	}
	
	@Override
	public Dimension getResolution(){
		return this.webcam.getViewSize();
	}
	
	@Override
	public Map<String, Image> getThumbnails() {
		Webcam original = this.webcam;
		try{
			if(this.webcam.isOpen()) {
				this.webcam.close();
			}
			this.thumbnails.clear();

			Webcam.getDefault().open();
		} catch (WebcamLockException wle){
			// Keep quiet
		}
		this.thumbnails.put("DEFAULT", convertToFxImg(Webcam.getDefault().getImage()));
		Webcam.getDefault().close();
		for(Webcam iCam : Webcam.getWebcams()) {
			try{
				iCam.open();
				this.thumbnails.put(iCam.getName(), convertToFxImg(iCam.getImage()));
				iCam.close();
			} catch (WebcamLockException wle){
				// Keep quiet
			}

		}
		
		this.webcam = original;
		this.webcam.open();
		
		return this.thumbnails;
	}
	

	@Override
	public void selectCam(String camName) {
		try{
			if(!(this.webcam == null)) {
				this.webcam.close();
			}
			if(this.webcams.containsKey(camName)) {
				this.webcam = this.webcams.get(camName);
			} else if (camName == "DEFAULT"){
				this.webcam = Webcam.getDefault();
			}
			this.AppSettingsSingleton.storeCamName(camName);
			try{
				this.webcam.setViewSize(new Dimension(this.AppSettingsSingleton.getCamResolution()));
			} catch (IllegalArgumentException iae) {
				this.webcam.setViewSize(new Dimension(640,480));
			}
		
			this.webcam.open();
		} catch (WebcamLockException wle){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Webcam already in use!");
			alert.setHeaderText("Webcam already in use!");
			alert.setContentText(	"The webcam \n\r"
								+ 	this.AppSettingsSingleton.getCamName() + "\n\r"
								+ 	"is being used by another Application. \n"
								+ 	"It will not be available for use in ManuMaker. \n"
								+ 	"If you need to use this cam, close the other Application \n"
								+ 	"or change its settngs to use a different webcam. \n\n"
								+ 	"Please select a different webcam, or ignore this dialog if your task does not require a webcam.");
			alert.showAndWait();
		} 
	}
	
	@Override
	public Image captureCam() {
		return convertToFxImg(this.webcam.getImage());
	}
	
	
	
	@Override
	public String currentCamName() {
		return this.AppSettingsSingleton.getCamName();
	}
}
