package com.ikamobile.ikasoa.core.thrift.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TNonblockingSocket;
import org.junit.Test;

import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.thrift.Factory;
import com.ikamobile.ikasoa.core.thrift.GeneralFactory;
import com.ikamobile.ikasoa.core.thrift.server.MultiplexedProcessor;
import com.ikamobile.ikasoa.core.thrift.server.ServerArgsAspect;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServer;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikamobile.ikasoa.core.thrift.service.impl.CallBack;

import junit.framework.TestCase;

/**
 * 异步服务单元测试
 */
public class AysncServiceTest extends TestCase {

	private static String LOCAL_HOST = "localhost";

	private String testString1 = "12345678abcdefgABCDEFG一二三四五~!@#$%^&*()_+";

	private String testString2 = "1234567890-= ";

	@Test
	public void testAysncServiceImpl() {
		int serverPort = 49001;
		TProcessor p = new ServiceProcessor(new TestThriftServiceImpl1());
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
		ThriftServer thriftServer = factory.getThriftServer(serverPort, new TestThriftServiceImpl1());
		thriftServer.run();
		try {
			AsyncService service = factory.getAsyncService(new TNonblockingSocket(LOCAL_HOST, serverPort));
			Thread.sleep(500);
			TestCallback1 callback = new TestCallback1();
			service.get(testString1, callback);
			Thread.sleep(1000);
		} catch (Exception e) {
			fail();
		} finally {
			thriftServer.stop();
		}
	}

	@Test
	public void testAysncMultiplexedServiceImpl() {
		int serverPort = 49002;
		Map<String, TProcessor> processorMap = new HashMap<>();
		processorMap.put("testAysncService1", new ServiceProcessor(new TestThriftServiceImpl1()));
		processorMap.put("testAysncService2", new ServiceProcessor(new TestThriftServiceImpl2()));
		MultiplexedProcessor p = new MultiplexedProcessor(processorMap);
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
		ThriftServer thriftServer = factory.getThriftServer("testAysncMultiplexedService", serverPort, p);
		thriftServer.run();
		try {
			Thread.sleep(500);
			AsyncService service1 = factory.getAsyncService(new TNonblockingSocket(LOCAL_HOST, serverPort),
					"testAysncService1");
			TestCallback1 callback1 = new TestCallback1();
			service1.get(testString1, callback1);
			AsyncService service2 = factory.getAsyncService(new TNonblockingSocket(LOCAL_HOST, serverPort),
					"testAysncService2");
			TestCallback2 callback2 = new TestCallback2();
			service2.get(testString2, callback2);
			Thread.sleep(1000);
		} catch (Exception e) {
			fail();
		} finally {
			thriftServer.stop();
		}
	}

	private class TestThriftServiceImpl1 implements Service {

		@Override
		public String get(String arg) throws STException {
			return arg;
		}
	}

	private class TestThriftServiceImpl2 implements Service {

		@Override
		public String get(String arg) throws STException {
			return testString1 + arg;
		}
	}

	private class TestCallback1 implements AsyncMethodCallback<CallBack> {

		@Override
		public void onComplete(CallBack response) {
			try {
				assertEquals(response.getResult(), testString1);
			} catch (TException e) {
				fail();
			}
		}

		@Override
		public void onError(Exception exception) {
			fail();
		}
	}

	private class TestCallback2 implements AsyncMethodCallback<CallBack> {

		@Override
		public void onComplete(CallBack response) {
			try {
				assertEquals(response.getResult(), testString1 + testString2);
			} catch (TException e) {
				fail();
			}
		}

		@Override
		public void onError(Exception exception) {
			fail();
		}
	}
}
