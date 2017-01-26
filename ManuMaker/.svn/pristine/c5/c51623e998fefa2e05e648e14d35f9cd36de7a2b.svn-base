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

import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Annotation {
	
	// Private fields
	private SimpleStringProperty draftText = new SimpleStringProperty();
	private SimpleStringProperty finalText = new SimpleStringProperty();
	
	// Public fields
	public Annotation() {
		this.draftText.set("");
		this.finalText.set("");
	}
	
	
	/**
	 * Copies the draft annotation text into the final annotation container, 
	 * overwriting any existing rich text. 
	 * 
	 * If the parameter passed to this function is false, the draft text will only be copied
	 * if the final annotation is empty. 
	 * If the parameter is true, the text will be replaced with prejudice.
	 * If no parameter is passed, the user will be prompted whether to replace the 
	 * final text or not.
	 * @param evenIfNotEmpty
	 */
	public void reloadFinal(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Discard existing refined annotation?");
		alert.setHeaderText(alert.getTitle());
		alert.setContentText( "The annotation text in the \"Refine\"/\"View\" Tabs does not seem to be empty. \n\r"
							+ "Please confirm that you wish to replace it with the plaintext-annotation \n"
							+ "from the \"Draft\" Tab! \n\r"
							+ "This action cannot be undone.");

		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK){
			this.finalText.set(this.draftText.get());
		}
	}
	
	/**
	 * Copies the draft annotation text into the final annotation container, 
	 * overwriting any existing rich text. 
	 * 
	 * If the parameter passed to this function is false, the draft text will only be copied
	 * if the final annotation is empty. 
	 * If the parameter is true, the text will be replaced with prejudice.
	 * If no parameter is passed, the user will be prompted whether to replace the 
	 * final text or not.
	 * @param evenIfNotEmpty
	 */
	public void reloadFinal(boolean evenIfNotEmpty){
		if(evenIfNotEmpty == true) {
			this.finalText.set(this.draftText.get());
		} else if(this.finalText.get().isEmpty()){
			this.finalText.set(this.draftText.get());
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Discard existing refined annotation?");
			alert.setHeaderText(alert.getTitle());
			alert.setContentText( "The annotation text in the \"Refine\"/\"View\" Tabs does not seem to be empty. \n\r"
								+ "Please confirm that you wish to replace it with the plaintext-annotation \n"
								+ "from the \"Draft\" Tab! \n\r"
								+ "This action cannot be undone.");
	
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == ButtonType.OK){
				this.finalText.set(this.draftText.get());
			}
		
		}
	}
	

	/**
	 * Draft text string setter
	 * @param nText
	 */
	public void draftTextSet(String nText) {
		assert !(nText == null);
		this.draftText.set(nText);
	}
	
	/**
	 * Draft text string getter
	 * @return
	 */
	public String draftTextGet() {
		return this.draftText.get();
	}
	
	/**
	 * gets the draft string property
	 * @return
	 */
	public StringProperty draftTextProperty(){
		return this.draftText;
	}
	
	
	/**
	 * Draft text string setter
	 * @param nText
	 */
	public void finalTextSet(String nText) {
		assert !(nText == null);
		this.finalText.set(nText);
	}
	
	/**
	 * Draft text string getter
	 * @return
	 */
	public String finalTextGet() {
		return this.finalText.get();
	}
	
	/**
	 * gets the draft string property
	 * @return
	 */
	public StringProperty finalTextProperty(){
		return this.finalText;
	}
}
