//
//  CodingHistory.java
//  hclaps
//
//  Created by Robert La Ferla on 5/22/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

import java.util.*;

/**
 * CodingHistory.
 *
 * @author Robert La Ferla
 */

public class CodingHistory {
	ArrayList history = new ArrayList();

	/**
	 * Returns a new CodingHistory instance.
	 */
	public CodingHistory() {
		//System.out.println("CodingHistory()");
	}
	
	/**
	 * Returns a new CodingHistory instance from the specified string.
	 *
	 * @param str A String instance.
	 */
	public CodingHistory(String str) {
		addCodingEventsFromString(str);
	}
	
	/**
	 * Returns a new CodingHistory instance from the specified CodingHistory.
	 *
	 * @param ch A CodingHistory instance.
	 */
	public CodingHistory(CodingHistory ch) {
		ArrayList al;
		
		//System.out.println("CodingHistory("+ch+")");
		al = ch.getCodingEvents();
		if (al != null) {
			history = new ArrayList(al);
		}
	}
	
	/**
	 * Adds a CodingEvent instance to the coding history.
	 *
	 * @param ce A CodingEvent instance.
	 */
	public void addCodingEvent(CodingEvent ce) {
		history.add(ce);
	}
	
	/**
	 * Returns an ArrayList of coding events.
	 */
	public ArrayList getCodingEvents() {
		return history;
	}
	
	/**
	 * Adds coding events from the specifed string.
	 *
	 * @param str A String instance.
	 */
	public void addCodingEventsFromString(String str) {
		String [] lines;
				
		lines = str.split("\r\n");
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].length() > 0) {
				CodingEvent ce;
				char c;
				
				c = lines[i].charAt(0);
				if (c == 'F' || c == 'A' || c == 'B' || c == 'M' || c == 'W' || c == 'T') {
					ce = new CodingEvent(lines[i]);
					history.add(ce);
				}
			}
		}
	}
	
	/**
	 * Returns a string representation of the coding history.
	 */
	public String toString() {
		String str = "";
		Iterator it;
		
		it = history.iterator();
		while (it.hasNext()) {
			CodingEvent ce;
			
			ce = (CodingEvent)it.next();
			str += ce.toString();
		}
		
		return str;
	}
	
	/**
	 * Returns the length of this coding history.
	 */
	public int length() {
		String str;
		int len = 0;
		
		str = toString();
		if (str != null) {
			len = str.length();
		}
		return len;
	}
	
}
