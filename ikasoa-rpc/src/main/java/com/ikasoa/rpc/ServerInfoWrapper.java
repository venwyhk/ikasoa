package com.ikasoa.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;

import lombok.Data;

/**
 * 服务信息包装器
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.2
 */
@Data
public class ServerInfoWrapper {

	/**
	 * 服务信息列表 如果列表长度大于1,则会通过默认负载均衡策略进行调用
	 */
	private List<ServerInfo> serverInfoList = new ArrayList<>();

	/**
	 * 负载均衡实现类
	 */
	private Class<LoadBalance> loadBalanceClass;

	/**
	 * 自定义参数 (比如负载均衡的hash值等)
	 */
	private String param;

	public ServerInfoWrapper(String host, int port) {
		serverInfoList.add(new ServerInfo(host, port));
	}

	public ServerInfoWrapper(List<ServerInfo> serverInfoList) {
		this.serverInfoList = serverInfoList;
	}

	public ServerInfoWrapper(List<ServerInfo> serverInfoList, Class<LoadBalance> loadBalanceClass) {
		this.serverInfoList = serverInfoList;
		this.loadBalanceClass = loadBalanceClass;

	}

	public ServerInfoWrapper(List<ServerInfo> serverInfoList, Class<LoadBalance> loadBalanceClass, String param) {
		this.serverInfoList = serverInfoList;
		this.loadBalanceClass = loadBalanceClass;
		this.param = param;

	}

	public boolean isNotNull() {
		return !serverInfoList.isEmpty() && serverInfoList.size() > 0;
	}

	public boolean isCluster() {
		return serverInfoList.size() > 1;
	}

	public ServerInfo getServerInfo() {
		return isNotNull() ? serverInfoList.get(0) : null;
	}

	public String getHost() {
		return Optional.ofNullable(getServerInfo()).map(i -> i.getHost()).orElse(null);
	}

	public int getPort() {
		return Optional.ofNullable(getServerInfo()).map(i -> i.getPort()).orElse(0);
	}

}
