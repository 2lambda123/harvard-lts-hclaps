//
//  ContChunk.java
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

public class ContChunk extends Chunk implements Cloneable {
	public static final int ID = 0x434F4E54; //CONT
	int objectVersion;
	String title;
	String author;
	String copyright;
	String comment;
	
	public ContChunk ()
	{
		chunkID = ID;
		title = null;
		author = null;
		copyright = null;
		comment = null;
	}
	
	public ContChunk (ContChunk aContChunk) {
		chunkID = ID;
		objectVersion = aContChunk.objectVersion;
		title = aContChunk.title;
		author = aContChunk.author;
		copyright = aContChunk.copyright;
		comment = aContChunk.comment;
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
	
	public String getTitle ()
	{
		return title;
	}
	
	public String getAuthor()
	{
		return author;
	}
	
	public String getCopyright()
	{
		return copyright;
	}
	
	public String getComment()
	{
		return comment;
	}
	
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		objectVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (objectVersion == 0)
		{	
			int tmpLen = 0;
			
			tmpLen = file.readUNUM16(); 
			bytesRead += 2;
			if (tmpLen > 0)
			{
				title = file.readStringArray(tmpLen);
				bytesRead += tmpLen;
			}
			
			tmpLen = file.readUNUM16();
			bytesRead += 2;
			if (tmpLen > 0)
			{
				author = file.readStringArray(tmpLen);
				bytesRead += tmpLen;
			}
						
			tmpLen = file.readUNUM16();
			bytesRead += 2;
			if (tmpLen > 0)
			{
				copyright = file.readStringArray(tmpLen);
				bytesRead += tmpLen;
			}
			
			tmpLen = file.readUNUM16();
			bytesRead += 2;
			if (tmpLen > 0)
			{
				comment = file.readStringArray(tmpLen);
				bytesRead += tmpLen;
			}
			
			
		}
		else
		{
			System.err.println("Unsupported version of Content Chunk found. Skipping...");
			int remainingBytes = (int)(chunkSize - bytesRead - 8);
			file.skipBytes(remainingBytes);
			bytesRead += remainingBytes;
		}
		
		return bytesRead;
		
	}
	
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
		System.out.println("title = " + title);
		System.out.println("author = " + author);
		System.out.println("copyright = " + copyright);
		System.out.println("comment = " + comment);
		System.out.println("");
	}
	
	
}
