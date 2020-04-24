package com.ikasoa.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 服务器信息
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.4
 */
@AllArgsConstructor
@Data
@ToString
public class ServerInfo {

	/**
	 * 服务器地址
	 */
	private String host;

	/**
	 * 服务器端口
	 */
	private int port;

}
