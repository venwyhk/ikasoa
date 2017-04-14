package com.ikasoa.core.thrift.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ikasoa.core.STException;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.impl.DefaultThriftClientImpl;
import com.ikasoa.core.thrift.client.impl.LoadBalanceThriftClientImpl;

import junit.framework.TestCase;

/**
 * 客户端单元测试
 * <p>
 * 仅测试基础属性读写.
 */
public class ClientTest extends TestCase {

	private static String LOCAL_HOST = "localhost";

	private ThriftClientConfiguration configuration = new ThriftClientConfiguration();

	/**
	 * 默认客户端测试
	 */
	@Test
	public void testDefaultThriftClientImpl() {
		int serverPort = 29000;
		ThriftClient defaultThriftClient = new DefaultThriftClientImpl(LOCAL_HOST, serverPort, configuration);
		assertEquals(defaultThriftClient.getServerHost(), LOCAL_HOST);
		assertEquals(defaultThriftClient.getServerPort(), serverPort);
		assertEquals(defaultThriftClient.getThriftClientConfiguration(), configuration);
	}

	/**
	 * 负载均衡客户端测试
	 */
	@Test
	public void testLoadBalanceThriftClientImpl() {
		String serverHost1 = LOCAL_HOST;
		int serverPort1 = 29001;
		List<ServerInfo> serverInfoList = new ArrayList<>();
		serverInfoList.add(new ServerInfo(serverHost1, serverPort1));
		ThriftClient loadBalanceThriftClient1 = new LoadBalanceThriftClientImpl(
				new PollingLoadBalanceImpl(serverInfoList), configuration);
		assertEquals(loadBalanceThriftClient1.getServerHost(), serverHost1);
		assertEquals(loadBalanceThriftClient1.getServerPort(), serverPort1);
		assertEquals(loadBalanceThriftClient1.getThriftClientConfiguration(), configuration);
		// 以下测试利用自定义负载均衡类型通过GeneralFactory获取Client对象
		try {
			String serverHost2 = "127.0.0.1";
			int serverPort2 = 29002;
			serverInfoList.add(new ServerInfo(serverHost2, serverPort2));
			@SuppressWarnings("rawtypes")
			Class cls = Class.forName("com.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl");
			@SuppressWarnings("unchecked")
			ThriftClient loadBalanceThriftClient2 = new GeneralFactory(configuration).getThriftClient(serverInfoList,
					cls);
			loadBalanceThriftClient2.getTransport();
			assertEquals(loadBalanceThriftClient2.getServerHost(), serverHost1);
			assertEquals(loadBalanceThriftClient2.getServerPort(), serverPort1);
			loadBalanceThriftClient2.getTransport();
			assertEquals(loadBalanceThriftClient2.getServerHost(), serverHost2);
			assertEquals(loadBalanceThriftClient2.getServerPort(), serverPort2);
			assertEquals(loadBalanceThriftClient2.getThriftClientConfiguration(), configuration);
		} catch (ClassNotFoundException e) {
			fail();
		} catch (STException e) {
			fail();
		}
	}

}
