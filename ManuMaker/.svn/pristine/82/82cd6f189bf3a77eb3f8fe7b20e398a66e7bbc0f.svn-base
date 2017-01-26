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

import java.util.HashMap;

import gallery.abstractclasses.ControllerAbstr;
import gallery.abstractclasses.GalleryItem;
import gallery.model.Gallery;
import gallery.model.GallerySlide;
import gallery.model.SMPFrame;
import gallery.model.TagList;
import gallery.utils.TagType;
import gallery.utils.ToolTipMaker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * @author Daniel Lachmann
 *
 */
public class SlideListController extends ControllerAbstr{
	@FXML
	private TabPane TP_Slides;
	
	@FXML
	private Tab TAB_Index;
	
	@FXML
	private Tab TAB_Search;
	
	@FXML
	private TableView<GalleryItem> TV_IndexTable;
	
	@FXML 
	private TableColumn<GalleryItem, String> TC_colTitle_Index;
	
	@FXML 
	private TableColumn<GalleryItem, Integer> TC_colOrdinal_Index;

	@FXML
	private TableView<GalleryItem> TV_SearchTable;
 
	@FXML 
	private TableColumn<GalleryItem, String> TC_colTitle_Search;
	
	@FXML 
	private TableColumn<GalleryItem, Integer> TC_colOrdinal_Search;
	
	@FXML
	private MenuButton MB_TagSearch;
	
	@FXML
	private CheckMenuItem CMI_Instructions;
	
	@FXML
	private CheckMenuItem CMI_Tools;
	
	@FXML
	private CheckMenuItem CMI_Parts;
	
	@FXML
	private TextField TF_TagSearch;
	
	@FXML
	private TitledPane TP_Tags;
	
	@FXML
	private TitledPane TP_Tools;
	
	@FXML
	private TitledPane TP_Parts;
	
	@FXML
	private Button BT_Up;
	
	@FXML
	private Button BT_Down;
	
	private GalleryBrowserController owningController = null;
	private ObservableList<GalleryItem> searchResults = FXCollections.observableArrayList();
	private ObservableList<GalleryItem> slideList = FXCollections.observableArrayList();
	private Gallery gallery = null;
	private TagList searchWords = new TagList(); 
	
	
	
