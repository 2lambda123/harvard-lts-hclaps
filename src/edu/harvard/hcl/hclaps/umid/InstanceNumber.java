//
//  InstanceNumber.java
//  hclaps
//
//  Created by Robert La Ferla on 1/23/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

import edu.harvard.hcl.hclaps.util.*;

/**
 * @author Robert La Ferla
 */

public class InstanceNumber {
	final static byte CREATION_METHOD_NONE = (byte)0x00;
	final static byte CREATION_METHOD_LOCAL_REGISTRATION = (byte)0x01;
	final static byte CREATION_METHOD_24_BIT_SEEDED_PRS = (byte)0x02;

	byte[] value = new byte[3];  // 3 byte value

	public InstanceNumber() {
	}
	
	public byte[] getAsBytes() {
		return value;
	}
	
	public void dump() {
		System.out.println("*** Instance Number (NONE) ***");
		for (int i = 0; i < 3; i++) {
			System.out.print(ByteConvertor.hexForByte(value[i]));
		}
		System.out.println("");
	}
}
