package com.ikasoa.zk.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import junit.framework.TestCase;

public class TestLocalUtil extends TestCase {

	@Test
	public void testGetLocalHostName() {
		try {
			assertEquals(InetAddress.getLocalHost().getHostName(), LocalUtil.getLocalHostName());
		} catch (UnknownHostException e) {
			fail();
		}
	}
}
