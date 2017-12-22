package com.ikasoa.core.thrift.client.impl;

import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikasoa.core.STException;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;

/**
 * HttpThrift客户端实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.5.1
 */
public class HttpThriftClientImpl extends AbstractThriftClientImpl {
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpThriftClientImpl.class);

	public HttpThriftClientImpl() {
		// Do nothing
	}

	public HttpThriftClientImpl(String serverHost, ThriftClientConfiguration configuration) {
		setServerHost(serverHost);
		if (configuration == null) {
			LOG.debug("ThriftClientConfiguration is null .");
			configuration = new ThriftClientConfiguration();
		}
		setThriftClientConfiguration(configuration);
	}

	@Override
	protected TTransport getTransport(String serverHost, int serverPort) throws STException {
		try {
			return new THttpClient(serverHost);
		} catch (TTransportException e) {
			throw new STException("Get http transport failed !", e);
		}
	}

	@Override
	public int getServerPort() {
		return 0;
	}

}
