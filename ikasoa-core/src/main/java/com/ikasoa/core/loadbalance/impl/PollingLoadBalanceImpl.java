package com.ikasoa.core.loadbalance.impl;

import java.util.List;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.Node;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.ObjectUtil;

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
public class PollingLoadBalanceImpl<S> implements LoadBalance<S> {

	/**
	 * 临时记数器
	 */
	private int i, j = 0;

	/**
	 * 服务器节点和权重列表
	 */
	private List<Node<S>> nodeList;

	/**
	 * 当前服务器节点
	 */
	private Node<S> node;

	public PollingLoadBalanceImpl(List<Node<S>> nodeList) {
		init(nodeList);
	}

	public PollingLoadBalanceImpl(List<Node<S>> nodeList, String context) {
		init(nodeList);
	}

	private void init(List<Node<S>> nodeList) {
		if (ListUtil.isEmpty(nodeList))
			throw new IllegalArgumentException("'nodeList' is null !");
		this.nodeList = nodeList;
		try {
			next();
		} catch (IkasoaException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Node<S> getNode() {
		if (ObjectUtil.isNull(node))
			log.error("'node' is null !");
		return node;
	}

	@Override
	public synchronized Node<S> next() throws IkasoaException {
		int size = nodeList.size();
		if (size == 0)
			throw new IkasoaException("Get node failed !");
		Node<S> node = nodeList.get(i);
		if (ObjectUtil.isNull(node) || node.getWeightNumber() < 0)
			throw new IkasoaException("Node error !");
		this.node = node;
		int weightNumber = node.getWeightNumber();
		log.debug("Node is : {}, WeightNumber is : {}", node.getValue().toString(), weightNumber);
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
		return this.getNode();
	}

}
