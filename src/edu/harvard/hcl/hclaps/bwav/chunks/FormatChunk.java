//
//  FormatChunk.java
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
 * The FormatChunk contains information about the format of the audio in the data chunk.  It is a required chunk in all WAVE files.
 *
 * @author Robert La Ferla
 */
public class FormatChunk extends Chunk implements Cloneable {
	public final static int ID = 0x666d7420;  // "fmt "
	short formatTag;
	int channels;
	long samplesPerSecond;
	long avgBytesPerSecond;
	int blockAlign;
	int bitsPerSample;
	int numberOfAdditionalBytes;
	byte [] additionalBytes;
	int skippedBytes;

	/**
	 * Returns a new FormatChunk instance.
	 */
	public FormatChunk() {
		chunkID = ID;
	}
	
	/**
	 * Returns a new FormatChunk instance from the specified FormatChunk instance.
	 *
	 * @param aFormatChunk A FormatChunk instance.
	 */
	public FormatChunk(FormatChunk aFormatChunk) {
		chunkID = ID;
		formatTag = aFormatChunk.formatTag;
		channels = aFormatChunk.channels;
		samplesPerSecond = aFormatChunk.samplesPerSecond;
		avgBytesPerSecond = aFormatChunk.avgBytesPerSecond;
		blockAlign = aFormatChunk.blockAlign;
		bitsPerSample = aFormatChunk.bitsPerSample;
		numberOfAdditionalBytes = aFormatChunk.numberOfAdditionalBytes;
		if (numberOfAdditionalBytes > 0) {
			additionalBytes = new byte[numberOfAdditionalBytes];
			for (int i = 0; i < numberOfAdditionalBytes; i++) {
				additionalBytes[i] = aFormatChunk.additionalBytes[i];
			}
		}
		skippedBytes = 0;
	}
	
	/**
	 * Returns a clone of this FormatChunk instance.
	 */
	public Object clone() {
		Object obj;
		
		obj = super.clone();
		((FormatChunk)obj).skippedBytes = 0;

		return obj;
    }
	
	/**
	 * Returns the chunk ID.
	 */
	public int getChunkID() {
		return ID;
	}

	/**
	 * Returns the size of the chunk.
	 */
	public long getChunkSize() {
		//System.out.println("FormatChunk.getChunkSize()");
		chunkSize = 16;
		if (numberOfAdditionalBytes > 0) {
			chunkSize += (numberOfAdditionalBytes + 2);
		}
		chunkSize += skippedBytes;
		//System.out.println("chunkSize = " + chunkSize);
		return chunkSize;
	}

	/**
	 * Returns the format tag.
	 */	
	public short getFormatTag() {
		return formatTag;
	}
	
