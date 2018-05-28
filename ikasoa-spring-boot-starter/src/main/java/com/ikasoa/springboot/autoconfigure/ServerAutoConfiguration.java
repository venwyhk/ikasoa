package com.ikasoa.springboot.autoconfigure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.IkasoaException;
import com.ikasoa.rpc.IkasoaFactory;
import com.ikasoa.rpc.IkasoaServer;
import com.ikasoa.rpc.ImplClsCon;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * IKASOA服务端自动配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Configuration
@Slf4j
public class ServerAutoConfiguration extends AbstractAutoConfiguration implements ApplicationContextAware {

	@Setter
	private ApplicationContext applicationContext;

	@Bean
	public IkasoaServer getServer() throws IkasoaException {
		return getServer(getIkasoaFactoryFactory().getIkasoaDefaultFactory());
	}

	private IkasoaServer getServer(IkasoaFactory factory) throws IkasoaException {
		if (StringUtil.isEmpty(names) && StringUtil.isEmpty(classes))
			log.warn("Server configuration (${ikasoa.server.names} or ${ikasoa.server.classes}) is null !");
		List<ImplClsCon> implClsConList = new ArrayList<>();
		String[] nameStrs = names.split(",");
		for (String name : nameStrs) {
			log.debug("Add ikasoa service : {}", name);
			try {
				ImplClsCon icc = applicationContext != null && applicationContext.getBean(name) != null
						? new ImplClsCon(applicationContext.getBean(name).getClass()) : null;
				Optional.ofNullable(icc).map(i -> implClsConList.add(i)).orElseThrow(IkasoaException::new);
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		String[] classStrs = classes.split(",");
		for (String classStr : classStrs) {
			log.debug("Add ikasoa service : {}", classStr);
			try {
				ImplClsCon icc = StringUtil.isNotEmpty(classStr) ? new ImplClsCon(Class.forName(classStr)) : null;
				Optional.ofNullable(icc).map(i -> implClsConList.add(i)).orElseThrow(IkasoaException::new);
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		return factory.getIkasoaServer(implClsConList, getPort());
	}

}
