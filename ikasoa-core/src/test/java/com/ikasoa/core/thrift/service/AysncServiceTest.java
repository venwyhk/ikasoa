package com.ikasoa.core.thrift.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TNonblockingSocket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.TestConstants;
import com.ikasoa.core.thrift.Factory;
import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.server.MultiplexedProcessor;
import com.ikasoa.core.thrift.server.ServerArgsAspect;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.utils.ServerUtil;

import junit.framework.TestCase;

/**
 * 异步服务单元测试
 */
public class AysncServiceTest extends TestCase {

	private String testString1 = TestConstants.TEST_STRING;

	private String testString2 = "1234567890-= ";

	private ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();

	@Before
	public void setUp() {
		thriftServerConfiguration.setProtocolFactory(new TCompactProtocol.Factory());
		thriftServerConfiguration.setServerArgsAspect(new ServerArgsAspect() {
			@Override
			public TThreadPoolServer.Args tThreadPoolServerArgsAspect(TThreadPoolServer.Args args) {
				args.stopTimeoutVal = 1;
				return args;
			}
		});
	}

	@Test
	public void testAysncServiceImpl() {
		int serverPort = ServerUtil.getNewPort();
		TProcessor p = new ServiceProcessor(new TestThriftServiceImpl1());
		thriftServerConfiguration.setProcessorFactory(new TProcessorFactory(p));
		Factory factory = new GeneralFactory(thriftServerConfiguration);
		ThriftServer thriftServer = factory.getThriftServer(serverPort, new TestThriftServiceImpl1());
		thriftServer.run();
		try {
			AsyncService service = factory
					.getAsyncService(new TNonblockingSocket(TestConstants.LOCAL_HOST, serverPort));
			Thread.sleep(500);
			service.get(testString1, new TestCallback1());
			Thread.sleep(1000);
		} catch (Exception e) {
			fail();
		} finally {
			thriftServer.stop();
		}
	}

	@Test
	public void testAysncMultiplexedServiceImpl() {
		int serverPort = ServerUtil.getNewPort();
		Map<String, TProcessor> processorMap = new HashMap<>();
		processorMap.put("testAysncService1", new ServiceProcessor(new TestThriftServiceImpl1()));
		processorMap.put("testAysncService2", new ServiceProcessor(new TestThriftServiceImpl2()));
		MultiplexedProcessor p = new MultiplexedProcessor(processorMap);
		thriftServerConfiguration.setProcessorFactory(new TProcessorFactory(p));
		Factory factory = new GeneralFactory(thriftServerConfiguration);
		ThriftServer thriftServer = factory.getThriftServer("testAysncMultiplexedService", serverPort, p);
		thriftServer.run();
		try {
			Thread.sleep(500);
			AsyncService service1 = factory
					.getAsyncService(new TNonblockingSocket(TestConstants.LOCAL_HOST, serverPort), "testAysncService1");
			service1.get(testString1, new TestCallback1());
			AsyncService service2 = factory
					.getAsyncService(new TNonblockingSocket(TestConstants.LOCAL_HOST, serverPort), "testAysncService2");
			service2.get(testString2, new TestCallback2());
			Thread.sleep(1000);
		} catch (Exception e) {
			fail();
		} finally {
			thriftServer.stop();
		}
	}

	@After
	public void tearDown() {
		thriftServerConfiguration = null;
	}

	private class TestThriftServiceImpl1 implements Service {

		@Override
		public String get(String arg) throws IkasoaException {
			return arg;
		}

	}

	private class TestThriftServiceImpl2 implements Service {

		@Override
		public String get(String arg) throws IkasoaException {
			return testString1 + arg;
		}

	}

	private class TestCallback1 implements AsyncMethodCallback<String> {

		@Override
		public void onComplete(String response) {
			assertEquals(response, testString1);
		}

		@Override
		public void onError(Exception exception) {
			fail();
		}

	}

	private class TestCallback2 implements AsyncMethodCallback<String> {

		@Override
		public void onComplete(String response) {
			assertEquals(response, testString1 + testString2);
		}

		@Override
		public void onError(Exception exception) {
			fail();
		}

	}

}
