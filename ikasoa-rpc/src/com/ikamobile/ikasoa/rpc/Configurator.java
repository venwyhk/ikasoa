package com.ikamobile.ikasoa.rpc;

import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikamobile.ikasoa.rpc.handler.ClientInvocationHandler;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandlerFactory.ProtocolType;

/**
 * 配置信息
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class Configurator {

	// 服务器配置信息
	private ThriftServerConfiguration thriftServerConfiguration;

	// 客户端配置信息
	private ThriftClientConfiguration thriftClientConfiguration;

	// 转换协议类型
	private ProtocolType protocolType;

	// 客户端调用拦截器
	private ClientInvocationHandler clientInvocationHandler;

	public Configurator() {
	}

	public Configurator(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public ThriftServerConfiguration getThriftServerConfiguration() {
		return thriftServerConfiguration;
	}

	public void setThriftServerConfiguration(ThriftServerConfiguration thriftServerConfiguration) {
		this.thriftServerConfiguration = thriftServerConfiguration;
	}

	public ThriftClientConfiguration getThriftClientConfiguration() {
		return thriftClientConfiguration;
	}

	public void setThriftClientConfiguration(ThriftClientConfiguration thriftClientConfiguration) {
		this.thriftClientConfiguration = thriftClientConfiguration;
	}

	public ProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public ClientInvocationHandler getClientInvocationHandler() {
		return clientInvocationHandler;
	}

	public void setClientInvocationHandler(ClientInvocationHandler clientInvocationHandler) {
		this.clientInvocationHandler = clientInvocationHandler;
	}

}
