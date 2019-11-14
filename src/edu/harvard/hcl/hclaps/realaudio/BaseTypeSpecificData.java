//
//  BaseTypeSpecificData.java
//  hclaps
//
//  Created by David Ackerman on 10/15/06.
//  Copyright 2006 David Ackerman. All rights reserved.
//


package edu.harvard.hcl.hclaps.realaudio;

import java.io.*;
import java.util.*;
import edu.harvard.hcl.hclaps.bwav.IFFRandomAccessFile;

public abstract class BaseTypeSpecificData implements Cloneable {
	
	
	public BaseTypeSpecificData ()
	{
		
	}
	
	
	public Object clone ()
	{
		try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("This should not occur since we implement Cloneable");
        }
    }

	
		
	
	public long readFromFile(IFFRandomAccessFile file) throws IOException
	{
		long bytesRead = 0;
		
		return bytesRead;
		
	}
	
	public void dump() {
	
	}
	
	
}

