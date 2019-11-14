//
//  RIFFFile.java
//  bwav
//
//  Created by Robert La Ferla on 1/18/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

import java.io.*;
import java.net.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.chunks.*;

/**
 * A RIFF file is a file in the Resource Interchange File Format that contains data stored in tagged chunks.  It is a little-endian format.
 *
 * @author Robert La Ferla
 */

public class RIFFFile extends IFFFile {
	protected RIFFRandomAccessFile rraf = null;
	private File theFile = null;
	
	/**
	 * Returns a new RIFFFile instance with a single RIFF chunk.
	 */
	public RIFFFile() {
		RIFFChunk rc;
		
		rc = new RIFFChunk();
		addChunk(rc);
	}

	/**
	 * Returns a new RIFFFile instance from the specified File using the specified mode.
	 *
	 * @param aFile The file.
	 * @param mode The file mode.
	 * @see #open(String)
	 * @see #load()
	 */	
	public RIFFFile(File aFile, String mode) throws FileNotFoundException {
		theFile = aFile;
		open(mode);
		load();
	}

	/**
	 * Returns a new RIFFFile instance from the specified File.
	 *
	 * @param aFile The file.
	 * @throws FileNotFoundException
	 */
	public RIFFFile(File aFile) throws FileNotFoundException {
		this(aFile, "r");
	}

	
	/**
	 * Returns a new RIFFFile instance from the specified path.
	 *
	 * @param pathname The path.
	 * @throws FileNotFoundException
	 */
	public RIFFFile(String pathname) throws FileNotFoundException {
		this(pathname, "r");
	}

	/**
	 * Returns a new RIFFFile instance from the specified path using the specified mode.
	 *
	 * @param pathname The path.
	 * @param mode The file mode.
	 * @throws FileNotFoundException
	 * @see #open(String)
	 * @see #load()
	 */	
	public RIFFFile(String pathname, String mode) throws FileNotFoundException {
		theFile = new File(pathname);
		open(mode);
		load();
	}
	
	/**
	 * Returns a new RIFFFile instance from the specified URI.
	 *
	 * @param uri The URI.
	 * @throws FileNotFoundException
	 * @see #open(String)
	 * @see #load()
	 */
	public RIFFFile(URI uri) throws FileNotFoundException {
		theFile = new File(uri);
		open("r");
		load();
	}

	/**
	 * Returns a new RIFFFile instance from the specified URI and mode.
	 *
	 * @param uri The URI.
	 * @param mode The file mode.
	 * @throws FileNotFoundException
	 * @see #open(String)
	 * @see #load()
	 */
	public RIFFFile(URI uri, String mode) throws FileNotFoundException {
		theFile = new File(uri);
		open(mode);
		load();
	}
	
