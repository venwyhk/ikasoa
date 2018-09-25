package com.ikasoa.core;

/**
 * 服务器接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.5.5
 */
public interface Server {

	/**
	 * 运行Thrift服务器
	 */
	void run();

	/**
	 * 停止Thrift服务器
	 */
	void stop();

}
