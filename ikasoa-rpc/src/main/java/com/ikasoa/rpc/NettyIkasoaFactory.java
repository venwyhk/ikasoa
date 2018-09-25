package com.ikasoa.rpc;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TServerTransport;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.thrift.server.impl.AbstractThriftServerImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.ikasoa.core.nifty.server.NettyServerConfiguration;
import com.ikasoa.core.nifty.server.NiftyServerConfiguration;
import com.ikasoa.core.nifty.server.impl.NettyServerImpl;

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
	private NettyServerConfiguration nettyServerConfig;

	@Getter
	@Setter
	private ChannelGroup channelGroup;

	public NettyIkasoaFactory(Configurator configurator) {
		super(configurator);
	}

	public NettyIkasoaFactory(NettyServerConfiguration nettyServerConfig) {
		this.nettyServerConfig = nettyServerConfig;
		this.channelGroup = new DefaultChannelGroup();
	}

	public NettyIkasoaFactory(NettyServerConfiguration nettyServerConfig, ChannelGroup channelGroup) {
		this.nettyServerConfig = nettyServerConfig;
		this.channelGroup = channelGroup == null ? new DefaultChannelGroup() : channelGroup;
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, TProcessor processor) {
		return new NiftyThriftServerImpl("NiftyServer-" + serverPort, serverPort, processor);
	}

	private class NiftyThriftServerImpl extends AbstractThriftServerImpl {

		private NettyServerImpl server;

		public NiftyThriftServerImpl(String serverName, int serverPort, TProcessor processor) {
			setServerName(serverName);
			setServerPort(serverPort);
			setConfiguration(new ThriftServerConfiguration());
			setProcessor(processor);
		}

		@Override
		protected void initServer(TServerTransport serverTransport) {
		}

		@Override
		public void start() throws IkasoaException {
			if (server == null) {
				NiftyServerConfiguration niftyServer = new NiftyServerConfiguration("NiftyServer", getServerPort(), getProcessor());
				server = nettyServerConfig == null ? new NettyServerImpl(niftyServer)
						: new NettyServerImpl(niftyServer, nettyServerConfig, channelGroup);
			}
			server.run();
			log.debug("Server start .");
		}

		@Override
		public void stop() {
			if (server != null)
				server.stop();
			else
				log.warn("Server is not start , Can't to execute stop !");
		}
	}
}
