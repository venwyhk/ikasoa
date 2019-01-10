package com.ikasoa.core.thrift.client;

import java.util.List;

import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;

/**
 * Thrift客户端工厂接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public interface ThriftClientFactory {

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

}
