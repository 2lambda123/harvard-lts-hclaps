//
//  AES31Time.java
//  APXE
//
//  Created by Robert La Ferla (robertlaferla@comcast.net) on Thu Jan 22 2004.
//  Transferred from APXE to hclaps on 3/1/2007
//
//  Copyright 2004 by the President and Fellows of Harvard College.
//

package edu.harvard.hcl.hclaps.util;

import org.aes.tcf.*;
import org.aes.tcf.types.*;
import java.util.*;
import java.text.NumberFormat;
import java.math.BigDecimal;

/**
 * AES31Time is a collection of utility methods for dealing with AES-31 time code character format.
 * 
 * @author Robert La Ferla
 */
public class AES31Time {
	private final static int FRAME_ERROR_PER_HOUR		= 108;
	private final static int MINUTES_PER_HOUR			=  60;
	private final static int SECONDS_PER_MINUTE			=  60;
	private final static int DECIMAL_SCALE				=  64;
	
	public static TimeCodeCharacterFormatType parseString(String timeCodeString) {
		TimeCodeCharacterFormatType tccft;
		
		//System.out.println("parseString(" + timeCodeString + ")");
		tccft = new TimeCodeCharacterFormatType();
		tccft.setHours(Integer.parseInt(timeCodeString.substring(0,2)));
		tccft.setMinutes(Integer.parseInt(timeCodeString.substring(3,5)));
		tccft.setSeconds(Integer.parseInt(timeCodeString.substring(6,8)));
		tccft.setFrames(Integer.parseInt(timeCodeString.substring(9,11)));
		
		if (timeCodeString.length() > 11) {
			SamplesType st;
			char indicatorVal;
			String rateName;
			int numSamples;
			
			st = new SamplesType();
			indicatorVal = timeCodeString.charAt(11);
			rateName = TCFData.getSampleRateNameForIndicator(indicatorVal);
			// System.out.println("rateName = " + rateName);
			st.setSampleRate(SampleRateType.valueOf(rateName));
			numSamples = Integer.parseInt(timeCodeString.substring(12,16));
			st.setNumberOfSamples(numSamples);
			//System.out.println("NumberOfSamples = " + numSamples);
			tccft.setSamples(st);
		}
		
		//System.out.println("timeCodeString.charAt(2)=" + timeCodeString.charAt(2));
		tccft.setFrameCount(TCFData.getFrameCountForIndicator(timeCodeString.charAt(2)));
		tccft.setTimeBase(TCFData.getTimeBaseForIndicator(timeCodeString.charAt(2)));		
		
		//System.out.println("timeCodeString.charAt(8)=" + timeCodeString.charAt(8));
		tccft.setVideoField(TCFData.getVideoFieldForIndicator(timeCodeString.charAt(8)));
		tccft.setCountingMode(TCFData.getCountingModeForIndicator(timeCodeString.charAt(8)));
		
		//System.out.println("timeCodeString.charAt(5)=" + timeCodeString.charAt(5));
		if (tccft.getCountingMode() == CountingModeType.PAL) {
			PalFilmFramingType pfft;
			
			pfft = new PalFilmFramingType();
			tccft.setFilmFraming(pfft);
			// Is the frame count/timebase indicator repeated?
			if (timeCodeString.charAt(5) == timeCodeString.charAt(2)) {
				pfft.setFraming(PalListType.NOT_APPLICABLE);
			}
		}
		else {
			NtscFilmFramingType nfft;
			
			nfft = new NtscFilmFramingType();
			tccft.setFilmFraming(nfft);
			// Is the frame count/timebase indicator repeated?
			if (timeCodeString.charAt(5) == timeCodeString.charAt(2)) {
				nfft.setFraming(NtscListType.NOT_APPLICABLE);
			}
		}
		
		// System.out.println("tccft = " + toString(tccft));
		//System.out.println("tccft.getHours() = " + tccft.getHours());
		
		return tccft;
	}
	
