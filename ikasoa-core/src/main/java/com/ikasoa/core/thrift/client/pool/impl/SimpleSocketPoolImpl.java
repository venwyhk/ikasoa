package com.ikasoa.core.thrift.client.pool.impl;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.client.pool.ClientSocketPoolParameters;
import com.ikasoa.core.thrift.client.pool.SocketPool;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;
import com.ikasoa.core.utils.MapUtil;
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
	private byte size = defaultSize;

	private static Map<String, SimpleSocketPoolImpl> selfMap = MapUtil.newHashMap();

	/**
	 * 是否空闲 (true: 不空闲, false: 空闲)
	 */
	private boolean[] socketStatusArray = null;

	private Hashtable<Byte, ThriftSocket> socketPool;

	public SimpleSocketPoolImpl(byte size) {
		this.size = size;
	}

	/**
	 * 初始化连接池
	 * 
	 * @param parameters
	 *            客户端Socket参数对象
	 * @return SimpleSocketPoolImpl 连接池对象
	 */
	public synchronized SimpleSocketPoolImpl init(ClientSocketPoolParameters parameters) {
		if (!ServerUtil.checkHostAndPort(parameters.getHost(), parameters.getPort()))
			throw new IllegalArgumentException("Server host or port is null !");
		SimpleSocketPoolImpl self = new SimpleSocketPoolImpl();
		selfMap.put(parameters.getKey(), self);
		self.socketPool = MapUtil.newHashtable(size);
		self.socketStatusArray = new boolean[size];
		// 初始化连接池
		log.debug("Initiation pool ......");
		buildThriftSocketPool(parameters);
		return self;
	}

	/**
	 * 创建连接池
	 * 
	 * @param parameters
	 *            Socket连接池参数对象
	 */
	public synchronized void buildThriftSocketPool(ClientSocketPoolParameters parameters) {
		if (!ServerUtil.checkHostAndPort(parameters.getHost(), parameters.getPort()))
			throw new IllegalArgumentException("Server host or port is null !");
		SimpleSocketPoolImpl self = selfMap.get(parameters.getKey());
		if (self == null)
			self = init(parameters);
		try {
			for (byte i = 0; i < size; i++) {
				self.socketPool.put(new Byte(i), parameters.buildClientThriftSocket());
				self.socketStatusArray[i] = false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从连接池中获取一个空闲的ThriftSocket连接
	 * 
	 * @param parameters
	 *            Socket连接池参数对象
	 * @throws IkasoaException
	 *             异常
	 */
	@Override
	public synchronized ThriftSocket buildThriftSocket(ClientSocketPoolParameters parameters) throws IkasoaException {
		if (!ServerUtil.checkHostAndPort(parameters.getHost(), parameters.getPort()))
			throw new IllegalArgumentException("Server host or port is null !");
		SimpleSocketPoolImpl self = selfMap.get(parameters.getKey());
		if (self == null || self.socketStatusArray == null)
			self = init(parameters);
		byte i = 0;
		for (; i < size; i++)
			if (!self.socketStatusArray[i]) {
				ThriftSocket thriftSocket = getThriftSocket(self, i, parameters);
				if (!thriftSocket.isOpen()) {
					// 如果socket未连接,则新建一个socket
					// TODO: 用isOpen方法判断是否保持连接并不准确,所以这里有可能会额外创建一些socket
					thriftSocket = parameters.buildClientThriftSocket();
					self.socketPool.put(new Byte(i), thriftSocket);
				}
				self.socketStatusArray[i] = true;
				return thriftSocket;
			}
		// 如果连接不够用,就初始化连接池.
		try {
			for (i = 0; i < size; i++) {
				ThriftSocket thriftSocket = self.socketPool.get(i);
				if (self.socketStatusArray[i] && (thriftSocket == null || thriftSocket.getSocket() == null
						|| (thriftSocket.isOpen() && thriftSocket.getSocket().isClosed()))) {
					return parameters.buildClientThriftSocket();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		log.warn("Not enough pooled connection ! Again retry initiation pool .");
		init(parameters);
		return buildThriftSocket(parameters);
	}

	private ThriftSocket getThriftSocket(SimpleSocketPoolImpl self, byte i, ClientSocketPoolParameters parameters)
			throws IkasoaException {
		log.debug("Get socket number is {} .", i);
		ThriftSocket thriftSocket = self.socketPool.get(new Byte(i));
		if (thriftSocket == null || thriftSocket.getSocket() == null) {
			log.warn("Socket is null ! Again retry initiation pool .");
			init(parameters);
			return buildThriftSocket(parameters);
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
			log.debug("Release unsuccessful .");
			return;
		}
		if (!ServerUtil.checkHostAndPort(host, port)) {
			log.error("Server host or port is null ! Release unsuccessful .");
			return;
		}
		log.debug("Release socket , host is {} and port is {} .", host, port);
		SimpleSocketPoolImpl self = selfMap.get(ServerUtil.buildCacheKey(host, port));
		if (self == null)
			return;
		for (byte i = 0; i < size; i++)
			if (self.socketPool.get(new Byte(i)) == thriftSocket) {
				self.socketStatusArray[i] = false;
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
		if (selfMap == null || selfMap.isEmpty())
			return;
		ThriftSocket socket;
		for (Entry<String, SimpleSocketPoolImpl> entry : selfMap.entrySet()) {
			SimpleSocketPoolImpl self = entry.getValue();
			for (byte i = 0; i < size; i++) {
				socket = self.socketPool.get(new Byte(i));
				try {
					socket.close();
					self.socketStatusArray[i] = false;
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
	}

}
