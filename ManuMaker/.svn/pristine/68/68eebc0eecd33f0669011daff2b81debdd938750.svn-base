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
package gallery.abstractclasses;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;

public abstract interface CamInterface {
	

	/**
	 * Updates the webcam hashmap of all Webcams currently connected
	 */
	public abstract void updateWebcamList();
	
	/**
	 * returns list of available Webcams as a List<String>
	 * 
	 */
	public abstract List<String> getWebcamNames();
	
	/**
	 * returns a list of available resolutions
	 */
	public abstract List<Dimension> getResolutions();
	
	
	/**
	 * Sets the view size
	 */
	public abstract void setResolution(Dimension nSize);
	
	/**
	 * Return actual webcam Resolution
	 */
	public abstract Dimension getResolution();
	
	/**
	 * Returns the list of images from all available Webcams in chronological order. 
	 * 
	 * @return List<FXImage> List of images in JavaFX compatible format.
	 */
	public abstract  Map<String, Image> getThumbnails();
	
	/**
	 * Selects the Webcam to be used.
	 */
	public abstract void selectCam(String camName);
	
	/**
	 * Captures the image of the currently selected Webcam
	 */
	public abstract Image captureCam();
	
	
	
	/**
	 * Returns index of Current Webcam 
	 */
	public abstract String currentCamName();

	public abstract void init(String camName, Dimension nDim);


}
