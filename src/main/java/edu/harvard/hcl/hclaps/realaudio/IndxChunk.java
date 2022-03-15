//
//  IndxChunk.java
//  hclaps
//
//  Created by Kaylie Ackerman on 10/15/06.
//  Copyright 2006 Kaylie Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;
import edu.harvard.hcl.hclaps.bwav.chunks.*;

public class IndxChunk extends Chunk implements Cloneable {
	public static final int ID = 0x494e4458; //DATA
	int objectVersion;
	long numIndices;
	int streamNumber;
	long nextIndexHeader;
	ArrayList<IndxRecord> records;
	
	public IndxChunk ()
	{
		chunkID = ID;
	}
	
	public IndxChunk (IndxChunk aIndxChunk) {
		chunkID = ID;
		objectVersion = aIndxChunk.objectVersion;
		numIndices = aIndxChunk.numIndices;
		streamNumber = aIndxChunk.streamNumber;
		nextIndexHeader = aIndxChunk.nextIndexHeader;
		
		records = new ArrayList<IndxRecord>();
		for (IndxRecord ir: aIndxChunk.records)
		{
			records.add((IndxRecord)ir.clone());
		}
	}
	
	public Object clone () {
		Object obj;
		obj = super.clone ();
		return obj;
    }
	
	public int getObjectVersion ()
	{
		return objectVersion;
	}
	
	public long getNumIndices ()
	{
		return numIndices;
	}
	
	public int getStreamNumber()
	{
		return streamNumber;
	}
	
	public long getNextIndexHeader()
	{
		return nextIndexHeader;
	}
	
	public ArrayList<IndxRecord> getPackets ()
	{
		return records;
	}
	
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		objectVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (objectVersion == 0)
		{	
			
			numIndices = file.readUNUM32();
			bytesRead += 4;
									
			streamNumber = file.readUNUM16();
			bytesRead += 2;
			
			nextIndexHeader = file.readUNUM32();
			bytesRead += 4;
			
			IndxRecord rec;
			records = new ArrayList<IndxRecord>();
			for (int i = 0; i < numIndices; i++)
			{
				rec = new IndxRecord();
				rec.readFromFile(file);
				records.add(rec);
			}
			
			
		}
		else
		{
			System.err.println("Unsupported version of index Chunk found. Skipping...");
			int remainingBytes = (int)(chunkSize - bytesRead - 8);
			file.skipBytes(remainingBytes);
			bytesRead += remainingBytes;
		}
		
		return bytesRead;
		
	}
	
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
		System.out.println("objectVersion = " + objectVersion);
		System.out.println("numIndices = " + numIndices);
		System.out.println("streamNumber = " + streamNumber);
		System.out.println("nextIndexHeader = " + nextIndexHeader);
		int i = 0;
		for (IndxRecord ir : records)
		{
			System.out.println("\nINDEX: " + i++);
			ir.dump();
		}
		System.out.println("");
	}
	
	
}
