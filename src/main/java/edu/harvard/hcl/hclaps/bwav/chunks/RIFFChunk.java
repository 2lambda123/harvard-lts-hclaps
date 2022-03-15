//
//  RIFFChunk.java
//  bwav
//
//  Created by Robert La Ferla on 1/18/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav.chunks;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;

/**
 * The RIFF chunk is a required chunk for all RIFF files.  It is always the very first chunk in the file.
 * 
 * @author Robert La Ferla
 */

public class RIFFChunk extends Chunk {
	public final static int ID = 0x52494646; // "RIFF"
	int signature;
	
	/**
	 * Returns a new RIFFChunk instance.
	 */
	public RIFFChunk() {
		chunkID = ID;
	}
	
	/**
	 * Returns a copy of the specified RIFFChunk instance.
	 */
	public RIFFChunk(RIFFChunk aRIFFChunk) {
		chunkID = ID;
		signature = aRIFFChunk.signature;
	}
	
	/**
	 * Returns the ID of the chunk.
	 */
	public int getChunkID() {
		return ID;
	}
	
	/**
	 * Returns the size of the RIFF chunk.
	 */
	public long getChunkSize() {
		Iterator it;
		
		//System.out.println("RIFFChunk.getChunkSize()");

		chunkSize = 4;

		// Add up sizes of all other chunks
		it = ((WAVEFile)parentFile).getChunks().iterator();
		while (it.hasNext()) {
			Chunk ch;
			
			ch = (Chunk)it.next();
			if (ch.getChunkID() != RIFFChunk.ID) {
				chunkSize += 8;
				chunkSize += ch.getChunkSize();
				// Account for pad byte if present
				if (ch.getChunkSize() % 2 != 0) {
					chunkSize++;
				}	
			}
		}
		//System.out.println("chunkSize = " + chunkSize);
		return chunkSize;
	}
	
	/**
	 * Sets the signature to specified value.
	 *
	 * @param anInt The signature.
	 */
	public void setSignature(int anInt) {
		signature = anInt;
	}
	
	/**
	 * Returns the signature.
	 */
	public int getSignature() {
		return signature;
	}
	
	/**
	 * Reads the RIFF chunk from the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 * @see RIFFRandomAccessFile
	 */
	public long readFromFile(RIFFRandomAccessFile file) throws IOException {
		long bytesRead = 0;
		
		super.readFromFile(file);
		signature = file.readID();
		if (signature != -1) {
			bytesRead += 4;
			//System.out.println("signature = 0x" + Integer.toHexString(signature));
			//System.out.println(RIFFFile.stringForID(signature));
		}
		
		return bytesRead;
	}
	
	/**
	 * Writes the RIFF chunk to the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 * @see RIFFRandomAccessFile
	 */	
	public void writeToFile(RIFFRandomAccessFile file) throws IOException {
		int bytesWritten = 0;
		
		//System.out.println(getClass() + ".writeToFile(" + file + ")");

		// This writes out a zero size chunk.  After all chunks have been writtten,
		// writeActualSizeToFile() gets called to write the actual chunksize

		file.writeID(ID);
		bytesWritten += 4;
		file.writeUNUM32(0);
		bytesWritten += 4;
		file.writeID(signature);
		bytesWritten += 4;
		if ((bytesWritten % 2) != 0) {
			file.write(0);  // pad byte to make chunk even
			bytesWritten += 1;		
		}
		//System.out.println("bytesWritten = " + bytesWritten);
	}
	
	public void writeActualSizeToFile(RIFFRandomAccessFile file) throws IOException {		
		long curPos = 0;
		
		//System.out.println(getClass() + ".writeActualSizeToFile(" + file + ")");

		curPos = file.getFilePointer();
		file.seek(4);  // RIFF chunk is aways at 0 so add 4 bytes for position of chunksize
		file.writeUNUM32(getChunkSize());
		file.seek(curPos);
	}	

	/**
	 * Dumps debugging information to the console.
	 */	
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
		System.out.print("signature = " + RIFFFile.stringForID(signature));
		System.out.println("(0x" + Integer.toHexString(signature) + ")");
	}
}
