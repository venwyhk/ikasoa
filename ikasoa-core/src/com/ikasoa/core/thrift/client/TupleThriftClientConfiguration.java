package com.ikasoa.core.thrift.client;

import org.apache.thrift.protocol.TTupleProtocol;

/**
 * Thrift客户端配置 (<i>TTupleProtocol</i>方式)
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class TupleThriftClientConfiguration extends ThriftClientConfiguration {

	public TupleThriftClientConfiguration() {
		setProtocolFactory(new TTupleProtocol.Factory());
	}

}