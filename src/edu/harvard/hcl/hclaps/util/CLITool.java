//
//  CLITool.java
//  hclaps
//
//  Created by Robert La Ferla on 4/13/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import java.util.*;
import java.io.*;

/**
 * CLITool provides core functionality such as option parsing for any command-line tool.
 *
 * @see CLIParser
 * @author Robert La Ferla
 */

public class CLITool {
	String version;
	String name;
	public CLIParser options;
	
	/**
	 * This constructor creates a new CLITool instance. 
	 */
	public CLITool() {
		options = new CLIParser();
		options.setTool(this);
	}
	
	/**
	 * This sets the version of the CLI tool.
     * @param aStr A string.
	 */
	
	public void setVersion(String aStr) {
		version = aStr;
	}
	
	/**
	 * Returns the version of the CLI tool.
	 * @return The version of the CLI tool.
	 */
	
	public String getVersion() {
		return version;
	}
	
	/**
	 * This sets the name of the CLI tool.
     * @param aStr A string.
	 */
	public void setName(String aStr) {
		name = aStr;
	}
	
	/**
	 * Returns the name of the CLI tool.
	 * @return The name of the CLI tool.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the argument parser for this tool.
	 * @return the argument parser for this tool.
	 */
	public CLIParser argParser() {
		return options;
	}
	
	/**
	 * This escapes characters in a path twice.
     * @param aPath A path.
	 * @return An escaped path.
	 * @see edu.harvard.hcl.hclaps.util.CLITool#reescape
	 * @see edu.harvard.hcl.hclaps.util.CLITool#escapePath
	 */
	public static String doubleEscape(String aPath) {
		return reescape(escapePath(aPath));
	}
	
	/**
	 * This escapes escaped characters in a path again.
     * @param aPath A path.
	 * @return An escaped path.
	 */
	public static String reescape(String aPath) {
		String newPath = "";
		int len;
		
		//System.out.println("reescape(" + aPath + ")");
		len = aPath.length();
		for (int i = 0; i < len; i++) {
			String str;
			char c;
			
			c = aPath.charAt(i);
			if (c == '\\') {
				str = "\\\\";
			}
			else {
				str = "" + c;
			}
			newPath = newPath + str;
		}
		
		//System.out.println("newPath = " + newPath);
		return newPath;
	}
	
	/**
	 * This escapes special characters in a path such as spaces, curly braces and parentheses.
     * @param aPath A path.
	 * @return An escaped path.
	 */
	public static String escapePath(String aPath) {
		String newPath = "";
		
		if (isRunningOnWindows()) {
			newPath = aPath;
		}
		else {
			int len;
			
			//System.out.println("escapePath(" + aPath + ")");
			len = aPath.length();
			for (int i = 0; i < len; i++) {
				String str;
				char c;
				
				c = aPath.charAt(i);
				if (c == ' ') {
					str = "\\ ";
				}
				else if (c == '{') {
					str = "\\{";
				}
				else if (c == '}') {
					str = "\\}";
				}
				else if (c == '(') {
					str = "\\(";
				}
				else if (c == ')') {
					str = "\\)";
				}
				else {
					str = "" + c;
				}
				newPath = newPath + str;
			}
		}
		//System.out.println("newPath = " + newPath);
		return newPath;
	}

	/**
	 * This left pads integers less than 10 with a zero.
     * @param num An integer.
	 * @return A padded number.
	 */
	public static String pad(int num) {
		String str = "";
		
		if (num <  10) {
			str = "0";
		}
		str = str + num;
		return str;
	}
	
	public static boolean isRunningOnWindows() {
		return System.getProperty("os.name").startsWith("Windows");
	}
	
	public static int runSubProcess(String cmd) {
		int retval = -1;
		
		try {
			Runtime rt;
			Process p;
			rt = Runtime.getRuntime();
			
			System.out.println("Executing " + cmd);
			p = rt.exec(cmd);
			retval = p.waitFor();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return retval;
	}	
	
	public static String getOutputFromSubProcess(String[] cmdargs) {
		String str = null;
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			Runtime rt;
			Process p;
			String line;
			
			rt = Runtime.getRuntime();
			
			// System.out.println("Executing " + cmdargs);
			p = rt.exec(cmdargs);
			is = p.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			str = new String();
			do {
				line = br.readLine();
				if (line != null) {
					str = str + line;
				}
			} while (line != null);
			
			try {
                if (p.waitFor() != 0) {
                    System.err.println("Could not execute subprocess.");
                }
            }
            catch (InterruptedException ie) {
				ie.printStackTrace();
            }
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (IOException ioex) {
					System.err.println("Cannot close buffered reader.");
				}
			}
			if (isr != null) {
				try {
					isr.close();
				}
				catch (IOException ioex) {
					System.err.println("Cannot close input stream reader.");
				}
			}
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException ioex) {
					System.err.println("Cannot close buffered input stream.");
				}
			}
		}
		return str;
	}	
		
}
