//
//  IndxRecord.java
//  hclaps
//
//  Created by Kaylie Ackerman on 10/15/06.
//  Copyright 2006 Kaylie Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.IFFRandomAccessFile;

public class IndxRecord implements Cloneable {
	int objectVersion;
	long timestamp;
	long offset;
	long packetCountForThisPacket;
	
	public IndxRecord ()
	{

	}
	
	public IndxRecord (IndxRecord aIndxRecord) {
		objectVersion = aIndxRecord.objectVersion;
		timestamp = aIndxRecord.timestamp;
		offset = aIndxRecord.offset;
		packetCountForThisPacket = aIndxRecord.packetCountForThisPacket;
	}
	
	public Object clone ()
	{
		try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("This should not occur since we implement Cloneable");
        }
    }
	
	public int getObjectVersion ()
	{
		return objectVersion;
	}
	
	public long getTimestamp ()
	{
		return timestamp;
	}
	
	public long getOffset()
	{
		return offset;
	}
	
	public long getPacketCountForThisPacket()
	{
		return packetCountForThisPacket;
	}
	
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		objectVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (objectVersion == 0)
		{	
			
			timestamp = file.readUNUM32();
			bytesRead += 4;
									
			offset = file.readUNUM32();
			bytesRead += 4;
			
			packetCountForThisPacket = file.readUNUM32();
			bytesRead += 4;
			
			
		}
		else
		{
			System.err.println("Unsupported version of index Chunk found. Skipping...");
			int remainingBytes = 12;
			file.skipBytes(remainingBytes);
			bytesRead += remainingBytes;
		}
		
		return bytesRead;
		
	}
	
	public void dump() {
		System.out.println("objectVersion = " + objectVersion);
		System.out.println("timestamp = " + timestamp);
		System.out.println("offset = " + offset);
		System.out.println("packetCountForThisPacket = " + packetCountForThisPacket);
		
		System.out.println("");
	}
	
	
}
