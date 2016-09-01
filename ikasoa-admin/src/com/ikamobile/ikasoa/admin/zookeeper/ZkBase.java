package com.ikamobile.ikasoa.admin.zookeeper;

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

	public ZkBase(String zkServer, String zkNode) {
		if (StringUtil.isEmpty(zkServer)) {
			throw new RuntimeException("serverstring is null !");
		} else {
			this.zkClient = new ZkClient(zkServer);
		}
		if (StringUtil.isEmpty(zkNode)) {
			this.zkNode = "";
		} else {
			this.zkNode = zkNode;
		}
	}

}
