//
//  SMPTEMaterialNumber.java
//  hclaps
//
//  Created by Robert La Ferla on 1/24/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

/**
 * @author Robert La Ferla
 */

public class SMPTEMaterialNumber extends MaterialNumber {
	
	public short[] getTimeSnapCount() {
		short[] val = new short[4];
		
		for (int i = 0; i < 4; i++) {
			val[i] = value[i];
		}		
		return val;
	}
	
	public short[] getTimeSnapMJD() {
		short[] val = new short[4];
		
		for (int i = 0; i < 4; i++) {
			val[i] = value[4+i];
		}		
		return val;
	}
	
	public int getRandom() {
		int val;
		
		val = value[8] + (256 * value[9]);  // Big-endian
		return val;
	}
	
	public short[] getMachineNode() {
		short[] val = new short[6];
		
		for (int i = 0; i < 6; i++) {
			val[i] = value[10+i];
		}		
		return val;		
	}	
	
	public void dump() {
		short[] val;
		
		System.out.println("*** Material Number (SMPTE) ***");
		
		val = getTimeSnapCount();
		System.out.print("Time snap count = ");
		for (int i = 0; i < val.length; i++) {
			System.out.print(Integer.toHexString(val[i]));
		}
		System.out.println("");
		
		val = getTimeSnapMJD();
		System.out.print("Time snap MJD = ");
		for (int i = 0; i < val.length; i++) {
			System.out.print(Integer.toHexString(val[i]));
		}
		System.out.println("");

		System.out.println("Random = " + getRandom());
		
		val = getMachineNode();
		System.out.print("Machine node = ");
		for (int i = 0; i < 6; i++) {
			System.out.print(Integer.toHexString(val[i]));
			if (i < 5) {
				System.out.print(":");
			}
		}
		System.out.println("");

	}
}
