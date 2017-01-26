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
import java.util.ArrayList;
import java.util.List;
import javafx.scene.media.Media;

/**
 * This class defines a gallery slide which contains instructions and instruction-related information
 * @author Daniel Lachmann	
 *
 */
public class GallerySlide extends GallerySafetySlide {

	protected TagList parts = new TagList();
	protected TagList tools = new TagList();
	protected AudioDraft audioDraft = null;
	protected String audioFileName = "";
	
	private void init(){
		this.picture = new SlideInstructionalPic();
		this.audioDraft = new AudioDraft();
	}
	
	public GallerySlide() {
		init();
	}
	public GallerySlide(Integer sldNumber) {
		init();
		this.galleryItemLoad(sldNumber);
	}
	
	public void setAudioFileName(String nAudioName) {
		if(nAudioName != null) {
			this.audioFileName = nAudioName;
		} else {
			this.audioFileName = "";
		}
	}
	
	public String getAudioFileName(){
		return this.audioFileName;
	}
	
	public void setPicture(SlidePic nPic) {
		this.picture = nPic;
	}
	
	@Override
	public void deletePicture() {
		this.picture = new SlideInstructionalPic();
	}

	
	/**
	 * Sets the member object containing the audio draft. Can be set to null.
	 * @param nAD
	 */
	public void setAudioDraft(Media nAD, String nAudioFileName) {
		File currentAudio = new File(this.GPSSingleton.getSavePath() + "\\Resources\\" + this.audioFileName);
		File newAudio = new File(this.GPSSingleton.getSavePath() + "\\Resources\\" + nAudioFileName);
		
		if(this.audioDraft == null){
			this.audioDraft = new AudioDraft();
		}
		
		if(nAD != null && newAudio.exists()){
			this.audioDraft.setAudioDraft(nAD, newAudio);
			this.audioFileName = nAudioFileName;
		} else {
		//	System.out.println("GallerySlide.setAudioDraft() -> Media parameter null or new File file does not exist.");
		}
	}
	
	public void deleteAudioDraft(){
		File ad = new File(this.GPSSingleton.getSavePath() + "\\Resources\\" + audioFileName);
		if(ad.exists()){
			
			this.audioDraft.deleteAudioDraft();
			this.audioFileName = "";
		}
	}
	
	/**
	 * Gets the AudioDraft of a GallerySlide. Returns null if none is present.
	 * @return
	 */
	public Media getAudioDraft(){
		return this.audioDraft.getAudioDraft();
	}
	
	/**
	 * Gets the parts contained in one string separated by a delimiter
	 * @return [String]
	 */
	public String getParts() {
		return this.parts.getTagString();
	}
	
	/**
	 * Takes a string containing substrings separated by a delimiter and 
	 * stores them.
	 * @param nParts [String]
	 */
	public void setParts(String nParts) {
		this.parts.setTags(nParts);
	}
	
	public TagList getPartList() {
		return this.parts;
	}
	
	/**
	 * Gets the tools contained in one string separated by a delimiter
	 * @return [String]
	 */
	public String getTools() {
		return this.tools.getTagString();
	}
	
	/**
	 * Takes a string containing substrings separated by a delimiter and 
	 * stores them.
	 * @param nParts [String]
	 */
	public void setTools(String nTools) {
		this.tools.setTags(nTools);
	}
	
	public TagList getToolList() {
		return this.tools;
	}
	
	
	/**
	 * Returns true if the slide contains information.
	 * The Annotation text fields are checked for empty strings.
	 * The picture is checked, if one has been set.
	 * The audio Draft is checked if one is present.
	 * Tag fields are not evaluated.
	 * @return
	 */
	@Override
	public boolean isInitialized() {
		boolean ret = false; 
		ret = super.isInitialized();
		ret = ret || (this.audioDraft.getAudioDraft() != null);
		
		return ret;
	}
	

