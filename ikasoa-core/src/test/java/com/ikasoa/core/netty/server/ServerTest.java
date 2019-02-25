package com.ikasoa.core.netty.server;

import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import com.ikasoa.core.ServerTestCase;
import com.ikasoa.core.TestConstants;
import com.ikasoa.core.netty.NettyGeneralFactory;
import com.ikasoa.core.thrift.Factory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.server.ThriftSimpleService;
import com.ikasoa.core.thrift.server.ThriftSimpleServiceImpl;
import com.ikasoa.core.thrift.server.ThriftSimpleService.Iface;
import com.ikasoa.core.utils.ServerUtil;

/**
 * Netty服务端单元测试
 */
public class ServerTest extends ServerTestCase {

	private static String serverName = "TestNettyServer";

	private static Factory factory = new NettyGeneralFactory();

	@Test
	public void testDefaultNettyServerImpl() {
		int serverPort = ServerUtil.getNewPort();
		NettyServer defaultNettyServer = (NettyServer) factory.getThriftServer(serverName, serverPort,
				new ThriftSimpleService.Processor<Iface>(new ThriftSimpleServiceImpl()));
		assertEquals(defaultNettyServer.getServerName(), serverName);
		assertEquals(defaultNettyServer.getServerPort(), serverPort);
		defaultNettyServer.run();
		waiting();
		try (ThriftClient client = factory.getThriftClient(TestConstants.LOCAL_HOST, serverPort);
				TTransport transport = client.getTransport()) {
			transport.open();
			assertEquals(TestConstants.TEST_STRING,
					new ThriftSimpleService.Client(client.getProtocol(transport)).get(TestConstants.TEST_STRING));
		} catch (Exception e) {
			fail();
		} finally {
			defaultNettyServer.stop();
		}
	}

}
