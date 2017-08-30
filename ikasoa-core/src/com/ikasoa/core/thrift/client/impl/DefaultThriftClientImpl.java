package com.ikasoa.core.thrift.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikasoa.core.thrift.client.ThriftClientConfiguration;

/**
 * 默认Thrift客户端实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class DefaultThriftClientImpl extends AbstractThriftClientImpl {
	
	private static final Logger LOG = LoggerFactory.getLogger(DefaultThriftClientImpl.class);

	public DefaultThriftClientImpl() {
		// Do nothing
	}

	public DefaultThriftClientImpl(String serverHost, int serverPort, ThriftClientConfiguration configuration) {
		setServerHost(serverHost);
		setServerPort(serverPort);
		if (configuration == null) {
			LOG.debug("ThriftClientConfiguration is null .");
			configuration = new ThriftClientConfiguration();
		}
		setThriftClientConfiguration(configuration);
	}

}
