package com.ikamobile.ikasoa.admin.zookeeper;

import java.util.ArrayList;
import java.util.List;

import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;

/**
 * Zookeeper客户端工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ZkClientTools extends ZkBase {

	public ZkClientTools(String zkServer, String zkNode) {
		super(zkServer, zkNode);
	}

	public List<ServerInfo> getServerInfoList() {
		List<ServerInfo> serverInfoList = new ArrayList<>();
		List<String> nList = zkClient.getChildren(zkNode);
		for (String n : nList) {
			ZkServerNodeObject sno = (ZkServerNodeObject) zkClient
					.readData(new StringBuilder(zkNode).append("/").append(n).toString());
			serverInfoList.add(new ServerInfo(sno.getServerHost(), sno.getServerPort()));
		}
		return serverInfoList;
	}

}
