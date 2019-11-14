//
//  CueChunk.java
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
 * This class is currently not supported.
 *
 * @author Robert La Ferla
 */

public class CueChunk extends Chunk {
	public final static int ID = 0x63756520; // "cue "
	int numberOfCuePoints;
	Vector cuePoints = new Vector();

	public CueChunk() {
	}
	
	public CueChunk(CueChunk aCueChunk) {
		chunkID = ID;
		numberOfCuePoints = aCueChunk.numberOfCuePoints;
		// **** NOT COMPLETE *****
		// need to copy cuePoints....
	}
	
	public int getChunkID() {
		return ID;
	}
	
	public long readFromFile(RIFFRandomAccessFile file) throws IOException {
		long bytesRead = 0;
		
		super.readFromFile(file);
		numberOfCuePoints = file.readNUM32();
		bytesRead += 4;
		for (int i = 0; i < numberOfCuePoints; i++) {
			CuePoint cp;
			
			cp = new CuePoint();
			cp.readFromFile(file);
			cuePoints.add(cp);
			cp.dump();
		}
		
		return bytesRead;
	}

	public void writeToFile(RIFFRandomAccessFile file) throws IOException {
		int bytesWritten = 0;
		
		//System.out.println(getClass() + ".writeToFile(" + file + ")");
		file.writeID(getChunkID());
		bytesWritten += 4;
		file.writeUNUM32(getChunkSize());
		bytesWritten += 4;

		if ((bytesWritten % 2) != 0) {
			file.write(0);  // pad byte to make chunk even
			bytesWritten += 1;		
		}
		
		//System.out.println("bytesWritten = " + bytesWritten);
	}
	
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
		System.out.println("numberOfCuePoints = " + numberOfCuePoints);
	}
	
}
