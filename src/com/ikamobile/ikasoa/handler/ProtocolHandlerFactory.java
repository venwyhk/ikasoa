package com.ikamobile.ikasoa.handler;

import com.ikamobile.ikasoa.handler.impl.JsonProtocolHandlerImpl;
import com.ikamobile.ikasoa.handler.impl.KryoProtocolHandlerImpl;
import com.ikamobile.ikasoa.handler.impl.XmlProtocolHandlerImpl;

/**
 * 转换协议处理器工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ProtocolHandlerFactory<T1, T2> {

	public ProtocolHandler<T1, T2> getProtocolHandler(Class<?>[] argClasses, ReturnData resultData) {
		return getProtocolHandler(argClasses, resultData, null);
	}

	public ProtocolHandler<T1, T2> getProtocolHandler(Class<?>[] argClasses, ReturnData resultData,
			ProtocolType protocolType) {
		if (protocolType != null) {
			switch (protocolType) {
			case JSON:
				return new JsonProtocolHandlerImpl<T1, T2>(argClasses, resultData);
			case KRYO:
				return new KryoProtocolHandlerImpl<T1, T2>(resultData);
			case XML:
				return new XmlProtocolHandlerImpl<T1, T2>(resultData);
			default:
				return new JsonProtocolHandlerImpl<T1, T2>(argClasses, resultData);
			}
		} else {
			return new JsonProtocolHandlerImpl<T1, T2>(argClasses, resultData);
		}

	}

	public enum ProtocolType {
		JSON, KRYO, XML;
	}

}