	public List<String> galleryItemLoad(Integer sldNumber) {
		List<String> data = new ArrayList<String>();
		File testStill = new File(this.GPSSingleton.getSavePath() + "\\Resources\\slide_" + sldNumber.toString() + ".png");
		File testSMP = new File(this.GPSSingleton.getSavePath() + "\\Resources\\slide_" + sldNumber.toString() + "_" + "0.png");
		data = super.galleryItemLoad(sldNumber);
		Integer iteratorCTR = 0;
		
	 	// Get AudioPath Fields: 
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("§AUDIOPATH[")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"§AUDIOPATH[\"");
	 	}
	 	iteratorCTR++;
	 	this.audioFileName = data.get(iteratorCTR++).replace("§", ""); 	
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("]AUDIOPATH§")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"]AUDIOPATH§\"");
	 	}
		
	 	// Get Parts: 
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("§PARTS[")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"§PARTS[\"");
	 	}
	 	iteratorCTR++;
	 	this.parts.setTags(data.get(iteratorCTR++).replace("§", ""));	
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("]PARTS§")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"]PARTS§\"");
	 	}
		
	 	// Get Tools: 
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("§TOOLS[")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"§TOOLS[\"");
	 	}
	 	iteratorCTR++;
	 	this.tools.setTags(data.get(iteratorCTR++).replace("§", ""));	
	 	while(iteratorInvar(data, iteratorCTR) && !data.get(iteratorCTR).contains("]TOOLS§")){
	 		iteratorCTR++;
	 	//	System.out.println("GalleryItem.galleryItemLoad() -> looking for \"]TOOLS§\"");
	 	}
		
		
		if(testSMP.exists()) {
			this.picture = new SlideStopMotion();
		//	System.out.println("\n\rGallerySlide.galleryItemLoad: Slide " + sldNumber + " Loaded successfully: " + ((SlideStopMotion)this.picture).loadImage(sldNumber));
		} else if (testStill.exists()) {
			this.picture = new SlideInstructionalPic();
		//	System.out.println("\n\rGallerySlide.galleryItemLoad: Slide " + sldNumber + " Loaded successfully: " + ((SlideInstructionalPic)this.picture).loadImage(sldNumber));
		} else {
		//	System.out.println("\n\rGallerySlide.galleryItemLoad: Slide " + sldNumber + " does not seem to contain an image.");
		}
		File tmpAudio = new File(GPSSingleton.getSavePath() + "\\Resources\\"+ this.audioFileName);
		if(audioFileName != "" && tmpAudio.exists() && tmpAudio.isFile()){
			this.audioDraft.load(this.audioFileName);
		} else {
		//	System.out.println("\n\rGallerySlide.galleryItemLoad: Slide " + sldNumber + " does not seem to contain an audioDraft.");
		}
		
		return super.galleryItemLoad(sldNumber);
	}

	
	public void galleryItemSave(Integer sldNumber, List<String> nData) {
		List<String> data = null;
		if(sldNumber != null){
			if(nData != null){
				data = nData; 
			} else {
				data = new ArrayList<String>();
			}
			// 1. Save picture
			
			if(this.picture.getClass().equals(SlideStopMotion.class)){
				((SlideStopMotion)this.picture).saveImage(sldNumber);
			} else if (this.picture.getClass().equals(SlideInstructionalPic.class)){
				((SlideInstructionalPic)this.picture).saveImage(sldNumber);
			} else {
				this.picture.saveImage(sldNumber);
			}
		
		
			// 2. Save audio Draft
	//		if(this.audioDraft != null){	//DONE BY AudioRecorder!
	//			this.audioPath = this.audioDraft.save(sldNumber);
	//		}
		
			data.add("§AUDIOPATH[");
			data.add("§" + this.audioFileName);
			data.add("]AUDIOPATH§");
			
			
			// 3. Add Parts
			data.add("§PARTS[");
			data.add("§" + this.getParts());
			data.add("]PARTS§");
			
			// 4. Add Tools
			data.add("§TOOLS[");
			data.add("§" + this.getTools());
			data.add("]TOOLS§");
			
			
			super.galleryItemSave(sldNumber, data);
		}
	}
	
	
}
