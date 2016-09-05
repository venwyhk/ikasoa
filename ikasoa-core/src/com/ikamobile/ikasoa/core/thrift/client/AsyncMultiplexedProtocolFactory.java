package com.ikamobile.ikasoa.core.thrift.client;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

import com.ikamobile.ikasoa.core.utils.StringUtil;

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
		if (StringUtil.isNotEmpty(serviceName)) {
			return new TMultiplexedProtocol(super.getProtocol(trans), serviceName);
		} else {
			return super.getProtocol(trans);
		}
	}

}
