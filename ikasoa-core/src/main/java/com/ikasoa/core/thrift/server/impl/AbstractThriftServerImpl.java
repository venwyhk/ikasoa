package com.ikasoa.core.thrift.server.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.core.utils.ServerUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Thrift服务实现抽象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Slf4j
public abstract class AbstractThriftServerImpl implements ThriftServer {

	private ExecutorService executorService;

	private TServerSocket serverSocket;

	protected TServer server;

	/**
	 * Thrift服务器名称
	 */
	@Getter
	@Setter
	private String serverName;

	/**
	 * Thrift服务器端口
	 * <p>
	 * 如同时启动多个服务,请避免端口重复.
	 */
	@Getter
	@Setter
	private int serverPort;

	@Setter
	private ThriftServerConfiguration configuration;

	@Getter
	@Setter
	private TProcessor processor;

	protected abstract void initServer(TServerTransport serverTransport);

	/**
	 * 获取一个服务传输类型
	 * <p>
	 * 如果使用非Socket传输类型,需要重写此方法.
	 * 
	 * @return TServerTransport 服务传输类型
	 */
	@Override
	public TServerTransport getTransport() throws TTransportException {
		if (ObjectUtil.isNull(serverSocket)) {
			TSSLTransportParameters params = getServerConfiguration().getSslTransportParameters();
			serverSocket = ObjectUtil.isNull(params) ? new TServerSocket(getServerPort())
					: TSSLTransportFactory.getServerSocket(getServerPort(), 0, null, params);
		}
		return serverSocket;
	}

	/**
	 * 运行一个Thrift服务线程
	 */
	@Override
	public void run() {
		if (ObjectUtil.isNull(executorService))
			executorService = Executors.newSingleThreadExecutor();
		if (!isServing()) {
			beforeStart(getServerConfiguration().getServerAspect());
			executorService.execute(() -> {
				try {
					start();
				} catch (IkasoaException e) {
					throw new RuntimeException(e);
				}
			});
			Runtime.getRuntime().addShutdownHook(new Thread(() -> stop()));
			afterStart(getServerConfiguration().getServerAspect());
		}
	}

	/**
	 * 启动Thrift服务
	 * 
	 * @exception IkasoaException
	 *                异常
	 */
	public void start() throws IkasoaException {
		if (ObjectUtil.isNull(server)) {
			log.debug("Server configuration : {}", configuration);
			// 不允许使用1024以内的端口.
			if (!ServerUtil.isSocketPort(serverPort))
				throw new IkasoaException(String.format(
						"Server initialize failed ! Port range must is 1025 ~ 65535 . Your port is : %d .",
						serverPort));
			try {
				initServer(getTransport());
			} catch (TTransportException e) {
				throw new IkasoaException("Server initialize failed !", e);
			}
		}
		// 如果服务没有启动,则自动启动服务.
		if (ObjectUtil.isNotNull(server)) {
			if (server.isServing()) {
				log.info("Server already run .");
				return;
			}
			server.serve();
			log.info("Startup server ... (name : {} , port : {})", getServerName(), getServerPort());
		} else
			log.warn("Startup server failed !");
	}

	/**
	 * 停止Thrift服务
	 */
	@Override
	public void stop() {
		if (ObjectUtil.isNotNull(server) && server.isServing()) {
			beforeStop(getServerConfiguration().getServerAspect());
			log.info("stopping server ... (name: {})", getServerName());
			server.stop();
			shutdownExecutor(executorService);
			server = null;
			serverSocket = null;
			afterStop(getServerConfiguration().getServerAspect());
		} else
			log.debug("Server not run . (name: {})", getServerName());
	}

	@Override
	public boolean isServing() {
		return ObjectUtil.isNull(server) ? false : server.isServing();
	}

	@Override
	public ThriftServerConfiguration getServerConfiguration() {
		if (ObjectUtil.isNull(configuration))
			throw new IllegalArgumentException("'configuration' is null !");
		return configuration;
	}

}
