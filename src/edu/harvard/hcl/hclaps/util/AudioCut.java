//
//  AudioCut.java
//  adlmarker
//
//  Created by Robert La Ferla on 9/29/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import java.util.*;
import com.therockquarry.aes31.adl.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Robert La Ferla
 */

public class AudioCut {
	String path = null;
	long srcIn = 0L;
	long destIn = 0L;
	long destOut = 0L;
	long preGap = 0L;
	long postGap = 0L;
	long fileIn = 0L;
	
	public void setPath(String aString) {
		path = aString;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setSrcIn(long l) {
		srcIn = l;
	}
	
	public long getSrcIn() {
		return srcIn;
	}

	public void setDestIn(long l) {
		destIn = l;
	}
	
	public long getDestIn() {
		return destIn;
	}	

	public void setDestOut(long l) {
		destOut = l;
	}
	
	public long getDestOut() {
		return destOut;
	}
	
	public void setPreGap(long l) {
		preGap = l;
	}
	
	public long getPreGap() {
		return preGap;
	}

	public void setPostGap(long l) {
		postGap = l;
	}
	
	public long getPostGap() {
		return postGap;
	}

	public void setFileIn(long l) {
		fileIn = l;
	}
	
	public long getFileIn() {
		return fileIn;
	}	
	
	public static String timeForSamples(long samples, double sampleRate) {
		BigDecimal total;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		int milliseconds = 0;
		BigDecimal mantissa;
		
		//System.out.println("nptForSamples(" + samples + ")");
		//System.out.println("sampleRate = " + timeline.getSampleRate());
		
		total = new BigDecimal(samples);
		total = total.divide(new BigDecimal(sampleRate), 48, RoundingMode.HALF_EVEN);
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
		mantissa = mantissa.multiply(new BigDecimal(1000));
		//System.out.println("mantissa = " + mantissa);
		milliseconds = mantissa.intValue();
		//System.out.println("milliseconds = " + milliseconds);
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds);
	}
	
	public static ArrayList<AudioCut> getAudioCutsFor(ADLSection adls, int channel, long start, long end) {
		ArrayList<AudioCut> audioCuts;
		EventListSection els;
		SequenceSection sqs;
		ArrayList eventEntriesForChannel;
		int numberOfEventEntries;
		ArrayList<CutEditEntry> cuts;
		Iterator<CutEditEntry> cit;
		CutEditEntry firstCut, lastCut;
		int cutnum;
		
		// System.out.println("getAudioCutsFor(" + channel + "," + start + "," + end + ")");
		
		els = adls.getEventListSection();
		sqs = adls.getSequenceSection();
		
		audioCuts = new ArrayList<AudioCut>();
		eventEntriesForChannel = els.getEventEntriesForDestChannel(channel);		
		numberOfEventEntries = eventEntriesForChannel.size();
		
		// Adjust destIn of cuts that overlap.
		for (int i = numberOfEventEntries; i > 1; i--) {
			CutEditEntry cee, pcee;
			
			cee = (CutEditEntry)eventEntriesForChannel.get(i - 1);
			pcee = (CutEditEntry)eventEntriesForChannel.get(i - 2);
			if (cee.getDestIn().valueOf() <= pcee.getDestOut().valueOf()) {
				TcfToken tt;
				long samples;
				
				samples = cee.getDestIn().valueOf();
				tt = new TcfToken(samples - 1, sqs.getSeqSampleRateAsDouble(), cee.getDestIn().getTcfTokenFormatProperties());
				pcee.setDestOut(tt);
			}
		}
		
		cuts = els.findCutsInDestChannelBetween(channel, start, end);
		
		firstCut = cuts.get(0);
		lastCut = cuts.get(cuts.size() - 1);
		cit = cuts.iterator();
		cutnum = 0;
		while (cit.hasNext()) {
			CutEditEntry cee;
			AudioCut ac;
			long srcIn, destIn, destOut;
			FileSourceIndexEntry fsie;
			
			cee = cit.next();
			// System.out.println("si=" + cee.getSrcIn().valueOf() + " di=" + cee.getDestIn().valueOf() + " do=" + cee.getDestOut().valueOf());
			// System.out.println("si=" + timeForSamples(cee.getSrcIn().valueOf()) + " di=" + timeForSamples(cee.getDestIn().valueOf()) + " do=" + timeForSamples(cee.getDestOut().valueOf()));
			srcIn = cee.getSrcIn().valueOf();
			destIn = cee.getDestIn().valueOf();
			destOut = cee.getDestOut().valueOf();
			
			ac = new AudioCut();
			
			if (destIn < start) {
				ac.setDestIn(start);
			}
			else {
				ac.setDestIn(destIn);
			}
			
			if (destOut > end) {
				ac.setDestOut(end);
			}
			else {
				ac.setDestOut(destOut);
			}
			ac.setSrcIn(srcIn);
			
			if (cee == firstCut) {
				if (destIn > start) {
					ac.setPreGap(destIn - start);
				}
			}
			else {
				CutEditEntry pcee;
				
				pcee = cuts.get(cutnum - 1);
				ac.setPreGap(destIn - pcee.getDestOut().valueOf());
			}
			
			if (cee == lastCut) {
				if (destOut < end) {
					ac.setPostGap(end - destOut);
				}
			}
			
			fsie = getFileForCutEdit(cee);
			ac.setPath(fsie.getFilePath());
			ac.setFileIn(fsie.getFileIn().valueOf());
			audioCuts.add(ac);
			cutnum++;
		}
		
		return audioCuts;
	}
	
	public static FileSourceIndexEntry getFileForCutEdit(CutEditEntry cee) {
		FileSourceIndexEntry fsie = null;
		SourceEntry se;
		
		se = cee.getSourceIndexEntry();
		try {
			fsie = (FileSourceIndexEntry)se.getIndexEntries(SourceEntry.SourceType.FILE_SRC).get(0);
		}
		catch (InvalidDataException ex) {
			ex.printStackTrace();
		}
		return fsie;
	}
	
	public String toString() {
		String str;
		
		str = "si=" + srcIn + " di=" + destIn + " do=" + destOut + " fi=" + fileIn + " pre=" + preGap + " post=" + postGap + " path=" + path;
		
		return str;
	}
}
