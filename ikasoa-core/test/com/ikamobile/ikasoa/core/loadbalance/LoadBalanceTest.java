package com.ikamobile.ikasoa.core.loadbalance;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.loadbalance.LoadBalance;
import com.ikamobile.ikasoa.core.loadbalance.impl.ConsistencyHashLoadBalanceImpl;
import com.ikamobile.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikamobile.ikasoa.core.loadbalance.impl.RandomLoadBalanceImpl;

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
		List<ServerInfo> serverInfoList = new ArrayList<>();
		for (int i = 1; i <= testSize; i++) {
			serverInfoList.add(new ServerInfo(new StringBuilder("192.168.1.").append(i).toString(), 20000 + i));
		}
		LoadBalance loadBalance = new PollingLoadBalanceImpl(serverInfoList);
		for (int j = 1; j <= testSize; j++) {
			ServerInfo serverInfo = loadBalance.getServerInfo();
			assertNotNull(serverInfo);
			assertEquals(serverInfo.getHost(), new StringBuilder("192.168.1.").append(j).toString());
			assertEquals(serverInfo.getPort(), 20000 + j);
			serverInfo = loadBalance.getServerInfo();
			assertEquals(serverInfo.getHost(), new StringBuilder("192.168.1.").append(j).toString());
			assertEquals(serverInfo.getPort(), 20000 + j);
			next(loadBalance);
		}
		// 测试新增服务器地址
		serverInfoList.add(new ServerInfo(LOCAL_IP, 33333));
		for (int k = 1; k <= testSize; k++) {
			next(loadBalance);
		}
		ServerInfo serverInfo = loadBalance.getServerInfo();
		assertEquals(serverInfo.getHost(), LOCAL_IP);
		assertEquals(serverInfo.getPort(), 33333);
	}

	/**
	 * 带权重的轮询负载均衡测试
	 */
	@Test
	public void testWeightPollingLoadBalanceImpl() {
		List<ServerInfo> serverInfoList = new ArrayList<>();
		serverInfoList.add(new ServerInfo("192.168.1.1", 30001, 1));
		serverInfoList.add(new ServerInfo("192.168.1.2", 30002, 0));
		serverInfoList.add(new ServerInfo("192.168.1.3", 30003, 1));
		LoadBalance loadBalance = new PollingLoadBalanceImpl(serverInfoList);
		ServerInfo serverInfo = loadBalance.getServerInfo();
		assertNotNull(serverInfo);
		assertEquals(serverInfo.getHost(), "192.168.1.1");
		assertEquals(serverInfo.getPort(), 30001);
		serverInfo = loadBalance.getServerInfo();
		assertEquals(serverInfo.getHost(), "192.168.1.1");
		assertEquals(serverInfo.getPort(), 30001);
		serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), "192.168.1.1");
		assertEquals(serverInfo.getPort(), 30001);
		serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), "192.168.1.2");
		assertEquals(serverInfo.getPort(), 30002);
		serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), "192.168.1.3");
		assertEquals(serverInfo.getPort(), 30003);
		serverInfo = next(loadBalance);
		assertEquals(serverInfo.getHost(), "192.168.1.3");
		assertEquals(serverInfo.getPort(), 30003);
		// 测试新增服务器地址
		serverInfoList.add(new ServerInfo(LOCAL_IP, 30004, 0));
		for (int i = 1; i <= 6; i++) {
			serverInfo = next(loadBalance);
		}
		assertEquals(serverInfo.getHost(), LOCAL_IP);
		assertEquals(serverInfo.getPort(), 30004);
	}

	/**
	 * 随机负载均衡测试
	 */
	@Test
	public void testRandomLoadBalanceImpl() {
		List<ServerInfo> serverInfoList = new ArrayList<>();
		serverInfoList.add(new ServerInfo("192.168.1.1", 40001));
		serverInfoList.add(new ServerInfo("192.168.1.2", 40002));
		serverInfoList.add(new ServerInfo("192.168.1.3", 40003));
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
		List<ServerInfo> serverInfoList = new ArrayList<>();
		serverInfoList.add(new ServerInfo("192.168.1.1", 50001));
		serverInfoList.add(new ServerInfo("192.168.1.2", 50002));
		serverInfoList.add(new ServerInfo("192.168.1.3", 50003));
		LoadBalance loadBalance1, loadBalance2;
		try {
			loadBalance1 = new ConsistencyHashLoadBalanceImpl(serverInfoList, "abc");
			ServerInfo serverInfo1 = loadBalance1.getServerInfo();
			assertNotNull(serverInfo1.getHost());
			assertEquals(loadBalance1.getServerInfo().getHost(), serverInfo1.getHost());
			serverInfo1 = next(loadBalance1);
			assertEquals(loadBalance1.getServerInfo().getHost(), serverInfo1.getHost());
			loadBalance2 = new ConsistencyHashLoadBalanceImpl(serverInfoList, "123");
			ServerInfo serverInfo2 = loadBalance2.getServerInfo();
			assertNotNull(serverInfo2.getHost());
			assertEquals(loadBalance2.getServerInfo().getHost(), serverInfo2.getHost());
			serverInfo2 = next(loadBalance2);
			assertEquals(loadBalance2.getServerInfo().getHost(), serverInfo2.getHost());
			assertFalse(serverInfo1.getHost().equals(serverInfo2.getHost()));
		} catch (Exception e) {
			fail();
		}
	}

	private ServerInfo next(LoadBalance loadBalance) {
		ServerInfo serverInfo = null;
		try {
			serverInfo = loadBalance.next();
		} catch (STException e) {
			fail();
		}
		assertNotNull(serverInfo);
		return serverInfo;
	}

}
