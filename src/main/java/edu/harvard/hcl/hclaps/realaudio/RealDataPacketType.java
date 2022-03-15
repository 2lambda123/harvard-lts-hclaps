//
//  RealDataPacketChunk.java
//  hclaps
//
//  Created by Kaylie Ackerman on 10/15/06.
//  Copyright 2006 Kaylie Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.IFFRandomAccessFile;

public class RealDataPacketType implements Cloneable {
	int objectVersion;
	int length;
	int streamNumber;
	long timestamp;
	int packetGroup;
	int flags;
	int asmRules;
	int asmFlags;
	//int[] data;
	
	public RealDataPacketType ()
	{
		packetGroup = -1;
		flags = -1;
		asmRules = -1;
		asmFlags = -1;
	}
	
	public RealDataPacketType (RealDataPacketType aRealDataPacketType) {
		objectVersion = aRealDataPacketType.objectVersion;
		length = aRealDataPacketType.length;
		streamNumber = aRealDataPacketType.streamNumber;
		timestamp = aRealDataPacketType.timestamp;
		packetGroup = aRealDataPacketType.packetGroup;
		flags = aRealDataPacketType.flags;
		asmRules = aRealDataPacketType.asmRules;
		asmFlags = aRealDataPacketType.asmFlags;
		/*data = new int[length];
		for (int i = 0; i < aRealDataPacketType.data.length; i++)
		{
			data[i] = aRealDataPacketType.data[i];
		}*/
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
	
	public int getLength ()
	{
		return length;
	}
	
	public int getStreamNumber()
	{
		return streamNumber;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public int getPacketGroup()
	{
		return packetGroup;
	}
	
	public int getFlags()
	{
		return flags;
	}
	
	public int getAsmRules()
	{
		return asmRules;
	}
	
	public int getAsmFlags()
	{
		return asmFlags;
	}
	
	/*public int[] getData()
	{
		return data;
	}*/
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		objectVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (objectVersion == 0)
		{	
			length = file.readUNUM16();
			bytesRead += 2;
			
			streamNumber = file.readUNUM16();
			bytesRead += 2;
			
			timestamp = file.readUNUM32();
			bytesRead += 4;
			
			packetGroup = file.readNUM8();
			bytesRead += 1;
									
			flags = file.readNUM8();
			bytesRead += 1;
			
			int remainder = (int)(length - bytesRead);
			/*data = new int[remainder];
			
			for (int i = 0; i < remainder; i++)
			{
				data[i] = file.readNUM8();
				bytesRead += remainder;
			}*/
			
			file.skipBytes(remainder);
			bytesRead += remainder;
			
		}
		else if (objectVersion == 1)
		{
			length = file.readUNUM16();
			bytesRead += 2;
			
			streamNumber = file.readUNUM16();
			bytesRead += 2;
			
			timestamp = file.readUNUM32();
			bytesRead += 4;
			
			asmRules = file.readUNUM16();
			bytesRead += 2;
									
			asmFlags = file.readNUM8();
			bytesRead += 1;
			
			int remainder = (int)(length - bytesRead);
			/*data = new int[remainder];
			
			for (int i = 0; i < remainder; i++)
			{
				data[i] = file.readNUM8();
				bytesRead += remainder;
			}*/
			file.skipBytes(remainder);
			bytesRead += remainder;
		}
		else
		{
			System.err.println("Unsupported version of Data Packet found. Skipping...");
			int remainingBytes = (int)(length - bytesRead - 8);
			file.skipBytes(remainingBytes);
			bytesRead += remainingBytes;
		}
		
		return bytesRead;
		
	}
	
	public void dump() {
		System.out.print("length = " + length);
		System.out.print("\tstreamNumber = " + streamNumber);
		System.out.print("\ttimestamp = " + timestamp);
		if (objectVersion == 0)
		{
			System.out.print("\tpacketGroup = " + packetGroup);
			System.out.print("\tflags = " + flags);
		}
		else if (objectVersion == 1)
		{
			System.out.print("\tasmRules = " + asmRules);
			System.out.print("\tasmFlags = " + asmFlags);
		}
		
		System.out.println("");
	}
	
	
}
