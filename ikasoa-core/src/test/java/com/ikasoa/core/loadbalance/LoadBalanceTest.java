package com.ikasoa.core.loadbalance;

import java.util.List;

import org.junit.Test;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.impl.ConsistencyHashLoadBalanceImpl;
import com.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikasoa.core.loadbalance.impl.RandomLoadBalanceImpl;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;

import junit.framework.TestCase;

/**
 * 负载均衡单元测试
 */
public class LoadBalanceTest extends TestCase {

	private static String LOCAL_IP = "127.0.0.1";

	/**
	 * 轮询负载均衡测试
	 */
	@Test
	public void testPollingLoadBalanceImpl() {
		int testSize = 10;
		List<ServerInfo> serverInfoList = ListUtil.newArrayList();
		for (int i = 1; i <= testSize; i++)
			serverInfoList.add(new ServerInfo(StringUtil.merge("192.168.1.", i),
					ServerUtil.getNewPort(String.format("testPollingLoadBalanceImpl_%d", i))));
		LoadBalance loadBalance = new PollingLoadBalanceImpl(serverInfoList);
		for (int j = 1; j <= testSize; j++) {
			ServerInfo serverInfo = loadBalance.getServerInfo();
			assertNotNull(serverInfo);
			assertEquals(serverInfo.getHost(), StringUtil.merge("192.168.1.", j));
			assertEquals(serverInfo.getPort(),
					ServerUtil.getNewPort(String.format("testPollingLoadBalanceImpl_%d", j)));
			serverInfo = loadBalance.getServerInfo();
			assertEquals(serverInfo.getHost(), StringUtil.merge("192.168.1.", j));
			assertEquals(serverInfo.getPort(),
					ServerUtil.getNewPort(String.format("testPollingLoadBalanceImpl_%d", j)));
			next(loadBalance);
		}
		// 测试新增服务器地址
		serverInfoList.add(new ServerInfo(LOCAL_IP, ServerUtil.getNewPort("testPollingLoadBalanceImpl_new")));
		for (int k = 1; k <= testSize; k++)
			next(loadBalance);
		ServerInfo serverInfo = loadBalance.getServerInfo();
		assertEquals(serverInfo.getHost(), LOCAL_IP);
		assertEquals(serverInfo.getPort(), ServerUtil.getNewPort("testPollingLoadBalanceImpl_new"));
	}

	/**
	 * 带权重的轮询负载均衡测试
	 */
	@Test
	public void testWeightPollingLoadBalanceImpl() {
		List<ServerInfo> serverInfoList = ListUtil.buildArrayList(
				new ServerInfo("192.168.1.1", ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_1"), 1),
				new ServerInfo("192.168.1.2", ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_2"), 0),
				new ServerInfo("192.168.1.3", ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_3"), 1));
		LoadBalance loadBalance = new PollingLoadBalanceImpl(serverInfoList);
		ServerInfo serverInfo = loadBalance.getServerInfo();
		assertNotNull(serverInfo);
		assertEquals(serverInfo.getHost(), "192.168.1.1");
		assertEquals(serverInfo.getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_1"));
		serverInfo = loadBalance.getServerInfo();
		assertEquals(serverInfo.getHost(), "192.168.1.1");
		assertEquals(serverInfo.getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_1"));
		serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), "192.168.1.1");
		assertEquals(serverInfo.getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_1"));
		serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), "192.168.1.2");
		assertEquals(serverInfo.getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_2"));
		serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), "192.168.1.3");
		assertEquals(serverInfo.getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_3"));
		serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), "192.168.1.3");
		assertEquals(serverInfo.getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_3"));
		// 测试新增服务器地址
		serverInfoList.add(new ServerInfo(LOCAL_IP, ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_4"), 0));
		for (int i = 1; i <= 6; i++)
			serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), LOCAL_IP);
		assertEquals(serverInfo.getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_4"));
	}

	/**
	 * 随机负载均衡测试
	 */
	@Test
	public void testRandomLoadBalanceImpl() {
		List<ServerInfo> serverInfoList = ListUtil.buildArrayList(
				new ServerInfo("192.168.1.1", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.2", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.3", ServerUtil.getNewPort()));
		LoadBalance loadBalance = new RandomLoadBalanceImpl(serverInfoList);
		ServerInfo serverInfo = loadBalance.getServerInfo();
		assertNotNull(serverInfo.getHost());
		String serverHost1 = serverInfo.getHost();
		assertEquals(loadBalance.getServerInfo().getHost(), serverHost1);
		serverInfo = next(loadBalance);
		String serverHost2 = serverInfo.getHost();
		assertEquals(loadBalance.getServerInfo().getHost(), serverHost2);
	}

	/**
	 * 一致性Hash负载均衡测试
	 */
	@Test
	public void testConsistencyHashLoadBalanceImpl() {
		List<ServerInfo> serverInfoList = ListUtil.buildArrayList(
				new ServerInfo("192.168.1.1", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.2", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.3", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.4", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.5", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.6", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.7", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.8", ServerUtil.getNewPort()),
				new ServerInfo("192.168.1.9", ServerUtil.getNewPort()));
		try {
			ServerInfo serverInfo1 = testSimpleConsistencyHashLoadBalanceImpl(serverInfoList, "abcdef");
			ServerInfo serverInfo2 = testSimpleConsistencyHashLoadBalanceImpl(serverInfoList, "123456");
			// 这里有一定机率是相同的.如果凑巧hash到同一地址,就换个hash再试一次.
			if (serverInfo1.getHost().equals(serverInfo2.getHost())) {
				ServerInfo serverInfo3 = testSimpleConsistencyHashLoadBalanceImpl(serverInfoList, "987654321");
				assertFalse(serverInfo1.getHost().equals(serverInfo3.getHost()));
			}
		} catch (Exception e) {
			fail();
		}
	}

	private ServerInfo testSimpleConsistencyHashLoadBalanceImpl(List<ServerInfo> serverInfoList, String hash) {
		LoadBalance loadBalance;
		loadBalance = new ConsistencyHashLoadBalanceImpl(serverInfoList, hash);
		ServerInfo serverInfo = loadBalance.getServerInfo();
		assertNotNull(serverInfo.getHost());
		assertEquals(loadBalance.getServerInfo().getHost(), serverInfo.getHost());
		serverInfo = next(loadBalance);
		assertEquals(loadBalance.getServerInfo().getHost(), serverInfo.getHost());
		return serverInfo;
	}

	private ServerInfo next(LoadBalance loadBalance) {
		ServerInfo serverInfo = null;
		try {
			serverInfo = loadBalance.next();
		} catch (IkasoaException e) {
			fail();
		}
		assertNotNull(serverInfo);
		return serverInfo;
	}

}
