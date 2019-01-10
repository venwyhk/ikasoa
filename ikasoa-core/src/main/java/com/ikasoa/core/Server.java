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

	/**
	 * Thrift服务器是否运行
	 * 
	 * @return boolean 服务器是否运行
	 */
	boolean isServing();

	/**
	 * 获取Thrift服务名称
	 * <p>
	 * 这里的服务名称仅作为标记,方便记录和检索.在客户端依然采用<i>serverHost</i>和 <i>serverPort</i> 来定位服务.
	 * 
	 * @return String 服务名称
	 */
	String getServerName();

	/**
	 * 获取Thrift服务器端口
	 * <p>
	 * 端口不能重复,且端口号需大于1024.
	 * 
	 * @return int 服务器端口
	 */
	int getServerPort();

}
