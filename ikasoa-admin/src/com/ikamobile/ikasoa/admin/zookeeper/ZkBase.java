package com.ikamobile.ikasoa.admin.zookeeper;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import com.ikamobile.ikasoa.core.utils.StringUtil;

/**
 * Zookeeper基础类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ZkBase {

	protected ZkClient zkClient;

	protected String zkNode;

	public ZkBase(String zkServerString, String zkNode) {
		if (StringUtil.isEmpty(zkServerString)) {
			throw new RuntimeException("zkServerString is null !");
		} else {
			this.zkClient = new ZkClient(zkServerString);
		}
		if (StringUtil.isEmpty(zkNode)) {
			this.zkNode = "";
		} else {
			this.zkNode = zkNode;
		}
	}

	protected boolean isExistNode(String serverName, String serverHost, int serverPort) {
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

}
