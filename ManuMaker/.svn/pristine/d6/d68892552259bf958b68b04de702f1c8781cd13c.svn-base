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


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;

public class AudioRecorder {
	private ApplicationSettings AppSettingsSingleton = ApplicationSettings.INSTANCE;
	private GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	
	private byte[] audioData = null;
	//private Media audioClip = null;
	
	private ByteArrayOutputStream recorded = null;
	private String fileName = null;
//	private File temp = new File("tmp");
//	private String savePath = new String(this.AppSettingsSingleton.getSavePath() + "temp\\");
	
	private Thread recordThread = null;
	private AudioFormat audioFormat = null;
	private  AudioFileFormat.Type fileFormat = null;
	private TargetDataLine tdLine = null;
	public volatile boolean stopRecording = false;
	public volatile boolean doneRecording = true;
	
	public AudioRecorder(){
		this.audioFormat = this.AppSettingsSingleton.getAudioFormat();
        this.tdLine = this.AppSettingsSingleton.getMicDataLine();
        this.fileFormat = this.AppSettingsSingleton.getAudioFileType();
	}
	
	public void record() {
	    try{
	        this.audioFormat = this.AppSettingsSingleton.getAudioFormat();
	        this.tdLine = this.AppSettingsSingleton.getMicDataLine();
	        this.fileFormat = this.AppSettingsSingleton.getAudioFileType();
	        this.threadInit();
	      }catch (Exception e) {
	        e.printStackTrace();
	      }
	}	
	
	public Media getRecordedMedia() {
		Media audioTrack = null;
		this.tdLine.stop();
		this.tdLine.close();
		//load data from temp file into JavaFX Media object:
		File tmpLoad = new File(this.GPSSingleton.getSavePath() + "\\Resources\\" + AudioRecorder.this.fileName);
		System.out.println("\n\rAudioRecorder.getRecordedMedia() -> loading Media from :" + tmpLoad.toURI().toString());
		audioTrack = new Media(tmpLoad.toURI().toString());
		
		return audioTrack;
	}
	
	public String getLastMediaPath(){
		if(this.fileName != null){
			return fileName;
		} else {
			return "";
		}
	}
	
	public void resetRecorder(){
		audioData = null;
		recorded = null;
		fileName = null;
		recordThread = null;
		audioFormat = null;
		fileFormat = null;
		tdLine = null;
		stopRecording = false;
		doneRecording = true;
		
		this.audioFormat = this.AppSettingsSingleton.getAudioFormat();
        this.tdLine = this.AppSettingsSingleton.getMicDataLine();
        this.fileFormat = this.AppSettingsSingleton.getAudioFileType();
	}
	
	private void threadInit() {
		if(this.recordThread == null) {
			System.out.println("\n\rInitializing recordThread...");
			//((SlideStopMotion)this.slidePicture).reset();
			this.recordThread = new Thread(new RecRunnable());
	 		this.recordThread.setName("RecRunnable");
	 		this.recordThread.setDaemon(true);
	 		System.out.println("\n\rStarting recordThread...");

	 		this.recordThread.start();
	 		System.out.println("\n\rrecordThread started...");
		} 		
	}
	
	
	public boolean threadInterrupted() {
		if(this.recordThread != null) {
			return recordThread.isInterrupted();
		} else {
			return true;
		}
	}
	
	/**
	 * Runnable for recording audio draft.
	 * 
	 *  Saves recorded audio to project dir with a running number, NOT EQUAL to slide number.
	 *  The Slide owns a path directing to it's own audiodraft file. 
	 */
	public class RecRunnable implements Runnable {
	
		@Override
		public void run() {
			Integer ctr = 0;
			File temp = null;
			AudioRecorder.this.stopRecording = false;
			AudioRecorder.this.doneRecording = false;
			AudioInputStream strm = null;
			while((temp = new File(AudioRecorder.this.GPSSingleton.getSavePath() + "\\Resources\\audio" + ctr.toString() + ".wav")).exists()){
				ctr++;
			}
			
			AudioRecorder.this.fileName = "audio" + ctr.toString() + ".wav";
			try{
			
//			AudioRecorder.this.temp = new File(AudioRecorder.this.savePath);
//			AudioRecorder.this.temp = new File(AudioRecorder.this.savePath + "audio.wav"); 
			File tmpRes =  new File(AudioRecorder.this.GPSSingleton.getSavePath() + "\\Resources\\");
			if(!tmpRes.exists()){
				if(tmpRes.mkdirs()){
					
				} else {
					System.out.println("\n\rAudioRecorder.RecRunnable.run() -> Could not create directory to save in! \n\r->" + tmpRes.toString());
				}
			}
			
				tdLine.open(audioFormat);
				tdLine.start();
				strm = new AudioInputStream(tdLine);
				AudioSystem.write(strm, fileFormat, temp);
			} catch (Exception e) {
				e.printStackTrace(); // Just for debugging
			} finally {
				try {
					strm.close();
				
				} catch (IOException e) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING: Audio resources are blocked!");
					alert.setHeaderText("The audio file may not have been saved properly!");
					alert.setContentText("If the application becomes unstable, \n"
										+ "save your current progress in a new directory \n"
										+ "and restart ManuMaker.");
					alert.showAndWait();
					e.printStackTrace();
				}
	
				tdLine.stop();
				tdLine.close();
			}
		
			AudioRecorder.this.doneRecording = true;
		}
	}
}
