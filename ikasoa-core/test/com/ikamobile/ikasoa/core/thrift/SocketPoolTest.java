package com.ikamobile.ikasoa.core.thrift;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * Socket连接池单元测试
 */
public class SocketPoolTest extends TestCase {

	private static final String HOST = "localhost";

	private static final int PORT = 38001;

	@Test
	public void testBuildThriftSocket() {
		ThriftSocketThreadTest tstt = new ThriftSocketThreadTest();
		tstt.start();
		ThriftSocket ts = SocketPool.buildThriftSocket(HOST, PORT);
		assertNotNull(ts);
		try {
			ts.open();
			Socket s = ts.getSocket();
			assertEquals(s.getInetAddress().getHostName(), HOST);
			assertEquals(s.getPort(), PORT);
		} catch (TTransportException e) {
			fail();
		} finally {
			ts.close();
		}
		tstt.interrupt();
	}

	private class ThriftSocketThreadTest extends Thread {
		@SuppressWarnings("resource")
		public void run() {
			try {
				new ServerSocket(PORT).accept();
			} catch (IOException e) {
			}
		}
	}

}
