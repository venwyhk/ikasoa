package com.ikamobile.ikasoa.example.rpc;

/**
 * IKASOA的简单例子执行程序
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class Main {

	public static void main(String[] args) {
		try {
			// 启动服务
			Server.start();
			Thread.sleep(100);
			// 客户端调用
			Client.call();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 停止服务
			Server.stop();
		}
	}
}
