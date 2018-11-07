package com.ikasoa.core.netty.server.impl;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.netty.NettyDispatcher;
import com.ikasoa.core.netty.handler.impl.ThriftFrameCodeHandlerImpl;
import com.ikasoa.core.netty.server.NettyServer;
import com.ikasoa.core.netty.server.NettyServerConfiguration;
import com.ikasoa.core.thrift.server.ServerConfiguration;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ServerChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerBossPool;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioWorkerPool;
import org.jboss.netty.util.ExternalResourceReleasable;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.ThreadNameDeterminer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Netty服务器默认实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
@Slf4j
public class DefaultNettyServerImpl implements NettyServer, ExternalResourceReleasable {

	private ExecutorService bossExecutorService = Executors.newSingleThreadExecutor();

	private ExecutorService workerExecutorService;

	@Getter
	private int workerCount;

	@Getter
	@Setter
	private String serverName;

	private final int requestedPort;

	private int actualPort;

	@Getter
	private ServerBootstrap bootstrap;

	@Setter
	private NettyServerConfiguration configuration;

	@Getter
	@Setter
	private TProcessor processor;

	@Getter
	private final ChannelGroup allChannels;

	@Setter
	private ServerChannelFactory channelFactory;

	private Channel serverChannel;

	public DefaultNettyServerImpl(String serverName, int serverPort, NettyServerConfiguration configuration,
			TProcessor processor) {
		this(serverName, serverPort, configuration, processor, new DefaultChannelGroup());
	}

	public DefaultNettyServerImpl(String serverName, int serverPort, NettyServerConfiguration configuration,
			TProcessor processor, final ChannelGroup allChannels) {
		setProcessor(processor);
		setConfiguration(configuration == null ? new NettyServerConfiguration(new TProcessorFactory(getProcessor()))
				: configuration);
		setServerName(serverName);
		requestedPort = serverPort;
		workerExecutorService = this.configuration.getExecutorService() == null ? Executors.newFixedThreadPool(2)
				: this.configuration.getExecutorService();
		workerCount = this.configuration.getWorkerCount();
		this.allChannels = allChannels;
	}

	@Override
	public void run() {
		try {
			channelFactory = new NioServerSocketChannelFactory(
					new NioServerBossPool(bossExecutorService, 1, ThreadNameDeterminer.CURRENT),
					new NioWorkerPool(workerExecutorService, workerCount, ThreadNameDeterminer.CURRENT));
			bootstrap = new ServerBootstrap(channelFactory);
			bootstrap.setOptions(new HashMap<String, Object>());
			bootstrap.setPipelineFactory(() -> {
				ChannelPipeline cp = Channels.pipeline();
				cp.addLast("frameCodec", new ThriftFrameCodeHandlerImpl(configuration.getMaxFrameSize(),
						getServerConfiguration().getProtocolFactory()));
				cp.addLast("dispatcher", new NettyDispatcher(configuration, new HashedWheelTimer()));
				return cp;
			});
			serverChannel = bootstrap.bind(new InetSocketAddress(requestedPort));
			actualPort = ((InetSocketAddress) serverChannel.getLocalAddress()).getPort();
			if (actualPort == 0 || (actualPort != requestedPort && requestedPort != 0))
				throw new IkasoaException("Server initialize failed !");
			log.info("started transport {}:{} .", getServerName(), actualPort);
		} catch (IkasoaException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@SneakyThrows
	public void stop() {
		if (serverChannel != null) {
			log.info("stopping transport {}:{} .", getServerName(), actualPort);
			final CountDownLatch latch = new CountDownLatch(1);
			serverChannel.close().addListener(future -> latch.countDown());
			latch.await();
			serverChannel = null;
		}
		if (channelFactory != null) {
			channelFactory.releaseExternalResources();
			channelFactory.shutdown();
		}
		if (allChannels != null)
			allChannels.close();
		if (bossExecutorService != null)
			shutdownExecutor(bossExecutorService, "bossExecutorService");
		if (workerExecutorService != null)
			shutdownExecutor(workerExecutorService, "workerExecutorService");
	}

	private void shutdownExecutor(ExecutorService executorService, final String name) {
		try {
			log.debug("Waiting for {} to shutdown .", name);
			while (executorService != null && !executorService.isTerminated()) {
				executorService.awaitTermination(10, TimeUnit.SECONDS);
				log.debug("{} did not shutdown properly .", name);
			}
		} catch (InterruptedException e) {
			log.warn("Interrupted while waiting for {} to shutdown .", name);
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	public Channel getServerChannel() {
		return serverChannel;
	}

	@Override
	public int getServerPort() {
		return actualPort != 0 ? actualPort : requestedPort;
	}

	@Override
	public void releaseExternalResources() {
		if (bootstrap != null)
			bootstrap.releaseExternalResources();
	}

	@Override
	public boolean isServing() {
		return serverChannel != null && serverChannel.isOpen();
	}

	@Override
	public ServerConfiguration getServerConfiguration() {
		if (configuration == null)
			throw new RuntimeException("'configuration' is null !");
		return configuration;
	}

	@Override
	public TServerTransport getTransport() throws TTransportException {
		return null;
	}

}
