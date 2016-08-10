package com.ikamobile.ikasoa.rpc;

import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikamobile.ikasoa.rpc.handler.ClientInvocationHandler;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandler;

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

	// 转换协议实现类
	@SuppressWarnings("rawtypes")
	private Class<ProtocolHandler> protocolHandlerClass;

	// 客户端调用拦截器
	private ClientInvocationHandler clientInvocationHandler;

	public Configurator() {
	}

	@SuppressWarnings("rawtypes")
	public Configurator(Class<ProtocolHandler> protocolHandlerClass) {
		this.protocolHandlerClass = protocolHandlerClass;
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

	@SuppressWarnings("rawtypes")
	public Class<ProtocolHandler> getProtocolHandlerClass() {
		return protocolHandlerClass;
	}

	@SuppressWarnings("rawtypes")
	public void setProtocolHandlerClass(Class<ProtocolHandler> protocolHandlerClass) {
		this.protocolHandlerClass = protocolHandlerClass;
	}

	public ClientInvocationHandler getClientInvocationHandler() {
		return clientInvocationHandler;
	}

	public void setClientInvocationHandler(ClientInvocationHandler clientInvocationHandler) {
		this.clientInvocationHandler = clientInvocationHandler;
	}

}
