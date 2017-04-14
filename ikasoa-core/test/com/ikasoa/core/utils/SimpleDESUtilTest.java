package com.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * 简单DES加解密工具类单元测试
 */
public class SimpleDESUtilTest extends TestCase {

	private static String key = "12345678";

	private static String testString = "12345678abcdefgABCDEFG~!@#$%^&*()_+";

	@Test
	public void test() {
		try {
			assertEquals(testString, new String(SimpleDESUtil.decrypt(SimpleDESUtil.encrypt(testString, key), key)));
		} catch (Exception e) {
			fail();
		}
	}

}
