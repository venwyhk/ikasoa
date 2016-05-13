package com.ikamobile.ikasoa.core.thrift.server.impl;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerTransport;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;

/**
 * 默认Thrift服务器实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class DefaultThriftServerImpl extends AbstractThriftServerImpl {

	/**
	 * 最大线程数
	 */
	private final static int MAX_WORKER_THREADA = Integer.MAX_VALUE;

	public DefaultThriftServerImpl() {
	}

	public DefaultThriftServerImpl(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor) {
		setServerName(serverName);
		setServerPort(serverPort);
		if (configuration == null) {
			configuration = new ThriftServerConfiguration();
		}
		setThriftServerConfiguration(configuration);
		setProcessor(processor);
	}

	/**
	 * 初始化Thrift服务
	 * <p>
	 * 启动Thrift服务之前必须要进行初始化.
	 * 
	 * @param serverTransport
	 *            服务传输类型
	 */
	protected void initServer(TServerTransport serverTransport) {
		// 默认使用TThreadPoolServer方式启动Thrift服务器,对每个连接都会单独建立一个线程.
		TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
		ThriftServerConfiguration configuration = getThriftServerConfiguration();
		args.transportFactory(configuration.getTransportFactory());
		args.protocolFactory(configuration.getProtocolFactory());
		// 如果不设置ExecutorService,则默认使用ThreadPoolExecutor实现.
		if (configuration.getExecutorService() != null) {
			args.executorService(configuration.getExecutorService());
		}
		// 最大线程数,如果有自定义ExecutorService则该值无效.
		args.maxWorkerThreads(MAX_WORKER_THREADA);
		server = new TThreadPoolServer(args.processor(getProcessor()));
		if (configuration.getServerEventHandler() != null) {
			server.setServerEventHandler(configuration.getServerEventHandler());
		}
	}

}