	public static String toString(TimeCodeCharacterFormatType tccft) {
		FilmFramingType fft;
		String str;
		String sampleStr = "";
		char ind1;
		char ind2;
		char ind3;
		char ind4;		
		
		// System.out.println("tccft = " + tccft);
		
		// System.out.println("tccft.getFrameCount() = " + tccft.getFrameCount());
		// System.out.println("tccft.getTimeBase() = " + tccft.getTimeBase());
		
		// First Indicator (1)
		ind1 = TCFData.getIndicatorForFrameCountAndTimeBase(tccft.getFrameCount(), tccft.getTimeBase()); 
		
		// Second Indicator (2)
		fft = tccft.getFilmFraming();
		if (fft.getClass() == NtscFilmFramingType.class) {
			NtscFilmFramingType nfft;
			NtscListType nlt;
			int idx;
			
			nfft = (NtscFilmFramingType)fft;
			nlt = nfft.getFraming();
			idx = nlt.getType();
			if (idx != NtscListType.NOT_APPLICABLE_TYPE) {
				ind2 = TCFData.getIndicatorForNTSCListType(nlt);
			}
			else {
				ind2 = ind1;
			}
		}
		else {
			PalFilmFramingType pfft;
			PalListType plt;
			int idx;
			
			pfft = (PalFilmFramingType)fft;
			plt = pfft.getFraming();
			idx = plt.getType();
			if (idx != PalListType.NOT_APPLICABLE_TYPE) {
				ind2 = TCFData.getIndicatorForPALListType(plt);
			}
			else {
				ind2 = ind1;
			}
		}
		
		// Third Indicator (3)
		ind3 = TCFData.getIndicatorForCountingModeAndVideoField(tccft.getCountingMode(), tccft.getVideoField());
		
		// Construct time code group
		str = pad(tccft.getHours()) + ind1 + pad(tccft.getMinutes()) + ind2 + pad(tccft.getSeconds()) + ind3 + pad(tccft.getFrames());
		if (tccft.getSamples() != null) {
			SampleRateType sampleRate;
			NumberFormat nf;
			
			nf = NumberFormat.getIntegerInstance();
			nf.setMinimumIntegerDigits(4);
			nf.setGroupingUsed(false);
			
			sampleRate = tccft.getSamples().getSampleRate();
			//System.out.println("sampleRate = " + sampleRate);
			// Fourth Indicator (4)
			ind4 = TCFData.getIndicatorForSampleRate(sampleRate.toString());
			// System.out.println("ind4 = " + ind4);
			sampleStr = ind4 + sampleStr + nf.format(tccft.getSamples().getNumberOfSamples());
		}
		return str+sampleStr;
	}
	
	
	public static BigDecimal getNumberOfSecondsForTimeCode(TimeCodeCharacterFormatType tccft) {
		BigDecimal time;
		SamplesType st;
		int frameCount = 0;
		double sampleRate = (double)0;
		double timeBase = (double)0;
		
		frameCount = getFrameCountForType(tccft.getFrameCount());
		
		//discover the samplerate for this tcf object
		st = tccft.getSamples();
		sampleRate = getSampleRateForType(st.getSampleRate());
		
		// Timebase
		timeBase = getTimeBaseForType(tccft.getTimeBase());
		
		//recalc the timecode as seconds
		time = new BigDecimal(0.0);
		time.setScale(DECIMAL_SCALE, BigDecimal.ROUND_UNNECESSARY);
		
		time = time.add(new BigDecimal(tccft.getHours() * MINUTES_PER_HOUR * SECONDS_PER_MINUTE));
		time = time.add(new BigDecimal(tccft.getMinutes() * SECONDS_PER_MINUTE));
		time = time.add(new BigDecimal(tccft.getSeconds()));
		time = time.add(new BigDecimal(tccft.getFrames() * (1.0 / frameCount)));
		if (st.hasNumberOfSamples()) {
			time = time.add(new BigDecimal(st.getNumberOfSamples() * 1.0 / sampleRate));
		}
		
		if (tccft.getCountingMode().getType() == CountingModeType.NTSC_DROP_FRAME_TYPE) {
			int minutesToDrop = ((tccft.getHours() * MINUTES_PER_HOUR) + tccft.getMinutes());
			int framesToDrop = ((minutesToDrop - (minutesToDrop / 10)) * 2);
			time = time.subtract(new BigDecimal(framesToDrop * 1.0 / frameCount));
		}
		
		time = time.multiply(new BigDecimal(timeBase));
		
		return time;
	}
	
