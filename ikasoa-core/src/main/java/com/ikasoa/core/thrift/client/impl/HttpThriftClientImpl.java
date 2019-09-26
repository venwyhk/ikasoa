package com.ikasoa.core.thrift.client.impl;

import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;

import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.utils.ObjectUtil;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
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
		if (ObjectUtil.isNull(configuration)) {
			log.debug("Thrift client configuration is null .");
			setConfiguration(new ThriftClientConfiguration());
		} else
			setConfiguration(configuration);
	}

	@Override
	@SneakyThrows
	protected TTransport getTransport(String serverHost, int serverPort) {
		return new THttpClient(serverHost);
	}

	@Override
	public int getServerPort() {
		return 0;
	}

}
