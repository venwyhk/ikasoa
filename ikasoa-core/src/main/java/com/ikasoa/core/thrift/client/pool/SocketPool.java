package com.ikasoa.core.thrift.client.pool;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;
import com.ikasoa.core.utils.ObjectUtil;

/**
 * Socket连接池
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.2
 */
public interface SocketPool {

	/**
	 * 连接池默认大小
	 */
	byte defaultSize = 0x10;

	/**
	 * 从连接池中获取一个空闲的ThriftSocket连接
	 * 
	 * @param parameters
	 *            Socket连接池参数对象
	 * @return ThriftSocket ThriftSocket连接对象
	 * @throws IkasoaException
	 *             异常
	 */
	ThriftSocket buildThriftSocket(ClientSocketPoolParameters parameters) throws IkasoaException;

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
	void releaseThriftSocket(final ThriftSocket thriftSocket, final String host, final int port);

	default void releaseThriftSocket(ThriftSocket thriftSocket) {
		if (ObjectUtil.isNotNull(thriftSocket))
			if (ObjectUtil.isNotNull(thriftSocket.getSocket())
					&& ObjectUtil.isNotNull(thriftSocket.getSocket().getInetAddress()))
				releaseThriftSocket(thriftSocket, thriftSocket.getSocket().getInetAddress().getHostName(),
						thriftSocket.getSocket().getPort());
			else
				thriftSocket.close();
	}

	/**
	 * 回收所有ThriftSocket连接
	 */
	void releaseAllThriftSocket();

}
