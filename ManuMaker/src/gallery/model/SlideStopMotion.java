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

import gallery.utils.PathUtils;
import gallery.utils.WarningLevel;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class SlideStopMotion extends SlideInstructionalPic {
	private Integer ctr = 0;
	private List<Image> imgList = new ArrayList<Image>();
	private List<String> timeStampList = new ArrayList<String>();
	/**
	 * Invariant which ensures the counter never exceeds imgList.size()
	 * Has to be called at the very end of every method defined in this class.
	 */
	private void invarCtr() {
		if(this.ctr >= this.imgList.size() || this.ctr >= this.timeStampList.size()) {
			this.ctr = 0;
		}
		
		if(this.imgList.size() < this.timeStampList.size()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning: Stop Motion Picture not correctly formatted");
			alert.setHeaderText(alert.getTitle());
			alert.setContentText("Timestamp list longer than Image list. Attempting to fix the Picture Series. \n"
					+ "Please check your manual if all frames are accounted for.");
			alert.showAndWait();
			
			while(this.imgList.size() < this.timeStampList.size()){
				this.timeStampList.remove(this.timeStampList.size()-1);
			}
		}
		
		if(this.imgList.size() > this.timeStampList.size()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning: Stop Motion Picture not correctly formatted");
			alert.setHeaderText(alert.getTitle());
			alert.setContentText("Image list larger than timestamp list. Attempting to fix the Picture Series. \n"
					+ "Please check your manual if all frames are accounted for.");
			alert.showAndWait();
			Integer i = this.imgList.size();
			while(this.imgList.size() > this.timeStampList.size()){
				this.timeStampList.add(i.toString());
				i++;
			}
		}
		
	}
	
	@Override
	public Image getImage(){
		Image tempImg = null;
		if(this.imgList != null && this.imgList.size() > this.ctr) {
			tempImg = this.imgList.get(this.ctr);
			ctr++;
			invarCtr();
		} 
		return tempImg;
	}
	
	
	public Image getImage(Integer index){
		Image tempImg = null;
		if(index != null){
			if(this.imgList != null && this.imgList.size() > index) {
				tempImg = this.imgList.get(index);
				ctr++;
				invarCtr();
			} 
		}

		return tempImg;
	}
	
	
	public SMPFrame getSMPFrame() {
		SMPFrame temp = new SMPFrame();
		if(this.imgList != null && this.imgList.size() > this.ctr) {
			temp.setImage(this.imgList.get(this.ctr));
			temp.setTimestamp(this.timeStampList.get(this.ctr));
			temp.setOrdinal(this.ctr);
			ctr++;
			invarCtr();
		} 
		return temp;
	}
	
	@Override
	public Image getFramedImage(WarningLevel currentLevel)  {
		Image tempImg = this.imgList.get(this.ctr++);
		if(currentLevel != null) {
			
			invarCtr();
//			tempImg = this.warningFrame.getMarkedImage(tempImg, currentLevel);
		} else {
			 
		}
		
		return tempImg;
	}
	
	/**
	 * Returns the number of frames contained in this image.
	 * @return
	 */
	public Integer getSize(){
		return this.imgList.size();
	}
	
	public Integer getCurrentIndex(){
		return this.ctr;
	}
	public void reset(){
		this.ctr = 0;
	}
	
	public void addImage(Integer pos, Image nImage, String timeStamp)  {
		if(pos != null && nImage != null && timeStamp != null) {
			this.imgList.add(pos, nImage);	
			this.timeStampList.add(pos, timeStamp);
		} else {
			System.out.println("!!ERROR: SlideStopMotion.addImage(): one or more arguments are null.");
			 
		}
	}
	
	public void addImage(SMPFrame nFrame)  {
		if(nFrame != null){
			this.imgList.add(nFrame.getOrdinal(), nFrame.getImage());
			this.timeStampList.add(nFrame.getOrdinal(), nFrame.getTimestamp());
		} else {
			System.out.println("!!ERROR: SlideStopMotion.addImage(SMPFrame): argument is null.");
			 
		}
	}
	
	public void deleteImage(int pos) {
		if(this.imgList.size() > pos) {
			this.imgList.remove(pos);
			this.timeStampList.remove(pos);
			this.ctr--;
		} else {
			System.out.println("!!ERROR: SlideStopMotion.deleteImage(): given index exceeds List.size()!");
		}
		this.invarCtr();
	}
	
	public void clearList(){
		this.imgList.clear();
		this.timeStampList.clear();
		this.ctr = 0;
		this.invarCtr();
	}
	
	@Override
	public boolean isImageSet() {
		if(this.imgList.size() >= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean loadImage(Integer sldNumber){
		boolean loaded = false;
		File imagePath = null;
		Integer sub_index = 0;
		
//		try {
			this.imgList.clear();
			// Sorry to whoever reads this... assign imagePath the next image, and see if a file with that name exists.
			while((imagePath = new File(this.GPSSingleton.getSavePath() + "\\Resources\\slide_" + sldNumber + "_" + sub_index + ".png")).exists() 
					&& imagePath.canRead()){
				this.addImage(sub_index, PathUtils.loadImagePng(imagePath), "Frame " + sub_index.toString());
				sub_index++;
			}
			
			if(this.imgList.size() > 0){
				loaded = true;
			}		
		
//		} catch (IOException e) {
//	    	Alert alert = new Alert(AlertType.WARNING);
//	    	alert.setTitle("WARNING: Stop Motion Image could not be loaded!");
//	    	alert.setHeaderText("Stop Motion Image missing or corrupted!");
//	    	alert.setContentText(	"Please check the project directory, if all frames of the image exist.\n\r"
//	    					+ 		"If the files are available, you can follow these steps \n"
//	    					+ 		"to recostruct the image-series:\n\r"
//	    					+ 		"-> BEFORE closing ManuMaker, open the project folder in your file-explorer \n\r"
//	    					+ 		"-> Copy all the image frames into a backup directory \n\r"
//	    					+ 		"-> Save your project and close ManuMaker \n\r"
//	    					+ 		"-> Restart the Application, and navigate to the slide in question. \n\r"
//	    					+ 		"-> Open the StopMotionMaker tool and add the backed-up frames via the \n"
//	    					+ 		"  \"Insert from Clipboard\" button.");
//	    	
//	    	alert.showAndWait();
//	    	
//			this.imgList.clear();
//			this.timeStampList.clear();
//			this.ctr = 0;
//			loaded = false;
//		}
		return loaded;
	}
	
	@Override
	public boolean saveImage(Integer sldNumber){
		boolean saved = false;
		File savePath = new File(this.GPSSingleton.getSavePath() + "\\Resources\\");
//		File timeStamps = new File(this.GPSSingleton.getSavePath() + "\\Resources\\timeStamps" + sldNumber + ".txt");
		
		PathUtils.checkIfPathExistsAndMake(savePath);
//		PathUtils.textFileSaveLines(timeStamps, timeStampList);
				
		for(Integer i = 0; i < imgList.size(); i++) {
			//saved = saved && 
			System.out.println("\n\rSlideStopMotion.saveImage: Saving Frame...");
			saved = PathUtils.saveImagePng(savePath, this.imgList.get(i), "slide_" + sldNumber.toString() + "_" + i.toString() + ".png");
		}

		return saved; 
	}
	
	
}
