package com.ikasoa.core.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.experimental.UtilityClass;

/**
 * Date工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.3
 */
@UtilityClass
public class DateUtil {

	private final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static Date now() {
		return new Date();
	}

	public static String dateToString(Date date) {
		return dateToString(date, DEFAULT_FORMAT);
	}

	public static String dateToString(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static Date stringToDate(String dateString) {
		return stringToDate(dateString, DEFAULT_FORMAT);
	}

	public static Date stringToDate(String dateString, String format) {
		return new SimpleDateFormat(format).parse(dateString, new ParsePosition(0));
	}

}
