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
package gallery.utils;

import gallery.abstractclasses.GalleryItem;
import gallery.model.GalleryChapter;
import gallery.model.GallerySafetySlide;
import gallery.model.GallerySlide;
import gallery.model.GalleryTitle;

public class SlideUtils {

	public static GalleryItem getNewSlide(String classname, Integer sldNumber){
		GalleryItem ret = null; 
		
		switch(classname){
		case "GalleryTitle":
			if(sldNumber == null){
				ret = new GalleryTitle();
			} else {
				ret = new GalleryTitle(sldNumber);
			}
			break;
		case "GalleryChapter":
			if(sldNumber == null){
				ret = new GalleryChapter();
			} else {
				ret = new GalleryChapter(sldNumber);
			}
			break;
		case "GallerySafetySlide":
			if(sldNumber == null){
				ret = new GallerySafetySlide();
			} else {
				ret = new GallerySafetySlide(sldNumber);
			}
			break;
			
		default: 
			if(sldNumber == null){
				ret = new GallerySlide();
			} else {
				ret = new GallerySlide(sldNumber);
			}
			break;
			
		}
		return ret;
	}
}
