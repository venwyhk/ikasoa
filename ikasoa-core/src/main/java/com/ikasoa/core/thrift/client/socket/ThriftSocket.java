package com.ikasoa.core.thrift.client.socket;

import java.net.Socket;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Socket对象
 * <p>
 * 继承于<i>TSocket</i>.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class ThriftSocket extends TSocket {

	public ThriftSocket(Socket socket) throws TTransportException {
		super(socket);
	}

	public ThriftSocket(String host, int port) {
		super(host, port);
	}

	public ThriftSocket(String host, int port, int timeout) {
		super(host, port, timeout);
	}

}
