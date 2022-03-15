//
//  AudioTypeSpecificData.java
//  hclaps
//
//  Created by Kaylie Ackerman on 10/14/06.
//  Copyright 2006 Kaylie Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;

public class AudioTypeSpecificData extends BaseTypeSpecificData implements Cloneable {
	public static final int ID = 0x2E7261FD; //.raf0xfd
	int id;
	long size;
	int audioSpecificVersion;
	int audioSpecificRevision;
	int raID;
	long raChunkSize;
	int version;
	int revision;
	int headerBytesTotal;
	int compressionFlavorIndex;
	long granularity;
	long bytesTotal;
	long bytesPerMinute;
	long bytesPerMinute2;
	int interleaveFactor;
	int interleaveBlockSize;
	long userData;
	long sampleRate;
	long sampleRate2;
	int u_data;
	int sampleSize;
	int numChannels;
	int interleaveCode;
	int compressionCode;
	
	
	public AudioTypeSpecificData (long size)
	{
		id = ID;
		this.size = size;
	}
	
	public AudioTypeSpecificData (AudioTypeSpecificData anAudioTypeSpecificData) {
		id = ID;
		size = anAudioTypeSpecificData.size;
		audioSpecificVersion = anAudioTypeSpecificData.audioSpecificVersion;
		audioSpecificRevision = anAudioTypeSpecificData.audioSpecificRevision;
		raID = anAudioTypeSpecificData.raID;
		raChunkSize = anAudioTypeSpecificData.raChunkSize;
		version = anAudioTypeSpecificData.version;
		revision = anAudioTypeSpecificData.revision;
		headerBytesTotal = anAudioTypeSpecificData.headerBytesTotal;
		compressionFlavorIndex = anAudioTypeSpecificData.compressionFlavorIndex;
		granularity = anAudioTypeSpecificData.granularity;
		bytesTotal = anAudioTypeSpecificData.bytesTotal;
		bytesPerMinute = anAudioTypeSpecificData.bytesPerMinute;
		bytesPerMinute2 = anAudioTypeSpecificData.bytesPerMinute2;
		interleaveFactor = anAudioTypeSpecificData.interleaveFactor;
		interleaveBlockSize = anAudioTypeSpecificData.interleaveBlockSize;
		userData = anAudioTypeSpecificData.userData;
		sampleRate = anAudioTypeSpecificData.sampleRate;
		sampleRate2 = anAudioTypeSpecificData.sampleRate2;
		u_data = anAudioTypeSpecificData.u_data;
		sampleSize = anAudioTypeSpecificData.sampleSize;
		numChannels = anAudioTypeSpecificData.numChannels;
		interleaveCode = anAudioTypeSpecificData.interleaveCode;
		compressionCode = anAudioTypeSpecificData.compressionCode;
		
	}
	
	public Object clone () {
		Object obj;
		
		obj = super.clone();

		return obj;
    }

	
	public int getAudioSpecificVersion ()
	{
		return audioSpecificVersion;
	}

	public int getAudioSpecificRevision ()
	{
		return audioSpecificRevision;
	}
	
	public int getRaID ()
	{
		return raID;
	}
	
	public long getRaChunkSize ()
	{
		return raChunkSize;
	}
	
	public int getVersion ()
	{
		return version;
	}
	
	public int getRevision ()
	{
		return revision;
	}
	
	public int getHeaderBytesTotal ()
	{
		return headerBytesTotal;
	}
		
	public int getCompressionFlavorIndex ()
	{
		return compressionFlavorIndex;
	}
	
	public long getGranularity ()
	{
		return granularity;
	}
	
	public long getBytesTotal ()
	{
		return bytesTotal;
	}

	public long getBytesPerMinute ()
	{
		return bytesPerMinute;
	}
	
	public long getBytesPerMinute2 ()
	{
		return bytesPerMinute2;
	}
	
	public int getInterleaveFactor ()		//sub-packet_h
	{
		return interleaveFactor;
	}
	
	public int getInterleaveBlockSize ()		//framesize
	{
		return interleaveBlockSize;
	}
	
	public long getUserData ()
	{
		return userData;
	}
	
	public long getSampleRate ()
	{
		return sampleRate;
	}
	
	public long getSampleRate2 ()
	{
		return sampleRate2;
	}
	
	public int getU_data ()
	{
		return u_data;
	}
	
	public int getSampleSize ()
	{
		return sampleSize;
	}
	
	public int getNumChannels ()
	{
		return numChannels;
	}
	
