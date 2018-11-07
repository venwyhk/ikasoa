package com.ikasoa.core.thrift.server;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.server.TServerEventHandler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Thrift服务器配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Getter
@NoArgsConstructor
@Setter
public class ThriftServerConfiguration extends ServerConfiguration {

	/**
	 * Thrift服务事件处理器,用于扩展其它功能
	 */
	private TServerEventHandler serverEventHandler;

	private ServerArgsAspect serverArgsAspect = new ServerArgsAspect();

	public ThriftServerConfiguration(TProcessorFactory processorFactory) {
		super.processorFactory = processorFactory;
	}

}
