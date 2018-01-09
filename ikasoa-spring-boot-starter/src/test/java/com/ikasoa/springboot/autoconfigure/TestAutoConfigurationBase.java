package com.ikasoa.springboot.autoconfigure;

import org.junit.Test;

import com.ikasoa.rpc.IkasoaException;

import junit.framework.TestCase;

public class TestAutoConfigurationBase extends TestCase {

	TestAutoConfiguration testAutoConfiguration = new TestAutoConfiguration();

	private final static String TEST_HOST = "127.0.0.1";

	private final static String TEST_PORT = "19999";

	@Test
	public void testGetHost() {
		try {
			assertEquals(TEST_HOST, testAutoConfiguration.getHost());
		} catch (IkasoaException e) {
			fail();
		}
	}

	@Test
	public void testGetPort() {
		try {
			assertEquals(Integer.parseInt(TEST_PORT), testAutoConfiguration.getPort());
		} catch (IkasoaException e) {
			fail();
		}
	}

	class TestAutoConfiguration extends AutoConfigurationBase {

		TestAutoConfiguration() {
			super.host = TEST_HOST;
			super.port = TEST_PORT;
		}

		public String getHost() throws IkasoaException {
			return super.getHost();
		}

		protected int getPort() throws IkasoaException {
			return super.getPort();
		}

	}

}
