//
//  CodingEvent.java
//  hclaps
//
//  Created by Robert La Ferla on 5/22/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

/**
 * CodingEvent
 *
 * @author Robert La Ferla
 */

public class CodingEvent {
	public final static int NONE = -1;

	// Coding Algorithm
	public enum CodingAlgorithm { NONE, ANALOGUE, PCM, MPEG1L1, MPEG1L2, MPEG1L3, MPEG2L1, MPEG2L2, MPEG2L3 };
	
	// Mode
	public enum Mode { NONE, MONO, STEREO, DUAL_MONO, JOINT_STEREO };
	
	CodingAlgorithm codingAlgorithm = CodingAlgorithm.NONE;
	int samplingFrequency = NONE;
	int bitRate = NONE;
	int wordLength = NONE;
	Mode mode = Mode.NONE;
	String text = null;

	/**
	 * Returns a new CodingEvent instance.
	 */	
	public CodingEvent() {
		//System.out.println("CodingEvent()");
	}
	
	/**
	 * Returns a new CodingEvent instance from a string.
	 *
	 * @param str A string.
	 */	
	public CodingEvent(String str) {
		String [] fields;
		
		// System.out.println("CodingEvent(" + str + ")");
		fields = str.split(",");
		for (int i = 0; i < fields.length; i++) {
			String field;
			char c;
			String s;
			
			field = fields[i];
			//System.out.println("fields[" +i+"]=" + field);
			c = field.charAt(0);
			switch (c) {
				case 'A':
					if (field.compareToIgnoreCase("A=ANALOGUE") == 0) {
						setCodingAlgorithm(CodingAlgorithm.ANALOGUE);
					}
					else if (field.compareToIgnoreCase("A=PCM") == 0) {
						setCodingAlgorithm(CodingAlgorithm.PCM);
					}
					else if (field.compareToIgnoreCase("A=MPEG1L1") == 0) {
						setCodingAlgorithm(CodingAlgorithm.MPEG1L1);
					}
					else if (field.compareToIgnoreCase("A=MPEG1L2") == 0) {
						setCodingAlgorithm(CodingAlgorithm.MPEG1L2);
					}
					else if (field.compareToIgnoreCase("A=MPEG1L3") == 0) {
						setCodingAlgorithm(CodingAlgorithm.MPEG1L3);
					}
					else if (field.compareToIgnoreCase("A=MPEG2L1") == 0) {
						setCodingAlgorithm(CodingAlgorithm.MPEG2L1);
					}
					else if (field.compareToIgnoreCase("A=MPEG2L2") == 0) {
						setCodingAlgorithm(CodingAlgorithm.MPEG2L2);
					}
					else if (field.compareToIgnoreCase("A=MPEG2L3") == 0) {
						setCodingAlgorithm(CodingAlgorithm.MPEG2L3);
					}
					break;
				case 'F':					
					s = field.substring(2, field.length());
					setSamplingFrequency(Integer.parseInt(s));
					break;
				case 'B':					
					s = field.substring(2, field.length());
					setBitRate(Integer.parseInt(s));
					break;
				case 'W':					
					s = field.substring(2, field.length());
					setWordLength(Integer.parseInt(s));
					break;
				case 'M':
					if (field.compareToIgnoreCase("M=mono") == 0) {
						setMode(Mode.MONO);
					}
					else if (field.compareToIgnoreCase("M=stereo") == 0) {
						setMode(Mode.STEREO);
					}
					else if (field.compareToIgnoreCase("M=dual-mono") == 0) {
						setMode(Mode.DUAL_MONO);
					}
					else if (field.compareToIgnoreCase("M=joint-stereo") == 0) {
						setMode(Mode.JOINT_STEREO);
					}
					break;
				case 'T':					
					s = field.substring(2, field.length());
					setText(s);
					break;
			}
		}
	}	
	
	/**
     * Sets the coding algorithm to the specified value.
	 *
	 * @param aCodingAlgorithm The coding algorithm.
	 */
	
	public void setCodingAlgorithm(CodingAlgorithm aCodingAlgorithm) {
		codingAlgorithm = aCodingAlgorithm;
	}
	
	/**
	 * Returns the coding algorithm.
	 */	
	public CodingAlgorithm getCodingAlgorithm() {
		return codingAlgorithm;
	}
	
	/**
	 * Sets the sampling frequency to the specified value.
	 *
	 * @param i The sampling frequency.
	 */	
	public void setSamplingFrequency(int i) {
		samplingFrequency = i;
	}
	
	/**
	 * Returns the sampling frequency.
	 */	
	public int getSamplingFrequency() {
		return samplingFrequency;
	}
	
	/**
	 * Sets the bit rate to the specified value.
	 *
	 * @param i The bit rate.
	 */		
	public void setBitRate(int i) {
		bitRate = i;
	}
	
	/**
	 * Returns the bit rate.
	 */
	public int getBitRate() {
		return bitRate;
	}
	
	/**
	 * Sets the word length to the specified value.
	 *
	 * @param i The word length.
	 */		
	public void setWordLength(int i) {
		wordLength = i;
	}
	
	/**
	 * Returns the word length.
	 */
	public int getWordLength() {
		return wordLength;
	}
	
	/**
	 * Sets the mode to the specified value.
	 *
	 * @param aMode The mode.
	 */		
	public void setMode(Mode aMode) {
		mode = aMode;
	}

	/**
	 * Returns the mode.
	 */
	public Mode getMode() {
		return mode;
	}
	
	/**
	 * Sets the text to the specified value.
	 *
	 * @param str A String.
	 */
	public void setText(String str) {
		text = str;
	}
	
	/**
	 * Returns the text.
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Returns a string representation of the CodingEvent.
	 */
	public String toString() {
		String str = "";
		
		// Coding Algorithm
		if (codingAlgorithm != CodingAlgorithm.NONE) {
			str += "A=";
			switch (codingAlgorithm) {
				case ANALOGUE:
					str += "ANALOGUE";
					break;
				case PCM:
					str += "PCM";
					break;
				case MPEG1L1:
					str += "MPEG1L1";
					break;
				case MPEG1L2:
					str += "MPEG1L2";
					break;
				case MPEG1L3:
					str += "MPEG1L3";
					break;
				case MPEG2L1:
					str += "MPEG2L1";
					break;
				case MPEG2L2:
					str += "MPEG2L2";
					break;
				case MPEG2L3:
					str += "MPEG2L3";
					break;
			}
		}
		
		// Sampling Frequency
		if (samplingFrequency != NONE) {
			if (!str.equals("")) {
				str += ",";
			}
			str += "F=" + samplingFrequency;
		}
		
		// Bit Rate
		if (bitRate != NONE) {
			if (!str.equals("")) {
				str += ",";
			}
			str += "B=" + bitRate;
		}

		// Word Length
		if (wordLength != NONE) {
			if (!str.equals("")) {
				str += ",";
			}
			str += "W=" + wordLength;
		}
		
		// Mode
		if (mode != Mode.NONE) {
			if (!str.equals("")) {
				str += ",";
			}
			str += "M=";
			switch (mode) {
				case MONO:
					str += "mono";
					break;
				case STEREO:
					str += "stereo";
					break;
				case DUAL_MONO:
					str += "dual-mono";
					break;
				case JOINT_STEREO:
					str += "joint-stereo";
					break;
			}
		}
		
		// Text
		if (text != null) {
			if (!str.equals("")) {
				str += ",";
			}
			str += "T=" + text;
		}

		if (!str.equals("")) {
			str += "\r\n";
		}
		return str;
	}
	
	
}
