//
//  RIFFRandomAccessFile.java
//  bwav
//
//  Created by Robert La Ferla on 2/1/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import edu.harvard.hcl.hclaps.util.ByteConvertor;

/**
 * @author Robert La Ferla
 */

public class RIFFRandomAccessFile extends java.io.RandomAccessFile {

	/**
	 * Returns a new RIFFRandomAccessFile instance from the given path using the given mode.
	 *
	 * @param path A path.
	 * @param mode The mode.
	 * @throws FileNotFoundException
	 * @see RandomAccessFile#RandomAccessFile(File, String) RandomAccessFile documentation for list of available modes.
	 */
	public RIFFRandomAccessFile(String path, String mode) throws FileNotFoundException {
		super(path, mode);
	}
	
	/**
	 * Returns a new RIFFRandomAccessFile instance from the given File instance using the given mode.
	 *
	 * @param file An instance of File.
	 * @param mode The mode.
	 * @throws FileNotFoundException
	 * @see RandomAccessFile#RandomAccessFile(File, String) RandomAccessFile documentation for list of available modes.
	 */	
	public RIFFRandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}
	
	/*
	 * READ
	 */
	
	public final int readCHR() throws IOException {
        return read() & 0x7f;
	}
	
	// Reads a null-terminated string and throws away any extra bytes until a total of "len" bytes have been read.
	public final String readCHR(int len) throws IOException {
		String str = "";
        byte ba[];
		int bytesRead = 0;
		int i = 0;
		byte b;
		
		ba = new byte[len];
		do {
			b = (byte)(read() & 0x7f);
			bytesRead++;
			if (b != 0) {
				ba[i] = b;
				i++;
			}
		} while (b != 0 && i < len);
		
		if (i > 0) {
			byte bb[];
			
			// Trim null bytes off end of string
			bb = new byte[i];
			for (int j = 0; j < i; j++) {
				bb[j] = ba[j];
			}			
		    str = new String(bb, "US-ASCII");
		}
		skipBytes(len - bytesRead);
		return str;
	}
	
	// Reads a null-terminated string that is a maximum of "max" characters (including null)
	public final String readString(int max) throws IOException {
		String str = "";
		byte ba[];
		int i = 0;
		byte b;
		
		// System.out.println("readString(" + max +")");

		ba = new byte[max];
		do {
			b = (byte)(read() & 0x7f);
			if (b != 0) {
				ba[i] = b;
				i++;
			}
		} while (b != 0 && i < max);
		
		if (i > 0) {
			str = new String(ba, 0, i, "US-ASCII");
		}
		return str;
	}
	
	public final int readNUM8() throws IOException {
        return read();
	}
	
	public final int readUNUM16() throws IOException {
		int result;
		byte buffer[];
		
		buffer = new byte[2];
		
		result = read(buffer, 0, 2);
		if (result != -1) {
			result = ByteConvertor.shortForBytes(buffer, ByteConvertor.LITTLE);
		}
		return result;
	}
	
	public final short readNUM16() throws IOException {
		return (short)readUNUM16();
	}
	
	public final long readUNUM32() throws IOException {
		long result;
		byte buffer[];
		
		buffer = new byte[4];
		
        result = read(buffer, 0, 4);
		if (result != -1) {
			result = ByteConvertor.uIntForBytes(buffer, ByteConvertor.LITTLE);
		}
		return result;
	}
	
	public final int readNUM32() throws IOException {
		return (int)readUNUM32();
	}

	public final long readUNUM64() throws IOException {
		long result;
		byte buffer[];
		
		buffer = new byte[8];
		
        result = read(buffer, 0, 8);
		if (result != -1) {
			result = ByteConvertor.longForBytes(buffer, ByteConvertor.LITTLE);
		}
		return result;
	}
	
	public final long readNUM64() throws IOException {
		return (long)readUNUM64();
	}
	
	
	public final int readID() throws IOException {
		int result;
		byte buffer[];
		
		buffer = new byte[4];
		
        result = read(buffer, 0, 4);
		if (result != -1) {
			// IDs are always BIG endian
			result = (int)ByteConvertor.uIntForBytes(buffer, ByteConvertor.BIG);
		}
		return result;
	}
	
	public final String readPstring() throws IOException {
		String str = "";
		int len;
		byte buffer[];
		
		len = read();
		
		buffer = new byte[len];
		
		read(buffer, 0, len);
		str = new String(buffer);
		return str;
	}

	
	/*
	 * WRITE
	 */
	
	public final void writeCHR(byte b) throws IOException {
		write((int)b);
	}
	
	public final void writeCHR(String str) throws IOException {
		byte [] bytes = str.getBytes("US-ASCII");
		write(bytes, 0, bytes.length);
	}
	
	public final void writeCHR(String str, int len) throws IOException {
		byte [] bytes = str.getBytes("US-ASCII");
		write(bytes, 0, bytes.length);
		// pad bytes to length of field
		for (int i = bytes.length; i < len; i++) {
			write(0);
		}
	}
	
	public final void writeNUM8(short num) throws IOException {
		write(num);
	}
	
	public final void writeUNUM16(int num) throws IOException {
		if (num >= 0) {
			int lsb, msb;
			
			msb = (num >> 8) & 0xff;
			lsb = num & 0xff;
			// little endian (little end first)
			write(lsb);
			write(msb);
		}
	}
	
	public final void writeNUM16(short num) throws IOException {
		int lsb, msb;
		
		msb = (num >> 8) & 0xff;
		lsb = num & 0xff;
		// little endian (little end first)
		write(lsb);
		write(msb);
	}
	
	public final void writeUNUM32(long num) throws IOException {
		if (num >= 0L && num <= 4294967295L) {
			byte [] b;
			
			b = ByteConvertor.bytesForUInt(num, ByteConvertor.LITTLE);
			write(b);
		}
	}
	
	public final void writeNUM32(int num) throws IOException {
		byte [] b;
		
		b = ByteConvertor.bytesForLong(num, ByteConvertor.LITTLE);
		write(b);
	}
	
	public final void writeID(int num) throws IOException {
		byte [] b;
		
		//System.out.println("writeID(" + num +")");
		// IDs are always BIG endian
		b = ByteConvertor.bytesForInt(num, ByteConvertor.BIG);
		write(b);
	}
	
	public final void writePstring(String str) throws IOException {
		int len;
		
		len = str.length();
		// Length should be > 0 and < 256
		if (len > 0 && len < 256) {
			write(len);
			write(str.getBytes(), 0, len);
		}
	}
	
}
