package com.ikasoa.core.thrift.client.pool.impl;

import com.ikasoa.core.thrift.client.pool.SocketPool;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;

/**
 * Socket连接池实现
 * <p>
 * 此实现不会使用连接池,每次都建立新的连接.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.4
 */
public class NoSocketPoolImpl implements SocketPool {

	/**
	 * 连接超时时间
	 */
	private int time = 0;

	public NoSocketPoolImpl() {
		// Do nothing
	}

	public NoSocketPoolImpl(int time) {
		this.time = time;
	}

	@Override
	public ThriftSocket buildThriftSocket(String host, int port) {
		return new ThriftSocket(host, port, time);
	}

	@Override
	public void releaseThriftSocket(ThriftSocket thriftSocket) {
		if (thriftSocket != null)
			thriftSocket.close();
	}

	@Override
	public void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port) {
		if (thriftSocket != null)
			thriftSocket.close();
	}

	@Override
	public void releaseAllThriftSocket() {
		// Do nothing
	}

}
