//
//  CLIParser.java
//
//  Created by Robert La Ferla on 4/11/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import java.util.*;

/**
 * CLIParser parses command-line options passed into a CLITool.  It validates the arguments for proper form.  It also supports
 * dependencies amongst options so that if one option is specified, it may require that other options be present. 
 * e.g.  if you have a --output-file, you also need a --input-file.
 *
 * @author Robert La Ferla
 */

public class CLIParser {
	CLITool tool;
	ArrayList<String> preArgs = new ArrayList<String>();
	ArrayList<String> postArgs = new ArrayList<String>();
	//String appName = "";
	String preArgsDescription = "";
	String postArgsDescription = "";
	String optionsDescription = "";
	HashMap allPossibleOptions = new HashMap();
	TreeMap possibleOptions = new TreeMap();
	HashMap appOptions = new HashMap();
	boolean allowsPreArgs = false;
	boolean allowsPostArgs = false;
	
	/**
	 * This sets the tool associated with this parser.
     * @param aTool The CLITool associated with this parser.
	 */
	public void setTool(CLITool aTool) {
		tool = aTool;
	}
	
	/**
	 * Returns the tool associated with this parser.
     * @return The tool associated with this parser.
	 */
	public CLITool getTool() {
		return tool;
	}
	
	/**
	 * This sets whether there is a variable number of arguments before the first fixed argument.
     * @param aBool A boolean.
	 */
	public void setAllowsPreArgs(boolean aBool) {
		allowsPreArgs = aBool;
	}
	
	/**
	 * Returns whether there is a variable number of arguments before the first fixed argument.
     * @return Whether there is a variable number of arguments before the first fixed argument..
	 */
	public boolean getAllowsPreArgs() {
		return allowsPreArgs;
	}
	
	/**
	 * This sets whether there is a variable number of arguments after the last fixed argument.
     * @param aBool A boolean.
	 */
	public void setAllowsPostArgs(boolean aBool) {
		allowsPostArgs = aBool;
	}
	
	/**
	 * Returns whether there is a variable number of arguments after the last fixed argument.
     * @return Whether there is a variable number of arguments after the last fixed argument..
	 */
	public boolean getAllowsPostArgs() {
		return allowsPostArgs;
	}	
	
	/**
	 * This sets the description of the variable arguments before the first fixed argument.
     * @param aStr The description of the variable arguments before the first fixed argument.
	 */
	public void setPreArgsDescription(String aStr) {
		preArgsDescription = aStr;
	}
	
	/**
	 * Returns the description of the variable arguments before the first fixed argument.
     * @return The description of the variable arguments before the first fixed argument.
	 */
	public String getPreArgsDescription() {
		return preArgsDescription;
	}
	
	/**
	 * This sets the description of the variable arguments after the last fixed argument.
     * @param aStr The description of the variable arguments after the last fixed argument.
	 */
	public void setPostArgsDescription(String aStr) {
		postArgsDescription = aStr;
	}
	
	/**
	 * Returns the description of the variable arguments after the last fixed argument.
     * @return The description of the variable arguments after the last fixed argument.
	 */	
	public String getPostArgsDescription() {
		return postArgsDescription;
	}
	
	/**
	 * This sets the description of the options.
     * @param aStr The description of the options.
	 */
	public void setOptionsDescription(String aStr) {
		optionsDescription = aStr;
	}
	
	/**
	 * Returns the description of the options.
     * @return The description of the options.
	 */	
	public String getOptionsDescription() {
		return optionsDescription;
	}
	
	/**
	 * Returns the arguments before the first fixed argument.
     * @return An ArrayList of the arguments before the first fixed argument.
	 */	
	public ArrayList<String> getPreArgs() {
		return preArgs;
	}
	
	/**
	 * Returns the arguments after the last fixed argument.
     * @return An ArrayList of the arguments after the last fixed argument.
	 */	
	public ArrayList<String> getPostArgs() {
		return postArgs;
	}
	
