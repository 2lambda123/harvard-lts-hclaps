//
//  UniversalLabel.java
//  hclaps
//
//  Created by Robert La Ferla on 1/23/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

/**
 * @author Robert La Ferla
 */

public class UniversalLabel {
	final static byte UMID_TYPE_PICTURE_MATERIAL = 0x01;
	final static byte UMID_TYPE_AUDIO_MATERIAL = 0x02;
	final static byte UMID_TYPE_DATA_MATERIAL = 0x03;
	final static byte UMID_TYPE_OTHER_MATERIAL = 0x04;
	
	final static byte DESIGNATION_ISO = 0x2b;
	final static byte DESIGNATION_SMPTE = 0x34;

	final static byte REGISTRY_CATEGORIES_DICTIONARIES = 0x01;
	
	final static byte SPECIFIC_CATEGORY_METADATA_DICTIONARIES = 0x01;
	
	final static byte STRUCTURE_DICTIONARY_STANDARD = 0x01;
	
	byte objectIdentifier = 0x06;
	byte labelSize = 0x0a;
	byte designation1 = DESIGNATION_ISO; // ISO
	byte designation2 = DESIGNATION_SMPTE; // SMPTE
	byte registryCategories = REGISTRY_CATEGORIES_DICTIONARIES; // Dictionaries
	byte specificCategory = SPECIFIC_CATEGORY_METADATA_DICTIONARIES; // Metadata dictionaries
	byte structure = STRUCTURE_DICTIONARY_STANDARD; // Dictionary standard
	byte versionNumber = 0x01;
	byte identifiersAndLocators = 0x01;
	byte globallyUniqueIdentifiers = 0x01;
	byte umidType = UMID_TYPE_AUDIO_MATERIAL;
	byte numberCreationMethod = MaterialNumber.CREATION_METHOD_NONE|InstanceNumber.CREATION_METHOD_NONE;
	
	public void dump() {
		System.out.println("*** Universal Label ***");
		System.out.println("objectIdentifier = " + objectIdentifier);
		System.out.println("labelSize = " + labelSize);
		System.out.println("designation1 = " + designation1);
		System.out.println("designation2 = " + designation2);
		System.out.println("registryCategories = " + registryCategories);
		System.out.println("specificCategory = " + specificCategory);
		System.out.println("structure = " + structure);
		System.out.println("versionNumber = " + versionNumber);
		System.out.println("identifiersAndLocators = " + identifiersAndLocators);
		System.out.println("globallyUniqueIdentifiers = " + globallyUniqueIdentifiers);
		System.out.println("umidType = " + umidType);
		System.out.println("numberCreationMethod = " + numberCreationMethod);
	}
}
