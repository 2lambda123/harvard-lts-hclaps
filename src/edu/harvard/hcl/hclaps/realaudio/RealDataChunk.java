//
//  RealDataChunk.java
//  hclaps
//
//  Created by David Ackerman on 10/15/06.
//  Copyright 2006 David Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;
import edu.harvard.hcl.hclaps.bwav.chunks.*;

public class RealDataChunk extends Chunk implements Cloneable {
	public static final int ID = 0x44415441; //DATA
	int objectVersion;
	long numPackets;
	long nextDataHeader;
	ArrayList<RealDataPacketType> packets;
	
	public RealDataChunk ()
	{
		chunkID = ID;
	}
	
	public RealDataChunk (RealDataChunk aRealDataChunk) {
		chunkID = ID;
		objectVersion = aRealDataChunk.objectVersion;
		numPackets = aRealDataChunk.numPackets;
		nextDataHeader = aRealDataChunk.nextDataHeader;
		packets = new ArrayList<RealDataPacketType>();
		for (RealDataPacketType rdpt: aRealDataChunk.packets)
		{
			packets.add((RealDataPacketType)rdpt.clone());
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
	
	public long getNumPackets ()
	{
		return numPackets;
	}
	
	public long getNextDataHeader()
	{
		return nextDataHeader;
	}
	
	public ArrayList<RealDataPacketType> getPackets ()
	{
		return packets;
	}
	
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		objectVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (objectVersion == 0)
		{	
			
			numPackets = file.readUNUM32();
			bytesRead += 4;
									
			nextDataHeader = file.readUNUM32();
			bytesRead += 4;
			RealDataPacketType pack;
			packets = new ArrayList<RealDataPacketType>();
			for (int i = 0; i < numPackets; i++)
			{
				pack = new RealDataPacketType();
				pack.readFromFile(file);
				packets.add(pack);
			}
			
			
		}
		else
		{
			System.err.println("Unsupported version of Data Chunk found. Skipping...");
			int remainingBytes = (int)(chunkSize - bytesRead - 8);
			file.skipBytes(remainingBytes);
			bytesRead += remainingBytes;
		}
		
		return bytesRead;
		
	}
	
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
		System.out.println("numPackets = " + numPackets);
		System.out.println("nextDataHeader = " + nextDataHeader);
		int i = 0;
		for (RealDataPacketType rdpt : packets)
		{
			System.out.println("\nPACKET: " + i++);
			rdpt.dump();
		}
		System.out.println("");
	}
	
	
}
