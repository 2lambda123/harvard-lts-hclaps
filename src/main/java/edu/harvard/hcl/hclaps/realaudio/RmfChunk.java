//
//  RmfChunk.java
//  hclaps
//
//  Created by Kaylie Ackerman on 10/13/06.
//  Copyright 2006 Kaylie Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.IOException;

import edu.harvard.hcl.hclaps.bwav.IFFRandomAccessFile;
import edu.harvard.hcl.hclaps.bwav.RIFFFile;
import edu.harvard.hcl.hclaps.bwav.chunks.Chunk;

public class RmfChunk extends Chunk implements Cloneable {
	public static final int ID = 0x2E524D46  ; //.rmf
	int chunkVersion;
	long fileVersion;
	long numHeaders;
	
	public RmfChunk ()
	{
		chunkID = ID;
	}
	
	public RmfChunk (RmfChunk aFormatChunk) {
		chunkID = ID;
		chunkVersion = aFormatChunk.chunkVersion;
		fileVersion = aFormatChunk.fileVersion;
		numHeaders = aFormatChunk.numHeaders;
	}
	
	public Object clone () {
		Object obj;
		obj = super.clone ();
		return obj;
    }
	
	public int getChunkVersion ()
	{
		return chunkVersion;
	}
	
	public long getFileVersion ()
	{
		return fileVersion;
	}
	
	public long getNumHeaders ()
	{
		return numHeaders;
	}
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		chunkVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (chunkVersion == 0)
		{
			fileVersion = file.readUNUM32();
			bytesRead += 4;
			
			numHeaders = file.readUNUM32();
			bytesRead += 4;
		}
		else
		{
			System.err.println("Unsupported version of RMF Chunk found. Skipping...");
			int remainingBytes = (int)(chunkSize - bytesRead);
			file.skipBytes(remainingBytes);
			bytesRead += remainingBytes;
		}
		
		return bytesRead;
		
	}
	
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
		System.out.println("chunkVersion = " + chunkVersion);
		System.out.println("fileVersion = " + fileVersion);
		System.out.println("numHeaders = " + numHeaders);
		System.out.println("");
	}
	
	
}
