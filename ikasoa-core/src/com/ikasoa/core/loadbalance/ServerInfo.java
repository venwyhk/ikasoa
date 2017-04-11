package com.ikasoa.core.loadbalance;

/**
 * 服务器信息
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.4
 */
public class ServerInfo {

	// 服务器地址
	private String host;

	// 服务器端口
	private int port;

	// 权重值 (如果有用负载均衡,那么此值越大,被调用的机率越高)
	private int weightNumber = 0;

	public ServerInfo(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public ServerInfo(String host, int port, int weightNumber) {
		this.host = host;
		this.port = port;
		this.weightNumber = weightNumber;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getWeightNumber() {
		return weightNumber;
	}

	public void setWeightNumber(int weightNumber) {
		this.weightNumber = weightNumber;
	}

	public String toString() {
		return new StringBuilder("host : ").append(host).append(" , port : ").append(port).append(" , weightNumber : ")
				.append(weightNumber).toString();
	}

}
