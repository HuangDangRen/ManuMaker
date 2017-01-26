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



import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import gallery.model.Annotation;
import gallery.model.GalleryProjectSettings;
import gallery.model.TagList;
import gallery.utils.PathUtils;
import gallery.utils.TagType;
import gallery.utils.TypeUtils;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 * This is the base class from which all types of gallery slides are derived. 
 * 
 * @author Daniel Lachmann
 *
 */
public abstract class GalleryItem {
	public GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE; 
	protected HashMap<String, String> textFields = new HashMap<String, String>();
	protected final TagList tags = new TagList(); 
	public Annotation annotation = new Annotation();		// access directly 
	private IntegerProperty slideOrdinal = new SimpleIntegerProperty(); 
	private StringProperty slideTitle = new SimpleStringProperty(""); 
	public GalleryItem(){
			
			this.textFields.put("ORDINAL", "");
			this.textFields.put("TITLE", "");
	}
	
	public GalleryItem(Integer ord){
		
		this.textFields.put("ORDINAL", ord.toString());
		this.textFields.put("TITLE", "");
		this.slideOrdinal.set(ord);
	}
	
	
	/**
	 * Returns true if the slide contains information.
	 * The Annotation text fields are checked for empty strings.
	 * Tag fields are not evaluated.
	 * @return
	 */
	public boolean isInitialized() {
		boolean ret = false; 

		ret = ret || ((this.annotation.draftTextGet() != "") || (this.annotation.finalTextGet() != ""));	
		
		return ret;
	};
	
	/**
	 * Sets the Image title
	 */
	public void setSlideTitle(String nTitle) {
		this.slideTitle.setValue(nTitle);
		this.textFields.put("TITLE", nTitle);
		//this.slideTitle = new String(nTitle);
	}
	
	/**
	 * Returns image title
	 */
	public String getSlideTitle() {
		return this.textFields.get("TITLE");
		//return this.slideTitle.get();
	}
	

	/**
	 * Experimental: returns the title as a string property:
	 */
	public StringProperty slideTitleProperty(){
		return this.slideTitle;
	}

	/**
	 * sets slide number
	 */
	public void setSlideOrdinal(Integer nOrd){
		this.textFields.put("ORDINAL", nOrd.toString());
		this.slideOrdinal.set(nOrd);
	}
	
	/**
	 * gets slide number
	 */
	public Integer getSlideOrdinal(){
		return Integer.valueOf(this.textFields.get("ORDINAL"));
//		return this.slideOrdinal.get();
	}
		
	
	/**
	 * Experimental: returns the slideOrdinal as an integer property:
	 */
	public IntegerProperty slideOrdinalProperty(){
		return slideOrdinal;
	}
	
	/**
	 * Gets the tags contained in one string separated by a delimiter
	 * @return [String]
	 */
	public String getTags() {
		return this.tags.getTagString();
	}
	
	/**
	 * Takes a string containing substrings separated by a delimiter and 
	 * stores them.
	 * @param nTags [String]
	 */
	public void setTags(String nTags) {
		this.tags.setTags(nTags);
	}
	
	public TagList getTagList() {
		return this.tags;
	}
	
