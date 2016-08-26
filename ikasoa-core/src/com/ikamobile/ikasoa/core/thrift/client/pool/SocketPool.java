package com.ikamobile.ikasoa.core.thrift.client.pool;

import com.ikamobile.ikasoa.core.thrift.ThriftSocket;

/**
 * Socket连接池
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.2
 */
public interface SocketPool {

	/**
	 * 从连接池中获取一个空闲的ThriftSocket连接
	 * 
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 * @return ThriftSocket ThriftSocket连接对象
	 */
	public ThriftSocket buildThriftSocket(String host, int port);

	/**
	 * 回收ThriftSocket连接
	 * <p>
	 * 将会从Socket中获取服务器的地址和端口,以判断具体的池.
	 * 
	 * @param thriftSocket
	 *            待回收的ThriftSocket连接对象
	 */
	public void releaseThriftSocket(ThriftSocket thriftSocket);

	/**
	 * 回收ThriftSocket连接
	 * 
	 * @param thriftSocket
	 *            待回收的ThriftSocket连接对象
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 */
	public void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port);

	/**
	 * 回收所有ThriftSocket连接
	 */
	public void releaseAllThriftSocket();

}
