package com.ikasoa.core.security.impl;

import com.ikasoa.core.security.SymmetricKeyEncrypt;
import com.ikasoa.core.utils.StringUtil;

/**
 * Caesar加解密实现类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.3
 */
public class CaesarEncryptImpl implements SymmetricKeyEncrypt {

	/**
	 * 对数据进行Caesar加密.
	 * 
	 * @param data
	 *            待进行加密的数据
	 * @param key
	 *            加密的key
	 * @return String 返回经过加密后的数据
	 */
	@Override
	public String encrypt(String data, String key) throws Exception {
		return execute(data, getOffset(key));
	}

	/**
	 * 对用Caesar加密过的数据进行解密.
	 * 
	 * @param data
	 *            加密数据 (String)
	 * @param key
	 *            加密的key
	 * @return String 返回解密后的数据
	 */
	@Override
	public String decrypt(String data, String key) throws Exception {
		return execute(data, ~getOffset(key) + 1);
	}

	private int getOffset(String key) {
		if (StringUtil.isEmpty(key) || key.length() < 4)
			throw new IllegalArgumentException("Key invalid !");
		byte[] b = key.getBytes();
		int value = 0;
		for (byte i = 0; i < 4; i++)
			value += (b[i] & 0xFF) << (3 - i) * 8;
		return value % 25 + 1;
	}

	private String execute(String data, int offset) throws Exception {
		StringBuilder cipher = new StringBuilder();
		for (int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);
			if (c >= 'a' && c <= 'z') {
				c += offset % 26;
				if (c < 'a')
					c += 26;
				if (c > 'z')
					c -= 26;
			} else if (c >= 'A' && c <= 'Z') {
				c += offset % 26;
				if (c < 'A')
					c += 26;
				if (c > 'Z')
					c -= 26;
			}
			cipher.append(c);
		}
		return cipher.toString();
	}

}
