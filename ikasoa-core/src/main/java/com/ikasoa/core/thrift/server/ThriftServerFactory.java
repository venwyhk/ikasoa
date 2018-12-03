package com.ikasoa.core.thrift.server;

import java.util.Map;

import org.apache.thrift.TProcessor;

import com.ikasoa.core.thrift.service.Service;

/**
 * Thrift服务端工厂接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public interface ThriftServerFactory {

	/**
	 * 获取默认的ThriftServer对象
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param processor
	 *            Thrift处理器
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getThriftServer(final String serverName, final int serverPort, final TProcessor processor);

	/**
	 * 获取NIO的ThriftServer对象
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param processor
	 *            Thrift处理器
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getNonblockingThriftServer(final String serverName, final int serverPort, final TProcessor processor);

	/**
	 * 获取默认的ThriftServer对象
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param service
	 *            通用服务
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getThriftServer(final String serverName, final int serverPort, final Service service);

	/**
	 * 获取NIO的ThriftServer对象
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param service
	 *            通用服务
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getNonblockingThriftServer(final String serverName, final int serverPort, final Service service);

	/**
	 * 获取默认的ThriftServer对象
	 * 
	 * @param serverPort
	 *            服务器端口
	 * @param service
	 *            通用服务
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getThriftServer(final int serverPort, final Service service);

	/**
	 * 获取NIO的ThriftServer对象
	 * 
	 * @param serverPort
	 *            服务器端口
	 * @param service
	 *            通用服务
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getNonblockingThriftServer(final int serverPort, final Service service);

	/**
	 * 获取默认的ThriftServer对象
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param serviceMap
	 *            Thrift服务器集合
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getThriftServer(final String serverName, final int serverPort, final Map<String, Service> serviceMap);

	/**
	 * 获取NIO的ThriftServer对象
	 * 
	 * @param serverPort
	 *            服务器端口
	 * @param serviceMap
	 *            Thrift服务器集合
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getNonblockingThriftServer(final int serverPort, final Map<String, Service> serviceMap);

	/**
	 * 获取默认的ThriftServer对象
	 * 
	 * @param serverPort
	 *            服务器端口
	 * @param serviceMap
	 *            Thrift服务器集合
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getThriftServer(final int serverPort, final Map<String, Service> serviceMap);

	/**
	 * 获取NIO的ThriftServer对象
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param serviceMap
	 *            Thrift服务器集合
	 * @return ThriftServer Thrift服务器
	 */
	ThriftServer getNonblockingThriftServer(final String serverName, final int serverPort,
			final Map<String, Service> serviceMap);

}
