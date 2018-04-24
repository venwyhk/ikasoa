package com.ikasoa.springboot.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.rpc.IkasoaException;
import com.ikasoa.springboot.IkasoaServiceProxy;

/**
 * IKASOA客户端自动配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Configuration
public class ClientAutoConfiguration extends AbstractAutoConfiguration {

	private IkasoaServiceProxy ikasoaServiceProxy;

	@Bean
	public IkasoaServiceProxy getServiceFactory() throws IkasoaException {
		return ikasoaServiceProxy != null ? ikasoaServiceProxy
				: new IkasoaServiceProxy(getHost(), getPort(), getIkasoaFactoryFactory());
	}

}
