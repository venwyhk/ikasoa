package com.ikasoa.core.nifty;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import lombok.Getter;
import lombok.Setter;

/**
 * Wraps incoming channel buffer into TTransport and provides a output buffer.
 */
public class TNiftyTransport extends TTransport {

	private static final int DEFAULT_OUTPUT_BUFFER_SIZE = 1024;

	private final Channel channel;

	private final ChannelBuffer inputBuffer;

	@Getter
	private final ThriftTransportType thriftTransportType;

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

	public TNiftyTransport(Channel channel, ChannelBuffer inputBuffer, ThriftTransportType thriftTransportType) {
		this.channel = channel;
		this.inputBuffer = inputBuffer;
		this.thriftTransportType = thriftTransportType;
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
			// Without this, reading from a !in.hasArray() buffer will advance the
			// readerIndex
			// of the buffer, while reading from a in.hasArray() buffer will not advance the
			// readerIndex, and this has led to subtle bugs. This should help to identify
			// those problems by making things more consistent.
			inputBuffer.readerIndex(inputBuffer.readerIndex() + inputBuffer.readableBytes());
		}
	}

	public TNiftyTransport(Channel channel, ThriftMessage message) {
		this(channel, message.getBuffer(), message.getTransportType());
	}

	@Override
	public boolean isOpen() {
		return channel.isOpen();
	}

	@Override
	public void open() throws TTransportException {
		// no-op
	}

	@Override
	public void close() {
		// no-op
		channel.close();
	}

	@Override
	public int read(byte[] bytes, int offset, int length) throws TTransportException {
		if (getBytesRemainingInBuffer() >= 0) {
			int _read = Math.min(getBytesRemainingInBuffer(), length);
			System.arraycopy(getBuffer(), getBufferPosition(), bytes, offset, _read);
			consumeBuffer(_read);
			return _read;
		} else {
			int _read = Math.min(inputBuffer.readableBytes(), length);
			inputBuffer.readBytes(bytes, offset, _read);
			return _read;
		}
	}

	@Override
	public int readAll(byte[] bytes, int offset, int length) throws TTransportException {
		if (read(bytes, offset, length) < length) {
			throw new TTransportException("Buffer doesn't have enough bytes to read");
		}
		return length;
	}

	@Override
	public void write(byte[] bytes, int offset, int length) throws TTransportException {
		outputBuffer.writeBytes(bytes, offset, length);
	}

	@Override
	public void flush() throws TTransportException {
		// Flush is a no-op: NiftyDispatcher will write the response to the Channel, in
		// order to
		// guarantee ordering of responses when required.
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
