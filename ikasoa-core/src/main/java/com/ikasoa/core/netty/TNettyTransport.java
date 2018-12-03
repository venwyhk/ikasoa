package com.ikasoa.core.netty;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import lombok.Getter;
import lombok.Setter;

/**
 * 将传入的信道buffer封装到<code>TTransport</code>中，并提供输出buffer
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public class TNettyTransport extends TTransport {

	private static final int DEFAULT_OUTPUT_BUFFER_SIZE = 1024;

	private final Channel channel;

	private final ChannelBuffer inputBuffer;

	@Getter
	private final TNettyTransportType tNettyTransportType;

	@Getter
	@Setter
	private ChannelBuffer outputBuffer;

	private final int initialReaderIndex;

	private final int initialBufferPosition;

	@Getter
	private int bufferPosition;

	private int bufferEnd;

	@Getter
	private final byte[] buffer;

	@Getter
	@Setter
	private TApplicationException tApplicationException;

	public TNettyTransport(Channel channel, ChannelBuffer inputBuffer, TNettyTransportType tNettyTransportType) {
		this.channel = channel;
		this.inputBuffer = inputBuffer;
		this.tNettyTransportType = tNettyTransportType;
		this.outputBuffer = ChannelBuffers.dynamicBuffer(DEFAULT_OUTPUT_BUFFER_SIZE);
		this.initialReaderIndex = inputBuffer.readerIndex();

		if (!inputBuffer.hasArray()) {
			buffer = null;
			bufferPosition = 0;
			initialBufferPosition = bufferEnd = -1;
		} else {
			buffer = inputBuffer.array();
			initialBufferPosition = bufferPosition = inputBuffer.arrayOffset() + inputBuffer.readerIndex();
			bufferEnd = bufferPosition + inputBuffer.readableBytes();
			inputBuffer.readerIndex(inputBuffer.readerIndex() + inputBuffer.readableBytes());
		}
	}

	public TNettyTransport(Channel channel, TNettyMessage message) {
		this(channel, message.getBuffer(), message.getTransportType());
	}

	@Override
	public boolean isOpen() {
		return channel.isOpen();
	}

	@Override
	public void open() throws TTransportException {
		// Do nothing
	}

	@Override
	public void close() {
		channel.close();
	}

	@Override
	public int read(byte[] bytes, int offset, int length) throws TTransportException {
		if (getBytesRemainingInBuffer() >= 0) {
			int read = Math.min(getBytesRemainingInBuffer(), length);
			System.arraycopy(getBuffer(), getBufferPosition(), bytes, offset, read);
			consumeBuffer(read);
			return read;
		} else {
			int read = Math.min(inputBuffer.readableBytes(), length);
			inputBuffer.readBytes(bytes, offset, read);
			return read;
		}
	}

	@Override
	public int readAll(byte[] bytes, int offset, int length) throws TTransportException {
		if (read(bytes, offset, length) < length)
			throw new TTransportException("Buffer doesn't have enough bytes to read .");
		return length;
	}

	@Override
	public void write(byte[] bytes, int offset, int length) throws TTransportException {
		outputBuffer.writeBytes(bytes, offset, length);
	}

	@Override
	public void flush() throws TTransportException {
		// Do nothing
	}

	@Override
	public void consumeBuffer(int len) {
		bufferPosition += len;
	}

	@Override
	public int getBytesRemainingInBuffer() {
		return bufferEnd - bufferPosition;
	}

	public int getReadByteCount() {
		return getBytesRemainingInBuffer() >= 0 ? getBufferPosition() - initialBufferPosition
				: inputBuffer.readerIndex() - initialReaderIndex;
	}

	public int getWrittenByteCount() {
		return getOutputBuffer().writerIndex();
	}
}
