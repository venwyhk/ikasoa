package com.ikasoa.rpc.handler;

/**
 * 转换协议处理器工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ProtocolHandlerFactory<T, R> {

	private static final String DEFAULT_PROTOCOL_HANDLER_CLASS_STRING = "com.ikasoa.rpc.handler.impl.JsonProtocolHandlerImpl";

	public ProtocolHandler<T, R> getProtocolHandler(ReturnData resultData) {
		return getProtocolHandler(resultData, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ProtocolHandler<T, R> getProtocolHandler(ReturnData resultData,
			Class<ProtocolHandler> protocolHandlerClass) {
		try {
			Class[] paramTypes = { ReturnData.class };
			Object[] params = { resultData };
			return protocolHandlerClass == null
					? (ProtocolHandler<T, R>) Class.forName(DEFAULT_PROTOCOL_HANDLER_CLASS_STRING)
							.getConstructor(paramTypes).newInstance(params)
					: (ProtocolHandler<T, R>) protocolHandlerClass.getConstructor(paramTypes).newInstance(params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
