//
//  TCFData.java
//  APXE
//
//  Created by Robert La Ferla (robertlaferla@comcast.net) on Wed Mar 24 2004.
//  Transferred from APXE to hclaps on 3/1/2007
//
//  Copyright 2004 by the President and Fellows of Harvard College.
//

package edu.harvard.hcl.hclaps.util;

import org.aes.tcf.types.*;
import org.aes.tcf.data.*;
import java.io.*;
import org.exolab.castor.xml.*;
import java.util.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author Robert La Ferla
 */

/**
 * Class TCFData.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/11/22 21:48:40 $
 */
public class TCFData {
	static TcfDataEnumerationType tdet = null;
	
	static {		
		InputStream is;
		
		is = new TCFData().getClass().getResourceAsStream("tcfdata.xml");
		try {
			InputStreamReader isr;

			isr = new InputStreamReader(is, Charset.forName("UTF-8"));
			tdet = (TcfDataEnumerationType)Unmarshaller.unmarshal(TcfDataEnumerationType.class, isr);
		}
		catch (MarshalException e) {
			System.out.println("Exception = " + e);
		}
		catch (ValidationException e) {
			System.out.println("Exception = " + e);
		}
		
		//System.out.println("tdet=" + tdet);
	}
	
	public static char getIndicatorForSampleRate(String rateName) {
		char indicator = (char)0;
		SampleRateTableType srtt;
		
		//System.out.println("rateName=" + rateName);
		srtt = tdet.getSampleRateTable();
		for (Iterator it = srtt.getRowAsReference().iterator(); it.hasNext() ;) {
			SampleRateRowType srrt;
			
			srrt = (SampleRateRowType)it.next();
			//System.out.println("srrt.getRateName()=" + srrt.getRateName() + " srrt.getIndicator()=" + srrt.getIndicator());
			if (srrt.getRateName().equals(rateName)) {
				indicator = (char)srrt.getIndicator();
				break;
			}
		}
		return indicator;
	}
	
	public static String getSampleRateNameForIndicator(char indicator) {
		String sampleRateName = null;
		SampleRateTableType srtt;
		
		srtt = tdet.getSampleRateTable();
		for (Iterator it = srtt.getRowAsReference().iterator(); it.hasNext() ;) {
			SampleRateRowType srrt;
			
			srrt = (SampleRateRowType)it.next();
			if ((char)srrt.getIndicator() == indicator) {
				sampleRateName = srrt.getRateName();
				break;
			}
		}
		return sampleRateName;
	}
	
	public static char getIndicatorForPALListType(PalListType plt) {
		char indicator = (char)0;
		Pal_11_3_TableType ptt;
		String palTypeName;
		
		palTypeName = plt.toString();
		ptt = tdet.getPal_11_3_Table();
		for (Iterator it = ptt.getRowAsReference().iterator(); it.hasNext() ;) {
			Pal_11_3_RowType prt;
			
			prt = (Pal_11_3_RowType)it.next();
			if (prt.getEnumValue().equals(palTypeName)) {
				indicator = (char)prt.getIndicator();
				break;
			}
		}
		return indicator;
	}
	
	public static char getIndicatorForNTSCListType(NtscListType nlt) {
		char indicator = (char)0;
		Ntsc_2_3_TableType ntt;
		String ntscTypeName;
		
		ntscTypeName = nlt.toString();
		ntt = tdet.getNtsc_2_3_Table();
		for (Iterator it = ntt.getRowAsReference().iterator(); it.hasNext() ;) {
			Ntsc_2_3_RowType nrt;
			
			nrt = (Ntsc_2_3_RowType)it.next();
			if (nrt.getEnumValue().equals(ntscTypeName)) {
				indicator = (char)nrt.getIndicator();
				break;
			}
		}
		return indicator;
	}
	
	public static char getIndicatorForFrameCountAndTimeBase(FrameCountType fct, TimeBaseType tbt) {
		char indicator = (char)0;
		FrameCountTableType fctt;
		int framecount;
		String timebase = null;
		
		//System.out.println("getIndicatorForFrameCountAndTimeBase(" + fct + "," + tbt + ")");
		framecount = Integer.parseInt(fct.toString());
		switch(tbt.getType()) {
			case TimeBaseType.UNKNOWN_TYPE:
				timebase = "Unknown";
				break;
			case TimeBaseType.VALUE_1000_TYPE:
				timebase = "1.000";
				break;
			case TimeBaseType.VALUE_1001_TYPE:
				timebase = "1.001";
				break;
		}
		
		fctt = tdet.getFrameCountTable();
		for (Iterator it = fctt.getRowAsReference().iterator(); it.hasNext() ;) {
			FrameCountRowType fcrt;
			
			fcrt = (FrameCountRowType)it.next();
			if (fcrt.getFrameCount() == framecount && fcrt.getTimeBase().equals(timebase)) {
				indicator = (char)fcrt.getIndicator();
				break;
			}
		}
		return indicator;
	}
	
