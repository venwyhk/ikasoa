package com.ikamobile.ikasoa.core.thrift.client.impl;

import java.io.IOException;

import org.apache.thrift.transport.TTransport;

import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.loadbalance.LoadBalance;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.client.pool.SocketChannelPool;
import com.ikamobile.ikasoa.core.thrift.client.socket.ThriftSocketChannel;

/**
 * 负载均衡NonblockingThrift客户端实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class LoadBalanceNonblockingThriftClientImpl extends LoadBalanceThriftClientImpl {

	protected ThreadLocal<ThriftSocketChannel> socketChannelThread = new ThreadLocal<>();

	public LoadBalanceNonblockingThriftClientImpl() {
		// Do nothing
	}

	public LoadBalanceNonblockingThriftClientImpl(LoadBalance loadBalance, ThriftClientConfiguration configuration) {
		super(loadBalance, configuration);
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
		socketChannelThread.get().close();
	}
}
