//
//  PRS24BitSeedednstanceNumber.java
//  hclaps
//
//  Created by Robert La Ferla on 1/26/06.
//  Copyright (c) 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

import edu.harvard.hcl.hclaps.util.ByteConvertor;

/**
 * @author Robert La Ferla
 */

public class PRS24BitSeededInstanceNumber extends InstanceNumber {
	public void dump() {
		System.out.println("*** Instance Number (24-bit Seeded PRS Generator) ***");
		for (int i = 0; i < 3; i++) {
			System.out.print(ByteConvertor.hexForByte(value[i]));
		}
		System.out.println("");
	}
	
}
