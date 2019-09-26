package com.ikasoa.core.netty.handler.impl;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;
import org.apache.thrift.transport.TTransportException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;

import com.ikasoa.core.netty.TNettyTransport;
import com.ikasoa.core.netty.TNettyTransportType;
import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.core.netty.TNettyMessage;

/**
 * ThriftFrameDecoder
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public class ThriftFrameDecoder extends FrameDecoder {

	public final static short MESSAGE_FRAME_SIZE = 4;

	private final int maxFrameSize;

	private final TProtocolFactory inputProtocolFactory;

	public ThriftFrameDecoder(int maxFrameSize, TProtocolFactory inputProtocolFactory) {
		this.maxFrameSize = maxFrameSize;
		this.inputProtocolFactory = inputProtocolFactory;
	}

	@Override
	protected TNettyMessage decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {

		if (!buffer.readable())
			return null;

		short firstByte = buffer.getUnsignedByte(0);
		if (firstByte >= 0x80) {
			ChannelBuffer messageBuffer = tryDecodeUnframedMessage(ctx, channel, buffer, inputProtocolFactory);
			return ObjectUtil.isNull(messageBuffer) ? null
					: new TNettyMessage(messageBuffer, TNettyTransportType.UNFRAMED);
		} else if (buffer.readableBytes() < MESSAGE_FRAME_SIZE)
			return null;
		else {
			ChannelBuffer messageBuffer = tryDecodeFramedMessage(ctx, channel, buffer, true);
			return ObjectUtil.isNull(messageBuffer) ? null
					: new TNettyMessage(messageBuffer, TNettyTransportType.FRAMED);
		}
	}

	protected ChannelBuffer tryDecodeFramedMessage(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer,
			boolean stripFraming) {

		int messageStartReaderIndex = buffer.readerIndex();
		int messageContentsOffset = stripFraming ? messageStartReaderIndex + MESSAGE_FRAME_SIZE
				: messageStartReaderIndex;
		int messageLength = buffer.getInt(messageStartReaderIndex) + MESSAGE_FRAME_SIZE;
		int messageContentsLength = messageStartReaderIndex + messageLength - messageContentsOffset;

		if (messageContentsLength > maxFrameSize)
			Channels.fireExceptionCaught(ctx,
					new TooLongFrameException(String.format("Maximum frame size of %d exceeded .", maxFrameSize)));

		if (messageLength == 0) {
			buffer.readerIndex(messageContentsOffset);
			return null;
		} else if (buffer.readableBytes() < messageLength)
			return null;
		else {
			ChannelBuffer messageBuffer = extractFrame(buffer, messageContentsOffset, messageContentsLength);
			buffer.readerIndex(messageStartReaderIndex + messageLength);
			return messageBuffer;
		}
	}

	protected ChannelBuffer tryDecodeUnframedMessage(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer,
			TProtocolFactory inputProtocolFactory) throws TException {

		int messageLength = 0;
		int messageStartReaderIndex = buffer.readerIndex();

		try {
			TNettyTransport decodeAttemptTransport = new TNettyTransport(channel, buffer, TNettyTransportType.UNFRAMED);
			int initialReadBytes = decodeAttemptTransport.getReadByteCount();
			TProtocol inputProtocol = inputProtocolFactory.getProtocol(decodeAttemptTransport);
			inputProtocol.readMessageBegin();
			TProtocolUtil.skip(inputProtocol, TType.STRUCT);
			inputProtocol.readMessageEnd();
			messageLength = decodeAttemptTransport.getReadByteCount() - initialReadBytes;
		} catch (TTransportException | IndexOutOfBoundsException e) {
			return null;
		} finally {
			if (buffer.readerIndex() - messageStartReaderIndex > maxFrameSize)
				Channels.fireExceptionCaught(ctx,
						new TooLongFrameException(String.format("Maximum frame size of %d exceeded .", maxFrameSize)));
			buffer.readerIndex(messageStartReaderIndex);
		}

		if (messageLength <= 0)
			return null;

		ChannelBuffer messageBuffer = extractFrame(buffer, messageStartReaderIndex, messageLength);
		buffer.readerIndex(messageStartReaderIndex + messageLength);
		return messageBuffer;
	}

	protected ChannelBuffer extractFrame(ChannelBuffer buffer, int index, int length) {
		return buffer.slice(index, length);
	}
}
