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

import gallery.controller.GalleryBrowserController;
import gallery.model.GalleryProjectSettings;

public class SubControllerAbstr extends ControllerAbstr {

	private GalleryBrowserController galleryBrowser = null;
	private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	
	public void setOwner(GalleryBrowserController nGBC) {
		this.galleryBrowser = nGBC;
		if(!invar("SubControllerAbstr.setOwner( ): invar not satisfied!")) {
			return;
		}
	}
	
	@Override 
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null);
		check = check & (this.galleryBrowser != null);
		
		if(check == false) {
			System.out.println(text);
		}
		return check;	
	}
	
	@Override
	public void updateUI() {
		if(!invar("SubControllerAbstr.updateUI( ): invar not satisfied @start of method!")) {
			return;
		}
		if(!invar("SubControllerAbstr.updateUI( ): invar not satisfied @end of method!")) {
			return;
		}
	}

	@Override
	public void initCTRL() {
		if(!invar("SubControllerAbstr.initCTRL( ): invar not satisfied @start of method!")) {
			return;
		}
		if(!invar("SubControllerAbstr.initCTRL( ): invar not satisfied @start of method!")) {
			return;
		}
	}
	
	

}
