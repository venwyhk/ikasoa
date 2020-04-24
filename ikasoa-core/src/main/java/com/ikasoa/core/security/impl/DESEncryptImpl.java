package com.ikasoa.core.security.impl;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.ikasoa.core.security.SymmetricKeyEncrypt;
import com.ikasoa.core.utils.StringUtil;

import lombok.NoArgsConstructor;

/**
 * DES加解密实现类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
@NoArgsConstructor
public class DESEncryptImpl implements SymmetricKeyEncrypt {

	/**
	 * 加密算法
	 */
	private static final String ALGORITHM = "DES";

	/**
	 * 对数据进行DES加密.
	 * 
	 * @param data
	 *            待进行加密的数据
	 * @param key
	 *            加密的key
	 * @return 返回经过加密后的数据
	 * @throws Exception
	 *             异常
	 */
	@Override
	public String encrypt(String data, String key) throws Exception {
		if (StringUtil.orIsEmpty(data, key))
			return null;
		return byte2hex(encrypt(data.getBytes(), key.getBytes()));
	}

	/**
	 * 对用DES加密过的数据进行解密.
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            加密的key
	 * @return 返回解密后的数据
	 * @throws Exception
	 *             异常
	 */
	@Override
	public String decrypt(String data, String key) throws Exception {
		if (StringUtil.orIsEmpty(data, key))
			return null;
		return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
	}

	/**
	 * 用指定的key对数据进行DES加密.
	 * 
	 * @param data
	 *            待加密的数据
	 * @param key
	 *            加密的key
	 * @return 返回加密后的数据
	 * @throws Exception
	 *             异常
	 */
	private byte[] encrypt(byte[] data, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance(ALGORITHM).generateSecret(new DESKeySpec(key)),
				new SecureRandom());
		return cipher.doFinal(data);
	}

	/**
	 * 用指定的key对数据进行DES解密.
	 * 
	 * @param data
	 *            待解密的数据
	 * @param key
	 *            DES解密的key
	 * @return 返回DES解密后的数据
	 * @throws Exception
	 *             异常
	 */
	private byte[] decrypt(byte[] data, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance(ALGORITHM).generateSecret(new DESKeySpec(key)),
				new SecureRandom());
		return cipher.doFinal(data);
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("Data length is not even number !");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2)
			b2[n / 2] = (byte) Integer.parseInt(new String(b, n, 2), 16);
		return b2;
	}

	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0");
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
}