package com.ikasoa.core.thrift.protocol;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

import com.ikasoa.core.security.impl.RC4EncryptImpl;

/**
 * 基于RC4加密的序列化工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.5.4
 */
public class RC4CompactProtocolFactory extends SecurityCompactProtocol.Factory implements TProtocolFactory {

	private static final long serialVersionUID = 4493597272193255035L;

	public RC4CompactProtocolFactory(String key) {
		super(key, new RC4EncryptImpl());
	}

	@Override
	public TProtocol getProtocol(TTransport trans) {
		return new SecurityCompactProtocol(trans, key_, encrypt_);
	}

}
