//
//  Script.java
//  hclaps
//
//  Created by Robert on 11/14/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import java.io.*;

/**
 * @author Robert La Ferla
 */

public class Script {
	String script = "";

	/**
	 * This adds a single line to the script.
     * @param aStr A string.
	 */
	public void addLine(String aStr) {
		script = script + aStr + '\n';
	}
	
	/**
	 * This saves the script to the specified file path.
     * @param path The path to save the script to.
	 */
	public void saveTo(String path) {
		try {
			File file;
			FileWriter fw; 
			BufferedWriter bw;
			
			file = new File(path);
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(script, 0, script.length());
			bw.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This dumps the script to standard output which is useful for debugging.
	 */
	public void dump() {
		System.out.println(script);
	}
	
	
}
