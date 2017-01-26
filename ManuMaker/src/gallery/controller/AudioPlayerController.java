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

import gallery.abstractclasses.ControllerAbstr;
import gallery.model.AudioRecorder;
import gallery.model.GallerySlide;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayerController extends ControllerAbstr{
	private Media audioClip = null; 
	private MediaPlayer mediaPlayer = null;
	private AudioRecorder rec = null;
	private Duration clipDuration = null;
	@FXML
	private AnchorPane AP_AudioPlayer;
	@FXML
	private Slider SLDR_Time;
	@FXML
	private Button BT_PlayPause;
	@FXML
	private Button BT_Stop;
	@FXML
	private Slider SLDR_Volume;
	@FXML
	private Button BT_StopRecording;
	private GalleryBrowserController owningController = null;
	private boolean playing = false;
	private SimpleStringProperty labelText = new SimpleStringProperty("00:00:00"); 
	
	/** 
	 * Sets the reference to the owning controller
	 * @param nOC
	 */
	public void setOwningController(GalleryBrowserController nGBC) {
		try{
			this.owningController = nGBC;
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		this.invar("setOwningController() @ end of method");
 	}
	
	
	/**
	 * Enables / disables the controls of the controller.
	 * @param nBool
	 */
	public void disableGUI(boolean nBool) {
		this.AP_AudioPlayer.setVisible(!nBool);
		this.BT_PlayPause.disableProperty().set(nBool);
		this.BT_Stop.disableProperty().set(nBool);
		this.SLDR_Time.setDisable(nBool);
		//this.mediaPlayer.seek(new Duration(0.0));
	}
	
	
	public void updateBindings() {
		if((this.mediaPlayer != null) && (this.audioClip != null)) {
			this.mediaPlayer.volumeProperty().bind(this.SLDR_Volume.valueProperty());
			
			
			
			this.SLDR_Time.valueProperty().addListener(new InvalidationListener() {
				public void invalidated(Observable  obs){
					if(AudioPlayerController.this.SLDR_Time.isValueChanging()){
						AudioPlayerController.this.mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(SLDR_Time.getValue() / 100.0));
						AudioPlayerController.this.playing = false;
						AudioPlayerController.this.mediaPlayer.pause();
					}
				}
			});
			
			this.mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
				public void invalidated(Observable  obs){
					AudioPlayerController.this.SLDR_Time.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
					AudioPlayerController.this.SLDR_Time.setValue(mediaPlayer.currentTimeProperty().getValue().toSeconds());
				}
			});

		mediaPlayer.setOnReady(() -> {
			clipDuration = mediaPlayer.getMedia().getDuration();
			updateUI();
		});
			
		} else {
			System.out.println("\n\rAudioPlayerController.updateBindings() -> mediaPlayer OR audioClip is null!");
		}
	}
	

	
	
	@Override
	public void updateUI() {
		if(mediaPlayer != null && audioClip != null){
			
			Platform.runLater(() -> {
				Duration currentProgress = mediaPlayer.getCurrentTime();
				//this.labelText.set(TimeUtils.durationSecToString(currentProgress));
			});	
			
		}else {
			System.out.println("\n\rAudioPlayerController.updateUI() -> mediaPlayer OR audioClip is null!");
		}
	}

	@Override
	public void initCTRL() {
		this.rec = new AudioRecorder();	
	}
	
	public void setAudioClip(Media nAC){
		if(nAC != null) {
			this.audioClip = nAC;
			if(this.mediaPlayer != null){
				this.mediaPlayer.dispose();
				this.mediaPlayer = null;
			}
			this.mediaPlayer = new MediaPlayer(this.audioClip);
			updateBindings();
			updateUI();
		} 		
	}
	

	@FXML
	public void handlePlayPause() {
		if(this.mediaPlayer != null){
			if(playing == false) {	
				playing = true;
				this.mediaPlayer.play();
			} else {
				playing = false;
				this.mediaPlayer.pause();
			}
		}
	}
	
	@FXML
	public void handleStop() {
		if(this.mediaPlayer != null){
			if(playing == true) {
				playing = false;
				this.mediaPlayer.stop();
				this.SLDR_Time.setValue(0.0);
			} 
		}
	}
	
	@FXML
	public void handleVolDrag() {
		//Handled via binding for the moment.
	}

	@FXML
	public void handleTimeDrag() {
		this.clipDuration = mediaPlayer.getTotalDuration();
		if(this.clipDuration != null){
			this.mediaPlayer.seek(clipDuration.multiply(this.SLDR_Time.getValue()/this.SLDR_Time.getMax()));
		}
	}
	
	public void deleteAudio() {
		this.audioClip = null;
		if(mediaPlayer != null){
			this.mediaPlayer.dispose();
			this.mediaPlayer = null;
		}
		this.rec.resetRecorder();
		updateBindings();
	}
	public void handleRecord() {
		if(this.rec != null) {
				System.out.println("\n\rAudioPlayerController.handleRecord() -> Start Recording!");
				this.rec.record();
			}

		updateBindings();
		updateUI();
	}

	@FXML
	public void handleStopRecording(){
		if(this.rec.doneRecording == false) {//IF record method is running...
			this.audioClip = this.rec.getRecordedMedia();
			if(this.audioClip != null){
				((GallerySlide)this.owningController.currentSlide).setAudioDraft(this.audioClip, this.rec.getLastMediaPath());
				this.mediaPlayer = new MediaPlayer(((GallerySlide)this.owningController.currentSlide).getAudioDraft());
				this.owningController.currentSlide.galleryItemSave(this.owningController.activeGallery.getCurrentIndex(), null);
				System.out.println("\n\rAudioPlayerController.handleRecord() -> Got recorded media!");
			}

			updateUI();
		} 
		this.owningController.audioRecorderDone();
	}

	@Override
	protected boolean invar(String text) {
		boolean check = true;
		check = check & (this.mainApp != null);
		check = check & (this.owningController != null); 
		
		if(check == false){
			System.out.println("\n\rAudioPlayerController->" + text + "--> invar not satisfied!\n\r");
		}
		return check;
	}
	
}
