package com.ikasoa.rpc.utils;

import org.junit.Test;

import junit.framework.TestCase;

public class TestStreamUtil extends TestCase {

	@Test
	public void testStreamChangeUtil() {
		String str = "测试字符串string";
		assertEquals(str, new String(StreamUtil.inputStreamToBytes(StreamUtil.bytesToInputStream(str.getBytes()))));
	}

}
