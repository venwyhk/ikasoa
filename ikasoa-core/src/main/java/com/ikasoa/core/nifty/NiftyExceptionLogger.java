package com.ikasoa.core.nifty;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.handler.logging.LoggingHandler;

import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

@Slf4j
public class NiftyExceptionLogger extends LoggingHandler {

	@Override
	public void log(ChannelEvent event) {
		if (event instanceof ExceptionEvent) {
			ExceptionEvent exceptionEvent = (ExceptionEvent) event;
			SocketAddress remoteAddress = exceptionEvent.getChannel().getRemoteAddress();
			log.error("Exception triggered on channel connected to {}", remoteAddress);
		}
	}
}
