//
//  DataChunk.java
//  bwav
//
//  Created by Robert La Ferla on 1/18/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav.chunks;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.*;

/**
 * The DataChunk contains the actual audio samples.  The samples are grouped into frames where each frame contains the samples for each
 * audio channel in the data.
 *
 * @author Robert La Ferla
 */
public class DataChunk extends Chunk {
	public final static int ID = 0x64617461;  // "data"
	private int numberOfBlocks = 0;
	private int numberOfWholeBlocks = 0;
	private int numberOfFramesInRemainderBlock = 0;
	private int numberOfFramesPerBlock = 0;
	private int block = 0;
	private long startFrame = 0;
	private long endFrame = 0;
	private final boolean FORWARD = false;
	private final boolean BACKWARD = true;
	private boolean readDirection = FORWARD;
	private long discFileOffset = 0;

	/**
	 * Returns a new DataChunk instance.
	 */
	public DataChunk() {
		chunkID = ID;
	}
	
	/**
	 * Returns the chunk id.
	 *
	 * @return An integer representing the chunk id.
	 */
	public int getChunkID() {
		return ID;
	}
	
	/**
	 * Sets the size of the data chunk in bytes.
	 *
	 * @param aLong	Size in bytes.
	 */
	public void setChunkSize(long aLong) {
		chunkSize = aLong;
	}

	/**
	 * Returns the number of sample frames in the data chunk.
	 *
	 * @return The number of sample frames in the data chunk.
	 */
	
	public long getNumberOfSampleFrames() {
		int frameSizeInBytes = 0;

		frameSizeInBytes = ((WAVEFile)parentFile).formatChunk().getBlockAlign();

		return (chunkSize / frameSizeInBytes);
	}
	
	/**
	 * Reads the data chunk from the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 * @see RIFFRandomAccessFile
	 */
	public long readFromFile(RIFFRandomAccessFile file) throws IOException {
		long bytesRead = 0;
		
		super.readFromFile(file);
		discFileOffset = file.getFilePointer();
		//System.out.println("File pointer = " + discFileOffset);
		//System.out.println("Position in File = " + positionInFile);
		file.skipBytes((int)chunkSize);
		bytesRead = chunkSize;
	
		return bytesRead;
	}

	/**
	 * Writes the data chunk to the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 * @see RIFFRandomAccessFile
	 */
	
	public void writeToFile(RIFFRandomAccessFile file) throws IOException {
		long bytesWritten = 0;
		long curPos = 0;
		
		// System.out.println("******" + getClass() + ".writeToFile(" + file + ")");
		
		file.writeID(getChunkID());
		bytesWritten += 4;
		file.writeUNUM32(0); // Temporarily zero until we know the actual size
		bytesWritten += 4;
		
		chunkSize = ((WAVEFile)parentFile).getDelegate().writeDataChunkForFile((WAVEFile)parentFile);
		//System.out.println("chunkSize = 0x" +  Long.toHexString(chunkSize));
		//System.out.println("chunkSize = " + chunkSize);

		bytesWritten += chunkSize;
		if ((bytesWritten % 2) != 0) {
			file.write(0);  // pad byte to make chunk even
			bytesWritten += 1;
		}
		curPos = file.getFilePointer();
		//System.out.println("curPos = " + curPos);
		//System.out.println("positionInFile = " + positionInFile);
		file.seek(positionInFile + 4);
		file.writeUNUM32(chunkSize);  // Write out actual size of chunk.
		file.seek(curPos);
		//System.out.println("Completed write of data chunk.");
	}

