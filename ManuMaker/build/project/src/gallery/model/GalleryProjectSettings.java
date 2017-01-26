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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import gallery.utils.PathUtils;
import gallery.utils.TypeUtils;
import javafx.scene.image.Image;

/**
 * This singleton wraps all Project-Specific settings, to synchronize them between all
 * settings-dependent instances at any given time. 
 *   
 * 
 * @author Daniel Lachmann
 *
 */

public class GalleryProjectSettings extends gallery.abstractclasses.SettingsAbstr {
	// Private fields 
	private Image logo = null;
	private GalleryProjectSettings() { 
		init();
        
	}


	// Reference to gallery:
	private Gallery gallery = null; 
	
	public final static GalleryProjectSettings INSTANCE = new GalleryProjectSettings();
	
	private void saveLogo(){
		if(this.logo != null) {
			File projectPath = new File(this.getResourcePath());
			PathUtils.saveImagePng(projectPath, this.logo, "Logo.png");
	    }		
	}
	
	private void loadLogo(){
		File projectPath = new File(this.getResourcePath() + "Logo.png");
		this.logo = PathUtils.loadImagePng(projectPath);
		if(this.logo != null) {
			System.out.println("\n\rGalleryProjectSettings.loadImage() -> Successfully loaded logo image...");
		}
	}
	
	public void init(){
		String text = "";
		// Has to be handled correctly if App does not have access priviliges: 
		//this.settings.put("SAVEPATH", System.getProperty("user.home")+ "\\ProjectDocs\\Default\\");
		this.settings.put("SAVEPATH", "C:\\ProjectDocs\\Default\\");
		this.settings.put("IMGFORMAT",  "png");  		//Fixed value
		this.settings.put("SLIDECOUNT", "0");
		this.settings.put("DEFAULTSAVEPATH", "C:\\ProjectDocs\\Default\\");
		this.settings.put("SMPRATE", "1000"); // DEPRECATED! Now used in AppSettings
		
		// Project Settings from ProjectSettingsWindow
		this.settings.put("PROJTITLE", "");
		this.settings.put("AUTHOR", "");
		this.settings.put("DOCTYPE", "");
		this.settings.put("PRODUCTNAME", "");
		this.settings.put("PRODVERSIONT", "");
		this.settings.put("PRODVERSION", "");
		this.settings.put("DOCREVISIONT", "");
		this.settings.put("DOCREVISION", "");
		this.settings.put("AUDIENCET", "");
		this.settings.put("AUDIENCE", "");
		this.settings.put("COMPANYNAMET", "");
		this.settings.put("COMPANYNAME", "");
		this.settings.put("ADDRESS", "");
		this.settings.put("CONTACTT", "");
		this.settings.put("CONTACT", "");
		this.settings.put("REF1", "");
		this.settings.put("DATA1", "");
		this.settings.put("REQTITLE", "false");
		this.settings.put("REQTEXTORPIC", "false");
		
		
		this.updateSettings();
		this.createKeyList();
		text = String.format("Project Settings: Size of this.settings: ");
		text = String.format(text + Integer.toString(this.settings.size()) + "\n" );

		text = String.format(text + "Project Settings: Size of this.keyList: ");
		text = String.format(text + Integer.toString(this.keyList.size()) + "\n" );
		System.out.println(text);
		
        File savefile = new File(this.settings.get("SAVEPATH")+"Settings\\Settings.txt");
        if(!savefile.exists()){
        	saveSettings();
        }
        try{
        	loadSettings();
        } catch (Exception e){
        	e.printStackTrace();
        }

	}
	
