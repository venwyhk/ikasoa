package com.ikamobile.ikasoa.core.thrift;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TTransportFactory;

/**
 * Thrift服务配置抽象类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public abstract class AbstractThriftConfiguration {

	/**
	 * Thrift服务传输协议工厂
	 */
	private TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();

	/**
	 * Thrift传输类型工厂
	 * <p>
	 * 如果提供非阻塞服务,则必须为<code>new TFramedTransport.Factory()</code>.
	 */
	private TTransportFactory transportFactory = new TFramedTransport.Factory();

	public TProtocolFactory getProtocolFactory() {
		// 默认使用二进制方式
		return protocolFactory;
	}

	public void setProtocolFactory(TProtocolFactory protocolFactory) {
		this.protocolFactory = protocolFactory;
	}

	public TTransportFactory getTransportFactory() {
		return transportFactory;
	}

	public void setTransportFactory(TTransportFactory transportFactory) {
		this.transportFactory = transportFactory;
	}
	
}
