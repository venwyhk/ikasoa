package com.ikasoa.core;

import com.ikasoa.core.thrift.client.ThriftClient;

/**
 * 服务器连接失败处理接口
 * <p>
 * 如果有配置服务器可用性检测<i>ServerCheck</i>,且检测不通过需要做相应的处理时,则可以使用该接口.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@FunctionalInterface
public interface ServerCheckFailProcessor {

	/**
	 * 服务器连接失败处理
	 * 
	 * @param client
	 *            客户端对象
	 * @exception STException
	 *                异常
	 */
	void process(ThriftClient client) throws STException;

}
