package com.ikasoa.core.thrift.client.pool.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import com.ikasoa.core.thrift.client.pool.SocketPool;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;
import com.ikasoa.core.utils.ServerUtil;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单Socket连接池实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.3
 */
@NoArgsConstructor
@Slf4j
public class SimpleSocketPoolImpl implements SocketPool {

	/**
	 * 单个池容量
	 */
	private byte size = 0x10;

	/**
	 * 连接超时时间
	 */
	private int time = 0;

	private static Map<String, SimpleSocketPoolImpl> selfMap = new HashMap<>();

	/**
	 * 是否空闲 (true: 不空闲, false: 空闲)
	 */
	private boolean[] socketStatusArray = null;

	private Hashtable<Byte, ThriftSocket> socketPool;

	public SimpleSocketPoolImpl(byte size) {
		this.size = size;
	}

	public SimpleSocketPoolImpl(int time) {
		this.time = time;
	}

	public SimpleSocketPoolImpl(byte size, int time) {
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
	 * @return SimpleSocketPoolImpl 连接池对象
	 */
	public synchronized SimpleSocketPoolImpl init(String host, int port) {
		if (!ServerUtil.checkHostAndPort(host, port))
			throw new IllegalArgumentException("Server host or port is null !");
		SimpleSocketPoolImpl self = new SimpleSocketPoolImpl();
		selfMap.put(ServerUtil.buildCacheKey(host, port), self);
		self.socketPool = new Hashtable<>(size);
		self.socketStatusArray = new boolean[size];
		// 初始化连接池
		log.debug("Initiation pool ......");
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
		if (!ServerUtil.checkHostAndPort(host, port))
			throw new IllegalArgumentException("Server host or port is null !");
		SimpleSocketPoolImpl self = selfMap.get(ServerUtil.buildCacheKey(host, port));
		if (self == null)
			self = init(host, port);
		try {
			for (byte i = 0; i < size; i++) {
				self.socketPool.put(new Byte(i), new ThriftSocket(host, port, time));
				self.socketStatusArray[i] = Boolean.FALSE;
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
		if (!ServerUtil.checkHostAndPort(host, port))
			throw new IllegalArgumentException("Server host or port is null !");
		SimpleSocketPoolImpl self = selfMap.get(ServerUtil.buildCacheKey(host, port));
		if (self == null || self.socketStatusArray == null)
			self = init(host, port);
		byte i = 0;
		for (; i < size; i++)
			if (!self.socketStatusArray[i]) {
				ThriftSocket thriftSocket = getThriftSocket(self, i, host, port);
				if (!thriftSocket.isOpen()) {
					// 如果socket未连接,则新建一个socket
					// TODO: 用isOpen方法判断是否保持连接并不准确,所以这里有可能会额外创建一些socket
					thriftSocket = new ThriftSocket(host, port, time);
					self.socketPool.put(new Byte(i), thriftSocket);
				}
				self.socketStatusArray[i] = Boolean.TRUE;
				return thriftSocket;
			}
		// 如果连接不够用,就初始化连接池.
		try {
			for (i = 0; i < size; i++) {
				ThriftSocket thriftSocket = self.socketPool.get(i);
				if (self.socketStatusArray[i] && (thriftSocket == null || thriftSocket.getSocket() == null
						|| (thriftSocket.isOpen() && thriftSocket.getSocket().isClosed()))) {
					return new ThriftSocket(host, port, time);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		log.warn("Not enough pooled connection ! Again retry initiation pool .");
		init(host, port);
		return buildThriftSocket(host, port);
	}

	private ThriftSocket getThriftSocket(SimpleSocketPoolImpl self, byte i, String host, int port) {
		log.debug("Get socket number is {} .", i);
		ThriftSocket thriftSocket = self.socketPool.get(new Byte(i));
		if (thriftSocket == null || thriftSocket.getSocket() == null) {
			log.warn("Socket is null ! Again retry initiation pool .");
			init(host, port);
			return buildThriftSocket(host, port);
		} else
			return thriftSocket;
	}

	/**
	 * 回收ThriftSocket连接
	 */
	@Override
	public synchronized void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port) {
		if (thriftSocket == null || thriftSocket.getSocket() == null
				|| thriftSocket.getSocket().getInetAddress() == null) {
			log.warn("Release unsuccessful .");
			return;
		}
		if (!ServerUtil.checkHostAndPort(host, port)) {
			log.error("Server host or port is null ! Release unsuccessful .");
			return;
		}
		log.debug("Release socket , host is {} and port is {} .", host, port);
		SimpleSocketPoolImpl self = selfMap.get(ServerUtil.buildCacheKey(host, port));
		if (self == null)
			self = init(host, port);
		for (byte i = 0; i < size; i++)
			if (self.socketPool.get(new Byte(i)) == thriftSocket) {
				self.socketStatusArray[i] = Boolean.FALSE;
				return;
			}
		// 如果socket不在池中,就直接关闭
		thriftSocket.close();
	}

	/**
	 * 回收所有ThriftSocket连接
	 * <p>
	 * 会断开所有连接.
	 */
	@Override
	public synchronized void releaseAllThriftSocket() {
		if (selfMap == null || selfMap.size() == 0)
			return;
		ThriftSocket socket;
		for (Entry<String, SimpleSocketPoolImpl> entry : selfMap.entrySet()) {
			SimpleSocketPoolImpl self = entry.getValue();
			for (byte i = 0; i < size; i++) {
				socket = self.socketPool.get(new Byte(i));
				try {
					socket.close();
					self.socketStatusArray[i] = Boolean.FALSE;
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
	}

}
