//
//  CLIOption.java
//
//  Created by Robert La Ferla on 4/11/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;
import java.util.*;

/**
 * @author Robert La Ferla
 */

public class CLIOption {
	String shortOption;
	String longOption;
	int minimumNumberOfArguments;
	int maximumNumberOfArguments;
	String argumentDesc;
	ArrayList requiredOptions = new ArrayList();
	String description;
	
	/**
	 * This sets the required options.
     * @param al An ArrayList of required options.
	 */
	public void setRequiredOptions(ArrayList al) {
		requiredOptions = al;
	}

	/**
	 * Returns the array of required options.
	 * @return An ArrayList of required options.
	 */
	public ArrayList getRequiredOptions() {
		return requiredOptions;
	}
	
	/**
	 * This sets the optional short name of a command line option.  Typically a hyphen followed by a single character.
     * @param aStr A string.
	 */
	public void setShortOption(String aStr) {
		shortOption = aStr;
	}
	
	
	/**
	 * Returns the optional short name.
	 * @return The short name of the option.
	 */
	public String getShortOption() {
		return shortOption;
	}
	
	/**
	 * This sets the required long name of a command line option.
     * @param aStr A string.
	 */
	public void setLongOption(String aStr) {
		longOption = aStr;
	}
	
	/**
	 * Returns the required long name.
	 * @return The long name of the option.
	 */
	public String getLongOption() {
		return longOption;
	}
	
	/**
	 * This sets the minimum number of arguments that the option takes.
     * @param i A positive integer.
	 */
	public void setMinimumNumberOfArguments(int i) {
		minimumNumberOfArguments = i;
	}
	
	/**
	 * Returns the minimum number of arguments for the option.
	 * @return The minimum number of arguments for the option.
	 */
	public int getMinimumNumberOfArguments() {
		return minimumNumberOfArguments;
	}
	
	/**
	 * This sets the maximum number of arguments that the option takes.
     * @param i A positive integer.
	 */	
	public void setMaximumNumberOfArguments(int i) {
		maximumNumberOfArguments = i;
	}
	
	/**
	 * Returns the maximum number of arguments for the option.
	 * @return The maximum number of arguments for the option.
	 */
	public int getMaximumNumberOfArguments() {
		return maximumNumberOfArguments;
	}
	
	/**
	 * This sets the description for each and every argument value used in the help/usage message.
	 * The argument number is appended to each argument.
	 * e.g. clitool --option val1 val2 val3 val4
     * @param aStr The argument value description.
	 */
	public void setArgumentDescription(String aStr) {
		argumentDesc = aStr;
	}
	
	/**
	 * Returns the argument description.
	 * @return The argument description.
	 */
	public String getArgumentDescription() {
		return argumentDesc;
	}

	/**
	 * This sets the description for the option used in the help/usage message.
     * @param aStr The option description.
	 */
	
	public void setDescription(String aStr) {
		description = aStr;
	}
	
	/**
	 * Returns the description of the option.
	 * @return The description of the option.
	 */
	public String getDescription() {
		return description;
	}
	
}