	/**
	 * Returns the GalleryItem object if it contains the specified tags
	 */
	public GalleryItem containsTags(HashMap<TagType, TagList> Tags, List<String> searchWords){
		boolean matched = false;
		
		List<TagType> keyList = TypeUtils.getKeyList(Tags.keySet());
		
		for(TagType i : keyList){
			if(Tags.get(i).matched(searchWords)){
				matched = true;
			}
		}
		if(matched){
			return this;
		} else {
			return null;
		}
	}
	
	
	/**
	 *	Loads the components common to all file system 
	 *
	 * @param sldNumber
	 */
	public List<String> galleryItemLoad(Integer sldNumber){
		List<String> data = new ArrayList<String>();
	 	File projectPath = new File(this.GPSSingleton.getResourcePath() + "GalleryItem" + sldNumber + ".txt");
	 	Integer iteratorCTR = 0;
	 
	 	try {
		  // Load GalleryItem data: 
	 		data = Files.readAllLines(projectPath.toPath().toAbsolutePath(), Charset.forName("UTF-8")); 
	 	} catch (IOException e) {
	 		e.printStackTrace();
	 	}
	 	
	 	// Get Text Fields: 
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("§TEXTFIELDS[")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"§TEXTFIELDS[\"");
	 	}
	 	iteratorCTR++;
	 	List<String> keys = TypeUtils.getKeyList(textFields.keySet());
	 	String itData = null;
	 	
	 	//while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("]TEXTFIELDS§")) {
	 	for(Integer i = 0; i < keys.size() && iteratorInvar(data, i); i++){
	 		int ctrk = 1;
	 		itData = data.get(iteratorCTR);
	 		if(itData.startsWith("§")){ // Save to new TextField
	 		//	System.out.println("GalleryItem.galleryItemLoad() -> Found §, replacing...");
	 			itData = itData.replace("§", "");
	 			ctrk = 1;
	 			textFields.put(keys.get(i), itData);
	 		} else if(!itData.isEmpty()) { // Append last TextField
	 			textFields.put(keys.get(i-ctrk), textFields.get(keys.get(i-ctrk)) + "\n" + itData);
	 			ctrk++;	 			
	 		}
	 		iteratorCTR++;
	 	//	System.out.println("\n\rGalleryItem.galleryItemLoad() -> Setting textFields[" + keys.get(i) + "] = " + itData);
	 	}
	 	
	 	// Get Tags:
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("§TAGS[")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"§TAGS[\"");
	 	}
	 	iteratorCTR++;
	 	this.tags.setTags(data.get(iteratorCTR++).replace("§", "")); 	
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("]TAGS§")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"]TAGS§\"");
	 	}
	 	
	 	// Get Annotation Draft
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("§ANNODRAFT[")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"§ANNODRAFT[\"");
	 	}
	 	iteratorCTR++;
	 	this.annotation.draftTextSet(data.get(iteratorCTR++).replace("§", "")); 	
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("]ANNODRAFT§")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"]ANNODRAFT§\"");
	 	}
	 	
	 	// Get Annotation Final
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("§ANNOFINAL[")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"§ANNOFINAL[\"");
	 	}
	 	iteratorCTR++;
	 	this.annotation.finalTextSet(data.get(iteratorCTR++).replace("§", "")); 	
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("]ANNOFINAL§")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"]ANNOFINAL§\"");
	 	}
	 	 	
	 	if(this.textFields.get("ORDINAL") != null && this.textFields.get("ORDINAL") != ""){
	 		this.slideOrdinal.set(Integer.parseInt(this.textFields.get("ORDINAL")));
	 	} 
	 	this.slideTitle.set(this.textFields.get("TITLE"));
	 	return data;
	};
	
	/**
	 * Prototype for saving a slide to file system
	 * 
	 * @param sldNumber
	 */
	public void galleryItemSave(Integer sldNumber, List<String> nData){
		List<String> data = null; //new ArrayList<String>();
		BufferedWriter textWriter = null;
		
		if(nData == null){
			data = new ArrayList<String>();
		} else {
			data = nData;
		}
		
		// 1. Add TextFields 
		Set<String> keys = this.textFields.keySet();
		data.add("§TEXTFIELDS[");
		for(String i : keys) {
			data.add("§" + TypeUtils.newlinesToHtml(this.textFields.get(i))); // Add § as starting mark and newline to every field
		} 
		data.add("]TEXTFIELDS§");
		
		// 2. Add Tags 
		data.add("§TAGS[");
		data.add("§" + this.getTags());
		data.add("]TAGS§");
		// Add Annotation

		data.add("§ANNODRAFT[");
		data.add("§" + TypeUtils.removeNewlines(this.annotation.draftTextGet()));
	
		data.add("]ANNODRAFT§");
		
		data.add("§ANNOFINAL[");
		data.add("§" + TypeUtils.newlinesToHtml(this.annotation.finalTextGet()));
		data.add("]ANNOFINAL§");
		
		// Write to file
		File filePath = new File(this.GPSSingleton.getResourcePath() + "GalleryItem" + sldNumber + ".txt");
		PathUtils.checkIfPathExistsAndMake(this.GPSSingleton.getResourcePath());
		PathUtils.textFileSaveLines(filePath, data);
	};
	
	public static boolean iteratorInvar(List<String> list, Integer ctr){
		boolean ret = true; 
		if(ctr >= list.size()){
			ret = false;
		}
		
		return ret;
	}
	
}
