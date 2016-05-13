package com.ikamobile.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * 字符串工具单元测试
 */
public class StringUtilTest extends TestCase {

	@Test
	public void testIsEmpty() {
		assertTrue(StringUtil.isEmpty(null));
		assertTrue(StringUtil.isEmpty(""));
		assertFalse(StringUtil.isEmpty(" "));
	}

	@Test
	public void testIsNotEmpty() {
		assertFalse(StringUtil.isNotEmpty(null));
		assertFalse(StringUtil.isNotEmpty(""));
		assertTrue(StringUtil.isNotEmpty(" "));
	}

	@Test
	public void testIsBlank() {
		assertTrue(StringUtil.isBlank(null));
		assertTrue(StringUtil.isBlank(""));
		assertTrue(StringUtil.isBlank(" "));
	}

	@Test
	public void testIsNotBlank() {
		assertFalse(StringUtil.isNotBlank(null));
		assertFalse(StringUtil.isNotBlank(""));
		assertFalse(StringUtil.isNotBlank(" "));
	}

	@Test
	public void testEquals() {
		assertTrue(StringUtil.equals("abc", "abc"));
		assertFalse(StringUtil.equals("abc", "abcd"));
	}

	@Test
	public void testToInt() {
		assertEquals(StringUtil.toInt("123"), 123);
	}

}
