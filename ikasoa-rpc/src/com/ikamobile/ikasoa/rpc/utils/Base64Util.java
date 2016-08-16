package com.ikamobile.ikasoa.rpc.utils;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 编码工具
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class Base64Util {

	/**
	 * 编码
	 * 
	 * @param bstr
	 *            待编码数据
	 * @return String 编码后数据
	 */
	public static String encode(byte[] bstr) {
		return new BASE64Encoder().encode(bstr);
	}

	/**
	 * 解码
	 * 
	 * @param str
	 *            待解码数据
	 * @return byte[] 解码后数据
	 */
	public static byte[] decode(String str) {
		byte[] bt = null;
		try {
			bt = new BASE64Decoder().decodeBuffer(str);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return bt;
	}
}
