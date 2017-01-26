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



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import gallery.abstractclasses.GalleryItem;
import gallery.model.GalleryProjectSettings;
import gallery.model.GallerySlide;
import gallery.utils.PathUtils;
import gallery.utils.SlideUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Gallery {
	
	// Private fields
	//private List<GallerySlide> gallerySlides = new ArrayList<GallerySlide>();
	private ObservableList<GalleryItem> gallerySlides = FXCollections.observableArrayList();
	
	private Integer currentSlideIndex = 0; 	
	public GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE; 
	public ApplicationSettings 	AppSettingsSingleton = ApplicationSettings.INSTANCE;
	public GalleryItem currentSlide = null;


	

	/**
	 * Updates the gallerySlides' index Ordinals
	 */
	public void updateOrdinals(){
		int k = 1;
		for(int i = 0; i < this.getSlideCount(); i++) {
			this.gallerySlides.get(i).setSlideOrdinal(i+1);
			if(this.gallerySlides.get(i).getClass().equals(GalleryChapter.class)){
				((GalleryChapter)this.gallerySlides.get(i)).setChapterNo(k);
				k++;
			}
		}
		if(this.currentSlideIndex >= this.getSlideCount()){
			this.currentSlideIndex = this.getSlideCount()-1;
		}
	}	

	/**
	 * This overloaded Method adds a Slide to the Gallery. 
	 * If no Index given, new Slide is added at the end, if index is given, Slide is inserted at that position. 
	 */
	public void newSlide() {
		this.gallerySlides.add(new GallerySlide());
		this.updateOrdinals();
	}

	public void newSlide(int newSlideIndex) { 
		this.gallerySlides.add(newSlideIndex,new GallerySlide());
		this.updateOrdinals();
	} 
	
	public void newSlide(int newSlideIndex, GalleryItem nSlide) {
		if(nSlide != null) {
			this.gallerySlides.add(newSlideIndex,nSlide);
			this.updateOrdinals();
		}
	} 
	
	
	
	// Public fields
	/**
	 * Runnable for capturing images in separate thread: 
	 */

	/**
	 * Default constructor. 
	 */
	public Gallery() {
		// Default constructor
	}
	
	public Gallery(boolean empty) {
		if(empty == false) {
			this.gallerySlides.add(new GallerySlide());
			this.updateOrdinals();
		}
	}
	
	/**
	 * Initializing Method:
	 */
	public void init() {
		this.GPSSingleton.setGallery(this);
		this.updateOrdinals();
	}
	
	/**
	 * Returns the Observable gallerySlides
	 */
	public ObservableList<GalleryItem> getGallerySlides(){
		return this.gallerySlides;
	}

	/**
	 * Method to test if current slide has been initialized: 
	 */
	public boolean slideInitialized(){
		return this.gallerySlides.get(this.currentSlideIndex).isInitialized();
	}
	
	/**
	 * Returns current index
	 * @return
	 */
	public Integer getCurrentIndex() {
		return this.currentSlideIndex;
	}
	
	/**
	 * Sets current index
	 */
	public void setCurrentIndex(Integer index) {
		if(index >= 0 && index < gallerySlides.size()){
			this.currentSlideIndex = index.intValue();
		}
	}
	
	/**
	 * Returns whether any images have been initialized. 
	 */
	public boolean galleryInitialized() {
		boolean initialized = false; 
		
		for(GalleryItem cSlide:this.gallerySlides) {
			initialized = initialized || cSlide.isInitialized();
		}
		
		return initialized;
	}
		
	

	
	/**
	 * Deletes the slide 
	 */
	public String deleteSlide() {
		String outstring = null;
		boolean deleted = false; 
		if(this.gallerySlides.size() >= 2) {
			// Remove Audio slide if present:
//			if(this.currentSlide != null && this.currentSlide.getClass().equals(GallerySlide.class)){
//				System.out.print("Gallery.deleteSlide() -> Deleting audioDraft");
//				((GallerySlide)this.currentSlide).deleteAudioDraft();
//			}
			deleted = this.gallerySlides.remove(this.gallerySlides.get(this.currentSlideIndex));
			outstring = String.format("\n\rSlide %d deleted...............",this.currentSlideIndex);
			System.out.print(deleted);
			this.GPSSingleton.updateSettings();
			this.updateOrdinals();
		}
		else {
			outstring = "\n\rYou cannot delete the last slide in a Gallery";
		}
		
		return outstring;
	}
	
	
	/**
	 * Move to next slide. Add new Slide if none exists & edit mode is enabled.
	 * @return
	 */
	public String nextSlide(boolean forceNext) {
		String outstring = null;
		if (forceNext && (this.currentSlideIndex+1 == this.gallerySlides.size())) {
			newSlide();
			this.currentSlideIndex++;
			outstring = "New slide";
		}
		else if (this.currentSlideIndex+1 < this.gallerySlides.size()) { 
			this.currentSlideIndex++;	
			outstring = "Next slide";
		}
		else{
			outstring = "End of gallery";
		}
	//	System.out.println(outstring);
		return outstring;
	}
	
	/**
	 * Move back to previous Slide. Not further than 0.
	 * @return
	 */
	public String prevSlide() {
		String outstring = null;
		if (this.currentSlideIndex >= 1) {
			this.currentSlideIndex--;
			outstring = "Previous slide";
		}
		else {
			outstring = "Reached first slide";
		}
	//	System.out.println(outstring);
		return outstring;
	}
	

	
	/**
	 * Get the current Slide.
	 * @return
	 */
	public GalleryItem getCurrentItem() {
		if (!this.gallerySlides.isEmpty()) {
			return this.gallerySlides.get(this.currentSlideIndex);
		}else {
			return new GallerySlide();
		}
	}
	
	public void setCurrentItem(GalleryItem nSlide) {
		String absPlaceholder = "file:\\" + this.GPSSingleton.getSafetyDataPath();
		if(nSlide != null) {
			this.gallerySlides.set(this.currentSlideIndex, nSlide);
			System.out.println(nSlide.annotation.finalTextGet());
			this.updateOrdinals();
		} else {
		//	System.out.println("\n\rGallery.setCurrentItem(nSlide) -> Argument is null!\n\r");
		}
	}
	
	/**
	 * Returns the number of Slides currently in the Gallery
	 */
	public Integer getSlideCount() {
		if(this.gallerySlides != null){
			return this.gallerySlides.size();
		}
		else {
			return 0;
		}
	}
	
	/**
	 * Returns a List<String> containing all the parts listed in all
	 * Slides in their respective tag-fields. 
	 * 
	 * @return
	 */
	public List<String> getAllParts() {
		List<String> parts = new ArrayList<String>();
		
		for(GalleryItem i:gallerySlides){
			if(i.getClass().equals(GallerySlide.class) && ((GallerySlide)i).getPartList().isEmpty() == false){
				parts.addAll(((GallerySlide)i).getPartList().tagList);
			}
		}
		return parts;
	}
	
	
	
	/**
	 * Returns a List<String> containing all the tools listed in all
	 * Slides in their respective tag-fields. 
	 * 
	 * @return
	 */
	public List<String> getAllTools() {
		List<String> tools = new ArrayList<String>();
		
		for(GalleryItem i:gallerySlides){
			if(i.getClass().equals(GallerySlide.class) && ((GallerySlide)i).getToolList().isEmpty() == false){
				tools.addAll(((GallerySlide)i).getToolList().tagList);
			}
		}
		return tools;
	}


	/**
	 * Set up a new Gallery 
	 */
	public void newGallery(boolean empty) {
		this.init();
		this.gallerySlides.clear();
		if(!empty) {
			this.gallerySlides.add(new GallerySlide());
			this.updateOrdinals();
		}
		this.currentSlideIndex = 0;
	}
	
	/**
	 * Save the Gallery's slides in the directory specified in the project path.
	 */
	public String saveGallery() {
		BufferedWriter textWriter = null;
		File manifestPath = new File(this.GPSSingleton.getSavePath()+"\\Resources\\");	
		boolean save = true;
		String outString = null;
		this.GPSSingleton.updateSettings();
		if(save == true) {
			// Make Manifest
			String manifest = new String("");
			
			for(GalleryItem i:this.gallerySlides){
				manifest = manifest + i.getClass().getSimpleName() + "\n"; 	// save each slide in new line
			}
			// Save Manifest
			PathUtils.checkIfPathExistsAndMake(manifestPath);
			try {
				textWriter = new BufferedWriter( new FileWriter(manifestPath.getAbsolutePath() + "\\manifest.txt"));
			    textWriter.write(manifest);
				// Save GalleryItems
				for (int i = 0; i < this.GPSSingleton.getSlideCount(); i++) {
					this.gallerySlides.get(i).galleryItemSave(i, null);
				}
							
				// Save Project Settings
				this.GPSSingleton.saveSettings();
				outString = String.format("Gallery Project saved at " + this.GPSSingleton.getSavePath());
				
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("ERROR: Manifest file creation failed!");
		    	alert.setHeaderText("ERROR: Manifest file creation failed!");
		    	alert.setContentText(	"The project structure may be corrupted. \n\r"
		    					+ 		"Attempt saving the project in a different directory."
		    					+ 		"Otherwise, some data may be salvageable through manual \n"
		    					+ 		"intervention.");
		    	alert.showAndWait();
				e.printStackTrace();
			} finally {
		        try  {
		            if ( !(textWriter == null))
		            	textWriter.close( );
		        } catch ( IOException e){ 
		        	e.printStackTrace();
		        }
		    }  

		} else {
			outString = String.format("Some slides do not have instructions (text or image) attached, \n\ror have not been given a title. \n\rCannot perform save operation.");
		}
		System.out.println(outString);
		return outString;
	}
	
	/**
	 * load the Gallery's slides and add them to the List
	 */
	public String loadGallery() {
		String outString = null;
		List<String> manifest = null;
		this.newGallery(true);	
	//	System.out.println("\n\rLoading ProjectSettings from: "+this.GPSSingleton.getSavePath()+"Settings\\");
		
	//	System.out.println("\n\rLoading Project Manifest from: "+this.GPSSingleton.getSavePath()+"Resources\\");
		File projectFilePath = new File(this.GPSSingleton.getSavePath() + "\\Resources\\");	

	    if (projectFilePath.exists()) {
	    	//Load settings
	    	try {
				this.GPSSingleton.loadSettings();
			} catch (IOException e) {
		    	Alert alert = new Alert(AlertType.WARNING);
		    	alert.setTitle("WARNING: Project settings could not be loaded!");
		    	alert.setHeaderText("Project settings not loaded!");
		    	alert.setContentText(	"Default settings will be loaded and saved to \n"
		    					+ 		"replace the missing or corrupted file.");
		    	alert.showAndWait();
		    	this.GPSSingleton.init();
			}
			
	    	
	    	// Load manifest 
			try {
				manifest = Files.readAllLines(Paths.get(projectFilePath.getPath() + "\\manifest.txt")); 
				outString = String.format("\n\rSuccessfully Project Manifest\n\r");
				
				if(manifest != null) {
					for (int i = 0; i < manifest.size(); i++) {		// add existing Gallery based on project path set in GPSSingleton
		
						this.gallerySlides.add(SlideUtils.getNewSlide(manifest.get(i), i));
		
						outString = String.format("Successfully loaded slide No. %2d",i);
					//	System.out.println(outString);
					}
					
					outString = String.format("Successfully loaded project from " + this.GPSSingleton.getSavePath());
				}
			} catch (IOException e) {
		    	Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("ERROR: Project manifest could not be loaded!");
		    	alert.setHeaderText("Project manifest not loaded!");
		    	alert.setContentText(	"The project file-structure may be corrupted.");
		    	alert.showAndWait();
				e.printStackTrace();
			}

	    } else {
	    	outString = String.format("Manifest file could not be found at " + projectFilePath.getPath() + "\\Resources\\manifest.txt" + ".");
	    }

	
		
		return outString; 
	}
	
	
}
