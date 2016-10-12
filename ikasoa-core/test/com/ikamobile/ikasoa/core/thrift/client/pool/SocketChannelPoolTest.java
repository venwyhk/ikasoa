package com.ikamobile.ikasoa.core.thrift.client.pool;

import java.io.IOException;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.junit.Test;

import com.ikamobile.ikasoa.core.thrift.Factory;
import com.ikamobile.ikasoa.core.thrift.GeneralFactory;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.client.pool.impl.DefaultSocketChannelPoolImpl;
import com.ikamobile.ikasoa.core.thrift.client.pool.impl.SimpleSocketChannelPoolImpl;
import com.ikamobile.ikasoa.core.thrift.client.socket.ThriftSocketChannel;

import junit.framework.TestCase;

/**
 * SocketChannel连接池单元测试
 */
public class SocketChannelPoolTest extends TestCase {

	private static String LOCAL_HOST = "localhost";

	@Test
	public void testDefaultSocketChannelPool() {
		SocketChannelPool pool = new DefaultSocketChannelPoolImpl();
		try {
			assertNotNull(pool.buildThriftSocketChannel(LOCAL_HOST, 38101));
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public void testSimpleSocketChannelPool() {
		SocketChannelPool pool = new SimpleSocketChannelPoolImpl();
		try {
			assertNotNull(pool.buildThriftSocketChannel(LOCAL_HOST, 38102));
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public void testCustomSocketChannelPool() {
		int serverPort = 38103;
		ThriftClientConfiguration configuration = new ThriftClientConfiguration();
		configuration.setTransportFactory(new TTransportFactory());
		configuration.setSocketChannelPool(new TestSocketChannelPoolImpl());
		Factory factory = new GeneralFactory(configuration);
		ThriftClient thriftClient = factory.getNonblockingThriftClient(LOCAL_HOST, serverPort);
		TTransport transport = null;
		try {
			transport = thriftClient.getTransport();
			assertNull(transport);
		} catch (Exception e) {
			fail();
		} finally {
			if (thriftClient != null) {
				thriftClient.close();
			}
		}
	}

	private class TestSocketChannelPoolImpl implements SocketChannelPool {

		public TestSocketChannelPoolImpl() {
		}

		@Override
		public ThriftSocketChannel buildThriftSocketChannel(String host, int port) throws IOException {
			assertEquals(host, LOCAL_HOST);
			assertEquals(port, 38103);
			return null;
		}

		@Override
		public void releaseThriftSocketChannel(ThriftSocketChannel thriftSocketChannel, String host, int port) {
			assertNull(thriftSocketChannel);
			assertEquals(host, LOCAL_HOST);
			assertEquals(port, 38103);
		}

		@Override
		public void releaseAllThriftSocketChannel() {
			// Do nothing
		}

	}

}
