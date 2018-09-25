package com.ikasoa.core.nifty;

import org.jboss.netty.channel.Channel;

public class ConnectionContexts {
	public static ConnectionContext getContext(Channel channel) {
		ConnectionContext context = (ConnectionContext) channel.getPipeline().getContext(ConnectionContextHandler.class)
				.getAttachment();
		Preconditions.checkState(context != null, "Context not yet set on channel {}", channel);
		return context;
	}
}
