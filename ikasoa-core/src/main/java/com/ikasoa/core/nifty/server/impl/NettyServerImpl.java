package com.ikasoa.core.nifty.server.impl;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.nifty.NiftyDispatcher;
import com.ikasoa.core.nifty.handler.impl.ThriftFrameCodeHandlerImpl;
import com.ikasoa.core.nifty.server.NettyServer;
import com.ikasoa.core.nifty.server.NiftyServerConfiguration;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.thrift.protocol.TProtocolFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
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
 * Netty服务器实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
@Slf4j
public class NettyServerImpl implements NettyServer, ExternalResourceReleasable {

	private static final int DEFAULT_WORKER_THREAD_COUNT = 2;

	private final int requestedPort;
	private int actualPort;
	private final ChannelPipelineFactory pipelineFactory;
	private ServerBootstrap bootstrap;
	private final ChannelGroup allChannels;
	private ExecutorService bossExecutorService = Executors.newSingleThreadExecutor();
	private ExecutorService workerExecutorService = Executors.newFixedThreadPool(DEFAULT_WORKER_THREAD_COUNT);
	private ServerChannelFactory channelFactory;
	private Channel serverChannel;
	private final NiftyServerConfiguration serverConfiguration;

	public NettyServerImpl(final NiftyServerConfiguration serverConfiguration) {
		this(serverConfiguration, new DefaultChannelGroup());
	}

	public NettyServerImpl(final NiftyServerConfiguration serverConfiguration, final ChannelGroup allChannels) {
		this.serverConfiguration = serverConfiguration;
		this.requestedPort = serverConfiguration.getServerPort();
		this.allChannels = allChannels;
		this.pipelineFactory = new NiftyChannelPipelineFactory(serverConfiguration);
	}

	@Override
	public void run() {
		channelFactory = new NioServerSocketChannelFactory(
				new NioServerBossPool(bossExecutorService, 1, ThreadNameDeterminer.CURRENT),
				new NioWorkerPool(workerExecutorService, DEFAULT_WORKER_THREAD_COUNT, ThreadNameDeterminer.CURRENT));
		try {
			start(channelFactory);
		} catch (IkasoaException e) {
			throw new RuntimeException(e);
		}
	}

	public void start(ServerChannelFactory serverChannelFactory) throws IkasoaException {
		bootstrap = new ServerBootstrap(serverChannelFactory);
		bootstrap.setOptions(new HashMap<String, Object>());
		bootstrap.setPipelineFactory(pipelineFactory);
		serverChannel = bootstrap.bind(new InetSocketAddress(requestedPort));
		InetSocketAddress actualSocket = (InetSocketAddress) serverChannel.getLocalAddress();
		actualPort = actualSocket.getPort();
		if (actualPort == 0 || (actualPort != requestedPort && requestedPort != 0))
			throw new IkasoaException("Server initialize failed !");
		log.info("started transport {}:{} .", serverConfiguration.getName(), actualPort);
	}

	@Override
	@SneakyThrows
	public void stop() {
		if (serverChannel != null) {
			log.info("stopping transport {}:{} .", serverConfiguration.getName(), actualPort);
			// first stop accepting
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

	public Channel getServerChannel() {
		return serverChannel;
	}

	public int getPort() {
		return actualPort != 0 ? actualPort : requestedPort;
	}

	@Override
	public void releaseExternalResources() {
		if (bootstrap != null)
			bootstrap.releaseExternalResources();
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

	@AllArgsConstructor
	private class NiftyChannelPipelineFactory implements ChannelPipelineFactory {

		private NiftyServerConfiguration configuration;

		@Override
		public ChannelPipeline getPipeline() throws Exception {
			ChannelPipeline cp = Channels.pipeline();
			TProtocolFactory inputProtocolFactory = configuration.getProtocolFactory();
			cp.addLast("frameCodec",
					new ThriftFrameCodeHandlerImpl(configuration.getMaxFrameSize(), inputProtocolFactory));
			cp.addLast("dispatcher", new NiftyDispatcher(configuration, new HashedWheelTimer()));
			return cp;
		}

	}

}
