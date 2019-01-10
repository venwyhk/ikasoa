package com.ikasoa.rpc;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TServerTransport;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.netty.server.NettyServer;
import com.ikasoa.core.netty.server.NettyServerConfiguration;
import com.ikasoa.core.netty.server.impl.DefaultNettyServerImpl;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.thrift.server.impl.AbstractThriftServerImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * IKASOA服务工厂(Netty实现)
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
@Slf4j
public class NettyIkasoaFactory extends DefaultIkasoaFactory {

	@Getter
	@Setter
	private NettyServerConfiguration configuration;

	@Getter
	@Setter
	private ChannelGroup channelGroup;

	public NettyIkasoaFactory(Configurator configurator) {
		super(configurator);
	}

	public NettyIkasoaFactory(NettyServerConfiguration configuration) {
		this.configuration = configuration;
		this.channelGroup = new DefaultChannelGroup();
	}

	public NettyIkasoaFactory(NettyServerConfiguration configuration, ChannelGroup channelGroup) {
		this.configuration = configuration;
		this.channelGroup = channelGroup == null ? new DefaultChannelGroup() : channelGroup;
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, TProcessor processor) {
		return new NettyServerImpl("NettyServer-" + serverPort, serverPort, processor);
	}

	private class NettyServerImpl extends AbstractThriftServerImpl {

		private NettyServer server;

		public NettyServerImpl(String serverName, int serverPort, TProcessor processor) {
			setServerName(serverName);
			setServerPort(serverPort);
			setConfiguration(new ThriftServerConfiguration());
			setProcessor(processor);
			server = new DefaultNettyServerImpl(getServerName(), getServerPort(), configuration, getProcessor(),
					channelGroup);
		}

		@Override
		protected void initServer(TServerTransport serverTransport) {
			// Do nothing
		}

		@Override
		public void start() throws IkasoaException {
			server.run();
			log.info("Starting server ... (name : {} , port : {})", getServerName(), getServerPort());
		}

		@Override
		public void stop() {
			if (server != null && server.isServing()) {
				server.stop();
				log.info("Stoping server ... (name: {})", getServerName());
			} else
				log.debug("Server not run . (name: {})", getServerName());
		}
	}
}
