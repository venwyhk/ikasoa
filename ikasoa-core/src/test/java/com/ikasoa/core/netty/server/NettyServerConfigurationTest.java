package com.ikasoa.core.netty.server;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Netty服务端配置对象单元测试
 */
public class NettyServerConfigurationTest extends TestCase {

	@Test
	public void testNettyServerConfiguration() {
		NettyServerConfiguration nettyServerConfiguration = new NettyServerConfiguration(null);
		// 是否有默认值
		assertNotNull(nettyServerConfiguration.getWorkerCount());
		assertNotNull(nettyServerConfiguration.getMaxFrameSize());
		assertNotNull(nettyServerConfiguration.getQueuedResponseLimit());
	}

}
