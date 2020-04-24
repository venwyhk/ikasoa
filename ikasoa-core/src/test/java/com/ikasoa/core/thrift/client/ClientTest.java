package com.ikasoa.core.thrift.client;

import java.util.List;

import org.junit.Test;

import com.ikasoa.core.ServerInfo;
import com.ikasoa.core.TestConstants;
import com.ikasoa.core.loadbalance.Node;
import com.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.impl.LoadBalanceThriftClientImpl;
import com.ikasoa.core.thrift.client.impl.ThriftClientImpl;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.ServerUtil;

import junit.framework.TestCase;
import lombok.Cleanup;

/**
 * 客户端单元测试
 * <p>
 * 仅测试基础属性读写.
 */
public class ClientTest extends TestCase {

	private ThriftClientConfiguration configuration = new ThriftClientConfiguration();

	/**
	 * 客户端测试
	 */
	@Test
	public void testThriftClientImpl() {
		int serverPort = ServerUtil.getNewPort();
		try (ThriftClient thriftClient = new ThriftClientImpl(new ServerInfo(TestConstants.LOCAL_HOST, serverPort),
				configuration)) {
			assertEquals(thriftClient.getServerInfo().getHost(), TestConstants.LOCAL_HOST);
			assertEquals(thriftClient.getServerInfo().getPort(), serverPort);
			assertEquals(thriftClient.getThriftClientConfiguration(), configuration);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * 负载均衡客户端测试
	 */
	@Test
	public void testLoadBalanceThriftClientImpl() {
		String serverHost1 = TestConstants.LOCAL_HOST;
		int serverPort1 = ServerUtil.getNewPort();
		List<Node<ServerInfo>> serverInfoNodeList = ListUtil
				.buildArrayList(new Node<>(new ServerInfo(serverHost1, serverPort1)));
		// 以下测试利用自定义负载均衡类型通过GeneralFactory获取Client对象
		try {
			@Cleanup
			ThriftClient loadBalanceThriftClient1 = new LoadBalanceThriftClientImpl(
					new PollingLoadBalanceImpl<>(serverInfoNodeList), configuration);
			assertEquals(loadBalanceThriftClient1.getServerInfo().getHost(), serverHost1);
			assertEquals(loadBalanceThriftClient1.getServerInfo().getPort(), serverPort1);
			assertEquals(loadBalanceThriftClient1.getThriftClientConfiguration(), configuration);
			String serverHost2 = "127.0.0.1";
			int serverPort2 = ServerUtil.getNewPort();
			serverInfoNodeList.add(new Node<>(new ServerInfo(serverHost2, serverPort2)));
			@Cleanup
			ThriftClient loadBalanceThriftClient2 = new GeneralFactory(configuration)
					.getThriftClient(serverInfoNodeList, new PollingLoadBalanceImpl<>());
			loadBalanceThriftClient2.getTransport();
			assertEquals(loadBalanceThriftClient2.getServerInfo().getHost(), serverHost1);
			assertEquals(loadBalanceThriftClient2.getServerInfo().getPort(), serverPort1);
			loadBalanceThriftClient2.getTransport();
			assertEquals(loadBalanceThriftClient2.getServerInfo().getHost(), serverHost2);
			assertEquals(loadBalanceThriftClient2.getServerInfo().getPort(), serverPort2);
			assertEquals(loadBalanceThriftClient2.getThriftClientConfiguration(), configuration);
		} catch (Exception e) {
			fail();
		}
	}

}
