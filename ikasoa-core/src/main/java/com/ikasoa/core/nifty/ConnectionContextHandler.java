package com.ikasoa.core.nifty;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ConnectionContextHandler extends SimpleChannelUpstreamHandler {

	@Override
	public void channelConnected(ChannelHandlerContext context, ChannelStateEvent e) throws Exception {
		super.channelConnected(context, e);
		IkasoaConnectionContext ikasoaConnectionContext = new IkasoaConnectionContext();
		ikasoaConnectionContext.setRemoteAddress(context.getChannel().getRemoteAddress());
		context.setAttachment(context);
	}

	private class IkasoaConnectionContext implements ConnectionContext {

		private SocketAddress remoteAddress;
		private Map<String, Object> attributes = new ConcurrentHashMap<>();

		@Override
		public SocketAddress getRemoteAddress() {
			return remoteAddress;
		}

		public void setRemoteAddress(SocketAddress remoteAddress) {
			this.remoteAddress = remoteAddress;
		}

		@Override
		public Object getAttribute(String attributeName) {
			if (attributeName == null)
				throw new NullPointerException();
			return attributes.get(attributeName);
		}

		@Override
		public Object setAttribute(String attributeName, Object value) {
			if (attributeName == null)
				throw new NullPointerException();
			if (value == null)
				throw new NullPointerException();
			return attributes.put(attributeName, value);
		}

		@Override
		public Object removeAttribute(String attributeName) {
			if (attributeName == null)
				throw new NullPointerException();
			return attributes.remove(attributeName);
		}

		@Override
		public Iterator<Map.Entry<String, Object>> attributeIterator() {
			return Collections.unmodifiableSet(attributes.entrySet()).iterator();
		}
	}

};
