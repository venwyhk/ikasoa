package com.ikasoa.core.thrift.protocol;

import org.apache.thrift.protocol.TProtocolFactory;

import com.ikasoa.core.security.impl.DESEncryptImpl;

/**
 * 基于DES加密的序列化工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class DESCompactProtocolFactory extends SecurityCompactProtocol.Factory implements TProtocolFactory {

	private static final long serialVersionUID = 3160327006165069226L;

	public DESCompactProtocolFactory(String key) {
		super(key, new DESEncryptImpl());
	}

}