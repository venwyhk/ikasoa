package com.ikamobile.ikasoa.core.thrift.client;

import org.apache.thrift.protocol.TCompactProtocol;

/**
 * Thrift客户端配置 (<i>TCompactProtocol</i>方式)
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class CompactThriftClientConfiguration extends ThriftClientConfiguration {

	public CompactThriftClientConfiguration() {
		setProtocolFactory(new TCompactProtocol.Factory());
	}

}
