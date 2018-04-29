package com.ikasoa.core.thrift.client.impl;

import java.util.Optional;

import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.ServerCheck;
import com.ikasoa.core.ServerCheckFailProcessor;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.client.pool.SocketPool;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;

/**
 * Thrift客户端实现抽象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public abstract class AbstractThriftClientImpl implements ThriftClient {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractThriftClientImpl.class);

	protected ThreadLocal<ThriftSocket> socketThread = new ThreadLocal<>();

	/**
	 * 需连接的Thrift服务地址
	 */
	private String serverHost;

	/**
	 * 需连接的Thrift服务端口
	 */
	private int serverPort;

	/**
	 * 连接的Thrift服务所需的配置信息
	 */
	private ThriftClientConfiguration configuration = new ThriftClientConfiguration();

	/**
	 * 服务器可用性检测
	 */
	private ServerCheck serverCheck;

	/**
	 * 连接失败异常处理器
	 */
	private ServerCheckFailProcessor serverCheckFailProcessor;

	@Override
	public TTransport getTransport() throws IkasoaException {
		if (getServerCheck() != null && !getServerCheck().check(getServerHost(), getServerPort()))
			// 如果服务器检测不可用,需要做相应的处理.默认为抛异常.
			getServerCheckFailProcessor().process(this);
		return getTransport(getServerHost(), getServerPort());
	}

	protected TTransport getTransport(String serverHost, int serverPort) throws IkasoaException {
		SocketPool pool = getThriftClientConfiguration().getSocketPool();
		socketThread.set(pool.buildThriftSocket(serverHost, serverPort));
		return getThriftClientConfiguration().getTransportFactory().getTransport(socketThread.get());
	}

	@Override
	public TProtocol getProtocol(TTransport transport) {
		if (transport == null)
			throw new RuntimeException("'transport' is null !");
		return getThriftClientConfiguration().getProtocolFactory().getProtocol(transport);
	}

	@Override
	public TProtocol getProtocol(TTransport transport, String serviceName) {
		return Optional.ofNullable(serviceName)
				.map(sName -> (TProtocol) new TMultiplexedProtocol(getProtocol(transport), sName))
				.orElseGet(() -> getProtocol(transport));
	}

	@Override
	public String getServerHost() {
		if (StringUtil.isEmpty(serverHost))
			throw new RuntimeException("'serverHost' is null !");
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	@Override
	public int getServerPort() {
		if (!ServerUtil.isSocketPort(serverPort))
			throw new RuntimeException("'serverPort' range must is 1025 ~ 65535 . Your port is : " + serverPort + " .");
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public ThriftClientConfiguration getThriftClientConfiguration() {
		if (configuration == null)
			throw new RuntimeException("Get thrift protocol failed ! Configuration is null !");
		return configuration;
	}

	public void setThriftClientConfiguration(ThriftClientConfiguration configuration) {
		this.configuration = configuration;
	}

	protected ServerCheck getServerCheck() {
		return getServerCheck(null);
	}

	protected ServerCheck getServerCheck(ServerCheck defaultServerCheck) {
		if (serverCheck == null)
			if (getThriftClientConfiguration().getServerCheck() == null)
				serverCheck = defaultServerCheck;
			else
				serverCheck = getThriftClientConfiguration().getServerCheck();
		return serverCheck;
	}

	protected ServerCheckFailProcessor getServerCheckFailProcessor() {
		return getServerCheckFailProcessor(new ExceProcessImpl());
	}

	protected ServerCheckFailProcessor getServerCheckFailProcessor(ServerCheckFailProcessor defaultProcessor) {
		if (serverCheckFailProcessor == null)
			if (getThriftClientConfiguration().getServerCheckFailProcessor() == null) {
				if (defaultProcessor == null)
					LOG.warn("'defaultProcessor' is null !");
				serverCheckFailProcessor = defaultProcessor;
			} else
				serverCheckFailProcessor = getThriftClientConfiguration().getServerCheckFailProcessor();
		return serverCheckFailProcessor;
	}

	@Override
	public void close() {
		// 仅回收连接,并没有断开
		getThriftClientConfiguration().getSocketPool().releaseThriftSocket(socketThread.get(), getServerHost(),
				getServerPort());
	}

	/**
	 * 服务器连接失败异常处理
	 * 
	 * @version 0.2
	 */
	protected class ExceProcessImpl implements ServerCheckFailProcessor {

		@Override
		public void process(ThriftClient client) throws IkasoaException {
			throw new IkasoaException("Server is not available (serverHost : " + client.getServerHost() + ", serverPort : "
					+ client.getServerPort() + ") !");
		}

	}

}