	/**
	 * Reads a specified number of bytes from a specified byte point in the data chunk.
	 *
	 * @param fromByte The byte to start reading from.
	 * @param length The number of bytes to read.
	 */
	public byte[] readBytes(long fromByte, int length) {
		byte [] data = new byte[length];
		int frameSizeInBytes = 0;
		RIFFRandomAccessFile file;
		
		// System.out.println("readBytes(" + fromByte + "," + length + ")");
		file = ((WAVEFile)parentFile).getRandomAccessFile();
		try {
			//file.seek(positionInFile + 8 + fromByte);
			file.seek(discFileOffset + fromByte);
			file.read(data, 0, length);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return data;
	}
	
	/**
	 * Writes an array of bytes to the parent file.
	 *
	 * @param data An array of bytes.
	 */
	public void writeBytes(byte [] data) {
		RIFFRandomAccessFile file;
		
		//System.out.println("writeBytes(data)");
		file = ((WAVEFile)parentFile).getRandomAccessFile();
		try {
			file.write(data);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Writes an array of bytes to the parent file starting at the specified frame.
	 *
	 * @param data An array of bytes.
	 * @param fromFrame The frame to start writing from.
	 */
	public void writeBytes(byte [] data, long fromFrame) {
		RIFFRandomAccessFile file;
		int frameSizeInBytes = 0;
		
		//System.out.println("writeBytes(data, " + fromFrame + ")");
		frameSizeInBytes = ((WAVEFile)parentFile).formatChunk().getBlockAlign();
		file = ((WAVEFile)parentFile).getRandomAccessFile();
		try {
			file.seek(positionInFile + 8 + (fromFrame * frameSizeInBytes));
			file.write(data);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Writes an array of sample frames (represented in bytes) to the parent file.
	 *
	 * @param data An array of sample frames (represented in bytes)
	 * @param fromFrame The frame to start writing from.
	 */
	public void writeSampleFrames(byte [] data, long fromFrame) {
		// Maybe we should check size of bytes against FormatChunk and throw an exception?
		writeBytes(data, fromFrame);
	}

	/*
	public byte[] readSampleFramesInRange(long startTime, long endTime) {
		byte [] data;

		return data;
	}
	 */
	
	
	/**
	 * Returns an array of bytes containing the specified number of sample frames
	 * starting at the specified sample frame.  The first position of a sample frame in the file is 0.
	 *
	 * @param fromFrame	The sample frame (position in file) to start reading from.
	 * @param length	The number of sample frames to read.
	 * @return An array of bytes containing sample points.
	 * @see edu.harvard.hcl.hclaps.bwav.chunks.DataChunk#getNumberOfSampleFrames()
	 */
	public byte[] readSampleFrames(long fromFrame, int length) {
		byte [] data;
		int frameSizeInBytes = 0;
		RIFFRandomAccessFile file;
		int bytesToRead = 0;
		
		//System.out.println("readSampleFrames(" + fromFrame + "," + length + ")");
		frameSizeInBytes = ((WAVEFile)parentFile).formatChunk().getBlockAlign();
		bytesToRead = length * frameSizeInBytes;
		data = new byte[bytesToRead];
				
		file = ((WAVEFile)parentFile).getRandomAccessFile();
		try {
			//file.seek(positionInFile + 8 + (fromFrame * frameSizeInBytes));
			file.seek(discFileOffset + (fromFrame * frameSizeInBytes));
			file.read(data, 0, bytesToRead);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return data;
	}
	
	/**
	 * Returns an array of bytes containing the sample points for the specified channel
	 * starting at the specified frame.
	 *
	 * @param channel	The channel.
	 * @param fromFrame	The sample frame to start reading from.
	 * @return An array of bytes containing sample points.
	 */
	public byte[] readSamplePointsForChannel(short channel, long fromFrame, int length) {
		byte [] data;		
		byte [] frameData;
		int frameSizeInBytes = 0;
		int samplePointSizeInBytes = 0;
		int numberOfChannels = 0;
		FormatChunk fc;
		WAVEFile wf;
		
		// System.out.println("readSamplePointsForChannel(" + channel + "," + fromFrame + "," + length + ")");
		
		wf = (WAVEFile)parentFile;
		fc = wf.formatChunk();
		
		frameSizeInBytes = fc.getBlockAlign();
		//System.out.println("frameSizeInBytes = " + frameSizeInBytes);
		//System.out.println("fc.getNumberOfChannels() = " + fc.getNumberOfChannels());
		samplePointSizeInBytes = frameSizeInBytes / fc.getNumberOfChannels();
		//System.out.println("samplePointSizeInBytes = " + samplePointSizeInBytes);

		data = new byte[samplePointSizeInBytes * length];
		frameData = readSampleFrames(fromFrame, length);
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < samplePointSizeInBytes; j++) {
				data[(i * samplePointSizeInBytes) + j] = frameData[(i * frameSizeInBytes) + (channel * samplePointSizeInBytes) + j];
			}
		}
		//System.out.println("data.length = " + data.length);
		return data;
	}
	
	/**
	 * Writes an array of bytes containing the sample points for the specified channel
	 * starting at the specified frame.  NOT IMPLEMENTED!
	 */
	public void writeSamplePointsForChannel(byte [] data, short channel, long fromFrame) {
	}
	
	/**
	 * Dumps debugging information to the console.
	 */
	public void dump() {
		int frameSizeInBytes = 0;
		
		frameSizeInBytes = ((WAVEFile)parentFile).formatChunk().getBlockAlign();
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize() + " bytes");
		System.out.println("number of sample frames = " + getNumberOfSampleFrames());
		System.out.println("frame size = " + frameSizeInBytes + " bytes");
	}
	
	/**
	 * Sets how the data chunk will be read using blocks.  This is used for reading very large files to
	 * reduce memory usage by reading a block at a time.
	 *
	 * @param fromFrame The starting frame.
	 * @param toFrame The ending frame.
	 * @param framesPerBlock The number of frames to read per block. 
	 */
	public void setReadBlockIterator(long fromFrame, long toFrame, int framesPerBlock) {
		long numberOfSampleFrames;
		
		block = 0;
		if (toFrame >= fromFrame) {
			readDirection = FORWARD;
			startFrame = fromFrame;
			endFrame = toFrame;
		}
		else {
			readDirection = BACKWARD;
			startFrame = toFrame;
			endFrame = fromFrame;
		}
		
		numberOfFramesPerBlock = framesPerBlock;
		numberOfSampleFrames = java.lang.Math.abs(toFrame - fromFrame);

		numberOfWholeBlocks = (int)numberOfSampleFrames / numberOfFramesPerBlock;
		numberOfFramesInRemainderBlock = (int)numberOfSampleFrames % numberOfFramesPerBlock;
		numberOfBlocks = numberOfWholeBlocks;
		if (numberOfFramesInRemainderBlock > 0) {
			numberOfBlocks++;
		}
	}
	
	/**
	 * Returns the number of sample frames in the next block.
	 */
	public int getNumberOfSampleFramesInNextBlock() {
		int framesToRead = 0;
		
		if (block < numberOfWholeBlocks) {
			framesToRead = numberOfFramesPerBlock;
		}
		else if (block == numberOfWholeBlocks) {
			framesToRead = numberOfFramesInRemainderBlock;
		}
		return framesToRead;
	}
	
	/**
	 * Returns the next block as an array of ints.
	 */
	public int[] getNextBlockAsIntegers() {
		byte[] byteData = null;
		
		byteData = getNextBlockAsBytes();
		return convertBytesToInts(byteData);
	}
	
	/**
	 * Returns the next block as an array of bytes.
	 */
	public byte[] getNextBlockAsBytes() {
		byte[] data = null;
		int framesToRead = 0;
		
		// System.out.println("getNextBlockAsBytes()");
		framesToRead = getNumberOfSampleFramesInNextBlock();
		if (framesToRead > 0) {
			long start = 0;
			
			if (readDirection == FORWARD) {
				start = startFrame + (block * numberOfFramesPerBlock);
			}
			else {
				start = endFrame - (block * numberOfFramesPerBlock) - framesToRead;
			}
			
			data = readSampleFrames(start, framesToRead);
			block++;
		}
		
		return data;
	}
	
	/**
	 * Returns whether or not there is another block to read.
	 */
	public boolean hasNextBlock() {
		boolean value = false;
		
		if (block < numberOfBlocks) {
			value = true;
		}
		return value;
	}
	
	/**
	 * Converts an array of bytes to an array of ints.
	 *
	 * @param byteData An array of bytes.
	 */
	public int[] convertBytesToInts(byte [] byteData) {
		int [] data = null;
		int j = 0;
		int bytesPerSample;
		FormatChunk fc;
		
		//System.out.println("convertBytesToInts()");
		
		// Input: array of bytes (little Endian)
		// Output: array of Java ints (big endian)

		fc = ((WAVEFile)parentFile).formatChunk();
		bytesPerSample = (fc.getBitsPerSample() / 8);
		if ((fc.getBitsPerSample() % 8) != 0) {
			bytesPerSample++;
		}
		data = new int[byteData.length / bytesPerSample];
		int signMask = 0x80 << (8 * (bytesPerSample - 1));
		
		for (int i = 0; i < byteData.length; i+= bytesPerSample) {
			int ival = 0;
			
			// bytes are in little-endian order
			for (int k = 0; k < bytesPerSample; k++) {
				int b;
				
				b = (int)byteData[i + k] & 0xff;
				ival += (b << (8 * k));
			}
			int sign = ival & signMask;
			//System.out.println("sign = " + sign);
			//ival = ival & ~signMask;
			
			//sign = sign << (8 *(4 - bytesPerSample));
			if (sign > 1)
			{
				sign = sign | 0xff000000;
			}
			
			ival = ival | sign;
			data[j++] = ival;
		}
		
		return data;
	}

	/**
	 * Converts an array of ints to an array of bytes.
	 *
	 * @param intData An array of ints.
	 */
	public byte[] convertIntsToBytes(int [] intData) {
		byte [] data = null;
		int j = 0;
		int bytesPerSample;
		FormatChunk fc;
		
		//System.out.println("convertIntsToBytes()");
		
		// Input: array of Java ints (big endian)
		// Output: array of bytes (little Endian)
		
		fc = ((WAVEFile)parentFile).formatChunk();
		bytesPerSample = (fc.getBitsPerSample() / 8);
		if ((fc.getBitsPerSample() % 8) != 0) {
			bytesPerSample++;
		}
		data = new byte[intData.length * bytesPerSample];
		
		for (int i = 0; i < intData.length; i++) {
			byte bval[];
			int ival;
			
			ival = intData[i];
			bval = new byte[bytesPerSample];
			for (int k = 0; k < bytesPerSample; k++) {
				bval[k] = (byte)((ival >>> (k * 8)) & 0xff);
				data[j++] = bval[k];
			}
		}
		return data;
	}
	
	
}
