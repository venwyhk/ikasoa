package com.ikasoa.core.nifty;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

/**
 * Dispatches downstream messages from the worker threads to the IO threads, so
 * that code in IO threads do not have to worry about downstream paths being
 * invoked from multiple threads.
 */
public class NiftyIODispatcher extends SimpleChannelDownstreamHandler {

	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		ctx.getPipeline().execute(new Runnable() {
			@Override
			public void run() {
				try {
					NiftyIODispatcher.super.handleDownstream(ctx, e);
				} catch (Exception ex) {
					Channels.fireExceptionCaught(ctx.getChannel(), ex);
				}
			}
		});
	}
}
