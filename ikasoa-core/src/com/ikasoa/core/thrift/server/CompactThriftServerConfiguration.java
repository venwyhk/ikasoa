package com.ikasoa.core.thrift.server;

import org.apache.thrift.protocol.TCompactProtocol;

/**
 * Thrift服务器配置 (<i>TCompactProtocol</i>方式)
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class CompactThriftServerConfiguration extends ThriftServerConfiguration {

	public CompactThriftServerConfiguration() {
		setProtocolFactory(new TCompactProtocol.Factory());
	}

}
