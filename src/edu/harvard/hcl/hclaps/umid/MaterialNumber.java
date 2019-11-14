//
//  MaterialNumber.java
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

public class MaterialNumber {
	final static byte CREATION_METHOD_NONE = (byte)0x00;
	final static byte CREATION_METHOD_SMPTE = (byte)0x10;
	final static byte CREATION_METHOD_ISO_IEC_11578_A = (byte)0x20;

	byte[] value = new byte[16];  // 16 byte value
	
	public MaterialNumber() {
	}
	
	public byte[] getAsBytes() {
		return value;
	}
	
	public void dump() {
		System.out.println("*** Material Number (NONE) ***");
		for (int i = 0; i < 16; i++) {
			System.out.print(ByteConvertor.hexForByte(value[i]));
		}
		System.out.println("");
	}
}
