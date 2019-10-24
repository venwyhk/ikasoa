package com.ikasoa.zk;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Optional;

import org.I0Itec.zkclient.ZkClient;
import org.apache.thrift.TProcessor;

import com.ikasoa.zk.utils.LocalUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.ikasoa.core.thrift.server.ServerAspect;
import com.ikasoa.core.thrift.server.ServerConfiguration;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.utils.ObjectUtil;
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
@Slf4j
public class ZkServerAspect implements ServerAspect {

	private ZkBase zkBase;

	/**
	 * 自定义注册到Zookeeper的服务地址
	 */
	@Getter
	@Setter
	private String host;

	/**
	 * 自定义注册到Zookeeper的端口
	 */
	@Getter
	@Setter
	private int port;

	/**
	 * 是否使用本地IP地址.如果服务部署在docker容器中,将此参数设置为true可获取宿主机IP.
	 */
	private boolean isLocalIp = false;

	private String sNodeStr;

	/**
	 * 默认节点名称
	 */
	private static final String DEFAULT_NODE_NAME = "ikasoa_node";

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
	public void beforeStart(String serverName, int serverPort, ServerConfiguration configuration, TProcessor processor,
			ThriftServer server) {
		// Do nothing
	}

	@Override
	public void afterStart(String serverName, int serverPort, ServerConfiguration configuration, TProcessor processor,
			ThriftServer server) {
		ZkClient zkClient = zkBase.getZkClient();
		String zkNode = zkBase.getZkNode();
		if (!zkClient.exists(zkNode))
			zkClient.createPersistent(zkNode, StringUtil.merge(ZkBase.ZK_ROOT_NODE, DEFAULT_NODE_NAME));
		try {
			String serverHost = StringUtil.isNotEmpty(host) ? host
					: isLocalIp ? LocalUtil.getLocalIP() : InetAddress.getLocalHost().getHostAddress();
			if (ServerUtil.isPort(port))
				serverPort = port;
			if (!StringUtil.equals(ZkBase.ZK_ROOT_NODE, zkNode))
				zkNode = StringUtil.merge(zkNode, ZkBase.ZK_ROOT_NODE);
			sNodeStr = StringUtil.merge(zkNode, serverName, "-", serverHost, "-", serverPort, " ");
			if (zkBase.isExistNode(serverName, serverHost, serverPort))
				throw new RuntimeException("Server already register ! (name: " + serverName + " , host : " + serverHost
						+ " , port : " + serverPort + ")");
			ZkServerNode zkSNObj = new ZkServerNode(serverName, serverHost, serverPort);
			if (ObjectUtil.isNotNull(configuration)) {
				if (ObjectUtil.isNotNull(configuration.getTransportFactory()))
					zkSNObj.setTransportFactoryClassName(configuration.getTransportFactory().getClass().getName());
				if (ObjectUtil.isNotNull(configuration.getProtocolFactory()))
					zkSNObj.setTransportFactoryClassName(configuration.getProtocolFactory().getClass().getName());
				if (ObjectUtil.isNotNull(configuration.getProcessorFactory()))
					zkSNObj.setTransportFactoryClassName(configuration.getProcessorFactory().getClass().getName());
			}
			Optional.ofNullable(processor).ifPresent(p -> zkSNObj.setProcessorClassName(p.getClass().getName()));
			log.debug("Create server node : {}", sNodeStr);
			zkClient.createEphemeralSequential(sNodeStr, zkSNObj);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void beforeStop(String serverName, int serverPort, ServerConfiguration configuration, TProcessor processor,
			ThriftServer server) {
		// Do nothing
	}

	@Override
	public void afterStop(String serverName, int serverPort, ServerConfiguration configuration, TProcessor processor,
			ThriftServer server) {
		if (StringUtil.isNotEmpty(sNodeStr)) {
			log.debug("Delete server node : {}", sNodeStr);
			zkBase.getZkClient().delete(sNodeStr);
		}
	}

}
