package com.ikamobile.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Base64工具类单元测试
 */
public class Base64UtilTest extends TestCase {

	private static String testString = "12345678abcdefgABCDEFG一二三四五~!@#$%^&*()_+";

	@Test
	public void test() {
		assertEquals(testString, new String(Base64Util.decode(Base64Util.encode(testString.getBytes()).toCharArray())));
	}

}
