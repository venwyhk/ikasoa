package com.ikasoa.core.nifty;

import org.jboss.netty.channel.Channel;

public class ConnectionContexts {
	public static ConnectionContext getContext(Channel channel) {
		ConnectionContext context = (ConnectionContext) channel.getPipeline().getContext(ConnectionContextHandler.class)
				.getAttachment();
		if(context == null)
			throw new IllegalStateException("Context not yet set on channel " + channel.toString());
		return context;
	}
}
