package com.ikasoa.springboot.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.IkasoaException;
import com.ikasoa.springboot.IkasoaServiceFactory;

/**
 * IKASOA客户端自动配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Configuration
public class ClientAutoConfiguration extends AutoConfigurationBase {

	@Bean
	public IkasoaServiceFactory getServiceFactory() throws IkasoaException {
		if (StringUtil.isEmpty(host))
			throw new IkasoaException("Server host (${spring.ikasoa.server.host}) is null !");
		if (StringUtil.isEmpty(port))
			throw new IkasoaException("Server port (${spring.ikasoa.server.port}) is null !");
		int iPort = StringUtil.toInt(port.trim());
		if (!ServerUtil.isPort(iPort))
			throw new IkasoaException("Configuration 'port' is error !");
		return new IkasoaServiceFactory(host, iPort, super.getIkasoaFactoryFactory());
	}
}
