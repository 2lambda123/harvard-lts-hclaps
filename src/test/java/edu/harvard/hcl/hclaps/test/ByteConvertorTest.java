//
//  ByteConvertorTest.java
//  hclaps
//
//  Created by Robert La Ferla on 5/2/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.test;

import junit.framework.TestCase;
import edu.harvard.hcl.hclaps.util.*;

public class ByteConvertorTest extends TestCase {

	private void dumpArray(byte[] array) {
		System.out.println();
		for (int i = 0; i < array.length; i++) {
			System.out.println("array[" + i +"] = " + array[i]);
		}
	}
	

	// SHORT LITTLE-ENDIAN
	
	public void testLittleEndianShortInversion259() {
		short before;
		short after;
		byte [] array;
		
		before = (short)259;
		array = ByteConvertor.bytesForShort(before, ByteConvertor.LITTLE);
		after = ByteConvertor.shortForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}

	// INT LITTLE-ENDIAN
	
	public void testLittleEndianIntInversion259() {
		int before;
		int after;
		byte [] array;
		
		before = 259;
		array = ByteConvertor.bytesForInt(before, ByteConvertor.LITTLE);
		after = ByteConvertor.intForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}
	
	
	// LONG LITTLE-ENDIAN
	public void testLittleEndianLongInversion259() {
		long before;
		long after;
		byte [] array;
		
		before = 259L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}
	
	public void testLittleEndianLongInversion1() {
		long before;
		long after;
		byte [] array;
		
		before = 1L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}
	
	public void testLittleEndianLongInversion13() {
		long before;
		long after;
		byte [] array;
		
		before = 12L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}
	
	public void testLittleEndianLongInversion128() {
		long before;
		long after;
		byte [] array;
		
		before = 128L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}
	
	public void testLittleEndianLongInversion255() {
		long before;
		long after;
		byte [] array;
		
		before = 255L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}
	
	public void testLittleEndianLongInversion256() {
		long before;
		long after;
		byte [] array;
		
		before = 256L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}
	
	public void testLittleEndianLongInversion65536() {
		long before;
		long after;
		byte [] array;
		
		before = 65536L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
	}
	
	public void testLittleEndianLongInversion2147483647() {
		long before;
		long after;
		byte [] array;
		
		before = 2147483647L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
		
	}

	public void testLittleEndianLongInversion4294967295() {
		long before;
		long after;
		byte [] array;
		
		before = 4294967295L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.LITTLE);
		after = ByteConvertor.longForBytes(array, ByteConvertor.LITTLE);
		assertEquals(before, after);
		
	}
	
	
/////////////////////////////////////////////	
	
	
	public void testBigEndianLongInversion259() {
		long before;
		long after;
		byte [] array;
		
		before = 259L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);
	}
	
	public void testBigEndianLongInversion1() {
		long before;
		long after;
		byte [] array;
		
		before = 1L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);
	}
	
	public void testBigEndianLongInversion13() {
		long before;
		long after;
		byte [] array;
		
		before = 12L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);
	}
	
	public void testBigEndianLongInversion128() {
		long before;
		long after;
		byte [] array;
		
		before = 128L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);
	}
	
	public void testBigEndianLongInversion255() {
		long before;
		long after;
		byte [] array;
		
		before = 255L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);
	}
	
	public void testBigEndianLongInversion256() {
		long before;
		long after;
		byte [] array;
		
		before = 256L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);
	}
	
	public void testBigEndianLongInversion65536() {
		long before;
		long after;
		byte [] array;
		
		before = 65536L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);
	}
	
	public void testBigEndianLongInversion2147483647() {
		long before;
		long after;
		byte [] array;
		
		before = 2147483647L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);
		
	}
	
	public void testBigEndianLongInversion4294967295() {
		long before;
		long after;
		byte [] array;
		
		before = 4294967295L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);		
	}
	
	public void testBigEndianLongInversion4294967296() {
		long before;
		long after;
		byte [] array;
		
		before = 4294967296L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);		
	}

	public void testBigEndianLongInversion4294967297() {
		long before;
		long after;
		byte [] array;
		
		before = 4294967297L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		assertEquals(before, after);		
	}

	public void testBigEndianLongInversion9223372036854775807() {
		long before;
		long after;
		byte [] array;
		
		before = 9223372036854775807L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		//dumpArray(array);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		//System.out.println("before = " + before);
		//System.out.println("after = " + after);
		assertEquals(before, after);		
	}
	
	public void testBigEndianLongInversion18446744073709551615() {
		long before;
		long after;
		byte [] array;
		
		before = -1L;
		array = ByteConvertor.bytesForLong(before, ByteConvertor.BIG);
		//dumpArray(array);
		after = ByteConvertor.longForBytes(array, ByteConvertor.BIG);
		//System.out.println("before = " + before);
		//System.out.println("after = " + after);
		assertEquals(before, after);		
	}
	
	
}
