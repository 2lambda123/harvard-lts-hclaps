//
//  InvalidArgumentException.java
//  BWAVParser
//
//  Created by Robert La Ferla on 1/24/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

/**
 * @author Robert La Ferla
 */

public class InvalidArgumentException extends Exception {
	public InvalidArgumentException(String str) {
		super(str);
	}
}
