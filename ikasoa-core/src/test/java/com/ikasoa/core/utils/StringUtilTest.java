package com.ikasoa.core.utils;

import org.junit.Test;

import com.ikasoa.core.TestConstants;

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
	public void testAndIsEmpty() {
		assertTrue(StringUtil.andIsEmpty(null, ""));
		assertTrue(StringUtil.andIsEmpty(null, "", ""));
		assertFalse(StringUtil.andIsEmpty(null, " "));
		assertFalse(StringUtil.andIsEmpty("", " "));
		assertFalse(StringUtil.andIsEmpty(" ", "a"));
	}

	@Test
	public void testOrIsEmpty() {
		assertTrue(StringUtil.orIsEmpty(null, ""));
		assertTrue(StringUtil.orIsEmpty(null, " "));
		assertTrue(StringUtil.orIsEmpty("", " "));
		assertFalse(StringUtil.orIsEmpty(" ", "a"));
		assertTrue(StringUtil.orIsEmpty(null, " ", "a"));
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
	public void testAndIsBlank() {
		assertTrue(StringUtil.andIsBlank(null, ""));
		assertTrue(StringUtil.andIsBlank(null, "", ""));
		assertTrue(StringUtil.andIsBlank(null, " "));
		assertTrue(StringUtil.andIsBlank("", " "));
		assertFalse(StringUtil.andIsBlank(" ", "a"));
	}

	@Test
	public void testOrIsBlank() {
		assertTrue(StringUtil.orIsBlank(null, ""));
		assertTrue(StringUtil.orIsBlank("", " "));
		assertFalse(StringUtil.orIsBlank("a", "b"));
		assertTrue(StringUtil.orIsBlank(null, "a"));
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
	public void testStrTohexStrAndHexStrToStr() {
		String str = "abc123xyz";
		assertEquals(StringUtil.hexStrToStr(StringUtil.strToHexStr(str)), str);
	}

	@Test
	public void testHexStrToBytesAndBytesToHexStr() {
		String hexStr = "123456ABCDEF";
		assertEquals(StringUtil.bytesToHexStr(StringUtil.hexStrToBytes(hexStr)), hexStr);
	}

	@Test
	public void testStrToBytesAndBytesToStr() {
		String str = TestConstants.TEST_STRING;
		assertEquals(
				StringUtil.hexStrToStr(StringUtil.bytesToHexStr(StringUtil.hexStrToBytes(StringUtil.strToHexStr(str)))),
				str);
	}

	@Test
	public void testMerge() {
		assertEquals(StringUtil.merge("a", "b"), "ab");
		assertEquals(StringUtil.merge("a", "b", "c"), "abc");
		assertEquals(StringUtil.merge("a", "b", "c", "d"), "abcd");
		assertEquals(StringUtil.merge("a", "b", "c", "d", "e"), "abcde");
		assertEquals(StringUtil.merge("a", "b", "c", "d", "e", "f"), "abcdef");
	}

	@Test
	public void testToInt() {
		assertEquals(StringUtil.toInt("123 "), 123);
	}

	@Test
	public void testToLong() {
		assertEquals(StringUtil.toLong("123 "), 123L);
	}

	@Test
	public void testToDouble() {
		assertEquals(StringUtil.toDouble("123.321 "), 123.321);
	}

	@Test
	public void testToFloat() {
		assertEquals(StringUtil.toFloat("123.321 "), 123.321F);
	}

	@Test
	public void testToMD5() {
		assertEquals(StringUtil.toMD5("test123"), "cc03e747a6afbbcbf8be7668acfebee5");
	}

}
