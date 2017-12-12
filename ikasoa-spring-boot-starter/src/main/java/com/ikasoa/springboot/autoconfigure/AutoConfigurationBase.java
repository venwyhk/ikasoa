package com.ikasoa.springboot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.rpc.Configurator;
import com.ikasoa.springboot.IkasoaFactoryFactory;

/**
 * IKASOA自动配置基础类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Configuration
public class AutoConfigurationBase {

	private static final Logger LOG = LoggerFactory.getLogger(AutoConfigurationBase.class);

	@Value("${ikasoa.server.host:localhost}")
	protected String host;

	@Value("${ikasoa.server.port:9999}")
	protected String port;

	@Value("${ikasoa.server.service.implClasses}")
	protected String serviceImplClasses;

	@Value("${ikasoa.configuratorClass:com.ikasoa.rpc.Configurator}")
	protected String configuratorClass;

	protected Configurator getConfigurator() {
		try {
			return (Configurator) Class.forName(configuratorClass).newInstance();
		} catch (ClassNotFoundException e) {
			LOG.warn(e.getMessage());
		} catch (InstantiationException e) {
			LOG.warn(e.getMessage());
		} catch (IllegalAccessException e) {
			LOG.warn(e.getMessage());
		}
		return null;
	}

	protected IkasoaFactoryFactory getIkasoaFactoryFactory() {
		Configurator configurator = getConfigurator();
		if (configurator != null) {
			return new IkasoaFactoryFactory(configurator);
		} else {
			return new IkasoaFactoryFactory();
		}
	}

}
