package com.ikasoa.core.thrift.server.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikasoa.core.STException;
import com.ikasoa.core.thrift.server.ServerAspect;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.utils.ServerUtil;

/**
 * Thrift服务实现抽象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public abstract class AbstractThriftServerImpl implements ThriftServer {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractThriftServerImpl.class);

	private ExecutorService executorService;

	private TServerSocket serverSocket;

	protected TServer server;

	/**
	 * Thrift服务器名称
	 */
	private String serverName;

	/**
	 * Thrift服务器端口
	 * <p>
	 * 如同时启动多个服务,请避免端口重复.
	 */
	private int serverPort;

	private ThriftServerConfiguration configuration;

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
				} catch (STException e) {
					throw new RuntimeException(e);
				}
			});
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					AbstractThriftServerImpl.this.stop();
				}
			});
			afterStart(getThriftServerConfiguration().getServerAspect());
		}
	}

	/**
	 * 启动Thrift服务
	 * 
	 * @exception STException
	 *                异常
	 */
	public void start() throws STException {
		if (server == null) {
			LOG.debug("Server configuration : " + configuration);
			// 不允许使用1024以内的端口.
			if (!ServerUtil.isSocketPort(serverPort))
				throw new STException("Server initialize failed ! Port range must is 1025 ~ 65535 . Your port is : "
						+ serverPort + " .");
			try {
				initServer(getTransport());
			} catch (TTransportException e) {
				throw new STException("Server initialize failed !", e);
			}
		}
		// 如果服务没有启动,则自动启动服务.
		if (server != null) {
			if (server.isServing()) {
				LOG.info("Server already run .");
				return;
			}
			server.serve();
			LOG.info("Startup server ...... (name : " + serverName + " , port : " + serverPort + ")");
		} else {
			LOG.warn("Startup server failed !");
		}
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
					while (executorService != null && !executorService.isTerminated()) {
						executorService.awaitTermination(10, TimeUnit.SECONDS);
					}
				} catch (InterruptedException e) {
					LOG.debug("Server thread shutdown exception !", e);
					executorService.shutdownNow();
					Thread.currentThread().interrupt();
				}
			}
			server = null;
			serverSocket = null;
			executorService = null;
			LOG.info("Stop server ...... (name: " + serverName + ")");
			afterStop(getThriftServerConfiguration().getServerAspect());
		} else {
			LOG.info("Server not run. (name: " + serverName + ")");
		}
	}

	private void beforeStart(ServerAspect serverAspect) {
		if (serverAspect != null)
			serverAspect.beforeStart(serverName, serverPort, configuration, processor, this);
	}

	private void afterStart(ServerAspect serverAspect) {
		if (serverAspect != null)
			serverAspect.afterStart(serverName, serverPort, configuration, processor, this);
	}

	private void beforeStop(ServerAspect serverAspect) {
		if (serverAspect != null)
			serverAspect.beforeStop(serverName, serverPort, configuration, processor, this);
	}

	private void afterStop(ServerAspect serverAspect) {
		if (serverAspect != null)
			serverAspect.afterStop(serverName, serverPort, configuration, processor, this);
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
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public boolean isServing() {
		if (server == null)
			return Boolean.FALSE;
		return server.isServing();
	}

	@Override
	public ThriftServerConfiguration getThriftServerConfiguration() {
		if (configuration == null)
			throw new RuntimeException("Server initialize failed ! Configuration is null !");
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
