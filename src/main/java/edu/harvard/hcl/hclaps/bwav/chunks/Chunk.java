//
//  Chunk.java
//  bwav
//
//  Created by Robert La Ferla on 1/18/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav.chunks;

import java.io.IOException;

import edu.harvard.hcl.hclaps.bwav.Chunkable;
import edu.harvard.hcl.hclaps.bwav.IFFRandomAccessFile;
import edu.harvard.hcl.hclaps.bwav.RIFFFile;
import edu.harvard.hcl.hclaps.bwav.RIFFRandomAccessFile;

/**
 * Chunk is the super class of all chunks.
 *
 * @author Robert La Ferla
 */

public abstract class Chunk implements Cloneable {
	public int chunkID = 0;
	public long chunkSize = 0;
	Chunkable parentFile;
	long positionInFile = 0;
	
	/**
	 * Returns a new Chunk instance.
	 */
	public Chunk() {
	}
	
	/**
	 * Returns a new Chunk instance from the specified Chunk.
	 *
	 * @param aChunk A Chunk instance.
	 */
	public Chunk(Chunk aChunk) {
		chunkID = aChunk.chunkID;
		chunkSize = aChunk.chunkSize;
	}
	
	/**
	 * Returns the name of the chunk.
	 */
	public String getChunkName() {
		return RIFFFile.stringForID(chunkID);
	}
	
	/**
	 * Returns the chunk ID.
	 */
	public int getChunkID() {
		return chunkID;
	}
	
	/**
	 * Sets the chunk ID to the specified value.
	 *
	 * @param anInt The chunk ID.
	 */
	public void setChunkID(int anInt) {
		chunkID = anInt;
	}
	
	/**
	 * Returns the size of the chunk.
	 */
	public long getChunkSize() {
		//System.out.println("getChunkSize()");
		//System.out.println("chunkID = " + stringForID());
		//System.out.println("chunkSize = " + chunkSize);
		return chunkSize;
	}
	
	/**
	 * Sets the size of the chunk to the specified value.
	 *
	 * @param aLong The chunk size.
	 */
	public void setChunkSize(long aLong) {
		chunkSize = aLong;
	}
	
	/**
	 * Returns the position of the chunk within the file.
	 */
	public long getPositionInFile() {
		return positionInFile;	
	}
	
	/**
	 * Sets the position of the chunk in the file to specified position.
	 *
	 * @param pos The position in the file.
	 */
	public void setPositionInFile(long pos) {
		positionInFile = pos;
	}
	
	/**
	 * Sets the parent file that this chunk is associated with.
	 *
	 * @param chunkableFile A File that implements the Chunkable interface.
	 */
	public void setParentFile(Chunkable chunkableFile) {
		parentFile = chunkableFile;
	}
	
	/**
	 * Returns the parent file that this chunk is associated with.
	 */
	public Chunkable getParentFile() {
		return parentFile;
	}
		
	/**
	 * Dumps debugging information to the console.
	 */
	public void dump() {};

	/**
	 * Returns a string representation of the chunk ID.
	 */
	public String stringForID() {
		return RIFFFile.stringForID(chunkID);
	}
	
	/**
	 * Reads the chunk from the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 * @see RIFFRandomAccessFile
	 */
	public long readFromFile(RIFFRandomAccessFile file) throws IOException {
		long bytesRead = 0;
		
		return bytesRead;
	}

	/**
	 * Writes the chunk to the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 * @see RIFFRandomAccessFile
	 */
	public void writeToFile(RIFFRandomAccessFile file) throws IOException {
	}
	
	/**
	 * Reads the chunk from the specified IFFRandomAccessFile.
	 *
	 * @param file An IFFRandomAccessFile instance.
	 * @throws IOException
	 * @see IFFRandomAccessFile
	 */	
	public long readFromFile(IFFRandomAccessFile file) throws IOException {
		long bytesRead = 0;
		
		return bytesRead;
	}
	
	/**
	 * Writes the chunk to the specified IFFRandomAccessFile.
	 *
	 * @param file An IFFRandomAccessFile instance.
	 * @throws IOException
	 * @see IFFRandomAccessFile
	 */	
	public void writeToFile(IFFRandomAccessFile file) throws IOException {
	}
	
	/**
	 * Returns a clone of the Chunk.
	 */
	public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("This should not occur since we implement Cloneable");
        }
    }
}
