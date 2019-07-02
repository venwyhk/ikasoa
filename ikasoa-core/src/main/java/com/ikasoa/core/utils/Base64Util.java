package com.ikasoa.core.utils;

import java.util.Base64;

import lombok.experimental.UtilityClass;

/**
 * 编码工具
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.2
 */
@UtilityClass
public class Base64Util {

	/**
	 * 编码
	 * 
	 * @param bstr
	 *            待编码数据
	 * @return String 编码后数据
	 */
	public static String encode(byte[] bstr) {
		return Base64.getEncoder().encodeToString(bstr);
	}

	/**
	 * 解码
	 * 
	 * @param str
	 *            待解码数据
	 * @return byte[] 解码后数据
	 */
	public static byte[] decode(String str) {
		return Base64.getDecoder().decode(str);
	}
}
