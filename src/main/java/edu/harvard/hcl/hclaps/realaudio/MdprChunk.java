//
//  MdprChunk.java
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

public class MdprChunk extends Chunk implements Cloneable {
	public static final int ID = 0x4D445052; //MDPR 
	int chunkVersion;
	int streamNumber;
	long maxBitRate;
	long avgBitRate;
	long maxDataPacketSize;
	long avgDataPacketSize;
	long streamStartOffset;
	long preroll;
	long duration;
	String streamName;
	String mimeType;
	long typeSpecificDataLength;
	BaseTypeSpecificData typeSpecificData;
	
	public MdprChunk ()
	{
		chunkID = ID;
	}
	
	public MdprChunk (MdprChunk aMdprChunk) {
		chunkID = ID;
		chunkVersion = aMdprChunk.chunkVersion;
		streamNumber = aMdprChunk.streamNumber;
		maxBitRate = aMdprChunk.maxBitRate;
		avgBitRate = aMdprChunk.avgBitRate;
		maxDataPacketSize = aMdprChunk.maxDataPacketSize;
		avgDataPacketSize = aMdprChunk.avgDataPacketSize;
		
		streamStartOffset = aMdprChunk.streamStartOffset;
		preroll = aMdprChunk.preroll;
		duration = aMdprChunk.duration;
		streamName = new String(aMdprChunk.streamName);
		mimeType = new String(aMdprChunk.mimeType);
		
		typeSpecificDataLength = aMdprChunk.typeSpecificDataLength;
		typeSpecificData = (BaseTypeSpecificData)aMdprChunk.typeSpecificData.clone();
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
	
	public int getStreamNumber ()
	{
		return streamNumber;
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
	
	public long getStreamStartOffset()
	{
		return streamStartOffset;
	}
	
	public long getPreroll()
	{
		return preroll;
	}
	
	public long getDuration()
	{
		return duration;
	}
	
	public String getStreamName()
	{
		return streamName;
	}
	
	public String getMimeType()
	{
		return mimeType;
	}
	
	public long getTypeSpecificDataLength()
	{	
		return typeSpecificDataLength;
	}
	
	public BaseTypeSpecificData getTypeSpecificData()
	{
		return typeSpecificData;
	}
	
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		chunkVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (chunkVersion == 0)
		{
		
			streamNumber = file.readUNUM16();
			bytesRead += 2;
			
			maxBitRate = file.readUNUM32();
			bytesRead += 4;
			
			avgBitRate = file.readUNUM32();
			bytesRead += 4;
			
			maxDataPacketSize = file.readUNUM32();
			bytesRead += 4;
			
			avgDataPacketSize = file.readUNUM32();
			bytesRead += 4;
			
			streamStartOffset = file.readUNUM32();
			bytesRead += 4;
			
			preroll = file.readUNUM32();
			bytesRead += 4;
			
			duration = file.readUNUM32();
			bytesRead += 4;
			
			streamName = file.readPstring();
			bytesRead += (streamName.length() + 1);
			
			mimeType = file.readPstring();
			bytesRead += (mimeType.length() + 1);
			
			typeSpecificDataLength = file.readUNUM32();
			bytesRead += 4;
			
			if (mimeType.startsWith("logical"))
			{
				typeSpecificData = new LogicalStreamTypeSpecificData();
			}
			else if (mimeType.startsWith("audio"))
			{
				typeSpecificData = new AudioTypeSpecificData(typeSpecificDataLength);
			}
			
			typeSpecificData.readFromFile(file);	
		}
		else
		{
			System.err.println("Unsupported version of Media Pointer Chunk found. Skipping...");
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
		System.out.println("streamNumber = " + streamNumber);
		System.out.println("maxBitRate = " + maxBitRate);
		System.out.println("avgBitRate = " + avgBitRate);
		System.out.println("maxDataPacketSize = " + maxDataPacketSize);
		System.out.println("avgDataPacketSize = " + avgDataPacketSize);
		System.out.println("streamStartOffset = " + streamStartOffset);
		System.out.println("preroll = " + preroll);
		System.out.println("duration = " + duration);
		System.out.println("streamName = " + streamName);
		System.out.println("mimeType = " + mimeType);
		System.out.println("typeSpecificDataLength = " + typeSpecificDataLength);
		typeSpecificData.dump();
		
		System.out.println("");
	}
	
	
}

