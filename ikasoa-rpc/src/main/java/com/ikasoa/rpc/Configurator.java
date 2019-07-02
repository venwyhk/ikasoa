package com.ikasoa.rpc;

import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.rpc.handler.ClientInvocationHandler;
import com.ikasoa.rpc.handler.ProtocolHandler;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置信息
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Data
@NoArgsConstructor
public class Configurator {

	/**
	 * 服务器配置信息
	 */
	private ThriftServerConfiguration thriftServerConfiguration;

	/**
	 * 客户端配置信息
	 */
	private ThriftClientConfiguration thriftClientConfiguration;

	/**
	 * 客户端配置服务调用信息
	 */
	private ServerInfoWrapper serverInfoWrapper;

	/**
	 * 转换协议实现类
	 */
	private ProtocolHandler<?, ?> protocolHandler;

	/**
	 * 客户端调用拦截器
	 */
	private ClientInvocationHandler clientInvocationHandler;

	/**
	 * 是否使用阻塞式IO
	 */
	private boolean isNonBlockingIO = false;

	public Configurator(ServerInfoWrapper serverInfoWrapper) {
		this.serverInfoWrapper = serverInfoWrapper;
	}

	public Configurator(ProtocolHandler<?, ?> protocolHandler) {
		this.protocolHandler = protocolHandler;
	}

	public Configurator(ServerInfoWrapper serverInfoWrapper, ProtocolHandler<?, ?> protocolHandler) {
		this.serverInfoWrapper = serverInfoWrapper;
		this.protocolHandler = protocolHandler;
	}

}
