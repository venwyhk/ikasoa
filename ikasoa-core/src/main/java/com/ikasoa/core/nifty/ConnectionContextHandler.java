package com.ikasoa.core.nifty;

import com.ikasoa.core.nifty.ssl.SslSession;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ConnectionContextHandler extends SimpleChannelUpstreamHandler {
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);

		NiftyConnectionContext context = new NiftyConnectionContext();
		context.setRemoteAddress(ctx.getChannel().getRemoteAddress());

		ctx.setAttachment(context);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof SslSession) {
			NiftyConnectionContext context = (NiftyConnectionContext) ctx.getAttachment();
			context.setSslSession((SslSession) e.getMessage());
		} else {
			super.messageReceived(ctx, e);
		}
	}
};
