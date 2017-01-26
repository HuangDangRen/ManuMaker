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

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class ToolTipMaker {
		
	public static Tooltip simpleToolTip(String nText){
		Tooltip nTooltip = new Tooltip();
		nTooltip.setFont(Font.font(null, 12));
		if(nText != null){
			nTooltip.setText(nText);
		} else { 
			nTooltip.setText("WARNING: NO VALID TEXT PASSED TO TOOLTIP!");
		}
		
		return nTooltip;	
	}
	
	public static Tooltip simpleToolTip(String nText, int size){
		Tooltip nTooltip = simpleToolTip(nText);
		nTooltip.setFont(Font.font(null, size));
		
		return nTooltip;
	}
	

	public static Tooltip textImageToolTip(String nText, Image nImage){
		Tooltip nTooltip = simpleToolTip(nText);

		
		if(nImage != null){
			ImageView nImageView = new ImageView();
			nImageView.setImage(nImage);
			nTooltip.setGraphic(nImageView);
		}
		
		return nTooltip;	
	}
	
	public static Tooltip textImageToolTip(String nText, int size, Image nImage){
		Tooltip nTooltip = simpleToolTip(nText, size);

		
		if(nImage != null){
			ImageView nImageView = new ImageView();
			nImageView.setImage(nImage);
			nTooltip.setGraphic(nImageView);
		}
		
		return nTooltip;	
	}
}
