package com.ikasoa.core.utils;

import java.util.regex.Pattern;

/**
 * 服务器相关工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class ServerUtil {

	public static boolean isIpv4(String ip) {
		return Pattern
				.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
				.matcher(ip).matches();
	}

	public static boolean isPort(int serverPort) {
		return serverPort > 0 && serverPort <= 0xFFFF;
	}

	public static boolean isSocketPort(int serverPort) {
		return serverPort > 0x400 && serverPort <= 0xFFFF;
	}

	public static boolean checkHostAndPort(String serverHost, int serverPort) {
		return StringUtil.isNotEmpty(serverHost) && isPort(serverPort);
	}

	public static String getKey(String serverHost, int serverPort) {
		return new StringBuilder(serverHost).append(":").append(serverPort).toString();
	}

}
