package com.ikasoa.core.loadbalance;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.ikasoa.core.STException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.impl.ConsistencyHashLoadBalanceImpl;
import com.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikasoa.core.loadbalance.impl.RandomLoadBalanceImpl;

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
		serverInfoList.add(new ServerInfo("192.168.1.4", 50004));
		serverInfoList.add(new ServerInfo("192.168.1.5", 50005));
		serverInfoList.add(new ServerInfo("192.168.1.6", 50006));
		serverInfoList.add(new ServerInfo("192.168.1.7", 50007));
		serverInfoList.add(new ServerInfo("192.168.1.8", 50008));
		serverInfoList.add(new ServerInfo("192.168.1.9", 50009));
		try {
			ServerInfo serverInfo1 = testSimpleConsistencyHashLoadBalanceImpl(serverInfoList, "abcdef");
			ServerInfo serverInfo2 = testSimpleConsistencyHashLoadBalanceImpl(serverInfoList, "123456");
			// 这里有一定机率是相同的.如果凑巧hash到同一地址,就换个hash再试一次.
			if(serverInfo1.getHost().equals(serverInfo2.getHost())) {
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
		} catch (STException e) {
			fail();
		}
		assertNotNull(serverInfo);
		return serverInfo;
	}

}
