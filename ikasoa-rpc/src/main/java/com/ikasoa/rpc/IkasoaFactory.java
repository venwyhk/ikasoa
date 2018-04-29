package com.ikasoa.rpc;

import java.util.List;
import java.util.Map;

import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.thrift.Factory;
import com.ikasoa.core.thrift.service.Service;

/**
 * IKASOA工厂接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface IkasoaFactory extends Factory {

	/**
	 * 获取IKASOA客户端
	 * 
	 * @param <T>
	 *            IKASOA客户端类型
	 * @param iClass
	 *            应用接口类
	 * @param serverHost
	 *            服务地址
	 * @param serverPort
	 *            服务端口
	 * @return T IKASOA客户端
	 */
	<T> T getIkasoaClient(Class<T> iClass, String serverHost, int serverPort);

	/**
	 * 获取IKASOA客户端
	 * 
	 * @param <T>
	 *            IKASOA客户端类型
	 * @param iClass
	 *            应用接口类
	 * @param serverInfoList
	 *            服务信息列表(将会通过默认负载均衡策略进行调用)
	 * @return T IKASOA客户端
	 */
	<T> T getIkasoaClient(Class<T> iClass, List<ServerInfo> serverInfoList);

	/**
	 * 获取IKASOA客户端
	 * 
	 * @param <T>
	 *            IKASOA客户端类型
	 * @param iClass
	 *            应用接口类
	 * @param serverInfoList
	 *            服务信息列表(将会通过默认负载均衡策略进行调用)
	 * @param loadBalanceClass
	 *            负载均衡实现类
	 * @return T IKASOA客户端
	 */
	<T> T getIkasoaClient(Class<T> iClass, List<ServerInfo> serverInfoList, Class<LoadBalance> loadBalanceClass);

	/**
	 * 获取IKASOA客户端
	 * 
	 * @param <T>
	 *            IKASOA客户端类型
	 * @param iClass
	 *            应用接口类
	 * @param serverInfoList
	 *            服务信息列表(将会通过默认负载均衡策略进行调用)
	 * @param loadBalanceClass
	 *            负载均衡实现类
	 * @param param
	 *            自定义参数 (比如负载均衡的hash值等)
	 * @return T IKASOA客户端
	 */
	<T> T getIkasoaClient(Class<T> iClass, List<ServerInfo> serverInfoList, Class<LoadBalance> loadBalanceClass,
			String param);

	/**
	 * 获取IKASOA服务端
	 * 
	 * @param implClass
	 *            应用接口实现类
	 * @param serverPort
	 *            服务端口
	 * @return IkasoaServer IKASOA服务端
	 * @exception RpcException
	 *                异常
	 */
	IkasoaServer getIkasoaServer(Class<?> implClass, int serverPort) throws RpcException;

	/**
	 * 获取IKASOA服务端
	 * 
	 * @param implWrapper
	 *            接口实现对象包装器
	 * @param serverPort
	 *            服务端口
	 * @return IkasoaServer IKASOA服务端
	 * @exception RpcException
	 *                异常
	 */
	IkasoaServer getIkasoaServer(ImplWrapper implWrapper, int serverPort) throws RpcException;

	/**
	 * 获取IKASOA服务端
	 * 
	 * @param implWrapperList
	 *            接口实现对象包装器
	 * @param serverPort
	 *            服务端口
	 * @return IkasoaServer IKASOA服务端
	 * @exception RpcException
	 *                异常
	 */
	IkasoaServer getIkasoaServer(List<ImplWrapper> implWrapperList, int serverPort) throws RpcException;

	/**
	 * 获取IKASOA服务端
	 * 
	 * @param serverPort
	 *            服务端口
	 * @param serviceMap
	 *            服务Map
	 * @return IkasoaServer IKASOA服务端
	 * @exception RpcException
	 *                异常
	 */
	IkasoaServer getIkasoaServer(int serverPort, Map<String, Service> serviceMap) throws RpcException;

}
