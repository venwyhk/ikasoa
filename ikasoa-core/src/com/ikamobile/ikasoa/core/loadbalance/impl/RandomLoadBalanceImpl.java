package com.ikamobile.ikasoa.core.loadbalance.impl;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.loadbalance.LoadBalance;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;

/**
 * 随机负载均衡实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class RandomLoadBalanceImpl implements LoadBalance {

	private static final Logger LOG = LoggerFactory.getLogger(RandomLoadBalanceImpl.class);

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
		if (serverInfoList == null || serverInfoList.size() == 0) {
			throw new RuntimeException("serverInfoList is null !");
		}
		this.serverInfoList = serverInfoList;
		try {
			next();
		} catch (STException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ServerInfo getServerInfo() {
		if (serverInfo == null) {
			LOG.error("serverInfo is null !");
		}
		return serverInfo;
	}

	@Override
	public ServerInfo next() throws STException {
		int size = serverInfoList.size();
		if (size == 0) {
			throw new STException("Get server info failed !");
		}
		serverInfo = serverInfoList.get(new Random().nextInt(size) % (size + 1));
		LOG.debug("serverInfo : " + serverInfo);
		return getServerInfo();
	}

}
