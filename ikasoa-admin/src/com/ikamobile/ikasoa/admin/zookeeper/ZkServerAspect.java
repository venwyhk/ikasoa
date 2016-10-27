package com.ikamobile.ikasoa.admin.zookeeper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.thrift.TProcessor;

import com.ikamobile.ikasoa.core.thrift.server.ServerAspect;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServer;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikamobile.ikasoa.core.utils.ServerUtil;
import com.ikamobile.ikasoa.core.utils.StringUtil;

/**
 * 服务注册
 * <p>
 * 将Thrift服务添加为Zookeeper节点的切面实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ZkServerAspect extends ZkBase implements ServerAspect {

	// 自定义注册到Zookeeper的服务地址
	private String host;

	// 自定义注册到Zookeeper的端口
	private int port;

	// 默认节点名称
	private static String DEFAULT_NODE_NAME = "ikasoa_node";

	private String sNodeStr;

	public ZkServerAspect(String zkServerString, String zkNode) {
		super(zkServerString, zkNode);
	}

	@Override
	public void beforeStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server) {
	}

	@Override
	public void afterStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server) {
		if (!zkClient.exists(zkNode)) {
			zkClient.createPersistent(zkNode, new StringBuilder(ZK_ROOT_NODE).append(DEFAULT_NODE_NAME).toString());
		}
		try {
			String serverHost = InetAddress.getLocalHost().getHostAddress();
			if (StringUtil.isNotEmpty(host)) {
				serverHost = host;
			}
			if (ServerUtil.isPort(port)) {
				serverPort = port;
			}
			StringBuilder sNodeSB = new StringBuilder(zkNode);
			if (!ZK_ROOT_NODE.equals(zkNode)) {
				sNodeSB.append(ZK_ROOT_NODE);
			}
			sNodeStr = sNodeSB.append(serverName).append("-").append(serverHost).append("-").append(serverPort)
					.append(" ").toString();
			if (isExistNode(serverName, serverHost, serverPort)) {
				throw new RuntimeException("Thrift server already register ! (name: " + serverName + " , host : "
						+ serverHost + " , port : " + serverPort + ")");
			}
			ZkServerNode zkSNObj = new ZkServerNode(serverName, serverHost, serverPort);
			if (configuration != null) {
				if (configuration.getTransportFactory() != null) {
					zkSNObj.setTransportFactoryClassName(configuration.getTransportFactory().getClass().getName());
				}
				if (configuration.getProtocolFactory() != null) {
					zkSNObj.setTransportFactoryClassName(configuration.getProtocolFactory().getClass().getName());
				}
				if (configuration.getProcessorFactory() != null) {
					zkSNObj.setTransportFactoryClassName(configuration.getProcessorFactory().getClass().getName());
				}
			}
			if (processor != null) {
				zkSNObj.setProcessorClassName(processor.getClass().getName());
			}
			zkClient.createEphemeralSequential(sNodeStr, zkSNObj);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void beforeStop(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server) {
	}

	@Override
	public void afterStop(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server) {
		if (StringUtil.isNotEmpty(sNodeStr)) {
			zkClient.delete(sNodeStr);
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
