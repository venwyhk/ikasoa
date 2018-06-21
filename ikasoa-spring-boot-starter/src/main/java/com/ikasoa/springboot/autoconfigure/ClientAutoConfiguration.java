package com.ikasoa.springboot.autoconfigure;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import com.ikasoa.rpc.RpcException;
import com.ikasoa.springboot.ServiceProxy;
import com.ikasoa.springboot.annotation.RpcClient;

/**
 * IKASOA客户端自动配置
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Configuration
public class ClientAutoConfiguration extends AbstractAutoConfiguration implements ImportAware {

	@Bean
	public ServiceProxy getServiceProxy() throws RpcException {
		return new ServiceProxy(getHost(), getPort(), getConfigurator());
	}

	@Override
	public void setImportMetadata(AnnotationMetadata annotationMetadata) {
		Map<String, Object> rpcClientAttributes = annotationMetadata.getAnnotationAttributes(RpcClient.class.getName());
		if (rpcClientAttributes != null && !rpcClientAttributes.isEmpty()) {
			if (rpcClientAttributes.containsKey("host"))
				this.host = rpcClientAttributes.get("host").toString();
			if (rpcClientAttributes.containsKey("port"))
				this.port = rpcClientAttributes.get("port").toString();
			if (rpcClientAttributes.containsKey("config"))
				this.configurator = rpcClientAttributes.get("config").toString();
		}
	}

}
