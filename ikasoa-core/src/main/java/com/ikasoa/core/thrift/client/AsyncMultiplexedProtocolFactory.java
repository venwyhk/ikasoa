package com.ikasoa.core.thrift.client;

import java.util.Optional;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

/**
 * AsyncMultiplexedProtocolFactory
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class AsyncMultiplexedProtocolFactory extends TCompactProtocol.Factory implements TProtocolFactory {

	private static final long serialVersionUID = 1L;

	private String serviceName;

	public AsyncMultiplexedProtocolFactory(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public TProtocol getProtocol(TTransport trans) {
		return Optional.ofNullable(serviceName)
				.map(n -> (TProtocol) new TMultiplexedProtocol(super.getProtocol(trans), n))
				.orElse(super.getProtocol(trans));
	}

}