	@Override
	public void createKeyList(){
		this.keyList.clear();
		System.out.println("\n\rGalleryProjectSettings.createKeyList() called");
		this.keyList.add(0, "DEFAULT");
		this.keyList.add(1, "TEXTENCODING");
		this.keyList.add(2, "TEXTFORMAT");
		this.keyList.add(3, "SAVEPATH");
		this.keyList.add(4, "IMGFORMAT");
		this.keyList.add(5, "SLIDECOUNT");
		this.keyList.add(6, "DEFAULTSAVEPATH");
		this.keyList.add(7, "SMPRATE");
		this.keyList.add(8, "PROJTITLE");
		this.keyList.add(9, "AUTHOR");
		this.keyList.add(10, "DOCTYPE");
		this.keyList.add(11, "PRODUCTNAME");
		this.keyList.add(12, "PRODVERSIONT");
		this.keyList.add(12, "PRODVERSION");
		this.keyList.add(13, "DOCREVISIONT");
		this.keyList.add(14, "DOCREVISION");
		this.keyList.add(15, "AUDIENCET");
		this.keyList.add(16, "AUDIENCE");
		this.keyList.add(17, "COMPANYNAMET");
		this.keyList.add(18, "COMPANYNAME");
		this.keyList.add(19, "ADDRESS");
		this.keyList.add(21, "CONTACTT");
		this.keyList.add(22, "CONTACT");
		this.keyList.add(23, "REF1");
		this.keyList.add(24, "DATA1");
		this.keyList.add(25, "REQTITLE");
		this.keyList.add(26, "REQTEXTORPIC");
		
	}
	
	public void setGallery(Gallery nGallery) {
			this.gallery = nGallery;
	}
	
	/**
	 * Updates the time-variable data (e.g. slidecount)
	 */
	@Override
	public void updateSettings( ) {
		if(this.gallery != null) {
			this.settings.put("SLIDECOUNT", Integer.toString(this.gallery.getSlideCount()));
		}
	}
	
	/**
	 * Set savepath.
	 * @param nSavePath String Used to identify the index of the list element
	 */
	@Override
	public void setSavePath(String nSavePath) {
		if(nSavePath == "DEFAULTSAVEPATH" || nSavePath == "DEFAULT") {
			this.settings.put("SAVEPATH", this.settings.get("DEFAULTSAVEPATH"));
		} else {
			if(nSavePath.endsWith("\\")){
				this.settings.put("SAVEPATH",nSavePath.trim());
			} else {
				this.settings.put("SAVEPATH",nSavePath.trim().concat("\\"));
			}
			this.saveLogo();
		}
	}
	
	public String getProjTitle() {
		return this.settings.get("PROJTITLE");
	}
	
