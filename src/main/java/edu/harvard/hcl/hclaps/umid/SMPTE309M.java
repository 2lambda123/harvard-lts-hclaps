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
	HashMap codeToUTC;
	
	public SMPTE309M() {
		Iterator it;

		codeToUTC = new HashMap();
		
		codeToUTC.put(new Integer(0),new Float(0));  // UTC
		codeToUTC.put(new Integer(1),new Float(-1));  // UTC-01:00
		codeToUTC.put(new Integer(2),new Float(-2));  // UTC-02:00
		codeToUTC.put(new Integer(3),new Float(-3));  // UTC-03:00
		codeToUTC.put(new Integer(4),new Float(-4));  // UTC-04:00
		codeToUTC.put(new Integer(5),new Float(-5));  // UTC-05:00
		codeToUTC.put(new Integer(6),new Float(-6));  // UTC-06:00
		codeToUTC.put(new Integer(7),new Float(-7));  // UTC-07:00
		codeToUTC.put(new Integer(8),new Float(-8));  // UTC-08:00
		codeToUTC.put(new Integer(9),new Float(-9));  // UTC-09:00
		
		codeToUTC.put(new Integer(10),new Float(-0.5));  // UTC-00:30
		codeToUTC.put(new Integer(11),new Float(-1.5));  // UTC-01:30
		codeToUTC.put(new Integer(12),new Float(-2.5));  // UTC-02:30
		codeToUTC.put(new Integer(13),new Float(-3.5));  // UTC-03:30
		codeToUTC.put(new Integer(14),new Float(-4.5));  // UTC-04:30
		codeToUTC.put(new Integer(15),new Float(-5.5));  // UTC-05:30

		codeToUTC.put(new Integer(16),new Float(-10));  // UTC-10:00
		codeToUTC.put(new Integer(17),new Float(-11));  // UTC-11:00
		codeToUTC.put(new Integer(18),new Float(-12));  // UTC-12:00
		codeToUTC.put(new Integer(19),new Float(13));  // UTC+13:00
		codeToUTC.put(new Integer(20),new Float(12));  // UTC+12:00
		codeToUTC.put(new Integer(21),new Float(11));  // UTC+11:00
		codeToUTC.put(new Integer(22),new Float(10));  // UTC+10:00
		codeToUTC.put(new Integer(23),new Float(9));  // UTC+09:00
		codeToUTC.put(new Integer(24),new Float(8));  // UTC+08:00
		codeToUTC.put(new Integer(25),new Float(7));  // UTC+07:00
		
		codeToUTC.put(new Integer(26),new Float(-6.5));  // UTC-06:30
		codeToUTC.put(new Integer(27),new Float(-7.5));  // UTC-07:30
		codeToUTC.put(new Integer(28),new Float(-8.5));  // UTC-08:30
		codeToUTC.put(new Integer(29),new Float(-9.5));  // UTC-09:30
		codeToUTC.put(new Integer(30),new Float(-10.5));  // UTC-10:30
		codeToUTC.put(new Integer(31),new Float(-11.5));  // UTC-11:30
		
		codeToUTC.put(new Integer(32),new Float(6));  // UTC+06:00
		codeToUTC.put(new Integer(33),new Float(5));  // UTC+05:00
		codeToUTC.put(new Integer(34),new Float(4));  // UTC+04:00
		codeToUTC.put(new Integer(35),new Float(3));  // UTC+03:00
		codeToUTC.put(new Integer(36),new Float(2));  // UTC+02:00
		codeToUTC.put(new Integer(37),new Float(1));  // UTC+01:00
		
		codeToUTC.put(new Integer(38), null);  // Reserved: DO NOT USE
		codeToUTC.put(new Integer(39), null);  // Reserved: DO NOT USE
		
		codeToUTC.put(new Integer(40), null);  // TP-3
		codeToUTC.put(new Integer(41), null);  // TP-2
		
		codeToUTC.put(new Integer(42),new Float(11.5));  // UTC+11:30
		codeToUTC.put(new Integer(43),new Float(10.5));  // UTC+10:30
		codeToUTC.put(new Integer(44),new Float(9.5));  // UTC+09:30
		codeToUTC.put(new Integer(45),new Float(8.5));  // UTC+08:30
		codeToUTC.put(new Integer(46),new Float(7.5));  // UTC+07:30
		codeToUTC.put(new Integer(47),new Float(6.5));  // UTC+06:30

		codeToUTC.put(new Integer(48), null);  // TP-1
		codeToUTC.put(new Integer(49), null);  // TP-0

		codeToUTC.put(new Integer(50), new Float(12.75));  // UTC+12:45
		codeToUTC.put(new Integer(51), null);  // Reserved: DO NOT USE
		codeToUTC.put(new Integer(52), null);  // Reserved: DO NOT USE
		codeToUTC.put(new Integer(53), null);  // Reserved: DO NOT USE
		codeToUTC.put(new Integer(54), null);  // Reserved: DO NOT USE
		codeToUTC.put(new Integer(55), null);  // Reserved: DO NOT USE
		codeToUTC.put(new Integer(56), null);  // User defined
		codeToUTC.put(new Integer(57), null);  // Undefined
		
		codeToUTC.put(new Integer(58),new Float(5.5));  // UTC+05:30
		codeToUTC.put(new Integer(59),new Float(4.5));  // UTC+04:30
		codeToUTC.put(new Integer(60),new Float(3.5));  // UTC+03:30
		codeToUTC.put(new Integer(61),new Float(2.5));  // UTC+02:30
		codeToUTC.put(new Integer(62),new Float(1.5));  // UTC+01:30
		codeToUTC.put(new Integer(63),new Float(0.5));  // UTC+00:30

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
		
		i = (Integer)utcToCode.get(new Float(f));
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
