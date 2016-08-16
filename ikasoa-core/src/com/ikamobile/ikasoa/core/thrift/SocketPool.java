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

	// 默认单个连接池容量
	private static final byte DEFAULT_POOL_SIZE = 0x10;

	// 默认连接超时时间
	private static final int DEFAULT_TIMEOUT = 0;

	private static Map<String, SocketPool> selfMap = new HashMap<>();

	// 是否空闲 (true: 不空闲, false: 空闲)
	private boolean[] socketStatusArray = null;

	private Hashtable<Byte, ThriftSocket> socketPool = null;

	/**
	 * 初始化连接池
	 * 
	 * @param host
	 *            地址
	 * @param port
	 *            端口
	 * @return SocketPool 池对象
	 */
	public static synchronized SocketPool init(String host, int port) {
		if (!checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		SocketPool self = new SocketPool();
		selfMap.put(getPoolKey(host, port), self);
		self.socketPool = new Hashtable<Byte, ThriftSocket>(DEFAULT_POOL_SIZE);
		self.socketStatusArray = new boolean[DEFAULT_POOL_SIZE];
		// 初始化连接池
		LOG.debug("Initiation pool ......");
		buildThriftSocketPool(host, port);
		return self;
	}

	/**
	 * 创建连接池
	 * 
	 * @param host
	 *            地址
	 * @param port
	 *            端口
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
			for (byte i = 0; i < DEFAULT_POOL_SIZE; i++) {
				socket = new ThriftSocket(host, port, DEFAULT_TIMEOUT);
				self.socketPool.put(new Byte(i), socket);
				self.socketStatusArray[i] = false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从连接池中获取一个空闲的连接
	 * 
	 * @param host
	 *            地址
	 * @param port
	 *            端口
	 * @return SocketPool 池对象
	 */
	public synchronized static ThriftSocket buildThriftSocket(String host, int port) {
		if (!checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		SocketPool self = selfMap.get(getPoolKey(host, port));
		if (self == null || self.socketStatusArray == null) {
			self = init(host, port);
		}
		byte i = 0;
		for (; i < DEFAULT_POOL_SIZE; i++) {
			if (!self.socketStatusArray[i]) {
				self.socketStatusArray[i] = true;
				return getThriftSocket(self, i, host, port);
			}
		}
		// 如果连接不够用,就初始化连接池.
		try {
			for (i = 0; i < DEFAULT_POOL_SIZE; i++) {
				ThriftSocket thriftSocket = self.socketPool.get(i);
				if (self.socketStatusArray[i] && (thriftSocket == null || thriftSocket.getSocket() == null
						|| (thriftSocket.isOpen() && thriftSocket.getSocket().isClosed()))) {
					thriftSocket = new ThriftSocket(host, port, DEFAULT_TIMEOUT);
					return thriftSocket;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		LOG.warn("Not enough pooled connection ! Again retry initiation pool .");
		init(host, port);
		return buildThriftSocket(host, port);
	}

	private static ThriftSocket getThriftSocket(SocketPool self, byte i, String host, int port) {
		LOG.debug("Get socket number is " + i + " .");
		ThriftSocket thriftSocket = self.socketPool.get(new Byte(i));
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
	 * 
	 * @param thriftSocket
	 *            ThriftSocket对象
	 */
	public synchronized static void releaseThriftSocket(ThriftSocket thriftSocket) {
		if (thriftSocket == null || thriftSocket.getSocket() == null
				|| thriftSocket.getSocket().getInetAddress() == null) {
			LOG.warn("Release unsuccessful .");
			return;
		}
		releaseThriftSocket(thriftSocket, thriftSocket.getSocket().getInetAddress().getHostName(),
				thriftSocket.getSocket().getPort());
	}

	/**
	 * 将用完的连接放回池中,并调整为空闲状态
	 * 
	 * @param thriftSocket
	 *            ThriftSocket对象
	 * @param host
	 *            地址
	 * @param port
	 *            端口
	 */
	public synchronized static void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port) {
		if (thriftSocket == null || !checkHostAndPort(host, port)) {
			LOG.error("Server host or port is null ! Release unsuccessful .");
			return;
		}
		SocketPool self = selfMap.get(getPoolKey(host, port));
		if (self == null) {
			self = init(host, port);
		}
		for (byte i = 0; i < DEFAULT_POOL_SIZE; i++) {
			if (self.socketPool.get(new Byte(i)) == thriftSocket) {
				self.socketStatusArray[i] = false;
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
			for (byte i = 0; i < DEFAULT_POOL_SIZE; i++) {
				socket = self.socketPool.get(new Byte(i));
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
