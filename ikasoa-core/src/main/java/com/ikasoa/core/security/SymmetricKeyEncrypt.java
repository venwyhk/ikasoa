package com.ikasoa.core.security;

/**
 * 对称加密接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.5.4
 */
public interface SymmetricKeyEncrypt {

	/**
	 * 对数据进行加密.
	 * 
	 * @param data
	 *            待进行加密的数据
	 * @param key
	 *            加密的key
	 * @return 返回经过加密后的数据
	 * @throws Exception
	 *             异常
	 */
	String encrypt(String data, String key) throws Exception;

	/**
	 * 对用加密过的数据进行解密.
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            加密的key
	 * @return 返回解密后的数据
	 * @throws Exception
	 *             异常
	 */
	String decrypt(String data, String key) throws Exception;

}