	public int getInterleaveCode ()
	{
		return interleaveCode;
	}
	
	public String getInterleaveCodeString ()
	{
		return RIFFFile.stringForID(interleaveCode);
	}
	
	public int getCompressionCode ()
	{
		return compressionCode;
	}
	
	public String getCompressionCodeString ()
	{
		return RIFFFile.stringForID(compressionCode) ;
	}
	
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		int tmp = file.readID();
		bytesRead += 4;
		if (tmp != this.ID)
		{
			System.err.println("Audio Type Secific Data signature mismatch!");
			System.err.println("READ = 0x" + Integer.toHexString(tmp) + " (" + RIFFFile.stringForID(tmp) + ")");
			System.err.println("EXPECTED = 0x" + Integer.toHexString(ID) + " (" + RIFFFile.stringForID(ID) + ")");
		}
		
		audioSpecificVersion = file.readUNUM16();
		bytesRead += 2;
		
		audioSpecificRevision = file.readUNUM16();
		bytesRead += 2;
		
		raID = file.readID();
		bytesRead += 4;
		
		raChunkSize = file.readUNUM32();
		bytesRead += 4;
		
		version = file.readUNUM16();
		bytesRead += 2;
		
		revision = file.readUNUM16();
		bytesRead += 2;
		if (version == 5)
		{
		
			headerBytesTotal = file.readUNUM16();
			bytesRead += 2;
			
			compressionFlavorIndex = file.readUNUM16();
			bytesRead += 2;
			
			granularity = file.readUNUM32();
			bytesRead += 4;
			
			bytesTotal = file.readUNUM32();
			bytesRead += 4;
			
			bytesPerMinute = file.readUNUM32();
			bytesRead += 4;
			
			bytesPerMinute2 = file.readUNUM32();
			bytesRead += 4;
			
			interleaveFactor = file.readUNUM16();
			bytesRead += 2;
			
			interleaveBlockSize = file.readUNUM16();
			bytesRead += 2;
			
			userData = file.readUNUM32();
			bytesRead += 4;
			
			sampleRate = file.readUNUM32();
			bytesRead += 4;
			
			sampleRate2 = file.readUNUM32();
			bytesRead += 4;
			
			u_data = file.readUNUM16();
			bytesRead += 2;
			
			sampleSize = file.readUNUM16();
			bytesRead += 2;
			
			numChannels = file.readUNUM16();
			bytesRead += 2;
			
			interleaveCode = file.readID();
			bytesRead += 4;
			
			compressionCode = file.readID();
			bytesRead += 4;
		}
		
		int remainingBytes = (int)(size - bytesRead);
		file.skipBytes(remainingBytes);
		bytesRead += remainingBytes;
		
		return bytesRead;
		
	}
	
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(id) + " (" + RIFFFile.stringForID(id) + ")");
		System.out.println("size = " + size);
		System.out.println("audioSpecificVersion = " + audioSpecificVersion);
		System.out.println("audioSpecificRevision = " + audioSpecificRevision);		
		System.out.println("raID = 0x" + Integer.toHexString(raID) + " (" + RIFFFile.stringForID(raID) + ")");
		System.out.println("raChunkSize = " + raChunkSize);
		System.out.println("version = " + version);
		System.out.println("revision = " + revision);
		System.out.println("headerBytesTotal = " + headerBytesTotal);
		System.out.println("compressionFlavorIndex = " + compressionFlavorIndex);
		System.out.println("granularity = " + granularity);
		System.out.println("bytesTotal = " + bytesTotal);
		System.out.println("bytesPerMinute = " + bytesPerMinute);
		System.out.println("bytesPerMinute2 = " + bytesPerMinute2);
		System.out.println("interleaveFactor = " + interleaveFactor);
		System.out.println("interleaveBlockSize = " + interleaveBlockSize);
		System.out.println("userData = " + userData);
		System.out.println("sampleRate = " + sampleRate);
		System.out.println("sampleRate2 = " + sampleRate2);
		System.out.println("u_data = " + u_data);
		System.out.println("sampleSize = " + sampleSize);
		System.out.println("numChannels = " + numChannels);
		System.out.println("interleaveCode = 0x" + Integer.toHexString(interleaveCode) + " (" + RIFFFile.stringForID(interleaveCode) + ")");
		System.out.println("compressionCode = 0x" + Integer.toHexString(compressionCode) + " (" + RIFFFile.stringForID(compressionCode) + ")");
		
		System.out.println("");
	}
	
	
}

