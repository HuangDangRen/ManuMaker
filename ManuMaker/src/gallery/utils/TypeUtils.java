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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class TypeUtils {

	public static <T> List<T> getKeyList(Set<T> keySet){
		return new ArrayList<T>(keySet);		
	}
	
	public static String newlinesToNbsp(String base){
		String retHtml = null;
		retHtml = base.replaceAll("[\r\n]", "&nbsp;");
		retHtml = retHtml.replaceAll("[\n]", "&nbsp;");
		retHtml = retHtml.trim();
		
		if(retHtml.trim().endsWith("<br>")){
			retHtml = retHtml.trim().substring(0, retHtml.length() - 4);
		}
//		System.out.println(	"\n\rTagUtils.newLinesToHtml -> \n"
//						+ 	"Old Text: " + base +
//							"\nNew Text: " + retHtml);
		return retHtml;
	}
	
	public static String removeNewlines(String base){
		String ret = "";
		if(base != null && base.isEmpty() == false){
			ret = base.replace("\n", " ");
		}
		
		return ret;
	}
	
	public static String newlinesToHtml(String base){
		String retHtml = null;
		retHtml = base.replaceAll("[\r\n]", "<br>");
		retHtml = retHtml.replaceAll("[\n]", "<br>");
		retHtml = retHtml.trim();
		
		if(retHtml.trim().endsWith("<br>")){
			retHtml = retHtml.trim().substring(0, retHtml.length() - 4);
		}
//		System.out.println(	"\n\rTagUtils.newLinesToHtml -> \n"
//						+ 	"Old Text: " + base +
//							"\nNew Text: " + retHtml);
		return retHtml;
	}
	
	public static String makeListToString(List<String> nText){
		String ret = "";
		
		for(String i:nText){
			ret = ret.concat(i + "\n");
		}
		
		return ret;
	}
	
}
