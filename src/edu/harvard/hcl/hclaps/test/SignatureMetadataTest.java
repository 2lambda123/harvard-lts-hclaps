//
//  SignatureMetadataTest.java
//  hclaps
//
//  Created by Robert La Ferla on 5/8/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.test;

import junit.framework.TestCase;
import edu.harvard.hcl.hclaps.umid.*;

public class SignatureMetadataTest extends TestCase {

	public void testTimeDate() {
		SignatureMetadata sm;
		ISO6709 coords;
		
		sm = new SignatureMetadata();
		sm.getTimeDate();
		coords = sm.getSpatialCoordinates();
		coords.setAltitude(99999999);
		coords.setLongitude(new DMS(179.1, false));
		coords.setLatitude(new DMS(90.0, true));
		sm.setSpatialCoordinates(coords);
		sm.dump();
	}

}
