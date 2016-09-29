package com.ikamobile.ikasoa.core.thrift.protocol;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

import com.ikamobile.ikasoa.core.utils.Base64Util;
import com.ikamobile.ikasoa.core.utils.SimpleDESUtil;
import com.ikamobile.ikasoa.core.utils.StringUtil;

/**
 * 基于DES加密的序列化实现
 * <p>
 * 继承于<code>TCompactProtocol</code>
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class SecurityCompactProtocol extends TCompactProtocol {

	private final String key;

	public static class Factory implements TProtocolFactory {

		private static final long serialVersionUID = 1L;

		private final String key_;

		public Factory(String key) {
			if (StringUtil.isNotEmpty(key)) {
				this.key_ = key;
			} else {
				throw new RuntimeException("Key is null !");
			}
		}

		public TProtocol getProtocol(TTransport trans) {
			return new SecurityCompactProtocol(trans, key_);
		}
	}

	public SecurityCompactProtocol(TTransport transport, String key) {
		super(transport);
		if (StringUtil.isEmpty(key)) {
			throw new RuntimeException("Key is null !");
		}
		if (key.length() < 8) {
			this.key = formatStr(key, 8);
		} else {
			this.key = key;
		}
	}

	@Override
	public void writeString(String str) throws TException {
		if (StringUtil.isEmpty(str)) {
			super.writeString(str);
		}
		try {
			super.writeString(Base64Util.encode(SimpleDESUtil.encrypt(str.getBytes(), getKey())));
		} catch (Exception e) {
			throw new TException(e);
		}
	}

	@Override
	public String readString() throws TException {
		String str = super.readString();
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		try {
			return new String(SimpleDESUtil.decrypt(Base64Util.decode(str.toCharArray()), getKey()));
		} catch (Exception e) {
			throw new TException(e);
		}
	}

	private String getKey() {
		if (key.length() < 8) {
			return formatStr(key, 8);
		} else {
			return key;
		}
	}

	private static String formatStr(String str, int length) {
		int strLen = str.getBytes().length;
		if (strLen == length) {
			return str;
		} else if (strLen < length) {
			int temp = length - strLen;
			String tem = "";
			for (int i = 0; i < temp; i++) {
				tem = tem + " ";
			}
			return str + tem;
		} else {
			return str.substring(0, length);
		}
	}
}
