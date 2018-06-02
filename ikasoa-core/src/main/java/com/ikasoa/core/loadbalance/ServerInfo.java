package com.ikasoa.core.loadbalance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 服务器信息
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.4
 */
@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class ServerInfo {

	/**
	 * 服务器地址
	 */
	@NonNull
	private String host;

	/**
	 * 服务器端口
	 */
	@NonNull
	private int port;

	/**
	 * 权重值 (如果有用负载均衡,那么此值越大,被调用的机率越高)
	 */
	private int weightNumber = 0;

	public String toString() {
		return new StringBuilder("host : ").append(host).append(" , port : ").append(port).append(" , weightNumber : ")
				.append(weightNumber).toString();
	}

}
