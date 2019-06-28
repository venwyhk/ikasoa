package com.ikasoa.core.thrift.client;

import java.util.Optional;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

import lombok.AllArgsConstructor;

/**
 * AsyncMultiplexedProtocolFactory
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
@AllArgsConstructor
public class AsyncMultiplexedProtocolFactory extends TCompactProtocol.Factory implements TProtocolFactory {

	private static final long serialVersionUID = -7061805270980551456L;

	private String serviceName;

	@Override
	public TProtocol getProtocol(TTransport trans) {
		return Optional.ofNullable(serviceName)
				.map(n -> (TProtocol) new TMultiplexedProtocol(super.getProtocol(trans), n))
				.orElse(super.getProtocol(trans));
	}

}
