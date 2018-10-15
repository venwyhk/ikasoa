package com.ikasoa.core.nifty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ConnectionContextHandler extends SimpleChannelUpstreamHandler {
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);

		NiftyConnectionContext context = new NiftyConnectionContext();
		context.setRemoteAddress(ctx.getChannel().getRemoteAddress());

		ctx.setAttachment(context);
	}
};
