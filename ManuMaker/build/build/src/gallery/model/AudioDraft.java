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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;

/**
 * This class stores an audio clip.
 * @author Daniel Lachmann
 *
 */


public class AudioDraft {

	private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	private Media audioDraft = null;
	private String audioFileName = "";
	
	public AudioDraft(){
		
	}
	
	public AudioDraft(Media nAD, String rl){
		File temp = new File(rl);
		if(nAD != null && temp.exists()){
			this.audioDraft = nAD;
			this.audioFileName = rl;
		}
	}
	/**
	 * Sets the AudioClip. Cannot be set to null, use the method AudioDraft.deleteAudioDraft() for that purpose.
	 * @param nAudio [AudioClip]
	 */
	public void setAudioDraft(Media nAudio, File audioPath) {
		File tmp = new File(GPSSingleton.getSavePath() + "\\Resources\\" + this.audioFileName);
		if(nAudio != null && audioPath != null){
			if(tmp.exists() && tmp.isFile()){ // IF this audiodraft is being overwritten, delete old file first
				FileUtils.deleteQuietly(tmp);
				this.audioFileName = "";
			}	
			this.audioDraft = nAudio;
			this.audioFileName = audioPath.getPath().replace(this.GPSSingleton.getSavePath() + "\\Resources\\", "");
			this.save(this.GPSSingleton.getSavePath()); 	//Make sure the file is copied to the new Directory, in case the Project has been saved as a copy.
		}
		
		
		this.audioDraft = nAudio;
	}
	
	
	public void deleteAudioDraft(){
		File audioFile = new File(GPSSingleton.getSavePath() + "\\Resources\\" + audioFileName);
		
		if(audioFile.exists()){
			this.audioDraft = null;
			this.audioFileName = "";
			audioFile.delete();
			System.out.println("\n\rGallerySlide.deleteAudioDraft() -> Audio file deleted!.");
		}
		
	}
	
	/**
	 * Gets the AudioClip stored in this AudioDraft. 
	 * Returns null if audioDraft has not been set. 
	 * @return [AudioClip, null]
	 */
	public Media getAudioDraft() {
		return this.audioDraft;
	}
	
	
	public File getFileName(){
		return new File(this.audioFileName);
	}
	/**
	 * Takes the project path, determines new filename at target location by itself.
	 * @param newProjectPath
	 * @return
	 */
	public boolean save(String newProjectPath){
		boolean saved = false;
		File oldFile = new File(GPSSingleton.getSavePath() + "\\Resources\\" + this.audioFileName); 
		File newFile = null;
		if(newProjectPath != null && oldFile.exists()) {
			newFile = new File(newProjectPath);
			if(!oldFile.toString().contains(newFile.toString())){ 	// If new resource directory is the same as old one, keep file as is. 
				Integer ctr = 0;
				while((newFile = new File(newProjectPath + "\\Resources\\audio" + ctr.toString() + ".wav")).exists()){
					ctr++;
				}
				try {
					audioFileName = Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING).toFile().toString();
					audioFileName.replace(GPSSingleton.getSavePath() + "\\Resources\\", ""); 	//Shorten full filepath to just the filename
					saved = true;
				} catch (IOException e) {
			    	Alert alert = new Alert(AlertType.ERROR);
			    	alert.setTitle("ERROR: Could not migrate audio file!");
			    	alert.setHeaderText("Audio draft could not be migrated!");
			    	alert.setContentText(	"There was an error copying an audio draft file\n\r"
			    					+ 		"to the new project location. \n"
			    					+ 		"You may attempt moving the audio-files manually. \n ");
			    	alert.showAndWait();
					e.printStackTrace();
				}
			}
			saved = true;
		}		
		return saved;
	}
	
	/**
	 * Loads an audiofile into the AudioDraft object, given a fully qualified filepath as string.
	 * @param fileString
	 * @return
	 */
	public boolean load(String fileName){
		boolean loaded = false;
		
		if(fileName != ""){
			File newFile = new File(GPSSingleton.getSavePath() + "\\Resources\\" + fileName);
			if(newFile.exists() && newFile.isFile()){
				this.audioDraft = new Media(newFile.toURI().toString());
				this.audioFileName = fileName;
				loaded = true;
			}
		}

		return loaded;
	}
}