	/**
	 * Parses the arguments in the array usually argv[].
     * @param array An array of strings usually the argument vector argv[].
	 */
	public void parseArgs(String array[]) {
		if (array.length > 0) {
			Iterator it;
			ArrayList<String> al = null;
			CLIOption co = null;

			// Parse pre-args if enabled
			if (allowsPreArgs == true) {
				for (int i = 0; i < array.length; i++) {
					if (!allPossibleOptions.containsKey(array[i])) {
						preArgs.add(array[i]);
					}
					else {
						break;
					}
				}
			}
			
			// Parse options
			for (int i = 0; i < array.length; i++) {
				String key;
				
				key = array[i];
				if (allPossibleOptions.containsKey(key)) {
					co = (CLIOption)allPossibleOptions.get(key);

					if (!appOptions.containsKey(key)) {
						al = new ArrayList<String>();
						appOptions.put(co.getLongOption(), al);
					}
					else {
						printUsageAndExit("Duplicate option " + co.getLongOption() + "/" + co.getShortOption());
					}
				}
				else {
					if (al != null) {
						al.add(key);
					}
				}
			}
			
			// Parse post-args if enabled
			if (allowsPostArgs == true) {
				// Last option is special because there could be a list of arguments at the end not associated with any option
				if ((co.getMaximumNumberOfArguments() >= 0) && al.size() > co.getMaximumNumberOfArguments()) {
					int num;
					
					num = al.size() - co.getMaximumNumberOfArguments();
					for (int i = 0; i < num; i++) {
						String str;
						int idx;
						
						idx = co.getMaximumNumberOfArguments() + i;
						str = (String)al.get(idx);
						postArgs.add(str);
					}
					for (int i = 0; i < num; i++) {
						al.remove(co.getMaximumNumberOfArguments());
					}
				}
			}
			
			// Validate options
			it = appOptions.keySet().iterator();
			while (it.hasNext()) {
				String key;
				
				key = (String)it.next();
				co = (CLIOption)allPossibleOptions.get(key);
				al = (ArrayList<String>)appOptions.get(key);
				
				// If there are a limited number of arguments and we have exceeded it, print error message.
				if ((co.getMaximumNumberOfArguments() >= 0) && al.size() > co.getMaximumNumberOfArguments()) {
					String errMsg;
					
					errMsg = "Too many values for option " + co.getLongOption();
					if (co.getShortOption() != null) {
						errMsg = errMsg + " (" + co.getShortOption() + ")";
					}
					printUsageAndExit(errMsg);
				}
				if (al.size() < co.getMinimumNumberOfArguments()) {
					String errMsg;

					errMsg = "Too few values for option " + co.getLongOption();
					if (co.getShortOption() != null) {
						errMsg = errMsg + " (" + co.getShortOption() + ")";
					}
					printUsageAndExit(errMsg);
				}
			}
			
			checkRequiredOptions();
			//System.out.println(preArgs);
			//System.out.println(appOptions);
			//System.out.println(postArgs);
		}
		else {
			checkRequiredOptions();
		}
	}

	
	/**
	 * Returns the options passed in on the command-line.
	 */
	public HashMap getOptions() {
		return appOptions;
	}
	
	/**
	 * This checks whether the required options were supplied and informs the user if not.
	 */
	public void checkRequiredOptions() {
		String errStr = "";
		int errors = 0;
		Iterator it;
		
		it = appOptions.keySet().iterator();
		while (it.hasNext()) {
			CLIOption co;
			String key;
			ArrayList otherRequiredOptions;
			Iterator oro_it;
			
			key = (String)it.next();
			co = (CLIOption)allPossibleOptions.get(key);
			otherRequiredOptions = co.getRequiredOptions();
			if (otherRequiredOptions != null) {
				oro_it = otherRequiredOptions.iterator();
				while (oro_it.hasNext()) {
					String otherOption;
					
					otherOption = (String)oro_it.next();
					if (!appOptions.containsKey(otherOption)) {
						errors++;
						if (errors == 1) {
							errStr += otherOption;
						}
						else {
							errStr += ", " + otherOption;
						}
					}
				}
			}
		}
		switch (errors) {
			case 0:
				break;
			case 1:
				errStr += " is a required option.\n";
				printUsageAndExit(errStr);
				break;
			default:
				errStr += " are required options.\n";
				printUsageAndExit(errStr);
				break;
		}
		
	}

	/**
	 * This prints the usage banner and then exits the tool.
	 */
	public void printUsageAndExit() {
		printUsageAndExit("");
	}
	
	/**
	 * This prints the supplied error message and then exits the tool.
     * @param str The error message.
	 */

	public void printUsageAndExit(String str) {
		if (!str.equals("")) {
			System.err.println(tool.getName() + ": " + str);
		}
		System.err.println(usage());
		System.exit(-1);
	}
	
	/**
     * Returns whether the parser found the specified option.
	 * @param aStr The long name of the option.
     * @return Whether the parser found the specified option.
	 */	
	
	public boolean hasOption(String aStr) {
		return appOptions.containsKey(aStr);
	}
	
	/**
	 * Returns the arguments for the specified option.
	 * @param aStr The long name of the option.
     * @return The arguments for the specified option.
	 */	
		
	public ArrayList<String> argumentsForOption(String aStr) {
		return (ArrayList)appOptions.get(aStr);
	}
	
	/**
	 * Returns the value of the specified option.
	 * @param aStr The long name of the option.
     * @return The value of the specified option.
	 */	
	public String valueFor(String aStr) {
		ArrayList al;
		String value = null;
		
		al = argumentsForOption(aStr);
		if (al.size() > 0) {
			value = (String)al.get(0);
		}
		return value;
	}

