//
//  ISOIEC11578AMaterialNumber.java
//  hclaps
//
//  Created by Robert La Ferla on 1/24/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

import edu.harvard.hcl.hclaps.util.*;

/**
 * @author Robert La Ferla
 */

public class ISOIEC11578AMaterialNumber extends MaterialNumber {

	public int getVersion() throws Exception {
		int version;
		
		version = (value[7] & (byte)0x0f);  // last 4-bits of 64-bit space (8 bytes)

		if (version == (byte)0) {
			throw new Exception("ISO/IEC 11578 material number UUID version is invalid.");
		}
		return version;
	}
	
	public byte[] getTimeSnap() {
		int i;
		byte[] val = new byte[8];
		
		for (i = 0; i < 7; i++) {
			val[i] = value[i];
		}
		val[i] = (byte)((value[i] & 0xf0) >> 4);  // only 4-bits of the last byte
		return val;
	}
	
	public byte getVariant() {
		return (byte)((value[8] & 0xc0) >> 6);  // big-endian 2 most significant bits
	}

	public byte getRandom() {
		return (byte)((value[8] & 0x3F) + (byte)(value[9] * (byte)256));  // big-endian 14-bit
	}
	
	public byte[] getMachineNode() {
		byte[] val = new byte[6];
		
		for (int i = 0; i < 6; i++) {
			val[i] = value[10+i];
		}		
		return val;		
	}	
	
	public void dump() {
		byte[] val;
		
		System.out.println("*** Material Number (ISO/IEC 11578 A) ***");
		
		try {
			System.out.println("Version = " + getVersion());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		val = getTimeSnap();
		System.out.print("Time snap = ");
		for (int i = 0; i < val.length; i++) {
			System.out.print(ByteConvertor.hexForByte(val[i]));
		}
		System.out.println("");

		System.out.println("Variant = " + getVariant());
		
		System.out.println("Random = " + getRandom());
		
		val = getMachineNode();
		System.out.print("Machine node = ");
		for (int i = 0; i < 6; i++) {
			System.out.print(ByteConvertor.hexForByte(val[i]));
			if (i < 5) {
				System.out.print(":");
			}
		}
		System.out.println("");
	}
		
		
}
