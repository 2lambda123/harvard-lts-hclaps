//
//  BExtChunkTest.java
//  hclaps
//
//  Created by Robert La Ferla on 5/16/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.test;

import java.io.File;

import edu.harvard.hcl.hclaps.bwav.chunks.BExtChunk;
import junit.framework.TestCase;

public class BExtChunkTest extends TestCase {

	public void testFile() {
		BExtChunk bc;
		
		bc = new BExtChunk(new File("src/test/resources/bext.xml"));
		bc.dump();
	}
	
}