	public enum FormatTagDescription
	{
		WAVE_FORMAT_UNKNOWN (0x0000, "WAVE_FORMAT_UNKNOWN"),
		PCM (0X0001, "PCM"),
		WAVE_FORMAT_ADPCM (0X0002, "WAVE_FORMAT_ADPCM"),
		WAVE_FORMAT_IBM_CVSD (0x0005, "WAVE_FORMAT_IBM_CVSD"),
		WAVE_FORMAT_ALAW (0x0006, "WAVE_FORMAT_ALAW"),
		WAVE_FORMAT_MULAW (0x0007, "WAVE_FORMAT_MULAW"),
		WAVE_FORMAT_OKI_ADPCM (0x0010, "WAVE_FORMAT_OKI_ADPCM"),
		WAVE_FORMAT_DVI_ADPCM (0x0011, "WAVE_FORMAT_DVI_ADPCM"),
		WAVE_FORMAT_MEDIASPACE_ADPCM (0x0012, "WAVE_FORMAT_MEDIASPACE_ADPCM"),
		WAVE_FORMAT_SIERRA_ADPCM (0x0013, "WAVE_FORMAT_SIERRA_ADPCM"),
		WAVE_FORMAT_G723_ADPCM (0x0014, "WAVE_FORMAT_G723_ADPCM"),
		WAVE_FORMAT_DIGISTD (0x0015, "WAVE_FORMAT_DIGISTD"),
		WAVE_FORMAT_DIGIFIX (0x0016, "WAVE_FORMAT_DIGIFIX"),
		WAVE_FORMAT_DIALOGIC_OKI_ADPCM (0x0017, "WAVE_FORMAT_DIALOGIC_OKI_ADPCM"),
		WAVE_FORMAT_YAMAHA_ADPCM (0x0020, "WAVE_FORMAT_YAMAHA_ADPCM"),
		WAVE_FORMAT_SONARC (0x0021, "WAVE_FORMAT_SONARC"),
		WAVE_FORMAT_DSPGROUP_TRUESPEECH (0x0022, "WAVE_FORMAT_DSPGROUP_TRUESPEECH"),
		WAVE_FORMAT_ECHOSC1 (0x0023, "WAVE_FORMAT_ECHOSC1"),
		WAVE_FORMAT_AUDIOFILE_AF36 (0x0024, "WAVE_FORMAT_AUDIOFILE_AF36"),
		WAVE_FORMAT_APTX (0x0025, "WAVE_FORMAT_APTX"),
		WAVE_FORMAT_AUDIOFILE_AF10 (0x0026, "WAVE_FORMAT_AUDIOFILE_AF10"),
		WAVE_FORMAT_DOLBY_AC2 (0x0030, "WAVE_FORMAT_DOLBY_AC2"),
		WAVE_FORMAT_GSM610 (0x0031, "WAVE_FORMAT_GSM610"),
		WAVE_FORMAT_ANTEX_ADPCME (0x0033, "WAVE_FORMAT_ANTEX_ADPCME"),
		WAVE_FORMAT_CONTROL_RES_VQLPC (0x0034, "WAVE_FORMAT_CONTROL_RES_VQLPC"),
		WAVE_FORMAT_DIGIREAL (0x0035, "WAVE_FORMAT_DIGIREAL"),
		WAVE_FORMAT_DIGIADPCM (0x0036, "WAVE_FORMAT_DIGIADPCM"),
		WAVE_FORMAT_CONTROL_RES_CR10 (0x0037, "WAVE_FORMAT_CONTROL_RES_CR10"),
		WAVE_FORMAT_NMS_VBXADPCM (0x0038, "WAVE_FORMAT_NMS_VBXADPCM"),
		WAVE_FORMAT_G721_ADPCM (0x0040, "WAVE_FORMAT_G721_ADPCM"),
		WAVE_FORMAT_MPEG (0x0050, "WAVE_FORMAT_MPEG"),
		WAVE_FORMAT_CREATIVE_ADPCM (0x0200, "WAVE_FORMAT_CREATIVE_ADPCM"),
		WAVE_FORMAT_CREATIVE_FASTSPEECH8 (0x0202, "WAVE_FORMAT_CREATIVE_FASTSPEECH8"),
		WAVE_FORMAT_CREATIVE_FASTSPEECH10 (0x0203, "WAVE_FORMAT_CREATIVE_FASTSPEECH10"),
		WAVE_FORMAT_FM_TOWNS_SND (0x0300, "WAVE_FORMAT_FM_TOWNS_SND"),
		WAVE_FORMAT_OLIGSM (0x1000, "WAVE_FORMAT_OLIGSM"),
		WAVE_FORMAT_OLIADPCM (0x1001, "WAVE_FORMAT_OLIADPCM"),
		WAVE_FORMAT_OLICELP (0x1002, "WAVE_FORMAT_OLICELP"),
		WAVE_FORMAT_OLISBC (0x1003, "WAVE_FORMAT_OLISBC"),
		WAVE_FORMAT_OLIOPR (0x1004, "WAVE_FORMAT_OLIOPR");
		
		private final int _lookup_code;
		private final String _description;
		
		FormatTagDescription (int lookupCode, String description)
		{
			_lookup_code = lookupCode;
			_description = description;
		}
		
		public int getLookupCode()
		{
			return _lookup_code;
		}
		
		public String getDescriptionString()
		{
			return _description;
		}
		
		public static FormatTagDescription getFormatTagDescription (int lookupCode)
		{
			for (FormatTagDescription tst : EnumSet.range(FormatTagDescription.WAVE_FORMAT_UNKNOWN, FormatTagDescription.WAVE_FORMAT_ADPCM))
			{
				if (tst.getLookupCode() == (lookupCode))
				{
					return tst;
				}
			}
			return null;
		}
	}

	/**
	 * Sets the format tag.
	 *
	 * @param aShort The format tag.
	 */	
	public void setFormatTag(short aShort) {
		formatTag = aShort;
	}
	
	/**
	 * Returns the number of channels.
	 */
	public int getNumberOfChannels() {
		return channels;
	}
	
	/**
	 * Sets the number of channels.
	 *
	 * @param anInt The number of channels.
	 */
	public void setNumberOfChannels(int anInt) {
		channels = anInt;
	}
	
	/**
	 * Returns the number of samples per second.
	 */
	public long getSamplesPerSecond() {
		return samplesPerSecond;
	}
	
	/**
	 * Sets the number of samples per second.
	 */
	public void setSamplesPerSecond(long aLong) {
		samplesPerSecond = aLong;
	}
	
	/**
	 * Returns the average bytes per second.
	 */
	public long getAverageBytesPerSecond() {
		return avgBytesPerSecond;
	}
	
	/**
	 * Sets the average bytes per second.
	 *
	 * @param aLong The average bytes per second.
	 */
	public void setAverageBytesPerSecond(long aLong) {
		avgBytesPerSecond = aLong;
	}
	
	/**
	 * Returns the block align.
	 */
	public int getBlockAlign() {
		return blockAlign;
	}
	
	/**
	 * Sets the block align to the specified value.
	 *
	 * @param anInt The block align.
	 */
	public void setBlockAlign(int anInt) {
		blockAlign = anInt;
	}
	
