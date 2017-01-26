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

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;

/***
 *  This class encapsulates the functions necessary for capturing images from the user's screen. 
 *  
 *  The following functions are supported:
 *  	Inserting an image into the Gallery from system clipboard 
 * 		(can be captured using "Print screen" key or using a 3rd party snipping tool)
 * 
 *  @author D. Lachmann
 */


import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;

public class ScreenCaptureTool {
    
	private Image cbImage = null;
//	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	
	/**
	 * Default Constructor 
	 * @return
	 * 
	 */
	public ScreenCaptureTool() {
		
	}
    
	/**
	 * returns an Image from the system clipboard
	 * @return Image - JavaFX 
	 */
	public Image getClipboardImage()
	{
		this.cbImage = null;
		Transferable clipboard = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		if(clipboard != null && clipboard.isDataFlavorSupported(DataFlavor.imageFlavor)){
		    try {
		     this.cbImage = SwingFXUtils.toFXImage((BufferedImage) clipboard.getTransferData(DataFlavor.imageFlavor), null);

		    } catch (SecurityException se) {
				se.printStackTrace();
			} catch (NullPointerException ne) {
				ne.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		    System.err.println("ScreenCaptureTool.getClipboardImage(): No Image found in clipboard!");
		}
		
		return this.cbImage;
	}
}