	private HashMap<TagType, TagList> makeTagMap(GalleryItem gItem){
		HashMap<TagType, TagList> tmp = new HashMap<TagType, TagList>();
		
		if(gItem.getClass().equals(GallerySlide.class)){
			if(gItem.getTagList().getTagList().size() > 0 && this.CMI_Instructions.selectedProperty().get()){
				tmp.put(TagType.TAGS, gItem.getTagList());	
			}
			if(((GallerySlide)gItem).getPartList().getTagList().size() > 0 && this.CMI_Parts.selectedProperty().get()){
				tmp.put(TagType.PARTS, ((GallerySlide)gItem).getPartList());	
			}
			if(((GallerySlide)gItem).getToolList().getTagList().size() > 0 && this.CMI_Tools.selectedProperty().get()){
				tmp.put(TagType.TOOLS, ((GallerySlide)gItem).getToolList());	
			}
		}
		return tmp;
	}
	/** 
	 * Sets the reference to the owning controller
	 * @param nOC
	 */
	public void setOwningController(GalleryBrowserController nOC) {
		try{
			this.owningController = nOC;
			this.gallery = this.owningController.activeGallery;
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		this.invar("setOwningController() @end of method");
 	}
	
	
	@Override
	public void updateUI() {
		if(this.gallery != null && this.gallery.getSlideCount() > 0){
			this.TV_IndexTable.disableProperty().set(false);
			this.TV_SearchTable.disableProperty().set(false);
			if(this.TAB_Index.selectedProperty().get() && this.gallery != null){
				this.TV_IndexTable.getSelectionModel().clearAndSelect(this.gallery.getCurrentIndex());
			}
		} else {
			this.TV_IndexTable.disableProperty().set(true);
			this.TV_SearchTable.disableProperty().set(true);
		}
		
		if(this.owningController.TAB_View.isSelected()){
			this.BT_Down.disableProperty().set(true);
			this.BT_Up.disableProperty().set(true);
		} else {
			this.BT_Down.disableProperty().set(false);
			this.BT_Up.disableProperty().set(false);
		}
	}

	@Override
	public void initCTRL() {
		
		this.TV_IndexTable.setTooltip(ToolTipMaker.simpleToolTip(	"This table shows all the slides in the Gallery. \n"
				+ "Chapter and Title slides are distinguished by their cell's background color.\n"
				+ "Navigate to any slide by clicking on it in the list.\n\n"
				+ "If specific information is required, try out the search bar below.\n"
				+ "Keep search terms short to match longer tags, \n"
				+ "e.g. searching for \"hammer\" also finds \"Sledge Hammer\"\n\n"
				+ "Search is not case-sensitive. Select the types of tags to search \n"
				+ "(General / Instruction related, Tools required for a step, Parts used in a step) \n"
				+ "in the \"Tag Search\" dropdown menu beside the search field."));
		
		this.TV_SearchTable.setTooltip(ToolTipMaker.simpleToolTip("This table displays the search results. Can be used for navigation."));
		//Column Ordinal Index:
		this.TC_colOrdinal_Index  = new TableColumn<>("#");
		
		this.TC_colOrdinal_Index.setCellValueFactory( 
				new PropertyValueFactory<GalleryItem, Integer>("slideOrdinal"));

		this.TC_colOrdinal_Index.setCellFactory(new Callback<TableColumn<GalleryItem, Integer>, TableCell<GalleryItem, Integer>>() {
			@Override
			public TableCell<GalleryItem, Integer> call(TableColumn<GalleryItem, Integer> param){
				return new TableCell<GalleryItem, Integer>() {
					
					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						
						if(!empty) {
							setText(item.toString());
//							System.out.println("SLC.initCTRL() -> TC_colOrdinal_Index update function: item == " + item);
						} else { 
							setText(null);
						}
					}
				};				
			}
		});
		
		
		//Column Title Index
		this.TC_colTitle_Index = new TableColumn<GalleryItem,String>("Slide Title");
		
		this.TC_colTitle_Index.setCellValueFactory( 
				new PropertyValueFactory<GalleryItem, String>("slideTitle"));
		
		this.TC_colTitle_Index.setCellFactory(new Callback<TableColumn<GalleryItem, String>, TableCell<GalleryItem, String>>() {
			@Override
			public TableCell<GalleryItem, String> call(TableColumn<GalleryItem, String> param){
				return new TableCell<GalleryItem, String>() {
					
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						
						if(!empty && item != "") {
							if(item.startsWith("Chapter")){
								setStyle("-fx-background-color:LightSteelBlue");
							} else if( item.startsWith("Project:")){
								setStyle("-fx-background-color:LightSlateGray");
							} else {
								setStyle("-fx-background-color:transparent");
							}
							setText(item);
//							System.out.println("SLC.initCTRL() -> TC_colOrdinal_Index update function: item == " + item);
						} else if(item == "") { 
							setText("Please set a slide title...");
						} else {
							setText(null);
						}
					}
				};				
			}
		});
		
		//Column Ordinal Search
		this.TC_colOrdinal_Search  = new TableColumn<>("#");
		
		this.TC_colOrdinal_Search.setCellValueFactory( 
				new PropertyValueFactory<GalleryItem, Integer>("slideOrdinal"));