	public static TimeCodeCharacterFormatType convertFromBigDecimal(BigDecimal time, SampleRateType sampleRateType, double timeBaseValue, int frameCountValue, CountingModeType countingModeType) {
		TimeCodeCharacterFormatType tccft;
		SamplesType st;
		int sampleRate;
		
		tccft = new TimeCodeCharacterFormatType();
		st = new SamplesType();
		tccft.setSamples(st);
		
		time.setScale(DECIMAL_SCALE,  BigDecimal.ROUND_HALF_EVEN);
		time = time.multiply(new BigDecimal(1.0 / timeBaseValue));
		
		int frames = time.divide(new BigDecimal(1.0 / frameCountValue), DECIMAL_SCALE,  BigDecimal.ROUND_HALF_EVEN).intValue();
		BigDecimal remainder = time.divide(new BigDecimal(1.0/frameCountValue), DECIMAL_SCALE,  BigDecimal.ROUND_HALF_EVEN);
		remainder = remainder.subtract(BigDecimal.valueOf(frames));
		
		if (countingModeType == CountingModeType.NTSC_DROP_FRAME) {
			int minutesToDrop = ((frames/frameCountValue) / SECONDS_PER_MINUTE);
			int framesToDrop = ((minutesToDrop - (minutesToDrop / 10)) * 2);
			frames += framesToDrop;
		}
		
		int seconds = frames/frameCountValue;
		frames -= seconds * frameCountValue;
		
		int minutes = seconds / SECONDS_PER_MINUTE;
		seconds -= minutes * SECONDS_PER_MINUTE;
		
		int hours = minutes / MINUTES_PER_HOUR;
		minutes -= hours * MINUTES_PER_HOUR;
		
		sampleRate = getSampleRateForType(sampleRateType);
		float samples = remainder.multiply(new BigDecimal(sampleRate/frameCountValue * timeBaseValue)).floatValue();
		
		float samplesRemainder = samples - (int)samples;
		samples = (int)samples;
		if (samplesRemainder >= 0.5) {
			samples++;
		}
		
		
		//System.out.println("H-> " + hours + " M-> " + minutes + " S-> " + seconds + " F-> " + frames + " s-> " + (int)samples);		
		
		st.setSampleRate(sampleRateType);
		st.setNumberOfSamples((int)samples);
		tccft.setHours(hours);
		tccft.setMinutes(minutes);
		tccft.setSeconds(seconds);
		tccft.setFrames(frames);
		return tccft;
	}
	
	public static int getFrameCountForType(FrameCountType frameCountType) {
		int frameCount = 0;
		
		switch (frameCountType.getType()) {
			case FrameCountType.VALUE_30_TYPE:
				frameCount = 30;
				break;
			case FrameCountType.VALUE_25_TYPE:
				frameCount = 25;
				break;
			case FrameCountType.VALUE_24_TYPE:
				frameCount = 24;
				break;
		}
		return frameCount;
	}
	
	public static double getTimeBaseForType(TimeBaseType timeBaseType) {
		double timeBase = (double)0;
		
		switch (timeBaseType.getType()) {
			case TimeBaseType.UNKNOWN_TYPE: // Unknown
				timeBase = 1.000;
				System.out.println("Unknown timebase encountered, will use 1.000"); 
				break;
			case TimeBaseType.VALUE_1000_TYPE: // 1.0
				timeBase = 1.000;
				break;
			case TimeBaseType.VALUE_1001_TYPE:
				timeBase = 1.001001001001001001;
				break;
		}
		return timeBase;
	}
	
	public static int getSampleRateForType(SampleRateType sampleRateType) {
		int sampleRate = 0;
		
		sampleRate = Integer.parseInt(sampleRateType.toString().substring(1));
		return sampleRate;
	}
	
	private static String pad(int num) {
		String str = "";
		
		if (num <  10) {
			str = "0";
		}
		str = str + num;
		return str;
	}
	
}
