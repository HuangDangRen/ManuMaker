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
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class defines all  properties needed for displaying a Project-title slide.
 * 
 * @author Daniel Lachmann
 *
 */

public class GalleryTitle extends GalleryChapter {
	
	private void init(){
		try {
			this.templateCode = new String("");
			List<String> lines = Files.readAllLines(Paths.get("resources/html_templates/TitleTemplate.html"),Charset.forName("utf-8"));
			for(String i:lines){
				this.templateCode = this.templateCode.concat(i);
			}
		} catch (IOException e) {
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("ERROR: Template could not be loaded!");
	    	alert.setHeaderText("Title template could not be loaded!");
	    	alert.setContentText("Make sure the application-root/resources folder has not been moved.");
	    	alert.showAndWait();
			e.printStackTrace();
		}
		this.textFields.clear();
		this.placeholders.clear();
		this.annotation.finalTextSet(this.templateCode);
		
//		this.placeholders.put("PROJTITLE", "title");
		this.placeholders.put("TITLE", "title");
		this.placeholders.put("AUTHOR", "plauthorpl");
		this.placeholders.put("DOCTYPE", "pldoctypepl");
		this.placeholders.put("PRODUCTNAME", "plproductpl");
		this.placeholders.put("PRODVERSIONT", "plvertpl");
		this.placeholders.put("PRODVERSION", "plversionpl");
		this.placeholders.put("DOCREVISIONT", "plrevtpl");
		this.placeholders.put("DOCREVISION", "plrevisionpl");
		this.placeholders.put("AUDIENCET", "plaudtpl");
		this.placeholders.put("AUDIENCE", "plaudiencepl");
		this.placeholders.put("COMPANYNAMET", "plpubpl");
		this.placeholders.put("COMPANYNAME", "plcompanypl");
		this.placeholders.put("ADDRESS", "pladdresspl");
		this.placeholders.put("LOGOPATH", "pllogopl");
		this.placeholders.put("CONTACTT", "plconttpl");
		this.placeholders.put("CONTACT", "plcontactpl");
		this.placeholders.put("REF1", "plref1pl");
		this.placeholders.put("DATA1", "pldata1pl");
		
		this.loadDataFromProjectSettings();
		
		this.annotation.draftTextSet("TITLE SLIDE");
		this.setSlideTitle("Title Slide");
	
	}
	
	public GalleryTitle(){ 
		init();
		this.loadDataFromProjectSettings();
	}
	
	public GalleryTitle(Integer sldNumber){
		init();
		this.galleryItemLoad(sldNumber);
	}
	
	@Override 
	public void updateTemplate(){
		super.updateTemplate();
		
		this.slideTitleProperty().set("Project: " + this.GPSSingleton.getProjTitle());
	}

	public void loadDataFromProjectSettings() {
//		this.textFields.put("PROJTITLE", this.GPSSingleton.getProjTitle());
		this.textFields.put("TITLE", this.GPSSingleton.getProjTitle());
		this.textFields.put("AUTHOR", this.GPSSingleton.getAuthor());		// Generate Author List from Chapter Slides
		this.textFields.put("DOCTYPE", this.GPSSingleton.getDocType());
		this.textFields.put("PRODUCTNAME", this.GPSSingleton.getProductName());
		this.textFields.put("PRODVERSIONT", this.GPSSingleton.getProductVersionTitle());
		this.textFields.put("PRODVERSION", this.GPSSingleton.getProductVersion());
		this.textFields.put("DOCREVISIONT", this.GPSSingleton.getDocRevisionTitle());
		this.textFields.put("DOCREVISION", this.GPSSingleton.getDocRevision());
		this.textFields.put("AUDIENCET", this.GPSSingleton.getAudienceTitle());
		this.textFields.put("AUDIENCE", this.GPSSingleton.getAudience());
		this.textFields.put("COMPANYNAMET", this.GPSSingleton.getCompanyNameTitle());
		this.textFields.put("COMPANYNAME", this.GPSSingleton.getCompanyName());
		this.textFields.put("ADDRESS", this.GPSSingleton.getCompanyAddress());
		if(this.GPSSingleton.getLogoPic() != null){
			this.textFields.put("LOGOPATH", "<img src=\"pllogobasepathpl/Logo.png" + "\" alt=\"\" style=\"width:300px;\">");	
		} else {
			this.textFields.put("LOGOPATH", "");
		}
		this.textFields.put("CONTACTT", this.GPSSingleton.getContactTitle());
		this.textFields.put("CONTACT", this.GPSSingleton.getContact());
		this.textFields.put("REF1", this.GPSSingleton.getRef1());
		this.textFields.put("DATA1", this.GPSSingleton.getData1());
		this.slideTitleProperty().set("Project: " + this.GPSSingleton.getProjTitle());
		
		this.updateTemplate();
	}
	
	/**
	 * Returns true if the slide contains information.
	 * @return
	 */
	public boolean isInitialized() {
		return super.isInitialized();
	}
	
	


	public String getDocType() {
		return this.GPSSingleton.getDocType();
	}
	public String getProductName() {
		return this.GPSSingleton.getProductName();
	}
	public String getProductVersionHeader() {
		return this.GPSSingleton.getProductVersionTitle();
	}
	public String getProductVersion() {
		return this.GPSSingleton.getProductVersion();
	}
	public String getDocRevisionHeader() {
		return this.GPSSingleton.getDocRevisionTitle();
	}
	public String getDocRevision() {
		return this.GPSSingleton.getDocRevision();
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
	public String getLogoPathAbstract(){
		return this.textFields.get("LOGOPATH");
	}
	public String getContactInfoHeader() {
		return this.GPSSingleton.getContactTitle();
	}
	public String getContactInfo() {
		return this.GPSSingleton.getContact();
	}
	public String getRef1() {
		return this.GPSSingleton.getRef1();
	}
	public String getData1() {
		return this.GPSSingleton.getData1();
	}	
		
	
	@Override
	public List<String> galleryItemLoad(Integer sldNumber){
		List<String> data = null;
		data = super.galleryItemLoad(sldNumber);
		this.loadDataFromProjectSettings();
		
		return data;
	}
	
	@Override
	public void setSlideTitle(String nTitle){
		this.textFields.put("TITLE", nTitle);
		this.slideTitleProperty().set("Project: " + nTitle);
		this.updateTemplate();
	}
	
	
	@Override
	public String getSlideTitle() {
		return this.textFields.get("TITLE");
	}
	
}
