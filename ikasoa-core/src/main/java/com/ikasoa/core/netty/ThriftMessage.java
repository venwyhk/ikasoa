package com.ikasoa.core.netty;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * ThriftMessage
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
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

	public Factory getMessageFactory() {
		return new Factory() {
			@Override
			public ThriftMessage create(ChannelBuffer messageBuffer) {
				return new ThriftMessage(messageBuffer, getTransportType());
			}
		};
	}

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
