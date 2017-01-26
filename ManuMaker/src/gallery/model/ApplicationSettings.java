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

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

import gallery.abstractclasses.SettingsAbstr;
import gallery.utils.PathUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



/**
 * This singleton wraps all Application-Specific settings, to synchronize them between all
 * settings-dependent instances at any given time. 
 * 
 * Only the class operating the Settings Menu is supposed to change anything here.  
 * 
 * @author Daniel Lachmann
 *
 */


public class ApplicationSettings extends SettingsAbstr {
	// Private fields 
		
	private ApplicationSettings() {
		
		// Has to be handled correctly if App does not have access privileges: 
		//this.settings.put("SAVEPATH", System.getProperty("user.home")+ "\\ProjectDocs\\AppSettings\\");
		init();
		try {
			this.loadSettings();
		} catch (IOException e) {
	    	Alert alert = new Alert(AlertType.WARNING);
	    	alert.setTitle("WARNING: Settings could not be loaded on instantiation!");
	    	alert.setHeaderText("Settings file not readable!");
	    	alert.setContentText(	"Default settings will be loaded and saved to \n"
	    					+ 		"replace the missing or corrupted file.");
	    	alert.showAndWait();
	    	this.init();
		}
		
	}
	
	@Override
	public void init() {
		String text = "\n\rInitializing ApplicationSettings:\n";
		this.settings.put("SAVEPATH", "C:\\ProjectDocs\\AppSettings\\");
		this.settings.put("PROMPTOVERWRITE", "true");
		this.settings.put("TTSAPI", "DEFAULT");
		this.settings.put("STTAPI", "DEFAULT");
		this.settings.put("CAMNAME","DEFAULT");
		this.settings.put("MICNAME", "DEFAULT");
		this.settings.put("CAMANGLE", "0");
		this.settings.put("REFRESHRATE", "33");
		this.settings.put("SMPRATE", "2500");
		this.settings.put("IMGWIDTH", "640");
		this.settings.put("IMGHEIGHT", "480");
		//this.settings.put("DEFAULTSAVEPATH", System.getProperty("user.home")+ "\\ProjectDocs\\AppSettings\\");
		this.settings.put("DEFAULTSAVEPATH", "C:\\ProjectDocs\\AppSettings\\");
	
		this.updateSettings();
		text = String.format(text + "App Settings: Size of settings: ");
		text = String.format(text + Integer.toString(this.settings.size()) + "\n" );

		text = String.format(text + "App Settings: Size of this.keyList: ");
		text = String.format(text + Integer.toString(this.keyList.size()) + "\n" );
		System.out.println(text);
	    this.audioFormat = new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2, 8000.0F, false);
	    this.fileFormat = AudioFileFormat.Type.WAVE;
        this.dlInfo = new DataLine.Info(TargetDataLine.class,this.audioFormat);
        try {
			this.tdLine = (TargetDataLine)AudioSystem.getLine(this.dlInfo);
		} catch (LineUnavailableException e1) {
			// Only marginally relevant, stack-trace can be useful for console-debugging
			e1.printStackTrace();
		}
        File savefile = new File(this.settings.get("SAVEPATH")+"Settings.txt");
        if(!savefile.exists()){
        	saveSettings();
        }
		try {
			loadSettings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public final static ApplicationSettings INSTANCE = new ApplicationSettings();	  
	private AudioFormat audioFormat;
	private AudioFileFormat.Type fileFormat = null;
	private DataLine.Info dlInfo = null;
	private TargetDataLine tdLine = null;
	/**
	 * Enables / Disables the safety feature which prompts user 
	 * to confirm before existing Image on a slide is replaced. 
	 */
	public void setPromptOverwriteImage(boolean nBool) {
		this.settings.put("PROMPTOVERWRITE", String.valueOf(nBool));
	}
	
	/**
	 * Returns state of "Prompt before overwrite" setting.
	 */
	public boolean getPromptOverwriteImage() {
		return Boolean.getBoolean(this.settings.get("PROMPTOVERWRITE"));
	}
	

	/**
	 * Setting the Cam 
	 */
	public void storeCamName(String name) {
		this.settings.put("CAMNAME", name);		
	}
	
	/**
	 * get cam name
	 */
	public String getCamName() {		
		return this.settings.get("CAMNAME");
	}
	
	/**
	 * Setting the Mic
	 */
	public void storeMicName(String name) {
		this.settings.put("MICNAME", name);
	}

	/**
	 * get mic name
	 */
	public String getMicName() {
		return this.settings.get("MICNAME");
	}
	
	public TargetDataLine getMicDataLine() {
		return this.tdLine;
	}
	
	public Info getMicLineInfo() {
		return this.dlInfo;
	}
	public AudioFormat getAudioFormat(){
		return this.audioFormat;
	}
	public Type getAudioFileType(){
		return this.fileFormat;
	}
	
	/**
	 * Setting the Camera angle correction
	 */
	public void storeCamAngle(Integer camAngle){
		this.settings.put("CAMANGLE",camAngle.toString());
	}
	
	/**
	 * Get camera correction angle:
	 */
	public int getCamAngle(){
		return Integer.valueOf(this.settings.get("CAMANGLE"));
	}
	
	/**
	 * Set the refreshrate of the image relevant threads
	 */
	public void setCamRefreshRate(Integer nRefRate){
		this.settings.put("REFRESHRATE", nRefRate.toString());
	}
	
	/**
	 * Get the image refresh rate
	 */
	public Integer getCamRefreshRate() {
		return Integer.valueOf(this.settings.get("REFRESHRATE"));
	}
	
	public Integer getSMPRefreshRate() {
		return Integer.decode(this.settings.get("SMPRATE"));
	}
	
	public void setSMPRefreshRate(Integer nRate) {
		this.settings.put("SMPRATE", nRate.toString());
	}
	
	/**
	 * Set resolution for images taken by selected Camera
	 */
	public void setCamResolution(Dimension nDim) {
		if(!(nDim == null) && nDim.height > 0 && nDim.width > 0) {
			this.settings.put("IMGWIDTH", Integer.toString(nDim.width));
			this.settings.put("IMGHEIGHT", Integer.toString(nDim.height));
			System.out.printf("ApplicationSettings.setCamResolution(): Dimension saved in Settings: " + nDim.width + "x" + nDim.height + "\n\r");
		} else {
			System.out.printf("ApplicationSettings.setCamResolution(): invalid dimension given.\n\r		Dimension must not be null and height and width must be > 0\n\r");
		}
	}
	
	/**
	 * Get resolution for images taken by selected Camera
	 */
	public Dimension getCamResolution() {
		Dimension nDim = new Dimension();
		nDim.setSize(Integer.parseInt(this.settings.get("IMGWIDTH")), Integer.parseInt(this.settings.get("IMGHEIGHT")));
		
		return nDim;
	}
}
