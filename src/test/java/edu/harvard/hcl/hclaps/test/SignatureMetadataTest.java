//
//  SignatureMetadataTest.java
//  hclaps
//
//  Created by Robert La Ferla on 5/8/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.test;

import edu.harvard.hcl.hclaps.umid.DMS;
import edu.harvard.hcl.hclaps.umid.ISO6709;
import edu.harvard.hcl.hclaps.umid.SignatureMetadata;
import junit.framework.TestCase;

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
