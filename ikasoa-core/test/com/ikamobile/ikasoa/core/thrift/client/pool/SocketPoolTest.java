package com.ikamobile.ikasoa.core.thrift.client.pool;

import org.junit.Test;

import com.ikamobile.ikasoa.core.thrift.client.pool.impl.DefaultSocketPoolImpl;

import junit.framework.TestCase;

/**
 * Socket连接池单元测试
 */
public class SocketPoolTest extends TestCase {

	@Test
	public void testBuildThriftSocket() {
		SocketPool pool = new DefaultSocketPoolImpl();
		assertNotNull(pool.buildThriftSocket("localhost", 38001));
	}

}
