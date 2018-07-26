package com.ikasoa.springboot;

import java.util.Optional;

import org.apache.thrift.protocol.TJSONProtocol;

import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.DefaultIkasoaFactory;
import com.ikasoa.rpc.IkasoaFactory;
import com.ikasoa.rpc.NettyIkasoaFactory;
import com.ikasoa.rpc.ServletServerIkasoaFactory;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 获取IKASOA工厂的工厂类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
@AllArgsConstructor
public class IkasoaFactoryFactory {

	private Configurator configurator;

	public IkasoaFactory getIkasoaDefaultFactory() {
		return Optional.ofNullable(configurator).map(c -> new DefaultIkasoaFactory(c))
				.orElse(new DefaultIkasoaFactory());
	}

	public IkasoaFactory getIkasoaNettyFactory() {
		return Optional.ofNullable(configurator).map(c -> new NettyIkasoaFactory(c)).orElse(new NettyIkasoaFactory());
	}

	public IkasoaFactory getIkasoaServletFactory() {
		ThriftServerConfiguration config = Optional.ofNullable(configurator).map(c -> c.getThriftServerConfiguration())
				.orElse(new ThriftServerConfiguration());
		config.setProtocolFactory(new TJSONProtocol.Factory()); // Servlet需要使用Json协议
		configurator.setThriftServerConfiguration(config);
		return new ServletServerIkasoaFactory(config);
	}

}
