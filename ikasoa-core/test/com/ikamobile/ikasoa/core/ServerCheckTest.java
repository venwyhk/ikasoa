package com.ikamobile.ikasoa.core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.client.impl.DefaultThriftClientImpl;
import com.ikamobile.ikasoa.core.thrift.client.impl.LoadBalanceThriftClientImpl;
import com.ikamobile.ikasoa.core.utils.StringUtil;

import junit.framework.TestCase;

/**
 * 服务器检测接口测试
 */
public class ServerCheckTest extends TestCase {

	private int serverPort = 19000;

	private ThriftClientConfiguration configuration = new ThriftClientConfiguration();

	@Test
	public void testCheck() {
		configuration.setServerCheck(new ServerCheckTestImpl());
		ThriftClient defaultThriftClient1 = new DefaultThriftClientImpl("192.168.1.1", serverPort, configuration);
		try {
			assertNull(defaultThriftClient1.getTransport());
		} catch (Exception e) {
			assertTrue(!StringUtil.equals("", e.getMessage()));
		}
		ThriftClient defaultThriftClient2 = new DefaultThriftClientImpl("192.168.1.2", serverPort, configuration);
		try {
			assertNotNull(defaultThriftClient2.getTransport());
		} catch (Exception e) {
			fail();
		}
		ThriftClient defaultThriftClient3 = new DefaultThriftClientImpl("192.168.1.3", serverPort, configuration);
		try {
			assertNull(defaultThriftClient3.getTransport());
		} catch (Exception e) {
			assertTrue(!StringUtil.equals("", e.getMessage()));
		}
	}

	@Test
	public void testLoadBalanceCheck() {
		configuration.setServerCheck(new ServerCheckTestImpl());
		List<ServerInfo> serverInfoList = new ArrayList<>();
		serverInfoList.add(new ServerInfo("192.168.1.1", serverPort));
		serverInfoList.add(new ServerInfo("192.168.1.2", serverPort));
		serverInfoList.add(new ServerInfo("192.168.1.3", serverPort));
		ThriftClient loadBalanceThriftClient = new LoadBalanceThriftClientImpl(
				new PollingLoadBalanceImpl(serverInfoList), configuration);
		try {
			loadBalanceThriftClient.getTransport();
		} catch (STException e) {
			fail();
		}
		assertEquals(loadBalanceThriftClient.getServerHost(), "192.168.1.2");
	}

	@Test
	public void testCheckFailProcess() {
		configuration.setServerCheck(new ServerCheckTestImpl());
		configuration.setServerCheckFailProcessor(new ServerCheckFailProcessTestImpl());
		try {
			new DefaultThriftClientImpl("192.168.1.1", serverPort, configuration).getTransport();
		} catch (Exception e) {
			assertTrue(StringUtil.equals("exce", e.getMessage()));
		}
	}

	class ServerCheckTestImpl implements ServerCheck {

		@Override
		public boolean check(String serverHost, int serverPort) {
			return "192.168.1.2".equals(serverHost) ? true : false;
		}

	}

	class ServerCheckFailProcessTestImpl implements ServerCheckFailProcessor {

		@Override
		public void process(ThriftClient client) {
			throw new RuntimeException("exce");
		}

	}

}
