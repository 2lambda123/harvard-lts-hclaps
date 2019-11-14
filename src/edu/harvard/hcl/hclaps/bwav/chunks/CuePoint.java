//
//  CuePoint.java
//  bwav
//
//  Created by Robert La Ferla on 1/19/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav.chunks;

import java.io.*;
import edu.harvard.hcl.hclaps.bwav.*;

/*
 * This class is currently not supported.
 *
 * @author Robert La Ferla
 */

public class CuePoint {
	int identifier;
	int position;
	int chunk;
	int chunkStart;
	int blockStart;
	int sampleOffset;
	
	public long readFromFile(RIFFRandomAccessFile file) throws IOException {
		long bytesRead = 0;
		
		identifier = file.readNUM32();
		bytesRead += 4;
		position = file.readNUM32();
		bytesRead += 4;
		chunk = file.readID();
		bytesRead += 4;
		chunkStart = file.readNUM32();
		bytesRead += 4;
		blockStart = file.readNUM32();
		bytesRead += 4;
		sampleOffset = file.readNUM32();
		bytesRead += 4;
		return bytesRead;
	}
	
	public void dump() {
		System.out.println("identifier = " + identifier);
		System.out.println("position = " + position);
		System.out.println("chunk = " +  RIFFFile.stringForID(chunk));
		System.out.println("chunkStart = " + chunkStart);
		System.out.println("blockStart = " + blockStart);
		System.out.println("sampleOffset = " + sampleOffset);
	}
	
}
