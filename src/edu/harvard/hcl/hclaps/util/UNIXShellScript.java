//
//  UNIXShellScript.java
//  hclaps
//
//  Created by Robert on 11/14/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

/**
 * @author Robert La Ferla
 */

public class UNIXShellScript extends Script {

	/**
	 * This constructor creates a new UNIXShellScript with "#!/bin/sh" as a header. 
	 */
	public UNIXShellScript() {
		script = "#!/bin/sh\n";
	}
	
}
