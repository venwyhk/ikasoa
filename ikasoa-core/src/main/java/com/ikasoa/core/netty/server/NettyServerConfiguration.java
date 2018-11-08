package com.ikasoa.core.netty.server;

import lombok.Getter;
import lombok.Setter;

import org.apache.thrift.TProcessorFactory;

import com.ikasoa.core.thrift.server.ServerConfiguration;

/**
 * Netty服务器配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public class NettyServerConfiguration extends ServerConfiguration {

	@Getter
	@Setter
	private int workerCount = 2;

	@Getter
	private final int maxFrameSize = 64 * 1024 * 1024;

	@Getter
	private final int queuedResponseLimit = 16;

	public NettyServerConfiguration(TProcessorFactory processorFactory) {
		super.processorFactory = processorFactory;
	}

}
