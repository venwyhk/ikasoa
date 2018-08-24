package com.ikasoa.rpc;

import java.util.List;
import java.util.Map;

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
	 * 获取IKASOA接口实例
	 * 
	 * @param <T>
	 *            接口类型
	 * @param iClass
	 *            应用接口类
	 * @return T IKASOA接口实例
	 */
	<T> T getInstance(Class<T> iClass);

	/**
	 * 获取IKASOA接口实例
	 * 
	 * @param <T>
	 *            接口类型
	 * @param iClass
	 *            应用接口类
	 * @param serverInfoWrapper
	 *            服务信息包装器
	 * @return T IKASOA接口实例
	 */
	<T> T getInstance(Class<T> iClass, ServerInfoWrapper serverInfoWrapper);

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
	 * @param serverName
	 *            服务名称
	 * @param implClass
	 *            应用接口实现类
	 * @param serverPort
	 *            服务端口
	 * @return IkasoaServer IKASOA服务端
	 * @exception RpcException
	 *                异常
	 */
	IkasoaServer getIkasoaServer(String serverName, Class<?> implClass, int serverPort) throws RpcException;

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
	 * @param serverName
	 *            服务名称
	 * @param implWrapper
	 *            接口实现对象包装器
	 * @param serverPort
	 *            服务端口
	 * @return IkasoaServer IKASOA服务端
	 * @exception RpcException
	 *                异常
	 */
	IkasoaServer getIkasoaServer(String serverName, ImplWrapper implWrapper, int serverPort) throws RpcException;

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
	 * @param serverName
	 *            服务名称
	 * @param implWrapperList
	 *            接口实现对象包装器
	 * @param serverPort
	 *            服务端口
	 * @return IkasoaServer IKASOA服务端
	 * @exception RpcException
	 *                异常
	 */
	IkasoaServer getIkasoaServer(String serverName, List<ImplWrapper> implWrapperList, int serverPort)
			throws RpcException;

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

	/**
	 * 获取IKASOA服务端
	 * 
	 * @param serverName
	 *            服务名称
	 * @param serverPort
	 *            服务端口
	 * @param serviceMap
	 *            服务Map
	 * @return IkasoaServer IKASOA服务端
	 * @exception RpcException
	 *                异常
	 */
	IkasoaServer getIkasoaServer(String serverName, int serverPort, Map<String, Service> serviceMap)
			throws RpcException;

}
