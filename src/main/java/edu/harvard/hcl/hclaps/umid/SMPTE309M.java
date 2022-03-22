//
//  SMPTE309M.java
//  hclaps
//
//  Created by Robert La Ferla on 5/1/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.umid;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * @author Robert La Ferla
 */

public class SMPTE309M {
	private static SMPTE309M instance = null;
	HashMap utcToCode;
	HashMap<Integer, Float> codeToUTC;
	
	public SMPTE309M() {
		Iterator it;

		codeToUTC = new HashMap();
		
		codeToUTC.put(0, (float)0);  // UTC
		codeToUTC.put(1, (float)-1);  // UTC-01:00
		codeToUTC.put(2, (float)-2);  // UTC-02:00
		codeToUTC.put(3, (float)-3);  // UTC-03:00
		codeToUTC.put(4, (float)-4);  // UTC-04:00
		codeToUTC.put(5, (float)-5);  // UTC-05:00
		codeToUTC.put(6, (float)-6);  // UTC-06:00
		codeToUTC.put(7, (float)-7);  // UTC-07:00
		codeToUTC.put(8, (float)-8);  // UTC-08:00
		codeToUTC.put(9, (float)-9);  // UTC-09:00
		
		codeToUTC.put(10, (float)-0.5);  // UTC-00:30
		codeToUTC.put(11, (float)-1.5);  // UTC-01:30
		codeToUTC.put(12, (float)-2.5);  // UTC-02:30
		codeToUTC.put(13, (float)-3.5);  // UTC-03:30
		codeToUTC.put(14, (float)-4.5);  // UTC-04:30
		codeToUTC.put(15, (float)-5.5);  // UTC-05:30

		codeToUTC.put(16, (float)-10);  // UTC-10:00
		codeToUTC.put(17, (float)-11);  // UTC-11:00
		codeToUTC.put(18, (float)-12);  // UTC-12:00
		codeToUTC.put(19, (float)13);  // UTC+13:00
		codeToUTC.put(20, (float)12);  // UTC+12:00
		codeToUTC.put(21, (float)11);  // UTC+11:00
		codeToUTC.put(22, (float)10);  // UTC+10:00
		codeToUTC.put(23, (float)9);  // UTC+09:00
		codeToUTC.put(24, (float)8);  // UTC+08:00
		codeToUTC.put(25, (float)7);  // UTC+07:00
		
		codeToUTC.put(26, (float)-6.5);  // UTC-06:30
		codeToUTC.put(27, (float)-7.5);  // UTC-07:30
		codeToUTC.put(28, (float)-8.5);  // UTC-08:30
		codeToUTC.put(29, (float)-9.5);  // UTC-09:30
		codeToUTC.put(30, (float)-10.5);  // UTC-10:30
		codeToUTC.put(31, (float)-11.5);  // UTC-11:30
		
		codeToUTC.put(32, (float)6);  // UTC+06:00
		codeToUTC.put(33, (float)5);  // UTC+05:00
		codeToUTC.put(34, (float)4);  // UTC+04:00
		codeToUTC.put(35, (float)3);  // UTC+03:00
		codeToUTC.put(36, (float)2);  // UTC+02:00
		codeToUTC.put(37, (float)1);  // UTC+01:00
		
		codeToUTC.put(38,  null);  // Reserved: DO NOT USE
		codeToUTC.put(39,  null);  // Reserved: DO NOT USE
		
		codeToUTC.put(40,  null);  // TP-3
		codeToUTC.put(41,  null);  // TP-2
		
		codeToUTC.put(42, (float)11.5);  // UTC+11:30
		codeToUTC.put(43, (float)10.5);  // UTC+10:30
		codeToUTC.put(44, (float)9.5);  // UTC+09:30
		codeToUTC.put(45, (float)8.5);  // UTC+08:30
		codeToUTC.put(46, (float)7.5);  // UTC+07:30
		codeToUTC.put(47, (float)6.5);  // UTC+06:30

		codeToUTC.put(48,  null);  // TP-1
		codeToUTC.put(49,  null);  // TP-0

		codeToUTC.put(50,  (float)12.75);  // UTC+12:45
		codeToUTC.put(51,  null);  // Reserved: DO NOT USE
		codeToUTC.put(52,  null);  // Reserved: DO NOT USE
		codeToUTC.put(53,  null);  // Reserved: DO NOT USE
		codeToUTC.put(54,  null);  // Reserved: DO NOT USE
		codeToUTC.put(55,  null);  // Reserved: DO NOT USE
		codeToUTC.put(56,  null);  // User defined
		codeToUTC.put(57,  null);  // Undefined
		
		codeToUTC.put(58, (float)5.5);  // UTC+05:30
		codeToUTC.put(59, (float)4.5);  // UTC+04:30
		codeToUTC.put(60, (float)3.5);  // UTC+03:30
		codeToUTC.put(61, (float)2.5);  // UTC+02:30
		codeToUTC.put(62, (float)1.5);  // UTC+01:30
		codeToUTC.put(63, (float)0.5);  // UTC+00:30

		// Create reverse map
		utcToCode = new HashMap();

		it = codeToUTC.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry mapEntry;
			
			mapEntry = (Map.Entry)it.next();
			utcToCode.put(mapEntry.getValue(), mapEntry.getKey());
		}
	}
	
	public static SMPTE309M getInstance() {
		if (instance == null) {
			instance = new SMPTE309M();
		}
		return instance;
	}
	
	public int codeForUTCOffset(float f) {
		Integer i;
		
		i = (Integer)utcToCode.get(f);
		return i.intValue();
	}
	
	public byte[] getMJDForCalendar(GregorianCalendar aCalendar) {
		int upper, lower;
		byte [] val;
		int offset;
		int code;
		int year;
		int month;
		int day;
		TimeZone tz;
		Date date;
		
		// System.out.println("getMJDForCalendar()");

		// Lookup code in hash table for given calendar's time zone
		tz = aCalendar.getTimeZone();
		offset = tz.getRawOffset();
		code = codeForUTCOffset(offset / 3600000);
		
		// Create packed BCD representation of date
		val = new byte[4];
		upper = 0x80;  // High bit means MJD.  No need to shift!
		lower = (code & 0x3f);
		val[0] = (byte)((upper | lower) & 0xff);
		
		year = aCalendar.get(Calendar.YEAR) % 100;
		upper = ((year / 10) << 4);
		lower = year % 10;
		val[1] = (byte)((upper | lower) & 0xff);
		
		month = aCalendar.get(Calendar.MONTH) + 1;
		upper = (month / 10) << 4;
		lower = month % 10;		
		val[2] = (byte)((upper | lower) & 0xff);

		day = aCalendar.get(Calendar.DAY_OF_MONTH);
		upper = (day / 10) << 4;
		lower = day % 10;
		val[3] = (byte)((upper | lower) & 0xff);
		
		return val;
	}
	
	public GregorianCalendar getCalendarForMJD(byte [] mjd) {
		GregorianCalendar cal, ncal;
		int year, month, day;
		int upper, lower;
		int code;
		Float utcOffset;
		SimpleTimeZone stz;
		float rawOffset;
		String tzString;
		
		//System.out.println("getCalendarForMJD()");

		// Unpack BCD to get timezone code
		upper = (int)(mjd[0] & 0x70) << 4;
		lower = (int)(mjd[0] & 0x0f);
		code = (upper * 10) + lower;

		// Lookup timezone code in table
		utcOffset = (Float)codeToUTC.get(code);
		rawOffset = utcOffset.floatValue() * 3600000;
		
		// Create a new calendar using offset from UTC timezone
		tzString = "UTC" + utcOffset.intValue();
		stz = new SimpleTimeZone((int)rawOffset, tzString);
		cal = new GregorianCalendar(stz);

		// Unpack BCD to get year, month and day
		upper = (int)(mjd[1] & 0xf0) << 4;
		lower = (int)(mjd[1] & 0x0f);
		year = 2000 + (upper * 10) + lower;
		
		upper = (int)(mjd[2] & 0xf0) << 4;
		lower = (int)(mjd[2] & 0x0f);
		month = (upper * 10) + lower;

		upper = (int)(mjd[3] & 0xf0) << 4;
		lower = (int)(mjd[3] & 0x0f);
		day = (upper * 10) + lower;
		
		// System.out.println(month + "/" + day + "/" + year);
		
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);

		// Convert UTC time back to local time
		ncal = new GregorianCalendar();
		ncal.setTime(cal.getTime());
		return ncal;
	}
	
}
