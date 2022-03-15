//
//  BasicUMID.java
//  BWAVParser
//
//  Created by Robert La Ferla on 1/23/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

import edu.harvard.hcl.hclaps.util.*;

/**
 * @author Robert La Ferla
 */

public class BasicUMID {
	UniversalLabel ul;
	byte length;
	InstanceNumber in;
	MaterialNumber mn;

	public BasicUMID() {
		ul = new UniversalLabel();
		in = new InstanceNumber();
		mn = new MaterialNumber();
	}
	
	public BasicUMID(byte[] umid) throws InvalidArgumentException {
		if (umid.length != 32) {
			throw new InvalidArgumentException("Basic UMID length needs to be 32 bytes long.");
		}
		else {
			ul = new UniversalLabel();
			ul.objectIdentifier = umid[0];
			ul.labelSize = umid[1];
			ul.designation1 = umid[2];
			ul.designation2 = umid[3];
			ul.registryCategories = umid[4];
			ul.specificCategory = umid[5];
			ul.structure = umid[6];
			ul.versionNumber = umid[7];
			ul.identifiersAndLocators = umid[8];
			ul.globallyUniqueIdentifiers = umid[9];
			ul.umidType = umid[10];
			ul.numberCreationMethod = umid[11];
			
			length = umid[12];

            switch ((ul.numberCreationMethod & 0x0f)) {
				case 0:  // No defined method
					in = new InstanceNumber();
					break;
				case 1:  // Local Registration
					in = new LocalRegistrationInstanceNumber();
					break;
				case 2:  // 24-bit seeded PRS generator
					in = new PRS24BitSeededInstanceNumber();
					break;
                default:
                    in = new InstanceNumber();
                    break;
			}
			in.value[0] = umid[13];
			in.value[1] = umid[14];
			in.value[2] = umid[15];
			
			switch ((ul.numberCreationMethod & 0xf0) >> 4) {
				case 0:  // No defined method
					mn = new MaterialNumber();
					break;
				case 1: // SMPTE method
					mn = new SMPTEMaterialNumber();
					break;
				case 2: // As per ISO/IEC 11578 Annex A
					mn = new ISOIEC11578AMaterialNumber();
					break;
				default: // 3-15 are reserved but not defined
					mn = new MaterialNumber();
					break;
			}
			for (int i = 0; i < 16; i++) {
				mn.value[i] = umid[16 + i];
			}
		}
	}
	
	public byte[] getAsBytes() {
		byte[] basic_umid = new byte[32];
		byte[] array;
		
		// Universal Label
		basic_umid[0] = ul.objectIdentifier ;
		basic_umid[1] = ul.labelSize;
		basic_umid[2] = ul.designation1;
		basic_umid[3] = ul.designation2;
		basic_umid[4] = ul.registryCategories;
		basic_umid[5] = ul.specificCategory;
		basic_umid[6] = ul.structure;
		basic_umid[7] = ul.versionNumber;
		basic_umid[8] = ul.identifiersAndLocators;
		basic_umid[9] = ul.globallyUniqueIdentifiers;
		basic_umid[10] = ul.umidType;
		basic_umid[11] = ul.numberCreationMethod;
		
		// Length
		basic_umid[12] = length;

		// Instance Number
		array = in.getAsBytes();
		for (int i = 0; i < 3; i++) {
			basic_umid[13+i] = array[i];
		}
		
		// Material Number
		array = mn.getAsBytes();
		for (int i = 0; i < 16; i++) {
			basic_umid[16+i] = array[i];
		}
		
		return basic_umid;
	}
	
	public void dump() {
		System.out.println("*** Basic UMID ***");
		ul.dump();
		System.out.println("length = " + ByteConvertor.hexForByte(length));
		in.dump();
		mn.dump();
	}
	
}
