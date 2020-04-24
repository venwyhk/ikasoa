package com.ikasoa.core.thrift.client.impl;

import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;

import com.ikasoa.core.ServerInfo;
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
		setServerInfo(new ServerInfo(serverHost, 1025));
		if (ObjectUtil.isNull(configuration)) {
			log.debug("Thrift client configuration is null .");
			setConfiguration(new ThriftClientConfiguration());
		} else
			setConfiguration(configuration);
	}

	@Override
	@SneakyThrows
	protected TTransport getTransport(ServerInfo serverInfo) {
		return new THttpClient(serverInfo.getHost());
	}

}
