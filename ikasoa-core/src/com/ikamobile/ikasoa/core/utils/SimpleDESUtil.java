package com.ikamobile.ikasoa.core.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 简单DES加解密工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class SimpleDESUtil {

	private static final String ALGORITHM_DES = "DES";

	/**
	 * 加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密后数据
	 * @exception Exception
	 */
	public static byte[] encrypt(byte[] data, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		cipher.init(Cipher.ENCRYPT_MODE,
				SecretKeyFactory.getInstance(ALGORITHM_DES).generateSecret(new DESKeySpec(key.getBytes())));
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密后数据
	 * @exception Exception
	 */
	public static byte[] decrypt(byte[] data, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		cipher.init(Cipher.DECRYPT_MODE,
				SecretKeyFactory.getInstance(ALGORITHM_DES).generateSecret(new DESKeySpec(key.getBytes())));
		return cipher.doFinal(data);
	}

}
