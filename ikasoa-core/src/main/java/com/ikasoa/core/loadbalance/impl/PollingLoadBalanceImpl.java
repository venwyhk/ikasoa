package com.ikasoa.core.loadbalance.impl;

import java.util.List;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.core.utils.StringUtil;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 轮询负载均衡实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
@Slf4j
public class PollingLoadBalanceImpl implements LoadBalance {

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
		init(serverInfoList);
	}

	public PollingLoadBalanceImpl(List<ServerInfo> serverInfoList, String context) {
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
	public synchronized ServerInfo next() throws IkasoaException {
		int size = serverInfoList.size();
		if (size == 0)
			throw new IkasoaException("Get server host failed !");
		ServerInfo serverInfo = serverInfoList.get(i);
		if (ObjectUtil.isNull(serverInfo) || StringUtil.isEmpty(serverInfo.getHost())
				|| serverInfo.getWeightNumber() < 0)
			throw new IkasoaException("serverInfo error !");
		this.serverInfo = serverInfo;
		int weightNumber = serverInfo.getWeightNumber();
		log.debug("ServerHost is : {}, WeightNumber is : {}", serverInfo.getHost(), weightNumber);
		if (size > i + 1)
			if (weightNumber > j)
				j++;
			else {
				i++;
				j = 0;
			}
		else if (weightNumber > j)
			j++;
		else
			i = j = 0;
		return getServerInfo();
	}

}
