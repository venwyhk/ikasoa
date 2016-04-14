package com.ikamobile.ikasoa.service;

import org.sulei.core.STException;
import org.sulei.core.thrift.service.Service;
import org.sulei.core.utils.StringUtil;

import com.ikamobile.ikasoa.BaseGetService;
import com.ikamobile.ikasoa.handler.ProtocolHandler;

/**
 * 抽象服务端服务
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public abstract class AbstractGetService<T1, T2> implements Service, BaseGetService<T1, T2> {

	protected ProtocolHandler<T1, T2> protocolHandler;

	public AbstractGetService() {
	}

	@Override
	public String get(String argStr) throws STException {
		if (protocolHandler == null) {
			throw new STException("'protocolHandler' is null !");
		}
		if (StringUtil.isEmpty(argStr)) {
			throw new STException("'argStr' is null !");
		}
		try {
			return protocolHandler.resultToStr(get(protocolHandler.strToArg(argStr)));
		} catch (Throwable t) {
			throw new STException(t);
		}
	}

}
