package com.ikasoa.core;

/**
 * 服务器可用性检测接口
 * <p>
 * 用于建立与服务器连接前检测服务器是否可用.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public interface ServerCheck {

	/**
	 * 服务器检测
	 * 
	 * @param serverHost
	 *            服务器地址
	 * @param serverPort
	 *            服务器端口
	 * @return boolean 服务器是否可用
	 */
	public boolean check(String serverHost, int serverPort);

}
