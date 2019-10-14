package com.ikasoa.rpc.service;

import com.ikasoa.core.thrift.service.Service;
import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.handler.ProtocolHandler;

import lombok.SneakyThrows;

import com.ikasoa.rpc.BaseGetService;
import com.ikasoa.rpc.RpcException;

/**
 * 抽象服务端服务
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public abstract class AbstractGetService<T, R> implements Service, BaseGetService<T, R> {

	protected ProtocolHandler<T, R> protocolHandler;

	@Override
	@SneakyThrows
	public String get(String argStr) throws RpcException {
		if (ObjectUtil.isNull(protocolHandler))
			throw new RpcException("'protocolHandler' is null !");
		if (StringUtil.isEmpty(argStr))
			throw new RpcException("'argStr' is null !");
		return protocolHandler.resultToStr(get(protocolHandler.strToArg(argStr)));
	}

}
