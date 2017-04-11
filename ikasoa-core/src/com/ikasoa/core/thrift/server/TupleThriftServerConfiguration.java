package com.ikasoa.core.thrift.server;

import org.apache.thrift.protocol.TTupleProtocol;

/**
 * Thrift服务器配置 (<i>TTupleProtocol</i>方式)
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class TupleThriftServerConfiguration extends ThriftServerConfiguration {

	public TupleThriftServerConfiguration() {
		setProtocolFactory(new TTupleProtocol.Factory());
	}

}