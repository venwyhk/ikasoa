package com.ikasoa.core.security.impl;

import com.ikasoa.core.security.SymmetricKeyEncrypt;
import com.ikasoa.core.utils.StringUtil;

import lombok.NoArgsConstructor;

/**
 * RC4加解密实现类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.5.4
 */
@NoArgsConstructor
public class RC4EncryptImpl implements SymmetricKeyEncrypt {

	/**
	 * 对数据进行RC4加密.
	 * 
	 * @param data
	 *            待进行加密的数据
	 * @param key
	 *            加密的key
	 * @return 返回经过加密后的数据 (String)
	 */
	@Override
	public String encrypt(String data, String key) {
		if (StringUtil.orIsEmpty(data, key))
			return null;
		return toHexString(asString(encryptByte(data, key)));
	}

	/**
	 * 对用RC4加密过的数据进行解密.
	 * 
	 * @param data
	 *            加密数据 (String)
	 * @param key
	 *            加密的key
	 * @return 返回解密后的数据
	 */
	@Override
	public String decrypt(String data, String key) {
		if (StringUtil.orIsEmpty(data, key))
			return null;
		return new String(rc4Base(toBytes(data), key));
	}

	/**
	 * 对数据进行RC4加密.
	 * 
	 * @param data
	 *            待进行加密的数据
	 * @param key
	 *            加密的key
	 * @return 返回经过加密后的数据 (byte)
	 */
	public byte[] encryptByte(String data, String key) {
		if (StringUtil.orIsEmpty(data, key))
			return null;
		return rc4Base(data.getBytes(), key);
	}

	/**
	 * 对用RC4加密过的数据进行解密.
	 * 
	 * @param data
	 *            加密数据 (Byte)
	 * @param key
	 *            加密的key
	 * @return 返回解密后的数据
	 */
	public String decrypt(byte[] data, String key) {
		if (data == null || StringUtil.isEmpty(key))
			return null;
		return asString(rc4Base(data, key));
	}

	private static String asString(byte[] buf) {
		StringBuffer strbuf = new StringBuffer(buf.length);
		for (int i = 0; i < buf.length; i++)
			strbuf.append((char) buf[i]);
		return strbuf.toString();
	}

	private static byte[] initKey(String aKey) {
		byte[] bKey = aKey.getBytes(), state = new byte[256];
		for (int i = 0; i < 256; i++)
			state[i] = (byte) i;
		int index1 = 0, index2 = 0;
		if (bKey == null || bKey.length == 0)
			return null;
		for (int i = 0; i < 256; i++) {
			index2 = ((bKey[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
			byte tmp = state[i];
			state[i] = state[index2];
			state[index2] = tmp;
			index1 = (index1 + 1) % bKey.length;
		}
		return state;
	}

	private static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch & 0xFF);
			if (s4.length() == 1)
				s4 = "0" + s4;
			str += s4;
		}
		return str; // 0x表示十六进制
	}

	private static byte[] toBytes(String src) {
		int size = src.length();
		byte[] ret = new byte[size / 2], tmp = src.getBytes();
		for (int i = 0; i < size / 2; i++)
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		return ret;
	}

	private static byte uniteBytes(byte src0, byte src1) {
		return (byte) ((char) ((char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue() << 4)
				^ (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue());
	}

	private byte[] rc4Base(byte[] input, String mKkey) {
		int x = 0, y = 0;
		int xorIndex;
		byte[] key = initKey(mKkey), result = new byte[input.length];
		for (int i = 0; i < input.length; i++) {
			x = (x + 1) & 0xff;
			y = ((key[x] & 0xff) + y) & 0xff;
			byte tmp = key[x];
			key[x] = key[y];
			key[y] = tmp;
			xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
			result[i] = (byte) (input[i] ^ key[xorIndex]);
		}
		return result;
	}

}
