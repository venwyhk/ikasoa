package com.ikamobile.ikasoa.core.thrift;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Socket连接池
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.3
 */
public class SocketPool {

	private static final Logger LOG = LoggerFactory.getLogger(SocketPool.class);

	private static Map<String, SocketPool> selfMap = new HashMap<String, SocketPool>();

	private Hashtable<Integer, ThriftSocket> socketPool = null;

	// 默认单个连接池容量
	private static final int POOL_SIZE = 10;

	// 是否空闲 (true: 不空闲, false: 空闲)
	private boolean[] socketStatusArray = null;

	/**
	 * 初始化连接池
	 */
	public static synchronized SocketPool init(String host, int port) {
		if (!checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		SocketPool self = new SocketPool();
		selfMap.put(getPoolKey(host, port), self);
		self.socketPool = new Hashtable<Integer, ThriftSocket>();
		self.socketStatusArray = new boolean[POOL_SIZE];
		// 初始化连接池
		LOG.debug("Initiation pool ......");
		buildThriftSocketPool(host, port);
		return self;
	}

	/**
	 * 创建连接池
	 */
	public synchronized static void buildThriftSocketPool(String host, int port) {
		if (!checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		SocketPool self = selfMap.get(getPoolKey(host, port));
		if (self == null) {
			self = init(host, port);
		}
		ThriftSocket socket = null;
		try {
			for (int i = 0; i < POOL_SIZE; i++) {
				socket = new ThriftSocket(host, port);
				self.socketPool.put(new Integer(i), socket);
				self.socketStatusArray[i] = false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从连接池中获取一个空闲的连接
	 */
	public static ThriftSocket buildThriftSocket(String host, int port) {
		if (!checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		SocketPool self = selfMap.get(getPoolKey(host, port));
		if (self == null || self.socketStatusArray == null) {
			self = init(host, port);
		}
		int i = 0;
		for (i = 0; i < POOL_SIZE; i++) {
			if (!self.socketStatusArray[i]) {
				self.socketStatusArray[i] = true;
				break;
			}
		}
		if (i >= POOL_SIZE) {
			LOG.error("No enough pooled connection ! Again retry initiation pool .");
			init(host, port);
			return buildThriftSocket(host, port);
		}
		LOG.debug("Get socket number is " + i + " .");
		ThriftSocket thriftSocket = self.socketPool.get(new Integer(i));
		if (thriftSocket != null) {
			return thriftSocket;
		} else {
			LOG.warn("Pool is null ! Again retry initiation pool .");
			init(host, port);
			return buildThriftSocket(host, port);
		}
	}

	/**
	 * 将用完的连接放回池中,并调整为空闲状态
	 * <p>
	 * 服务地址和IP将从Socket中获取.
	 */
	public static void releaseThriftSocket(ThriftSocket socket) {
		if (socket == null || socket.getSocket() == null || socket.getSocket().getInetAddress() == null) {
			LOG.warn("Release unsuccessful .");
			return;
		}
		releaseThriftSocket(socket, socket.getSocket().getInetAddress().getHostName(), socket.getSocket().getPort());
	}

	/**
	 * 将用完的连接放回池中,并调整为空闲状态
	 */
	public static void releaseThriftSocket(ThriftSocket socket, String host, int port) {
		if (socket == null || !checkHostAndPort(host, port)) {
			LOG.error("Server host or port is null ! Release unsuccessful .");
			return;
		}
		SocketPool self = selfMap.get(getPoolKey(host, port));
		if (self == null) {
			self = init(host, port);
		}
		for (int i = 0; i < POOL_SIZE; i++) {
			if (self.socketPool.get(new Integer(i)) == socket) {
				self.socketStatusArray[i] = false;
				LOG.debug("Release socket number is " + i + " .");
				break;
			}
		}
	}

	/**
	 * 断开池中所有连接
	 */
	public synchronized static void releaseAllThriftSocket() {
		if (selfMap == null || selfMap.size() == 0) {
			return;
		}
		ThriftSocket socket = null;
		for (Entry<String, SocketPool> entry : selfMap.entrySet()) {
			SocketPool self = entry.getValue();
			for (int i = 0; i < POOL_SIZE; i++) {
				socket = self.socketPool.get(new Integer(i));
				try {
					socket.close();
					self.socketStatusArray[i] = false;
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
		}
	}

	private static boolean checkHostAndPort(String host, int port) {
		return (host != null && port > 0);
	}

	private static String getPoolKey(String host, int port) {
		return new StringBuilder(host).append(":").append(port).toString();
	}
}
