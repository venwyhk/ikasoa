package com.ikasoa.springboot;

import java.util.List;

import com.ikasoa.core.ServerInfo;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.Node;
import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.IkasoaFactory;
import com.ikasoa.rpc.ServerInfoWrapper;

/**
 * 获取IKASOA服务代理
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ServiceProxy {

	private String host;

	private int port;

	private List<Node<ServerInfo>> serverInfoList;

	private IkasoaFactoryFactory ikasoaFactoryFactory;

	private IkasoaFactory defaultFactory;

	private IkasoaFactory nettyFactory;

	public ServiceProxy(String host, int port) {
		this(host, port, new IkasoaFactoryFactory());
	}

	public ServiceProxy(List<Node<ServerInfo>> serverInfoList) {
		this(serverInfoList, new IkasoaFactoryFactory());
	}

	public ServiceProxy(String host, int port, Configurator configurator) {
		this(host, port, new IkasoaFactoryFactory(configurator));
	}

	public ServiceProxy(List<Node<ServerInfo>> serverInfoList, Configurator configurator) {
		this(serverInfoList, new IkasoaFactoryFactory(configurator));
	}

	public ServiceProxy(String host, int port, IkasoaFactoryFactory ikasoaFactoryFactory) {
		this.host = host;
		this.port = port;
		this.ikasoaFactoryFactory = ikasoaFactoryFactory;
	}

	public ServiceProxy(List<Node<ServerInfo>> serverInfoList, IkasoaFactoryFactory ikasoaFactoryFactory) {
		if (serverInfoList == null || serverInfoList.isEmpty())
			throw new IllegalArgumentException("'serverInfoList' is null !");
		this.serverInfoList = serverInfoList;
		this.host = serverInfoList.get(0).getValue().getHost();
		this.port = serverInfoList.get(0).getValue().getPort();
		this.ikasoaFactoryFactory = ikasoaFactoryFactory;
	}

	public <T> T getService(Class<T> iClass) {
		if (defaultFactory == null)
			defaultFactory = ikasoaFactoryFactory.getIkasoaDefaultFactory();
		return defaultFactory.getInstance(iClass, new ServerInfoWrapper(host, port));
	}

	public <T> T getService(Class<T> iClass, LoadBalance<ServerInfo> loadBalance) {
		if (defaultFactory == null)
			defaultFactory = ikasoaFactoryFactory.getIkasoaDefaultFactory();
		return !serverInfoList.isEmpty() && loadBalance != null
				? defaultFactory.getInstance(iClass, new ServerInfoWrapper(serverInfoList, loadBalance))
				: getService(iClass);
	}

	public <T> T getNettyService(Class<T> iClass) {
		if (nettyFactory == null)
			nettyFactory = ikasoaFactoryFactory.getIkasoaNettyFactory();
		return nettyFactory.getInstance(iClass, new ServerInfoWrapper(host, port));
	}

	public <T> T getNettyService(Class<T> iClass, LoadBalance<ServerInfo> loadBalance) {
		if (nettyFactory == null)
			nettyFactory = ikasoaFactoryFactory.getIkasoaNettyFactory();
		return !serverInfoList.isEmpty() && loadBalance != null
				? nettyFactory.getInstance(iClass, new ServerInfoWrapper(serverInfoList, loadBalance))
				: getNettyService(iClass);
	}

}
