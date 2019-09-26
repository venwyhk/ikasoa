package com.ikasoa.core.thrift.client.impl;

import org.apache.thrift.transport.TTransport;
import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.ServerCheck;
import com.ikasoa.core.ServerCheckFailProcessor;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.utils.ObjectUtil;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 负载均衡Thrift客户端实现
 * <p>
 * 如果提供相同的Thrift服务,那么端口号也需要相同.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
@Slf4j
public class LoadBalanceThriftClientImpl extends AbstractThriftClientImpl {

	private LoadBalance loadBalance;

	private ServerCheckFailProcessor serverCheckFailProcessor = getServerCheckFailProcessor(new NextProcessImpl());

	public LoadBalanceThriftClientImpl(final LoadBalance loadBalance, final ThriftClientConfiguration configuration) {
		if (ObjectUtil.isNull(loadBalance))
			throw new IllegalArgumentException("'loadBalance' is null !");
		this.loadBalance = loadBalance;
		updateServerInfo();
		if (ObjectUtil.isNull(configuration)) {
			log.debug("Thrift client configuration is null .");
			setConfiguration(new ThriftClientConfiguration());
		} else
			setConfiguration(configuration);
	}

	/**
	 * 获取服务传输协议
	 * <p>
	 * 这里重写了<code>getTransport()</code>方法,在每次获取服务传输协议后,切换服务器地址.
	 * 
	 * @return TTransport 服务传输协议
	 */
	@Override
	public TTransport getTransport() throws IkasoaException {
		updateServerInfo();
		ServerCheck serverCheck = getServerCheck();
		// 如果有配置检测实现,则在建立连接前尝试检测服务器,如果服务器不可用则尝试切换到另一台服务器,切换规则取决于负载均衡实现.
		// 需要注意,如果列表中的服务器全都不可用,则会无限循环下去,直到有可用的服务为止.
		if (ObjectUtil.isNotNull(serverCheck) && !serverCheck.check(getServerHost(), getServerPort())) {
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
		if (ObjectUtil.isNull(serverInfo))
			throw new RuntimeException("'serverInfo' is null !");
		setServerHost(serverInfo.getHost());
		setServerPort(serverInfo.getPort());
		log.debug("Update server info . ({})", serverInfo.toString());
	}

	/**
	 * 服务器连接失败自动切换处理
	 * 
	 * @version 0.2
	 */
	private class NextProcessImpl implements ServerCheckFailProcessor {

		@Override
		public void process(ThriftClient client) throws IkasoaException {
			log.warn("Server is not available (serverHost : {}, serverPort : {}) , try next server .",
					client.getServerHost(), client.getServerPort());
			loadBalance.next();
		}

	}

}
