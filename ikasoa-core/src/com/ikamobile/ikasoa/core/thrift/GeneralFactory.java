package com.ikamobile.ikasoa.core.thrift;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TProcessor;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.client.impl.DefaultThriftClientImpl;
import com.ikamobile.ikasoa.core.thrift.client.impl.LoadBalanceThriftClientImpl;
import com.ikamobile.ikasoa.core.thrift.server.MultiplexedProcessor;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServer;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikamobile.ikasoa.core.thrift.server.impl.DefaultThriftServerImpl;
import com.ikamobile.ikasoa.core.thrift.service.Service;
import com.ikamobile.ikasoa.core.thrift.service.ServiceProcessor;
import com.ikamobile.ikasoa.core.thrift.service.impl.ServiceClientImpl;
import com.ikamobile.ikasoa.core.utils.StringUtil;

/**
 * 通用工厂实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class GeneralFactory implements Factory {

	protected ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();
	protected ThriftClientConfiguration thriftClientConfiguration = new ThriftClientConfiguration();

	public GeneralFactory() {
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
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, Service service) {
		return getThriftServer(serverName, serverPort, new ServiceProcessor(service));
	}

	/**
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(int serverPort, Service service) {
		return getThriftServer("DefaultThriftServer-" + serverPort, serverPort, service);
	}

	/**
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, Map<String, Service> serviceMap)
			throws STException {
		if (serviceMap != null) {
			Map<String, TProcessor> processorMap = new HashMap<String, TProcessor>();
			for (String key : serviceMap.keySet()) {
				processorMap.put(key, new ServiceProcessor(serviceMap.get(key)));
			}
			return getThriftServer(serverName, serverPort, new MultiplexedProcessor(processorMap));
		} else {
			throw new STException("serviceMap is null .");
		}
	}

	/**
	 * 获取默认的ThriftServer对象
	 */
	@Override
	public ThriftServer getThriftServer(int serverPort, Map<String, Service> serviceMap) throws STException {
		if (serviceMap != null) {
			Map<String, TProcessor> processorMap = new HashMap<String, TProcessor>();
			for (String key : serviceMap.keySet()) {
				processorMap.put(key, new ServiceProcessor(serviceMap.get(key)));
			}
			return getThriftServer("DefaultThriftServer-" + serverPort, serverPort,
					new MultiplexedProcessor(processorMap));
		} else {
			throw new STException("serviceMap is null .");
		}
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
	public ThriftClient getThriftClient(List<ServerInfo> serverInfoList) {
		return new LoadBalanceThriftClientImpl(new PollingLoadBalanceImpl(serverInfoList), thriftClientConfiguration);
	}

	/**
	 * 获取客户端Service对象
	 */
	@Override
	public Service getService(ThriftClient thriftClient) throws STException {
		return getService(thriftClient, null);
	}

	/**
	 * 获取客户端Service对象
	 */
	@Override
	public Service getService(ThriftClient thriftClient, String serviceName) throws STException {
		if (thriftClient == null) {
			throw new STException("thriftClient is null .");
		}
		if (StringUtil.isEmpty(serviceName)) {
			return new ServiceClientImpl(thriftClient.getProtocol(thriftClient.getTransport()));
		} else {
			return new ServiceClientImpl(thriftClient.getProtocol(thriftClient.getTransport(), serviceName));
		}
	}

}
