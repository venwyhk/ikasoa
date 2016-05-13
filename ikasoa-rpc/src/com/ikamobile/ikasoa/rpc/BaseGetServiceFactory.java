package com.ikamobile.ikasoa.rpc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.thrift.GeneralFactory;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikamobile.ikasoa.rpc.client.IkasoaClientService;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandlerFactory;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandlerFactory.ProtocolType;
import com.ikamobile.ikasoa.rpc.handler.ClientInvocationHandler;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandler;
import com.ikamobile.ikasoa.rpc.handler.ReturnData;

/**
 * 基础服务工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class BaseGetServiceFactory<T1, T2> extends GeneralFactory {

	private static final Logger LOG = LoggerFactory.getLogger(BaseGetServiceFactory.class);

	private ProtocolType protocolType;

	private ProtocolHandlerFactory<T1, T2> protocolHandlerFactory = new ProtocolHandlerFactory<T1, T2>();

	private ClientInvocationHandler clientInvocationHandler;

	public BaseGetServiceFactory() {
	}

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

	public BaseGetService<T1, T2> getBaseGetService(String serverHost, int serverPort, String serviceKey,
			ReturnData resultData) {
		return getBaseGetService(serverHost, serverPort, serviceKey,
				protocolHandlerFactory.getProtocolHandler(resultData, getProtocolType()));
	}

	public BaseGetService<T1, T2> getBaseGetService(String serverHost, int serverPort, String serviceKey,
			ProtocolHandler<T1, T2> protocolHandler) {
		return getBaseGetService(getThriftClient(serverHost, serverPort), serviceKey, protocolHandler);
	}

	public BaseGetService<T1, T2> getBaseGetService(ThriftClient thriftClient, String serviceKey,
			ReturnData resultData) {
		return getBaseGetService(thriftClient, serviceKey,
				protocolHandlerFactory.getProtocolHandler(resultData, getProtocolType()));
	}

	public BaseGetService<T1, T2> getBaseGetService(List<ServerInfo> serverInfoList, String serviceKey,
			ReturnData resultData) {
		return getBaseGetService(serverInfoList, serviceKey,
				protocolHandlerFactory.getProtocolHandler(resultData, getProtocolType()));
	}

	public BaseGetService<T1, T2> getBaseGetService(List<ServerInfo> serverInfoList, String serviceKey,
			ProtocolHandler<T1, T2> protocolHandler) {
		return getBaseGetService(getThriftClient(serverInfoList), serviceKey, protocolHandler);
	}

	public BaseGetService<T1, T2> getBaseGetService(ThriftClient thriftClient, String serviceKey,
			ProtocolHandler<T1, T2> protocolHandler) {
		if (thriftClient == null) {
			LOG.error("thriftClient is null !");
			return null;
		}
		LOG.debug("Create new instance 'IkasoaClientService' . (serverHost : " + thriftClient.getServerHost()
				+ ", serverPort : " + thriftClient.getServerPort() + ", serviceKey : " + serviceKey + ")");
		return new IkasoaClientService<T1, T2>(this, thriftClient, serviceKey, protocolHandler,
				clientInvocationHandler);
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
