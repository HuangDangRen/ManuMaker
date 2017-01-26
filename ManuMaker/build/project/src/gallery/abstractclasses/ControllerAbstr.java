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

import gallery.Main;
import gallery.model.ApplicationSettings;
import javafx.scene.layout.AnchorPane;

public abstract class ControllerAbstr {
	protected Main mainApp = null; 
	protected ApplicationSettings AppSettingsSingleton = ApplicationSettings.INSTANCE;
	
	protected abstract boolean invar(String text);
//	{
//		boolean check = true;
//		
//		check = check & (this.mainApp != null); 
//		
//		if(check == false){
//			System.out.println("\n\r" + text + "--> invar not satisfied!\n\r");
//		}
//		return check;
//	}
	/**
	 * Sets the anchors of a given child note to 0.0 so it will fit 
	 * precisely into its parent.
	 * @param child
	 */
	public void fitAPToParent(AnchorPane child) {
		AnchorPane.setBottomAnchor(child, 0.0);
		AnchorPane.setLeftAnchor(child, 0.0);
		AnchorPane.setRightAnchor(child, 0.0);
		AnchorPane.setTopAnchor(child, 0.0);
	}
	
	
	/**
	 * Generic method for UI-controllers to recompute relevant user info.
	 */
	public abstract void updateUI();
	
	/**
	 * Initializes the controller
	 */
	public abstract void initCTRL();

	
	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
		//this.invar("ControllerAbstr.setMainApp() invar not satisfied! See " + this.toString());
	}
}
