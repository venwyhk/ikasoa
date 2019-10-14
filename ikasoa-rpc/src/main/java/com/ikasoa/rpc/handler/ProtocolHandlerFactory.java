package com.ikasoa.rpc.handler;

import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.rpc.handler.impl.JsonProtocolHandlerImpl;

import lombok.SneakyThrows;

/**
 * 转换协议处理器工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ProtocolHandlerFactory<T, R> {

	/**
	 * 默认转换协议处理器F实现
	 */
	private static final ProtocolHandler<?, ?> DEFAULT_PROTOCOL_HANDLER = new JsonProtocolHandlerImpl<>();

	public ProtocolHandler<T, R> getProtocolHandler(ReturnData resultData) {
		return getProtocolHandler(resultData, null);
	}

	@SneakyThrows
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ProtocolHandler<T, R> getProtocolHandler(ReturnData resultData, ProtocolHandler<?, ?> protocolHandler) {
		Class[] paramTypes = { ReturnData.class };
		Object[] params = { resultData };
		return ObjectUtil.isNull(protocolHandler)
				? (ProtocolHandler<T, R>) DEFAULT_PROTOCOL_HANDLER.getClass().getConstructor(paramTypes)
						.newInstance(params)
				: (ProtocolHandler<T, R>) protocolHandler.getClass().getConstructor(paramTypes).newInstance(params);
	}

}
