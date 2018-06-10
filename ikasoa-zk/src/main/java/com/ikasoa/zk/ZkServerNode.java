package com.ikasoa.zk;

import java.io.Serializable;

import lombok.Data;

/**
 * 服务注册对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Data
public class ZkServerNode implements Serializable {

	private static final long serialVersionUID = 1L;

	private String serverName;

	private String serverHost;

	private int serverPort;

	private String transportFactoryClassName;

	private String protocolFactoryClassName;

	private String processorFactoryClassName;

	private String processorClassName;

	public ZkServerNode(String serverName, String serverHost, int serverPort) {
		this.serverName = serverName;
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}

}
