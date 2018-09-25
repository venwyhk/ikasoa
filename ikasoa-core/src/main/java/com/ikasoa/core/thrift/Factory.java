package com.ikasoa.core.thrift;

import java.util.List;
import java.util.Map;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TNonblockingTransport;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.service.AsyncService;
import com.ikasoa.core.thrift.service.Service;

/**
 * 通用工厂接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public interface Factory {

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

	/**
	 * 获取默认的ThriftClient对象
	 * 
	 * @param serverHost
	 *            服务器地址
	 * @param serverPort
	 *            服务器端口
	 * @return ThriftClient Thrift客户端
	 */
	ThriftClient getThriftClient(final String serverHost, final int serverPort);

	/**
	 * 获取带负载均衡的ThriftClient对象
	 * 
	 * @param serverInfoList
	 *            服务器信息列表
	 * @return ThriftClient Thrift客户端
	 */
	ThriftClient getThriftClient(final List<ServerInfo> serverInfoList);

	/**
	 * 获取带负载均衡的ThriftClient对象
	 * 
	 * @param serverInfoList
	 *            服务器信息列表
	 * @param loadBalance
	 *            负载均衡实现类
	 * @return ThriftClient Thrift客户端
	 */
	ThriftClient getThriftClient(final List<ServerInfo> serverInfoList, final LoadBalance loadBalance);

	/**
	 * 获取带负载均衡的ThriftClient对象
	 * 
	 * @param serverInfoList
	 *            服务器信息列表
	 * @param loadBalance
	 *            负载均衡实现类
	 * @param param
	 *            负载均衡实现参数
	 * @return ThriftClient Thrift客户端
	 */
	ThriftClient getThriftClient(final List<ServerInfo> serverInfoList, final LoadBalance loadBalance,
			final String param);

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
