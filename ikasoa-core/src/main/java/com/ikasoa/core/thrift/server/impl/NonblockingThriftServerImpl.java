package com.ikasoa.core.thrift.server.impl;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.utils.ObjectUtil;

import lombok.NoArgsConstructor;

/**
 * 非阻塞Thrift服务器实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@NoArgsConstructor
public class NonblockingThriftServerImpl extends AbstractThriftServerImpl {

	private TNonblockingServerSocket serverSocket;

	public NonblockingThriftServerImpl(final String serverName, final int serverPort,
			final ThriftServerConfiguration configuration, TProcessor processor) {
		setServerName(serverName);
		setServerPort(serverPort);
		configuration.setTransportFactory(new TFramedTransport.Factory()); // 如果提供非阻塞服务,则必须为TFramedTransport.Factory().
		setConfiguration(configuration);
		setProcessor(processor);
	}

	@Override
	public TServerTransport getTransport() throws TTransportException {
		if (ObjectUtil.isNull(serverSocket))
			serverSocket = new TNonblockingServerSocket(getServerPort());
		return serverSocket;
	}

	/**
	 * 初始化Thrift服务
	 * 
	 * @param serverTransport
	 *            服务传输类型
	 */
	@Override
	protected void initServer(TServerTransport serverTransport) {
		ThriftServerConfiguration configuration = getServerConfiguration();
		// 使用多线程半同步半异步方式
		TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args((TNonblockingServerSocket) serverTransport)
				.transportFactory(configuration.getTransportFactory())
				.protocolFactory(configuration.getProtocolFactory());
		if (ObjectUtil.isNotNull(configuration.getExecutorService()))
			args.executorService(configuration.getExecutorService());
		server = new TThreadedSelectorServer(
				configuration.getServerArgsAspect().tThreadedSelectorServerArgsAspect(args).processor(getProcessor()));
		if (ObjectUtil.isNotNull(configuration.getServerEventHandler()))
			server.setServerEventHandler(configuration.getServerEventHandler());
	}

}
