package com.ikasoa.core.thrift.client;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import com.ikasoa.core.IkasoaException;

/**
 * Thrift客户端接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface ThriftClient extends AutoCloseable {

	/**
	 * 获取Thrift传输类型
	 * <p>
	 * 需要与服务端相匹配.
	 * 
	 * @return TTransport 传输类型
	 * @exception IkasoaException
	 *                异常
	 */
	TTransport getTransport() throws IkasoaException;

	/**
	 * 获取Thrift传输协议
	 * 
	 * @param transport
	 *            传输类型
	 * @return TProtocol 传输协议
	 */
	TProtocol getProtocol(final TTransport transport);

	/**
	 * 获取Thrift传输协议
	 * <p>
	 * 当服务器使用<i>MultiplexedProcessor</i>配置多个服务时,需要通过服务名来区分具体的服务.
	 * <p>
	 * <i>serviceName</i>与<i>MultiplexedProcessor</i>中 <i>processorMap</i>
	 * 的key相对应.
	 * 
	 * @param transport
	 *            传输类型
	 * @param serviceName
	 *            服务名
	 * @return TProtocol 传输协议
	 */
	TProtocol getProtocol(final TTransport transport, final String serviceName);

	/**
	 * 获取Thrift服务器地址
	 * 
	 * @return String 服务器地址
	 */
	String getServerHost();

	/**
	 * 获取Thrift服务器端口
	 * 
	 * @return int 服务器端口
	 */
	int getServerPort();

	/**
	 * 获取Thrift服务配置
	 * 
	 * @return ThriftClientConfiguration 服务配置
	 */
	ThriftClientConfiguration getThriftClientConfiguration();

}
