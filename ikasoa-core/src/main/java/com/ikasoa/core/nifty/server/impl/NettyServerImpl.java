package com.ikasoa.core.nifty.server.impl;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.nifty.ChannelStatistics;
import com.ikasoa.core.nifty.ConnectionContextHandler;
import com.ikasoa.core.nifty.IdleDisconnectHandler;
import com.ikasoa.core.nifty.NiftyDispatcher;
import com.ikasoa.core.nifty.NiftyExceptionLogger;
import com.ikasoa.core.nifty.NiftyIODispatcher;
import com.ikasoa.core.nifty.NiftyMetrics;
import com.ikasoa.core.nifty.NiftySecurityHandlers;
import com.ikasoa.core.nifty.handler.impl.ThriftFrameCodeHandlerImpl;
import com.ikasoa.core.nifty.server.NettyServer;
import com.ikasoa.core.nifty.server.NettyServerConfiguration;
import com.ikasoa.core.nifty.server.NiftyServerConfiguration;
import com.ikasoa.core.nifty.ssl.SslPlaintextHandler;
import com.ikasoa.core.nifty.ssl.SslServerConfiguration;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.thrift.protocol.TProtocolFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ServerChannelFactory;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerBossPool;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioWorkerPool;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.ExternalResourceReleasable;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.ThreadNameDeterminer;

import javax.inject.Inject;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A core channel the decode framed Thrift message, dispatches to the TProcessor
 * given and then encode message back to Thrift frame.
 */
@Slf4j
public class NettyServerImpl implements NettyServer, ExternalResourceReleasable {

	private static final int DEFAULT_WORKER_THREAD_COUNT = 2;
	private static final int NO_WRITER_IDLE_TIMEOUT = 0;
	private static final int NO_ALL_IDLE_TIMEOUT = 0;

	private final int requestedPort;
	private int actualPort;
	private final ChannelPipelineFactory pipelineFactory;
	private ServerBootstrap bootstrap;
	private final ChannelGroup allChannels;
	private ExecutorService bossExecutor;
	private ExecutorService ioWorkerExecutor;
	private ServerChannelFactory channelFactory;
	private Channel serverChannel;
	private final NiftyServerConfiguration server;
	private final NettyServerConfiguration nettyServerConfig;
	private final ChannelStatistics channelStatistics;

	private AtomicReference<SslServerConfiguration> sslConfiguration = new AtomicReference<>();

	public NettyServerImpl(final NiftyServerConfiguration server) {
		this(server,
				new NettyServerConfiguration(new HashMap<String, Object>(), new HashedWheelTimer(),
						Executors.newSingleThreadExecutor(), 1,
						Executors.newFixedThreadPool(DEFAULT_WORKER_THREAD_COUNT), DEFAULT_WORKER_THREAD_COUNT),
				new DefaultChannelGroup());
	}

