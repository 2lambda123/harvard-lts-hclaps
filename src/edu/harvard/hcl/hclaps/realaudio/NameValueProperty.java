//
//  NameValueProperty.java
//  hclaps
//
//  Created by David Ackerman on 10/14/06.
//  Copyright 2006 David Ackerman. All rights reserved.
//

package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.IFFRandomAccessFile;

public class NameValueProperty implements Cloneable {
	long size;
	int objectVersion;
	String name;
	int type;
	int valueLength;
	String[] valueData;
	final static int U4_BYTE = 0;
	final static int SHORT_BUFFER = 1;
	final static int STRING_TYPE = 2;
	
	public NameValueProperty ()
	{

	}
	
	public NameValueProperty (NameValueProperty aNameValueProperty) {
		size = aNameValueProperty.size;
		objectVersion = aNameValueProperty.objectVersion;
		name = aNameValueProperty.name;
		type = aNameValueProperty.type;
		valueLength = aNameValueProperty.valueLength;
		for (int i = 0; i < aNameValueProperty.valueData.length; i++)
		{
			valueData[i] = aNameValueProperty.valueData[i];
		}
		
	}
	
	public Object clone () {
		try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("This should not occur since we implement Cloneable");
        }
    }

	
	public long getSize ()
	{
		return size;
	}
	
	public int getObjectVersion ()
	{
		return objectVersion;
	}
	
	public String getName ()
	{
		return name;
	}
	
	public int getType ()
	{
		return type;
	}

	public int getValueLength ()
	{
		return valueLength;
	}
	
	public String[] getValueData ()
	{
		return valueData;
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
			name = file.readPstring();
			bytesRead += (name.length() + 1);
			
			type = (int)file.readUNUM32();
			bytesRead += 4;
			
			valueLength = file.readUNUM16();
			bytesRead += 2;
			
			switch (type)
			{
				case U4_BYTE:
					long tmp = file.readUNUM32();
					valueData = new String[1];
					valueData[0] = String.valueOf(tmp);
					break;
				case SHORT_BUFFER:
					int numValues = file.readUNUM16();
					int t = -1;
					valueData = new String[numValues];
					for (int i = 0; i < numValues; i++)
					{
						t = file.readUNUM16();
						valueData[i] = String.valueOf(t);
					}
					break;
					
				case STRING_TYPE:				
					valueData = new String[1];
					valueData[0] = file.readString(valueLength);
					if (valueData[0].length() < (valueLength - 1)) //this shouldn't happen but just in case multiple nulls at the end of string
					{
						file.skipBytes((valueLength - 1) - valueData[0].length());
						System.out.println("Value length: " + valueLength + " valueData length: " + valueData[0].length());
					}
					break;
				default:
				    System.err.println("Unknown NameValueProperty type.");
			        break;
			}
			
			bytesRead += valueLength;
			
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
		System.out.println("name = " + name);
		System.out.println("type = " + type);
		System.out.println("value length = " + valueLength);
		
		for (int i = 0; i < valueData.length; i++)
		{
			System.out.print("value data = " + valueData[i] + " ");
		}
		
		
		System.out.println("");
	}
	
	
}

