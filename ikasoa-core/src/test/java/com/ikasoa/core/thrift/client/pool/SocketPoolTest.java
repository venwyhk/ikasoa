package com.ikasoa.core.thrift.client.pool;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.junit.Test;

import com.ikasoa.core.TestBase;
import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.client.pool.impl.SimpleSocketPoolImpl;
import com.ikasoa.core.thrift.client.pool.impl.CommonsPoolImpl;
import com.ikasoa.core.thrift.client.pool.impl.NoSocketPoolImpl;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;

/**
 * Socket连接池单元测试
 */
public class SocketPoolTest extends TestBase {

	@Test
	public void testSimpleSocketPool() {
		SocketPool pool = new SimpleSocketPoolImpl();
		assertNotNull(pool.buildThriftSocket(LOCAL_HOST, getNewPort()));
	}

	@Test
	public void testNoSocketPool() {
		SocketPool pool = new NoSocketPoolImpl();
		assertNotNull(pool.buildThriftSocket(LOCAL_HOST, getNewPort()));
	}

	@Test
	public void testCommonsSocketPool() {
		SocketPool pool = new CommonsPoolImpl();
		assertNotNull(pool.buildThriftSocket(LOCAL_HOST, getNewPort()));
	}

	@Test
	public void testCustomSimpleSocketPool() {
		int serverPort = getNewPort();
		ThriftClientConfiguration configuration = new ThriftClientConfiguration();
		configuration.setTransportFactory(new TTransportFactory());
		configuration.setSocketPool(new TestSocketPoolImpl(serverPort));
		try (ThriftClient thriftClient = new GeneralFactory(configuration).getThriftClient(LOCAL_HOST, serverPort);
				TTransport transport = thriftClient.getTransport()) {
			assertNull(transport);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testCustomNoSocketPool() {
		int serverPort = getNewPort();
		ThriftClientConfiguration configuration = new ThriftClientConfiguration();
		configuration.setSocketPool(new NoSocketPoolImpl());
		configuration.setTransportFactory(new TTransportFactory());
		configuration.setSocketPool(new TestSocketPoolImpl(serverPort));
		try (ThriftClient thriftClient = new GeneralFactory(configuration).getThriftClient(LOCAL_HOST, serverPort);
				TTransport transport = thriftClient.getTransport()) {
			assertNull(transport);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testCustomCommonsSocketPool() {
		int serverPort = getNewPort();
		ThriftClientConfiguration configuration = new ThriftClientConfiguration();
		configuration.setSocketPool(new CommonsPoolImpl());
		configuration.setTransportFactory(new TTransportFactory());
		configuration.setSocketPool(new TestSocketPoolImpl(serverPort));
		try (ThriftClient thriftClient = new GeneralFactory(configuration).getThriftClient(LOCAL_HOST, serverPort);
				TTransport transport = thriftClient.getTransport()) {
			assertNull(transport);
		} catch (Exception e) {
			fail();
		}
	}

	private class TestSocketPoolImpl implements SocketPool {

		private int port;

		public TestSocketPoolImpl(int port) {
			this.port = port;
		}

		@Override
		public ThriftSocket buildThriftSocket(String host, int port) {
			assertEquals(host, LOCAL_HOST);
			assertEquals(port, this.port);
			return null;
		}

		@Override
		public void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port) {
			assertNull(thriftSocket);
			assertEquals(host, LOCAL_HOST);
			assertEquals(port, this.port);
		}

		@Override
		public void releaseAllThriftSocket() {
			// Do nothing
		}

	}

}
