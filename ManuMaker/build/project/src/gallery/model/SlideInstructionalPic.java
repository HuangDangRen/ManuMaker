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
package gallery.model;

import java.io.File;
import gallery.utils.PathUtils;
import gallery.utils.WarningLevel;
import javafx.scene.image.Image;

public class SlideInstructionalPic extends SlidePic {
//	ImageOverlay warningFrame = null;
	WarningLevel currentLevel = WarningLevel.NONE;
	
	public SlideInstructionalPic() {
//		this.warningFrame = new ImageOverlay();
		this.image = null;
		this.imageSet = false;
	}
	
	public SlideInstructionalPic(Image nImage)   {
		if(nImage != null) {
//			this.warningFrame = new ImageOverlay();
			this.image = nImage; 
			this.setRotation(this.AppSettingsSingleton.getCamAngle());
			this.imageSet = true;
		} else {
			this.image = null;
			this.imageSet = false;
			  
		}

	}
	
	public void setWarningLevel(WarningLevel nLevel){
		if(nLevel != null){
			this.currentLevel = nLevel;
		} else {
			this.currentLevel = WarningLevel.NONE;
		}
	}
	
	public Image getFramedImage(){
		if(this.image != null){
//			return this.warningFrame.getMarkedImage(this.image, currentLevel);
			return this.image;
		} else{
			return null;
		}
	}
	
	public Image getFramedImage(WarningLevel newCurrentLevel){
//		return this.warningFrame.getMarkedImage(this.image, newCurrentLevel);
		return this.image;
	}
	

	public boolean loadImage(Integer sldNumber){
		boolean loaded = false;
		File imagePath = new File(this.GPSSingleton.getSavePath() + "\\Resources\\slide_" + sldNumber.toString() + ".png");
		
		if(imagePath.exists()){
			this.image = PathUtils.loadImagePng(imagePath);
			loaded = true;
			this.imageSet = true;
		}
	
		return loaded;
	}
	
	public boolean saveImage(Integer sldNumber){
		boolean saved = false;
		if(this.image != null){
			File imagePath = new File(this.GPSSingleton.getSavePath() + "\\Resources\\");
			saved = PathUtils.saveImagePng(imagePath, this.image, "slide_" + sldNumber + ".png");
		}
		return saved; 
	}
	
}
