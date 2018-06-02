package com.ikasoa.core.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 字符串常用操作工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@Slf4j
public class StringUtil {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
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

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean equals(String str1, String str2) {
		return isEmpty(str1) && isEmpty(str2) ? Boolean.TRUE
				: isNotEmpty(str1) && isNotEmpty(str2) ? str1.equals(str2) : Boolean.FALSE;
	}

	public static int toInt(String str) {
		if (isNotEmpty(str))
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				log.warn(e.getMessage());
				return 0;
			}
		return 0;
	}

}
