package com.ikamobile.ikasoa.admin.zookeeper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.thrift.TProcessor;

import com.ikamobile.ikasoa.core.thrift.server.ServerAspect;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServer;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;

/**
 * 服务注册
 * <p>
 * 将Thrift服务添加为Zookeeper节点的切面实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ZkServerAspect extends ZkBase implements ServerAspect {

	public ZkServerAspect(String zkServer, String zkNode) {
		super(zkServer, zkNode);
	}

	@Override
	public void beforeStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server) {
	}

	@Override
	public void afterStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server) {
		if (!zkClient.exists(zkNode)) {
			zkClient.createPersistent(zkNode, "ikasoa_node");
		}
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String n = new StringBuilder(zkNode).append("/").append(serverName).append("-").append(ip).append("-")
					.append(serverPort).append(" ").toString();
			zkClient.createEphemeralSequential(n, new ZkServerNodeObject(serverName, ip, serverPort));
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
		zkClient.delete(zkNode + "/" + serverName);
	}

}
