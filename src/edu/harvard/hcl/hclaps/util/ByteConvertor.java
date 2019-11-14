//
//  ByteConvertor.java
//  hclaps
//
//  Created by Robert La Ferla on 5/2/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import java.text.*;

/**
 * ByteConvertor converts primitive types to bytes and vice-versa using the specified endianness.
 *
 * @author Robert La Ferla
 */

public class ByteConvertor {
	public final static short LITTLE = 0;
	public final static short BIG = 1; 
	
	/**
	 * Given a long, this method returns bytes in the specified endianness.
	 */
	
	public static byte [] bytesForLong(long l, short endian) {
		byte [] bytes;
		
		bytes = new byte[8];
		switch (endian) {
			case BIG:
				bytes[0] = (byte)((long)(l >> 56) & 0xff);
				bytes[1] = (byte)((long)(l >> 48) & 0xff);
				bytes[2] = (byte)((long)(l >> 40) & 0xff);
				bytes[3] = (byte)((long)(l >> 32) & 0xff);
				bytes[4] = (byte)((long)(l >> 24) & 0xff);
				bytes[5] = (byte)((long)(l >> 16) & 0xff);
				bytes[6] = (byte)((long)(l >> 8) & 0xff);
				bytes[7] = (byte)(l & 0xff);
				break;
			case LITTLE:
				bytes[7] = (byte)((long)(l >> 56) & 0xff);
				bytes[6] = (byte)((long)(l >> 48) & 0xff);
				bytes[5] = (byte)((long)(l >> 40) & 0xff);
				bytes[4] = (byte)((long)(l >> 32) & 0xff);
				bytes[3] = (byte)((long)(l >> 24) & 0xff);
				bytes[2] = (byte)((long)(l >> 16) & 0xff);
				bytes[1] = (byte)((long)(l >> 8) & 0xff);
				bytes[0] = (byte)(l & 0xff);
				break;
		}
		return bytes;
	}
	
	/**
	 * Given bytes in the specified endianness, returns a long.
	 */
	public static long longForBytes(byte [] bytes, short endian) {
		long l = 0L;
		
		switch (endian) {
			case LITTLE:
				l = (long)(bytes[7] & 0xff) << 56 | (long)(bytes[6] & 0xff) << 48 | (long)(bytes[5] & 0xff) << 40 | (long)(bytes[4] & 0xff) << 32 | (long)(bytes[3] & 0xff) << 24 | (long)(bytes[2] & 0xff) << 16 | (long)(bytes[1] & 0xff) << 8 | (long)(bytes[0] & 0xff);
				break;
			case BIG:
				l = (long)(bytes[0] & 0xff) << 56 | (long)(bytes[1] & 0xff) << 48 | (long)(bytes[2] & 0xff) << 40 | (long)(bytes[3] & 0xff) << 32 | (long)(bytes[4] & 0xff) << 24 | (long)(bytes[5] & 0xff) << 16 | (long)(bytes[6] & 0xff) << 8 | (long)(bytes[7] & 0xff);
				break;
		}
		return l;
	}
	
	/**
	 * Given an int, this method returns bytes in the specified endianness.
	 */
	public static byte [] bytesForInt(int i, short endian) {
		byte [] bytes;
		
		//System.out.println("bytesForInt(" + i + ", " + endian + ")");
		bytes = new byte[4];
		switch (endian) {
			case BIG:
				bytes[0] = (byte)((int)(i >> 24) & 0xff);
				bytes[1] = (byte)((int)(i >> 16) & 0xff);
				bytes[2] = (byte)((int)(i >> 8) & 0xff);
				bytes[3] = (byte)(i & 0xff);
				break;
			case LITTLE:
				bytes[0] = (byte)(i & 0xff);
				bytes[1] = (byte)((int)(i >> 8) & 0xff);
				bytes[2] = (byte)((int)(i >> 16) & 0xff);
				bytes[3] = (byte)((int)(i >> 24) & 0xff);
				break;
		}
		
		return bytes;
	}
	
