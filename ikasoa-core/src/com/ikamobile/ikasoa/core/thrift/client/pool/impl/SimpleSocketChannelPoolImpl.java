package com.ikamobile.ikasoa.core.thrift.client.pool.impl;

import java.io.IOException;

import com.ikamobile.ikasoa.core.thrift.client.pool.SocketChannelPool;
import com.ikamobile.ikasoa.core.thrift.client.socket.ThriftSocketChannel;

/**
 * SocketChannel连接池简单实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class SimpleSocketChannelPoolImpl implements SocketChannelPool {

	// 连接超时时间
	private int time = 0;

	public SimpleSocketChannelPoolImpl() {
	}

	public SimpleSocketChannelPoolImpl(int time) {
		this.time = time;
	}

	@Override
	public ThriftSocketChannel buildThriftSocketChannel(String host, int port) throws IOException {
		return new ThriftSocketChannel(host, port, time);
	}

	@Override
	public void releaseThriftSocketChannel(ThriftSocketChannel thriftSocketChannel, String host, int port) {
		if (thriftSocketChannel != null) {
			thriftSocketChannel.close();
		}
	}

	@Override
	public void releaseAllThriftSocketChannel() {
	}

}
