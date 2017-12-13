package com.ikasoa.example.rpc;

import com.ikasoa.rpc.DefaultIkasoaFactory;

/**
 * IKASOA服务端例子程序
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3
 */
public class Client {

//	public static void main(String[] args) {
//		call();
//	}

	public static void call() {
		// 客户端获取远程接口实现
		ExampleService es = new DefaultIkasoaFactory().getIkasoaClient(ExampleService.class, "localhost", 9999);
		// 客户端输出结果
		System.out.println(es.findVO(1).getString());
	}
}
