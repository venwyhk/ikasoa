package com.ikasoa.core;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;

import com.ikasoa.core.utils.StringUtil;

import junit.framework.TestCase;
import lombok.Cleanup;
import lombok.SneakyThrows;

/**
 * 单元测试父类
 */
@Ignore
public class TestBase extends TestCase {

	private static Map<String, Integer> portCacheMap = new HashMap<>();

	protected final static String LOCAL_HOST = "localhost";

	protected final static String TEST_KEY8 = "12345678";

	protected final static String TEST_KEY = "87654321abcde";

	protected final static String TEST_STRING = "12345678abcdefgABCDEFG~!@#$%^&*()_+";

	protected int getNewPort() {
		return getNewPort(null);
	}

	@SneakyThrows
	protected int getNewPort(String key) {
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

}
