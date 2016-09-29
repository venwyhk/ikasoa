package com.ikamobile.ikasoa.core.thrift.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import com.ikamobile.ikasoa.core.thrift.Factory;
import com.ikamobile.ikasoa.core.thrift.GeneralFactory;
import com.ikamobile.ikasoa.core.thrift.client.CompactThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.client.TupleThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.protocol.SecurityCompactProtocol;
import com.ikamobile.ikasoa.core.thrift.server.impl.DefaultThriftServerImpl;
import com.ikamobile.ikasoa.core.thrift.server.impl.ServletThriftServerImpl;
import com.ikamobile.ikasoa.core.thrift.server.impl.SimpleThriftServerImpl;
import com.ikamobile.ikasoa.core.thrift.server.ThriftSimpleService;
import com.ikamobile.ikasoa.core.thrift.server.ThriftSimpleService.AsyncClient.get_call;
import com.ikamobile.ikasoa.core.thrift.server.ThriftSimpleService.Iface;

import junit.framework.TestCase;

/**
 * Thrift服务端单元测试
 */
public class ServerTest extends TestCase {

	private static String LOCAL_HOST = "localhost";

	private static String serverName = "TestThriftServer";

	private static String testString = "12345678abcdefgABCDEFG一二三四五~!@#$%^&*()_+";

	private static ThriftServerConfiguration configuration = new ThriftServerConfiguration();

	private static Factory factory = new GeneralFactory(configuration);

