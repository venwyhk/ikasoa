package com.ikasoa.core.utils;

import org.junit.Test;

import com.ikasoa.core.TestConstants;

import junit.framework.TestCase;

/**
 * 服务器工具单元测试
 */
public class ServerUtilTest extends TestCase {

	@Test
	public void testIsIpv4() {
		assertTrue(ServerUtil.isIpv4("127.0.0.1"));
		assertTrue(ServerUtil.isIpv4("192.168.1.111"));
		assertFalse(ServerUtil.isIpv4("99999.99999.99999.00000"));
		assertFalse(ServerUtil.isIpv4(TestConstants.LOCAL_HOST));
		assertFalse(ServerUtil.isIpv4(" "));
	}

	@Test
	public void testisPort() {
		assertTrue(ServerUtil.isPort(8080));
		assertTrue(ServerUtil.isPort(80));
		assertFalse(ServerUtil.isPort(-1));
		assertFalse(ServerUtil.isPort(65536));
	}

	@Test
	public void testIsSocketPort() {
		assertTrue(ServerUtil.isSocketPort(8080));
		assertFalse(ServerUtil.isSocketPort(80));
		assertFalse(ServerUtil.isSocketPort(-1));
		assertFalse(ServerUtil.isSocketPort(65536));
	}

	@Test
	public void testCheckHostAndPort() {
		assertTrue(ServerUtil.checkHostAndPort(TestConstants.LOCAL_HOST, 8080));
		assertFalse(ServerUtil.checkHostAndPort("", 6666));
		assertFalse(ServerUtil.checkHostAndPort(TestConstants.LOCAL_HOST, 65536));
	}

	@Test
	public void testBuildCacheKey() {
		assertEquals(ServerUtil.buildCacheKey(TestConstants.LOCAL_HOST, 8080),
				StringUtil.merge(TestConstants.LOCAL_HOST, ":8080"));
	}

}
