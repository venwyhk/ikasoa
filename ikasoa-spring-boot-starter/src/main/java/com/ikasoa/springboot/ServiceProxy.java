package com.ikasoa.springboot;

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

	private IkasoaFactoryFactory ikasoaFactoryFactory;

	private IkasoaFactory defaultFactory;

	private IkasoaFactory nettyFactory;

	public ServiceProxy(String host, int port) {
		this(host, port, new IkasoaFactoryFactory());
	}

	public ServiceProxy(String host, int port, Configurator configurator) {
		this(host, port, new IkasoaFactoryFactory(configurator));
	}

	public ServiceProxy(String host, int port, IkasoaFactoryFactory ikasoaFactoryFactory) {
		this.host = host;
		this.port = port;
		this.ikasoaFactoryFactory = ikasoaFactoryFactory;
	}

	public <T> T getService(Class<T> iClass) {
		if (defaultFactory == null)
			defaultFactory = ikasoaFactoryFactory.getIkasoaDefaultFactory();
		return defaultFactory.getInstance(iClass, new ServerInfoWrapper(host, port));
	}

	public <T> T getNettyService(Class<T> iClass) {
		if (nettyFactory == null)
			defaultFactory = ikasoaFactoryFactory.getIkasoaNettyFactory();
		return nettyFactory.getInstance(iClass, new ServerInfoWrapper(host, port));
	}

}
