package com.ikasoa.core;

/**
 * 服务器可用性检测接口
 * <p>
 * 用于建立与服务器连接前检测服务器是否可用.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@FunctionalInterface
public interface ServerCheck {

	/**
	 * 服务器检测
	 * 
	 * @param serverInfo
	 *            服务器信息
	 * @return boolean 服务器是否可用
	 */
	boolean check(ServerInfo serverInfo);

}
