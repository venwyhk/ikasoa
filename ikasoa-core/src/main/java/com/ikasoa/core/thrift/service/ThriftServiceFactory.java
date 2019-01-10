package com.ikasoa.core.thrift.service;

import org.apache.thrift.transport.TNonblockingTransport;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.client.ThriftClient;

/**
 * Thrift服务工厂接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public interface ThriftServiceFactory {

	/**
	 * 获取客户端Service对象
	 * <p>
	 * 如果需要使用负载均衡,则每次需要调用此方法重新获取服务对象.
	 * 
	 * @param thriftClient
	 *            Thrift客户端
	 * @return Service 通用服务
	 * @throws IkasoaException
	 *             异常
	 */
	Service getService(final ThriftClient thriftClient) throws IkasoaException;

	/**
	 * 获取客户端异步Service对象
	 * <p>
	 * 如果需要使用负载均衡,则每次需要调用此方法重新获取服务对象.
	 * 
	 * @param transport
	 *            传输对象
	 * @return AysncService 异步服务
	 * @throws IkasoaException
	 *             异常
	 */
	AsyncService getAsyncService(final TNonblockingTransport transport) throws IkasoaException;

	/**
	 * 获取客户端Service对象
	 * 
	 * @param thriftClient
	 *            Thrift客户端
	 * @param serviceName
	 *            服务名
	 * @return Service 通用服务
	 * @throws IkasoaException
	 *             异常
	 */
	Service getService(final ThriftClient thriftClient, final String serviceName) throws IkasoaException;

	/**
	 * 获取客户端异步Service对象
	 * 
	 * @param transport
	 *            传输对象
	 * @param serviceName
	 *            服务名
	 * @return AysncService 异步服务
	 * @throws IkasoaException
	 *             异常
	 */
	AsyncService getAsyncService(final TNonblockingTransport transport, final String serviceName)
			throws IkasoaException;

}
