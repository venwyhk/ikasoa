package com.ikasoa.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.rpc.client.IkasoaClientService;
import com.ikasoa.rpc.handler.ProtocolHandlerFactory;
import com.ikasoa.rpc.handler.ClientInvocationHandler;
import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.handler.ReturnData;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础服务工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class BaseGetServiceFactory<T, R> extends GeneralFactory {

	private static final Logger LOG = LoggerFactory.getLogger(BaseGetServiceFactory.class);

	private ProtocolHandlerFactory<T, R> protocolHandlerFactory = new ProtocolHandlerFactory<>();

	@Getter
	@Setter
	@SuppressWarnings("rawtypes")
	private Class<ProtocolHandler> protocolHandlerClass;

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
				protocolHandlerFactory.getProtocolHandler(resultData, getProtocolHandlerClass()));
	}

	public BaseGetService<T, R> getBaseGetService(ThriftClient thriftClient, String serviceKey,
			ProtocolHandler<T, R> protocolHandler) {
		if (thriftClient == null) {
			LOG.error("'thriftClient' is null !");
			return null;
		}
		LOG.debug("Create new instance 'IkasoaClientService' . (serverHost : {}, serverPort : {}, serviceKey : {})",
				thriftClient.getServerHost(), thriftClient.getServerPort(), serviceKey);
		return new IkasoaClientService<T, R>(this, thriftClient, serviceKey, protocolHandler, clientInvocationHandler);
	}

}
