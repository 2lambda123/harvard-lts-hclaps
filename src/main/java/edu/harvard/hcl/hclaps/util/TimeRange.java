//
//  TimeRange.java
//  hclaps
//
//  Created by Robert La Ferla on 12/2/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import com.therockquarry.aes31.adl.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * TimeRange represents the range between two TimePoint instances.
 *
 * @author Robert La Ferla
 */

public class TimeRange implements Comparable {
	TimePoint start = new TimePoint();
	TimePoint end = new TimePoint();
	TimePoint duration = new TimePoint();
	Object obj = null;
	TimeLine timeline = null;

	/**
	 * Returns a new TimeRange instance.
	 */
	public TimeRange() {
	}
	
	/**
	 * Returns a new TimeRange instance representing the range between two TimePoint instances.
	 */
	public TimeRange(TimePoint s, TimePoint e) {
		start = s;
		end = e;
	}

	
	/**
	 * Sets the timeline that this time range is associated with.  The format and sample rate
	 * of each time value will be taken from the timeline.
	 *
	 * @param tl The timeline.
	 */
	public void setTimeLine(TimeLine tl) {
		timeline = tl;
		start.setFormat(timeline.getTimeFormat());
		start.setSampleRate(timeline.getSampleRate());
		end.setFormat(timeline.getTimeFormat());
		end.setSampleRate(timeline.getSampleRate());
		duration.setFormat(timeline.getTimeFormat());
		duration.setSampleRate(timeline.getSampleRate());
	}
	
	/**
	 * Returns the timeline that this time range is associated with.
	 */	
	public TimeLine getTimeLine() {
		return timeline;
	}
	
	/**
	 * Sets the start sample for this time range.
	 *
	 * @param tp The start sample.
	 */
	public void setStart(TimePoint tp) {
		start = tp;
		duration.setSamples(end.getSamples() - start.getSamples() + 1);
	}

	/**
	 * Sets the start sample for this time range.
	 *
	 * @param l The start sample.
	 */
	public void setStartSamples(long l) {
		start.setSamples(l);
		duration.setSamples(end.getSamples() - start.getSamples() + 1);
	}

	
	/**
	 * Returns the start sample for this time range.
	 */
	public TimePoint getStart() {
		return start;
	}

	/**
	 * Sets the end sample for this time range.
	 *
	 * @param tp The end sample.
	 */	
	public void setEnd(TimePoint tp) {
		end = tp;
		duration.setSamples(end.getSamples() - start.getSamples() + 1);
	}

	/**
	 * Sets the end sample for this time range.
	 *
	 * @param l The end sample.
	 */	
	public void setEndSamples(long l) {
		end.setSamples(l);
		duration.setSamples(end.getSamples() - start.getSamples() + 1);
	}

	
	/**
	 * Returns the end sample for this time range.
	 */
	public TimePoint getEnd() {
		return end;
	}

	/**
	 * Sets the duration in samples from the start to the end sample.
	 *
	 * @param tp The number of samples between the start and end samples.
	 */
	public void setDuration(TimePoint tp) throws InvalidArgumentException {
		if (tp.getSamples() > 1) {
			duration = tp;
			end.setSamples(start.getSamples() + (duration.getSamples() - 1));
		}
		else {
			throw new InvalidArgumentException("Duration must be greater than 1.");
		}
	}

	/**
	 * Sets the duration in samples from the start to the end sample.
	 *
	 * @param l The number of samples between the start and end samples.
	 */
	public void setDurationSamples(long l) throws InvalidArgumentException {
		if (l > 1) {
			duration.setSamples(l);
			end.setSamples(start.getSamples() + (duration.getSamples() - 1));
		}
		else {
			throw new InvalidArgumentException("Duration must be greater than 1.");
		}
	}

	
	/**
	 * Returns the number of samples between the start and end samples.
	 */
	public TimePoint getDuration() {
		return duration;
	}
	
	/**
	 * Associates a user defined object with this time range.
	 *
	 * @param o The user defined object.
	 */
	public void setObject(Object o) {
		obj = o;
	}

	/**
	 * Returns a user defined object associated with this time range.
	 */
	public Object getObject() {
		return obj;
	}
	
	/**
	 * Compares the start samples in this time range to the specified time range.  This differs from equals().
	 *
	 * @param o Another time range.
	 */
	public int compareTo(Object o) {
		TimeRange tr;
		int cmp;
		
		tr = (TimeRange)o;
		cmp = 0;
		
		if (start.getSamples() < tr.getStart().getSamples()) {
			cmp = -1;
		}
		else if (start.getSamples() > tr.getStart().getSamples()) {
			cmp = 1;
		}
		return cmp;
	}
	
	/**
	 * Sets the format.
	 *
	 * @param f The format.
	 */
	public void setFormat(TimePoint.Format f) {
		start.setFormat(f);
		end.setFormat(f);
		duration.setFormat(f);
	}
	
	/**
	 * Sets the sample rate of the range.  This does not convert samples to the new sample rate.
	 *
	 * @param bd The sample rate.
	 */
	public void setSampleRate(BigDecimal bd) {
		start.setSampleRate(bd);
		end.setSampleRate(bd);
		duration.setSampleRate(bd);
	}
	
	/**
	 * Returns a String representation of the time range.  A range is denoted by a colon with the duration in parentheses.
	 */
	public String toString() {
		String str = "";
	
		if (duration.getSamples() > 1) {
			str = "[" + start.toString() + " : " + end.toString() + " (" + duration.toString() + ")]";
		}
		else {
			str = start.toString();
		}
		return str;
	}
}
