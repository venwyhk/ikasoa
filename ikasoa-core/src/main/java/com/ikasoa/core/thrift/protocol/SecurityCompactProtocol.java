package com.ikasoa.core.thrift.protocol;

import java.util.stream.IntStream;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

import com.ikasoa.core.security.SymmetricKeyEncrypt;
import com.ikasoa.core.utils.StringUtil;

/**
 * 安全的序列化实现
 * <p>
 * 继承于<code>TCompactProtocol</code>,该实现将对String进行加密.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class SecurityCompactProtocol extends TCompactProtocol {

	private static final short KEY_MAX_LENGHT = 8;

	private final String key;

	private final SymmetricKeyEncrypt encrypt;

	public static class Factory implements TProtocolFactory {

		private static final long serialVersionUID = 3451446981775690583L;

		protected final String key_;

		protected final SymmetricKeyEncrypt encrypt_;

		public Factory(final String key, SymmetricKeyEncrypt encrypt) {
			if (StringUtil.isEmpty(key))
				throw new IllegalArgumentException("Key invalid !");
			key_ = key;
			encrypt_ = encrypt;
		}

		public TProtocol getProtocol(TTransport trans) {
			return new SecurityCompactProtocol(trans,
					key_.length() < KEY_MAX_LENGHT ? formatStr(key_, KEY_MAX_LENGHT) : key_, encrypt_);
		}

	}

	public SecurityCompactProtocol(TTransport transport, final String key, SymmetricKeyEncrypt encrypt) {
		super(transport);
		this.key = key;
		this.encrypt = encrypt;
	}

	@Override
	public void writeString(String str) throws TException {
		if (StringUtil.isEmpty(str))
			super.writeString(str);
		else
			try {
				super.writeString(encrypt.encrypt(str, getKey()));
			} catch (Exception e) {
				throw new TException(e);
			}
	}

	@Override
	public String readString() throws TException {
		String str = super.readString();
		if (StringUtil.isEmpty(str))
			return str;
		try {
			return new String(encrypt.decrypt(str, getKey()));
		} catch (Exception e) {
			throw new TException(e);
		}
	}

	private String getKey() {
		return key.length() < KEY_MAX_LENGHT ? formatStr(key, KEY_MAX_LENGHT) : key;
	}

	private static String formatStr(final String str, int length) {
		int strLen = str.getBytes().length;
		if (strLen == length) {
			return str;
		} else if (strLen < length) {
			StringBuilder tem = new StringBuilder();
			IntStream.range(0, length - strLen).forEach(i -> tem.append(" "));
			return StringUtil.merge(str, tem);
		} else {
			return str.substring(0, length);
		}
	}

}
