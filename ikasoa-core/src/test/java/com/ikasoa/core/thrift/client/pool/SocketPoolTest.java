package com.ikasoa.core.thrift.client.pool;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.junit.Test;

import com.ikasoa.core.thrift.Factory;
import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.client.pool.impl.DefaultSocketPoolImpl;
import com.ikasoa.core.thrift.client.pool.impl.SimpleSocketPoolImpl;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;

import junit.framework.TestCase;

/**
 * Socket连接池单元测试
 */
public class SocketPoolTest extends TestCase {

	private static String LOCAL_HOST = "localhost";

	@Test
	public void testDefaultSocketPool() {
		SocketPool pool = new DefaultSocketPoolImpl();
		assertNotNull(pool.buildThriftSocket(LOCAL_HOST, 38001));
	}

	@Test
	public void testSimpleSocketPool() {
		SocketPool pool = new SimpleSocketPoolImpl();
		assertNotNull(pool.buildThriftSocket(LOCAL_HOST, 38002));
	}

	@Test
	public void testCustomSocketPool() {
		int serverPort = 38003;
		ThriftClientConfiguration configuration = new ThriftClientConfiguration();
		configuration.setTransportFactory(new TTransportFactory());
		configuration.setSocketPool(new TestSocketPoolImpl());
		Factory factory = new GeneralFactory(configuration);
		try (ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
				TTransport transport = thriftClient.getTransport()) {
			assertNull(transport);
		} catch (Exception e) {
			fail();
		}
	}

	private class TestSocketPoolImpl implements SocketPool {

		public TestSocketPoolImpl() {
		}

		@Override
		public ThriftSocket buildThriftSocket(String host, int port) {
			assertEquals(host, LOCAL_HOST);
			assertEquals(port, 38003);
			return null;
		}

		@Override
		public void releaseThriftSocket(ThriftSocket thriftSocket) {
			assertNull(thriftSocket);
		}

		@Override
		public void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port) {
			assertNull(thriftSocket);
			assertEquals(host, LOCAL_HOST);
			assertEquals(port, 38003);
		}

		@Override
		public void releaseAllThriftSocket() {
			// Do nothing
		}

	}

}
