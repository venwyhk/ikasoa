package com.ikamobile.ikasoa.example.rpc;

import com.ikamobile.ikasoa.rpc.DefaultIkasoaFactory;
import com.ikamobile.ikasoa.rpc.IkasoaException;
import com.ikamobile.ikasoa.rpc.IkasoaServer;

/**
 * IKASOA服务端例子程序
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3
 */
public class Server {

	private static IkasoaServer ikasoaServer;

	public static void main(String[] args) {
		start();
	}

	// 启动服务
	public static void start() {
		try {
			if (ikasoaServer == null) {
				ikasoaServer = new DefaultIkasoaFactory().getIkasoaServer(ExampleServiceImpl.class, 9999);
			}
			ikasoaServer.run();
		} catch (IkasoaException e) {
			e.printStackTrace();
		}
	}

	// 停止服务
	public static void stop() {
		if (ikasoaServer != null && ikasoaServer.isServing()) {
			ikasoaServer.stop();
		}
	}
}
