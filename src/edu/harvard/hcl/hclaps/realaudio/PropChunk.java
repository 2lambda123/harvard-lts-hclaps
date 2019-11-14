//
//  PropChunk.java
//  hclaps
//
//  Created by David Ackerman on 10/13/06.
//  Copyright 2006 David Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;
import edu.harvard.hcl.hclaps.bwav.chunks.*;

public class PropChunk extends Chunk implements Cloneable {
	public static final int ID = 0x50524F50; //PROP
	int chunkVersion;
	long maxBitRate;
	long avgBitRate;
	long maxDataPacketSize;
	long avgDataPacketSize;
	long numInterleavePackets;
	long fileDuration;
	long preroll;
	long indexOffset;
	long dataOffset;
	int numStreams;
	int flags;
	
	public PropChunk ()
	{
		chunkID = ID;
	}
	
	public PropChunk (PropChunk aFormatChunk) {
		chunkID = ID;
		maxBitRate = aFormatChunk.maxBitRate;
		avgBitRate = aFormatChunk.avgBitRate;
		maxDataPacketSize = aFormatChunk.maxDataPacketSize;
		avgDataPacketSize = aFormatChunk.avgDataPacketSize;
		numInterleavePackets = aFormatChunk.numInterleavePackets;
		fileDuration = aFormatChunk.fileDuration;
		preroll = aFormatChunk.preroll;
		indexOffset = aFormatChunk.indexOffset;
		dataOffset = aFormatChunk.dataOffset;
		numStreams = aFormatChunk.numStreams;
		flags = aFormatChunk.flags;
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
	
	public long getMaxBitRate ()
	{
		return maxBitRate;
	}
	
	public long getAvgBitRate ()
	{
		return avgBitRate;
	}
	
	public long getMaxDataPacketSize()
	{
		return maxDataPacketSize;
	}
	
	public long getAvgDataPacketSize()
	{
		return avgDataPacketSize;
	}
	
	public long getNumInterleavePackets()
	{
		return numInterleavePackets;
	}
	
	public long getFileDuration()
	{
		return fileDuration;
	}
	
	public long getPreroll()
	{
		return preroll;
	}
	
	public long getIndexOffset()
	{
		return indexOffset;
	}
	
	public long getDataOffset()
	{
		return dataOffset;
	}
	
	public int getNumStreams()
	{
		return numStreams;
	}
	
	public int getFlags()
	{
		return flags;
	}
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		chunkVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (chunkVersion == 0)
		{
		
			maxBitRate = file.readUNUM32();
			bytesRead += 4;
			
			avgBitRate = file.readUNUM32();
			bytesRead += 4;
			
			maxDataPacketSize = file.readUNUM32();
			bytesRead += 4;
			
			avgDataPacketSize = file.readUNUM32();
			bytesRead += 4;
			
			numInterleavePackets = file.readUNUM32();
			bytesRead += 4;
			
			fileDuration = file.readUNUM32();
			bytesRead += 4;
			
			preroll = file.readUNUM32();
			bytesRead += 4;
			
			indexOffset = file.readUNUM32();
			bytesRead += 4;
			
			dataOffset = file.readUNUM32();
			bytesRead += 4;
			
			numStreams = file.readUNUM16();
			bytesRead += 2;
			
			flags = file.readUNUM16();
			bytesRead += 2;
		}
		else
		{
			System.err.println("Unsupported version of Property Chunk found. Skipping...");
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
		System.out.println("maxBitRate = " + maxBitRate);
		System.out.println("avgBitRate = " + avgBitRate);
		
		System.out.println("maxDataPacketSize = " + maxDataPacketSize);
		System.out.println("avgDataPacketSize = " + avgDataPacketSize);
		System.out.println("numInterleavePackets = " + numInterleavePackets);
		System.out.println("fileDuration = " + fileDuration);
		System.out.println("preroll = " + preroll);
		System.out.println("indexOffset = " + indexOffset);
		System.out.println("dataOffset = " + dataOffset);
		System.out.println("numStreams = " + numStreams);
		System.out.println("flags = " + flags);
		System.out.println("");
	}
	
	
}
