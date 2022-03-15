//
//  ISO6709.java
//  hclaps
//
//  Created by Robert La Ferla on 4/26/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

import edu.harvard.hcl.hclaps.util.*;
import java.text.*;

/**
 * @author Robert La Ferla
 */

public class ISO6709 {
	DMS latitude;
	DMS longitude;
	int altitude = 0;
	
	public ISO6709() {
		latitude = new DMS(0.0, true);
		longitude = new DMS(0.0, false);
	}
	
	public ISO6709(byte [] bytes) {
		byte [] array;
		double d;
		double dir = (double)1;
		
		array = new byte[4];
		
		// Altitude
		for (int i = 0; i < 4; i++) {
			array[i] = bytes[i];
		}
		altitude = ByteConvertor.getIntegerForBCD(array, ByteConvertor.LITTLE);
		
		// Longitude
		for (int i = 0; i < 4; i++) {
			array[i] = bytes[i + 4];
		}
		switch (array[3] & 0x0f) {
			case 0:
			case 1:
				dir = -1.0;
				break;
			case 0x0e:
			case 0x0f:
				dir = (double)1;
				break;
		}
		array[3] = (byte)(array[3] & 0xf0);
		d = ByteConvertor.getDoubleForBCD(array, 5, ByteConvertor.LITTLE);
		longitude = new DMS(d * dir, false);
		
		// Latitude	
		for (int i = 0; i < 4; i++) {
			array[i] = bytes[i + 8];
		}		
		d = ByteConvertor.getDoubleForBCD(array, 5, ByteConvertor.LITTLE);
		latitude = new DMS(d, true);
	}
	
	// decimal degrees format is [+-]NN.NNNNN[+-]NNN.NNNNN[+-]NNNNNNNN[/]
	
	public ISO6709(String str) throws NumberFormatException {
		String field;
		int from;
		int len;
		
		//System.out.println("ISO6709(" + str + ")");
		len = str.length();
		from = 0;
		field = "";
		for (int i = from; i < len; i++) {
			char c;
			
			c = str.charAt(i);
			if (i != from && (c == '+' || c == '-' || c == '/')) {
				from = i;
				break;
			}
			else {
				field = field + c;
			}
		}

		latitude = new DMS(Double.parseDouble(field), true);
		
		field = "";
		for (int i = from; i < len; i++) {
			char c;
			
			c = str.charAt(i);
			if (i != from && (c == '+' || c == '-' || c == '/')) {
				from = i;
				break;
			}
			else {
				field = field + c;
			}
		}
		longitude = new DMS(Double.parseDouble(field), false);
		
		// Parse altitude
		field = "";
		for (int i = from; i < len; i++) {
			char c;
			
			c = str.charAt(i);
			if (i != from && (c == '+' || c == '-' || c == '/')) {
				from = i;
				break;
			}
			else {
				if (c != '+') {
					field = field + c;
				}
			}
		}
		if (!field.equals("/")) {
			int i;
			
			i = Integer.parseInt(field);
			setAltitude(i);
		}
	}
	
	public void setLatitude(DMS aDMS) {
		latitude = aDMS;
	}
	
	public DMS getLatitude() {
		return latitude;
	}

	public void setLongitude(DMS aDMS) {
		longitude = aDMS;
	}
	
	public DMS getLongitude() {
		return longitude;
	}
	
	// max 99,999,999
	public void setAltitude(int i) {
		if (i <= 99999999) {
			altitude = i;
		}
		else {
			System.err.println("Altitude must be <= 99,999,999.");
		}
	}
	
	public int getAltitude() {
		return altitude;
	}
	
	public String toString() {
		String str = "";
		Double lat, lon;
		DecimalFormat df;
		
		df = new DecimalFormat();
		df.setGroupingUsed(false);
		df.setMinimumFractionDigits(4);
		df.setMaximumFractionDigits(4);
		df.setMinimumIntegerDigits(2);
		df.setMaximumIntegerDigits(3);
		
		if (latitude.getDecimalDegrees() >= 0.0) {
			str += "+";
		}

		str += df.format(latitude.getDecimalDegrees());

		lon = new Double(longitude.getDecimalDegrees());
		if (longitude.getDecimalDegrees() >= 0.0) {
			str += "+";
		}
		str += df.format(longitude.getDecimalDegrees());

		if (altitude >= 0) {
			str += "+";
		}
		str += altitude;
		str += "/";
		return str;
	}
	
	public byte[] getAsBytes() {
		byte b[];
		byte array[];
		
		b = new byte[12];
		
		// Convert altitude
		array = ByteConvertor.getBCDForInteger(getAltitude(), ByteConvertor.LITTLE);
		for (int i = 0; i < array.length; i++) {
			b[i] = array[i];
		}
		
		// Convert longitude
		array = ByteConvertor.getBCDForDouble(Math.abs(longitude.getDecimalDegrees()), 8, 5, ByteConvertor.LITTLE);
		for (int i = 0; i < array.length; i++) {
			b[i + 4] = array[i];
		}
		if (longitude.getDirection() == 'E') {
			b[7] = (byte)(array[3] | 0x0e);
		}

		
		// Convert latitude
		array = ByteConvertor.getBCDForDouble(Math.abs(latitude.getDecimalDegrees()), 7, 5, ByteConvertor.LITTLE);
		for (int i = 0; i < array.length; i++) {
			b[i + 8] = array[i];
		}
		
		if (latitude.getDirection() == 'S') {			
			b[11] = (byte)(array[3] | 0xf0);
		}
		
		//System.out.println("b=" + ByteConvertor.hexForBytes(b));
		return b;
	}
	
	public void dump() {
		System.out.println("latitude=" + latitude);
		System.out.println("longitude=" + longitude);
		System.out.println("altitude=" + altitude);
		System.out.println(this);
	}
	
}
