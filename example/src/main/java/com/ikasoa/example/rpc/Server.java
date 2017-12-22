package com.ikasoa.example.rpc;

import com.ikasoa.rpc.DefaultIkasoaFactory;
import com.ikasoa.rpc.IkasoaException;
import com.ikasoa.rpc.IkasoaServer;

/**
 * IKASOA服务端例子程序
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3
 */
public class Server {

	private static IkasoaServer ikasoaServer;

//	public static void main(String[] args) {
//		start();
//	}

	// 启动服务
	public static void start() {
		try {
			if (ikasoaServer == null) {
				ikasoaServer = new DefaultIkasoaFactory().getIkasoaServer(ExampleServiceImpl.class, 9999);
			}
			ikasoaServer.run();
		} catch (IkasoaException e) {
		}
	}

	// 停止服务
	public static void stop() {
		if (ikasoaServer != null && ikasoaServer.isServing()) {
			ikasoaServer.stop();
		}
	}
}
