package com.ikasoa.core.netty;

import org.apache.thrift.TProcessor;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.ikasoa.core.netty.server.NettyServerConfiguration;
import com.ikasoa.core.netty.server.impl.DefaultNettyServerImpl;
import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.server.ThriftServer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Netty服务端工厂实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
@NoArgsConstructor
public class NettyServerFactory extends GeneralFactory {

	@Getter
	@Setter
	protected NettyServerConfiguration nettyServerConfiguration = null;

	@Getter
	@Setter
	protected DefaultChannelGroup defaultChannelGroup = new DefaultChannelGroup();

	public NettyServerFactory(NettyServerConfiguration nettyServerConfiguration) {
		this.nettyServerConfiguration = nettyServerConfiguration;
	}

	public NettyServerFactory(ThriftClientConfiguration thriftClientConfiguration) {
		super.thriftClientConfiguration = thriftClientConfiguration;
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, TProcessor processor) {
		return new DefaultNettyServerImpl(serverName, serverPort, nettyServerConfiguration, processor,
				defaultChannelGroup);
	}

	@Override
	public ThriftServer getNonblockingThriftServer(String serverName, int serverPort, TProcessor processor) {
		return getThriftServer(serverName, serverPort, processor); // netty服务端不区分是否为nio
	}

}
