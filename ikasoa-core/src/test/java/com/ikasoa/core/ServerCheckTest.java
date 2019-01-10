package com.ikasoa.core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.client.impl.DefaultThriftClientImpl;
import com.ikasoa.core.thrift.client.impl.LoadBalanceThriftClientImpl;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;

import junit.framework.TestCase;

/**
 * 服务器检测接口测试
 */
public class ServerCheckTest extends TestCase {

	private int serverPort = ServerUtil.getNewPort();

	private ThriftClientConfiguration configuration = new ThriftClientConfiguration();

	@Test
	public void testCheck() {
		configuration.setServerCheck(new ServerCheckTestImpl());
		try (ThriftClient defaultThriftClient1 = new DefaultThriftClientImpl("192.168.1.1", serverPort,
				configuration)) {
			assertNull(defaultThriftClient1.getTransport());
		} catch (Exception e) {
			assertFalse(StringUtil.equals("", e.getMessage()));
		}
		try (ThriftClient defaultThriftClient2 = new DefaultThriftClientImpl("192.168.1.2", serverPort,
				configuration)) {
			assertNotNull(defaultThriftClient2.getTransport());
		} catch (Exception e) {
			fail();
		}
		try (ThriftClient defaultThriftClient3 = new DefaultThriftClientImpl("192.168.1.3", serverPort,
				configuration)) {
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
		try (ThriftClient loadBalanceThriftClient = new LoadBalanceThriftClientImpl(
				new PollingLoadBalanceImpl(serverInfoList), configuration)) {
			loadBalanceThriftClient.getTransport();
			assertEquals(loadBalanceThriftClient.getServerHost(), "192.168.1.2");
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testCheckFailProcess() {
		configuration.setServerCheck(new ServerCheckTestImpl());
		configuration.setServerCheckFailProcessor(new ServerCheckFailProcessTestImpl());
		try (ThriftClient defaultThriftClient = new DefaultThriftClientImpl("192.168.1.1", serverPort, configuration)) {
			defaultThriftClient.getTransport();
		} catch (Exception e) {
			assertTrue(StringUtil.equals("exce", e.getMessage()));
		}
	}

	class ServerCheckTestImpl implements ServerCheck {

		@Override
		public boolean check(String serverHost, int serverPort) {
			return "192.168.1.2".equals(serverHost) ? Boolean.TRUE : Boolean.FALSE;
		}

	}

	class ServerCheckFailProcessTestImpl implements ServerCheckFailProcessor {

		@Override
		public void process(ThriftClient client) {
			throw new RuntimeException("exce");
		}

	}

}
