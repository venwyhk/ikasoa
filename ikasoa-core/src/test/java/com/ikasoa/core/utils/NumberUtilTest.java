package com.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Number工具单元测试
 */
public class NumberUtilTest extends TestCase {

	@Test
	public void testGetRandomInt() {
		int bound = 10;
		assertTrue(NumberUtil.getRandomInt(bound) < bound);
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
		System.out.print(NumberUtil.getDouble(d, 2));
		assertEquals(NumberUtil.getDouble(d, 2), 12.35);
	}

}
