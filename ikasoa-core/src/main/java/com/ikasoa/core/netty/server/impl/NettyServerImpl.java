package com.ikasoa.core.netty.server.impl;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.netty.NettyDispatcher;
import com.ikasoa.core.netty.handler.impl.ThriftFrameCodeHandlerImpl;
import com.ikasoa.core.netty.server.NettyServer;
import com.ikasoa.core.netty.server.NettyServerConfiguration;
import com.ikasoa.core.thrift.server.ServerConfiguration;
import com.ikasoa.core.utils.MapUtil;
import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.core.utils.ServerUtil;

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
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.ThreadNameDeterminer;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Netty服务器默认实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
@Slf4j
public class NettyServerImpl implements NettyServer {

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

	protected Channel serverChannel;

	public NettyServerImpl(final String serverName, final int serverPort, final NettyServerConfiguration configuration,
			final TProcessor processor) {
		this(serverName, serverPort, configuration, processor, new DefaultChannelGroup());
	}

	public NettyServerImpl(final String serverName, final int serverPort, final NettyServerConfiguration configuration,
			final TProcessor processor, final ChannelGroup allChannels) {
		setServerName(serverName);
		requestedPort = serverPort;
		setConfiguration(ObjectUtil.isNull(configuration)
				? new NettyServerConfiguration(new TProcessorFactory(processor)) : configuration);
		setProcessor(processor);
		this.allChannels = allChannels;
	}

	@Override
	public void run() {
		if (!isServing()) {
			if (ObjectUtil.isNull(channelFactory)) {
				if (ObjectUtil.isNull(workerExecutorService))
					workerExecutorService = ObjectUtil.isNull(getServerConfiguration().getExecutorService())
							? Executors.newFixedThreadPool(2) : getServerConfiguration().getExecutorService();
				if (workerCount <= 0)
					workerCount = configuration.getWorkerCount();
				channelFactory = new NioServerSocketChannelFactory(
						new NioServerBossPool(bossExecutorService, 1, ThreadNameDeterminer.CURRENT),
						new NioWorkerPool(workerExecutorService, workerCount, ThreadNameDeterminer.CURRENT));
			}
			if (ObjectUtil.isNull(getBootstrap())) {
				bootstrap = new ServerBootstrap(channelFactory);
				bootstrap.setOptions(MapUtil.newHashMap());
				bootstrap.setPipelineFactory(() -> {
					ChannelPipeline cp = Channels.pipeline();
					cp.addLast("frameCodec", new ThriftFrameCodeHandlerImpl(configuration.getMaxFrameSize(),
							getServerConfiguration().getProtocolFactory()));
					cp.addLast("dispatcher", new NettyDispatcher(configuration, new HashedWheelTimer()));
					return cp;
				});
			}
			try {
				// 不允许使用1024以内的端口.
				if (!ServerUtil.isSocketPort(requestedPort))
					throw new IkasoaException(String.format(
							"Server initialize failed ! Port range must is 1025 ~ 65535 . Your port is : %d .",
							requestedPort));
				beforeStart(getServerConfiguration().getServerAspect());
				log.info("Startup server ... (name : {} , port : {})", getServerName(), requestedPort);
				serverChannel = getBootstrap().bind(new InetSocketAddress(requestedPort));
				actualPort = ((InetSocketAddress) serverChannel.getLocalAddress()).getPort();
				if (actualPort == 0 || (actualPort != requestedPort && requestedPort != 0))
					throw new IkasoaException("Startup server failed !");
				afterStart(getServerConfiguration().getServerAspect());
			} catch (IkasoaException e) {
				throw new RuntimeException(e);
			}
		} else
			log.info("Server already run .");
	}

	@Override
	@SneakyThrows
	public void stop() {
		if (isServing()) {
			beforeStop(getServerConfiguration().getServerAspect());
			log.info("stopping server ... (name: {})", getServerName());
			final CountDownLatch latch = new CountDownLatch(1);
			serverChannel.close().addListener(future -> latch.countDown());
			latch.await();
			serverChannel = null;
			if (ObjectUtil.isNotNull(channelFactory)) {
				channelFactory.releaseExternalResources();
				channelFactory.shutdown();
			}
			if (ObjectUtil.isNotNull(allChannels))
				allChannels.close();
			if (ObjectUtil.isNotNull(bossExecutorService))
				shutdownExecutor(bossExecutorService);
			if (ObjectUtil.isNotNull(workerExecutorService))
				shutdownExecutor(workerExecutorService);
			afterStop(getServerConfiguration().getServerAspect());
		} else
			log.debug("Server not run . (name: {})", getServerName());
	}

	@Override
	public int getServerPort() {
		return actualPort != 0 ? actualPort : requestedPort;
	}

	@Override
	public boolean isServing() {
		return ObjectUtil.isNotNull(serverChannel) && serverChannel.isOpen();
	}

	@Override
	public ServerConfiguration getServerConfiguration() {
		if (ObjectUtil.isNull(configuration))
			throw new RuntimeException("'configuration' is null !");
		return configuration;
	}

	@Override
	public TServerTransport getTransport() throws TTransportException {
		log.debug("This function not run .");
		return null;
	}

	@Override
	public void releaseExternalResources() {
		if (ObjectUtil.isNotNull(getBootstrap()))
			getBootstrap().releaseExternalResources();
	}

}
