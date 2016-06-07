package com.ikamobile.ikasoa.example;

import com.ikamobile.ikasoa.rpc.DefaultIkasoaFactory;
import com.ikamobile.ikasoa.rpc.IkasoaException;
import com.ikamobile.ikasoa.rpc.IkasoaFactory;
import com.ikamobile.ikasoa.rpc.IkasoaServer;

/**
 * IKASOA的简单例子执行程序
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class Main {
	public static void main(String[] args) {
		IkasoaFactory ikasoaFactory = new DefaultIkasoaFactory();
		try {
			// 获取Ikasoa服务
			IkasoaServer ikasoaServer = ikasoaFactory.getIkasoaServer(ExampleServiceImpl.class, 9999);
			// 启动服务
			ikasoaServer.run();
			// 客户端获取远程接口实现
			ExampleService es = ikasoaFactory.getIkasoaClient(ExampleService.class, "localhost", 9999);
			// 客户端输出结果
			System.out.println(es.findVO(1).getString());
			// 停止服务
			ikasoaServer.stop();
		} catch (IkasoaException e) {
			e.printStackTrace();
		}
	}
}
