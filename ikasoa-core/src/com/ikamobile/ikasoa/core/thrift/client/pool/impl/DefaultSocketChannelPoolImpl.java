package com.ikamobile.ikasoa.core.thrift.client.pool.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikamobile.ikasoa.core.thrift.client.pool.SocketChannelPool;
import com.ikamobile.ikasoa.core.thrift.client.socket.ThriftSocketChannel;
import com.ikamobile.ikasoa.core.utils.ServerUtil;

/**
 * SocketChannel连接池默认实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class DefaultSocketChannelPoolImpl implements SocketChannelPool {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultSocketChannelPoolImpl.class);

	// 单个池容量
	private byte size = 0x10;

	// 连接超时时间
	private int time = 0;

	private static Map<String, DefaultSocketChannelPoolImpl> selfMap = new HashMap<>();

	// 是否空闲 (true: 不空闲, false: 空闲)
	private boolean[] socketStatusArray = null;

	private Hashtable<Byte, ThriftSocketChannel> socketChannelPool;

	public DefaultSocketChannelPoolImpl() {
		// Do nothing
	}

	public DefaultSocketChannelPoolImpl(byte size) {
		this.size = size;
	}

	public DefaultSocketChannelPoolImpl(int time) {
		this.time = time;
	}

	public DefaultSocketChannelPoolImpl(byte size, int time) {
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
	 * @return DefaultSocketChannelPoolImpl 默认的连接池对象
	 */
	public synchronized DefaultSocketChannelPoolImpl init(String host, int port) {
		if (!ServerUtil.checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		DefaultSocketChannelPoolImpl self = new DefaultSocketChannelPoolImpl();
		selfMap.put(ServerUtil.getKey(host, port), self);
		self.socketChannelPool = new Hashtable<>(size);
		self.socketStatusArray = new boolean[size];
		// 初始化连接池
		LOG.debug("Initiation pool ......");
		buildThriftSocketChannelPool(host, port);
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
	public synchronized void buildThriftSocketChannelPool(String host, int port) {
		if (!ServerUtil.checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		DefaultSocketChannelPoolImpl self = selfMap.get(ServerUtil.getKey(host, port));
		if (self == null) {
			self = init(host, port);
		}
		try {
			for (byte i = 0; i < size; i++) {
				self.socketChannelPool.put(new Byte(i), new ThriftSocketChannel(host, port, time));
				self.socketStatusArray[i] = false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从连接池中获取一个空闲的ThriftSocketChannel连接
	 */
	@Override
	public synchronized ThriftSocketChannel buildThriftSocketChannel(String host, int port) {
		if (!ServerUtil.checkHostAndPort(host, port)) {
			throw new RuntimeException("Server host or port is null !");
		}
		DefaultSocketChannelPoolImpl self = selfMap.get(ServerUtil.getKey(host, port));
		if (self == null || self.socketStatusArray == null) {
			self = init(host, port);
		}
		byte i = 0;
		for (; i < size; i++) {
			if (!self.socketStatusArray[i]) {
				self.socketStatusArray[i] = true;
				return getThriftSocketChannel(self, i, host, port);
			}
		}
		// 如果连接不够用,就初始化连接池.
		try {
			for (i = 0; i < size; i++) {
				ThriftSocketChannel thriftSocketChannel = self.socketChannelPool.get(i);
				if (self.socketStatusArray[i] && thriftSocketChannel == null) {
					thriftSocketChannel = new ThriftSocketChannel(host, port, time);
					return thriftSocketChannel;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		LOG.warn("Not enough pooled connection ! Again retry initiation pool .");
		init(host, port);
		return buildThriftSocketChannel(host, port);
	}

	private ThriftSocketChannel getThriftSocketChannel(DefaultSocketChannelPoolImpl self, byte i, String host,
			int port) {
		LOG.debug("Get socket number is " + i + " .");
		ThriftSocketChannel thriftSocketChannel = self.socketChannelPool.get(new Byte(i));
		if (thriftSocketChannel != null) {
			return thriftSocketChannel;
		} else {
			LOG.warn("Pool is null ! Again retry initiation pool .");
			init(host, port);
			return buildThriftSocketChannel(host, port);
		}
	}

	/**
	 * 回收ThriftSocketChannel连接
	 */
	@Override
	public synchronized void releaseThriftSocketChannel(ThriftSocketChannel thriftSocketChannel, String host,
			int port) {
		if (thriftSocketChannel == null) {
			LOG.warn("Release unsuccessful .");
			return;
		}
		if (!ServerUtil.checkHostAndPort(host, port)) {
			LOG.error("Server host or port is null ! Release unsuccessful .");
			return;
		}
		LOG.debug("Release socket , host is " + host + " and port is " + port + " .");
		DefaultSocketChannelPoolImpl self = selfMap.get(ServerUtil.getKey(host, port));
		if (self == null) {
			self = init(host, port);
		}
		for (byte i = 0; i < size; i++) {
			if (self.socketChannelPool.get(new Byte(i)) == thriftSocketChannel) {
				self.socketStatusArray[i] = false;
				break;
			}
		}
	}

	/**
	 * 回收所有ThriftSocketChannel连接
	 * <p>
	 * 会断开所有连接.
	 */
	@Override
	public synchronized void releaseAllThriftSocketChannel() {
		if (selfMap == null || selfMap.size() == 0) {
			return;
		}
		ThriftSocketChannel socketChannel;
		for (Entry<String, DefaultSocketChannelPoolImpl> entry : selfMap.entrySet()) {
			DefaultSocketChannelPoolImpl self = entry.getValue();
			for (byte i = 0; i < size; i++) {
				socketChannel = self.socketChannelPool.get(new Byte(i));
				try {
					socketChannel.close();
					self.socketStatusArray[i] = false;
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
		}
	}

}
