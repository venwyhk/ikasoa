package com.ikamobile.ikasoa.core.thrift.server;

import org.apache.thrift.TProcessor;

/**
 * 服务器切面接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.1
 */
public interface ServerAspect {

	/**
	 * 服务器启动前
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param configuration
	 *            服务器配置
	 * @param processor
	 *            Thrift处理器
	 * @param server
	 *            服务器对象
	 */
	public void beforeStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server);

	/**
	 * 服务器启动后
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param configuration
	 *            服务器配置
	 * @param processor
	 *            Thrift处理器
	 * @param server
	 *            服务器对象
	 */
	public void afterStart(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server);

	/**
	 * 服务器停止前
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param configuration
	 *            服务器配置
	 * @param processor
	 *            Thrift处理器
	 * @param server
	 *            服务器对象
	 */
	public void beforeStop(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server);

	/**
	 * 服务器停止后
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param serverPort
	 *            服务器端口
	 * @param configuration
	 *            服务器配置
	 * @param processor
	 *            Thrift处理器
	 * @param server
	 *            服务器对象
	 */
	public void afterStop(String serverName, int serverPort, ThriftServerConfiguration configuration,
			TProcessor processor, ThriftServer server);

}
