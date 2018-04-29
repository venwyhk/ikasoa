package com.ikasoa.core.loadbalance;

import com.ikasoa.core.IkasoaException;

/**
 * 负载均衡接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface LoadBalance {

	/**
	 * 获取服务器信息
	 * <p>
	 * 通过构造方法传入参数,一般传入一批服务器信息,如果有权重值也需要传入.
	 * <p>
	 * 通过此方法获取一批服务器信息中的一个.如果未执行<code>next()</code>方法,则每次获取的地址应该是相同的.
	 * 
	 * @return ServerInfo 服务器信息
	 */
	ServerInfo getServerInfo();

	/**
	 * 切换服务器信息
	 * <p>
	 * 按照负载均衡规则切换一个服务器信息.返回的信息或者之后通过<code>getServerInfo()</code>
	 * 方法获取的服务器信息为切换过后的信息,可能会与之前的不同.
	 * 
	 * @return ServerInfo
	 * @exception IkasoaException
	 *                异常
	 */
	ServerInfo next() throws IkasoaException;

}
