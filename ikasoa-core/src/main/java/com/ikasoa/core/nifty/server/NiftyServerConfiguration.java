package com.ikasoa.core.nifty.server;

import com.ikasoa.core.thrift.server.ServerAspect;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;

import lombok.Data;

import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;

@Data
public class NiftyServerConfiguration {

	private ChannelPipelineFactory channelPipelineFactory;

	private String name = "NiftyServer";
	private int serverPort = 8080;
	private final int maxFrameSize = 64 * 1024 * 1024;
	private final int queuedResponseLimit = 16;
	private TProcessorFactory processorFactory;
	private TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();

	/**
	 * Thrift服务器切面对象
	 */
	private ServerAspect serverAspect;

	public NiftyServerConfiguration(int serverPort, TProcessor processor) {
		this(null, serverPort, processor);
	}

	public NiftyServerConfiguration(String name, int serverPort, TProcessor processor) {
		this.serverPort = ServerUtil.isPort(serverPort) ? serverPort : this.serverPort;
		this.name = (StringUtil.isNotEmpty(name) ? name : this.name) + "-" + this.serverPort;
		this.processorFactory = new TProcessorFactory(processor);
	}

}