	protected void finalize() throws Throwable {
		if (rraf != null) {
			try {
				rraf.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets the File instance associated with this RIFFFile.
	 *
	 * @param aFile A File instance.
	 */
	public void setFile(File aFile) {
		theFile = aFile;
	}
	
	/**
	 * Returns the File instance associated with this RIFFFile.
	 */
	public File getFile() {
		return theFile;
	}
	
	/**
	 * Sets the filename of the RIFFFile.
	 *
	 * @param aFileName The file name.
	 */
	public void setFileName(String aFileName) {
		theFile = new File(aFileName);
	}
	
	/**
	 * Returns the file name of the RIFFFile.
	 */
	public String getFileName() {
		return theFile.getName();
	}
	
	/**
	 * Returns the RIFFRandomAccessFile associated with this RIFFFile.
	 */
	public RIFFRandomAccessFile getRandomAccessFile() {
		return rraf;
	}

	/**
	 * Opens the RIFFFile using the specified mode.
	 *
	 * @param mode The mode.
	 * @throws FileNotFoundException
	 * @see RandomAccessFile#RandomAccessFile(File, String) RandomAccessFile documentation for list of available modes.
	 */
	public void open(String mode) throws FileNotFoundException {
		if (rraf == null) {
			rraf = new RIFFRandomAccessFile(theFile, mode);
		}
	}
	
	/**
	 * Opens the RIFFFile using the "rw" read/write mode.
	 * @throws FileNotFoundException
	 */
	public void open() throws FileNotFoundException {
		open("rw");
	}

	/**
	 * Closes the RIFFFile.
	 *
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (rraf != null) {
			rraf.close();
			rraf = null;
		}
	}
	
	/*
	 * Returns the chunk in this RIFFFile specified by the ID or null if it is not present.
	 *
	 * @param aChunkID The ID of the chunk
	 */
	public Chunk getChunk(int aChunkID) {
		Chunk theChunk = null;
		Iterator it;
		
		it = chunks.iterator();
		while (it.hasNext()) {
			Chunk c;
			
			c = (Chunk)it.next();
			if (c.getChunkID() == aChunkID) {
				theChunk = c;
				break;
			}
		}
		
		return theChunk;
	}
	
	/**
	 * Returns the chunk in this RIFFFile named by the specified string or null if it is not present.
	 *
	 * @param aString The chunk name.
	 */
	public Chunk getChunkNamed(String aString) {
		Chunk theChunk = null;
		Iterator it;
		
		it = chunks.iterator();
		while (it.hasNext()) {
			Chunk c;
			
			c = (Chunk)it.next();
			if (c.getChunkName().equals(aString)) {
				theChunk = c;
				break;
			}
		}
		
		return theChunk;
	}
	
	/**
	 * Returns all the chunks in this RIFFFile.
	 */
	public ArrayList getChunks() {
		return chunks;
	}
	
	/**
	 * Copy all chunks (except the RIFF chunk) from the specified RIFFFile to this RIFFFile.
	 *
	 * @param aRIFFFile The RIFFFile to copy the chunks from.
	 */
	public void copyChunksFrom(RIFFFile aRIFFFile) {
		Iterator cit;
		
		cit = aRIFFFile.getChunks().iterator();
		while (cit.hasNext()) {
			Chunk ch;
			
			ch = (Chunk)cit.next();
			switch (ch.getChunkID()) {
				case RIFFChunk.ID:
					// Don't copy RIFF chunk!
					break;
				default:  // Copy additional chunks over verbatim
					addChunk((Chunk)ch.clone());
					break;
			}					
		}
	}

	
	/**
	 * Calculates the position of the specified chunk in the file using only those chunks specified.
	 * @param aChunk The chunk that you want the position of.
	 * @param anArray An array of all the chunks in the file.
	 */
	
	public void calculatePositionInFileForChunk(Chunk aChunk, ArrayList anArray) {
		long positionInFile = 0;
		int aChunkID;
		int idx = 0;
		
		// System.out.println("RIFFFile.getPositionInFileForChunk(" + aChunk.getChunkName() + ")");
		aChunkID = aChunk.getChunkID();
		//System.out.println("aChunk == " + aChunk.stringForID());
		
		idx = anArray.indexOf(aChunk);
		if (idx == 0) {
			positionInFile = 0; // RIFF
			//System.out.println("positionInFile == " + positionInFile);
		}
		else if (idx == 1) {
			positionInFile = 12; // 2nd chunk
			//System.out.println("positionInFile == " + positionInFile);
		}
		else if (idx >= 2) {
			Chunk previousChunk;
			//System.out.println("idx == " + idx);
			positionInFile = 12;
			for (int i = 2; i <= idx; i++) {
				previousChunk = (Chunk)anArray.get(i - 1);
				//System.out.println("previousChunk == " + previousChunk.stringForID());

				positionInFile += (8 + previousChunk.getChunkSize());
				//System.out.println("previousChunkSize == " + previousChunk.getChunkSize());
				if (previousChunk.getChunkSize() % 2 != 0) {
					positionInFile++;
				}
				//System.out.println("positionInFile == " + positionInFile);
			}
		}
		aChunk.setPositionInFile(positionInFile);
		//System.out.println("Setting: " + aChunk.stringForID() + " to position: " + positionInFile);
	}
	
	public void saveChunks(ArrayList anArray) {
		//System.out.println("saveChunks()");

		try {
			Iterator it;
			
			it = anArray.iterator();
			while (it.hasNext()) {
				Chunk ch;
				String key;
				
				ch = (Chunk)it.next();
				key = ch.getChunkName();
				calculatePositionInFileForChunk(ch, anArray);
				ch.writeToFile(rraf);
				//System.out.println("Wrote " + key + " chunk of size " + ch.getChunkSize() +" to position " + ch.getPositionInFile() + " in file.");
			}
			riffChunk().writeActualSizeToFile(rraf);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}		
		
	}
	
	/**
	 * Saves the RIFFFile.
	 */
	public void save() {
		//System.out.println("save()");
		//System.out.println("Saving " + getFileName());

		saveChunks(chunks);
		try
		{
			rraf.setLength(riffChunk().getChunkSize() + 8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Load the RIFFFile.  Unsupported chunks are added as GenericChunk instances.
	 *
	 * @see GenericChunk
	 */
	public void load() {
		try {
			Chunk ch;
			int chunkID = -1;
			long chunkSize = 0;
			
			//System.out.println("load()");
			//System.out.println("Loading " + getFileName());
			do {
				chunkID = rraf.readID();
                //System.out.println("raw chunkid = " + chunkID);
				if (chunkID != -1) {					
					 //System.out.print("chunkID = 0x" + Long.toHexString(chunkID) + " is " + RIFFFile.stringForID(chunkID) + " -- ");
					chunkSize = rraf.readUNUM32();
					 //System.out.print("chunkSize = " + chunkSize);
					switch (chunkID) {
						case RIFFChunk.ID:							
							ch = new RIFFChunk();
							break;
						case FormatChunk.ID:							
							ch = new FormatChunk();
							break;
						case BExtChunk.ID:
							ch = new BExtChunk();							
							break;
						case DataChunk.ID:							
							ch = new DataChunk();
							break;
						/*
						 * Unsupported at this time
						 *
						 case CueChunk.ID:							
							ch = new CueChunk();
							break;
						 */
						default:
							ch = new GenericChunk();
							break;
					}

					ch.setChunkID(chunkID);
					ch.setChunkSize(chunkSize);
					addChunk(ch);
					calculatePositionInFileForChunk(ch, chunks);
                    //System.out.println(" position = " + ch.getPositionInFile());
					ch.readFromFile(rraf);
					
					// Skip pad byte if present
					if (chunkID != RIFFChunk.ID && ((chunkSize % 2) != 0)) {
						rraf.skipBytes(1);
					}
					//ch.dump();

					//System.out.println();
				}
			} while (chunkID != -1);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns the string representation of the specified number.
	 *
	 * @param num The number.
	 */
	public static String stringForID(long num) {
		String str;
		char b[];
		
		/*
		 System.out.print("INT=" + num);
		 System.out.print(" HEX=" + Integer.toHexString(num));
		 */
		
		b = new char[4];
		
		b[0] = (char)((num >> 24) & 0x7f);
		b[1] = (char)((num >> 16) & 0x7f);
		b[2] = (char)((num >> 8) & 0x7f);
		b[3] = (char)(num & 0x7f);
		
		/*
		 System.out.print(" ASCII=");
		 System.out.print(b[0]);
		 System.out.print(b[1]);
		 System.out.print(b[2]);
		 System.out.print(b[3]);
		 System.out.println("");
		 */
		
		str = new String(b);
		return str;
	}
	
	/**
	 * Returns the RIFF chunk.
	 */
	public RIFFChunk riffChunk() {
		return (RIFFChunk)getChunkNamed("RIFF");
	}
	
}