	@Inject
	public NettyServerImpl(final NiftyServerConfiguration server, final NettyServerConfiguration nettyServerConfig,
			final ChannelGroup allChannels) {
		this.server = server;
		this.nettyServerConfig = nettyServerConfig;
		this.requestedPort = server.getServerPort();
		this.allChannels = allChannels;
		// connectionLimiter must be instantiated exactly once (and thus outside the
		// pipeline factory)
		final ConnectionLimiter connectionLimiter = new ConnectionLimiter(server.getMaxConnections());

		this.channelStatistics = new ChannelStatistics(allChannels);

		this.sslConfiguration.set(this.server.getSslConfiguration());

		this.pipelineFactory = new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline cp = Channels.pipeline();
				TProtocolFactory inputProtocolFactory = server.getProtocolFactory();
				NiftySecurityHandlers securityHandlers = server.getSecurityFactory().getSecurityHandlers(server,
						nettyServerConfig);
				cp.addLast("connectionContext", new ConnectionContextHandler());
				cp.addLast("connectionLimiter", connectionLimiter);
				cp.addLast(ChannelStatistics.NAME, channelStatistics);
				cp.addLast("encryptionHandler", securityHandlers.getEncryptionHandler());
				cp.addLast("ioDispatcher", new NiftyIODispatcher());
				cp.addLast("frameCodec",
						new ThriftFrameCodeHandlerImpl(server.getMaxFrameSize(), inputProtocolFactory));
				if (server.getClientIdleTimeout() > 0) {
					// Add handlers to detect idle client connections and disconnect them
					cp.addLast("idleTimeoutHandler",
							new IdleStateHandler(nettyServerConfig.getTimer(), server.getClientIdleTimeout(),
									NO_WRITER_IDLE_TIMEOUT, NO_ALL_IDLE_TIMEOUT, TimeUnit.MILLISECONDS));
					cp.addLast("idleDisconnectHandler", new IdleDisconnectHandler());
				}

				cp.addLast("authHandler", securityHandlers.getAuthenticationHandler());
				cp.addLast("dispatcher", new NiftyDispatcher(server, nettyServerConfig.getTimer()));
				cp.addLast("exceptionLogger", new NiftyExceptionLogger());

				SslServerConfiguration serverConfiguration = sslConfiguration.get();
				if (serverConfiguration != null)
					if (serverConfiguration.allowPlaintext)
						cp.addFirst("ssl_plaintext", new SslPlaintextHandler(serverConfiguration, "ssl"));
					else
						cp.addFirst("ssl", serverConfiguration.createHandler());
				return cp;
			}
		};
	}

	@Override
	public void run() {
		bossExecutor = nettyServerConfig.getBossExecutor();
		ioWorkerExecutor = nettyServerConfig.getWorkerExecutor();
		channelFactory = new NioServerSocketChannelFactory(
				new NioServerBossPool(bossExecutor, nettyServerConfig.getBossThreadCount(),
						ThreadNameDeterminer.CURRENT),
				new NioWorkerPool(ioWorkerExecutor, nettyServerConfig.getWorkerThreadCount(),
						ThreadNameDeterminer.CURRENT));
		try {
			start(channelFactory);
		} catch (IkasoaException e) {
			throw new RuntimeException(e);
		}
	}

	public void start(ServerChannelFactory serverChannelFactory) throws IkasoaException {
		bootstrap = new ServerBootstrap(serverChannelFactory);
		bootstrap.setOptions(nettyServerConfig.getBootstrapOptions());
		bootstrap.setPipelineFactory(pipelineFactory);
		serverChannel = bootstrap.bind(new InetSocketAddress(requestedPort));
		InetSocketAddress actualSocket = (InetSocketAddress) serverChannel.getLocalAddress();
		actualPort = actualSocket.getPort();
		if (actualPort == 0 || (actualPort != requestedPort && requestedPort != 0))
			throw new IkasoaException("Server initialize failed !");
		log.info("started transport {}:{}", server.getName(), actualPort);
		if (server.getTransportAttachObserver() != null)
			server.getTransportAttachObserver().attachTransport(this);
	}

	@Override
	@SneakyThrows
	public void stop() {
		if (serverChannel != null) {
			log.info("stopping transport {}:{}", server.getName(), actualPort);
			// first stop accepting
			final CountDownLatch latch = new CountDownLatch(1);
			serverChannel.close().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					latch.countDown();
				}
			});
			latch.await();
			serverChannel = null;
		}

		if (channelFactory != null) {
			channelFactory.releaseExternalResources();
			channelFactory.shutdown();
		}
		if (allChannels != null)
			allChannels.close();
		if (bossExecutor != null)
			shutdownExecutor(bossExecutor, "bossExecutor");
		if (ioWorkerExecutor != null)
			shutdownExecutor(ioWorkerExecutor, "workerExecutor");
		if (server.getTransportAttachObserver() != null)
			server.getTransportAttachObserver().detachTransport();
	}

	public Channel getServerChannel() {
		return serverChannel;
	}

	public int getPort() {
		return actualPort != 0 ? actualPort : requestedPort;
	}

	@Override
	public void releaseExternalResources() {
		bootstrap.releaseExternalResources();
	}

	private static class ConnectionLimiter extends SimpleChannelUpstreamHandler {
		private final int maxConnections;
		private final AtomicInteger numConnections;

		public ConnectionLimiter(int maxConnections) {
			this.maxConnections = maxConnections;
			this.numConnections = new AtomicInteger(0);
		}

		@Override
		public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			if (maxConnections > 0 && numConnections.incrementAndGet() > maxConnections) {
				ctx.getChannel().close();
				log.info("Accepted connection above limit ({}). Dropping.", maxConnections);
			}
			super.channelOpen(ctx, e);
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			if (maxConnections > 0 && numConnections.decrementAndGet() < 0)
				log.error("BUG in ConnectionLimiter");
			super.channelClosed(ctx, e);
		}
	}

	public NiftyMetrics getMetrics() {
		return channelStatistics;
	}

	/**
	 * Returns the current {@link SslServerConfiguration}.
	 *
	 * @return the configuration.
	 */
	public SslServerConfiguration getSSLConfiguration() {
		return sslConfiguration.get();
	}

	/**
	 * Atomically replaces the current {@link SslServerConfiguration} with the
	 * provided one.
	 *
	 * @param sslServerConfiguration
	 *            the new configuration.
	 */
	public void updateSSLConfiguration(SslServerConfiguration sslServerConfiguration) {
		sslConfiguration.set(sslServerConfiguration);
	}

	/**
	 * Atomically replaces the current {@link SslServerConfiguration} with
	 * {@code updated} if and only if the current configuration is {@code ==} to
	 * {@code expected}.
	 *
	 * @param expected
	 *            the expected current configuration.
	 * @param updated
	 *            the new configuration.
	 * @return true if the update succeeded, or false otherwise.
	 */
	public boolean compareAndSetSSLConfiguration(SslServerConfiguration expected, SslServerConfiguration updated) {
		return sslConfiguration.compareAndSet(expected, updated);
	}

	private void shutdownExecutor(ExecutorService executorService, final String name) {
		try {
			log.debug("Waiting for {} to shutdown", name);
			while (executorService != null && !executorService.isTerminated()) {
				executorService.awaitTermination(10, TimeUnit.SECONDS);
				log.debug("{} did not shutdown properly", name);
			}
		} catch (InterruptedException e) {
			log.warn("Interrupted while waiting for {} to shutdown", name);
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}

	}

}
