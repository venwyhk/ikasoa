package com.ikamobile.ikasoa.admin.zookeeper;

import java.util.List;

import com.ikamobile.ikasoa.core.ServerCheck;

/**
 * 服务可用性验证实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ZkServerCheck extends ZkBase implements ServerCheck {

	public ZkServerCheck(String zkServer, String zkNode) {
		super(zkServer, zkNode);
	}

	@Override
	public boolean check(String serverHost, int serverPort) {
		List<String> nList = zkClient.getChildren(zkNode);
		for (String n : nList) {
			if (n.contains(new StringBuilder(serverHost).append("-").append(serverPort).toString())) {
				return true;
			}
		}
		return false;
	}

}
