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

import java.util.Calendar;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;



public class SMPFrame {
	private IntegerProperty ordinal = new SimpleIntegerProperty();
	private Image image;
	private SimpleStringProperty timestamp = new SimpleStringProperty(); 


	public SMPFrame(Integer nOrd, Image nImage){
		this.ordinal.set(nOrd);
		this.image = nImage;
		Calendar tempCal = Calendar.getInstance();
		this.timestamp.set(tempCal.getTime().toString());
	}
	
	public SMPFrame(){
		Calendar tempCal = Calendar.getInstance();
		this.timestamp.set(tempCal.getTime().toString());
	}
	
	public void setImage(Image nImage){
		this.image = nImage;
	}
	
	public Image getImage(){
		return this.image;
	}
	
	
	public void setOrdinal(Integer nOrd){
		this.ordinal.set(nOrd);
	}
	
	public Integer getOrdinal() {
		return this.ordinal.get();
	}
	
	public IntegerProperty ordinalProperty(){
		return this.ordinal;
	}
	
	public void setTimestamp(String nTime) {
		this.timestamp.set(nTime);
	}
	
	public String getTimestamp(){
		return this.timestamp.get();
	}
	
	public StringProperty timestampProperty(){
		return this.timestamp;
	}
	

	
	
}
