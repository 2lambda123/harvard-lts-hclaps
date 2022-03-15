//
//  BExtChunk.java
//  bwav
//
//  Created by Robert La Ferla on 1/18/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav.chunks;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;
import edu.harvard.hcl.hclaps.umid.*;
import edu.harvard.hcl.hclaps.usid.*;
import edu.harvard.hcl.hclaps.util.*;
import org.jdom.input.*;
import org.jdom.*;

/**
 * BExtChunk contains Broadcast Extension (BEXT) metadata used in professional audio.
 *
 * @author Robert La Ferla
 */

public class BExtChunk extends Chunk {
	public final static int ID = 0x62657874; // "bext"
	private final static int SIZE_OF_FIXED_CHUNK = 602;
	String description;
	String originator;
	USID originatorReference;
	Date originationDateTime;
	long timeReferenceLow;
	long timeReferenceHigh;
	int version;
	UMID um;
	byte reserved[] = new byte[190];
	CodingHistory codingHistory;

	/**
	 * Returns a new BExtChunk instance.
	 */
	public BExtChunk() {
		// System.out.println("BExtChunk()");

		chunkID = ID;
		description = "";
		originator = "";
		codingHistory = new CodingHistory();
		originationDateTime = new Date();
		originatorReference = new USID();
		originatorReference.setOriginationTimeFromDate(originationDateTime);
		version = 1;
	}

	/**
	 * Returns a new BExtChunk instance that has the same values as the specified BExtChunk.
	 *
	 * @param aBExtChunk The BExtChunk to copy.
	 */
	public BExtChunk(BExtChunk aBExtChunk) {
		// System.out.println("BExtChunk(" + aBExtChunk + ")");
		chunkID = ID;
		description = aBExtChunk.description;
		originator = aBExtChunk.originator;
		originationDateTime = aBExtChunk.originationDateTime;
		originatorReference = new USID(aBExtChunk.originatorReference);
		timeReferenceLow = aBExtChunk.timeReferenceLow;
		timeReferenceHigh = aBExtChunk.timeReferenceHigh;
		version = aBExtChunk.version;
		um = new UMID(aBExtChunk.um);
		for (int i = 0; i < reserved.length; i++) {
			reserved[i] = aBExtChunk.reserved[i];
		}
		codingHistory = new CodingHistory(aBExtChunk.codingHistory);
	}
	
