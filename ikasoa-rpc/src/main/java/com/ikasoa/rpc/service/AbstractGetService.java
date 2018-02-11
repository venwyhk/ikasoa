package com.ikasoa.rpc.service;

import com.ikasoa.core.STException;
import com.ikasoa.core.thrift.service.Service;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.BaseGetService;

/**
 * 抽象服务端服务
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public abstract class AbstractGetService<T, R> implements Service, BaseGetService<T, R> {

	protected ProtocolHandler<T, R> protocolHandler;

	public AbstractGetService() {
		// Do nothing
	}

	@Override
	public String get(String argStr) throws STException {
		if (protocolHandler == null)
			throw new STException("'protocolHandler' is null !");
		if (StringUtil.isEmpty(argStr))
			throw new STException("'argStr' is null !");
		try {
			return protocolHandler.resultToStr(get(protocolHandler.strToArg(argStr)));
		} catch (Throwable t) {
			throw new STException(t);
		}
	}

}
