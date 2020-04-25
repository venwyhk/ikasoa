package com.ikasoa.core.loadbalance;

import com.ikasoa.core.IkasoaException;

/**
 * 负载均衡接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface LoadBalance<S> {

	/**
	 * 获取服务器节点信息
	 * <p>
	 * 通过构造方法传入参数,一般传入一批服务器节点,如果有权重值也需要传入.
	 * <p>
	 * 通过此方法获取一批服务器节点信息中的一个.如果未执行<code>next()</code>方法,则每次获取的节点是相同的.
	 * 
	 * @return Node 服务器节点
	 */
	Node<S> getNode();

	/**
	 * 切换服务器节点
	 * <p>
	 * 按照负载均衡规则切换一个服务器节点.返回节点信息或者通过<code>getNode()</code>获取节点信息
	 * 方法获取的服务器节点信息为切换过后的信息,可能会与之前的节点不同.
	 * 
	 * @return Node 服务器节点
	 * @exception IkasoaException
	 *                异常
	 */
	Node<S> next() throws IkasoaException;

}
