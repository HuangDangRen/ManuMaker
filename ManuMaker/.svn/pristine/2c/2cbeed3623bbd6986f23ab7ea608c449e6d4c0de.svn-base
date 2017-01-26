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
package gallery.controller;

import gallery.abstractclasses.ControllerAbstr;
import gallery.model.GallerySlide;
import gallery.utils.ToolTipMaker;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class TagUtilsController extends ControllerAbstr {

	@FXML
	AnchorPane AP_TagUtils;
	@FXML
	TextArea TA_ToolTags;
	@FXML
	TextArea TA_PartTags;
	@FXML
	TextArea TA_InstructionTags;
	@FXML
	TitledPane TP_Tags;
	@FXML
	TitledPane TP_Parts;
	@FXML
	TitledPane TP_Tools;
	
	
	private GalleryBrowserController owningController = null;
	/** 
	 * Sets the reference to the owning controller
	 * @param nOC
	 */
	public void setOwningController(GalleryBrowserController nOC) {
		try{
			this.owningController = nOC;
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		this.invar("setOwningController() @ end of method");
 	}
	
	public void setEditable(boolean editable) {
		this.TA_InstructionTags.setEditable(editable);
		this.TA_PartTags.setEditable(editable);
		this.TA_ToolTags.setEditable(editable);
	}
	
	/**
	 * Enables / disables the controls of the controller.
	 * @param nBool
	 */
	public void disableGUI(boolean nBool) {
		this.TP_Parts.disableProperty().set(nBool);
		this.TP_Tools.disableProperty().set(nBool);
	}
	
	/**
	 * Does nothing here...
	 */
	@Override
	public void updateUI() {
		
	}

	@Override
	public void initCTRL() {
		this.TP_Tags.setTooltip(ToolTipMaker.simpleToolTip("Write keywords here to help the reader find specific information via the search function. "
				+ "\n\rGuidelines: \n"
				+ "\n-Give instruction-related tags, e.g. \"Remove valve rocker cover\""
				+ "\n-Separate Tags with a semicolon (;)."
				+ "\n-List as many synonyms as possible for the terms you tag, to make the search more likely to produce a good result."));
		
		this.TP_Tools.setTooltip(ToolTipMaker.simpleToolTip("List the tools required for this step."
				+ "\n\rGuidelines: \n"
				+ "\nSeparate tools with semiconlon"
				+ "\nUse the most common name possible for a tool."
				+ "\nDo not use synonyms, and be consistent in terminology accross the project. Reason: The toolbox for the Project can be compiled to a list to see which tools are "
				+ "required for the whole project. \n\r"
				+ "The reader wants to know he needs a 13mm Wrench. If it appears on the list a few times, he knows he needs it frequently, maybe to pack a spare. "
				+ "\nWhat he isn't interested in, is finding out how many terms he can use to refer to a \"13mm Wrench\"."));
		
		this.TP_Parts.setTooltip(ToolTipMaker.simpleToolTip("List the required parts used in this step. \n"
				+ "\rGuidelines: \n"
				+ "\n-Be consistent with part-names throughout the project.\""
				+ "\n-Separate Parts with a semicolon (;)."
				+ "\n-Use ONE expression per part. If you wish to enable searching by part name as well as part number, "
				+ "\n Write both in one tag, e.g. \"Valve rocker cover 25-01-98057\" (without the quotes) so the slide is found "
				+ "\n when searching for the name as well as when searching for the number"));

		this.TP_Tags.expandedProperty().setValue(true);
		
		
	}
	
	@FXML
	public void handleToolsText() {
		if(this.TA_ToolTags.getText() != null){
			String text = new String(this.TA_ToolTags.getText());
			if(this.owningController.currentSlide instanceof GallerySlide) {
				((GallerySlide)this.owningController.currentSlide).setTools(text);
			}
		}
	}
	
	@FXML
	public void handlePartsText() {
		if(this.TA_PartTags.getText() != null){
			String text = new String(this.TA_PartTags.getText());
			if(this.owningController.currentSlide instanceof GallerySlide) {
				((GallerySlide)this.owningController.currentSlide).setParts(text);
			}
		}
	}
	
	@FXML
	public void handleInstructionsText() {
		if(this.TA_InstructionTags.getText() != null){
			String text = new String(this.TA_InstructionTags.getText());
			this.owningController.currentSlide.setTags(text);
		}
	}

	@Override
	protected boolean invar(String text) {
		boolean check = true;
		
		check = check & (this.mainApp != null); 
		check = check & (this.owningController != null);
		
		if(check == false){
			System.out.println("\n\rTagUtilsController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
	
	

}
