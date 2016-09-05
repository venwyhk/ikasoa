package com.ikamobile.ikasoa.admin.zookeeper;

import com.ikamobile.ikasoa.core.ServerCheck;

/**
 * 服务可用性验证实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ZkServerCheck extends ZkBase implements ServerCheck {

	public ZkServerCheck(String zkServerString, String zkNode) {
		super(zkServerString, zkNode);
	}

	@Override
	public boolean check(String serverHost, int serverPort) {
		return isExistNode("", serverHost, serverPort);
	}

}
