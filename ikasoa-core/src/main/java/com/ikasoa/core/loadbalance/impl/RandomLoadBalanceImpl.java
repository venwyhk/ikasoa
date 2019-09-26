package com.ikasoa.core.loadbalance.impl;

import java.util.List;
import java.util.Random;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.ObjectUtil;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 随机负载均衡实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
@Slf4j
public class RandomLoadBalanceImpl implements LoadBalance {

	/**
	 * 服务器信息列表
	 */
	private List<ServerInfo> serverInfoList;

	/**
	 * 当前服务器信息
	 */
	private ServerInfo serverInfo;

	public RandomLoadBalanceImpl(List<ServerInfo> serverInfoList) {
		init(serverInfoList);
	}

	public RandomLoadBalanceImpl(List<ServerInfo> serverInfoList, String context) {
		init(serverInfoList);
	}

	private void init(List<ServerInfo> serverInfoList) {
		if (ListUtil.isEmpty(serverInfoList))
			throw new IllegalArgumentException("'serverInfoList' is null !");
		this.serverInfoList = serverInfoList;
		try {
			next();
		} catch (IkasoaException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ServerInfo getServerInfo() {
		if (ObjectUtil.isNull(serverInfo))
			log.error("'serverInfo' is null !");
		return serverInfo;
	}

	@Override
	public ServerInfo next() throws IkasoaException {
		int size = serverInfoList.size();
		if (size == 0)
			throw new IkasoaException("Get server info failed !");
		serverInfo = serverInfoList.get(new Random().nextInt(size) % (size + 1));
		log.debug("ServerInfo is : {}", serverInfo);
		return getServerInfo();
	}

}
