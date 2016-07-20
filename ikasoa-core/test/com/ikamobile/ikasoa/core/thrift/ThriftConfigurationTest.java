package com.ikamobile.ikasoa.core.thrift;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.ServerContext;
import org.apache.thrift.server.TServerEventHandler;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;

import junit.framework.TestCase;

/**
 * Thrift配置对象单元测试
 */
public class ThriftConfigurationTest extends TestCase {

	@Test
	public void testThriftClientConfiguration() {
		ThriftClientConfiguration thriftClientConfiguration = new ThriftClientConfiguration();
		// 是否有默认值
		assertNotNull(thriftClientConfiguration.getTransportFactory());
		assertNotNull(thriftClientConfiguration.getProtocolFactory());
	}

	@Test
	public void testThriftServerConfiguration() {
		ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();
		// 是否有默认值
		assertNotNull(thriftServerConfiguration.getTransportFactory());
		assertNotNull(thriftServerConfiguration.getProtocolFactory());
		// 测试事件处理器配置
		TServerEventHandler testServerEventHandler = new TestServerEventHandler();
		thriftServerConfiguration.setServerEventHandler(testServerEventHandler);
		assertEquals(thriftServerConfiguration.getServerEventHandler(), testServerEventHandler);
		// 测试线程池配置
		ExecutorService executorService = new ScheduledThreadPoolExecutor(0);
		thriftServerConfiguration.setExecutorService(executorService);
		assertEquals(thriftServerConfiguration.getExecutorService(), executorService);
	}

	private class TestServerEventHandler implements TServerEventHandler {

		@Override
		public ServerContext createContext(TProtocol arg0, TProtocol arg1) {
			return null;
		}

		@Override
		public void deleteContext(ServerContext arg0, TProtocol arg1, TProtocol arg2) {
		}

		@Override
		public void preServe() {
		}

		@Override
		public void processContext(ServerContext arg0, TTransport arg1, TTransport arg2) {
		}
	}

}
