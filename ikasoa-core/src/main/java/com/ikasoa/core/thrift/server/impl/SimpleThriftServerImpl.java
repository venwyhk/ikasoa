package com.ikasoa.core.thrift.server.impl;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerTransport;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.utils.ObjectUtil;

import lombok.NoArgsConstructor;

/**
 * 单线程Thrift服务器实现
 * <p>
 * 此实现一般用于调试,正式生产环境请慎用.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
public class SimpleThriftServerImpl extends AbstractThriftServerImpl {

	public SimpleThriftServerImpl(final String serverName, final int serverPort,
			final ThriftServerConfiguration configuration, TProcessor processor) {
		setServerName(serverName);
		setServerPort(serverPort);
		setConfiguration(configuration);
		setProcessor(processor);
	}

	protected void initServer(TServerTransport serverTransport) {
		ThriftServerConfiguration configuration = getServerConfiguration();
		server = new TSimpleServer(configuration.getServerArgsAspect().tServerArgsAspect(
				new TServer.Args(serverTransport).transportFactory(configuration.getTransportFactory())
						.protocolFactory(configuration.getProtocolFactory()).processor(getProcessor())));
		if (ObjectUtil.isNotNull(configuration.getServerEventHandler()))
			server.setServerEventHandler(configuration.getServerEventHandler());
	}

}
