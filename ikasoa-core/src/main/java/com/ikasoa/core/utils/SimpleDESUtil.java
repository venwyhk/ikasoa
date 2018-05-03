package com.ikasoa.core.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 简单DES加解密工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class SimpleDESUtil {

	/**
	 * 加密算法
	 */
	private static final String ALGORITHM = "DES";

	/**
	 * 对数据进行DES加密.
	 * 
	 * @param data
	 *            待进行DES加密的数据
	 * @param key
	 *            DES加密的key
	 * @return 返回经过DES加密后的数据
	 * @throws Exception
	 *             异常
	 */
	public static final String encrypt(String data, String key) throws Exception {
		return byte2hex(encrypt(data.getBytes(), key.getBytes()));
	}

	/**
	 * 对用DES加密过的数据进行解密.
	 * 
	 * @param data
	 *            DES加密数据
	 * @param key
	 *            DES加密的key
	 * @return 返回解密后的数据
	 * @throws Exception
	 *             异常
	 */
	public static final String decrypt(String data, String key) throws Exception {
		return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
	}

	/**
	 * 用指定的key对数据进行DES加密.
	 * 
	 * @param data
	 *            待加密的数据
	 * @param key
	 *            DES加密的key
	 * @return 返回DES加密后的数据
	 * @throws Exception
	 *             异常
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
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
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
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
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
}