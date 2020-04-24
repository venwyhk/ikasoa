package com.ikasoa.core.thrift.client;

import java.util.List;

import com.ikasoa.core.ServerInfo;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.Node;

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
	 * 获取默认的ThriftClient对象
	 * 
	 * @param serverInfo
	 *            服务器信息
	 * @return ThriftClient Thrift客户端
	 */
	ThriftClient getThriftClient(final ServerInfo serverInfo);

	/**
	 * 获取带负载均衡的ThriftClient对象
	 * 
	 * @param serverInfoNodeList
	 *            服务器节点列表
	 * @return ThriftClient Thrift客户端
	 */
	ThriftClient getThriftClient(final List<Node<ServerInfo>> serverInfoNodeList);

	/**
	 * 获取带负载均衡的ThriftClient对象
	 * 
	 * @param serverInfoNodeList
	 *            服务器节点列表
	 * @param loadBalance
	 *            负载均衡实现类
	 * @return ThriftClient Thrift客户端
	 */
	ThriftClient getThriftClient(final List<Node<ServerInfo>> serverInfoNodeList,
			final LoadBalance<ServerInfo> loadBalance);

	/**
	 * 获取带负载均衡的ThriftClient对象
	 * 
	 * @param serverInfoNodeList
	 *            服务器节点列表
	 * @param loadBalance
	 *            负载均衡实现类
	 * @param param
	 *            负载均衡实现参数
	 * @return ThriftClient Thrift客户端
	 */
	ThriftClient getThriftClient(final List<Node<ServerInfo>> serverInfoNodeList,
			final LoadBalance<ServerInfo> loadBalance, final String param);

}
