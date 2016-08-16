package com.ikamobile.ikasoa.rpc;

import java.util.Map;
import java.util.Set;

import com.ikamobile.ikasoa.core.thrift.server.ThriftServer;
import com.ikamobile.ikasoa.core.thrift.service.Service;

/**
 * IKASOA服务端接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface IkasoaServer extends ThriftServer {

	/**
	 * 获取一批通用服务集合
	 * 
	 * @return Map 通用服务集合
	 */
	public Map<String, Service> getIkasoaServiceMap();

	/**
	 * 获取通用服务
	 * 
	 * @param serviceKey
	 *            服务标识
	 * @return Map 通用服务集合
	 */
	public Service getIkasoaService(String serviceKey);

	/**
	 * 获取所有服务标识
	 * 
	 * @return Set 服务标识集合
	 */
	public Set<String> getIkasoaServiceKeys();

}
