package com.ikasoa.springboot.autoconfigure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.IkasoaException;
import com.ikasoa.rpc.IkasoaFactory;
import com.ikasoa.rpc.IkasoaServer;
import com.ikasoa.rpc.ImplClsCon;

/**
 * IKASOA服务端自动配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Configuration
public class ServerAutoConfiguration extends AutoConfigurationBase implements ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(ServerAutoConfiguration.class);

	private ApplicationContext applicationContext;

	@Bean
	public IkasoaServer getServer() throws IkasoaException {
		return getServer(getIkasoaFactoryFactory().getIkasoaDefaultFactory());
	}

	private IkasoaServer getServer(IkasoaFactory factory) throws IkasoaException {
		if (StringUtil.isEmpty(names))
			throw new IkasoaException("Server service names (${ikasoa.server.service.names}) is null !");
		String[] nameStrs = names.split(",");
		List<ImplClsCon> implClsConList = new ArrayList<>();
		for (String name : nameStrs) {
			LOG.debug("Add ikasoa service : {}", name);
			ImplClsCon icc = applicationContext != null && applicationContext.getBean(name) != null
					? new ImplClsCon(applicationContext.getBean(name).getClass()) : null;
			Optional.ofNullable(icc).map(i -> implClsConList.add(i)).orElseThrow(IkasoaException::new);
		}
		return factory.getIkasoaServer(implClsConList, getPort());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
