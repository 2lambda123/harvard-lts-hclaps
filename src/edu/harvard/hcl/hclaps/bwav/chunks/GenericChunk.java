//
//  GenericChunk.java
//  bwav
//
//  Created by Robert La Ferla on 1/31/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav.chunks;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;

/**
 * A GenericChunk is a wrapper for any unsupported chunks.  While they are not decoded, they are also not discarded.
 *
 * @author Robert La Ferla
 */

public class GenericChunk extends Chunk {
	byte chunkData[];

	/**
	 * Returns a new GenericChunk instance.
	 */
	public GenericChunk() {
	}
	
	/**
	 * Returns a copy of the specified GenericChunk instance.
	 */
	public GenericChunk(GenericChunk aGenericChunk) {
		chunkID = aGenericChunk.getChunkID();
		chunkData = new byte[aGenericChunk.chunkData.length];
		for (int i = 0; i < aGenericChunk.chunkData.length; i++) {
			chunkData[i] = aGenericChunk.chunkData[i];
		}
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
        //System.out.println(" :: " + chunkSize);
		
		super.readFromFile(file);
		if (chunkSize > 0) {
			chunkData = new byte[(int)chunkSize];
			file.read(chunkData, 0, (int)chunkSize);
			bytesRead = chunkSize;
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
		file.writeID(getChunkID());
		bytesWritten += 4;
		file.writeUNUM32(getChunkSize());
		bytesWritten += 4;
		if (chunkData.length > 0) {
			file.write(chunkData);
			bytesWritten += chunkData.length;
		}
		if ((bytesWritten % 2) != 0) {
			file.write(0);  // pad byte to make chunk even
			bytesWritten += 1;		
		}		
		//System.out.println("bytesWritten = " + bytesWritten);
	}
	
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
	}
}
