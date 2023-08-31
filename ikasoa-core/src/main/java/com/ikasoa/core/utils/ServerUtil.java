package com.ikasoa.core.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * 服务器相关工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
@UtilityClass
public class ServerUtil {

	private static Map<String, Integer> portCacheMap = MapUtil.newHashMap();

	/**
	 * 获取本地IP地址(IPv4)
	 */
	public static String getLocalIPv4() {
		try {
			for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
				NetworkInterface item = e.nextElement();
				for (InterfaceAddress address : item.getInterfaceAddresses()) {
					if (item.isLoopback() || !item.isUp())
						continue;
					if (address.getAddress() instanceof Inet4Address)
						return ((Inet4Address) address.getAddress()).getHostAddress();
				}
			}
			return InetAddress.getLocalHost().getHostAddress();
		} catch (SocketException | UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isIPv4(String ip) {
		return Pattern
				.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
				.matcher(ip).matches();
	}

	public static int getNewPort() {
		return getNewPort(null);
	}

	@SneakyThrows
	public static int getNewPort(String key) {
		@Cleanup
		ServerSocket socket = new ServerSocket(0);
		if (StringUtil.isNotEmpty(key))
			if (portCacheMap.containsKey(key))
				return portCacheMap.get(key);
			else {
				int port = socket.getLocalPort();
				portCacheMap.put(key, port);
				return port;
			}
		else
			return socket.getLocalPort();
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

	public static String buildCacheKey(String serverHost, int serverPort) {
		return StringUtil.merge(serverHost, ":", serverPort);
	}

}
