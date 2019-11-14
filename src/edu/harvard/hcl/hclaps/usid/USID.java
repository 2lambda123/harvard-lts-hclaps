//
//  USID.java
//  hclaps
//
//  Created by Robert La Ferla on 4/3/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.usid;

import java.io.*;
import java.util.*;
import org.jdom.input.*;
import org.jdom.*;
import edu.harvard.hcl.hclaps.util.*;

/**
 * A USID is a Unique Source Identifier (EBU R99-1999).
 *
 * @author Robert La Ferla
 */

public class USID {
	byte [] usid = new byte[32];
	byte [] countryCode = new byte[2];
	byte [] organizationCode = new byte[3];
	byte [] serialNumber = new byte[12];
	byte [] originationTime = new byte[6];
	byte [] randomNumber = new byte[9];
	
	public USID() {
	}
	
	public USID(USID aUSID) {
		System.arraycopy(aUSID.usid, 0, usid, 0, aUSID.usid.length);
		System.arraycopy(aUSID.countryCode, 0, countryCode, 0, aUSID.countryCode.length);
		System.arraycopy(aUSID.organizationCode, 0, organizationCode, 0, aUSID.organizationCode.length);
		System.arraycopy(aUSID.serialNumber, 0, serialNumber, 0, aUSID.serialNumber.length);
		System.arraycopy(aUSID.originationTime, 0, originationTime, 0, aUSID.originationTime.length);
		System.arraycopy(aUSID.randomNumber, 0, randomNumber, 0, aUSID.randomNumber.length);
	}
	
	// Generate a new USID based on the user's settings.
	public USID(File aFile) {
		// System.out.println("USID(" + aFile + ")");
		try {
			SAXBuilder sb;
			Document doc;
			Element root;
			Element usid;
			String val;
			
			sb = new SAXBuilder();
			doc = sb.build(aFile);
			root = doc.getRootElement();

			val = root.getChildText("countrycode");
			setCountryCode(val);

			val = root.getChildText("orgcode");
			setOrganizationCode(val);

			val = root.getChildText("serial");
			setSerialNumber(val);	
			setOriginationTimeFromDate(new Date());
			setRandomNumber(generateRandomNumber());
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
		
	public USID(Element el) {
		String val;

		//System.out.println("USID(" + el + ")");

		val = el.getChildText("countrycode");
		setCountryCode(val);
		
		val = el.getChildText("orgcode");
		setOrganizationCode(val);
		
		val = el.getChildText("serial");
		setSerialNumber(val);	
		
		setOriginationTimeFromDate(new Date());
		setRandomNumber(generateRandomNumber());
	}	
	
	public USID(byte[] anId) throws InvalidArgumentException {
		if (anId.length != 32) {
			throw new InvalidArgumentException("USID length needs to be 32 bytes long.");
		}
		else {
			int j = 0;
			
			for (int i = 0; i < 2; i++) {
				countryCode[i] = anId[j++];
			}
			for (int i = 0; i < 3; i++) {
				organizationCode[i] = anId[j++];
			}	
			for (int i = 0; i < 12; i++) {
				serialNumber[i] = anId[j++];
			}
			for (int i = 0; i < 6; i++) {
				originationTime[i] = anId[j++];
			}
			for (int i = 0; i < 9; i++) {
				randomNumber[i] = anId[j++];
			}
		}
	}
	
	public USID(String str) throws InvalidArgumentException {
		this(str.getBytes());
	}
	
	public String toString() {
		String str;
		
		str = new String(getValue());
		return str.toUpperCase();
	}
	
	public byte[] getValue() {
		int j = 0;
		
		for (int i = 0; i < 2; i++) {
			usid[j++] = countryCode[i];
		}
		for (int i = 0; i < 3; i++) {
			usid[j++] = organizationCode[i];
		}	
		for (int i = 0; i < 12; i++) {
			usid[j++] = serialNumber[i];
		}
		for (int i = 0; i < 6; i++) {
			usid[j++] = originationTime[i];
		}
		for (int i = 0; i < 9; i++) {
			usid[j++] = randomNumber[i];
		}
		return usid;
	}
	
	public void setCountryCode(String aStr) {
		countryCode[0] = (byte)aStr.charAt(0);
		countryCode[1] = (byte)aStr.charAt(1);
	}
	
	public String getCountryCode() {
		return new String(countryCode);
	}

	public void setOrganizationCode(String aStr) {
		organizationCode[0] = (byte)aStr.charAt(0);
		organizationCode[1] = (byte)aStr.charAt(1);
		organizationCode[2] = (byte)aStr.charAt(2);
	}
	
	public String getOrganizationCode() {
		return new String(organizationCode);
	}
	
	public void setSerialNumber(String aStr) {
		for (int i = 0; i < 12; i++) {
			serialNumber[i] = (byte)aStr.charAt(i);
		}
	}
	
	public String getSerialNumber() {
		return new String(serialNumber);
	}
	
	public void setOriginationTimeFromDate(Date aDate) {
		GregorianCalendar gc;
		String ot;
		
		gc = new GregorianCalendar();
		gc.setTime(aDate);
		ot = "";
		if (gc.get(Calendar.HOUR_OF_DAY) < 10) {
			ot += "0";
		}
		ot += gc.get(Calendar.HOUR_OF_DAY);
		if (gc.get(Calendar.MINUTE) < 10) {
			ot += "0";
		}
		ot += gc.get(Calendar.MINUTE);
		if (gc.get(Calendar.SECOND) < 10) {
			ot += "0";
		}
		ot += gc.get(Calendar.SECOND);
		setOriginationTime(ot);
	}

	public void setOriginationTime(String aStr) {
		for (int i = 0; i < 6; i++) {
			originationTime[i] = (byte)aStr.charAt(i);
		}
	}
	
	public String getOriginationTime() {
		return new String(originationTime);
	}
	
	public void setRandomNumber(String aStr) {
		for (int i = 0; i < 9; i++) {
			randomNumber[i] = (byte)aStr.charAt(i);
		}
	}
	
	public String getRandomNumber() {
		return new String(randomNumber);
	}
	
	public static String generateRandomNumber() {
		String str;
		Random rnd;
		
		str = "";
		rnd = new Random();
		for (int i = 0; i < 9; i++) {
			str += rnd.nextInt(9);
		}
		return str;
	}
	
	
	public void dump() {
		System.out.println("*** USID ***");
		getValue();
		for (int i = 0; i < 32; i++) {
			System.out.print((char)usid[i]);
		}
		System.out.println();
		System.out.print("Country Code: ");
		dumpField(countryCode);
		System.out.print("OrganizationCode Code: ");
		dumpField(organizationCode);
		System.out.print("Serial Number: ");
		dumpField(serialNumber);
		System.out.print("Origination Time: ");
		dumpField(originationTime);
		System.out.print("Random Number: ");
		dumpField(randomNumber);
		System.out.println("******");
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
