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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import gallery.utils.WarningLevel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

@Deprecated
public class ImageOverlay {

	private HashMap<WarningLevel, Image> overlays = new HashMap<WarningLevel, Image>();
	
	public ImageOverlay() {
		this.overlays.put(WarningLevel.NONE, null);
		this.overlays.put(WarningLevel.GREEN, new Image("file:resources/overlays/Green.png"));
		this.overlays.put(WarningLevel.BLUE, new Image("file:resources/overlays/Blue.png"));
		this.overlays.put(WarningLevel.YELLOW, new Image("file:resources/overlays/Yellow.png"));
		this.overlays.put(WarningLevel.ORANGE, new Image("file:resources/overlays/Orange.png"));
		this.overlays.put(WarningLevel.RED, new Image("file:resources/overlays/Red.png"));
	}
	

	public Image getMarkedImage(Image baseImage, WarningLevel currentLevel){
		Image ret = null;
		BufferedImage baseBufImage = SwingFXUtils.fromFXImage(baseImage, null);
		if(!(currentLevel == WarningLevel.NONE)){
			int wb = (int) baseImage.getWidth();
			int hb = (int) baseImage.getHeight();
			
			BufferedImage frameImgDefault = SwingFXUtils.fromFXImage(this.overlays.get(currentLevel), null);
			BufferedImage resultImage = new BufferedImage(wb, hb, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graph2d = resultImage.createGraphics();
			
			graph2d.drawImage(baseBufImage, 0, 0, wb, hb, null);
			graph2d.drawImage(frameImgDefault, 0, 0, wb, hb, null);
			
			graph2d.dispose();		
			ret = SwingFXUtils.toFXImage(resultImage, null);
		} else {
			ret = baseImage;	
		}

		return ret;
	}
	
}
