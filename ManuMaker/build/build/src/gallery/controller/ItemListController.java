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

import java.util.List;

import gallery.abstractclasses.ControllerAbstr;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

public class ItemListController extends ControllerAbstr {
	@FXML
	private TitledPane TP_ItemList;
	@FXML
	private ListView<Integer> LV_Ordinal;
	@FXML
	private ListView<String> LV_Item;
	
	private ObservableList<String> items = FXCollections.observableArrayList();
	private ObservableList<Integer> ordinals = FXCollections.observableArrayList();
	private ControllerAbstr owningController = null;
	private Stage dialogStage;
	
	public void setOwningController(ControllerAbstr nOwner){
		this.owningController = nOwner;
	}
	
	/**
	 * The ItemList use their own stage.
	 *
	 */
	public void setStage(Stage nDialogStage) {
		this.dialogStage = nDialogStage;
	}
	
	@Override
	protected boolean invar(String text) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setItemList(List<String> nList, String nTitle){
		if(nTitle != null) {
			this.TP_ItemList.setText(nTitle);
		}
		
		if(nList != null){
			items.setAll(nList);
			ordinals.clear();
			for(Integer i = 0; i < items.size(); i++){
				ordinals.add(i, i+1); // Start displayed lists with 1 not 0
			}
		} else {
			items.clear();
			ordinals.clear();
		}
		this.updateUI();
	}
	
	public void clearList() {
		items.clear();
		this.updateUI();
	}
	
	public void handleClose() {
		this.dialogStage.close();
	}
	
	@Override
	public void updateUI() {
		this.LV_Item.setItems(items);
		this.LV_Ordinal.setItems(ordinals);

	}

	@Override
	public void initCTRL() {
		this.updateUI();
		
	}

}