	/**
	 * Returns a new BExtChunk from the contents of a XML file.
	 *
	 * @param aFile The XML file.
	 */
	public BExtChunk(File aFile) {
		// System.out.println("BExtChunk(" + aFile + ")");

		chunkID = ID;
		description = "";
		originator = "";
		codingHistory = new CodingHistory();
		originationDateTime = new Date();
		version = 0;

		try {
			SAXBuilder sb;
			Document doc;
			Element root;
			Element usidElement;
			Element umidElement;

			sb = new SAXBuilder();
			doc = sb.build(aFile);
			root = doc.getRootElement();
			
			umidElement = root.getChild("umid");
			usidElement = root.getChild("usid");
			originator = root.getChildText("originator");
				
			originatorReference = new USID(usidElement);
			originatorReference.setOriginationTimeFromDate(originationDateTime);
			
			um = new UMID(umidElement);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Sets default values for the BExtChunk instance from a XML file.
	 *
	 * @param aFile An XML file.
	 * @throws Exception
	 */
	public void readAndSetDefaultsFromFile(File aFile) throws Exception{
		// System.out.println("readAndSetDefaultsFromFile(" + aFile + ")");

		SAXBuilder sb;
		Document doc;
		Element root;
		Element usidElement;
		Element umidElement;
		
		sb = new SAXBuilder();
		doc = sb.build(aFile);
		root = doc.getRootElement();
		
		umidElement = root.getChild("umid");
		usidElement = root.getChild("usid");
		originator = root.getChildText("originator");
		
		originatorReference = new USID(usidElement);
		um = new UMID(umidElement);
	}
	
	/**
	 * Returns the chunk ID.
	 */
	public int getChunkID() {
		return ID;
	}
	
	/**
	 * Returns the size of the chunk.
	 */
//	public long getChunkSize() {
//		chunkSize = SIZE_OF_FIXED_CHUNK;
//		chunkSize += (codingHistory.length() + 1);
//		return chunkSize;
//	}
	
	/**
	 * Returns the BEXT description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the BEXT description to the specified string.
	 *
	 * @param aString The description string.
	 */
	public void setDescription(String aString) {
		description = aString;
	}
	
	/**
	 * Returns the BEXT originator.
	 * 
	 */
	public String getOriginator() {
		return originator;
	}
	
	/**
	 * Sets the BEXT originator to the specified string.
	 *
	 * @param aString The originator string.
	 */
	public void setOriginator(String aString) {
		originator = aString;
	}

	/**
	 * Returns the originator reference (USID).
	 */
	public USID getOriginatorReference() {
		return originatorReference;
	}
	
	/**
	 * Sets the originator reference to the specified USID.
	 *
	 * @param aUSID A USID.
	 * @see USID USID
	 */
	public void setOriginatorReference(USID aUSID) {
		originatorReference = aUSID;
	}

	/**
	 * Returns the originator timestamp.
	 */
	public Date getOriginatorDateTime() {
		return originationDateTime;
	}
	
	/**
	 * Sets the originator timestamp to the specified date.
	 *
	 * @param aDate A Date instance.
	 */
	public void setOriginatorDateTime(Date aDate) {
		originationDateTime = aDate;
	}
	
	/**
	 * Returns the time reference or sample count from midnight to the first sample.
	 *
	 * @return A sample count from midnight to the first sample of audio.
	 */
	public long getTimeReference() {
		return (timeReferenceHigh << 32) + timeReferenceLow;
	}
	
	/**
	 * Sets the time reference.  For larger values than a long, use setTimeReferenceHigh and setTimeReferenceLow.
	 *
	 * @param aLong The time reference.
	 * @see BExtChunk#setTimeReferenceHigh(long) setTimeReferenceHigh()
	 * @see BExtChunk#setTimeReferenceLow(long) setTimeReferenceLow()
	 */
	public void setTimeReference(long aLong) {
		timeReferenceLow = aLong & 0xffffffffL;
		timeReferenceHigh = (aLong >>> 32) & 0xffffffffL;
	}
	
	/**
	 * Returns the lower bits of the time reference.
	 */
	public long getTimeReferenceLow() {
		return timeReferenceLow;
	}
	
	/**
	 * Sets the lower bits of the time reference to the specified value.
	 *
	 * @param aLong The lower bits.
	 */
	public void setTimeReferenceLow(long aLong) {
		timeReferenceLow = aLong;
	}

	/**
	 * Returns the higher bits of the time reference.
	 */	
	public long getTimeReferenceHigh() {
		return timeReferenceHigh;
	}
	
	/**
	 * Sets the higher bits of the time reference to the specified value.
	 *
	 * @param aLong The higher bits.
	 */	
	public void setTimeReferenceHigh(long aLong) {
		timeReferenceHigh = aLong;
	}

	/**
	 * Returns the version.
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * Sets the version to the specified value.
	 *
	 * @param anInt The value.
	 */
	public void setVersion(int anInt) {
		version = anInt;
	}
	
	/**
	 * Returns the UMID.
	 */
	public UMID getUMID() {
		return um;
	}
	
	/**
	 * Sets the UMID.
	 *
	 * @param aUMID The UMID.
	 *
	 * @see UMID UMID
	 */
	public void setUMID(UMID aUMID) {
		um = aUMID;
	}
	
	/**
	 * Returns the coding history.
	 */
	public CodingHistory getCodingHistory() {
		return codingHistory;
	}
	
	/**
	 * Sets the coding history to the specified CodingHistory instance.
	 *
	 * @param ch A CodingHistory instance.
	 */
	public void setCodingHistory(CodingHistory ch) {
		codingHistory = ch;
	}
	
	private Date dateForStrings(String od, String ot) {
		Date theDate = null;
		
		//System.out.println("dateForStrings(" + od + "," + ot + ")");

		if (od.length() == 10 && ot.length() == 8) {
			GregorianCalendar gc;

			// Convert to a Java Date
			try {
				int year, month, day;
				int hour, minute, second;
				
				year = Integer.parseInt(od.substring(0,4));
				month = Integer.parseInt(od.substring(5,7));
				day = Integer.parseInt(od.substring(8,10));
				
				hour = Integer.parseInt(ot.substring(0,2));
				minute = Integer.parseInt(ot.substring(3,5));
				second = Integer.parseInt(ot.substring(6,8));
				
				gc = new GregorianCalendar(year, month - 1, day, hour, minute, second);
				theDate = gc.getTime();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			theDate = null;
		}
		//System.out.println("theDate = " + theDate);
		return theDate;
	}
	
	/**
	 * Reads the chunk from the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 */
	public long readFromFile(RIFFRandomAccessFile file) throws IOException {
		long bytesRead = 0;
		byte umid[] = new byte[64];
		byte usid[] = new byte[32];
		String od;
		String ot;
		String str;
		int codingHistorySize;
		
		super.readFromFile(file);
		
		// System.out.println(getClass() + ".readFromFile(" + file + ")");

		// Description
		description = file.readCHR(256);
		bytesRead += 256;
		//System.out.println("description = " + description);
		
		// Originator
		originator = file.readCHR(32);
		bytesRead += 32;
		//System.out.println("originator = " + originator);

		// Originator Reference (USID)
		for (int i = 0; i < 32; i++) {
			usid[i] = (byte)(file.read() & 0x7f);
		}
		bytesRead += usid.length;
		try {
			originatorReference = new USID(usid);
		}
		catch (InvalidArgumentException ex) {
			ex.printStackTrace();
		}
		
		// Origination Date and Time
		od = file.readCHR(10);
		bytesRead += 10;
		ot = file.readCHR(8);
		bytesRead += 8;
		originationDateTime = dateForStrings(od, ot);
		
		// Time Reference Low and High
		timeReferenceLow = file.readUNUM32();
		bytesRead += 4;
		timeReferenceHigh = file.readUNUM32();
		bytesRead += 4;
		
		// Version
		version = file.readUNUM16();
		bytesRead += 2;
		
		// UMID
		for (int i = 0; i < 64; i++) {
			umid[i] = (byte)file.read();
		}
		bytesRead += umid.length;
		try {
			um = new UMID(umid);
		}
		catch (InvalidArgumentException ex) {
			ex.printStackTrace();
		}
		
		// Reserved
		for (int i = 0; i < 190; i++) {
			reserved[i] = (byte)file.read();
		}
		bytesRead += reserved.length;
		
		//System.out.println("bytesRead = " + bytesRead);
		//System.out.println("chunkSize = " + chunkSize);
		//System.out.println("SIZE_OF_FIXED_CHUNK = " + SIZE_OF_FIXED_CHUNK);
		//System.out.println("chunkSize - SIZE_OF_FIXED_CHUNK = " + ((int)chunkSize - SIZE_OF_FIXED_CHUNK));
		codingHistory = new CodingHistory();
		codingHistorySize = (int)chunkSize - SIZE_OF_FIXED_CHUNK;
		if (codingHistorySize > 0) {
			str = file.readCHR(codingHistorySize);
			bytesRead += codingHistorySize;
			
			if (str != null && str.length() > 2) {
				String crlf;
				
				crlf = str.substring(str.length() - 1, str.length());
				if (!crlf.equals("\r\n")) {
					System.err.println("WARNING: The coding history in the bext chunk of the input file must end in CR/LF.");
				}
				codingHistory.addCodingEventsFromString(str);
			}
		}
		
		//System.out.println("bytesRead = " + bytesRead);
		
		return bytesRead;
	}

	/**
	 * Writes the chunk to the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 */
	public void writeToFile(RIFFRandomAccessFile file) throws IOException {
		int bytesWritten = 0;
		byte umid[];
		byte usid[];
		String od;
		String ot;
		String str;
		GregorianCalendar gc;
		
		//System.out.println(getClass() + ".writeToFile(" + file + ")");
		//System.out.println("getChunkSize() = " + getChunkSize());
		file.writeID(getChunkID());
		bytesWritten += 4;
		file.writeUNUM32(getChunkSize());
		bytesWritten += 4;
		
		// Description
		file.writeCHR(description);
		bytesWritten += description.length();
		// Pad remainder of field with null bytes
		for (int i = description.length(); i < 256; i++) {
			file.write(0);
			bytesWritten += 1;
		}

		// Originator
		file.writeCHR(originator);
		bytesWritten += originator.length();
		// Pad remainder of field with null bytes
		for (int i = originator.length(); i < 32; i++) {
			file.write(0);
			bytesWritten += 1;
		}
		
		// Originator Reference (USID)
		if (originatorReference != null) {
			usid = originatorReference.getValue();
		}
		else {
			// Create an empty zero byte filled USID
			usid = new byte[32];
			for (int i = 0; i < usid.length; i++) {
				usid[i] = 0;
			}
		}
		for (int i = 0; i < usid.length; i++) {
			file.write(usid[i]);
		}
		bytesWritten += usid.length;
		// Pad remainder of field with null bytes
		for (int i = usid.length; i < 32; i++) {
			file.write(0);
			bytesWritten += 1;
		}
		
		// Origination Date and Time
		if (originationDateTime != null) {
			gc = new GregorianCalendar();
			gc.setTime(originationDateTime);
			
			// DATE yyyy-mm-dd
			// yyyy-
			od = gc.get(Calendar.YEAR) + "-";
			
			// mm-
			if ((gc.get(Calendar.MONTH) + 1) < 10) {
				od += "0";
			}
			od += (gc.get(Calendar.MONTH) + 1) + "-";
			
			// dd
			if (gc.get(Calendar.DAY_OF_MONTH) < 10) {
				od += "0";
			}
			od += gc.get(Calendar.DAY_OF_MONTH);
			
			// TIME hh:mm:ss
			// hh:
			ot = "";
			if (gc.get(Calendar.HOUR_OF_DAY) < 10) {
				ot += "0";
			}
			ot += gc.get(Calendar.HOUR_OF_DAY) + ":";
			// mm-
			if (gc.get(Calendar.MINUTE) < 10) {
				ot += "0";
			}
			ot += gc.get(Calendar.MINUTE) + ":";
			// ss
			if (gc.get(Calendar.SECOND) < 10) {
				ot += "0";
			}
			ot += gc.get(Calendar.SECOND);
			file.writeCHR(od, 10);
			bytesWritten += 10;
			file.writeCHR(ot, 8);
			bytesWritten += 8;
		}
		else {
			for (int i = 0; i < 18; i++) {
				file.write(0);
			}
		}
		
		// Time Reference Low and High
		file.writeUNUM32(timeReferenceLow);
		bytesWritten += 4;
		file.writeUNUM32(timeReferenceHigh);
		bytesWritten += 4;
		
		// Version
		file.writeUNUM16(version);
		bytesWritten += 2;

		// UMID
		if (um != null) {
			umid = um.getAsBytes();
		}
		else {
			// Create an empty zero byte filled UMID
			umid = new byte[64];
			for (int i = 0; i < umid.length; i++) {
				umid[i] = 0;
			}
		}
		for (int i = 0; i < umid.length; i++) {
			file.write(umid[i]);
		}
		bytesWritten += umid.length;
		// Pad remainder of field with null bytes
		for (int i = umid.length; i < 64; i++) {
			file.write(0);
			bytesWritten += 1;
		}
		
		// Reserved
		for (int i = 0; i < reserved.length; i++) {
			file.write(reserved[i]);
		}
		bytesWritten += reserved.length;
		// Pad remainder of field with null bytes
		for (int i = reserved.length; i < 190; i++) {
			file.write(0);
			bytesWritten += 1;
		}
		
		// Coding History
		str = codingHistory.toString();
		if (str != null) {
			file.writeCHR(str);
			bytesWritten += codingHistory.length();
			file.write(0); // null-terminate coding history
			bytesWritten += 1;		
		}
		
		if ((bytesWritten % 2) != 0) {
			file.write(0);  // pad byte to make chunk even
			bytesWritten += 1;		
		}
		
		// System.out.println("Finished writing BEXT bytesWritten = " + bytesWritten);
	}
	
	/**
	 * Dumps debugging information to the console.
	 */
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
		System.out.println("description = " + description);
		System.out.println("originator = " + originator);
		System.out.println("originatorReference:");
		System.out.println("----------------------------------------------------------");
		originatorReference.dump();
		System.out.println("----------------------------------------------------------\n");
		System.out.println("originationDateTime = " + originationDateTime);
		System.out.println("timeReferenceLow = " + timeReferenceLow);
		System.out.println("timeReferenceHigh = " + timeReferenceHigh);
		System.out.println("version = " + version);
		System.out.println("\n----------------------------------------------------------");
		um.dump();
		System.out.println("----------------------------------------------------------");
		System.out.println("codingHistory = " + codingHistory);
	}
	
}
