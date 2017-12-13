package com.ikasoa.springboot;

import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.DefaultIkasoaFactory;
import com.ikasoa.rpc.IkasoaFactory;
import com.ikasoa.rpc.NettyIkasoaFactory;

/**
 * 获取IKASOA工厂的工厂类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class IkasoaFactoryFactory {

	private Configurator configurator;

	public IkasoaFactoryFactory() {
		// Do nothing
	}

	public IkasoaFactoryFactory(Configurator configurator) {
		this.configurator = configurator;
	}

	public IkasoaFactory getIkasoaDefaultFactory() {
		if (configurator != null)
			return new DefaultIkasoaFactory(configurator);
		else
			return new DefaultIkasoaFactory();
	}

	public IkasoaFactory getIkasoaNettyFactory() {
		if (configurator != null)
			return new NettyIkasoaFactory(configurator);
		else
			return new NettyIkasoaFactory();
	}

}
