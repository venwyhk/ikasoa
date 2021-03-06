package com.ikasoa.springboot.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.RpcException;
import com.ikasoa.springboot.IkasoaFactoryFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * IKASOA自动配置基础类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Configuration
@Slf4j
public abstract class AbstractAutoConfiguration {

	@Value("${spring.application.name}")
	protected String name;

	@Value("${ikasoa.server.host:localhost}")
	protected String host;

	@Value("${ikasoa.server.port:9999}")
	protected String port;

	@Value("${ikasoa.server.names:}")
	protected String names;

	@Value("${ikasoa.server.classes:}")
	protected String classes;

	@Value("${ikasoa.configurator:com.ikasoa.rpc.Configurator}")
	protected String configurator;

	@Value("${ikasoa.server.zkserver:}")
	protected String zkServerString;

	@Value("${ikasoa.server.zknode:/}")
	protected String zkNode;

	protected IkasoaFactoryFactory getIkasoaFactoryFactory() {
		return new IkasoaFactoryFactory(getConfigurator());
	}

	protected IkasoaFactoryFactory getIkasoaFactoryFactory(Configurator configurator) {
		return new IkasoaFactoryFactory(configurator);
	}

	protected Configurator getConfigurator() {
		try {
			return (Configurator) Class.forName(configurator).newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	protected String getHost() throws RpcException {
		if (StringUtil.isEmpty(host))
			throw new RpcException("Server configuration (${ikasoa.server.host}) is error !");
		return host;
	}

	protected int getPort() throws RpcException {
		if (StringUtil.isEmpty(port))
			throw new RpcException("Server port (${ikasoa.server.port}) is null !");
		int iPort = StringUtil.toInt(port.trim());
		if (!ServerUtil.isPort(iPort))
			throw new RpcException("Server configuration (${ikasoa.server.port}) is error !");
		return iPort;
	}

}
