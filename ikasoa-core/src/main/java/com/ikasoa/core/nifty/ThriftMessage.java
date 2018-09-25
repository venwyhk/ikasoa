package com.ikasoa.core.nifty;

import org.jboss.netty.buffer.ChannelBuffer;

public class ThriftMessage {
	private final ChannelBuffer buffer;
	private final ThriftTransportType transportType;
	private long processStartTimeMillis;

	public ThriftMessage(ChannelBuffer buffer, ThriftTransportType transportType) {
		this.buffer = buffer;
		this.transportType = transportType;
	}

	public ChannelBuffer getBuffer() {
		return buffer;
	}

	public ThriftTransportType getTransportType() {
		return transportType;
	}

	/**
	 * Gets a {@link Factory} for creating messages similar to this one. Used by
	 * {@link NiftyDispatcher} to create response messages that are similar to their
	 * corresponding request messages.
	 *
	 * @return The {@link Factory}
	 */
	public Factory getMessageFactory() {
		return new Factory() {
			@Override
			public ThriftMessage create(ChannelBuffer messageBuffer) {
				return new ThriftMessage(messageBuffer, getTransportType());
			}
		};
	}

	/**
	 * Standard Thrift clients require ordered responses, so even though Nifty can
	 * run multiple requests from the same client at the same time, the responses
	 * have to be held until all previous responses are ready and have been written.
	 * However, through the use of extended protocols and codecs, a request can
	 * indicate that the client understands out-of-order responses.
	 *
	 * @return {@code true} if ordered responses are required
	 */
	public boolean isOrderedResponsesRequired() {
		return true;
	}

	public long getProcessStartTimeMillis() {
		return processStartTimeMillis;
	}

	public void setProcessStartTimeMillis(long processStartTimeMillis) {
		this.processStartTimeMillis = processStartTimeMillis;
	}

	public static interface Factory {
		public ThriftMessage create(ChannelBuffer messageBuffer);
	}
}
