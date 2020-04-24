package com.ikasoa.core.loadbalance.impl;

import java.util.List;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.Node;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.NumberUtil;
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
public class RandomLoadBalanceImpl<I> implements LoadBalance<I> {

	/**
	 * 服务节点信息列表
	 */
	private List<Node<I>> nodeList;

	/**
	 * 当前服务节点信息
	 */
	private Node<I> node;

	public RandomLoadBalanceImpl(List<Node<I>> nodeList) {
		init(nodeList);
	}

	public RandomLoadBalanceImpl(List<Node<I>> nodeList, String context) {
		init(nodeList);
	}

	private void init(List<Node<I>> nodeList) {
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
	public Node<I> getNode() {
		if (ObjectUtil.isNull(node))
			log.error("'node' is null !");
		return node;
	}

	@Override
	public Node<I> next() throws IkasoaException {
		int size = nodeList.size();
		if (size == 0)
			throw new IkasoaException("Get node info failed !");
		node = nodeList.get(NumberUtil.getRandomInt(0, size));
		log.debug("Node is : {}", nodeList);
		return getNode();
	}

}
