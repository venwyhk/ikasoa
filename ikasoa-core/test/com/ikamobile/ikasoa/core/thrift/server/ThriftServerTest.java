package com.ikamobile.ikasoa.core.thrift.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;
import com.ikamobile.ikasoa.core.thrift.Factory;
import com.ikamobile.ikasoa.core.thrift.GeneralFactory;
import com.ikamobile.ikasoa.core.thrift.client.CompactThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.client.TupleThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.server.impl.DefaultThriftServerImpl;
import com.ikamobile.ikasoa.core.thrift.server.impl.NonblockingThriftServerImpl;
import com.ikamobile.ikasoa.core.thrift.server.impl.ServletThriftServerImpl;
import com.ikamobile.ikasoa.core.thrift.server.impl.SimpleThriftServerImpl;
import com.ikamobile.ikasoa.core.thrift.server.ThriftSimpleService;
import com.ikamobile.ikasoa.core.thrift.server.ThriftSimpleService.Iface;

import junit.framework.TestCase;

/**
 * Thrift服务端单元测试
 */
public class ThriftServerTest extends TestCase {

	private static String serverName = "TestThriftServer";

	private static String testString = "12345678abcdefg";

	private static ThriftServerConfiguration configuration = new ThriftServerConfiguration();

	private static Factory factory = new GeneralFactory(configuration);

	@Test
	public void testDefaultThriftServerImpl() {
		int serverPort = 9007;
		ThriftServer defaultThriftServer = factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		assertEquals(defaultThriftServer.getServerName(), serverName);
		assertEquals(defaultThriftServer.getServerPort(), serverPort);
		assertEquals(defaultThriftServer.getThriftServerConfiguration(), configuration);
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient("localhost", serverPort);
		TTransport transport = null;
		try {
			transport = thriftClient.getTransport();
			Thread.sleep(500);
			transport.open();
			ThriftSimpleService.Client client = new ThriftSimpleService.Client(thriftClient.getProtocol(transport));
			assertEquals(testString, client.get(testString));
		} catch (Exception e) {
			fail();
		} finally {
			if (transport != null) {
				transport.close();
			}
			defaultThriftServer.stop();
		}
	}

	@Test
	public void testNonblockingThriftServerImpl() {
		int serverPort = 9008;
		ThriftServer nioThriftServer = new NonblockingThriftServerImpl(serverName, serverPort, configuration,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		assertEquals(nioThriftServer.getServerName(), serverName);
		assertEquals(nioThriftServer.getServerPort(), serverPort);
		assertEquals(nioThriftServer.getThriftServerConfiguration(), configuration);
		nioThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient("localhost", serverPort);
		TTransport transport = null;
		try {
			transport = thriftClient.getTransport();
			Thread.sleep(500);
			transport.open();
			ThriftSimpleService.Client client = new ThriftSimpleService.Client(thriftClient.getProtocol(transport));
			assertEquals(testString, client.get(testString));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			if (transport != null) {
				transport.close();
			}
			nioThriftServer.stop();
		}
	}

	@Test
	public void testSimpleThriftServerImpl() {
		int serverPort = 9009;
		ThriftServer simpleThriftServer = new SimpleThriftServerImpl(serverName, serverPort, configuration,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		assertEquals(simpleThriftServer.getServerName(), serverName);
		assertEquals(simpleThriftServer.getServerPort(), serverPort);
		assertEquals(simpleThriftServer.getThriftServerConfiguration(), configuration);
		simpleThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient("localhost", serverPort);
		TTransport transport = null;
		try {
			transport = thriftClient.getTransport();
			Thread.sleep(500);
			transport.open();
			ThriftSimpleService.Client client = new ThriftSimpleService.Client(thriftClient.getProtocol(transport));
			assertEquals(testString, client.get(testString));
		} catch (Exception e) {
			fail();
		} finally {
			if (transport != null) {
				transport.close();
			}
			simpleThriftServer.stop();
		}
	}

	@Test
	public void testMultiplexedThriftServerImpl() {
		int serverPort = 9010;
		Map<String, TProcessor> processorMap = new HashMap<String, TProcessor>();
		processorMap.put("testServer", new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		MultiplexedProcessor processor = new MultiplexedProcessor(processorMap);
		ThriftServer defaultThriftServer = new DefaultThriftServerImpl(serverName, serverPort, configuration,
				processor);
		assertEquals(defaultThriftServer.getServerName(), serverName);
		assertEquals(defaultThriftServer.getServerPort(), serverPort);
		assertEquals(defaultThriftServer.getThriftServerConfiguration(), configuration);
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient("localhost", serverPort);
		TTransport transport = null;
		try {
			transport = thriftClient.getTransport();
			Thread.sleep(500);
			transport.open();
			ThriftSimpleService.Client client = new ThriftSimpleService.Client(
					thriftClient.getProtocol(transport, "testServer"));
			assertEquals(testString, client.get(testString));
		} catch (Exception e) {
			fail();
		} finally {
			if (transport != null) {
				transport.close();
			}
			defaultThriftServer.stop();
		}
	}

	@Test
	public void testCompactDefaultThriftServerImpl() {
		int serverPort = 9011;
		Factory factory = new GeneralFactory(new CompactThriftServerConfiguration(),
				new CompactThriftClientConfiguration());
		ThriftServer defaultThriftServer = factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient("localhost", serverPort);
		TTransport transport = null;
		try {
			transport = thriftClient.getTransport();
			Thread.sleep(500);
			transport.open();
			ThriftSimpleService.Client client = new ThriftSimpleService.Client(thriftClient.getProtocol(transport));
			assertEquals(testString, client.get(testString));
		} catch (Exception e) {
			fail();
		} finally {
			if (transport != null) {
				transport.close();
			}
			defaultThriftServer.stop();
		}
	}

	@Test
	public void testTupleDefaultThriftServerImpl() {
		int serverPort = 9012;
		Factory factory = new GeneralFactory(new TupleThriftServerConfiguration(),
				new TupleThriftClientConfiguration());
		ThriftServer defaultThriftServer = factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient("localhost", serverPort);
		TTransport transport = null;
		try {
			transport = thriftClient.getTransport();
			Thread.sleep(500);
			transport.open();
			ThriftSimpleService.Client client = new ThriftSimpleService.Client(thriftClient.getProtocol(transport));
			assertEquals(testString, client.get(testString));
		} catch (Exception e) {
			fail();
		} finally {
			if (transport != null) {
				transport.close();
			}
			defaultThriftServer.stop();
		}
	}

	@Test
	public void testServletThriftServerImpl() {
		ThriftServer servletThriftServer = new ServletThriftServerImpl(serverName, configuration, null);
		assertEquals(servletThriftServer.getServerName(), serverName);
		assertEquals(servletThriftServer.getThriftServerConfiguration(), configuration);
	}

	private class TestThriftServiceImpl implements ThriftSimpleService.Iface {
		@Override
		public String get(String arg) throws TException {
			return arg;
		}
	}

}
