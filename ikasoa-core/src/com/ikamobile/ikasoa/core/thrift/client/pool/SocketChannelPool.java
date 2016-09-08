package com.ikamobile.ikasoa.core.thrift.client.pool;

import java.io.IOException;

import com.ikamobile.ikasoa.core.thrift.client.socket.ThriftSocketChannel;

/**
 * SocketChannel连接池
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.4
 */
public interface SocketChannelPool {

	/**
	 * 从连接池中获取一个空闲的ThriftSocketChannel连接
	 * 
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 * @return ThriftSocketChannel ThriftSocketChannel连接对象
	 * @exception IOException IO异常
	 */
	public ThriftSocketChannel buildThriftSocketChannel(String host, int port) throws IOException;

	/**
	 * 回收ThriftSocket连接
	 * 
	 * @param thriftSocketChannel
	 *            待回收的ThriftSocketChannel连接对象
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 */
	public void releaseThriftSocketChannel(ThriftSocketChannel thriftSocketChannel, String host, int port);

	/**
	 * 回收所有ThriftSocket连接
	 */
	public void releaseAllThriftSocketChannel();

}
