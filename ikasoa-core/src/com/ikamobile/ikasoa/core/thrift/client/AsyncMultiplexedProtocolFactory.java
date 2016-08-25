package com.ikamobile.ikasoa.core.thrift.client;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

public class AsyncMultiplexedProtocolFactory extends TCompactProtocol.Factory implements TProtocolFactory {

	private static final long serialVersionUID = 1L;

	private String serviceName;

	public AsyncMultiplexedProtocolFactory(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public TProtocol getProtocol(TTransport trans) {
		if (serviceName != null) {
			return new TMultiplexedProtocol(super.getProtocol(trans), serviceName);
		} else {
			return super.getProtocol(trans);
		}
	}

}
