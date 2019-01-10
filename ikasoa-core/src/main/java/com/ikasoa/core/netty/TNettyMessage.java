package com.ikasoa.core.netty;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * ThriftMessage
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public class TNettyMessage {

	private final ChannelBuffer buffer;

	private final TNettyTransportType transportType;

	private long processStartTimeMillis;

	public TNettyMessage(ChannelBuffer buffer, TNettyTransportType transportType) {
		this.buffer = buffer;
		this.transportType = transportType;
	}

	public ChannelBuffer getBuffer() {
		return buffer;
	}

	public TNettyTransportType getTransportType() {
		return transportType;
	}

	public Factory getMessageFactory() {
		return new Factory() {
			@Override
			public TNettyMessage create(ChannelBuffer messageBuffer) {
				return new TNettyMessage(messageBuffer, getTransportType());
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
		public TNettyMessage create(ChannelBuffer messageBuffer);
	}
}