		this.TC_colOrdinal_Search.setCellFactory(new Callback<TableColumn<GalleryItem, Integer>, TableCell<GalleryItem, Integer>>() {
			@Override
			public TableCell<GalleryItem, Integer> call(TableColumn<GalleryItem, Integer> param){
				return new TableCell<GalleryItem, Integer>() {
					
					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						
						if(!empty) {
							setText(item.toString());
						} else { 
							setText(null);
						}
					}
				};				
			}
		});
		
		
		//Column Title Search
		this.TC_colTitle_Search = new TableColumn<GalleryItem,String>("Slide Title");
		
		this.TC_colTitle_Search.setCellValueFactory( 
				new PropertyValueFactory<GalleryItem, String>("slideTitle"));
		
		this.TC_colTitle_Search.setCellFactory(new Callback<TableColumn<GalleryItem, String>, TableCell<GalleryItem, String>>() {
			@Override
			public TableCell<GalleryItem, String> call(TableColumn<GalleryItem, String> param){
				return new TableCell<GalleryItem, String>() {
					
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if(!empty) {
							if(item.startsWith("Chapter")){
								setStyle("-fx-background-color:LightSteelBlue");
							} else if( item.startsWith("Project:")){
								setStyle("-fx-background-color:LightSlateGray");
							} else {
								setStyle("-fx-background-color:transparent");
							}
					
							setText(item);
						} else if(item == "") { 
							setText("Please set a slide title...");
						} else {
							setText(null);
						}
					}
				};				
			}
		});
		
		this.TC_colOrdinal_Index.setMinWidth(30.0);
		this.TC_colOrdinal_Index.setMaxWidth(40.0);	
		this.TC_colOrdinal_Index.setPrefWidth(40.0);
		this.TC_colOrdinal_Index.setSortable(false);
		this.TC_colTitle_Index.setSortable(false);
		this.TV_IndexTable.setItems(this.owningController.activeGallery.getGallerySlides());
		this.TV_IndexTable.getColumns().set(0, this.TC_colOrdinal_Index);
		this.TV_IndexTable.getColumns().set(1, this.TC_colTitle_Index);
		
		this.TC_colOrdinal_Search.setMinWidth(30.0);
		this.TC_colOrdinal_Search.setMaxWidth(40.0);	
		this.TC_colOrdinal_Search.setPrefWidth(40.0);
		this.TC_colOrdinal_Search.setSortable(false);
		this.TC_colTitle_Search.setSortable(false);
		this.TV_SearchTable.setItems(this.searchResults);
		this.TV_SearchTable.getColumns().set(0, this.TC_colOrdinal_Search);
		this.TV_SearchTable.getColumns().set(1, this.TC_colTitle_Search);
		
	}
	
	@FXML
	public void handleTabChange() {
		if(this.owningController != null) {
			this.gallery = this.owningController.activeGallery;	
		}
		
		//this.updateUI();
	}
	
	
	@FXML
	public void handleTagSearch() {
		searchResults.clear();
		slideList = this.owningController.activeGallery.getGallerySlides();
		searchWords.setTags(this.TF_TagSearch.getText());
		System.out.println("\n\rSearching for tags: " + searchWords.getTagString());
		GalleryItem gItem = null;
		
		for(GalleryItem i : slideList){
			gItem = i.containsTags(this.makeTagMap(i), searchWords.getTagList());
			
			
			if(gItem != null) {
				searchResults.add(gItem);
				System.out.println("\n\rFound Tags in Slide \"" + gItem.getSlideTitle() + "\" for tags: " + gItem.getTags());
			}
		}
		
		if(this.searchResults.size() > 0){
			this.TP_Slides.getSelectionModel().select(this.TAB_Search);
		}
		
		this.updateUI();
	}
	
	
	
	/**
	 * Navigate to slide which was clicked on in the index list
	 */
	@FXML
	public void handleIndexListSelection() {
		this.owningController.returnCurrentSlideToGallery();
		this.owningController.activeGallery.setCurrentIndex(this.TV_IndexTable.getSelectionModel().getSelectedIndex());
		this.owningController.updateCurrentSlide();
		this.owningController.updateUI();
	}
	
	/**
	 * Navigate to slide which was clicked on in the search-result list
	 */
	@FXML
	public void handleSearchListSelection() {
		this.owningController.returnCurrentSlideToGallery();
		this.owningController.activeGallery.setCurrentIndex(this.searchResults.get(this.TV_SearchTable.getSelectionModel().getSelectedIndex()).getSlideOrdinal()-1);
		this.owningController.updateCurrentSlide(); 
		this.owningController.updateUI();
	}
	
	@FXML
	public void handleMoveUp() {
		if(this.gallery.getCurrentIndex() > 0){
			GalleryItem tempItem = this.gallery.getCurrentItem();
			
			this.gallery.deleteSlide();
			this.gallery.newSlide(this.gallery.getCurrentIndex()-1, tempItem);
			this.gallery.setCurrentIndex(this.gallery.getCurrentIndex() -1 );
			
			this.gallery.updateOrdinals();
			this.updateUI();
		}
	}

	@FXML
	public void handleMoveDown() {
		
		if(this.gallery.getCurrentIndex() < this.gallery.getSlideCount() - 1){
			GalleryItem tempItem = this.gallery.getCurrentItem();
			
			this.gallery.deleteSlide();
			this.gallery.newSlide(this.gallery.getCurrentIndex() + 1, tempItem);
			this.gallery.setCurrentIndex(this.gallery.getCurrentIndex() + 1);
			
			this.gallery.updateOrdinals();
			this.updateUI();
		}
	}
	@Override
	protected boolean invar(String text) {
		boolean check = true;

		check = check & (this.mainApp != null); 
		check = check & (this.owningController != null); 
		
		if(check == false){
			System.out.println("\n\rSlideListController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
	

}
