package com.ikasoa.springboot.autoconfigure;

import org.junit.Test;

import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.rpc.RpcException;

import junit.framework.TestCase;

/**
 * TestAutoConfigurationBase测试
 */
public class TestAutoConfigurationBase extends TestCase {

	TestAutoConfiguration testAutoConfiguration = new TestAutoConfiguration();

	private final static String TEST_NAME = "TestServer";

	private final static String TEST_HOST = "127.0.0.1";

	private final static String TEST_PORT = "19999";

	private final static String TEST_NAMES = "TestService";

	private final static String TEST_CLASSES = "TestService.class";

	private final static String TEST_CONFIGURATOR = "com.ikasoa.rpc.Configurator";

	@Test
	public void testGetName() {
		try {
			assertEquals(TEST_NAME, testAutoConfiguration.getName());
		} catch (RpcException e) {
			fail();
		}
	}

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
			assertTrue(ServerUtil.isPort(testAutoConfiguration.getPort()));
		} catch (RpcException e) {
			fail();
		}
	}

	@Test
	public void testGetNames() {
		try {
			assertEquals(TEST_NAMES, testAutoConfiguration.getNames());
		} catch (RpcException e) {
			fail();
		}
	}

	@Test
	public void testGetClasses() {
		try {
			assertEquals(TEST_CLASSES, testAutoConfiguration.getClasses());
		} catch (RpcException e) {
			fail();
		}
	}

	@Test
	public void testGetConfiguratorStr() {
		try {
			assertEquals(TEST_CONFIGURATOR, testAutoConfiguration.getConfiguratorStr());
		} catch (RpcException e) {
			fail();
		}
	}

	@Test
	public void testGetConfigurator() {
		assertEquals(TEST_CONFIGURATOR, testAutoConfiguration.getConfigurator().getClass().getName());
	}

	class TestAutoConfiguration extends AbstractAutoConfiguration {

		TestAutoConfiguration() {
			name = TEST_NAME;
			host = TEST_HOST;
			port = TEST_PORT;
			names = TEST_NAMES;
			classes = TEST_CLASSES;
			configurator = TEST_CONFIGURATOR;
		}

		public String getName() throws RpcException {
			return name;
		}

		public String getHost() throws RpcException {
			return super.getHost();
		}

		protected int getPort() throws RpcException {
			return super.getPort();
		}

		protected String getNames() throws RpcException {
			return names;
		}

		protected String getClasses() throws RpcException {
			return classes;
		}

		protected String getConfiguratorStr() throws RpcException {
			return configurator;
		}

	}

}
