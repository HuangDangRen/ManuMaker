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


import java.util.ArrayList;
import java.util.List;
import gallery.abstractclasses.GalleryItem;
import javafx.scene.image.Image;

/**
 * This class defines a GalleryItem which contains safety-related information and an Image. 
 * 
 * @author Daniel Lachmann
 *
 */
public class GallerySafetySlide extends GalleryItem {
		
	protected SlidePic picture = null; 
	
	
	public GallerySafetySlide() {
		this.picture = new SlidePic();
	}
	
	public GallerySafetySlide(Image nPicture) {
		this.picture = new SlidePic();
		this.picture.setImage(nPicture);

	}
	
	public GallerySafetySlide(Integer slideNo) {
		this.picture = new SlidePic();
		galleryItemLoad(slideNo);							//	Load File at savePath
	}


	
	public SlidePic getPicture() {
		return this.picture;
	}

	
	public void setPicture(SlidePic nPic){
		this.picture = nPic;
	}
	
	public void deletePicture() {
		this.picture = new SlidePic();
	}
	
	
	@Override
	public boolean isInitialized() {
		boolean ret = false; 
		ret = super.isInitialized();
		ret = ret || (((this.annotation.draftTextGet() != "") || (this.annotation.finalTextGet() != "")) || this.picture.getImage() != null);	
		
		return ret;
	}
	
	public void imageCopy(Image nImage) {
		this.picture.copyImage(nImage);
	}
	
		
	
	public List<String> galleryItemLoad(Integer sldNumber) {
		List<String> data = new ArrayList<String>();
		if(sldNumber != null){
			data = super.galleryItemLoad(sldNumber);
			this.picture.loadImage(sldNumber);
		}		
		return data;
	}

	
	public void galleryItemSave(Integer sldNumber, List<String> nData) {
		
		if(sldNumber != null){
			this.picture.saveImage(sldNumber);
		}
	
		
		super.galleryItemSave(sldNumber, nData);
	}
}
