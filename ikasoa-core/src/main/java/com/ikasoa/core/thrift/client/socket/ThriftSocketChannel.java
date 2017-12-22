package com.ikasoa.core.thrift.client.socket;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.apache.thrift.transport.TNonblockingSocket;

/**
 * SocketChannel对象
 * <p>
 * 继承于<i>TNonblockingSocket</i>.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class ThriftSocketChannel extends TNonblockingSocket {

	public ThriftSocketChannel(SocketChannel socket) throws IOException {
		super(socket);
	}

	public ThriftSocketChannel(String host, int port) throws IOException {
		super(host, port);
	}

	public ThriftSocketChannel(String host, int port, int timeout) throws IOException {
		super(host, port, timeout);
	}

}
