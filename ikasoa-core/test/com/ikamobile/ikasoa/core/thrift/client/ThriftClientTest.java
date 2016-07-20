package com.ikamobile.ikasoa.core.thrift.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikamobile.ikasoa.core.thrift.client.impl.DefaultThriftClientImpl;
import com.ikamobile.ikasoa.core.thrift.client.impl.LoadBalanceThriftClientImpl;

import junit.framework.TestCase;

/**
 * Thrift客户端单元测试
 * <p>
 * 仅测试基础属性读写
 */
public class ThriftClientTest extends TestCase {

	private String serverHost = "localhost";

	private int serverPort = 29000;

	private ThriftClientConfiguration configuration = new ThriftClientConfiguration();

	/**
	 * 默认客户端测试
	 */
	@Test
	public void testDefaultThriftClientImpl() {
		ThriftClient defaultThriftClient = new DefaultThriftClientImpl(serverHost, serverPort, configuration);
		assertEquals(defaultThriftClient.getServerHost(), serverHost);
		assertEquals(defaultThriftClient.getServerPort(), serverPort);
		assertEquals(defaultThriftClient.getThriftClientConfiguration(), configuration);
	}

	/**
	 * 负载均衡客户端测试
	 */
	@Test
	public void testLoadBalanceThriftClientImpl() {
		List<ServerInfo> serverInfoList = new ArrayList<>();
		serverInfoList.add(new ServerInfo(serverHost, serverPort));
		ThriftClient loadBalanceThriftClient = new LoadBalanceThriftClientImpl(
				new PollingLoadBalanceImpl(serverInfoList), configuration);
		assertEquals(loadBalanceThriftClient.getServerHost(), serverHost);
		assertEquals(loadBalanceThriftClient.getServerPort(), serverPort);
		assertEquals(loadBalanceThriftClient.getThriftClientConfiguration(), configuration);
	}

}
