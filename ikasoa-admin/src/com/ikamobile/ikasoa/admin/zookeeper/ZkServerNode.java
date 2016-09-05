package com.ikamobile.ikasoa.admin.zookeeper;

import java.io.Serializable;

/**
 * 服务注册对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
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
		this.setServerName(serverName);
		this.setServerHost(serverHost);
		this.setServerPort(serverPort);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getTransportFactoryClassName() {
		return transportFactoryClassName;
	}

	public void setTransportFactoryClassName(String transportFactoryClassName) {
		this.transportFactoryClassName = transportFactoryClassName;
	}

	public String getProtocolFactoryClassName() {
		return protocolFactoryClassName;
	}

	public void setProtocolFactoryClassName(String protocolFactoryClassName) {
		this.protocolFactoryClassName = protocolFactoryClassName;
	}

	public String getProcessorFactoryClassName() {
		return processorFactoryClassName;
	}

	public void setProcessorFactoryClassName(String processorFactoryClassName) {
		this.processorFactoryClassName = processorFactoryClassName;
	}

	public String getProcessorClassName() {
		return processorClassName;
	}

	public void setProcessorClassName(String processorClassName) {
		this.processorClassName = processorClassName;
	}

}
