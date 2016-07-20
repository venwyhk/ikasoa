package com.ikamobile.ikasoa.core.thrift;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Socket连接池单元测试
 */
public class SocketPoolTest extends TestCase {

	@Test
	public void testBuildThriftSocket() {
		assertNotNull(SocketPool.buildThriftSocket("localhost", 38001));
	}

}
