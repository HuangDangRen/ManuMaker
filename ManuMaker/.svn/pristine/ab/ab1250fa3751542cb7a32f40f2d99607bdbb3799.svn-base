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

import javafx.util.Duration;

public class TimeUtils {

	public static String durationSecToString(Duration durationSec) {
		String timeString = new String("");
		String days = null;
		String hours = null;
		String minutes = null;
		String seconds = null;
		double remainder = 0;
		if(durationSec != null){
			remainder = durationSec.toSeconds();
		} else {
			remainder = 0;
		}
		
		System.out.println("\n\rAudioPlayerController.durationSecToString durationSec -> " + durationSec);
		System.out.println("\n\rAudioPlayerController.durationSecToString remainder -> " + remainder);
		if(remainder >= 24.0*60*60) {
			days = String.valueOf((int)Math.floor(durationSec.toHours()));
			remainder = remainder - (Math.floor(durationSec.toHours()*60.0*60.0));
			timeString = days;
		} 
		System.out.println("\n\rAudioPlayerController.durationSecToString days -> " + days);
		System.out.println("\n\rAudioPlayerController.durationSecToString timeString -> " + timeString);
		System.out.println("\n\rAudioPlayerController.durationSecToString remainder -> " + remainder);
		
		if(remainder >= (60*60)){
			hours = String.valueOf((int)Math.floor(remainder/(60*60)));
			remainder = remainder - Math.floor(remainder/(60*60));
			if(days == null){
				timeString = hours;
			} else {
				timeString = timeString + ":" + hours;
			}
		} else {
			timeString = "00:";
		}
		
		System.out.println("\n\rAudioPlayerController.durationSecToString hours -> " + hours);
		System.out.println("\n\rAudioPlayerController.durationSecToString timeString -> " + timeString);
		System.out.println("\n\rAudioPlayerController.durationSecToString remainder -> " + remainder);
		
		
		if(remainder >= 60) {
			minutes = String.valueOf((int)Math.floor(remainder/60));
			remainder = remainder - Math.floor(remainder/60);
			if(hours == null){
				timeString = timeString + minutes;
			} else {
				timeString = timeString + ":" + minutes;
			}
		} else {
			timeString = timeString + "00:";
		}
		
		System.out.println("\n\rAudioPlayerController.durationSecToString minutes -> " + minutes);
		System.out.println("\n\rAudioPlayerController.durationSecToString timeString -> " + timeString);
		System.out.println("\n\rAudioPlayerController.durationSecToString remainder -> " + remainder);
		
		if(remainder >= 1) {
			seconds = String.valueOf((int)Math.floor(remainder));
			if(minutes == null){
				timeString = timeString + seconds;
			} else {
				timeString = timeString + ":" + seconds;
			}
		} else {
			timeString = timeString + "00";
		}
		System.out.println("\n\rAudioPlayerController.durationSecToString minutes -> " + seconds);
		System.out.println("\n\rAudioPlayerController.durationSecToString timeString -> " + timeString);
		System.out.println("\n\rAudioPlayerController.durationSecToString remainder -> " + remainder);
		
		return timeString;
	}
}
