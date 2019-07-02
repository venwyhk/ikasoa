package com.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;

public class TestStreamUtil extends TestCase {

	private final String str = "测试字符串string";

	@Test
	public void testStreamChangeUtil() {
		assertEquals(str, new String(StreamUtil.inputStreamToBytes(StreamUtil.bytesToInputStream(str.getBytes()))));
	}

	@Test
	public void testObjectStreamChangeUtil() {
		assertEquals(str, ((SerializableTestObject) StreamUtil
				.bytesToObject(StreamUtil.objectToBytes(new SerializableTestObject(str)))).getValue());
	}

}
