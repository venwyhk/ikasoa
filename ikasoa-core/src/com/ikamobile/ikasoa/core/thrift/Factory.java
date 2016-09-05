package com.ikamobile.ikasoa.core.thrift;

import java.util.List;
import java.util.Map;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TNonblockingTransport;

import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.loadbalance.LoadBalance;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServer;
import com.ikamobile.ikasoa.core.thrift.service.AsyncService;
import com.ikamobile.ikasoa.core.thrift.service.Service;

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
	public ThriftServer getThriftServer(String serverName, int serverPort, TProcessor processor);

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
	public ThriftServer getNonblockingThriftServer(String serverName, int serverPort, TProcessor processor);

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
	public ThriftServer getThriftServer(String serverName, int serverPort, Service service);

	/**
	 * 获取默认的ThriftServer对象
	 * 
	 * @param serverPort
	 *            服务器端口
	 * @param service
	 *            通用服务
	 * @return ThriftServer Thrift服务器
	 */
	public ThriftServer getThriftServer(int serverPort, Service service);

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
	 * @throws STException
	 *             异常
	 */
	public ThriftServer getThriftServer(String serverName, int serverPort, Map<String, Service> serviceMap)
			throws STException;

	/**
	 * 获取默认的ThriftServer对象
	 * 
	 * @param serverPort
	 *            服务器端口
	 * @param serviceMap
	 *            Thrift服务器集合
	 * @return ThriftServer Thrift服务器
	 * @throws STException
	 *             异常
	 */
	public ThriftServer getThriftServer(int serverPort, Map<String, Service> serviceMap) throws STException;

	/**
	 * 获取默认的ThriftClient对象
	 * 
	 * @param serverHost
	 *            服务器地址
	 * @param serverPort
	 *            服务器端口
	 * @return ThriftClient Thrift客户端
	 */
	public ThriftClient getThriftClient(String serverHost, int serverPort);

	/**
	 * 获取带负载均衡的ThriftClient对象
	 * 
	 * @param serverInfoList
	 *            服务器信息列表
	 * @return ThriftClient Thrift客户端
	 */
	public ThriftClient getThriftClient(List<ServerInfo> serverInfoList);

	/**
	 * 获取带负载均衡的ThriftClient对象
	 * 
	 * @param serverInfoList
	 *            服务器信息列表
	 * @param loadBalanceClass
	 *            负载均衡实现类
	 * @return ThriftClient Thrift客户端
	 */
	public ThriftClient getThriftClient(List<ServerInfo> serverInfoList, Class<LoadBalance> loadBalanceClass);

	/**
	 * 获取NonblockingThriftClient对象
	 * 
	 * @param serverHost
	 *            服务器地址
	 * @param serverPort
	 *            服务器端口
	 * @return ThriftClient Thrift客户端
	 */
	public ThriftClient getNonblockingThriftClient(String serverHost, int serverPort);

	/**
	 * 获取带负载均衡的NonblockingThriftClient对象
	 * 
	 * @param serverInfoList
	 *            服务器信息列表
	 * @return ThriftClient Thrift客户端
	 */
	public ThriftClient getNonblockingThriftClient(List<ServerInfo> serverInfoList);

	/**
	 * 获取带负载均衡的NonblockingThriftClient对象
	 * 
	 * @param serverInfoList
	 *            服务器信息列表
	 * @param loadBalanceClass
	 *            负载均衡实现类
	 * @return ThriftClient Thrift客户端
	 */
	public ThriftClient getNonblockingThriftClient(List<ServerInfo> serverInfoList,
			Class<LoadBalance> loadBalanceClass);

	/**
	 * 获取客户端Service对象
	 * <p>
	 * 如果需要使用负载均衡,则每次需要调用此方法重新获取服务对象.
	 * 
	 * @param thriftClient
	 *            Thrift客户端
	 * @return Service 通用服务
	 * @throws STException
	 *             异常
	 */
	public Service getService(ThriftClient thriftClient) throws STException;

	/**
	 * 获取客户端异步Service对象
	 * <p>
	 * 如果需要使用负载均衡,则每次需要调用此方法重新获取服务对象.
	 * 
	 * @param transport
	 *            传输对象
	 * @return AysncService 异步服务
	 * @throws STException
	 *             异常
	 */
	public AsyncService getAsyncService(TNonblockingTransport transport) throws STException;

	/**
	 * 获取客户端Service对象
	 * 
	 * @param thriftClient
	 *            Thrift客户端
	 * @param serviceName
	 *            服务名
	 * @return Service 通用服务
	 * @throws STException
	 *             异常
	 */
	public Service getService(ThriftClient thriftClient, String serviceName) throws STException;

	/**
	 * 获取客户端异步Service对象
	 * 
	 * @param transport
	 *            传输对象
	 * @param serviceName
	 *            服务名
	 * @return AysncService 异步服务
	 * @throws STException
	 *             异常
	 */
	public AsyncService getAsyncService(TNonblockingTransport transport, String serviceName) throws STException;

}
