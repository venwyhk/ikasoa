package com.ikasoa.core.thrift.client.pool.impl;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.client.pool.ClientSocketPoolParameters;
import com.ikasoa.core.thrift.client.pool.SocketPool;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;
import com.ikasoa.core.utils.ObjectUtil;

import lombok.NoArgsConstructor;

/**
 * Socket连接池实现
 * <p>
 * 此实现不会使用连接池,每次都建立新的连接.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.4
 */
@NoArgsConstructor
public class NoSocketPoolImpl implements SocketPool {

	@Override
	public ThriftSocket buildThriftSocket(ClientSocketPoolParameters parameters) throws IkasoaException {
		return parameters.buildClientThriftSocket();
	}

	@Override
	public void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port) {
		if (ObjectUtil.isNotNull(thriftSocket))
			thriftSocket.close();
	}

	@Override
	public void releaseAllThriftSocket() {
		// Do nothing
	}

}
