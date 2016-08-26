package com.ikamobile.ikasoa.core.thrift.client.pool.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikamobile.ikasoa.core.thrift.ThriftSocket;
import com.ikamobile.ikasoa.core.thrift.client.pool.SocketPool;

/**
 * Socket连接池默认实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.3
 */
public class DefaultSocketPoolImpl implements SocketPool {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultSocketPoolImpl.class);

	// 单个池容量
	private byte size = 0x10;

	// 连接超时时间
	private int time = 0;

	private static Map<String, DefaultSocketPoolImpl> selfMap = new HashMap<>();

	// 是否空闲 (true: 不空闲, false: 空闲)
	private boolean[] socketStatusArray = null;

	private Hashtable<Byte, ThriftSocket> socketPool = null;

	public DefaultSocketPoolImpl() {
	}

	public DefaultSocketPoolImpl(byte size, int time) {
		this.size = size;
		this.time = time;
	}

	/**
	 * 初始化连接池
	 * 
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 * @return DefaultSocketPoolImpl 默认的连接池对象
	 */
	public synchronized DefaultSocketPoolImpl init(String host, int port) {
		if (!checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		DefaultSocketPoolImpl self = new DefaultSocketPoolImpl();
		selfMap.put(getPoolKey(host, port), self);
		self.socketPool = new Hashtable<Byte, ThriftSocket>(size);
		self.socketStatusArray = new boolean[size];
		// 初始化连接池
		LOG.debug("Initiation pool ......");
		buildThriftSocketPool(host, port);
		return self;
	}

	/**
	 * 创建连接池
	 * 
	 * @param host
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 */
	public synchronized void buildThriftSocketPool(String host, int port) {
		if (!checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		DefaultSocketPoolImpl self = selfMap.get(getPoolKey(host, port));
		if (self == null) {
			self = init(host, port);
		}
		ThriftSocket socket = null;
		try {
			for (byte i = 0; i < size; i++) {
				socket = new ThriftSocket(host, port, time);
				self.socketPool.put(new Byte(i), socket);
				self.socketStatusArray[i] = false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从连接池中获取一个空闲的ThriftSocket连接
	 */
	@Override
	public synchronized ThriftSocket buildThriftSocket(String host, int port) {
		if (!checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		DefaultSocketPoolImpl self = selfMap.get(getPoolKey(host, port));
		if (self == null || self.socketStatusArray == null) {
			self = init(host, port);
		}
		byte i = 0;
		for (; i < size; i++) {
			if (!self.socketStatusArray[i]) {
				self.socketStatusArray[i] = true;
				return getThriftSocket(self, i, host, port);
			}
		}
		// 如果连接不够用,就初始化连接池.
		try {
			for (i = 0; i < size; i++) {
				ThriftSocket thriftSocket = self.socketPool.get(i);
				if (self.socketStatusArray[i] && (thriftSocket == null || thriftSocket.getSocket() == null
						|| (thriftSocket.isOpen() && thriftSocket.getSocket().isClosed()))) {
					thriftSocket = new ThriftSocket(host, port, time);
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

	private ThriftSocket getThriftSocket(DefaultSocketPoolImpl self, byte i, String host, int port) {
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
	 * 回收ThriftSocket连接
	 */
	@Override
	public synchronized void releaseThriftSocket(ThriftSocket thriftSocket) {
		if (thriftSocket == null || thriftSocket.getSocket() == null
				|| thriftSocket.getSocket().getInetAddress() == null) {
			LOG.warn("Release unsuccessful .");
			return;
		}
		releaseThriftSocket(thriftSocket, thriftSocket.getSocket().getInetAddress().getHostName(),
				thriftSocket.getSocket().getPort());
	}

	/**
	 * 回收ThriftSocket连接
	 */
	@Override
	public synchronized void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port) {
		if (thriftSocket == null || !checkHostAndPort(host, port)) {
			LOG.error("Server host or port is null ! Release unsuccessful .");
			return;
		}
		DefaultSocketPoolImpl self = selfMap.get(getPoolKey(host, port));
		if (self == null) {
			self = init(host, port);
		}
		for (byte i = 0; i < size; i++) {
			if (self.socketPool.get(new Byte(i)) == thriftSocket) {
				self.socketStatusArray[i] = false;
				break;
			}
		}
	}

	/**
	 * 回收所有ThriftSocket连接
	 * <p>
	 * 会断开所有连接.
	 */
	@Override
	public synchronized void releaseAllThriftSocket() {
		if (selfMap == null || selfMap.size() == 0) {
			return;
		}
		ThriftSocket socket = null;
		for (Entry<String, DefaultSocketPoolImpl> entry : selfMap.entrySet()) {
			DefaultSocketPoolImpl self = entry.getValue();
			for (byte i = 0; i < size; i++) {
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

	private boolean checkHostAndPort(String host, int port) {
		return (host != null && port > 0);
	}

	private String getPoolKey(String host, int port) {
		return new StringBuilder(host).append(":").append(port).toString();
	}
}
