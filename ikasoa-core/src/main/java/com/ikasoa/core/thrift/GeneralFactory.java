package com.ikasoa.core.thrift;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingTransport;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.thrift.client.AsyncMultiplexedProtocolFactory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.client.impl.DefaultThriftClientImpl;
import com.ikasoa.core.thrift.client.impl.LoadBalanceThriftClientImpl;
import com.ikasoa.core.thrift.server.MultiplexedProcessor;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.thrift.server.impl.DefaultThriftServerImpl;
import com.ikasoa.core.thrift.server.impl.NonblockingThriftServerImpl;
import com.ikasoa.core.thrift.service.AsyncService;
import com.ikasoa.core.thrift.service.Service;
import com.ikasoa.core.thrift.service.ServiceProcessor;
import com.ikasoa.core.thrift.service.impl.AsyncServiceClientImpl;
import com.ikasoa.core.thrift.service.impl.ServiceClientImpl;
import com.ikasoa.core.utils.StringUtil;

/**
 * 通用工厂实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class GeneralFactory implements Factory {

	/**
	 * 服务端配置
	 */
	protected ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();

	/**
	 * 客户端配置
	 */
	protected ThriftClientConfiguration thriftClientConfiguration = new ThriftClientConfiguration();

	/**
	 * 默认负载均衡实现
	 */
	private static final String DEFAULT_LOAD_BALANCE_CLASS_NAME = "com.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl";

	public GeneralFactory() {
		// Do nothing
	}

	public GeneralFactory(ThriftServerConfiguration thriftServerConfiguration) {
		this.thriftServerConfiguration = thriftServerConfiguration;
	}

	public GeneralFactory(ThriftClientConfiguration thriftClientConfiguration) {
		this.thriftClientConfiguration = thriftClientConfiguration;
	}

	public GeneralFactory(ThriftServerConfiguration thriftServerConfiguration,
			ThriftClientConfiguration thriftClientConfiguration) {
		this.thriftServerConfiguration = thriftServerConfiguration;
		this.thriftClientConfiguration = thriftClientConfiguration;
	}

	/**
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, TProcessor processor) {
		return new DefaultThriftServerImpl(serverName, serverPort, thriftServerConfiguration, processor);
	}

	/**
	 * 获取NIO的ThriftServer对象
	 */
	@Override
	public ThriftServer getNonblockingThriftServer(String serverName, int serverPort, TProcessor processor) {
		return new NonblockingThriftServerImpl(serverName, serverPort, thriftServerConfiguration, processor);
	}

	/**
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, Service service) {
		return getThriftServer(serverName, serverPort, new ServiceProcessor(service));
	}

	/**
	 * 获取NIO的ThriftServer对象
	 */
	@Override
	public ThriftServer getNonblockingThriftServer(String serverName, int serverPort, Service service) {
		return getNonblockingThriftServer(serverName, serverPort, new ServiceProcessor(service));
	}

	/**
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(int serverPort, Service service) {
		return getThriftServer("DefaultServer-" + serverPort, serverPort, service);
	}

	/**
	 * 获取NIO的ThriftServer对象
	 */
	@Override
	public ThriftServer getNonblockingThriftServer(int serverPort, Service service) {
		return getNonblockingThriftServer("DefaultNonblockingServer-" + serverPort, serverPort, service);
	}

	/**
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(int serverPort, Map<String, Service> serviceMap) {
		return getThriftServer("DefaultServer-" + serverPort, serverPort, serviceMap);
	}

	/**
	 * 获取NIO的ThriftServer对象
	 */
	@Override
	public ThriftServer getNonblockingThriftServer(int serverPort, Map<String, Service> serviceMap) {
		return getNonblockingThriftServer("DefaultNonblockingServer-" + serverPort, serverPort, serviceMap);
	}

	/**
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, Map<String, Service> serviceMap) {
		return getThriftServer(serverName, serverPort, buildMultiplexedProcessor(serviceMap));
	}

	/**
	 * 获取NIO的ThriftServer对象
	 */
	@Override
	public ThriftServer getNonblockingThriftServer(String serverName, int serverPort, Map<String, Service> serviceMap) {
		return getNonblockingThriftServer(serverName, serverPort, buildMultiplexedProcessor(serviceMap));
	}

	/**
	 * 获取默认的ThriftClient对象
	 */
	@Override
	public ThriftClient getThriftClient(String serverHost, int serverPort) {
		return new DefaultThriftClientImpl(serverHost, serverPort, thriftClientConfiguration);
	}

	/**
	 * 获取带负载均衡的ThriftClient对象
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ThriftClient getThriftClient(List<ServerInfo> serverInfoList) {
		try {
			Class cls = Class.forName(DEFAULT_LOAD_BALANCE_CLASS_NAME);
			return getThriftClient(serverInfoList, cls);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ThriftClient getThriftClient(List<ServerInfo> serverInfoList, Class<LoadBalance> loadBalanceClass) {
		return getThriftClient(serverInfoList, loadBalanceClass, null);
	}

	/**
	 * 获取带负载均衡的ThriftClient对象
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public ThriftClient getThriftClient(List<ServerInfo> serverInfoList, Class<LoadBalance> loadBalanceClass,
			String param) {
		try {
			Class[] paramTypes = { List.class, String.class };
			Object[] params = { serverInfoList, param };
			return new LoadBalanceThriftClientImpl(
					(LoadBalance) loadBalanceClass.getConstructor(paramTypes).newInstance(params),
					thriftClientConfiguration);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取客户端Service对象
	 */
	@Override
	public Service getService(ThriftClient thriftClient) throws IkasoaException {
		return getService(thriftClient, null);
	}

	/**
	 * 获取客户端AsyncService对象
	 */
	@Override
	public AsyncService getAsyncService(TNonblockingTransport transport) throws IkasoaException {
		return getAsyncService(transport, null);
	}

	/**
	 * 获取客户端Service对象
	 */
	@Override
	public Service getService(ThriftClient thriftClient, String serviceName) throws IkasoaException {
		if (thriftClient == null)
			throw new IllegalArgumentException("'thriftClient' is null !");
		return StringUtil.isEmpty(serviceName)
				? new ServiceClientImpl(thriftClient.getProtocol(thriftClient.getTransport()))
				: new ServiceClientImpl(thriftClient.getProtocol(thriftClient.getTransport(), serviceName));
	}

	/**
	 * 获取客户端AsyncService对象
	 */
	@Override
	public AsyncService getAsyncService(TNonblockingTransport transport, String serviceName) throws IkasoaException {
		if (transport == null)
			throw new IllegalArgumentException("'transport' is null !");
		try {
			return StringUtil.isEmpty(serviceName)
					? new AsyncServiceClientImpl((TProtocolFactory) new TCompactProtocol.Factory(), transport)
					: new AsyncServiceClientImpl(new AsyncMultiplexedProtocolFactory(serviceName), transport);
		} catch (IOException e) {
			throw new IkasoaException(e);
		}
	}

	private MultiplexedProcessor buildMultiplexedProcessor(Map<String, Service> serviceMap) {
		if (serviceMap == null)
			throw new IllegalArgumentException("'serviceMap' is null !");
		Map<String, TProcessor> processorMap = new HashMap<>(serviceMap.size());
		for (Entry<String, Service> e : serviceMap.entrySet())
			processorMap.put(e.getKey(), new ServiceProcessor(serviceMap.get(e.getKey())));
		return new MultiplexedProcessor(processorMap);
	}

}
