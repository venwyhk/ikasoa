package com.ikasoa.core.loadbalance;

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

	/**
	 * 权重值 (如果有用负载均衡,那么此值越大,被调用的机率越高)
	 */
	private int weightNumber = 0;

	public ServerInfo(String host, int port) {
		this.host = host;
		this.port = port;
	}

}
