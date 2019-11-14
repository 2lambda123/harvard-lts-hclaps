//
//  RealAudioFile.java
//  hclaps
//
//  Created by David Ackerman on 10/15/06.
//  Copyright 2006 David Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.net.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;
import edu.harvard.hcl.hclaps.bwav.chunks.*;

public class RealAudioFile extends IFFFile {
	protected IFFRandomAccessFile iraf = null;
	private File theFile = null;
	
	public ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	
	public RealAudioFile (String pathname) throws FileNotFoundException
	{
		theFile = new File(pathname);
		open("r");
		load();
	}
	
	public RealAudioFile(String pathname, String mode) throws FileNotFoundException {
		theFile = new File(pathname);
		open(mode);
		load();
	}
	
	public RealAudioFile(URI uri) throws FileNotFoundException {
		theFile = new File(uri);
		open("r");
		load();
	}

	public RealAudioFile(URI uri, String mode) throws FileNotFoundException {
		theFile = new File(uri);
		open(mode);
		load();
	}
	
	protected void finalize() throws Throwable {
		if (iraf != null) {
			try {
				iraf.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void setFile(File aFile) {
		theFile = aFile;
	}
	
	public File getFile() {
		return theFile;
	}
	
	public void setFileName(String aFileName) {
		theFile = new File(aFileName);
	}
	
	public String getFileName() {
		return theFile.getName();
	}
	
	public IFFRandomAccessFile getRandomAccessFile() {
		return iraf;
	}

	public void open(String mode) throws FileNotFoundException {
		if (iraf == null) {
			iraf = new IFFRandomAccessFile(theFile, mode);
		}
	}
	
	public void open() throws FileNotFoundException {
		open("rw");
	}

	public void close() throws IOException {
		if (iraf != null) {
			iraf.close();
			iraf = null;
		}
	}
	
	public void addChunk(Chunk aChunk) {
		chunks.add(aChunk);
		aChunk.setParentFile(this);
	}
	
	public void load()
	{
		try
		{
			Chunk ch = null;
			int chunkID = -1;
			long chunkSize = 0;
			
			do {
				chunkID = iraf.readID();
				if (chunkID != -1)
				{
					chunkSize = iraf.readUNUM32();
					switch (chunkID)
					{
						case RmfChunk.ID:
							ch = new RmfChunk();
							break;
						case PropChunk.ID:
							ch = new PropChunk();
							break;
						case MdprChunk.ID:
							ch = new MdprChunk();
							break;
						case ContChunk.ID:
							ch = new ContChunk();
							break;
						case RealDataChunk.ID:
							ch = new RealDataChunk();
							break;
						case IndxChunk.ID:
							ch = new IndxChunk();
							break;
		
						default:
							System.out.println("FOUND: " + RIFFFile.stringForID(chunkID) + " skipping...");
							iraf.skipBytes((int)chunkSize - 8);
							continue;
					}
					
					ch.setChunkID(chunkID);
					ch.setChunkSize(chunkSize);
					ch.readFromFile(iraf);
					addChunk(ch);
					//ch.dump();
					//ch = null;
				}
			} while (chunkID != -1);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
}
