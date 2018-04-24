package com.ikasoa.springboot;

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

	public IkasoaServiceProxy(String host, int port, IkasoaFactoryFactory ikasoaFactoryFactory) {
		this.host = host;
		this.port = port;
		this.ikasoaFactoryFactory = ikasoaFactoryFactory;
	}

	public <T> T getDefaultService(Class<T> iClass) {
		return ikasoaFactoryFactory.getIkasoaDefaultFactory().getIkasoaClient(iClass, host, port);
	}

	public <T> T getNettyService(Class<T> iClass) {
		return ikasoaFactoryFactory.getIkasoaNettyFactory().getIkasoaClient(iClass, host, port);
	}

}
