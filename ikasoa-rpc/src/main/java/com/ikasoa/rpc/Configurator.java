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
	 * 转换协议实现类
	 */
	@SuppressWarnings("rawtypes")
	private Class<ProtocolHandler> protocolHandlerClass;

	/**
	 * 客户端调用拦截器
	 */
	private ClientInvocationHandler clientInvocationHandler;

	/**
	 * 是否使用阻塞式IO
	 */
	private boolean isNonBlockingIO = Boolean.FALSE;

	@SuppressWarnings("rawtypes")
	public Configurator(Class<ProtocolHandler> protocolHandlerClass) {
		this.protocolHandlerClass = protocolHandlerClass;
	}

}