	/**
	 * Given bytes in the specified endianness, returns an int.
	 */
	public static int intForBytes(byte [] bytes, short endian) {
		int i = 0;
		
		//System.out.println("intForBytes(bytes, " + endian + ")");

		switch (endian) {
			case LITTLE:
				i = (int)(bytes[3] & 0xff) << 24 | (int)(bytes[2] & 0xff) << 16 | (int)(bytes[1] & 0xff) << 8 | (int)(bytes[0] & 0xff);
				break;
			case BIG:
				i = (int)(bytes[0] & 0xff) << 24 | (int)(bytes[1] & 0xff) << 16 | (int)(bytes[2] & 0xff) << 8 | (int)(bytes[3] & 0xff);
				break;
		}
		return i;
	}
	
	/**
	 * Given an unsigned int represented by a long, this method returns bytes in the specified endianness.
	 */
	public static byte [] bytesForUInt(long l, short endian) {
		byte [] bytes;
		
		bytes = new byte[4];
		switch (endian) {
			case BIG:
				bytes[0] = (byte)((long)(l >> 24) & 0xff);
				bytes[1] = (byte)((long)(l >> 16) & 0xff);
				bytes[2] = (byte)((long)(l >> 8) & 0xff);
				bytes[3] = (byte)(l & 0xff);
				break;
			case LITTLE:
				bytes[3] = (byte)((long)(l >> 24) & 0xff);
				bytes[2] = (byte)((long)(l >> 16) & 0xff);
				bytes[1] = (byte)((long)(l >> 8) & 0xff);
				bytes[0] = (byte)(l & 0xff);
				break;
		}
		return bytes;
	}

	/**
	 * Given bytes in the specified endianness, returns an unsigned int represented by a long.
	 */
	public static long uIntForBytes(byte [] bytes, short endian) {
		long l = 0;
		
		switch (endian) {
			case LITTLE:
				l = (long)(bytes[3] & 0xff) << 24 | (int)(bytes[2] & 0xff) << 16 | (int)(bytes[1] & 0xff) << 8 | (int)(bytes[0] & 0xff);
				break;
			case BIG:
				l = (long)(bytes[0] & 0xff) << 24 | (int)(bytes[1] & 0xff) << 16 | (int)(bytes[2] & 0xff) << 8 | (int)(bytes[3] & 0xff);
				break;
		}
		return l;
	}
	
	/**
	 * Given a short, this method returns bytes in the specified endianness.
	 */
	public static byte [] bytesForShort(short s, short endian) {
		byte [] bytes;
		
		bytes = new byte[2];
		switch (endian) {
			case LITTLE:
				bytes[1] = (byte)((short)(s >> 8) & 0xff);
				bytes[0] = (byte)(s & 0xff);
				break;
			case BIG:
				bytes[0] = (byte)((short)(s >> 8) & 0xff);
				bytes[1] = (byte)(s & 0xff);
				break;
		}
		return bytes;
	}
	
	/**
	 * Given bytes in the specified endianness, returns a short.
	 */
	public static short shortForBytes(byte [] bytes, short endian) {
		short s = (short)0;
		
		switch (endian) {
			case LITTLE:
				s = (short)(((bytes[1] & 0xff) << 8 | (bytes[0] & 0xff)) & 0xffff);
				break;
			case BIG:
				s = (short)(((bytes[0] & 0xff) << 8 | (bytes[1] & 0xff)) & 0xffff);
				break;
		}
		return s;
	}

