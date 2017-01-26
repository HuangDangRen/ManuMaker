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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class defines the attributes and behaviour of a set of tags.
 * 
 * @author Daniel Lachmann
 *
 */
public class TagList {
	protected List<String> tagList = new ArrayList<String>();
	
	/**
	 * Returns a given List<String> as a single String, 
	 * with all items delimited by a "; " (semicolon + whitespace)
	 * @param tags	[List<String>]
	 * @param separator [String]
	 * @return [String]
	 */
	protected static String fillTagsInString(List<String> tags, String separator) { 
		
		String tagString = new String(String.join(separator, tags));

		return tagString;
	}
	
	
	/**
	 * Returns a given String containing substrings delimited by ";" in the 
	 * Form of a List<String>. 
	 * Whitespaces on either side of the ";" are discarded.
	 * @param tagString [String]
	 * @return [List<String>]
	 */
	protected static List<String> getTagsInString(String tagString) {
		
		List<String> localTagList = Arrays.asList(tagString.split("\\s*;\\s*")); 
		return localTagList;
	}
	
	/**
	 * Returns the tags in a single String, delimited with a "; "
	 * @return
	 */
	public String getTagString() {
		return TagList.fillTagsInString(this.tagList, "; ");
	}
	
	/**
	 * Sets the tags given a String delimited by ";". 
	 * Whitespace around the delimiter is discarded.
	 * @param nTags [String]
	 */
	public void setTags(String nTags) {
		try {
			this.tagList = TagList.getTagsInString(nTags);
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	/**
	 * Returns the tags in a List.
	 * 
	 * @return [List<String>]
	 */
	public List<String> getTagList() {
		return this.tagList;
	}
	
	
	public boolean isEmpty(){
		boolean empty = false;
			if(this.getTagString().trim().isEmpty()){
				empty = true;
			}
		
		return empty;
	}
	
	public boolean matched(List<String> keyWords){
		boolean matched = false; 
		
		for(String i : keyWords){	 // For each tag
			String temp = this.getTagString().toLowerCase();
			if(temp.contains(i.toLowerCase())){
				matched = true; 
				System.out.println("\n\rTagList.matched() -> Match confirmed! keyWord: " + i + ", tags in List: " + this.getTagString());
			}		
		}
		
		return matched; 
	}
	
}
