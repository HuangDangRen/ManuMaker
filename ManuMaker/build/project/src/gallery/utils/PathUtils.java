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
package gallery.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class PathUtils {

	/**
	 * Takes the root component out of all path-strings contained in data.
	 * @param data
	 * @param root
	 * @return
	 */
	public static String makeAllPathsRelative(String data, String root) {
		String ret = null;

		ret = data.replace(root, "");
		
		return ret;
	}
	
	
	/**
	 * Finds all instances of foothold and appends it with the absolute root. 
	 * 
	 * the absRoot string MUST contain the foothold-pattern.
	 * 
	 * @param data
	 * @param foothold
	 * @param absRoot
	 * @return
	 */
	public static String makeAllPathsAbsolute(String data, String foothold, String absRoot) throws InvalidParameterException {
		String ret = null;
		if(absRoot.contains(foothold)){
			ret = data.replace(foothold, absRoot);
		} else {
			throw new InvalidParameterException();
		}
		return ret;
	}
	
	/**
	 * Takes a path string and makes it relative, by replacing the given root component with 
	 * an empty string. 
	 * The returned relative Path always starts with the name of the first dir or file, 
	 * without preceding "\" or "/", to allow direct use in e.g. .html file links. 
	 * 
	 * This method should work for paths using forward as well as back slashes as path-separators.
	 * 
	 * @param absoluteRoot: the part of the path that is supposed to be subtracted, e.g. C\\ProjectDocs\\
	 * @param absolutePath: the absolute path to the directory or file. Must contain the absoluteRoot.
	 * @return
	 */
	public static String makePathRelative(String absoluteRoot, String absolutePath) throws InvalidParameterException {
		String ret = null;
		if(absolutePath.contains(absoluteRoot) && absolutePath.startsWith(absoluteRoot)){
			ret = absolutePath.replace(absoluteRoot, "");
			while(ret.startsWith("\\")){									// Purge backslashes at beginning of filepath.
				ret = ret.substring("\\".length());
			}
			while(ret.startsWith("/")){										// Purge slashes at beginning of filepath.
				ret = ret.substring("/".length());
			}
		} else {
			throw new InvalidParameterException();
		}
		
		
		return ret; 
	}
	

	/**
	 * Takes a root path string and concatenates a relative path string. 
	 * 
	 * Giving a Path Separator is optional, if set to null "\\" is used. 
	 *  
	 * @param absoluteRoot
	 * @param relativePath
	 * @param pathSeparator
	 * @return
	 * @throws InvalidParameterException
	 */
	public static String makePathAbsolute(String absoluteRoot, String relativePath, String nPathSeparator) throws InvalidParameterException {
		String ret = null;
		String pathSeparator = "\\";
		
		if(nPathSeparator != null && nPathSeparator.isEmpty() == false){
			pathSeparator = nPathSeparator;			
		}
		
		if(absoluteRoot != null && relativePath != null){
			ret = absoluteRoot.concat(pathSeparator + relativePath);
			if(ret.contains(pathSeparator + pathSeparator)){
				ret = ret.replace(pathSeparator + pathSeparator, pathSeparator);		// Fixes double separators, e.g. Dir1\\\\Dir2 
			}
		} else {
			throw new InvalidParameterException();
		}
		
		
		return ret; 
	}
	
	
	/**
	 * Checks if a given path exists. 
	 * If it does not exists, the directories / files are created. 
	 * 
	 * Returns false if path could not be created, 
	 * true if the path existed or was successfully created. 
	 * @param path
	 * @return
	 */
	public static boolean checkIfPathExistsAndMake(File path){
		boolean ret = false;

		if (!path.exists()) {
			
			if (path.mkdirs()) {
				ret = true;
	            System.out.println("PathUtils.checkIfPathExistsAndMake(): sub directories created successfully");
	        } else {
	        	ret = false;
	            System.out.println("PathUtils.checkIfPathExistsAndMake(): failed to create sub directories");
	        }
	    } else {
	    	ret = true;
	    }
		
		return ret;
	}
	
	
	/**
	 * Checks if a given path exists. 
	 * If it does not exists, the directories / files are created. 
	 * 
	 * Returns false if path could not be created, 
	 * true if the path existed or was successfully created. 
	 * @param path
	 * @return
	 */
	public static boolean checkIfPathExistsAndMake(String nPath){
		boolean ret = false;
		
		ret = PathUtils.checkIfPathExistsAndMake(new File(nPath));
		
		return ret;
	}
	
	/**
	 * Deletes an enumerated series of files.
	 * 
	 * Starts at 0 and counts up until a file with the current number cannot be found.
	 *  Only works for gap-free enumerations.
	 * 
	 * @param path
	 * @param filename
	 * @param ctrMark
	 * @return
	 */
	public static boolean deleteNumberedFiles(File path, String filename, String ctrMark){
		boolean deleted = true;
		File temp = null;
		Integer ctr = 0;
			while((temp = new File(path.getPath() + "\\" + filename.replace(ctrMark, ctr.toString()))).exists()){
				try {
					if(temp.isFile()){
						Files.delete(temp.toPath());
						deleted = deleted && true;
					} else {
						deleted = deleted && false;
					}
					
				} catch (IOException e) {
			    	Alert alert = new Alert(AlertType.ERROR);
			    	alert.setTitle("ERROR: Some files could not be deleted!");
			    	alert.setHeaderText("File series could not be deleted completely!");
			    	alert.setContentText(	"The project structure may be corrupted. \n\r"
			    					+ 		"Some data may be salvageable through manual \n"
			    					+ 		"completion of the task.");
			    	alert.showAndWait();
					e.printStackTrace();
					deleted = deleted && false;
				}
				ctr++;
			}
		
		return deleted;
	}
	
	/**
	 * Deletes file series with enumerated names.
	 * 
	 * Deletes all files that exist between start and end, 
	 * if filenames in the series do not exist, they are skipped 
	 * without comment.
	 * 
	 * @param path
	 * @param filename
	 * @param ctrMark
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean deleteNumberedFiles(File path, String filename, String ctrMark, Integer start, Integer end){
		boolean deleted = true;
		File temp = null;
		Integer ctr = 0;
			for(ctr = start; ctr <= end; ctr++){
				if((temp = new File(path.getPath() + "\\" + filename.replace(ctrMark, ctr.toString()))).exists()){
					try {
						if(temp.isFile()) {
							Files.delete(temp.toPath());
							deleted = deleted && true;
						} else {
							deleted = deleted && false;
						}
						
					} catch (IOException e) {
				    	Alert alert = new Alert(AlertType.ERROR);
				    	alert.setTitle("ERROR: Some files could not be deleted!");
				    	alert.setHeaderText("File series could not be deleted completely!");
				    	alert.setContentText(	"The project structure may be corrupted. \n\r"
				    					+ 		"Some data may be salvageable through manual \n"
				    					+ 		"completion of the task.");
				    	alert.showAndWait();
						e.printStackTrace();
						deleted = deleted && false;
					}
				}
			}
		
		return deleted;
	}
	
	
	/**
	 * Deletes a defined series of files with double enumeration.
	 * 
	 * @param path
	 * @param filename
	 * @param ctrMark
	 * @param subCtrMark
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean deleteNumberedFiles(File path, String filename, String ctrMark, String subCtrMark, Integer start, Integer end){
		boolean deleted = true;
		File temp = null;
		Integer ctr = 0;
		Integer sub = 0;
			for(ctr = start; ctr <= end; ctr++){
				sub = 0;
				while((temp = new File(path.getPath() + "\\" + filename
						.replace(ctrMark, ctr.toString())
						.replace(subCtrMark, sub.toString()))).exists()){
					try {
						if(temp.isFile()){
							Files.delete(temp.toPath());
							deleted = deleted && true;
						} else {
							deleted = deleted && false;
						}
					
					} catch (IOException e) {
				    	Alert alert = new Alert(AlertType.ERROR);
				    	alert.setTitle("ERROR: Some files could not be deleted!");
				    	alert.setHeaderText("File series could not be deleted completely!");
				    	alert.setContentText(	"The project structure may be corrupted. \n\r"
				    					+ 		"Some data may be salvageable through manual \n"
				    					+ 		"completion of the task.");
				    	alert.showAndWait();
						e.printStackTrace();
						deleted = deleted && false;
					}
					sub++;
				}
			}
		
		return deleted;
	}
	
	
	
	/**
	 * Writes String to textfile. 
	 * 
	 * Directory must be created beforehand and must be writable
	 * @param textfile
	 * @param data
	 * @param encoding
	 */
	public static void writeToTextFile(File textfile, String data, String encoding){
		try {
			BufferedWriter textWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(textfile.getAbsolutePath()), encoding));		
		
		    textWriter.append(data);
		    textWriter.flush();
		    textWriter.close();
			//textWriter.write(data);
		} catch (IOException e) {
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("ERROR: File could not be written!");
	    	alert.setHeaderText("File could not be written!");
	    	alert.setContentText(	"The project structure may be corrupted. \n\r");
	    	alert.showAndWait();
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * Loads all lines of a Text-File into a List<String> object and returns it. 
	 * @param textfile
	 * @return
	 * @throws IOException 
	 */
	public static List<String> textFileLoadLines(File textfile) throws IOException {
		List<String> text = new ArrayList<String>();
		
	
		text = Files.readAllLines(textfile.toPath().toAbsolutePath(), Charset.forName("UTF8"));

		
		return text;		
	}
	
	
	/**
	 * Saves a List<String> object to textfile (.txt, UTF-8 encoded) by putting each list-item in a new line. 
	 * 
	 * The Path needs to be created beforehand.
	 * 
	 * The File should specify path, filename, and file type.
	 * 
	 * NOTE that if your strings contain linebreaks this method can be used for saving, 
	 * but loading the file with the PathUtils.textFileLoadLines( ) will result in a 
	 * different list object!
	 * @param textfile
	 * @param textData
	 * @return
	 */
	public static boolean textFileSaveLines(File textfile, List<String> textData) {
		String text = "";
		boolean saved = false;
		BufferedWriter textWriter = null;
		
	    // Create text file and save
	    for (int i = 0; i < textData.size(); i++) {
	    	if(textData.get(i).endsWith("\n")){
	    		text += (textData.get(i));		// Settings are saved in separate lines
	    	} else {
	    		text += (textData.get(i) +"\n");		// Settings are saved in separate lines
	    	}
	    }
		try {
			textWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(textfile.getPath()), "UTF8"));		
		    textWriter.append(text);
		    textWriter.flush();
		    textWriter.close();

			//textWriter.write(data);
		} catch (IOException e) {
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("ERROR: File could not be written!");
	    	alert.setHeaderText("File could not be written!");
	    	alert.setContentText(	"The project structure may be corrupted. \n\r");
	    	alert.showAndWait();
			e.printStackTrace();
		}
		
		return saved;		
	}
	
	
	/**
	 * Saves a given image to file system, given a path-file and a filename.
	 * 
	 * The filename should be given with .png extension.
	 * Directories will be created if the given filepath does not exist in full. 
	 * 
	 * returns true if file was saved successfully, false otherwise.
	 * 
	 * @param imagePath
	 * @param imageFile
	 * @param fileName
	 * @return
	 */
	public static boolean saveImagePng(File imagePath, Image imageFile, String fileName) {
		boolean ret = false;
		if(imagePath != null && imageFile != null && fileName != null) {
		    if (!imagePath.exists()) {
		        if (imagePath.mkdirs()) {
		            System.out.println("\r\nsub directories created successfully");
		        } else {
		            System.out.println("\r\nfailed to create sub directories");
		        }
		    }
	   
		    try {
			    // Create Image file and save
			    File imgFile = new File(imagePath.getAbsolutePath() + "\\" + fileName);
			    BufferedImage bImage = SwingFXUtils.fromFXImage(imageFile, null);
		    	ImageIO.write(bImage,"png", imgFile);	
		    	 System.out.println("\r\nSaved image : " + imagePath.toString() + "\\" + fileName);
		    	ret = true;
		    	//System.out.println("PathUtils.saveImagePng() -> Successfully saved image at " + imagePath.getAbsolutePath() + "\\" + fileName);
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
	    }
		return ret;
	}
	
	
	/**
	 * Loads an image file at specified path (down to filename + filetype!)
	 * 
	 * Returns image if it could be loaded.
	 * Returns null if image was not loaded.
	 * @param imageFile
	 * @return
	 */
	public static Image loadImagePng(File imageFile){
		Image nImage = null;
	    if (!imageFile.exists()) {
	        System.out.println("\n\rPathUtils.loadImagePng() -> FILE NOT FOUND AT PATH! Please search for it manually...");
	    } else {
	    	BufferedImage bImage = null;
	    	try {
	   		    // Create Image file and save
	   		    //File imgFile = new File(imageFile.getAbsolutePath());
	   	    	bImage = ImageIO.read(imageFile);		  
	   	    	nImage = SwingFXUtils.toFXImage(bImage, null);
//	   	    	System.out.println("\n\rPathUtils.loadImagePng() -> Successfully loaded image...");

	   		    // Create image title file and save
	   	    } catch (IOException e) {
	   	    	e.printStackTrace();
	   	    }
	    }
	    return nImage;
	}
	
}
