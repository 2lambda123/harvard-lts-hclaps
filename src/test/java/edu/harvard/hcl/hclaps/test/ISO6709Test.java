//
//  ISO6709Test.java
//  hclaps
//
//  Created by Robert La Ferla on 5/18/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.test;

import junit.framework.TestCase;
import edu.harvard.hcl.hclaps.umid.*;

public class ISO6709Test extends TestCase  {

	public void testCoords() {
		ISO6709 c1, c2;
		byte [] bytes;
		
		c1 = new ISO6709();
		c1.setAltitude(12345678);
		c1.setLongitude(new DMS(179.1, false));
		c1.setLatitude(new DMS(90.0, true));
		c1.dump();
		System.out.println("");
		bytes = c1.getAsBytes();
		c2 = new ISO6709(bytes);
		c2.dump();
	
	}
	
}
