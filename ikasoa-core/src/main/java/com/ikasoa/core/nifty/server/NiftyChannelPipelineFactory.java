package com.ikasoa.core.nifty.server;

import org.apache.thrift.protocol.TProtocolFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.util.HashedWheelTimer;

import com.ikasoa.core.nifty.ConnectionContextHandler;
import com.ikasoa.core.nifty.NiftyDispatcher;
import com.ikasoa.core.nifty.handler.impl.ThriftFrameCodeHandlerImpl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NiftyChannelPipelineFactory implements ChannelPipelineFactory {

	private NiftyServerConfiguration configuration;

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline cp = Channels.pipeline();
		TProtocolFactory inputProtocolFactory = configuration.getProtocolFactory();
		cp.addLast("connectionContext", new ConnectionContextHandler());
		cp.addLast("frameCodec", new ThriftFrameCodeHandlerImpl(configuration.getMaxFrameSize(), inputProtocolFactory));
		cp.addLast("dispatcher", new NiftyDispatcher(configuration, new HashedWheelTimer()));
		return cp;
	}

}