	public void setProjTitle(String nProjTitle) {		
		try {
			this.settings.put("PROJTITLE", nProjTitle.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	public String getProductName() {
		return this.settings.get("PRODUCTNAME");
	}
	
	public void setProductName(String nProduct) {
		try {
			this.settings.put("PRODUCTNAME", nProduct.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	public String getProductVersion() {
		return this.settings.get("PRODVERSION");
	}
	
	public String getProductVersionTitle() {
		return this.settings.get("PRODVERSIONT");
	}
		
	public void setProductVersion(String nProdVersion) {
		try {
			if(!nProdVersion.isEmpty()){
				this.settings.put("PRODVERSIONT", "Version:");
			} else {
				this.settings.put("PRODVERSIONT", "");
			}
			this.settings.put("PRODVERSION", nProdVersion.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}	
	
	public String getDocType() {
		return this.settings.get("DOCTYPE");
	}
	
	public void setDocType(String nDocType) {
		try {
			this.settings.put("DOCTYPE", nDocType.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}

	public String getDocRevision() {
		return this.settings.get("DOCREVISION");
	}
	
	public String getDocRevisionTitle() {
		return this.settings.get("DOCREVISIONT");
	}
	
	public void setDocRevision(String nDocRev) {
		try {
			if(!nDocRev.isEmpty()){
				this.settings.put("DOCREVISIONT", "Manual Revision:");
			} else {
				this.settings.put("DOCREVISIONT", "");
			}
			this.settings.put("DOCREVISION", nDocRev.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	public String getAudience() {
		return this.settings.get("AUDIENCE");
	}	
	
	public String getAudienceTitle() {
		return this.settings.get("AUDIENCET");
	}	
	
	public void setAudience(String nAudience) {
		try {
			if(!nAudience.isEmpty()){
				this.settings.put("AUDIENCET", "Intended Readers:");
			} else {
				this.settings.put("AUDIENCET", "");
			}
			this.settings.put("AUDIENCE", nAudience.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	public String getAuthor() {
		return this.settings.get("AUTHOR");
	}	
	
	public void setAuthor(String nAuthor) {
		try {
			this.settings.put("AUTHOR", nAuthor.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
		
	public String getCompanyName() {
		return this.settings.get("COMPANYNAME");
	}	
	
	public String getCompanyNameTitle() {
		return this.settings.get("COMPANYNAMET");
	}	
	
	public void setCompanyName(String nCompanyName) {
		try {
			if(!nCompanyName.isEmpty()){
				this.settings.put("COMPANYNAMET", "Publisher:");
			} else {
				this.settings.put("COMPANYNAMET", "");
			}
			this.settings.put("COMPANYNAME", nCompanyName.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	public String getCompanyAddress() {
		return this.settings.get("ADDRESS");
	}	
	
	public void setCompanyAddress(String nAddress) {
		try {
			this.settings.put("ADDRESS", nAddress.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
		
	public String getContact() {
		return this.settings.get("CONTACT");
	}
	
	public String getContactTitle() {
		return this.settings.get("CONTACTT");
	}
	
	public void setContact(String nContact) {
		try {
			if(!nContact.isEmpty()){
				this.settings.put("CONTACTT", "Contact:");
			} else {
				this.settings.put("CONTACTT", "");
			}
			this.settings.put("CONTACT", nContact.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	public String getRef1() {
		return this.settings.get("REF1");
	}
	
	public void setRef1(String nRef1) {
		try {
			this.settings.put("REF1", nRef1.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
		
	public String getData1() {
		return this.settings.get("DATA1");
	}
	
	public void setData1(String nData1) {
		try {
			this.settings.put("DATA1", nData1.trim());
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	public void setReqTitle(boolean  req){
		if(req == true){
			this.settings.put("REQTITLE", "true");
		} else {
			this.settings.put("REQTITLE", "false");
		}
	}
	
	public boolean getReqTitle(){
		boolean ret = false;
		if(this.settings.get("REQTITLE") == "true"){
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}
	
	
	public void setReqInstr(boolean  req){
		if(req == true){
			this.settings.put("REQTEXTORPIC", "true");
		} else {
			this.settings.put("REQTEXTORPIC", "false");
		}
	}
	
	public boolean getReqInstr(){
		boolean ret = false;
		if(this.settings.get("REQTEXTORPIC") == "true"){
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}
	
	
	
	/**
	 * Get image extension format
	 * @return String Image format as String, without preceding period.
	 */
	public String getImageFormat() {
			return this.settings.get("IMGFORMAT");
	}
	

	
	/**
	 * Returns the current number of slides in the Gallery, according to the project settings.
	 * @return
	 */
	public int getSlideCount() {
//		synchronized(this.gallerySettingsLockObject){
//		this.updateSettings();
		return Integer.parseUnsignedInt(this.settings.get("SLIDECOUNT"));
//		}
	}
	
	/**
	 *  ##########    	WARNING! 	  ########## 
	 *	
	 *	ONLY INTENDED FOR RECOVERY OPERATIONS!
	 *
	 * This method sets the slide count directly,
	 * and can therefore corrupt a project.
	 * 
	 * Intended for use in situations where a corrupted 
	 * project has to be recovered and the settings file reset to default!
	 *  
	 * @return
	 */
	public void setSlideCount(Integer nCount) {
		if(nCount != null && nCount > 0) {
			this.settings.put("SLIDECOUNT", nCount.toString());
		}
	}

	/**
	 * Sets the Company Logo image. 
	 * Can be set to null.
	 * @param nPic
	 */
	public void setLogoPic(Image nPic) {
		if(nPic != null) {
			this.logo = nPic;
			this.saveLogo();
		} else {
			this.logo = null;
		}
	}
	
	
	/**
	 * Deletes the logo from the filesystem. 
	 * 
	 * Effective immediately if no entity is using the file,
	 * Otherwise it is labeled with deletOnExit and 
	 * deleted when the JVM terminates.
	 */
	public void deleteLogoPic(){
		File log = new File(this.getResourcePath() + "Logo.png");
		if(log.exists() && log.isFile()){
			if(log.canWrite()){
				log.delete();
			} else {
				log.deleteOnExit();
			}
		}
		
	}
	
	/**
	 * Returns the selected company logo image. 
	 * Can return null!
	 * @return
	 */
	public Image getLogoPic() {
		return this.logo;
	}
	
	/**
	 * Returns the absolute project resources path as a string.
	 * 
	 *  String ends with path-separator, so a filename can be concatenated without adding a separator. 
	 * @return
	 */
	public String getResourcePath(){
		String ret = null;
		if(this.getSavePath().endsWith("\\")){
			ret = this.getSavePath() + "Resources\\";
		} else {
			ret = this.getSavePath() + "\\Resources\\";
		}
		return ret;
	}
	
	
	public String getResourcePath(String separator){
		String ret = null;
		if(this.getSavePath().endsWith("\\")){
			ret = this.getSavePath() + "Resources\\";
		} else {
			ret = this.getSavePath() + "\\Resources\\";
		}
		if(separator != null ){
			ret = ret.replace("\\", separator);
		}
		
		return ret;
	}
	
	/**
	 * Returns the absolute safety-resources path as a string.
	 * 
	 *  String ends with path-separator, so a filename can be concatenated without adding a separator. 
	 * @return
	 */
	public String getSafetyDataPath(){
		String ret = null;
		ret = this.getResourcePath() + "SafetyData\\";
		return ret;
	}
	
	
	@Override
	public void saveSettings() {
		List<String> text = new ArrayList<String>(); 
		File projectPath = new File(this.getSavePath()+"\\Settings\\");	
		updateSettings();
		PathUtils.checkIfPathExistsAndMake(projectPath);
		    // Create text file and save
		    for (int i = 0; i < this.keyList.size(); i++) {
		    	if(this.settings.get(this.keyList.get(i)).endsWith("\n")){
		    		text.add(TypeUtils.newlinesToHtml(this.settings.get(this.keyList.get(i))));		// Settings are saved in separate lines
		    	} else {
		    		text.add(TypeUtils.newlinesToHtml(this.settings.get(this.keyList.get(i))) +"\n");		// Settings are saved in separate lines
		    	}
		    }
		    File dataFile = new File(projectPath.getAbsolutePath() + "\\Settings." + this.getTextFormat());
		    PathUtils.textFileSaveLines(dataFile, text);
	}

	@Override
	public void loadSettings() throws IOException { 
		String outString = null;
		File projectPathFile = new File(this.getSavePath() + "\\Settings\\");	

	    if (projectPathFile.exists()) {
		
			List<String> nSettings = PathUtils.textFileLoadLines(new File((projectPathFile.getPath() + "\\Settings." + this.getTextFormat()))); 
		    for(int i = 0; i < this.keyList.size(); i++) {
		    	this.settings.put(this.keyList.get(i), nSettings.get(i));		// Settings are saved in separate lines
		    	System.out.println("\n\rGPS.loadSettings() -> Setting field " + this.keyList.get(i) + " to " + nSettings.get(i));
		    }
			outString = String.format("Successfully Project loaded Settings from " + projectPathFile.toString());
			this.loadLogo();
		
	    } else {
	    	outString = String.format("Default Settings file could not be found at " + projectPathFile.getPath() + "Settings." + this.getTextFormat() + ". Creating new File.");
	    }

		for(int i = 0; i < this.settings.size(); i++)
			outString = String.format(outString + " \r\n " + this.keyList.get(i) +" = "+ this.settings.get(this.keyList.get(i)));
		System.out.println(outString);
	}
}
