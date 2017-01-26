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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

import gallery.abstractclasses.GalleryItem;
import gallery.model.Gallery;
import gallery.model.GalleryChapter;
import gallery.model.GalleryProjectSettings;
import gallery.model.GallerySafetySlide;
import gallery.model.GallerySlide;
import gallery.model.GalleryTitle;
import gallery.model.SlideStopMotion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class HTMLMaker {
	GalleryProjectSettings GPSSingleton = GalleryProjectSettings.INSTANCE;
	

	
	private static String fillGalleryTitle(GalleryTitle slide, String template, String savePath){
		final String imageTMP = "<img style=\"display:block;\" src=\"pllogobasepathpl/Logo.png\" alt=\"\" width=\"100%\">";
		String ret = template;
		
		// 1. Add image link to template, if image exists: 
		if(((GalleryTitle)slide).getLogoPathAbstract() != ""){
			ret = ret.replace("pllogopl", "<img style=\"display:block;\" src=\"pllogobasepathpl/Logo.png\" alt=\"\" width=\"100%\">").replace("pllogobasepathpl/", "file:Resources/");
		} else {
			ret = ret.replace("pllogobasepathpl/Logo.png", "");
		}
		
		
		// 2. Documentation type
		ret = ret.replace("pldoctypepl", TypeUtils.newlinesToHtml(slide.getDocType()));
		// 3. Product name 
		ret = ret.replace("plproductpl", TypeUtils.newlinesToHtml(slide.getProductName()));
		// 4. ProductVersion 
		ret = ret.replace("plvertpl", TypeUtils.newlinesToHtml(slide.getProductVersionHeader()));
		ret = ret.replace("plversionpl", TypeUtils.newlinesToHtml(slide.getProductVersion()));
		
		// 5. Audience 
		if(slide.getAudience().isEmpty()){
			ret = ret.replace("plaudtpl", "");
		} else {
			ret = ret.replace("plaudtpl", "Intended Readers:");
		}
		ret = ret.replace("plaudiencepl", TypeUtils.newlinesToHtml(slide.getAudience()));
		
		// 6. Additional Data: 
		ret = ret.replace("plref1pl", TypeUtils.newlinesToHtml(slide.getRef1()));
		ret = ret.replace("pldata1pl", TypeUtils.newlinesToHtml(slide.getData1()));
		
		// 7. Authors
		ret = ret.replace("plauthorpl", TypeUtils.newlinesToHtml(slide.getAuthor()));
		
		// 8. Manual Revision
		ret = ret.replace("plrevtpl", TypeUtils.newlinesToHtml(slide.getDocRevisionHeader()));
		ret = ret.replace("plrevisionpl", TypeUtils.newlinesToHtml(slide.getDocRevision()));
		
		// 9. Publisher:
		ret = ret.replace("plpublpl", TypeUtils.newlinesToHtml(slide.getCompanyNameHeader()));
		ret = ret.replace("plcompanypl", TypeUtils.newlinesToHtml(slide.getCompanyName()));
		
		// 10. Address
		ret = ret.replace("pladdresspl", TypeUtils.newlinesToHtml(slide.getAddress()));

		// 11. Contact Info
		ret = ret.replace("plconttpl", TypeUtils.newlinesToHtml(slide.getContactInfoHeader()));
		ret = ret.replace("plcontactpl", TypeUtils.newlinesToHtml(slide.getContactInfo()));
		
		return ret;
	}
	
	private static String fillGalleryChapter(GalleryChapter slide, String template, String savePath){
		String ret = template;
		
		// 1. Chapter number:
		ret = ret.replace("plchnumpl", slide.getChapterNo());
		// 2. Replace (slide =)project title in Template
		ret = ret.replace("pltitlepl", TypeUtils.newlinesToHtml(slide.getSimpleTitle()));
		
		// 3. Audience 
		ret = ret.replace("plaudiencepl", TypeUtils.newlinesToHtml(slide.getAudience()));
		
		// 4. Additional Data: 
		ret = ret.replace("plref1pl", TypeUtils.newlinesToHtml(slide.getRef1()));
		ret = ret.replace("pldata1pl", TypeUtils.newlinesToHtml(slide.getData1()));
		
		// 5. Authors
		ret = ret.replace("plauthorpl", TypeUtils.newlinesToHtml(slide.getAuthor()));
		
		
		// 6. Publisher:
		ret = ret.replace("plpublpl", 	 TypeUtils.newlinesToHtml(slide.getCompanyNameHeader()));
		ret = ret.replace("plcompanypl", TypeUtils.newlinesToHtml(slide.getCompanyName()));
		
		// 7. Address
		ret = ret.replace("pladdresspl", TypeUtils.newlinesToHtml(slide.getAddress()));

		// 8. Contact Info
		ret = ret.replace("plconttpl", TypeUtils.newlinesToHtml(slide.getContactInfoHeader()));
		ret = ret.replace("plcontactpl", TypeUtils.newlinesToHtml(slide.getContactInfo()));
		
		return ret;
	}
	
	private static String fillGallerySlide(GallerySlide slide, String template, String imgSavePath){
		final String imageTMP = "<img style=\"display:block;\" src=\"plimgpathpl\" alt=\"\" width=\"100%\">";
		final String imageMultiSingleTMP = "<img style=\"display:block;\" src=\"plimgpathpl\" alt=\"\" width=\"100%\">";
		final String imageMultiDualTMP = "<img style=\"display:block;\" src=\"plimgpathpl\" alt=\"\" width=\"100%\">";
		String ret = template;
		String imgFrameTMP = null;
		String imgFrameDualTMP = null;
		String allImages = "";
		
		ret = ret.replace("pltitlepl", slide.getSlideTitle());
		// Add image link to template, if image exists: 
		if(slide.getPicture().getClass().equals(SlideStopMotion.class)){			// If slide contains stop-motion images
			try {
				imgFrameTMP = TypeUtils.makeListToString(Files.readAllLines(Paths.get("resources/printable_html/TMP_ImageFrame.html"),Charset.forName("UTF8")));
				imgFrameDualTMP = TypeUtils.makeListToString(Files.readAllLines(Paths.get("resources/printable_html/TMP_ImageFrame_Dual.html"),Charset.forName("UTF8")));
			} catch (IOException e) {
				// Not of much conquence. Stack-trace is sufficient.
				e.printStackTrace();
			}
			SlideStopMotion smpic = (SlideStopMotion) slide.getPicture();
			smpic.reset(); 															//Reset so the next image requested is the first frame
			for(Integer i = 0; i < smpic.getSize(); i++) {
			
				if(smpic.getSize() - i >= 2){										//If two or more frames are left in the SMP, fill in a dual frame
					String 	dual = imgFrameDualTMP.replace("plimage1pl", imageMultiDualTMP.replace("plimgpathpl", "Resources\\slide_" + new Integer(slide.getSlideOrdinal()-1).toString() + i.toString() + ".png"));
							dual = dual.replace("plframe1pl", "Frame " + (i+1));
							i++;
							dual = dual.replace("plimage2pl", imageMultiDualTMP.replace("plimgpathpl", "Resources\\slide_" + new Integer(slide.getSlideOrdinal()-1).toString() + i.toString() + ".png"));
							dual = dual.replace("plframe2pl", "Frame " + (i+1));
					
					allImages = allImages.concat(dual);
							
				} else {															//If only one frame is left in the SMP, fill out a single template
					String 	single = imgFrameTMP.replace("plimagepl", imageMultiSingleTMP.replace("plimgpathpl", "Resources\\slide_" + new Integer(slide.getSlideOrdinal()-1).toString() + i.toString() + ".png"));
							single = single.replace("plframepl", "Frame " + (i+1));

					allImages = allImages.concat(single);
				}
			}
		
			ret = ret.replace("plimagepl", allImages);
			
		} else {																	// If slide contains static picture
			if(imgSavePath != null){
				ret = ret.replace("plimagepl", imageTMP.replace("plimgpathpl",  "Resources\\slide_" + new Integer(slide.getSlideOrdinal()-1).toString() + ".png"));
			}
		}
	
		ret = ret.replace("plannotationpl", slide.annotation.finalTextGet());
		ret = ret.replace("plpictogrampathpl", "file:Resources/SafetyData/");
		ret = ret.replace("plheaderpl", "Page " + slide.getSlideOrdinal());
		return ret;
	}
	
	
	/**
	 * Exports a gallery to a printable HTML format.
	 *  
	 * @param gallery
	 * @param nSavePath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	
	public static boolean ExportSlidesToHTML_Printable(Gallery gallery, File nSavePath, String fileName) throws IOException{
		boolean saved = false;
		boolean error = false;
//		System.out.println("\n\rHTMLMaker.ExportSlidesToHTML_Printable() -> Loading Templates...");
		List<String> doclines = Files.readAllLines(Paths.get(		"resources/printable_html/TMP_Document.html"),Charset.forName("utf-8"));
		List<String> pagelines = Files.readAllLines(Paths.get(		"resources/printable_html/TMP_Page.html"),Charset.forName("utf-8"));
		List<String> titlelines = Files.readAllLines(Paths.get(		"resources/printable_html/TMP_GalleryTitle_1.1.html"),Charset.forName("utf-8"));
		List<String> chapterlines = Files.readAllLines(Paths.get(	"resources/printable_html/TMP_GalleryChapter_1.1.html"),Charset.forName("utf-8"));
		List<String> gsstdlines = Files.readAllLines(Paths.get(		"resources/printable_html/TMP_GallerySlide_1.1.html"),Charset.forName("utf-8"));
		List<String> gstxtlines = Files.readAllLines(Paths.get(		"resources/printable_html/TMP_GallerySlideNoPic_1.1.html"),Charset.forName("utf-8"));		
		
		final String docTMP = TypeUtils.makeListToString(doclines);
		final String pageTMP = TypeUtils.makeListToString(pagelines);
		final String titleSlideTMP = TypeUtils.makeListToString(titlelines);
		final String chapterSlideTMP = TypeUtils.makeListToString(chapterlines);
		final String defaultSlideTMP = TypeUtils.makeListToString(gsstdlines);
		final String defaultSlide_TextTMP = TypeUtils.makeListToString(gstxtlines);
		
		List<GalleryItem> gallerySlides = gallery.getGallerySlides();
		String savePath =  nSavePath.toString() + "\\Resources\\";
		String documentContent = "";
		String slideTMP = null;
		
		for(GalleryItem i : gallerySlides){
			
			if(i.getClass().equals(GalleryTitle.class)) {
				
				// 1. Copy image file to export location, if it exists. 
				if(gallery.GPSSingleton.getLogoPic() != null){
					error = error || !PathUtils.saveImagePng(new File(savePath), gallery.GPSSingleton.getLogoPic(), "Logo.png");
				}
				
				// 2. Copy the folder "SafetyData" containing the warning pictograms
				File safety = new File(gallery.GPSSingleton.getSavePath() + "\\Resources\\SafetyData\\");
				if(safety.exists() && safety.isDirectory()){
					FileUtils.copyDirectory(safety, new File(savePath + "\\SafetyData\\"));
				}
				
				// 3. Fill the template 
				slideTMP = fillGalleryTitle((GalleryTitle)i, titleSlideTMP, savePath);
						
			} else if(i.getClass().equals(GalleryChapter.class)) {
				slideTMP = fillGalleryChapter((GalleryChapter)i, chapterSlideTMP, savePath);
			} else if(i.getClass().equals(GallerySafetySlide.class)) {
				error = true;
			} else if(i.getClass().equals(GallerySlide.class)) {
				if(((GallerySlide)i).getPicture().getImage() != null){
					String imgSavePath = null;
					if(((GallerySlide)i).getPicture().getClass().equals(SlideStopMotion.class)){
						SlideStopMotion pic = (SlideStopMotion)((GallerySlide)i).getPicture();
						pic.reset();
						for(Integer k = 0; k < pic.getSize() ; k++){
							imgSavePath = "\\Resources\\";
							if(pic.getImage(k) != null){
								PathUtils.saveImagePng(new File(savePath) , pic.getImage(k), "slide_" + new Integer(i.getSlideOrdinal()-1).toString() + k + ".png");
							} else {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Error in: HTMLMaker.ExportSlidesToHTML_Printable()");
								alert.setContentText("The stop-motion image seems to have empty frames.");
								alert.showAndWait();
							}
						}
					} else {
						imgSavePath =  new File("\\Resources\\slide_" + new Integer(i.getSlideOrdinal()-1) + ".png").toURI().toURL().toString();
						PathUtils.saveImagePng(new File(savePath) , ((GallerySlide)i).getPicture().getImage(), "slide_" + (i.getSlideOrdinal()-1) + ".png");
					}
					slideTMP = fillGallerySlide((GallerySlide)i, defaultSlideTMP, imgSavePath);

					
				} else {
					slideTMP = fillGallerySlide((GallerySlide)i, defaultSlide_TextTMP, null);
				}
				
			} else {
				error = true;
			}
			if(error == false){
				documentContent = documentContent.concat("\n " + pageTMP.replace("plgalleryslidepl", slideTMP));	// Insert slideTMP into page-template and add to document
				saved = true;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR: Could not determine type of slide " + i.getSlideOrdinal() + "!");
				alert.setHeaderText("ERROR!");
				alert.setContentText( "Could not determine type of slide " + i.getSlideOrdinal() + "! \n"
									+ "Exporting has been aborted, there might be \n"
									+ "residual data left at the target location.");
				alert.showAndWait();
			}
		}
		
		if(error == false){
			File docFile = new File(nSavePath.getAbsolutePath() + "\\" + fileName);
			String docTemp = docTMP.replace("pldocumentpl", documentContent);
			docTemp = docTemp.replace("pltitlepl", gallery.GPSSingleton.getProjTitle());
			//PathUtils.writeToTextFile(docFile, docTemp, "UTF8");
			FileUtils.writeStringToFile(docFile, docTemp);
		}
	
		
		
		
		
		return saved;
	}
}
