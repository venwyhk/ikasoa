package com.ikasoa.core.nifty;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.ikasoa.core.nifty.server.NettyServerConfiguration;
import com.ikasoa.core.nifty.server.NiftyServerConfiguration;

public class NiftyNoOpSecurityFactory implements NiftySecurityFactory {
	static final ChannelHandler noOpHandler = new SimpleChannelHandler() {
		@Override
		public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			super.channelOpen(ctx, e);
			ctx.getPipeline().remove(this);
		}
	};

	@Override
	public NiftySecurityHandlers getSecurityHandlers(NiftyServerConfiguration def, NettyServerConfiguration serverConfig) {
		return new NiftySecurityHandlers() {
			@Override
			public ChannelHandler getAuthenticationHandler() {
				return noOpHandler;
			}

			@Override
			public ChannelHandler getEncryptionHandler() {
				return noOpHandler;
			}
		};
	}
}
