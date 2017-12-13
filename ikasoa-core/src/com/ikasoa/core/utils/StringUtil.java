package com.ikasoa.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串常用操作工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class StringUtil {

	private static final Logger LOG = LoggerFactory.getLogger(StringUtil.class);

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
			if ((Character.isWhitespace(str.charAt(i)) == Boolean.FALSE))
				return Boolean.FALSE;
		return Boolean.TRUE;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean equals(String str1, String str2) {
		if (isEmpty(str1) && isEmpty(str2))
			return Boolean.TRUE;
		else if (!isEmpty(str1) && !isEmpty(str2))
			return str1.equals(str2);
		return Boolean.FALSE;
	}

	public static int toInt(String str) {
		if (str != null && !"".equals(str.trim()))
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				LOG.warn(e.getMessage());
				return 0;
			}
		return 0;
	}

}
