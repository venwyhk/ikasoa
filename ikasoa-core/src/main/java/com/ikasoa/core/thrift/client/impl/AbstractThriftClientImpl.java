package com.ikasoa.core.thrift.client.impl;

import java.util.Optional;

import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.ServerCheck;
import com.ikasoa.core.ServerCheckFailProcessor;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Thrift客户端实现抽象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Slf4j
public abstract class AbstractThriftClientImpl implements ThriftClient {

	protected ThreadLocal<ThriftSocket> socketThread = new ThreadLocal<>();

	/**
	 * 需连接的Thrift服务地址
	 */
	@Setter
	private String serverHost;

	/**
	 * 需连接的Thrift服务端口
	 */
	@Setter
	private int serverPort;

	/**
	 * 连接的Thrift服务所需的配置信息
	 */
	@Setter
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
		socketThread.set(getThriftClientConfiguration().getSocketPool().buildThriftSocket(serverHost, serverPort));
		return getThriftClientConfiguration().getTransportFactory().getTransport(socketThread.get());
	}

	@Override
	public TProtocol getProtocol(TTransport transport) {
		if (transport == null)
			throw new IllegalArgumentException("'transport' is null !");
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

	@Override
	public int getServerPort() {
		if (!ServerUtil.isSocketPort(serverPort))
			throw new RuntimeException(String.format(
					"Server initialize failed ! Port range must is 1025 ~ 65535 . Your port is : %d .", serverPort));
		return serverPort;
	}

	@Override
	public ThriftClientConfiguration getThriftClientConfiguration() {
		if (configuration == null)
			throw new RuntimeException("Get thrift protocol failed ! Configuration is null !");
		return configuration;
	}

	protected ServerCheck getServerCheck() {
		return getServerCheck(null);
	}

	protected ServerCheck getServerCheck(ServerCheck defaultServerCheck) {
		if (defaultServerCheck == null)
			log.debug("'defaultServerCheck' is null !");
		if (serverCheck == null)
			serverCheck = getThriftClientConfiguration().getServerCheck() == null ? defaultServerCheck
					: getThriftClientConfiguration().getServerCheck();
		return serverCheck;
	}

	protected ServerCheckFailProcessor getServerCheckFailProcessor() {
		return getServerCheckFailProcessor(new ExceProcessImpl());
	}

	protected ServerCheckFailProcessor getServerCheckFailProcessor(ServerCheckFailProcessor defaultProcessor) {
		if (defaultProcessor == null)
			log.debug("'defaultProcessor' is null !");
		if (serverCheckFailProcessor == null)
			serverCheckFailProcessor = getThriftClientConfiguration().getServerCheckFailProcessor() == null
					? defaultProcessor
					: getThriftClientConfiguration().getServerCheckFailProcessor();
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
			throw new IkasoaException(String.format("Server is not available (serverHost : %s, serverPort : %d) !",
					client.getServerHost(), client.getServerPort()));
		}

	}

}
