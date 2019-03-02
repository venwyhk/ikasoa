package com.ikasoa.core.thrift;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TTransportFactory;

import lombok.Data;

/**
 * Thrift服务配置抽象类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Data
public abstract class AbstractThriftConfiguration {

	/**
	 * Thrift服务传输协议工厂
	 */
	private TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();

	/**
	 * Thrift传输类型工厂
	 */
	private TTransportFactory transportFactory = new TFramedTransport.Factory();

	/**
	 * SSL参数(Null表示不使用SSL)
	 */
	private TSSLTransportParameters sslTransportParameters = null;

}
