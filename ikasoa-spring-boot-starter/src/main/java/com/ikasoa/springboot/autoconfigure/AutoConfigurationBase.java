package com.ikasoa.springboot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.IkasoaException;
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

	@Value("${ikasoa.server.names}")
	protected String names;

	@Value("${ikasoa.configurator:com.ikasoa.rpc.Configurator}")
	protected String configurator;

	protected Configurator getConfigurator() {
		try {
			return (Configurator) Class.forName(configurator).newInstance();
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
		return new IkasoaFactoryFactory(getConfigurator());
	}

	protected String getHost() throws IkasoaException {
		if (StringUtil.isEmpty(host))
			throw new IkasoaException("Server configuration (${ikasoa.server.host}) is error !");
		return host;
	}

	protected int getPort() throws IkasoaException {
		if (StringUtil.isEmpty(port))
			throw new IkasoaException("Server port (${ikasoa.server.port}) is null !");
		int iPort = StringUtil.toInt(port.trim());
		if (!ServerUtil.isPort(iPort))
			throw new IkasoaException("Server configuration (${ikasoa.server.port}) is error !");
		return iPort;
	}

}
