package com.ikasoa.springboot.autoconfigure;

import org.junit.Test;

import com.ikasoa.rpc.RpcException;

import junit.framework.TestCase;

public class TestAutoConfigurationBase extends TestCase {

	TestAutoConfiguration testAutoConfiguration = new TestAutoConfiguration();

	private final static String TEST_HOST = "127.0.0.1";

	private final static String TEST_PORT = "19999";

	@Test
	public void testGetHost() {
		try {
			assertEquals(TEST_HOST, testAutoConfiguration.getHost());
		} catch (RpcException e) {
			fail();
		}
	}

	@Test
	public void testGetPort() {
		try {
			assertEquals(Integer.parseInt(TEST_PORT), testAutoConfiguration.getPort());
		} catch (RpcException e) {
			fail();
		}
	}

	class TestAutoConfiguration extends AbstractAutoConfiguration {

		TestAutoConfiguration() {
			super.host = TEST_HOST;
			super.port = TEST_PORT;
		}

		public String getHost() throws RpcException {
			return super.getHost();
		}

		protected int getPort() throws RpcException {
			return super.getPort();
		}

	}

}
