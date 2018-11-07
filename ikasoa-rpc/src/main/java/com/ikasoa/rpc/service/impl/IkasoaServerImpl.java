package com.ikasoa.rpc.service.impl;

import java.util.Map;
import java.util.Set;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import com.ikasoa.core.thrift.server.ServerConfiguration;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.service.Service;
import com.ikasoa.rpc.IkasoaServer;

import lombok.AllArgsConstructor;

/**
 * IKASOA服务端实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@AllArgsConstructor
public class IkasoaServerImpl implements IkasoaServer {

	private ThriftServer thriftServer;

	private Map<String, Service> serviceMap;

	@Override
	public Map<String, Service> getIkasoaServiceMap() {
		return serviceMap;
	}

	@Override
	public Service getIkasoaService(String serviceKey) {
		return serviceMap.get(serviceKey);
	}

	@Override
	public Set<String> getIkasoaServiceKeys() {
		return serviceMap.keySet();
	}

	@Override
	public TServerTransport getTransport() throws TTransportException {
		return thriftServer.getTransport();
	}

	@Override
	public void run() {
		thriftServer.run();
	}

	@Override
	public void stop() {
		thriftServer.stop();
	}

	@Override
	public boolean isServing() {
		return thriftServer.isServing();
	}

	@Override
	public String getServerName() {
		return thriftServer.getServerName();
	}

	@Override
	public int getServerPort() {
		return thriftServer.getServerPort();
	}

	@Override
	public ServerConfiguration getServerConfiguration() {
		return thriftServer.getServerConfiguration();
	}

	@Override
	public TProcessor getProcessor() {
		return thriftServer.getProcessor();
	}

}
