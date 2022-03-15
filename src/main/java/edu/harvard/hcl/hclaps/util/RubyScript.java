//
//  RubyScript.java
//  hclaps
//
//  Created by Robert La Ferla on 6/19/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

/**
 * @author Robert La Ferla
 */

public class RubyScript extends Script {
	
	/**
	 * This constructor creates a new RubyScript with "#!/usr/bin/ruby -w" as a header. 
	 */
	public RubyScript() {
		script = "#!/usr/bin/ruby -w\n";
	}
	
	/**
	 * This adds a system call to the specified string to the Ruby script.
     * @param aStr A string.
	 */
	public void addCommand(String aStr) {
		//script = script + "puts \"" + aStr + "\"\n";
		script = script + "system(\"" + aStr + "\")" + '\n';
	}
		
}
