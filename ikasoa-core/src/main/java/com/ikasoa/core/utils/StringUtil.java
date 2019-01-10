package com.ikasoa.core.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * 字符串常用操作工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@UtilityClass
public class StringUtil {

	private final static String HEX_16 = "0123456789ABCDEF";

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean andIsEmpty(String... strs) {
		return Arrays.asList(strs).stream().filter(StringUtil::isNotEmpty).count() == 0;
	}

	public static boolean orIsEmpty(String... strs) {
		return Arrays.asList(strs).stream().filter(StringUtil::isEmpty).count() > 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return Boolean.TRUE;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return Boolean.FALSE;
		return Boolean.TRUE;
	}

	public static boolean andIsBlank(String... strs) {
		return Arrays.asList(strs).stream().filter(StringUtil::isNotBlank).count() == 0;
	}

	public static boolean orIsBlank(String... strs) {
		return Arrays.asList(strs).stream().filter(StringUtil::isBlank).count() > 0;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null && str2 == null ? Boolean.TRUE
				: str1 != null && str2 != null ? str1.equals(str2) : Boolean.FALSE;
	}

	public static byte[] strToBytes(String str) {
		return hexStrToBytes(strToHexStr(str));
	}

	public static String bytesToStr(byte[] bytes) {
		return hexStrToStr(bytesToHexStr(bytes));
	}

	public static String strToHexStr(String str) {
		if (isEmpty(str))
			return null;
		char[] chars = HEX_16.toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bytes = str.getBytes();
		int bit;
		for (int i = 0; i < bytes.length; i++) {
			bit = (bytes[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bytes[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}

	public static String hexStrToStr(String hexStr) {
		if (isEmpty(hexStr))
			return null;
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = HEX_16.indexOf(hexs[2 * i]) * 16;
			n += HEX_16.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	public static byte[] hexStrToBytes(String hexStr) {
		if (hexStr == null)
			return null;
		if (hexStr.length() == 0)
			return new byte[0];
		byte[] byteArray = new byte[hexStr.length() / 2];
		for (int i = 0; i < byteArray.length; i++)
			byteArray[i] = ((byte) Integer.parseInt(hexStr.substring(2 * i, 2 * i + 2), 16));
		return byteArray;
	}

	public static String bytesToHexStr(byte[] bytes) {
		if (bytes == null)
			return null;
		char[] hexArray = HEX_16.toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			int x = bytes[i] & 0xFF;
			hexChars[i * 2] = hexArray[x >>> 4];
			hexChars[i * 2 + 1] = hexArray[x & 0x0F];
		}
		return new String(hexChars);
	}

	@SneakyThrows
	public static int toInt(String str) {
		return isNotEmpty(str) ? Integer.parseInt(str.trim()) : 0;
	}

	@SneakyThrows
	public static String toMD5(String str) {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		return new BigInteger(1, md.digest()).toString(16);
	}

}
