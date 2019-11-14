//
//  BExtChunkTest.java
//  hclaps
//
//  Created by Robert La Ferla on 5/16/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.test;

import java.io.*;
import junit.framework.TestCase;
import edu.harvard.hcl.hclaps.bwav.*;
import edu.harvard.hcl.hclaps.bwav.chunks.*;

public class BExtChunkTest extends TestCase {

	public void testFile() {
		BExtChunk bc;
		
		bc = new BExtChunk(new File("/Users/rlaferla/bext.xml"));
		bc.dump();
	}
	
}
