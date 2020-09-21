package com.ikasoa.springboot.autoconfigure;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import com.ikasoa.core.ServerInfo;
import com.ikasoa.core.loadbalance.Node;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.RpcException;
import com.ikasoa.springboot.ServiceProxy;
import com.ikasoa.springboot.annotation.RpcEurekaClient;
import com.ikasoa.zk.ZkServerCheck;

import lombok.extern.slf4j.Slf4j;

import com.ikasoa.springboot.annotation.RpcClient;

/**
 * IKASOA客户端自动配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Configuration
@Slf4j
public class ClientAutoConfiguration extends AbstractAutoConfiguration implements ImportAware {

	private String eurekaAppName;

	private int eurekaAppPort;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Bean
	public ServiceProxy getServiceProxy() throws RpcException {
		if (discoveryClient != null && StringUtil.isNotEmpty(eurekaAppName) && ServerUtil.isPort(eurekaAppPort)) {
			log.debug("Eureka services : " + discoveryClient.getServices());
			if (!ServerUtil.isPort(eurekaAppPort))
				throw new RpcException("Port '" + eurekaAppPort + "' is error !");
			List<ServiceInstance> instanceList = discoveryClient.getInstances(eurekaAppName);
			if (instanceList.isEmpty())
				throw new RpcException(StringUtil.merge("Service '", eurekaAppName, "' is empty !"));
			List<Node<ServerInfo>> serverInfoList = instanceList.stream()
					.map(i -> new Node<ServerInfo>(new ServerInfo(i.getHost(), eurekaAppPort)))
					.collect(Collectors.toList());
			return StringUtil.isEmpty(configurator)
					? new ServiceProxy(serverInfoList)
					: new ServiceProxy(serverInfoList, getConfigurator());
		} else if (StringUtil.isNotEmpty(zkServerString)) {
			Configurator configurator = getConfigurator();
			ThriftClientConfiguration thriftClientConfiguration = new ThriftClientConfiguration();
			thriftClientConfiguration.setServerCheck(new ZkServerCheck(zkServerString, zkNode));
			configurator.setThriftClientConfiguration(thriftClientConfiguration);
			return new ServiceProxy(getHost(), getPort(), configurator);
		} else
			return StringUtil.isEmpty(configurator) ? new ServiceProxy(getHost(), getPort())
					: new ServiceProxy(getHost(), getPort(), getConfigurator());
	}

	@Override
	public void setImportMetadata(AnnotationMetadata annotationMetadata) {
		Map<String, Object> rpcClientAttributes = annotationMetadata.getAnnotationAttributes(RpcClient.class.getName());
		if (rpcClientAttributes != null && !rpcClientAttributes.isEmpty()) {
			if (rpcClientAttributes.containsKey("host"))
				host = rpcClientAttributes.get("host").toString();
			if (rpcClientAttributes.containsKey("port"))
				port = rpcClientAttributes.get("port").toString();
			if (rpcClientAttributes.containsKey("config"))
				configurator = rpcClientAttributes.get("config").toString();
		}
		Map<String, Object> rpcEurekaClientAttributes = annotationMetadata
				.getAnnotationAttributes(RpcEurekaClient.class.getName());
		if (rpcEurekaClientAttributes != null && !rpcEurekaClientAttributes.isEmpty()) {
			if (rpcEurekaClientAttributes.containsKey("name"))
				eurekaAppName = rpcEurekaClientAttributes.get("name").toString();
			if (rpcEurekaClientAttributes.containsKey("port"))
				eurekaAppPort = (Integer) rpcEurekaClientAttributes.get("port");
			if (rpcEurekaClientAttributes.containsKey("config"))
				configurator = rpcEurekaClientAttributes.get("config").toString();
		}
	}

}
