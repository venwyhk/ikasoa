package com.ikasoa.core.nifty.server;

import com.ikasoa.core.nifty.NiftyNoOpSecurityFactory;
import com.ikasoa.core.nifty.NiftySecurityFactory;
import com.ikasoa.core.nifty.ssl.TransportAttachObserver;
import com.ikasoa.core.thrift.server.ServerAspect;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;

import lombok.Data;

import com.ikasoa.core.nifty.ssl.SslServerConfiguration;

import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

@Data
public class NiftyServerConfiguration {

	private String name = "NiftyServer";
	private int serverPort = 8080;
	private final int maxFrameSize = 64 * 1024 * 1024;
	private final int maxConnections = 0; // maxConnections should be 0 (for unlimited) or positive
	private final int queuedResponseLimit = 16;
	private TProcessorFactory processorFactory;
	private TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();
	
	private int clientIdleTimeout = 0;

//	private final ThriftFrameCodecFactory thriftFrameCodecFactory = new DefaultThriftFrameCodecFactory();
	private final NiftySecurityFactory securityFactory = new NiftyNoOpSecurityFactory();;
	private final SslServerConfiguration sslConfiguration = null;
	private final TransportAttachObserver transportAttachObserver = null;
	
	/**
	 * Thrift服务器切面对象
	 */
	private ServerAspect serverAspect;

	public NiftyServerConfiguration(int serverPort, TProcessor processor) {
		this(null, serverPort, processor);
	}

	public NiftyServerConfiguration(String name, int serverPort, TProcessor processor) {
		this.serverPort = ServerUtil.isPort(serverPort) ? serverPort : 8080;
		this.name = StringUtil.isNotEmpty(name) ? name : "NiftyServer-" + this.serverPort;
		this.processorFactory = new TProcessorFactory(processor);
	}

}
