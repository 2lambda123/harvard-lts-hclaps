//
//  DMSCoordinates.java
//  hclaps
//
//  Created by Robert La Ferla on 4/28/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

/**
 * @author Robert La Ferla
 */

public class DMS {
	char direction; // ASCII 'N', 'S', 'E', or 'W'
	int degrees = 0;
	int minutes = 0;
	int seconds = 0;
	
	// From decimal degrees and whether it is latitude or longitude
	public DMS(double d, boolean isLatitude) {
		setDecimalDegrees(d, isLatitude);
	}
	
	// From a string e.g. 87d 43' 41" N
	public DMS(String str) {
		String [] parts;
		String s;
		
		parts = str.split(" ");
		s = parts[0].substring(0, parts[0].length() - 1);
		degrees = Integer.parseInt(s);
		s = parts[1].substring(0, parts[0].length() - 1);
		minutes = Integer.parseInt(s);
		s = parts[2].substring(0, parts[0].length() - 1);
		seconds = Integer.parseInt(s);
		direction = parts[3].charAt(0);
	}
	
	public void setDirection(char c) {
		direction = c;
	}
	
	public char getDirection() {
		return direction;
	}
	
	public void setDegrees(int i) {
		degrees = i;
	}
	
	public int getDegrees() {
		return degrees;
	}
	
	public void setMinutes(int i) {
		minutes = i;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public void setSeconds(int i) {
		seconds = i;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public double getDecimalDegrees() {
		double d = (double)0;
		
		//System.out.println("getDecimalDegrees()");
		d = degrees + (minutes + (seconds / 60.0)) / 60.0;
		if (direction == 'S' || direction == 'W') {
			d = 0.0 - d;
		}
		return d;
	}
	
	private double getFractionalPart(double d) {
		double val = (double)0;
		String ostr, nstr;
		
		ostr = Double.toString(d);
		nstr = "0" + ostr.substring(ostr.indexOf("."), ostr.length());
		val = Double.parseDouble(nstr);
		return val;
	}
	
	public void setDecimalDegrees(double d, boolean isLatitude) {
		double deg, min, sec, tmp;
		
		//System.out.println("setDecimalDegrees(" + d + "," + isLatitude +")");
		deg = Math.floor(d);
		tmp = getFractionalPart(d) * 60.0;
		min = Math.floor(tmp);
		sec = getFractionalPart(tmp) * 60.0;

		degrees = (int)deg;
		minutes = (int)min;
		seconds = (int)sec;
		if (isLatitude == true) {
			if (degrees < 0) {
				direction = 'S';
				degrees = 0 - degrees;
			}
			else {
				direction = 'N';
			}
		}
		else {
			if (degrees < 0) {
				direction = 'W';
				degrees = 0 - degrees;
			}
			else {
				direction = 'E';
			}
		}
	}
	
	public String toString() {
		String str = "";
		
		str = degrees + "d " + minutes + "' " + seconds + "\" " + direction; 
		return str;
	}
	
}
