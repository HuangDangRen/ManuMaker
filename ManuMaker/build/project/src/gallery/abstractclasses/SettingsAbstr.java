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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gallery.utils.PathUtils;
import gallery.utils.TypeUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This is class defines all fields and methods needed in the context 
 * of storing and retrieving settings for a given situation.
 *  
 * @author Daniel Lachmann
 *
 */
public abstract class SettingsAbstr {

		
		/*
		 * List which contains the settings in String form, initialized with default values.
		 */
		protected Map<String, String> settings = new HashMap<String, String>();
		protected List<String> keyList = new ArrayList<String>();
		
		
		// Public fields 
		public SettingsAbstr() {
			this.settings.put("DEFAULT", "ERROR");
			this.settings.put("TEXTENCODING", "UTF8"); 	//Fixed value
			this.settings.put("TEXTFORMAT", "txt");     	//Fixed value
			
		}
		
		
		protected abstract void init();
		
		/**
		 * Updates the time-variable data (e.g. slidecount)
		 */
		public void updateSettings( ) {
			createKeyList();
		}
		
		/**
		 * Creates the keyList
		 */
		public void createKeyList() {
			
			this.keyList = TypeUtils.getKeyList(this.settings.keySet());
					//new ArrayList<String>(this.settings.keySet());
		}
		/**
		 * Checks if project path has been set. 
		 */
		public boolean isCustomPathSet() {			
			String outString = String.format(this.settings.get("DEFAULTSAVEPATH") + "\n" + this.settings.get("SAVEPATH"));
			System.out.println(outString);
			
			if(this.settings.get("DEFAULTSAVEPATH").contentEquals(this.settings.get("SAVEPATH"))) {
				return false;
			}
			else {
				return true;
			}
		}
			
		/**
		 * Set savepath.
		 * @param nSavePath String Used to identify the index of the list element
		 */
		public void setSavePath(String nSavePath) {
			if(nSavePath == "DEFAULTSAVEPATH" || nSavePath == "DEFAULT") {
				this.settings.put("SAVEPATH", this.settings.get("DEFAULTSAVEPATH"));
			} else {
				this.settings.put("SAVEPATH",nSavePath);
			}
		}

		/**
		 * get current Savepath
		 */
		public String getSavePath() {
			return this.settings.get("SAVEPATH");
		}
		
		/**
		 * Get text encoding format (Default: UTF-8)
		 * @return String Text Encoding.
		 */
		public String getTextEncoding() {
				return this.settings.get("TEXTENCODING");
		}
		
		
		/**
		 * Get text file format (Default: txt)
		 * @return String Text format.
		 */
		public String getTextFormat() {		
				return this.settings.get("TEXTFORMAT");
		}
		
		/**
		 * get default Savepath (constant)
		 */
		public String getDefaultSavePath() {
			return this.settings.get("DEFAULTSAVEPATH");
		}
		
		
		/**
		 * Method for saving Settings to Project directory.
		 */
		public void saveSettings() {
			BufferedWriter textWriter = null;
			List<String> text = new ArrayList<String>(); 
			File projectPath = new File(this.getSavePath());	
			updateSettings();
			PathUtils.checkIfPathExistsAndMake(projectPath);
		    try {
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
		    } catch (Exception e) {
		    	Alert alert = new Alert(AlertType.WARNING);
		    	alert.setTitle("WARNING: Something went wrong!");
		    	alert.setHeaderText("Exception in SettingsAbstr.saveSettings()");
		    	alert.setContentText(	"An Exception was thrown during the saving procedure. \n"
		    						+ 	"The settings file may have been corrupted, \n"
		    						+ 	"and may need to be removed. \n\r"
		    						+ 	"Please manually delete the file and restart the application \n"
		    						+ 	"it will be replaced by default settings, which can then be edited.\n ");
		    	alert.showAndWait();
		    } finally {
		        try  {
		            if ( !(textWriter == null))
		            	textWriter.close( );
		        } catch ( IOException e){ 
		        	e.printStackTrace();
		        }
		    }   
		}; 	
		
		/**
		 * Method for loading settings from project directory. 
		 * Overloaded: Uses existing path or takes path as String Argument 
		 * @throws IOException 
		 */
		public void loadSettings() throws IOException {//throws IOException{
			String outString = null;
			File projectPathFile = new File(this.getSavePath());	

		    if (projectPathFile.exists()) {
		    	File settingsFile = new File(projectPathFile.getPath() + "\\Settings." + this.getTextFormat());
		    	if(settingsFile.exists()) {
					List<String> nSettings = PathUtils.textFileLoadLines(settingsFile); 
				    for(int i = 0; i < this.keyList.size(); i++) {
				    	this.settings.put(this.keyList.get(i), nSettings.get(i));		// Settings are saved in separate lines
				    	System.out.println("\n\rGPS.loadSettings() -> Setting field " + this.keyList.get(i) + " to " + nSettings.get(i));
				    }
					outString = String.format("Successfully Project loaded Settings");
		    	} else {
			    	outString = String.format("Default Settings file could not be found at " + projectPathFile.getPath() + "Settings." + this.getTextFormat() + ". Creating new File.");
			    	Alert alert = new Alert(AlertType.WARNING);
			    	alert.setTitle("WARNING: Settings could not be found!");
			    	alert.setHeaderText("Settings file not found!");
			    	alert.setContentText(	"Default settings will be loaded and saved to \n"
			    					+ 		"replace the missing file.");
			    	alert.showAndWait();
			    	
			    	this.init();
		    	}
			}

			for(int i = 0; i < this.settings.size(); i++)
				outString = String.format(outString + " \r\n " + this.keyList.get(i) +" = "+ this.settings.get(this.keyList.get(i)));
			System.out.println(outString);
		}
 	
};
