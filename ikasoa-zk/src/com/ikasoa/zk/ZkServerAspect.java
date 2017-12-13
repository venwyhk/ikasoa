package com.ikasoa.zk;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.I0Itec.zkclient.ZkClient;
import org.apache.thrift.TProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikasoa.zk.utils.LocalUtil;
import com.ikasoa.core.thrift.server.ServerAspect;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;

/**
 * 服务注册
 * <p>
 * 将Thrift服务添加为Zookeeper节点的切面实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ZkServerAspect implements ServerAspect {

	private static final Logger LOG = LoggerFactory.getLogger(ZkServerAspect.class);

	private ZkBase zkBase;

	// 自定义注册到Zookeeper的服务地址
	private String host;

	// 自定义注册到Zookeeper的端口
	private int port;

	// 是否使用本地IP地址
	// 如果服务部署在docker容器中,将此参数设置为true可获取宿主机IP.
	private boolean isLocalIp = Boolean.FALSE;

	// 默认节点名称
	private static String DEFAULT_NODE_NAME = "ikasoa_node";

	private String sNodeStr;

	public ZkServerAspect(String zkServerString, String zkNode) {
		this.zkBase = new ZkBase(zkServerString, zkNode);
	}

	public ZkServerAspect(String zkServerString, String zkNode, String host, int port) {
		this.zkBase = new ZkBase(zkServerString, zkNode);
		this.host = host;
		this.port = port;
	}

	public ZkServerAspect(String zkServerString, String zkNode, boolean isLocalIp) {
		this.zkBase = new ZkBase(zkServerString, zkNode);
		this.isLocalIp = isLocalIp;
	}

	@Override
	public void beforeStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server) {
	}

	@Override
	public void afterStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server) {
		ZkClient zkClient = zkBase.getZkClient();
		String zkNode = zkBase.getZkNode();
		if (!zkClient.exists(zkNode))
			zkClient.createPersistent(zkNode,
					new StringBuilder(zkBase.ZK_ROOT_NODE).append(DEFAULT_NODE_NAME).toString());
		try {
			String serverHost;
			if (isLocalIp)
				serverHost = LocalUtil.getLocalIP();
			else
				serverHost = InetAddress.getLocalHost().getHostAddress();
			if (StringUtil.isNotEmpty(host))
				serverHost = host;
			if (ServerUtil.isPort(port))
				serverPort = port;
			StringBuilder sNodeSB = new StringBuilder(zkNode);
			if (!zkBase.ZK_ROOT_NODE.equals(zkNode))
				sNodeSB.append(zkBase.ZK_ROOT_NODE);
			sNodeStr = sNodeSB.append(serverName).append("-").append(serverHost).append("-").append(serverPort)
					.append(" ").toString();
			if (zkBase.isExistNode(serverName, serverHost, serverPort))
				throw new RuntimeException("Thrift server already register ! (name: " + serverName + " , host : "
						+ serverHost + " , port : " + serverPort + ")");
			ZkServerNode zkSNObj = new ZkServerNode(serverName, serverHost, serverPort);
			if (configuration != null) {
				if (configuration.getTransportFactory() != null)
					zkSNObj.setTransportFactoryClassName(configuration.getTransportFactory().getClass().getName());
				if (configuration.getProtocolFactory() != null)
					zkSNObj.setTransportFactoryClassName(configuration.getProtocolFactory().getClass().getName());
				if (configuration.getProcessorFactory() != null)
					zkSNObj.setTransportFactoryClassName(configuration.getProcessorFactory().getClass().getName());
			}
			if (processor != null)
				zkSNObj.setProcessorClassName(processor.getClass().getName());
			LOG.debug("Create server node : " + sNodeStr);
			zkClient.createEphemeralSequential(sNodeStr, zkSNObj);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (SocketException e) {
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
			LOG.debug("Delete server node : " + sNodeStr);
			zkBase.getZkClient().delete(sNodeStr);
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
