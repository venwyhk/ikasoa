package com.ikasoa.core.loadbalance;

import java.util.List;

import org.junit.Test;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.ServerInfo;
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
		List<Node<ServerInfo>> serverInfoNodeList = ListUtil.newArrayList();
		for (int i = 1; i <= testSize; i++)
			serverInfoNodeList.add(new Node<ServerInfo>(new ServerInfo(StringUtil.merge("192.168.1.", i),
					ServerUtil.getNewPort(String.format("testPollingLoadBalanceImpl_%d", i)))));
		LoadBalance<ServerInfo> loadBalance = new PollingLoadBalanceImpl<>(serverInfoNodeList);
		for (int j = 1; j <= testSize; j++) {
			Node<ServerInfo> serverInfoNode = loadBalance.getNode();
			assertNotNull(serverInfoNode);
			assertEquals(serverInfoNode.getValue().getHost(), StringUtil.merge("192.168.1.", j));
			assertEquals(serverInfoNode.getValue().getPort(),
					ServerUtil.getNewPort(String.format("testPollingLoadBalanceImpl_%d", j)));
			serverInfoNode = loadBalance.getNode();
			assertEquals(serverInfoNode.getValue().getHost(), StringUtil.merge("192.168.1.", j));
			assertEquals(serverInfoNode.getValue().getPort(),
					ServerUtil.getNewPort(String.format("testPollingLoadBalanceImpl_%d", j)));
			next(loadBalance);
		}
		// 测试新增服务器节点
		serverInfoNodeList.add(new Node<ServerInfo>(
				new ServerInfo(LOCAL_IP, ServerUtil.getNewPort("testPollingLoadBalanceImpl_new"))));
		for (int k = 1; k <= testSize; k++)
			next(loadBalance);
		Node<ServerInfo> serverInfoNode = loadBalance.getNode();
		assertEquals(serverInfoNode.getValue().getHost(), LOCAL_IP);
		assertEquals(serverInfoNode.getValue().getPort(), ServerUtil.getNewPort("testPollingLoadBalanceImpl_new"));
	}

	/**
	 * 带权重的轮询负载均衡测试
	 */
	@Test
	public void testWeightPollingLoadBalanceImpl() {
		List<Node<ServerInfo>> serverInfoNodeList = ListUtil.buildArrayList(
				new Node<>(new ServerInfo("192.168.1.1", ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_1")),
						1),
				new Node<>(new ServerInfo("192.168.1.2", ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_2")),
						0),
				new Node<>(new ServerInfo("192.168.1.3", ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_3")),
						1));
		LoadBalance<ServerInfo> loadBalance = new PollingLoadBalanceImpl<>(serverInfoNodeList);
		Node<ServerInfo> serverInfoNode = loadBalance.getNode();
		assertNotNull(serverInfoNode);
		assertEquals(serverInfoNode.getValue().getHost(), "192.168.1.1");
		assertEquals(serverInfoNode.getValue().getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_1"));
		serverInfoNode = loadBalance.getNode();
		assertEquals(serverInfoNode.getValue().getHost(), "192.168.1.1");
		assertEquals(serverInfoNode.getValue().getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_1"));
		serverInfoNode = next(loadBalance);
		assertEquals(serverInfoNode.getValue().getHost(), "192.168.1.1");
		assertEquals(serverInfoNode.getValue().getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_1"));
		serverInfoNode = next(loadBalance);
		assertEquals(serverInfoNode.getValue().getHost(), "192.168.1.2");
		assertEquals(serverInfoNode.getValue().getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_2"));
		serverInfoNode = next(loadBalance);
		assertEquals(serverInfoNode.getValue().getHost(), "192.168.1.3");
		assertEquals(serverInfoNode.getValue().getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_3"));
		serverInfoNode = next(loadBalance);
		assertEquals(serverInfoNode.getValue().getHost(), "192.168.1.3");
		assertEquals(serverInfoNode.getValue().getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_3"));
		// 测试新增服务器节点
		serverInfoNodeList.add(
				new Node<>(new ServerInfo(LOCAL_IP, ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_4")), 0));
		for (int i = 1; i <= 6; i++)
			serverInfoNode = next(loadBalance);
		assertEquals(serverInfoNode.getValue().getHost(), LOCAL_IP);
		assertEquals(serverInfoNode.getValue().getPort(), ServerUtil.getNewPort("testWeightPollingLoadBalanceImpl_4"));
	}

	/**
	 * 随机负载均衡测试
	 */
	@Test
	public void testRandomLoadBalanceImpl() {
		List<Node<ServerInfo>> serverInfoNodeList = ListUtil.buildArrayList(
				new Node<>(new ServerInfo("192.168.1.1", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.2", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.3", ServerUtil.getNewPort())));
		LoadBalance<ServerInfo> loadBalance = new RandomLoadBalanceImpl<>(serverInfoNodeList);
		Node<ServerInfo> serverInfoNode = loadBalance.getNode();
		assertNotNull(serverInfoNode.getValue().getHost());
		String serverHost1 = serverInfoNode.getValue().getHost();
		assertEquals(loadBalance.getNode().getValue().getHost(), serverHost1);
		serverInfoNode = next(loadBalance);
		String serverHost2 = serverInfoNode.getValue().getHost();
		assertEquals(loadBalance.getNode().getValue().getHost(), serverHost2);
	}

	/**
	 * 一致性Hash负载均衡测试
	 */
	@Test
	public void testConsistencyHashLoadBalanceImpl() {
		List<Node<ServerInfo>> serverInfoNodeList = ListUtil.buildArrayList(
				new Node<>(new ServerInfo("192.168.1.1", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.2", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.3", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.4", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.5", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.6", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.7", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.8", ServerUtil.getNewPort())),
				new Node<>(new ServerInfo("192.168.1.9", ServerUtil.getNewPort())));
		try {
			Node<ServerInfo> serverInfoNode1 = testSimpleConsistencyHashLoadBalanceImpl(serverInfoNodeList, "abcdef");
			Node<ServerInfo> serverInfoNode2 = testSimpleConsistencyHashLoadBalanceImpl(serverInfoNodeList, "123456");
			// 这里有一定机率是相同的.如果凑巧hash到同一地址,就换个hash再试一次.
			if (StringUtil.equals(serverInfoNode1.getValue().getHost(), serverInfoNode2.getValue().getHost())) {
				Node<ServerInfo> serverInfoNode3 = testSimpleConsistencyHashLoadBalanceImpl(serverInfoNodeList,
						"987654321");
				assertFalse(serverInfoNode1.getValue().getHost().equals(serverInfoNode3.getValue().getHost()));
			}
		} catch (Exception e) {
			fail();
		}
	}

	private Node<ServerInfo> testSimpleConsistencyHashLoadBalanceImpl(List<Node<ServerInfo>> serverInfoNodeList,
			String hash) {
		LoadBalance<ServerInfo> loadBalance;
		loadBalance = new ConsistencyHashLoadBalanceImpl<>(serverInfoNodeList, hash);
		Node<ServerInfo> serverInfoNode = loadBalance.getNode();
		assertNotNull(serverInfoNode.getValue().getHost());
		assertEquals(loadBalance.getNode().getValue().getHost(), serverInfoNode.getValue().getHost());
		serverInfoNode = next(loadBalance);
		assertEquals(loadBalance.getNode().getValue().getHost(), serverInfoNode.getValue().getHost());
		return serverInfoNode;
	}

	private Node<ServerInfo> next(LoadBalance<ServerInfo> loadBalance) {
		Node<ServerInfo> serverInfoNode = null;
		try {
			serverInfoNode = loadBalance.next();
		} catch (IkasoaException e) {
			fail();
		}
		assertNotNull(serverInfoNode);
		return serverInfoNode;
	}

}