	/**
	 * Returns the number of bits per sample.
	 */
	public int getBitsPerSample() {
		return bitsPerSample;
	}
	
	/**
	 * Sets the number of bits per sample.
	 *
	 * @param anInt The number of bits per sample.
	 */
	public void setBitsPerSamples(int anInt) {
		bitsPerSample = anInt;
	}
	
	/**
	 * Returns any additional bytes in the chunk.
	 */
	public byte[] getAdditionalBytes() {
		return additionalBytes;
	}
	
	/**
	 * Sets any additional bytes in the chunk.
	 *
	 * @param bytes An array of additional bytes.
	 */
	public void setAdditionalBytes(byte [] bytes) {
		additionalBytes = bytes;
		numberOfAdditionalBytes = additionalBytes.length;
	}
	
	/**
	 * Reads the format chunk from the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 * @see RIFFRandomAccessFile
	 */
	
	public long readFromFile(RIFFRandomAccessFile file) throws IOException {
		long bytesRead = 0;
		
		super.readFromFile(file);
		formatTag = file.readNUM16(); // 2 bytes
		bytesRead += 2;
		channels = file.readUNUM16(); // 2 bytes
		bytesRead += 2;
		samplesPerSecond = file.readUNUM32(); // 4 bytes
		bytesRead += 4;
		avgBytesPerSecond = file.readUNUM32(); // 4 bytes
		bytesRead += 4;
		blockAlign = file.readUNUM16(); // 2 bytes
		bytesRead += 2;
		bitsPerSample = file.readUNUM16(); // 2 bytes
		bytesRead += 2;
		if (formatTag == 1) {
			// No Compression
			// There should be no additional bytes in an uncompressed file so if they are present, ignore them.
			if (chunkSize > 16) {				
				skippedBytes = (int)chunkSize - 16;
				file.skipBytes(skippedBytes);
				bytesRead = chunkSize;
			}
		}
		else {
			// Compression
			numberOfAdditionalBytes = file.readUNUM16();
			bytesRead = bytesRead + 2;
			additionalBytes = new byte[numberOfAdditionalBytes];
			file.read(additionalBytes, 0, numberOfAdditionalBytes);
			bytesRead = bytesRead + numberOfAdditionalBytes;
		}
		return bytesRead;
	}

	/**
	 * Writes the format chunk to the specified RIFFRandomAccessFile.
	 *
	 * @param file A RIFFRandomAccessFile instance.
	 * @throws IOException
	 * @see RIFFRandomAccessFile
	 */	
	public void writeToFile(RIFFRandomAccessFile file) throws IOException {
		int bytesWritten = 0;
		
		//System.out.println(getClass() + ".writeToFile(" + file + ")");
		file.writeID(getChunkID());
		bytesWritten += 4;
		file.writeUNUM32(getChunkSize());
		bytesWritten += 4;
		file.writeNUM16(formatTag);
		bytesWritten += 2;
		file.writeUNUM16(channels);
		bytesWritten += 2;
		file.writeUNUM32(samplesPerSecond);
		bytesWritten += 4;
		file.writeUNUM32(avgBytesPerSecond);
		bytesWritten += 4;
		file.writeUNUM16(blockAlign);
		bytesWritten += 2;
		file.writeUNUM16(bitsPerSample);
		bytesWritten += 2;
		if (formatTag == 1) {
			// No Compression
			/*
			// Hack for malformed files...
			for (int i = 0; i < skippedBytes; i++) {
				file.write(0);
			}
			bytesWritten += skippedBytes;
			 */
		}
		else {
			// Compression
			file.writeUNUM16(numberOfAdditionalBytes);
			bytesWritten += 2;
			file.write(additionalBytes);
			bytesWritten += additionalBytes.length;
		}
		if ((bytesWritten % 2) != 0) {
			file.write(0);  // pad byte to make chunk even
			bytesWritten += 1;		
		}			
		//System.out.println("bytesWritten = " + bytesWritten);
	}
	
	/**
	 * Dumps debugging information to the console.
	 */
	public void dump() {
		System.out.println("id = 0x" + Integer.toHexString(chunkID) + " (" + RIFFFile.stringForID(chunkID) + ")");
		System.out.println("size = " + getChunkSize());
		System.out.println("formatTag = " + formatTag);
		System.out.println("channels = " + channels);
		System.out.println("samplesPerSecond = " + samplesPerSecond);
		System.out.println("avgBytesPerSecond = " + avgBytesPerSecond);
		System.out.println("blockAlign = " + blockAlign);
		System.out.println("bitsPerSample = " + bitsPerSample);
		System.out.println("numberOfAdditionalBytes = " + numberOfAdditionalBytes);
		System.out.print("additionalBytes = ");
		for (int i = 0; i < numberOfAdditionalBytes; i++) {
			System.out.print(Integer.toHexString(additionalBytes[i]));
		}
		System.out.println("");
	}
}
