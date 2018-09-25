package com.ikasoa.core.nifty.handler;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelUpstreamHandler;

public interface ThriftFrameCodeHandler extends ChannelUpstreamHandler, ChannelDownstreamHandler {
}
