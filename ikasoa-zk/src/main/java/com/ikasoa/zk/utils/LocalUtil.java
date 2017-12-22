package com.ikasoa.zk.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import com.ikasoa.core.utils.StringUtil;

/**
 * 本地地址工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class LocalUtil {

	public static String getLocalIP() throws UnknownHostException, SocketException {
		if (isWindowsOS())
			return InetAddress.getLocalHost().getHostAddress();
		else
			return getLinuxLocalIp();
	}

	private static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (StringUtil.isNotEmpty(osName) && osName.toLowerCase().indexOf("windows") > -1)
			isWindowsOS = true;
		return isWindowsOS;
	}

	public static String getLocalHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}

	private static String getLinuxLocalIp() throws SocketException {
		String ip = "";
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			String name = intf.getName();
			if (!name.contains("docker") && !name.contains("lo"))
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String ipaddress = inetAddress.getHostAddress().toString();
						if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80"))
							ip = ipaddress;
					}
				}
		}
		return ip;
	}

}
