package com.ikasoa.core.thrift.server.impl;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Thrift服务器Servlet实现
 * <p>
 * 比较特殊的一种服务器实现,利用http协议来提供Thrift服务.一般用于构造<i>ThriftServlet</i>.
 * <p>
 * 生命周期与WebServer相同,服务端口也与http端口相同,所以<i>ThriftServer</i>的 <code>start()</code>,
 * <code>stop()</code>,<code>isServing()</code>, <code>getServerPort()</code>
 * 方法都没有具体的实现.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@NoArgsConstructor
@Slf4j
public class ServletThriftServerImpl implements ThriftServer {

	/**
	 * Thrift服务名称
	 */
	@Getter
	@Setter
	private String serverName;

	@Setter
	private ThriftServerConfiguration configuration;

	@Getter
	@Setter
	private TProcessor processor;

	public ServletThriftServerImpl(String serverName, ThriftServerConfiguration configuration, TProcessor processor) {
		setServerName(serverName);
		setConfiguration(configuration);
		setProcessor(processor);
	}

	@Override
	public TServerTransport getTransport() throws TTransportException {
		return null;
	}

	@Override
	public void run() {
		// Do nothing
	}

	@Override
	public void stop() {
		// Do nothing
	}

	@Override
	public boolean isServing() {
		return Boolean.TRUE;
	}

	@Override
	public int getServerPort() {
		log.debug("This function not run .");
		return 0;
	}

	@Override
	public ThriftServerConfiguration getServerConfiguration() {
		if (configuration == null)
			throw new RuntimeException("'configuration' is null !");
		return configuration;
	}

}
