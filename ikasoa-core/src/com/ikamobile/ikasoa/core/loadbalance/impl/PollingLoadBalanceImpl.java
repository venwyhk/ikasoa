package com.ikamobile.ikasoa.core.loadbalance.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.loadbalance.LoadBalance;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.utils.StringUtil;

/**
 * 轮询负载均衡实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class PollingLoadBalanceImpl implements LoadBalance {

	private static final Logger LOG = LoggerFactory.getLogger(PollingLoadBalanceImpl.class);

	/**
	 * 临时记数器
	 */
	private int i, j = 0;

	/**
	 * 服务器地址和权重列表
	 */
	private List<ServerInfo> serverInfoList;

	/**
	 * 当前服务器地址
	 */
	private ServerInfo serverInfo;

	public PollingLoadBalanceImpl(List<ServerInfo> serverInfoList) {
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
			throw new STException("Get server host failed !");
		}
		ServerInfo serverInfo = serverInfoList.get(i);
		if (serverInfo == null || StringUtil.isEmpty(serverInfo.getHost()) || serverInfo.getWeightNumber() < 0) {
			throw new STException("serverInfo error !");
		}
		this.serverInfo = serverInfo;
		int weightNumber = serverInfo.getWeightNumber();
		LOG.debug("serverHost : " + serverInfo.getHost() + ", weightNumber : " + weightNumber);
		if (size > i + 1) {
			if (weightNumber > j) {
				j++;
			} else {
				i++;
				j = 0;
			}
		} else {
			if (weightNumber > j) {
				j++;
			} else {
				i = j = 0;
			}
		}
		return getServerInfo();
	}

}