	/**
	 * Adds the specified option object to the list of options.
	 * @param co The option instance.
	 */	
	public void addOption(CLIOption co) throws Exception {
		if (!allPossibleOptions.containsKey(co.getShortOption()) && !allPossibleOptions.containsKey(co.getLongOption())) {
			if (co.shortOption != null) {
				allPossibleOptions.put(co.getShortOption(), co);
			}
			if (co.getLongOption() != null) {
				allPossibleOptions.put(co.getLongOption(), co);
				possibleOptions.put(co.getLongOption(), co);
			}
			else {
				throw new Exception("CLIParser addOption(): Attempt to add null long option.");
			}
		}
		else {
			throw new Exception("CLIParser addOption: Attempt to add duplicate command line option.");
		}
	}
	
	/**
	 * Adds a new option object to the list of options using the specified values.
	 * @param longOption The long name of the option.
	 * @param shortOption The short name of the option.
	 * @param minArgs The minimum number of arguments for the option.
	 * @param maxArgs The maximum number of arguments for the option.
	 * @param argumentDesc The description of the arguments.
	 * @param requiredOptions An ArrayList of required options.
	 * @param description The description of the option.
	 */	
	public void addOptionWithValues(String longOption, String shortOption, int minArgs, int maxArgs, String argumentDesc, ArrayList requiredOptions, String description) throws Exception {
		CLIOption co;
		
		if (!allPossibleOptions.containsKey(longOption)) {
			if (shortOption != null) {
				if (allPossibleOptions.containsKey(shortOption)) {
					throw new Exception("CLIParser addOptionWithValues(): Attempt to add duplicate command line option.");
				}
			}
			co = new CLIOption();
			co.setShortOption(shortOption);
			co.setLongOption(longOption);
			co.setMinimumNumberOfArguments(minArgs);
			co.setMaximumNumberOfArguments(maxArgs);
			co.setArgumentDescription(argumentDesc);
			co.setRequiredOptions(requiredOptions);
			co.setDescription(description);
			if (shortOption != null) {
				allPossibleOptions.put(shortOption, co);
			}
			if (longOption != null) {
				allPossibleOptions.put(longOption, co);
				possibleOptions.put(longOption, co);
			}
			else {
				throw new Exception("CLIParser addOptionWithValues(): Attempt to add null long option.");
			}
		}
		else {
			throw new Exception("CLIParser addOptionWithValues(): Attempt to add duplicate command line option.");
		}
	}
	
	/**
	 * Returns the usage banner.
     * @return The usage banner.
	 */	
	public String usage() {
		String usage_str = "Usage: ";
		Iterator it;
		String longestArg;
		String argument;
		
		usage_str += tool.getName() ;
		if (allowsPreArgs == true) {
			usage_str += " ";
			usage_str += preArgsDescription;
		}
		
		if (possibleOptions.size() > 0) {
			usage_str += " " + optionsDescription;
		}
		
		if (allowsPostArgs == true) {
			usage_str += " ";
			usage_str += postArgsDescription;
		}
		
		usage_str += "\n";
		
		longestArg = "";
		it = possibleOptions.values().iterator();
		while (it.hasNext()) {
			CLIOption co;
			
			co = (CLIOption)it.next();
			argument = co.getLongOption() + descriptionForOption(co);
			if (argument.length() > longestArg.length()) {
				longestArg = argument;
			}
		}
		
		it = possibleOptions.values().iterator();
		while (it.hasNext()) {
			CLIOption co;
			String str;
			
			co = (CLIOption)it.next();
			str = co.getShortOption();
			if (str != null) {
				usage_str += str;
				usage_str += ", ";
				for (int i = 0; i < (4 - str.length()); i++) {
					usage_str += " ";
				}
			}
			else {
				usage_str += "      ";
			}
			
			argument = co.getLongOption() + descriptionForOption(co);
			usage_str += argument;
			
			for (int i = 0; i < (longestArg.length() - argument.length()); i++) {
				usage_str += " ";
			}
			usage_str += " ";
			usage_str += co.getDescription();
			usage_str += "\n";
		}
		return usage_str;
	}
	
	/**
	 * Returns a  description for the specified CLIOption.
	 */
	String descriptionForOption(CLIOption co) {
		String optionDescription = "";
		int maxArgs;
		String argumentDesc;

		maxArgs = co.getMaximumNumberOfArguments();
		argumentDesc = co.getArgumentDescription();
		if (maxArgs < 0) {
			optionDescription += " ";
			optionDescription += argumentDesc;
			optionDescription += "... ";				
		}
		else if (maxArgs == 0) {
		}
		else if (maxArgs == 1) {
			optionDescription += " ";
			optionDescription += argumentDesc;
		}
		else {
			optionDescription += " ";
			for (int i = 1; i <= maxArgs; i++) {
				optionDescription += argumentDesc;
				optionDescription += i;
				optionDescription += " ";
			}
		}
		return optionDescription;
	}
	
}
