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

import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.NettyServerTransport;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.nifty.core.ThriftServerDefBuilder;

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
	private NettyServerConfig nettyServerConfig;

	@Getter
	@Setter
	private ChannelGroup channelGroup;

	public NettyIkasoaFactory(Configurator configurator) {
		super(configurator);
	}

	public NettyIkasoaFactory(NettyServerConfig nettyServerConfig) {
		this.nettyServerConfig = nettyServerConfig;
		this.channelGroup = new DefaultChannelGroup();
	}

	public NettyIkasoaFactory(NettyServerConfig nettyServerConfig, ChannelGroup channelGroup) {
		this.nettyServerConfig = nettyServerConfig;
		this.channelGroup = channelGroup == null ? new DefaultChannelGroup() : channelGroup;
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, TProcessor processor) {
		return new NiftyThriftServerImpl("NiftyServer-" + serverPort, serverPort, processor);
	}

	private class NiftyThriftServerImpl extends AbstractThriftServerImpl {

		private NettyServerTransport server;

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
				ThriftServerDef thriftServerDef = new ThriftServerDefBuilder().listen(getServerPort())
						.withProcessor(getProcessor()).build();
				server = nettyServerConfig == null ? new NettyServerTransport(thriftServerDef)
						: new NettyServerTransport(thriftServerDef, nettyServerConfig, channelGroup);
			}
			server.start();
			log.debug("Server start .");
		}

		@Override
		public void stop() {
			if (server != null)
				try {
					server.stop();
				} catch (InterruptedException e) {
					throw new RuntimeException("Server stop exception !", e);
				}
			else
				log.warn("Server is not start , Can't to execute stop !");
		}
	}
}