	/**
	 * Given a Binary Coded Decimal (BCD) in the specified endianness, return an int.
	 */
	public static int getIntegerForBCD(byte [] bytes, short endian) {
		int val;
		String nstr = "";
		
		//System.out.println("getIntegerForBCD(" + hexForBytes(bytes)+ ")");
		for (int i = 0; i < bytes.length; i++) {
			int idx;
			int lower, upper;
			
			if (endian == ByteConvertor.LITTLE) {
				idx = bytes.length - i - 1;
				upper = (bytes[idx] & 0xf0) >> 4;
				lower = (int)(bytes[idx] & 0x0f);
				
				nstr += (char)(lower + '0');
				nstr += (char)(upper + '0');
			}
			else {
				idx = i;
				upper = (bytes[idx] & 0xf0) >> 4;
				lower = (int)(bytes[idx] & 0x0f);
				
				nstr += (char)(upper + '0');
				nstr += (char)(lower + '0');
			}
		}
		val = Integer.parseInt(nstr);
		return val;
	}
	
	/**
	 * Given a Binary Coded Decimal (BCD) in the specified endianness, return a double.
	 */
	public static double getDoubleForBCD(byte [] bytes, int numberOfDecimalPlaces, short endian) {
		double d = (double)0;
		String nstr = "";
		int j = 0;
		
		//System.out.println("getDoubleForBCD(" + hexForBytes(bytes)+ ")");

		for (int i = 0; i < bytes.length; i++) {
			int idx;
			int lower, upper;
			
			if (endian == ByteConvertor.LITTLE) {
				idx = bytes.length - i - 1;
				upper = (bytes[idx] & 0xf0) >> 4;
				lower = (int)(bytes[idx] & 0x0f);

				nstr += (char)(lower + '0');
				j++;
				if (j == ((bytes.length * 2) - numberOfDecimalPlaces)) {
					nstr += ".";
				}
				nstr += (char)(upper + '0');
				j++;
				if (j == ((bytes.length * 2) - numberOfDecimalPlaces)) {
					nstr += ".";
				}
			}
			else {
				idx = i;
				upper = (bytes[idx] & 0xf0) >> 4;
				lower = (int)(bytes[idx] & 0x0f);
				
				nstr += (char)(upper + '0');
				j++;
				if (j == numberOfDecimalPlaces) {
					nstr += ".";
				}
				nstr += (char)(lower + '0');
				j++;
				if (j == numberOfDecimalPlaces) {
					nstr += ".";
				}
			}
		}
		//System.out.println("nstr="+nstr);
		d = Double.parseDouble(nstr);
		return d;
	}
	
	/**
	 * Given an int, return a Binary Coded Decimal (BCD) in the specified endianness.
	 */
	public static byte[] getBCDForInteger(int number, short endian) {
		String str;
		byte array[];
		int j = 0;
		
		// SMPTE 330M unfortunately only supports positive values.
		if (number < 0) {
			System.err.println("Altitude cannot be negative.  SMPTE 330M only supports positive values.");
			number = 0;
		}
		str = "" + number;
		
		// Make even if odd.
		if ((str.length() % 2) == 1) {
			str = "0" + str;
		}
		
		array = new byte[str.length() / 2];
		for (int i = 0; i < str.length(); i+=2) {
			char c1, c2;
			int upper, lower;
			
			c1 = str.charAt(i);
			c2 = str.charAt(i + 1);
			
			switch (endian) {
				case LITTLE:
					lower = Character.getNumericValue(c1);
					upper = Character.getNumericValue(c2) << 4;
					array[array.length - j - 1] = (byte)((upper | lower) & 0xff);
					break;
				case BIG:
					upper = Character.getNumericValue(c1) << 4;
					lower = Character.getNumericValue(c2);
					array[j] = (byte)((upper | lower) & 0xff);
					break;
			}
			j++;
		}

		return array;
	}
	
