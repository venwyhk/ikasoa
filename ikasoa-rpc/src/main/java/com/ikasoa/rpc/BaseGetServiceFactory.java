package com.ikasoa.rpc;

import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.client.IkasoaClientService;
import com.ikasoa.rpc.client.InvalidGetService;
import com.ikasoa.rpc.handler.ClientInvocationHandler;
import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.handler.ProtocolHandlerFactory;
import com.ikasoa.rpc.handler.ReturnData;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 基础服务工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Slf4j
public class BaseGetServiceFactory<T, R> extends GeneralFactory {

	private ProtocolHandlerFactory<T, R> protocolHandlerFactory = new ProtocolHandlerFactory<>();

	@Getter
	@Setter
	private ProtocolHandler<?, ?> protocolHandler;

	@Getter
	@Setter
	private ClientInvocationHandler clientInvocationHandler;

	public BaseGetServiceFactory(ThriftServerConfiguration thriftServerConfiguration) {
		super(thriftServerConfiguration);
	}

	public BaseGetServiceFactory(ThriftClientConfiguration thriftClientConfiguration) {
		super(thriftClientConfiguration);
	}

	public BaseGetServiceFactory(ThriftServerConfiguration thriftServerConfiguration,
			ThriftClientConfiguration thriftClientConfiguration) {
		super(thriftServerConfiguration, thriftClientConfiguration);
	}

	public BaseGetService<T, R> getBaseGetService(ThriftClient thriftClient, String serviceKey, ReturnData resultData) {
		return getBaseGetService(thriftClient, serviceKey,
				protocolHandlerFactory.getProtocolHandler(resultData, getProtocolHandler()));
	}

	public BaseGetService<T, R> getBaseGetService(ThriftClient thriftClient, String serviceKey,
			ProtocolHandler<T, R> protocolHandler) {
		if (ObjectUtil.orIsNull(thriftClient, protocolHandler) || StringUtil.isEmpty(serviceKey))
			return new InvalidGetService<T, R>();
		log.debug("Create new instance 'IkasoaClientService' . (serverHost : {}, serverPort : {}, serviceKey : {})",
				thriftClient.getServerHost(), thriftClient.getServerPort(), serviceKey);
		return new IkasoaClientService<T, R>(this, thriftClient, serviceKey, protocolHandler, clientInvocationHandler);
	}

}
