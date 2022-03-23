//
//  LogicalStreamTypeSpecificData.java
//  hclaps
//
//  Created by Kaylie Ackerman on 10/14/06.
//  Copyright 2006 Kaylie Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.IOException;
import java.util.ArrayList;

import edu.harvard.hcl.hclaps.bwav.IFFRandomAccessFile;

public class LogicalStreamTypeSpecificData extends BaseTypeSpecificData implements Cloneable {
	long size;
	int objectVersion;
	int numPhysicalStreams;
	int[] physicalStreamNumbers;
	long[] dataOffsets;	//stream report object has a num, a rule and an offset!
	int numRules;
	int[] ruleToPhysicalStreamNumberMap;
	int numProperties;
	ArrayList<NameValueProperty> properties;

	
	
	public LogicalStreamTypeSpecificData ()
	{

	}
	
	public LogicalStreamTypeSpecificData (LogicalStreamTypeSpecificData aLogicalStreamTypeSpecificData) {
		size = aLogicalStreamTypeSpecificData.size;
		objectVersion = aLogicalStreamTypeSpecificData.objectVersion;
		numPhysicalStreams = aLogicalStreamTypeSpecificData.numPhysicalStreams;
		for (int i = 0; i < numPhysicalStreams; i++)
		{
			physicalStreamNumbers[i] = aLogicalStreamTypeSpecificData.physicalStreamNumbers[i];
			dataOffsets[i] = aLogicalStreamTypeSpecificData.dataOffsets[i];
		}
		
		numRules = aLogicalStreamTypeSpecificData.numRules;
		for (int i = 0; i < numRules; i++)
		{
			ruleToPhysicalStreamNumberMap[i] = aLogicalStreamTypeSpecificData.ruleToPhysicalStreamNumberMap[i];
		}
		
		
		numProperties = aLogicalStreamTypeSpecificData.numProperties;
		properties = new ArrayList<NameValueProperty>();
		for (NameValueProperty rp: aLogicalStreamTypeSpecificData.properties)
		{
			properties.add((NameValueProperty)rp.clone());
		}
	}
	
	public Object clone () {
		LogicalStreamTypeSpecificData rval =  (LogicalStreamTypeSpecificData)super.clone();
		for (NameValueProperty rp: properties)
		{
			rval.properties.add((NameValueProperty)rp.clone());
		}
		return rval;
	}

	
	public long getSize ()
	{
		return size;
	}
	
	public int getObjectVersion ()
	{
		return objectVersion;
	}
	
	public int getNumPhysicalStreams ()
	{
		return numPhysicalStreams;
	}
	
	public int[] getPhysicalStreamNumbers ()
	{
		return physicalStreamNumbers;
	}

	public long[] getDataOffsets ()
	{
		return dataOffsets;
	}
	
	public int getNumRules ()
	{
		return numRules;
	}
	
	public int[] getRuleToPhysicalStreamNumberMap ()
	{
		return ruleToPhysicalStreamNumberMap;
	}
	
	public int getNumProperties ()
	{
		return numProperties;
	}
	
	public ArrayList<NameValueProperty> getProperties ()
	{
		return properties;
	}
	
	public String[] getValueForNamedProperty (String name)
	{
		
		for (NameValueProperty nvp: properties)
		{
			if (nvp.getName().equals(name))
			{
				return nvp.getValueData();
			}
		}
		return null;
	}

	
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		//super.readFromFile(file);
		
		size = file.readUNUM32();
		bytesRead += 4;
		
		objectVersion = file.readUNUM16();
		bytesRead += 2;
		
		if (objectVersion == 0)
		{
			numPhysicalStreams = file.readUNUM16();
			bytesRead += 2;
			
			physicalStreamNumbers = new int[numPhysicalStreams];
			for (int i = 0; i < numPhysicalStreams; i++)
			{
				physicalStreamNumbers[i] =  file.readUNUM16();
				bytesRead += 2;
			}
			
			dataOffsets = new long[numPhysicalStreams];
			for (int i = 0; i < numPhysicalStreams; i++)
			{
				dataOffsets[i] =  file.readUNUM32();
				bytesRead += 4;
			}
			
			numRules = file.readUNUM16();
			bytesRead += 2;
			
			ruleToPhysicalStreamNumberMap = new int[numRules];
			for (int i = 0; i < numRules; i++)
			{
				ruleToPhysicalStreamNumberMap[i] =  file.readUNUM16();
				bytesRead += 2;
			}
			
			numProperties = file.readUNUM16();
			bytesRead += 2;
			
			NameValueProperty prop;
			properties = new ArrayList<NameValueProperty>();
			for (int i = 0; i < numProperties; i++)
			{
					//System.out.println("READING RPOPERTY: " + i);
					prop = new NameValueProperty();
					prop.readFromFile(file);
					properties.add(prop);
			}
			
		}
		else
		{
			System.err.println("Unsupported version of Logical Stream Type Specific Data found. Skipping...");
			int remainingBytes = (int)(size - bytesRead);
			file.skipBytes(remainingBytes);
			bytesRead += remainingBytes;
		}
		
		return bytesRead;
		
	}
	
	public void dump() {
		System.out.println("size = " + size);
		System.out.println("objectVersion = " + objectVersion);
		System.out.println("numPhysicalStreams = " + numPhysicalStreams);
		
		System.out.print("physicalStreamNumbers = ");
		for (int i = 0; i < numPhysicalStreams; i++)
		{
			System.out.print(physicalStreamNumbers[i] + " ");
		}
		System.out.println("");
		
		System.out.print("dataOffsets = ");
		for (int i = 0; i < numPhysicalStreams; i++)
		{
			System.out.print(dataOffsets[i] + " ");
		}
		System.out.println("");
		
		System.out.println("numRules = " + numRules);
		
		System.out.print("ruleToPhysicalStreamNumberMap = ");
		for (int i = 0; i < numRules; i++)
		{
			System.out.print(ruleToPhysicalStreamNumberMap[i] + " ");
		}
		System.out.println("");
		
		System.out.println("numProperties = " + numProperties);
			
		for (NameValueProperty rp: properties)
		{
			rp.dump();
		}
		
		System.out.println("");
	}
	
	
}

