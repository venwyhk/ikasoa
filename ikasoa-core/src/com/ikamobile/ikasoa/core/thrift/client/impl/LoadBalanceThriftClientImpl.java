package com.ikamobile.ikasoa.core.thrift.client.impl;

import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.ServerCheck;
import com.ikamobile.ikasoa.core.ServerCheckFailProcessor;
import com.ikamobile.ikasoa.core.loadbalance.LoadBalance;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;

/**
 * 负载均衡Thrift客户端实现
 * <p>
 * 如果提供相同的Thrift服务,那么端口号也需要相同.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class LoadBalanceThriftClientImpl extends AbstractThriftClientImpl {

	private static final Logger LOG = LoggerFactory.getLogger(LoadBalanceThriftClientImpl.class);

	private LoadBalance loadBalance;

	private ServerCheckFailProcessor serverCheckFailProcessor = getServerCheckFailProcessor(new NextProcessImpl());

	public LoadBalanceThriftClientImpl() {
		// Do nothing
	}

	public LoadBalanceThriftClientImpl(LoadBalance loadBalance, ThriftClientConfiguration configuration) {
		if (loadBalance == null) {
			throw new RuntimeException("loadBalance is null !");
		}
		this.loadBalance = loadBalance;
		updateServerInfo();
		if (configuration == null) {
			LOG.debug("ThriftClientConfiguration is null .");
			configuration = new ThriftClientConfiguration();
		}
		setThriftClientConfiguration(configuration);
	}

	/**
	 * 获取服务传输协议
	 * <p>
	 * 这里重写了<code>getTransport()</code>方法,在每次获取服务传输协议后,切换服务器地址.
	 * 
	 * @return TTransport 服务传输协议
	 * @version 0.2
	 */
	@Override
	public TTransport getTransport() throws STException {
		updateServerInfo();
		ServerCheck serverCheck = getServerCheck();
		// 如果有配置检测实现,则在建立连接前尝试检测服务器,如果服务器不可用则尝试切换到另一台服务器,切换规则取决于负载均衡实现.
		// 需要注意,如果列表中的服务器全都不可用,则会无限循环下去,直到有可用的服务为止.
		if (serverCheck != null && !serverCheck.check(getServerHost(), getServerPort())) {
			serverCheckFailProcessor.process(this);
			return getTransport();
		}
		TTransport transport = super.getTransport(getServerHost(), getServerPort());
		loadBalance.next();
		return transport;
	}

	/**
	 * 更新服务器信息
	 * 
	 * @version 0.3.4
	 */
	private void updateServerInfo() {
		ServerInfo serverInfo = loadBalance.getServerInfo();
		if (serverInfo == null) {
			throw new RuntimeException("serverInfo is null !");
		}
		setServerHost(serverInfo.getHost());
		setServerPort(serverInfo.getPort());
		LOG.debug("Update server info . (" + serverInfo.toString() + ")");
	}

	/**
	 * 服务器连接失败自动切换处理
	 * 
	 * @version 0.2
	 */
	private class NextProcessImpl implements ServerCheckFailProcessor {

		@Override
		public void process(ThriftClient client) throws STException {
			LOG.warn("Server is not available (serverHost : " + client.getServerHost() + ", serverPort : "
					+ client.getServerPort() + ") , try next server .");
			loadBalance.next();
		}

	}

}
