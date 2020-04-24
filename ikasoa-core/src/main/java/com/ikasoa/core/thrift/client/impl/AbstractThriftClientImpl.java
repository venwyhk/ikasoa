package com.ikasoa.core.thrift.client.impl;

import java.util.Optional;

import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.ServerCheck;
import com.ikasoa.core.ServerCheckFailProcessor;
import com.ikasoa.core.ServerInfo;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.client.pool.ClientSocketPoolParameters;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;
import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.core.utils.ServerUtil;

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

	protected final ThreadLocal<ThriftSocket> socketThread = new ThreadLocal<>();

	/**
	 * 需连接的Thrift服务信息
	 */
	@Setter
	private ServerInfo serverInfo;

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
		if (ObjectUtil.isNotNull(getServerCheck()) && !getServerCheck().check(getServerInfo()))
			// 如果服务器检测不可用,需要做相应的处理.默认为抛异常.
			getServerCheckFailProcessor().process(this);
		return getTransport(getServerInfo());
	}

	protected TTransport getTransport(ServerInfo serverInfo) throws IkasoaException {
		socketThread.set(getThriftClientConfiguration().getSocketPool()
				.buildThriftSocket(new ClientSocketPoolParameters(serverInfo.getHost(), serverInfo.getPort(), 0,
						getThriftClientConfiguration().getSslTransportParameters())));
		return getThriftClientConfiguration().getTransportFactory().getTransport(socketThread.get());
	}

	@Override
	public TProtocol getProtocol(TTransport transport) {
		if (ObjectUtil.isNull(transport))
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
	public ServerInfo getServerInfo() {
		if (ObjectUtil.isNull(serverInfo))
			throw new RuntimeException("'serverInfo' is null !");
		if (!ServerUtil.isSocketPort(serverInfo.getPort()))
			throw new RuntimeException(
					String.format("Server initialize failed ! Port range must is 1025 ~ 65535 . Your port is : %d .",
							serverInfo.getPort()));
		return serverInfo;
	}

	@Override
	public ThriftClientConfiguration getThriftClientConfiguration() {
		if (ObjectUtil.isNull(configuration))
			throw new RuntimeException("Get thrift protocol failed ! Configuration is null !");
		return configuration;
	}

	protected ServerCheck getServerCheck() {
		return getServerCheck(null);
	}

	protected ServerCheck getServerCheck(ServerCheck defaultServerCheck) {
		if (ObjectUtil.isNull(defaultServerCheck))
			log.debug("'defaultServerCheck' is null !");
		if (ObjectUtil.isNull(serverCheck))
			serverCheck = ObjectUtil.isNull(getThriftClientConfiguration().getServerCheck()) ? defaultServerCheck
					: getThriftClientConfiguration().getServerCheck();
		return serverCheck;
	}

	protected ServerCheckFailProcessor getServerCheckFailProcessor() {
		return getServerCheckFailProcessor(new ExceProcessImpl());
	}

	protected ServerCheckFailProcessor getServerCheckFailProcessor(ServerCheckFailProcessor defaultProcessor) {
		if (ObjectUtil.isNull(defaultProcessor))
			log.debug("'defaultProcessor' is null !");
		if (ObjectUtil.isNull(serverCheckFailProcessor))
			serverCheckFailProcessor = ObjectUtil.isNull(getThriftClientConfiguration().getServerCheckFailProcessor())
					? defaultProcessor : getThriftClientConfiguration().getServerCheckFailProcessor();
		return serverCheckFailProcessor;
	}

	@Override
	public void close() {
		// 仅回收连接,并没有断开
		getThriftClientConfiguration().getSocketPool().releaseThriftSocket(socketThread.get(), serverInfo.getHost(),
				serverInfo.getPort());
	}

	/**
	 * 服务器连接失败异常处理
	 * 
	 * @version 0.2
	 */
	protected class ExceProcessImpl implements ServerCheckFailProcessor {

		@Override
		public void process(ThriftClient client) throws IkasoaException {
			throw new IkasoaException(
					String.format("Server is not available (%s) !", client.getServerInfo().toString()));
		}

	}

}
