package com.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Number工具单元测试
 */
public class NumberUtilTest extends TestCase {

	@Test
	public void testGetRandomInt() {
		int min = 10;
		int max = 100;
		int num = NumberUtil.getRandomInt(min, max);
		assertTrue(num > min);
		assertTrue(num <= max);
	}

	@Test
	public void testGetRandomLong() {
		long bound = 1234567890L;
		assertTrue(NumberUtil.getRandomLong(bound) < bound);
	}

	@Test
	public void testGetRandomFloat() {
		float bound = 12.34F;
		assertTrue(NumberUtil.getRandomFloat(bound, 2) < bound);
	}

	@Test
	public void testGetRandomDouble() {
		double bound = 12.345;
		assertTrue(NumberUtil.getRandomDouble(bound, 2) < bound);
	}

	@Test
	public void testGetDouble() {
		double d = 12.345;
		assertEquals(NumberUtil.getDouble(d, 2), 12.35);
	}

}
