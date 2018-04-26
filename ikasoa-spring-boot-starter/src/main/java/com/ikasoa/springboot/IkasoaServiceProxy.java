package com.ikasoa.springboot;

import com.ikasoa.rpc.IkasoaFactory;

/**
 * 获取IKASOA服务代理
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class IkasoaServiceProxy {

	private String host;

	private int port;

	private IkasoaFactoryFactory ikasoaFactoryFactory;

	private IkasoaFactory defaultFactory;

	private IkasoaFactory nettyFactory;

	public IkasoaServiceProxy(String host, int port, IkasoaFactoryFactory ikasoaFactoryFactory) {
		this.host = host;
		this.port = port;
		this.ikasoaFactoryFactory = ikasoaFactoryFactory;
	}

	public <T> T getDefaultService(Class<T> iClass) {
		if (defaultFactory == null)
			defaultFactory = ikasoaFactoryFactory.getIkasoaDefaultFactory();
		return defaultFactory.getIkasoaClient(iClass, host, port);
	}

	public <T> T getNettyService(Class<T> iClass) {
		if (nettyFactory == null)
			defaultFactory = ikasoaFactoryFactory.getIkasoaNettyFactory();
		return nettyFactory.getIkasoaClient(iClass, host, port);
	}

}
