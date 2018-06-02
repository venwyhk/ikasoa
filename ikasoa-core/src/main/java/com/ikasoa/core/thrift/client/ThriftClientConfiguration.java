package com.ikasoa.core.thrift.client;

import com.ikasoa.core.thrift.AbstractThriftConfiguration;
import com.ikasoa.core.thrift.client.pool.SocketPool;
import com.ikasoa.core.thrift.client.pool.impl.SimpleSocketPoolImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.ikasoa.core.ServerCheck;
import com.ikasoa.core.ServerCheckFailProcessor;

/**
 * Thrift客户端配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Getter
@NoArgsConstructor
@Setter
public class ThriftClientConfiguration extends AbstractThriftConfiguration {

	private SocketPool socketPool = new SimpleSocketPoolImpl();

	/**
	 * 服务器可用性检测接口
	 * <p>
	 * 如果不需要检测,则保持为空.
	 */
	private ServerCheck serverCheck;

	/**
	 * 服务器连接失败处理接口
	 */
	private ServerCheckFailProcessor serverCheckFailProcessor;

}
