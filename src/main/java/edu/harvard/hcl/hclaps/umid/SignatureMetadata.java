//
//  SignatureMetadata.java
//  hclaps
//
//  Created by Robert La Ferla on 1/23/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

import java.util.*;
import edu.harvard.hcl.hclaps.util.*;

/**
 * @author Robert La Ferla
 */

public class SignatureMetadata {
	byte [] timeDate;
	ISO6709 spatialCoordinates;
	byte [] countryCode;  // ISO 3166
	byte [] organizationCode;
	byte [] user;
	
	public SignatureMetadata() {
		timeDate = new byte[8];
		spatialCoordinates = new ISO6709();
		countryCode = new byte[4];
		organizationCode = new byte[4];
		user = new byte[4];
		setTimeDate(new java.util.Date());
	}
	
	public SignatureMetadata(byte[] signature_metadata) throws InvalidArgumentException {
		if (signature_metadata.length != 32) {
			throw new InvalidArgumentException("Signature metadata length needs to be 32 bytes long.");
		}
		else {
			byte [] coords;
			
			timeDate = new byte[8];
			coords = new byte[12];
			countryCode = new byte[4];
			organizationCode = new byte[4];
			user = new byte[4];
			for (int i = 0; i < 8; i++) {
				timeDate[i] = signature_metadata[i];
			}
			for (int i = 0; i < 12; i++) {
				coords[i] = signature_metadata[i + 8];
			}
			spatialCoordinates = new ISO6709(coords);

			for (int i = 0; i < 4; i++) {
				countryCode[i] = signature_metadata[i + 20];
			}
			for (int i = 0; i < 4; i++) {
				organizationCode[i] = signature_metadata[i + 24];
			}
			for (int i = 0; i < 4; i++) {
				user[i] = signature_metadata[i + 28];
			}
		}
	}
	
	public void setTimeDate(Date aDate) {
		GregorianCalendar cal, ncal;
		byte [] array;
		long time = 0L;
		
		//System.out.println("setTimeDate()");

		// Create a calendar for the given date.
		cal = new GregorianCalendar();
		cal.setTime(aDate);
		
		// Get the Modified Julian Date for the calendar and populate those fields.
		array = SMPTE309M.getInstance().getMJDForCalendar(cal);
		timeDate[4] = array[0];
		timeDate[5] = array[1];
		timeDate[6] = array[2];
		timeDate[7] = array[3];
		
		// Calculate the time since midnight using a 48KHz clock
		time = get48KHzCountFor(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
	
		array = ByteConvertor.bytesForUInt(time, ByteConvertor.LITTLE);
		timeDate[0] = array[0];
		timeDate[1] = array[1];
		timeDate[2] = array[2];
		timeDate[3] = array[3];
	}
	
	public long get48KHzCountFor(int hour, int minute, int second, int millisecond) {
		long time = 0L;

		time += (hour * 172800000);
		time += (minute * 2880000);
		time += (second * 48000);
		time += (millisecond * 48);
		return time;
	}
	
	public Date getTimeDate() {
		Date val = null;
		GregorianCalendar cal;
		byte [] array;
		long time;
		long hrs;
		long mins;
		long secs;
		long ms;
		
		// System.out.println("getTimeDate()");
		
		array = new byte[4];
		array[0] = timeDate[4];
		array[1] = timeDate[5];
		array[2] = timeDate[6];
		array[3] = timeDate[7];	
		
		cal = SMPTE309M.getInstance().getCalendarForMJD(array);
		cal.setLenient(false);

		array[0] = timeDate[0];
		array[1] = timeDate[1];
		array[2] = timeDate[2];
		array[3] = timeDate[3];
		
		time = ByteConvertor.uIntForBytes(array, ByteConvertor.LITTLE);
		hrs = time / 172800000;
		time = time % 172800000;
		mins = time / 2880000;
		time = time % 2880000;
		secs = time / 48000;
		time = time % 48000;
		ms = time / 48;
		
		cal.set(Calendar.HOUR_OF_DAY, (int)hrs);
		cal.set(Calendar.MINUTE, (int)mins);
		cal.set(Calendar.SECOND, (int)secs);
		cal.set(Calendar.MILLISECOND, (int)ms);
				
		val = cal.getTime();
		
		return val;
	}
	
	public void setCountryCode(String str) {
		int len;
		len = str.length();
		if (len <= 4)
		{
			//System.out.println("setCountryCode(" + str + ")");
			for (int i = 0; i < len; i++) {
				countryCode[i] = (byte)str.charAt(i);
			}
		}
		else
		{
			System.err.println("Country code must be 4 or less characters in length.");
		}
	}
	
	public String getCountryCode() {
		return new String(countryCode);
	}

	public void setOrganizationCode(String str) {
		int len;
		len = str.length();
		if (len <= 4)
		{
			for (int i = 0; i < len; i++) {
				organizationCode[i] = (byte)str.charAt(i);
			}
		}
		else
		{
			System.err.println("Org code must be 4 or less characters in length.");
		}
	}
	
	public String getOrganizationCode() {
		return new String(organizationCode);
	}

	public void setUser(String str) {
		int len;
		len = str.length();
		if (len <= 4)
		{
			for (int i = 0; i < len; i++) {
				user[i] = (byte)str.charAt(i);
			}
		}
		else
		{
			System.err.println("User code must be 4 or less characters in length.");
		}
	}
	
	public String getUser() {
		return new String(user);
	}
	
	public void setSpatialCoordinates(ISO6709 coords) {
		spatialCoordinates = coords;
	}
	
	public ISO6709 getSpatialCoordinates() {
		return spatialCoordinates;
	}

	public byte[] getAsBytes() {
		byte array[];
		byte[] signature_metadata = new byte[32];
		int j = 0;
		
		for (int i = 0; i < 8; i++) {
			signature_metadata[j++] = timeDate[i];
		}
		
		array = spatialCoordinates.getAsBytes();
		for (int i = 0; i < 12; i++) {
			signature_metadata[j++] = array[i];
		}
		
		for (int i = 0; i < 4; i++) {
			signature_metadata[j++] = countryCode[i];
		}

		for (int i = 0; i < 4; i++) {
			signature_metadata[j++] = organizationCode[i];
		}

		for (int i = 0; i < 4; i++) {
			signature_metadata[j++] = user[i];
		}
		
		return signature_metadata;
	}
	
	public void dump() {		
		System.out.println("*** Signature Metadata ***");
		spatialCoordinates.dump();

		System.out.print("country code = ");
		dumpField(countryCode);
		System.out.println();

		System.out.print("organization code = ");
		dumpField(organizationCode);
		System.out.println();
	
		System.out.print("user = ");
		dumpField(user);
		System.out.println();
		System.out.println("date/time = " + getTimeDate());
	}

	private void dumpField(byte [] array) {
		for (int i = 0; i < array.length; i++) {
			byte ch;
			
			ch = array[i];
			if (ch >= 32 && ch < 127) {
				System.out.print((char)ch);
			}
			else {
				System.out.print("?");
			}
		}
		System.out.print(" (0x");
		for (int i = 0; i < array.length; i++) {
			byte ch;
			
			ch = array[i];
			System.out.print(Integer.toHexString(ch));
		}
		System.out.println(")");
	}
	
}
