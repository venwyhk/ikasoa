package com.ikasoa.core.thrift.server.impl;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.server.ServerAspect;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
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
		if (serverSocket == null)
			serverSocket = new TServerSocket(getServerPort());
		return serverSocket;
	}

	/**
	 * 运行一个Thrift服务线程
	 */
	@Override
	public void run() {
		if (executorService == null)
			executorService = Executors.newSingleThreadExecutor();
		if (!isServing()) {
			beforeStart(getThriftServerConfiguration().getServerAspect());
			executorService.execute(() -> {
				try {
					start();
				} catch (IkasoaException e) {
					throw new RuntimeException(e);
				}
			});
			Runtime.getRuntime().addShutdownHook(new Thread(() -> stop()));
			afterStart(getThriftServerConfiguration().getServerAspect());
		}
	}

	/**
	 * 启动Thrift服务
	 * 
	 * @exception IkasoaException
	 *                异常
	 */
	public void start() throws IkasoaException {
		if (server == null) {
			log.debug("Server configuration : {}", configuration);
			// 不允许使用1024以内的端口.
			if (!ServerUtil.isSocketPort(serverPort))
				throw new IkasoaException("Server initialize failed ! Port range must is 1025 ~ 65535 . Your port is : "
						+ serverPort + " .");
			try {
				initServer(getTransport());
			} catch (TTransportException e) {
				throw new IkasoaException("Server initialize failed !", e);
			}
		}
		// 如果服务没有启动,则自动启动服务.
		if (server != null) {
			if (server.isServing()) {
				log.info("Server already run .");
				return;
			}
			server.serve();
			log.info("Starting server ... (name : {} , port : {})", serverName, serverPort);
		} else
			log.warn("Startup server failed !");
	}

	/**
	 * 停止Thrift服务
	 */
	@Override
	public void stop() {
		if (server != null && server.isServing()) {
			beforeStop(getThriftServerConfiguration().getServerAspect());
			server.stop();
			if (executorService != null && !executorService.isShutdown()) {
				executorService.shutdown();
				try {
					while (executorService != null && !executorService.isTerminated())
						executorService.awaitTermination(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					log.debug("Server thread shutdown exception !");
					executorService.shutdownNow();
					Thread.currentThread().interrupt();
				}
			}
			server = null;
			serverSocket = null;
			executorService = null;
			log.info("Stoping server ... (name: {})", serverName);
			afterStop(getThriftServerConfiguration().getServerAspect());
		} else
			log.debug("Server not run . (name: {})", serverName);
	}

	private void beforeStart(ServerAspect serverAspect) {
		Optional.ofNullable(serverAspect)
				.ifPresent(sAspect -> sAspect.beforeStart(serverName, serverPort, configuration, processor, this));
	}

	private void afterStart(ServerAspect serverAspect) {
		Optional.ofNullable(serverAspect)
				.ifPresent(sAspect -> sAspect.afterStart(serverName, serverPort, configuration, processor, this));
	}

	private void beforeStop(ServerAspect serverAspect) {
		Optional.ofNullable(serverAspect)
				.ifPresent(sAspect -> sAspect.beforeStop(serverName, serverPort, configuration, processor, this));
	}

	private void afterStop(ServerAspect serverAspect) {
		Optional.ofNullable(serverAspect)
				.ifPresent(sAspect -> sAspect.afterStop(serverName, serverPort, configuration, processor, this));
	}

	@Override
	public boolean isServing() {
		return server == null ? Boolean.FALSE : server.isServing();
	}

	@Override
	public ThriftServerConfiguration getThriftServerConfiguration() {
		if (configuration == null)
			throw new RuntimeException("'configuration' is null !");
		return configuration;
	}

}
