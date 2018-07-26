package com.ikasoa.springboot.autoconfigure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.thrift.protocol.TJSONProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.thrift.server.ThriftServlet;
import com.ikasoa.core.thrift.server.impl.ServletThriftServerImpl;
import com.ikasoa.core.thrift.service.Service;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.RpcException;
import com.ikasoa.rpc.ServiceKey;
import com.ikasoa.rpc.annotation.IkasoaService;
import com.ikasoa.rpc.annotation.Invalid;
import com.ikasoa.zk.ZkServerAspect;
import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.IkasoaFactory;
import com.ikasoa.rpc.IkasoaServer;
import com.ikasoa.rpc.ImplWrapper;

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

	@Value("${eureka.instance.appname:}")
	private String eurekaAppName;

	@Setter
	private ApplicationContext applicationContext;

	@Bean
	public IkasoaServer getServer() throws RpcException {
		return StringUtil.isEmpty(zkServerString)
				? getServer(getIkasoaFactoryFactory().getIkasoaDefaultFactory(), getImplWrapperList())
				: getServer(getIkasoaFactoryFactory(new ZkClientConfigurator(zkServerString, zkNode))
						.getIkasoaDefaultFactory(), getImplWrapperList());
	}

	protected List<ImplWrapper> getImplWrapperList() {
		if (StringUtil.isEmpty(names) && StringUtil.isEmpty(classes))
			log.warn("Server configuration (${ikasoa.server.names} or ${ikasoa.server.classes}) is null !");
		List<ImplWrapper> implWrapperList = new ArrayList<>();
		String[] nameStrs = names.split(",");
		for (String name : nameStrs) {
			log.debug("Add ikasoa service : {}", name);
			try {
				ImplWrapper iw = applicationContext != null && applicationContext.containsBean(name)
						? new ImplWrapper(applicationContext.getBean(name).getClass())
						: null;
				Optional.ofNullable(iw).map(i -> implWrapperList.add(i)).orElseThrow(RpcException::new);
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		String[] classStrs = classes.split(",");
		for (String classStr : classStrs) {
			log.debug("Add ikasoa service : {}", classStr);
			try {
				ImplWrapper iw = StringUtil.isNotEmpty(classStr) ? new ImplWrapper(Class.forName(classStr)) : null;
				Optional.ofNullable(iw).map(i -> implWrapperList.add(i)).orElseThrow(RpcException::new);
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		return implWrapperList;
	}

	protected IkasoaServer getServer(IkasoaFactory factory, List<ImplWrapper> implWrapperList) throws RpcException {
		return StringUtil.isNotEmpty(name) ? factory.getIkasoaServer(name, implWrapperList, getPort())
				: StringUtil.isNotEmpty(eurekaAppName)
						? factory.getIkasoaServer(eurekaAppName, implWrapperList, getPort())
						: factory.getIkasoaServer(implWrapperList, getPort());
	}

	private class ZkClientConfigurator extends Configurator {

		public ZkClientConfigurator(String zkServerString, String zkNode) {
			ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();
			thriftServerConfiguration.setServerAspect(new ZkServerAspect(zkServerString, zkNode));
			setThriftServerConfiguration(thriftServerConfiguration);
		}

	}

}
