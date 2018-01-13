package com.ikasoa.core.thrift.client;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

import com.ikasoa.core.utils.StringUtil;

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
		return StringUtil.isNotEmpty(serviceName) ? new TMultiplexedProtocol(super.getProtocol(trans), serviceName)
				: super.getProtocol(trans);
	}

}
