package com.ikasoa.core.thrift.client.impl;

import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HttpThrift客户端实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.5.1
 */
@NoArgsConstructor
@Slf4j
public class HttpThriftClientImpl extends AbstractThriftClientImpl {

	public HttpThriftClientImpl(String serverHost, ThriftClientConfiguration configuration) {
		setServerHost(serverHost);
		if (configuration == null) {
			log.debug("Thrift client configuration is null .");
			configuration = new ThriftClientConfiguration();
		}
		setConfiguration(configuration);
	}

	@Override
	protected TTransport getTransport(String serverHost, int serverPort) throws IkasoaException {
		try {
			return new THttpClient(serverHost);
		} catch (TTransportException e) {
			throw new IkasoaException("Get http transport failed !", e);
		}
	}

	@Override
	public int getServerPort() {
		return 0;
	}

}
