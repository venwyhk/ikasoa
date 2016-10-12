package com.ikamobile.ikasoa.core.thrift.client.impl;

import java.io.IOException;

import org.apache.thrift.transport.TTransport;

import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.client.pool.SocketChannelPool;
import com.ikamobile.ikasoa.core.thrift.client.socket.ThriftSocketChannel;

/**
 * NonblockingThrift客户端实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class NonblockingThriftClientImpl extends DefaultThriftClientImpl {

	protected ThreadLocal<ThriftSocketChannel> socketChannelThread = new ThreadLocal<>();

	public NonblockingThriftClientImpl() {
		// Do nothing
	}

	public NonblockingThriftClientImpl(String serverHost, int serverPort, ThriftClientConfiguration configuration) {
		super(serverHost, serverPort, configuration);
	}

	@Override
	public TTransport getTransport(String serverHost, int serverPort) throws STException {
		try {
			SocketChannelPool pool = getThriftClientConfiguration().getSocketChannelPool();
			socketChannelThread.set(pool.buildThriftSocketChannel(serverHost, serverPort));
			return getThriftClientConfiguration().getTransportFactory().getTransport(socketChannelThread.get());
		} catch (IOException e) {
			throw new STException(e);
		}
	}

	@Override
	public void close() {
		getThriftClientConfiguration().getSocketChannelPool().releaseThriftSocketChannel(socketChannelThread.get(),
				getServerHost(), getServerPort());
	}

}
