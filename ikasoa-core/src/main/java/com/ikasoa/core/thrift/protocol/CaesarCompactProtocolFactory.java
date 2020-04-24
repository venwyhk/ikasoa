package com.ikasoa.core.thrift.protocol;

import org.apache.thrift.protocol.TProtocolFactory;

import com.ikasoa.core.security.impl.CaesarEncryptImpl;

/**
 * 基于Caesar加密的序列化工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.3
 */
public class CaesarCompactProtocolFactory extends SecurityCompactProtocol.Factory implements TProtocolFactory {

	private static final long serialVersionUID = -7600792425778129842L;

	public CaesarCompactProtocolFactory(String key) {
		super(key, new CaesarEncryptImpl());
	}

}
