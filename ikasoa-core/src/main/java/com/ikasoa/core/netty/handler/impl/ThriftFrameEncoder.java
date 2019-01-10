package com.ikasoa.core.netty.handler.impl;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.ikasoa.core.netty.TNettyMessage;

/**
 * ThriftFrameEncoder
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public class ThriftFrameEncoder extends OneToOneEncoder {

	private final int maxFrameSize;

	public ThriftFrameEncoder(int maxFrameSize) {
		this.maxFrameSize = maxFrameSize;
	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

		if (!(msg instanceof TNettyMessage))
			return msg;

		TNettyMessage message = (TNettyMessage) msg;

		int frameSize = message.getBuffer().readableBytes();

		if (message.getBuffer().readableBytes() > maxFrameSize) {
			Channels.fireExceptionCaught(ctx,
					new TooLongFrameException(String.format(
							"Frame size exceeded on encode: frame was %d bytes, maximum allowed is %d bytes .",
							frameSize, maxFrameSize)));
			return null;
		}

		switch (message.getTransportType()) {
		case UNFRAMED:
			return message.getBuffer();
		case FRAMED:
			ChannelBuffer frameSizeBuffer = ChannelBuffers.buffer(4);
			frameSizeBuffer.writeInt(message.getBuffer().readableBytes());
			return ChannelBuffers.wrappedBuffer(frameSizeBuffer, message.getBuffer());
		case HEADER:
			throw new UnsupportedOperationException("Header transport is not supported .");
		case HTTP:
			throw new UnsupportedOperationException("HTTP transport is not supported .");
		default:
			throw new UnsupportedOperationException("Unrecognized transport type .");
		}
	}
}
