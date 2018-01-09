package com.ikasoa.springboot.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.core.utils.ServerUtil;
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
public class ServerAutoConfiguration extends AutoConfigurationBase {

	private static final Logger LOG = LoggerFactory.getLogger(ServerAutoConfiguration.class);

	@Bean
	public IkasoaServer getServer() throws IkasoaException {
		return getServer(super.getIkasoaFactoryFactory().getIkasoaDefaultFactory());
	}

	private IkasoaServer getServer(IkasoaFactory factory) throws IkasoaException {
		if (StringUtil.isEmpty(port))
			throw new IkasoaException("Server port (${spring.ikasoa.server.port}) is null !");
		if (StringUtil.isEmpty(serviceImplClasses))
			throw new IkasoaException(
					"Server service impl classes (${spring.ikasoa.server.service.implClasses}) is null !");
		String[] serviceImplStrs = serviceImplClasses.split(",");
		List<ImplClsCon> implClsConList = new ArrayList<ImplClsCon>();
		try {
			for (String serviceImplStr : serviceImplStrs) {
				LOG.debug("Add ikasoa service : " + serviceImplStr);
				Class<?> serviceImplCls = Class.forName(serviceImplStr);
				implClsConList.add(new ImplClsCon(serviceImplCls));
			}
			int iPort = StringUtil.toInt(port.trim());
			if (!ServerUtil.isPort(iPort))
				throw new IkasoaException("Configuration 'port' is error !");
			return factory.getIkasoaServer(implClsConList, iPort);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
