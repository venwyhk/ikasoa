package com.ikamobile.ikasoa.admin.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.utils.StringUtil;

/**
 * Zookeeper基础操作类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ZkBase {
	
	private static final Logger LOG = LoggerFactory.getLogger(ZkBase.class);

	private ZkClient zkClient;

	private String zkNode;

	public final String ZK_ROOT_NODE = "/";

	public ZkBase(String zkServerString, String zkNode) {
		if (StringUtil.isEmpty(zkServerString)) {
			throw new RuntimeException("zkServerString is null !");
		} else {
			this.zkClient = new ZkClient(zkServerString);
		}
		if (StringUtil.isEmpty(zkNode)) {
			this.zkNode = ZK_ROOT_NODE;
		} else {
			this.zkNode = zkNode;
		}
	}

	public boolean isExistNode(String serverName, String serverHost, int serverPort) {
		if (zkClient == null) {
			throw new RuntimeException("zkClient is null !");
		}
		List<String> nodeList = zkClient.getChildren(zkNode);
		for (String n : nodeList) {
			if (n.contains(new StringBuilder(serverName).append("-").append(serverHost).append("-").append(serverPort)
					.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public List<ServerInfo> getServerInfoList() {
		List<ServerInfo> serverInfoList = new ArrayList<>();
		List<String> nList = zkClient.getChildren(zkNode);
		for (String n : nList) {
			ZkServerNode zksn = (ZkServerNode) zkClient
					.readData(new StringBuilder(zkNode).append("/").append(n).toString());
			serverInfoList.add(new ServerInfo(zksn.getServerHost(), zksn.getServerPort()));
		}
		LOG.debug("serverInfoList is : " + serverInfoList);
		return serverInfoList;
	}

	public ZkClient getZkClient() {
		return zkClient;
	}

	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}

	public String getZkNode() {
		return zkNode;
	}

	public void setZkNode(String zkNode) {
		this.zkNode = zkNode;
	}

}
