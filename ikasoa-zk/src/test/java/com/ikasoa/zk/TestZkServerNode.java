package com.ikasoa.zk;

import org.junit.Test;

import junit.framework.TestCase;

public class TestZkServerNode extends TestCase {

	@Test
	public void test() {
		String _serverName = "serverName";
		String _serverHost = "serverHost";
		Integer _serverPort = 9981;
		ZkServerNode node = new ZkServerNode(_serverName, _serverHost, _serverPort);
		assertEquals(_serverName, node.getServerName());
		assertEquals(_serverHost, node.getServerHost());
		assertEquals(_serverPort, node.getServerPort());
		String serverName = "serverName";
		String serverHost = "serverHost";
		Integer serverPort = 9982;
		String transportFactoryClassName = "transportFactoryClassName";
		String protocolFactoryClassName = "protocolFactoryClassName";
		String processorFactoryClassName = "processorFactoryClassName";
		String processorClassName = "processorClassName";
		node.setServerName(serverName);
		node.setServerHost(serverHost);
		node.setServerPort(serverPort);
		node.setTransportFactoryClassName(transportFactoryClassName);
		node.setProtocolFactoryClassName(protocolFactoryClassName);
		node.setProcessorFactoryClassName(processorFactoryClassName);
		node.setProcessorClassName(processorClassName);
		assertEquals(serverName, node.getServerName());
		assertEquals(serverHost, node.getServerHost());
		assertEquals(serverPort, node.getServerPort());
		assertEquals(transportFactoryClassName, node.getTransportFactoryClassName());
		assertEquals(protocolFactoryClassName, node.getProtocolFactoryClassName());
		assertEquals(processorFactoryClassName, node.getProcessorFactoryClassName());
		assertEquals(processorClassName, node.getProcessorClassName());
	}

}
