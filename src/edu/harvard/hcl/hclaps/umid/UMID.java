//
//  UMID.java
//  hclaps
//
//  Created by Robert La Ferla on 1/23/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

import java.io.*;
import edu.harvard.hcl.hclaps.bwav.*;
import edu.harvard.hcl.hclaps.util.*;
import org.jdom.input.*;
import org.jdom.*;

/**
 * UMID is a SMPTE Unique Material Identifier.
 *
 * @author Robert La Ferla
 */

public class UMID {
	BasicUMID bu = new BasicUMID();
	SignatureMetadata sm = null;

	public UMID() {
	}
	
	// This only generstes a UMID with user's preferences for signature metadata.
	// Other data is not created.
	public UMID(Element el) {
		String val;
		ISO6709 coords;
		
		//System.out.println("UMID(" + el + ")");
		sm = new SignatureMetadata();
		
		val = el.getChildText("countrycode");
		sm.setCountryCode(val);
		
		val = el.getChildText("orgcode");
		sm.setOrganizationCode(val);
		
		val = el.getChildText("user");
		sm.setUser(val);	
		
		val = el.getChildText("spatial");
		coords = new ISO6709(val);
		sm.setSpatialCoordinates(coords);
	}
	
	public UMID(File aFile) {
		//System.out.println("UMID(" + aFile + ")");
		try {
			SAXBuilder sb;
			Document doc;
			Element root;
			
			sb = new SAXBuilder();
			doc = sb.build(aFile);
			root = doc.getRootElement();			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public UMID(UMID um) {
		bu = um.getBasicUMID();
		sm = um.getSignatureMetadata();
	}
	
	public UMID(String str) throws InvalidArgumentException {
			this(ByteConvertor.bytesForHexString(str));
	}

	public UMID(byte[] umid) throws InvalidArgumentException {
		if (umid.length != 32 && umid.length != 64) {
			throw new InvalidArgumentException("UMID length needs to be 32 or 64 bytes long.");
		}
		else {
			byte[] basic_umid = new byte[32];

			for (int i = 0; i < 32; i++) {
				basic_umid[i] = umid[i];
				//System.out.println("basic_umid[" + i + "] = " + basic_umid[i]);
			}
			bu = new BasicUMID(basic_umid);
			if (umid.length == 64) {
				byte[] signature_metadata = new byte[32];
				
				for (int i = 0; i < 32; i++) {
					signature_metadata[i] = umid[32 + i];
					//System.out.println("signature_metadata[" + i + "] = " + signature_metadata[i]);
				}
				sm = new SignatureMetadata(signature_metadata);
			}
		}
	}
	
	public void setBasicUMID(BasicUMID aBasicUMID) {
		bu = aBasicUMID;
	}
	
	public BasicUMID getBasicUMID() {
		return bu;
	}
	
	public void setSignatureMetadata(SignatureMetadata aSignatureMetadata) {
		sm = aSignatureMetadata;
	}
	
	public SignatureMetadata getSignatureMetadata() {
		return sm;
	}
	public byte[] getAsBytes() {
		byte umid[] = new byte[64];
		byte[] basic_umid;
		
		basic_umid = bu.getAsBytes();
		for (int i = 0; i < 32; i++) {
			umid[i] = basic_umid[i];
		}
		if (sm != null) {
			byte[] signature_metadata;

			signature_metadata = sm.getAsBytes();
			for (int i = 0; i < 32; i++) {
				umid[i + 32] = signature_metadata[i];
			}
		}
		return umid;
	}
	
	public void dump() {
		byte umid[];
		
		System.out.println("*** UMID ***");
		umid = getAsBytes();

		for (int i = 0; i < 32; i++) {
			System.out.print(ByteConvertor.hexForByte(umid[i]));
		}
		if (sm != null) {
			for (int i = 0; i < 32; i++) {
				System.out.print(ByteConvertor.hexForByte(umid[i + 32]));
			}
		}
		System.out.println("");
		bu.dump();
		if (sm != null) {
			sm.dump();
		}
	}
}
