package com.ikasoa.core.thrift.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import com.ikasoa.core.Server;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;

/**
 * Thrift服务器接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface ThriftServer extends Server {

	/**
	 * Thrift服务传输类型
	 * <p>
	 * 通常为<i>org.apache.thrift.transport.TServerSocket</i>
	 * ,但如果采用NonblockingServer来实现Thrift服务,这里就需要用 <i>TNonblockingServerSocket</i>. .
	 * 
	 * @return TServerTransport 服务传输类型
	 * @exception TTransportException
	 *                TransportExceptions
	 */
	TServerTransport getTransport() throws TTransportException;

	/**
	 * Thrift服务器是否运行
	 * 
	 * @return boolean 服务器是否运行
	 */
	boolean isServing();

	/**
	 * 获取Thrift服务名称
	 * <p>
	 * 这里的服务名称仅作为标记,方便记录和检索.在客户端依然采用<i>serverHost</i>和 <i>serverPort</i> 来定位服务.
	 * 
	 * @return String 服务名称
	 */
	String getServerName();

	/**
	 * 获取Thrift服务器端口
	 * <p>
	 * 端口不能重复,且端口号需大于1024.
	 * 
	 * @return int 服务器端口
	 */
	int getServerPort();

	/**
	 * 获取Thrift服务配置
	 * 
	 * @return ThriftServerConfiguration 服务配置
	 */
	ThriftServerConfiguration getThriftServerConfiguration();

	/**
	 * 获取Thrift处理器
	 * 
	 * @return TProcessor Thrift处理器
	 */
	TProcessor getProcessor();

}