	@Test
	public void testDefaultThriftServerImpl() {
		int serverPort = 39001;
		ThriftServer defaultThriftServer = factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		assertEquals(defaultThriftServer.getServerName(), serverName);
		assertEquals(defaultThriftServer.getServerPort(), serverPort);
		assertEquals(defaultThriftServer.getThriftServerConfiguration(), configuration);
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
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
			if (thriftClient != null) {
				thriftClient.close();
			}
			if (transport != null) {
				transport.close();
			}
			defaultThriftServer.stop();
		}
	}

	@Test
	public void testNonblockingThriftServerImpl() {
		int serverPort = 39002;
		ThriftServer nioThriftServer = factory.getNonblockingThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		assertEquals(nioThriftServer.getServerName(), serverName);
		assertEquals(nioThriftServer.getServerPort(), serverPort);
		assertEquals(nioThriftServer.getThriftServerConfiguration(), configuration);
		nioThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
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
			if (thriftClient != null) {
				thriftClient.close();
			}
			if (transport != null) {
				transport.close();
			}
			nioThriftServer.stop();
		}
	}

	@Test
	public void testSimpleThriftServerImpl() {
		int serverPort = 39003;
		ThriftServer simpleThriftServer = new SimpleThriftServerImpl(serverName, serverPort, configuration,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		assertEquals(simpleThriftServer.getServerName(), serverName);
		assertEquals(simpleThriftServer.getServerPort(), serverPort);
		assertEquals(simpleThriftServer.getThriftServerConfiguration(), configuration);
		simpleThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
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
			if (thriftClient != null) {
				thriftClient.close();
			}
			if (transport != null) {
				transport.close();
			}
			simpleThriftServer.stop();
		}
	}

	@Test
	public void testAysncDefaultThriftServerImpl() {
		int serverPort = 39004;
		TProcessor p = new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl());
		ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();
		thriftServerConfiguration.setProtocolFactory(new TCompactProtocol.Factory());
		thriftServerConfiguration.setProcessorFactory(new TProcessorFactory(p));
		thriftServerConfiguration.setServerArgsAspect(new ServerArgsAspect() {
			@Override
			public TThreadPoolServer.Args TThreadPoolServerArgsAspect(TThreadPoolServer.Args args) {
				args.stopTimeoutVal = 1;
				return args;
			}
		});
		Factory factory = new GeneralFactory(thriftServerConfiguration);
		ThriftServer thriftServer = factory.getThriftServer(serverName, serverPort, p);
		thriftServer.run();
		try {
			ThriftSimpleService.AsyncClient thriftClient = new ThriftSimpleService.AsyncClient(
					new TCompactProtocol.Factory(), new TAsyncClientManager(),
					new TNonblockingSocket(LOCAL_HOST, serverPort));
			Thread.sleep(500);
			TestCallback callback = new TestCallback();
			thriftClient.get(testString, callback);
			Thread.sleep(1000);
		} catch (Exception e) {
			fail();
		} finally {
			thriftServer.stop();
		}
	}

	@Test
	public void testMultiplexedThriftServerImpl() {
		int serverPort = 39101;
		Map<String, TProcessor> processorMap = new HashMap<>();
		processorMap.put("testServer", new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		MultiplexedProcessor processor = new MultiplexedProcessor(processorMap);
		ThriftServer defaultThriftServer = new DefaultThriftServerImpl(serverName, serverPort, configuration,
				processor);
		assertEquals(defaultThriftServer.getServerName(), serverName);
		assertEquals(defaultThriftServer.getServerPort(), serverPort);
		assertEquals(defaultThriftServer.getThriftServerConfiguration(), configuration);
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
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
			if (thriftClient != null) {
				thriftClient.close();
			}
			if (transport != null) {
				transport.close();
			}
			defaultThriftServer.stop();
		}
	}

	@Test
	public void testCompactDefaultThriftServerImpl() {
		int serverPort = 39201;
		Factory factory = new GeneralFactory(new CompactThriftServerConfiguration(),
				new CompactThriftClientConfiguration());
		ThriftServer defaultThriftServer = factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
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
			if (thriftClient != null) {
				thriftClient.close();
			}
			if (transport != null) {
				transport.close();
			}
			defaultThriftServer.stop();
		}
	}

	@Test
	public void testTupleDefaultThriftServerImpl() {
		int serverPort = 39202;
		Factory factory = new GeneralFactory(new TupleThriftServerConfiguration(),
				new TupleThriftClientConfiguration());
		ThriftServer defaultThriftServer = factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
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
			if (thriftClient != null) {
				thriftClient.close();
			}
			if (transport != null) {
				transport.close();
			}
			defaultThriftServer.stop();
		}
	}

	@Test
	public void testSecurityCompactDefaultThriftServerImpl() {
		int serverPort = 39203;
		String key = "12345678";
		ThriftServerConfiguration serverConfiguration = new ThriftServerConfiguration();
		serverConfiguration.setProtocolFactory(new SecurityCompactProtocol.Factory(key));
		ThriftClientConfiguration clientConfiguration = new ThriftClientConfiguration();
		clientConfiguration.setProtocolFactory(new SecurityCompactProtocol.Factory(key));
		Factory factory = new GeneralFactory(serverConfiguration, clientConfiguration);
		ThriftServer defaultThriftServer = factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		defaultThriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
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
			if (thriftClient != null) {
				thriftClient.close();
			}
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

	@Test
	public void testServerAspect() {
		int serverPort = 39301;
		ThriftServerConfiguration configuration = new ThriftServerConfiguration();
		configuration.setServerAspect(new TestServerAspectImpl());
		Factory factory = new GeneralFactory(configuration);
		ThriftServer defaultThriftServer = factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new TestThriftServiceImpl()));
		defaultThriftServer.run();
		defaultThriftServer.stop();
	}

	private class TestThriftServiceImpl implements ThriftSimpleService.Iface {
		@Override
		public String get(String arg) throws TException {
			return arg;
		}
	}

	private class TestCallback implements AsyncMethodCallback<get_call> {

		@Override
		public void onComplete(get_call response) {
			try {
				assertEquals(response.getResult(), testString);
			} catch (TException e) {
				fail();
			}
		}

		@Override
		public void onError(Exception exception) {
			fail();
		}
	}

	private class TestServerAspectImpl extends TestCase implements ServerAspect {

		@Test
		@Override
		public void beforeStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
				TProcessor processor, ThriftServer server) {
			assertEquals(ServerTest.serverName, serverName);
			assertEquals(39301, serverPort);
			assertFalse(server.isServing());
		}

		@Test
		@Override
		public void afterStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
				TProcessor processor, ThriftServer server) {
			assertEquals(ServerTest.serverName, serverName);
			assertEquals(39301, serverPort);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			assertTrue(server.isServing());
		}

		@Test
		@Override
		public void beforeStop(String serverName, int serverPort, ThriftServerConfiguration configuration,
				TProcessor processor, ThriftServer server) {
			assertEquals(ServerTest.serverName, serverName);
			assertEquals(39301, serverPort);
			assertTrue(server.isServing());
		}

		@Test
		@Override
		public void afterStop(String serverName, int serverPort, ThriftServerConfiguration configuration,
				TProcessor processor, ThriftServer server) {
			assertEquals(ServerTest.serverName, serverName);
			assertEquals(39301, serverPort);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			assertFalse(server.isServing());
		}

	}

}
