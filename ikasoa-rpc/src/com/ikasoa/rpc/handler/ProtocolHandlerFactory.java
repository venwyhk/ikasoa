package com.ikasoa.rpc.handler;

/**
 * 转换协议处理器工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ProtocolHandlerFactory<T1, T2> {

	private static final String DEFAULT_PROTOCOL_HANDLER_CLASS_STRING = "com.ikasoa.rpc.handler.impl.JsonProtocolHandlerImpl";

	public ProtocolHandler<T1, T2> getProtocolHandler(ReturnData resultData) {
		return getProtocolHandler(resultData, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ProtocolHandler<T1, T2> getProtocolHandler(ReturnData resultData,
			Class<ProtocolHandler> protocolHandlerClass) {
		try {
			Class[] paramTypes = { ReturnData.class };
			Object[] params = { resultData };
			if (protocolHandlerClass == null) {
				Class defaultProtocolHandlerClass = Class.forName(DEFAULT_PROTOCOL_HANDLER_CLASS_STRING);
				return (ProtocolHandler<T1, T2>) defaultProtocolHandlerClass.getConstructor(paramTypes)
						.newInstance(params);
			} else {
				return (ProtocolHandler<T1, T2>) protocolHandlerClass.getConstructor(paramTypes).newInstance(params);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
