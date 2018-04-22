package com.ikasoa.springboot.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		return new IkasoaServiceFactory(getHost(), getPort(), getIkasoaFactoryFactory());
	}
	
}
