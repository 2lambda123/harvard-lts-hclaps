//
//  TimePoint.java
//  hclaps
//
//  Created by Robert La Ferla on 12/12/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import com.therockquarry.aes31.adl.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * TimePoint represents one point on a TimeLine.
 *
 * @author Robert La Ferla
 */

public class TimePoint implements Comparable {
	BigDecimal sampleRate = null;
	long samples = 0L;
	public enum Format { SAMPLES, NPT, TCF };
	Format format = Format.SAMPLES;
	
	
	/**
	 * Returns a new TimePoint instance.
	 */
	public TimePoint() {
	}
	
	/**
	 * Returns a new TimePoint instance with the specified number of samples and sample rate.
	 *
	 * @param value The number of samples.
	 * @param rate The sample rate.
	 */	
	public TimePoint(long value, BigDecimal rate) {
		samples = value;
		sampleRate = rate;
	}

	/**
	 * Returns a new TimePoint instance with the specified number of samples, sample rate and format.
	 *
	 * @param value The number of samples.
	 * @param rate The sample rate.
	 * @param fmt The time format.
	 */	
	public TimePoint(long value, BigDecimal rate, Format fmt) {
		samples = value;
		sampleRate = rate;
		format = fmt;
	}
	
	/**
	 * Sets the number of samples.
	 *
	 * @param l The value.
	 */
	public void setSamples(long l) {
		samples = l;
	}
	
	/**
	 * Returns the number of samples.
	 */
	public long getSamples() {
		return samples;
	}
	
	/**
	 * Sets the sample rate.
	 *
	 * @param bd The sample rate.
	 */
	public void setSampleRate(BigDecimal bd) {
		sampleRate = bd;
	}
	
	/**
	 * Returns the sample rate.
	 */
	public BigDecimal getSampleRate() {
		return sampleRate;
	}
	
	/**
	 * Sets the format.
	 *
	 * @param f The format.
	 */
	public void setFormat(Format f) {
		format = f;
	}
	
	/**
	 * Returns the format.
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * Returns a formatted string representation of the time point.
	 */
	
	public String toString() {
		return timeForSamples(samples, sampleRate, format);
	}
	
	/**
	* Returns the normal play time (NPT) as a String for the specifed number of samples.
	 *
	 * @param samples The samples to format.
	 */
	public static String nptForSamples(long samples, BigDecimal sr) {
		BigDecimal total;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		int milliseconds = 0;
		BigDecimal mantissa;
		
		//System.out.println("nptForSamples(" + samples + ")");
		//System.out.println("sample rate = " + timeline.getSampleRate());
		
		total = new BigDecimal(samples);
		total = total.divide(sr, 64, RoundingMode.HALF_UP);
		//System.out.println("total = " + total);
		
		hours = (int)(total.intValue() / 3600);
		//System.out.println("hours = " + hours);
		
		total = total.subtract(new BigDecimal(hours * 3600));
		//System.out.println("total = " + total);
		
		minutes = (int)(total.intValue() / 60);
		//System.out.println("minutes = " + minutes);
		
		total = total.subtract(new BigDecimal(minutes * 60));
		//System.out.println("total = " + total);
		
		seconds = total.intValue();
		//System.out.println("seconds = " + seconds);
		mantissa = total;
		//System.out.println("mantissa = " + mantissa);
		mantissa = mantissa.subtract(new BigDecimal(seconds));
		//System.out.println("mantissa = " + mantissa);
		//mantissa = mantissa.multiply(new BigDecimal(1000));
		mantissa = mantissa.movePointRight(3);
		mantissa = mantissa.setScale(0, RoundingMode.DOWN);
		//System.out.println("mantissa = " + mantissa);
		milliseconds = mantissa.intValue();
		//System.out.println("milliseconds = " + milliseconds);
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds);
	}
	
	/**
	 * Returns the samples as a String using the specified time format.
	 *
	 * @param samples The samples.
	 * @param sr The sample rate.
	 * @param timeFormat The time format.
	 */
	public static String timeForSamples(long samples, BigDecimal sr, Format timeFormat) {
		String formattedTime = "";
		
		switch (timeFormat) {
			case SAMPLES:
				formattedTime = "" + samples;
				break;
			case NPT:
				formattedTime = nptForSamples(samples, sr);
				break;
			case TCF:
				TcfToken tt;
				TcfTokenFormatProperties ttfp;
				
				ttfp = new TcfTokenFormatProperties(1.0f, 30, ((char)0x00), 1, false);
				tt = new TcfToken(samples, sr.doubleValue(), ttfp);
				formattedTime = tt.toString();
				break;
		}
		return formattedTime;
	}
	
	/**
	 * Compares the samples and sample rate in this time point to the specified time point.  This differs from equals().
	 *
	 * @param o Another TimePoint instance.
	 */
	public int compareTo(Object o) {
		TimePoint tp;
		int cmp;
		
		tp = (TimePoint)o;
		cmp = 0;
		
		if (sampleRate == tp.getSampleRate()) {
			if (getSamples() < tp.getSamples()) {
				cmp = -1;
			}
			else if (getSamples() > tp.getSamples()) {
				cmp = 1;
			}
		}
		else {
			BigDecimal value1, value2;
			BigDecimal sampleRateRatio;
			
			sampleRateRatio = sampleRate.divide(tp.getSampleRate(), 64, RoundingMode.DOWN);
			value1 = new BigDecimal(getSamples());
			value2 = sampleRateRatio.multiply(new BigDecimal(tp.getSamples()));
			cmp = value1.compareTo(value2);
		}
		return cmp;
	}
	
}
