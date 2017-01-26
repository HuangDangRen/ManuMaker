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



import java.awt.image.BufferedImage;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import gallery.utils.PathUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;

/**
 * Encapsulates an fx Image and provides setter & getter methods
 * 
 * @author Daniel Lachmann
 *
 */
public class SlidePic {
	protected Image image = null;
	protected boolean imageSet = false;
	protected ApplicationSettings AppSettingsSingleton = ApplicationSettings.INSTANCE;
	protected GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;

//	protected static Image convertToFxImg(BufferedImage nImg) {
//		if(!(nImg == null)) {
//			return SwingFXUtils.toFXImage(nImg, null);
//		}
//		else {
//			return new Image("file:resources/default_files/BlankImage.png");
//		}
//	}
//	
	/**
	 * Turns the Image by a given angle given in degrees
	 * @param nAngle
	 */
	 /**
	 * @param nAngle
	 */
	protected synchronized void setRotation(int nAngle){
        int w;
        int h;
        @SuppressWarnings("unused")
		int wdash;
        @SuppressWarnings("unused")
		int hdash;
        int ws;
        int hs;
        int nb = 0;
        @SuppressWarnings("unused")
		int kb = 0;
        BufferedImage source = SwingFXUtils.fromFXImage(this.image, null);
        if(!(source == null)) {
	        ws = source.getWidth();
	        hs = source.getHeight();
	        
			if(this.imageSet) {
		        w =  (int) this.image.getWidth();
		        h =  (int) this.image.getHeight();
			} else {
				w = 640;
				h = 480;
			}     
			
			if(nAngle == 0) {
				wdash = w;
				hdash = h;
				
			} else if(nAngle == 90) {
				wdash = h; 
				hdash = w;
				BufferedImage target = new BufferedImage(source.getHeight(), source.getWidth(), BufferedImage.TYPE_INT_ARGB);
			        
				try {
					for(int n = 1; n < ws; n++){
						for(int k = 1; k < hs; k++){
							nb = n;
							kb = k;
							target.setRGB(k, ws-n, source.getRGB(n, k));
						}
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println("\n\rException thrown at Coordinates [x, y]: " + String.valueOf(nb) + ", " + String.valueOf(nb));
					e.printStackTrace();
					
				}
				if(target != null) {
					this.image = SwingFXUtils.toFXImage(target, null);
				}
				
			} else if(nAngle == 180) {
				wdash = w; 
				hdash = h;
				
				BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
		    	        
				try {
					for(int n = 1; n < ws; n++){
						for(int k = 1; k < hs; k++){
							nb = n;
							kb = k;
							target.setRGB(ws-n, hs-k, source.getRGB(n, k));
						}
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println("\n\rException thrown at Coordinates [x, y]: " + String.valueOf(nb) + ", " + String.valueOf(nb));
					e.printStackTrace();
				}
				if(target != null) {
					this.image = SwingFXUtils.toFXImage(target, null);
				}
			} else if(nAngle == 270) {
				wdash = h; 
				hdash = w;
				
				BufferedImage target = new BufferedImage(source.getHeight(), source.getWidth(), BufferedImage.TYPE_INT_ARGB);
				
				try {
					for(int n = 1; n < ws; n++){
						for(int k = 1; k < hs; k++){
							nb = n;
							kb = k;
							target.setRGB(hs-k, n, source.getRGB(n, k));
						}
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println("\n\rException thrown at Coordinates [x, y]: " + String.valueOf(nb) + ", " + String.valueOf(nb));
					e.printStackTrace();
				}
				if(target != null) {
					this.image = SwingFXUtils.toFXImage(target, null);
				}
			} else { 
				System.err.print("ERROR: Invalid rotation angle selected!");
				return;
			}
        }
	}
	
	
	public SlidePic() {
		//this.image = new Image("file:resources/default_files/BlankImage.png");
	}
	
	public SlidePic(Image nImage)   {
		if(nImage != null) {
			this.image = nImage; 
			this.imageSet = true;
		} else {
			this.image = null;
			this.imageSet = false;
		}

	}
	
	/**
	 * Setter Method which sets the Image and rotates it according to ApplicationSettings.CamAngle
	 * @param nImage
	 */
	public void setImage(Image nImage)    {
		if(nImage != null) {
			this.image = null;
			this.image = nImage;
			this.setRotation(this.AppSettingsSingleton.getCamAngle());
			this.imageSet = true;
		} else {
			this.image = null;
			this.imageSet = false;
		  
		}

	}
	
	/**
	 * Setter Method which sets the Image without further manipulation
	 * @param nImage
	 * @throws InvalidParameterException
	 */
	public void copyImage(Image nImage)   {
		if(nImage != null) {
			this.image = null;
			this.image = nImage;
			this.imageSet = true;
		} else {
			this.image = null;
			this.imageSet = false;
		}
	}
	
	
	/*
	 * deletes image
	 */
	public void deleteImage() {
		this.image = null;
		this.imageSet = false;
	}
	
	/*
	 * getter Method	
	 */
	public Image getImage() {
		synchronized (this) {
			return this.image;
		}
		
	}
	
	public boolean isImageSet() {
		return this.imageSet;
	}
	
	/**
	 * Returns the dimensions of the picture in an integer array list. 
	 * 
	 * the x-value (width) is stored in the first element, 
	 * the y-value (height) in the second. 
	 * @return
	 */
	@Deprecated
	public ArrayList<Integer> getDimensions(){
		ArrayList<Integer> arr = new ArrayList<Integer>(); 
		
		arr.add((int) this.image.getWidth());
		arr.add((int) this.image.getHeight());
		
		return arr;
	}
	
	
	public boolean loadImage(Integer sldNumber){
		boolean loaded = false;
		File imagePath = new File(this.GPSSingleton.getSavePath() + "\\Resources\\slide_" + sldNumber + ".png");
		
		if(imagePath.exists()){
			this.image = PathUtils.loadImagePng(imagePath);
			loaded = true;
			this.imageSet = true;
		}
		return loaded;
	}
	
	public boolean saveImage(Integer sldNumber){
		boolean saved = false;
	
		File imagePath = new File(this.GPSSingleton.getSavePath() + "\\Resources\\");
		saved = PathUtils.saveImagePng(imagePath, this.image, "slide_" + sldNumber + ".png");
		
		return saved; 
	}
	

}