	/**
	 * Given a double, return a Binary Coded Decimal (BCD) in the specified endianness.
	 */
	public static byte[] getBCDForDouble(double number, int numberOfDecimalNumbers, int numberOfDecimalPlaces, short endian) {
		byte array[];
		String str;
		String nstr = "";
		DecimalFormat df;
		int j = 0;
		
		// System.out.println("getBCDForDouble(" + number + ")");

		// Allocate one byte for every two digits.  If there is an odd number of digits, allocate an additional byte.
		array = new byte[(numberOfDecimalNumbers / 2) + (numberOfDecimalNumbers % 2)];

		df = new DecimalFormat();
		df.setGroupingUsed(false);
		df.setMinimumFractionDigits(numberOfDecimalPlaces);
		df.setMaximumFractionDigits(numberOfDecimalPlaces);
		df.setMinimumIntegerDigits(numberOfDecimalNumbers - numberOfDecimalPlaces);
		df.setMaximumIntegerDigits(numberOfDecimalNumbers - numberOfDecimalPlaces);
		str = df.format(number);

		// If there is an odd number of digits, prepend "0" to the string.
		if ((numberOfDecimalNumbers % 2) == 1) {
			nstr = "0";
		}
		
		// We're only concerned with digits.  Remove the decimal point "."
		for (int i = 0; i < str.length(); i++) {
			char c;
			
			c = str.charAt(i);
			if (c != '.') {
				nstr += c;
			}
		}
		//System.out.println(nstr);
		
		for (int i = 0; i < nstr.length(); i+=2) {
			char c1, c2;
			int upper, lower;
			
			c1 = nstr.charAt(i);
			c2 = nstr.charAt(i + 1);
			
			switch (endian) {
				case LITTLE:
					lower = Character.getNumericValue(c1);
					upper = Character.getNumericValue(c2) << 4;
					array[array.length - j - 1] = (byte)((upper | lower) & 0xff);
					break;
				case BIG:
					upper = Character.getNumericValue(c1) << 4;
					lower = Character.getNumericValue(c2);
					array[j] = (byte)((upper | lower) & 0xff);
					break;
			}
			j++;
		}
		
		//System.out.println("array = " + hexForBytes(array));
		return array;
	}
	
	/**
	 * Given a byte, return a short.
	 */
	public static short valForByte(byte b) {
		int i;
		
		if (b < 0) {
			i = 128 + (int)(b & 0x7f);
		}
		else {
			i = (int)b;
		}
		return (short)i;
	}
	
	/**
	 * Return the hexadecimal string for the specified byte.
	 */
	public static String hexForByte(byte b) {
		short s;
		String str = "";
		
		s = valForByte(b);
		if (s < 16) {
			str = "0";
		}
		str += Integer.toHexString(s);
		return str;
	}
	
	/**
	 * Return the hexadecimal string for the specified bytes.
	 */
	public static String hexForBytes(byte [] b) {
		String str = "";
		
		for (int i = 0; i < b.length; i++) {
			str += hexForByte(b[i]); 
		}
		return str;
	}

	/**
	 * Return an array of bytes for the specified hexadecimal string.  The string must be an even length.
	 */
	public static byte [] bytesForHexString(String str) {
		int len;
		byte [] bytes = null;
		
		len = str.length();
		if ((len % 2) == 0) {
			bytes = new byte[len / 2];
			for (int i = 0; i < len; i += 2) {
				byte upper, lower;
				
				upper = (byte)(byteForHexChar(str.charAt(i)) << 4);
				lower = byteForHexChar(str.charAt(i+1));
				bytes[i / 2] = (byte)(upper | lower);
			}
		}
		else {
			System.err.println("Error: The length of the hex string must be even.");
			System.exit(-1);
		}
		return bytes;
	}
	
	/**
	 * Return a byte from a hexadecimal character.
	 */
	public static byte byteForHexChar(char c) {
		byte b = (byte)0;
	
		if (c >= '0' && c <= '9') {
			b = (byte)(c - '0');
		}
		else if (c >= 'a' && c <= 'f') {
			b = (byte)(c - 'a' + 10);
		}
		else if (c >= 'A' && c <= 'F') {
			b = (byte)(c - 'A' + 10);
		}

		return b;
	}
}
