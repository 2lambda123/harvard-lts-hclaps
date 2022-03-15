//
//  WAVEFile.java
//  bwav
//
//  Created by Robert La Ferla on 1/18/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

import java.util.*;
import java.io.*;
import java.net.*;
import edu.harvard.hcl.hclaps.bwav.chunks.*;
import com.therockquarry.aes31.adl.TcfToken;

/**
 * A WAVE (or WAV) file is a Waveform audio format file.  It is RIFF file that is composed of a RIFFChunk, a FormatChunk, a DataChunk and possibly other chunks like the BExtChunk.  This class allows you to read, write, modify and access chunks in WAV files.  A delegate
 * object is used to write the DataChunk of a WAVE file in blocks to accomodate large files that may not fit in memory.
 * 
 * @see WAVEDataChunkWriter
 * @author Robert La Ferla
 */

public class WAVEFile extends RIFFFile {
	public final static int ID = 0x57415645;  // 'WAVE'
	WAVEDataChunkWriter delegate;
	
	/**
	 * Returns a new WAVEFile instance.
	 */
	public WAVEFile() {		
		riffChunk().setSignature(WAVEFile.ID);
	}

	/**
	 * Returns a new WAVEFile instance from the given path.
	 *
	 * @param pathname A path.
	 * @throws FileNotFoundException
	 */
	public WAVEFile(String pathname) throws FileNotFoundException {
		super(pathname);
	}
	
	/**
	 * Returns a new WAVEFile instance from the given URI.
	 *
	 * @param uri A URI.
	 * @throws FileNotFoundException
	 */
	public WAVEFile(URI uri) throws FileNotFoundException {
		super(uri);
	}
	
	/**
	 * Sets the delegate object that is resposible for writing out the data chunk.
	 *
	 * @param aDelegate An object that implements the WAVEDataChunkWriter interface.
	 */
	public void setDelegate(WAVEDataChunkWriter aDelegate) {
		delegate = aDelegate;
	}
	
	/**
	 * Returns the delegate responsible for writing out the data chunk.
	 */
	public WAVEDataChunkWriter getDelegate() {
		return delegate;
	}
	
	/**
	 * Returns the format chunk.
	 */	
	public FormatChunk formatChunk() {
		return (FormatChunk)getChunkNamed("fmt ");
	}

	/**
	 * Returns the data chunk.
	 */	
	public DataChunk dataChunk() {
		return (DataChunk)getChunkNamed("data");
	}
	
	/**
	 * Returns the BEXT chunk.
	 */	
	public BExtChunk bextChunk() {
		return (BExtChunk)getChunkNamed("bext");
	}
	
	/**
	 * Returns the chunks in the WAVEFile.  This currently returns only the supported chunks: RIFF, format, data, and if present, the BEXT chunk.
	 */
	public ArrayList getChunks() {
		ArrayList al;

		al = new ArrayList();
		al.add(getChunkNamed("RIFF"));
		al.add(getChunkNamed("fmt "));
		al.add(getChunkNamed("data"));
		if (getChunkNamed("bext") != null) {
			al.add(getChunkNamed("bext"));
		}
		return al;
	}
	
	/**
	 * Saves the WAVEFile.  This currently saves only the supported chunks (in order): RIFF, format, data, and if present, the BEXT chunk.
	 *
	 * @see RIFFFile#saveChunks(ArrayList)
	 */
	public void save() {
		ArrayList al;
		Iterator it;

		al = new ArrayList();
		al.add(getChunkNamed("RIFF"));
		al.add(getChunkNamed("fmt "));
		al.add(getChunkNamed("data"));
		if (getChunkNamed("bext") != null) {
			al.add(getChunkNamed("bext"));
		}
		
		// Copy other chunks verbatim.   This is currently disabled.
		// The only chunks we will write are RIFF, fmt, data and bext (if present)

		/*
		it = chunks.iterator();
		while (it.hasNext()) {
			Chunk ch;
			String key;
			
			ch = (Chunk)it.next();
			key = ch.getChunkName();
			if (!key.equals("RIFF") && !key.equals("fmt ") && !key.equals("data") && !key.equals("bext")) {
				al.add(ch);
			}
		}
		*/
		 
		saveChunks(al);
		
		try
		{
			rraf.setLength(riffChunk().getChunkSize() + 8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the start time of the file (either the time reference of the BEXT chunk or 0 if there is no BEXT chunk.)
	 */	
	public TcfToken getStartTime() {
		TcfToken tt;
		BExtChunk bc;
		long start = 0;
		
		bc = bextChunk();
		if (bc != null) {
			start = bc.getTimeReference();
		}
		tt = new TcfToken(start, (double)formatChunk().getSamplesPerSecond());
		return tt;
	}
	
	/*
	 * Returns the duration of the file.
	 */
	public TcfToken getDuration() {
		TcfToken tt;
		
		tt = new TcfToken(dataChunk().getNumberOfSampleFrames(), (double)formatChunk().getSamplesPerSecond());
		
		return tt;
	}
	
	
}
