//
//  TimeLine.java
//  hclaps
//
//  Created by Robert La Ferla on 12/2/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;
import com.therockquarry.aes31.adl.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * TimeLine is a class that represents audio cuts on a timeline.
 *
 * @author Robert La Ferla
 */

/**
 * Returns a new TimeLine instance with a default time format in samples.
 */
public class TimeLine {
	public enum ADLTimeLineType { SRC, DEST }
	public enum ADLInfoType { CUTS, BWAVS }
	ArrayList<TimeRange> timeRangeArray = new ArrayList<TimeRange>();
	BigDecimal sampleRate = null;
	TimePoint.Format timeFormat = TimePoint.Format.SAMPLES;
	
	/**
	 * Creates a new TimeLine instance.
	 */
	public TimeLine() {
	}
	
	/**
	 * Returns a new TimeLine instance representing the cut edits for a specific channel on the specified timeline of the specified ADL.
	 *
	 * @param adl An ADL instance.
	 * @param channel The channel number.
	 * @param timelineType The timeline type (SRC or DEST) for source or destination timeline respectively.
	 * @param infoType The information desired (CUTS or BWAVS) for cut or bwav time reference times respectively.
	 */
	public TimeLine(ADL adl, int channel, ADLTimeLineType timelineType, ADLInfoType infoType) {
		ADLSection adls;
		EventListSection els;
		SequenceSection ss;
		ArrayList<BaseEditEntry> destChannelEntries;
		Iterator<BaseEditEntry> event_it;
					 
		adls = adl.getADLSection(); 
		els = adls.getEventListSection();
		ss = adls.getSequenceSection();
		
		sampleRate = ss.getSeqSampleRateAsDecimal();
		
		destChannelEntries = els.getEventEntriesForDestChannel(channel);
		
		event_it = destChannelEntries.iterator();
		while (event_it.hasNext()) {
			BaseEditEntry bee;
			
			bee = event_it.next();
			if (bee.getClass().getName().equals("com.therockquarry.aes31.adl.CutEditEntry")) {
				CutEditEntry cee;
				TimeRange tr;
				
				cee = (CutEditEntry)bee;
				tr = new TimeRange();
				tr.setObject(cee);
				
				switch (infoType) {
					case CUTS:
						switch (timelineType) {
							case DEST:
								tr.setStartSamples(cee.getDestIn().valueOf());
								tr.setEndSamples(cee.getDestOut().valueOf());
								tr.setSampleRate(sampleRate);
								tr.setFormat(TimePoint.Format.NPT);
								break;
							case SRC:
								tr.setStartSamples(cee.getSrcIn().valueOf());
								tr.setEndSamples(cee.getSrcIn().valueOf() + cee.getDestOut().valueOf() - cee.getDestIn().valueOf());
								break;
						}
						break;
					case BWAVS:
						FileSourceIndexEntry fsie = null;
						SourceEntry se;
						
						se = cee.getSourceIndexEntry();
						try {
							fsie = (FileSourceIndexEntry)se.getIndexEntries(SourceEntry.SourceType.FILE_SRC).get(0);
							//System.out.println("fsie.getFileIn().valueOf() = " + fsie.getFileIn().valueOf());
							//System.out.println("fsie.getFileLength().valueOf() = " + fsie.getFileLength().valueOf());
							tr.setStartSamples(fsie.getFileIn().valueOf());
							tr.setDurationSamples(fsie.getFileLength().valueOf());
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
						break;
				}
				addTimeRange(tr);
			}				
		}
	}
	
	/**
	 * Creates a new TimeLine from markers in an ADL ignoring all PQ_INDEX markers
	 * and optionally including gaps between PQ_END and next PQ_START markers.
	 */
	public TimeLine(ADL adl, boolean includeGaps) {
		ADLSection adls;
		SequenceSection ss;
		MarkerListSection mls;
		ArrayList<BaseMarkerPoint> allMarkerList;
		ArrayList<BaseMarkerPoint> markerList;
		Iterator<BaseMarkerPoint> it;
		ListIterator<BaseMarkerPoint> lit;
		
		adls = adl.getADLSection(); 
		ss = adls.getSequenceSection();
		mls = adls.getMarkerListSection();
		
		markerList = new ArrayList<BaseMarkerPoint>();
		
		sampleRate = ss.getSeqSampleRateAsDecimal();
		// Ignore all index and comment markers by creating a new list with only start and end markers.
		allMarkerList = mls.getMarkers();
		it = allMarkerList.iterator();
		while (it.hasNext()) {
			BaseMarkerPoint marker;
			
			marker = it.next();
			if (marker.getClass().getName().equals("com.therockquarry.aes31.adl.PqStartMarker") || marker.getClass().getName().equals("com.therockquarry.aes31.adl.PqEndMarker")) {
				markerList.add(marker);
			}
		}	
		lit = markerList.listIterator();
		while (lit.hasNext()) {
			BaseMarkerPoint marker;
			
			marker = lit.next();
			if (marker.getClass().getName().equals("com.therockquarry.aes31.adl.PqStartMarker")) {
				TimeRange tr;
				BaseMarkerPoint nextMarker;
				
				nextMarker = markerList.get(lit.nextIndex());
				
				if (includeGaps == true) {
					if (nextMarker.getClass().getName().equals("com.therockquarry.aes31.adl.PqEndMarker")) {
						// Jump to the PQ_END marker
						nextMarker = lit.next();
						if (lit.hasNext()) {
							// This should always be a PQ_START.
							nextMarker = markerList.get(lit.nextIndex());
						}
					}
				}
				tr = new TimeRange();
				tr.setObject(marker);
				tr.setStartSamples(marker.getDestIn().valueOf());
				tr.setEndSamples(nextMarker.getDestIn().valueOf() -1);
				
				addTimeRange(tr);
			}
		}
	}		
	
	/**
	 * Converts all time ranges in the time line to the specified sample rate.
	 *
	 * @param bd The new sample rate.
	 * @see TimeLine#setSampleRate(BigDecimal) setSampleRate()
	 */
	public void convertToSampleRate(BigDecimal bd) {
		if (!sampleRate.equals(bd)) {
			Iterator<TimeRange> it;
			BigDecimal sampleRateRatio;
			
			sampleRateRatio = bd.divide(sampleRate, 64, RoundingMode.DOWN);
			it = timeRangeArray.iterator();
			while (it.hasNext()) {
				TimeRange tr;
				BigDecimal value;
				
				tr = it.next();
				value = sampleRateRatio.multiply(new BigDecimal(tr.getStart().getSamples()));
				tr.setStartSamples(value.longValue());
				value = sampleRateRatio.multiply(new BigDecimal(tr.getEnd().getSamples()));
				tr.setEndSamples(value.longValue());
			}
			setSampleRate(bd);
		}
	}
	
	/**
	 * Sets the sample rate of the time line.  This does not convert the time ranges
	 * in the timeline to the new sample rate.
	 *
	 * @param bd The sample rate.
	 * @see TimeLine#convertToSampleRate(BigDecimal) convertToSampleRate()
	 */
	public void setSampleRate(BigDecimal bd) {
		sampleRate = bd;
	}
	
	/**
	 * Returns the sample rate for the timeline.
	 */
	public BigDecimal getSampleRate() {
		return sampleRate;
	}
	
	/**
	 * Sets the time format of the timeline to the specified format.
	 *
	 * @param tf Time format.
	 */
	public void setTimeFormat(TimePoint.Format tf) {
		Iterator<TimeRange> it;

		timeFormat = tf;
		it = timeRangeArray.iterator();
		while (it.hasNext()) {
			TimeRange tr;
			
			tr = it.next();
			tr.setFormat(tf);
		}
	}
	
	/**
	 * Returns the time format of the timeline.
	 */
	public TimePoint.Format getTimeFormat() {
		return timeFormat;
	}
	
	/**
	 * Adds the specified time range to the timeline.
	 *
	 * @param tr The time range to add.
	 */
	public void addTimeRange(TimeRange tr) {
		// System.out.println("addTimeRange(" + tr.toString()+ ")");
		timeRangeArray.add(tr);
		tr.setTimeLine(this);
	}
	
	/**
	 * Returns the time range(s) in the time line at the specified sample position.
	 *
	 * @param point The sample position.
	 */
	public ArrayList<TimeRange> getTimeRangesFor(long point) {
		ArrayList<TimeRange> al = null;
		Iterator<TimeRange> it;
		
		al = new ArrayList();
		it = timeRangeArray.iterator();
		while (it.hasNext()) {
			TimeRange tr;
			
			tr = it.next();
			if (point >= tr.getStart().getSamples() && point <= tr.getEnd().getSamples()) {
				al.add(tr);
			}
		}
		return al;
	}
	
	/**
	 * Returns the time ranges in the time line.
	 */
	public ArrayList<TimeRange> getTimeRanges() {
		return timeRangeArray;
	}
	
	/**
	 * Returns the number of time ranges in the time line.
	 */
	public int size() {
		return timeRangeArray.size();
	}
	
	/**
	 * Returns the first time range in the time line.  If there are no time ranges, it returns null.
	 */
	public TimeRange getFirstTimeRange() {
		TimeRange firstRange = null;
		
		if (timeRangeArray.size() > 0) {
			firstRange = timeRangeArray.get(0);
		}
		
		return firstRange;
	}

	/**
	 * Returns the last time range in the time line.  If there are no time ranges, it returns null.
	 */
	public TimeRange getLastTimeRange() {
		TimeRange lastRange = null;

		if (timeRangeArray.size() > 0) {
			lastRange =  timeRangeArray.get(timeRangeArray.size() - 1);
		}
		return lastRange;
	}
	
	/**
	 * Returns a TimeLine object that represents a subset of the original timeline
	 * from the start sample to the end sample of the specified range.
	 *
	 * @param range The range of samples to extract from the timeline.
	 */
	public TimeLine getTimeLineBetween(TimeRange range) {
		TimeLine tl;
		Iterator<TimeRange> it;
		
		//System.out.println("getTimeLineBetween(" + range.getStart() + ", " + range.getEnd() + " = " + range.getDuration() + ")");
		
		tl = new TimeLine();
		tl.setSampleRate(sampleRate);
		tl.setTimeFormat(timeFormat);
		it = timeRangeArray.iterator();
		while (it.hasNext()) {
			TimeRange tr;
			
			tr = it.next();			
			if (tr.getStart().getSamples() >= range.getStart().getSamples() && tr.getStart().getSamples() <= range.getEnd().getSamples()) {
				TimeRange ntr;
				
				// Start of cut is contained in marker range.
				ntr = new TimeRange();
				ntr.setStartSamples(tr.getStart().getSamples());
				if (tr.getEnd().getSamples() > range.getEnd().getSamples()) {
					ntr.setEndSamples(range.getEnd().getSamples());
				}
				else {
					ntr.setEndSamples(tr.getEnd().getSamples());
				}
				
				ntr.setObject(tr.getObject());
				tl.addTimeRange(ntr);
			}
			else if (tr.getEnd().getSamples() <= range.getEnd().getSamples() && tr.getEnd().getSamples() >= range.getStart().getSamples()) {
				TimeRange ntr;
				
				// End of cut is contained in marker range.
				ntr = new TimeRange();
				ntr.setEndSamples(tr.getEnd().getSamples());
				if (tr.getStart().getSamples() < range.getStart().getSamples()) {
					ntr.setStartSamples(range.getStart().getSamples());
				}
				else {
					ntr.setStartSamples(tr.getStart().getSamples());
				}
				
				ntr.setObject(tr.getObject());
				tl.addTimeRange(ntr);
			}
			else if (range.getStart().getSamples() >= tr.getStart().getSamples() && range.getEnd().getSamples() <= tr.getEnd().getSamples()) {
				TimeRange ntr;
				
				// All of cut is contained within the marker range
				ntr = new TimeRange();
				ntr.setStartSamples(range.getStart().getSamples());
				ntr.setEndSamples(range.getEnd().getSamples());
				ntr.setObject(tr.getObject());
				tl.addTimeRange(ntr);
			}
			
		}
		return tl;
	}

	/**
	 * Shifts the time line by the specified number of samples.  A negative shift value shifts left whereas a
	 * positive value shifts right.
	 *
	 * @param l	The number of samples to shift by.
	 */
	public void shiftBy(long l) {
		Iterator<TimeRange> it;
		
		it = timeRangeArray.iterator();
		while (it.hasNext()) {
			TimeRange tr;
			
			tr = it.next();
			tr.setStartSamples(tr.getStart().getSamples() + l);
			tr.setEndSamples(tr.getEnd().getSamples() + l);
		}
	}
	
	/**
	 * Sorts the time ranges in the time line.
	 */
	public void sort() {
		Collections.sort(timeRangeArray);
	}
	
	/**
	 * Returns a TimeLine object that represents a relative timeline from the original time line.
	 * The returned object has the same sample rate and time format as the original (absolute) timeline.
	 *
	 * @return The relative timeline.
	 */
	public TimeLine getRelativeTimeLine() {
		TimeLine tl;
	
		tl = new TimeLine();
		tl.setSampleRate(sampleRate);
		tl.setTimeFormat(timeFormat);
		
		if (timeRangeArray.size() > 0) {
			ListIterator<TimeRange> lit;
			TimeRange firstTimeRange;
			TimeRange relativeTimeRange;

			
			firstTimeRange = timeRangeArray.get(0);
			relativeTimeRange = new TimeRange();
			relativeTimeRange.setObject(firstTimeRange);
			relativeTimeRange.setStartSamples(0);
			relativeTimeRange.setEndSamples(firstTimeRange.getEnd().getSamples() - firstTimeRange.getStart().getSamples());
			tl.addTimeRange(relativeTimeRange);
			
			lit = timeRangeArray.listIterator();
			while (lit.hasNext()) {
				TimeRange currentTimeRange;
				
				currentTimeRange = lit.next();
				if (lit.hasNext()) {
					TimeRange nextTimeRange;
					
					relativeTimeRange = new TimeRange();
					relativeTimeRange.setObject(currentTimeRange.getObject());
					tl.addTimeRange(relativeTimeRange);
					nextTimeRange = timeRangeArray.get(lit.nextIndex());
					//System.out.println("currentTimeRange=" + currentTimeRange.toString());
					//System.out.println("nextTimeRange=" + nextTimeRange.toString());
					relativeTimeRange.setStartSamples(nextTimeRange.getStart().getSamples() - currentTimeRange.getStart().getSamples());
					relativeTimeRange.setEndSamples(nextTimeRange.getEnd().getSamples() - currentTimeRange.getStart().getSamples());
					//System.out.println("relativeTimeRange=" + relativeTimeRange.toString());
					//System.out.println("");
				}
			}
		}
		return tl;
	}
	
	/**
	 * Returns a string representation of the timeline.
	 */
	public String toString() {
		String str = "";
		Iterator<TimeRange> it;
		
		it = timeRangeArray.iterator();
		while (it.hasNext()) {
			TimeRange tr;
			
			tr = it.next();
			str = str + tr.toString();
			if (it.hasNext()) {
				//str = str + ", ";
				str = str + "\n";
			}
		}
		
		return str;
	}
	
}
