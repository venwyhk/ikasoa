package com.ikasoa.rpc;

import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.thrift.server.impl.ServletThriftServerImpl;
import com.ikasoa.core.thrift.service.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import org.apache.thrift.TProcessor;

/**
 * Servlet服务工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.2
 */
@Slf4j
public class ServletServerIkasoaFactory extends DefaultIkasoaFactory {

	private static final String SERVER_NAME_PREFIX = "ServletServer-";

	public ServletServerIkasoaFactory(Configurator configurator) {
		super(configurator);
	}

	public ServletServerIkasoaFactory(ThriftServerConfiguration thriftServerConfiguration) {
		super.thriftServerConfiguration = thriftServerConfiguration;
	}

	public <T> T getInstance(Class<T> iClass, ServerInfoWrapper siw) {
		log.error("Can't get the instance !");
		return null;
	}

	@Override
	public ThriftServer getThriftServer(int serverPort, Service service) {
		return getThriftServer(SERVER_NAME_PREFIX + serverPort, serverPort, service);
	}

	@Override
	public ThriftServer getThriftServer(int serverPort, Map<String, Service> serviceMap) {
		return getThriftServer(SERVER_NAME_PREFIX + serverPort, serverPort, serviceMap);
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, TProcessor processor) {
		return new ServletThriftServerImpl(serverName, thriftServerConfiguration, processor);
	}

	@Override
	public ThriftServer getNonblockingThriftServer(String serverName, int serverPort, TProcessor processor) {
		log.error("Can't get the server !");
		return null;
	}

}
