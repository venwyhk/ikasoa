package com.ikamobile.ikasoa;

import java.util.List;

import org.sulei.core.thrift.GeneralFactory;
import org.sulei.core.thrift.client.ThriftClientConfiguration;
import org.sulei.core.thrift.server.ThriftServerConfiguration;

import com.ikamobile.ikasoa.client.IkasoaClientService;
import com.ikamobile.ikasoa.handler.ProtocolHandlerFactory;
import com.ikamobile.ikasoa.handler.ProtocolHandlerFactory.ProtocolType;
import com.ikamobile.ikasoa.handler.ProtocolHandler;
import com.ikamobile.ikasoa.handler.ReturnData;

/**
 * 基础服务工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class BaseGetServiceFactory<T1, T2> extends GeneralFactory {

	private ProtocolType protocolType;

	private ProtocolHandlerFactory<T1, T2> protocolHandlerFactory = new ProtocolHandlerFactory<T1, T2>();

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

	public BaseGetService<T1, T2> getBaseGetService(String serverHost, int serverPort, ReturnData resultData) {
		return getBaseGetService(serverHost, serverPort, null, resultData);
	}

	public BaseGetService<T1, T2> getBaseGetService(String serverHost, int serverPort, String serviceName,
			ReturnData resultData) {
		return new IkasoaClientService<T1, T2>(this, getThriftClient(serverHost, serverPort), serviceName,
				protocolHandlerFactory.getProtocolHandler(null, resultData, getProtocolType()));
	}

	public BaseGetService<T1, T2> getBaseGetService(String serverHost, int serverPort,
			ProtocolHandler<T1, T2> protocolHandler) {
		return getBaseGetService(serverHost, serverPort, null, protocolHandler);
	}

	public BaseGetService<T1, T2> getBaseGetService(String serverHost, int serverPort, String serviceName,
			ProtocolHandler<T1, T2> protocolHandler) {
		return new IkasoaClientService<T1, T2>(this, getThriftClient(serverHost, serverPort), serviceName,
				protocolHandler);
	}

	public BaseGetService<T1, T2> getBaseGetService(List<String> serverHostList, int serverPort,
			ReturnData resultData) {
		return new IkasoaClientService<T1, T2>(this, getThriftClient(serverHostList, serverPort), null,
				protocolHandlerFactory.getProtocolHandler(null, resultData, getProtocolType()));
	}

	public BaseGetService<T1, T2> getBaseGetService(List<String> serverHostList, int serverPort, String serviceName,
			ReturnData resultData) {
		return new IkasoaClientService<T1, T2>(this, getThriftClient(serverHostList, serverPort), serviceName,
				protocolHandlerFactory.getProtocolHandler(null, resultData, getProtocolType()));
	}

	public BaseGetService<T1, T2> getBaseGetService(List<String> serverHostList, int serverPort,
			ProtocolHandler<T1, T2> protocolHandler) {
		return new IkasoaClientService<T1, T2>(this, getThriftClient(serverHostList, serverPort), null,
				protocolHandler);
	}

	public BaseGetService<T1, T2> getBaseGetService(List<String> serverHostList, int serverPort, String serviceName,
			ProtocolHandler<T1, T2> protocolHandler) {
		return new IkasoaClientService<T1, T2>(this, getThriftClient(serverHostList, serverPort), serviceName,
				protocolHandler);
	}

	public ProtocolType getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

}
