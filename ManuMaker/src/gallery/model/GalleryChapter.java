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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import gallery.abstractclasses.GalleryItem;
import gallery.utils.TypeUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class defines all  properties needed for displaying a chapter-title slide.
 * 
 * @author Daniel Lachmann
 *
 */
public class GalleryChapter extends GalleryItem {
	protected HashMap<String, String> placeholders = new HashMap<String, String>();
	protected String templateCode = null;

	
	/**
	 * replaces a placeholder in the annotation HTML text with new content. 
	 * If placeholder is not found, nothing will be replaced.  
	 * @param placeholder 	[String]
	 * @param nContent		[String]
	 */
	protected void fillTemplateField(String placeholder, String nContent) {
		String temp = this.annotation.finalTextGet().replaceAll(placeholder, nContent);
//		System.out.println("GalleryChapter.fillTemplateField -> Placeholder found: " + temp.contains(placeholder));
//		System.out.println("GalleryChapter.fillTemplateField -> Filling placeholder " + placeholder + " with " + nContent);
//		System.out.println("GalleryChapter.fillTemplateField -> Placeholder still present: " + temp.contains(placeholder));
		this.annotation.finalTextSet(temp);
	}
	
	protected void cleanLeftoverPlaceholders(){
		Set<String> keyList = this.placeholders.keySet();
		for(String i: keyList) {
			this.annotation.finalTextGet().replaceAll(this.placeholders.get(i), "");
		}
	}
	
	private void init(){
		try {
			this.templateCode = new String("");
			List<String> lines = Files.readAllLines(Paths.get("resources/html_templates/ChapterTemplate.html"),Charset.forName("utf-8"));
			for(String i:lines){
				this.templateCode = this.templateCode.concat(i);
			}
				
		} catch (IOException e) {
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("ERROR: Template could not be loaded!");
	    	alert.setHeaderText("Chapter template could not be loaded!");
	    	alert.setContentText("Make sure the application-root/resources folder has not been moved.");
	    	alert.showAndWait();
			e.printStackTrace();
		}
		
		this.annotation.finalTextSet(this.templateCode);
		this.textFields.clear();
		this.textFields.put("TITLE", "");
		this.textFields.put("AUDIENCE", "");
		this.textFields.put("AUTHOR", "");
		this.textFields.put("CHAPTERNO", "");
		this.textFields.put("REF1", "");
		this.textFields.put("DATA1", "");
		
		this.placeholders.clear();
		this.placeholders.put("TITLE", "pltitlepl");
		this.placeholders.put("AUDIENCE", "plaudiencepl");
		this.placeholders.put("AUTHOR", "plauthorpl");
		this.placeholders.put("CHAPTERNO", "plchnumpl");
		this.placeholders.put("REF1", "plref1pl");
		this.placeholders.put("DATA1", "pldata1pl");
		this.slideTitleProperty().set(this.getSlideTitle());
		
		this.annotation.draftTextSet("CHAPTER SLIDE");
		//this.updateTemplate();
	}
	
	/**
	 * Reloads the annotation HTML text with the template HTML code 
	 * and replaces all placeholders with their respective values.
	 * 
	 */
	public void updateTemplate() {
		this.annotation.finalTextSet(this.templateCode);
		this.slideTitleProperty().set(this.getSlideTitle());
		Set<String> keySet = this.placeholders.keySet();
		for(String i : keySet){
			if(this.placeholders.get(i) != null){
				if(this.textFields.get(i) != null) {
					this.fillTemplateField(this.placeholders.get(i), TypeUtils.newlinesToHtml(this.textFields.get(i)));
				} else {
					System.out.println("GalleryChapter.updateTemplate -> this.textFields.get(i) == null for i = " + i);
				}
			} else {
				System.out.println("GalleryChapter.updateTemplate -> this.placeholders.get(i) == null for i = " + i);
			}
		}
	}
	
	/**
	 * Constructor: prepares all fields for a chapter slide
	 */
	public GalleryChapter(){ 
		init();
	}
	
	public GalleryChapter(Integer sldNumber){ 
		init();
		galleryItemLoad(sldNumber);
	}

	public String getCompanyNameHeader() {
		return this.GPSSingleton.getCompanyNameTitle();
	}
	public String getCompanyName() {
		return this.GPSSingleton.getCompanyName();
	}
	public String getAddress() {
		return this.GPSSingleton.getCompanyAddress();
	}	
	
	public String getContactInfoHeader() {
		return this.GPSSingleton.getContactTitle();
	}	
	public String getContactInfo() {
		return this.GPSSingleton.getContact();
	}	
	

	@Override
	public void setSlideTitle(String nTitle){
		this.textFields.put("TITLE", nTitle);
		this.slideTitleProperty().set("Chapter " + this.getChapterNo() + ": " + nTitle);
		this.updateTemplate();
	}
	
	@Override
	public String getSlideTitle(){
		return "Chapter " + this.getChapterNo() + ": " + textFields.get("TITLE");
	}
	
	public String getSimpleTitle(){
		return this.textFields.get("TITLE");
	}
	
	

	/**
	 * Setter for the author property
	 * @param nAuthor [String]
	 */
	public void setAuthor(String nAuthor){
//		try{
			this.textFields.put("AUTHOR", nAuthor);
//		} catch (NullPointerException ne) {
//			ne.printStackTrace();
//		}
		this.updateTemplate();
	}
	
	/**
	 * Getter for the author as string
	 * @return this.author.get() [String] 
	 */
	public String getAuthor(){
		return this.textFields.get("AUTHOR");
	}
	
	
	/**
	 * Setter for the audience property
	 * @param naudience [String]
	 */
	public void setAudience(String nAudience){
//		try{
			this.textFields.put("AUDIENCE", nAudience);
//		} catch (NullPointerException ne) {
//			ne.printStackTrace();
//		}
		this.updateTemplate();
	}
	
	/**
	 * Getter for the audience as string
	 * @return this.audience.get() [String] 
	 */
	public String getAudience(){
		return this.textFields.get("AUDIENCE");
	}
	
	public void setRef1(String nText){
		this.textFields.put("REF1", nText);
		this.updateTemplate();
	}
	
	public String getRef1() {
		return this.textFields.get("REF1");
	}
	
	public void setData1(String nText){
		this.textFields.put("DATA1", nText);
		this.updateTemplate();
	}
	
	public String getData1() {
		return this.textFields.get("DATA1");
	}
	
	public void setChapterNo(Integer chno){
		this.textFields.put("CHAPTERNO", chno.toString());
		this.updateTemplate();
	}
	
	public String getChapterNo() {
		return this.textFields.get("CHAPTERNO");
	}
	
	/**
	 * Returns true if the slide contains information.
	 * @return
	 */
	public boolean isInitialized() {
		return super.isInitialized();
	}
	
	public List<String> galleryItemLoad(Integer sldNumber) {
		List<String> data = null;
		data = super.galleryItemLoad(sldNumber);
		this.updateTemplate();
		
		
		return data;
	}


	public void galleryItemSave(Integer sldNumber, List<String> nData) {	
		super.galleryItemSave(sldNumber, nData);		
	}
}
