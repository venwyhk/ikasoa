package com.ikasoa.core.thrift.server.impl;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;

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
public class ServletThriftServerImpl implements ThriftServer {

	private static final Logger LOG = LoggerFactory.getLogger(ServletThriftServerImpl.class);

	/**
	 * Thrift服务名称
	 */
	private String serverName;

	private ThriftServerConfiguration configuration;

	private TProcessor processor;

	public ServletThriftServerImpl() {
		// Do nothing
	}

	public ServletThriftServerImpl(String serverName, ThriftServerConfiguration configuration, TProcessor processor) {
		setServerName(serverName);
		setThriftServerConfiguration(configuration);
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
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public int getServerPort() {
		LOG.warn("This function not run .");
		return 0;
	}

	@Override
	public ThriftServerConfiguration getThriftServerConfiguration() {
		if (configuration == null) {
			throw new RuntimeException("Thrift server initialize failed ! Configuration is null !");
		}
		return configuration;
	}

	public void setThriftServerConfiguration(ThriftServerConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public TProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(TProcessor processor) {
		this.processor = processor;
	}

}