	public static FrameCountType getFrameCountForIndicator(char c) {
		int frameCount = 0;
		FrameCountType fct = null;
		FrameCountTableType fctt;
		
		fctt = tdet.getFrameCountTable();
		for (Iterator it = fctt.getRowAsReference().iterator(); it.hasNext() ;) {
			FrameCountRowType fcrt;
			
			fcrt = (FrameCountRowType)it.next();
			if (fcrt.getIndicator() == c) {
				frameCount = fcrt.getFrameCount();
				break;
			}
		}
		switch(frameCount) {
			case 24:
				fct = FrameCountType.VALUE_24;
				break;
			case 25:
				fct = FrameCountType.VALUE_25;
				break;
			case 30:
				fct = FrameCountType.VALUE_30;
				break;
		}
		return fct;
	}
	
	public static TimeBaseType getTimeBaseForIndicator(char c) {
		TimeBaseType tbt = null;
		FrameCountTableType fctt;
		String timeBase = null;
		
		fctt = tdet.getFrameCountTable();
		for (Iterator it = fctt.getRowAsReference().iterator(); it.hasNext() ;) {
			FrameCountRowType fcrt;
			
			fcrt = (FrameCountRowType)it.next();
			if (fcrt.getIndicator() == c) {				
				timeBase = fcrt.getTimeBase();
				break;
			}
		}
		if (timeBase.equals("Unknown")) {
			tbt = TimeBaseType.UNKNOWN;
		}
		else if (timeBase.equals("1.000")) {
			tbt = TimeBaseType.VALUE_1000;
		}
		else if (timeBase.equals("1.001")) {
			tbt = TimeBaseType.VALUE_1001;
		}
		return tbt;
	}	
	
	
	public static char getIndicatorForCountingModeAndVideoField(CountingModeType cmt, VideoFieldType vft) {
		char indicator = (char)0;
		CountingModeTableType cmtt;
		int field = 0;
		String countingmode = null;
		
		countingmode = cmt.toString();
		switch(vft.getType()) {
			case VideoFieldType.FIELD_1_TYPE:
				field = 1;
				break;
			case VideoFieldType.FIELD_2_TYPE:
				field = 2;
				break;
		}
		
		cmtt = tdet.getCountingModeTable();
		for (Iterator it = cmtt.getRowAsReference().iterator(); it.hasNext() ;) {
			CountingModeRowType cmrt;
			
			cmrt = (CountingModeRowType)it.next();
			if (cmrt.getCountingMode().equals(countingmode) && cmrt.getVideoField() == field) {
				indicator = (char)cmrt.getIndicator();
				break;
			}
		}
		return indicator;
	}
	
	public static VideoFieldType getVideoFieldForIndicator(char c) {
		int videoField = 0;
		VideoFieldType vft = null;
		CountingModeTableType cmtt;
		
		cmtt = tdet.getCountingModeTable();
		for (Iterator it = cmtt.getRowAsReference().iterator(); it.hasNext() ;) {
			CountingModeRowType cmrt;
			
			cmrt = (CountingModeRowType)it.next();
			if (cmrt.getIndicator() == c) {
				videoField = cmrt.getVideoField();
				break;
			}
		}
		switch(videoField) {
			case 1:
				vft = VideoFieldType.FIELD_1;
				break;
			case 2:
				vft = VideoFieldType.FIELD_2;
				break;
		}
		return vft;
	}
	
	public static CountingModeType getCountingModeForIndicator(char c) {
		String countingmode = null;
		CountingModeType cmt = null;
		CountingModeTableType cmtt;
		
		cmtt = tdet.getCountingModeTable();
		for (Iterator it = cmtt.getRowAsReference().iterator(); it.hasNext() ;) {
			CountingModeRowType cmrt;
			
			cmrt = (CountingModeRowType)it.next();
			if (cmrt.getIndicator() == c) {
				countingmode = cmrt.getCountingMode();
				break;
			}
		}
		if (countingmode.equals("PAL")) {
			cmt = CountingModeType.PAL;
		}
		else if (countingmode.equals("NTSC_NON_DROP_FRAME")) {
			cmt = CountingModeType.NTSC_NON_DROP_FRAME;
		}
		else if (countingmode.equals("NTSC_DROP_FRAME_TYPE")) {
			cmt = CountingModeType.NTSC_DROP_FRAME;
		}
		return cmt;
	}
	
	
}
