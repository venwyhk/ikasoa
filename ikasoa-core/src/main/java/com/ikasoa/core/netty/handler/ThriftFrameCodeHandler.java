package com.ikasoa.core.netty.handler;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelUpstreamHandler;

/**
 * ThriftFrameCodeHandler
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public interface ThriftFrameCodeHandler extends ChannelUpstreamHandler, ChannelDownstreamHandler {
}
