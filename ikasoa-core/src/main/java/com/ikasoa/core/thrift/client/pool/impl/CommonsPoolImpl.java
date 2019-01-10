package com.ikasoa.core.thrift.client.pool.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.ikasoa.core.thrift.client.pool.SocketPool;
import com.ikasoa.core.thrift.client.socket.ThriftSocket;
import com.ikasoa.core.utils.ServerUtil;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Socket连接池实现(commons-pool2)
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.5.3
 */
@Slf4j
public class CommonsPoolImpl implements SocketPool {

	/**
	 * 单个池容量
	 */
	private byte size = defaultSize;

	/**
	 * 连接超时时间
	 */
	private int time = defaultTime;

	/**
	 * 连接池设置
	 */
	private GenericObjectPoolConfig<ThriftSocket> conf = new GenericObjectPoolConfig<>();

	private static Map<String, ObjectPool<ThriftSocket>> poolMap = new HashMap<>();

	public CommonsPoolImpl() {
		conf.setMaxTotal(size);
	}

	public CommonsPoolImpl(byte size) {
		this.size = size;
		conf.setMaxTotal(size);
	}

	public CommonsPoolImpl(GenericObjectPoolConfig<ThriftSocket> conf) {
		this.conf = conf;
	}

	private void initPool(String host, int port) {
		String key = ServerUtil.buildCacheKey(host, port);
		if (!poolMap.containsKey(key))
			poolMap.put(key, new GenericObjectPool<>(new ThriftSocketFactory(host, port, time), conf));
	}

	@Override
	public synchronized ThriftSocket buildThriftSocket(String host, int port) {
		initPool(host, port);
		try {
			return poolMap.get(ServerUtil.buildCacheKey(host, port)).borrowObject();
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new ThriftSocket(host, port);
	}

	@Override
	@SneakyThrows
	public synchronized void releaseThriftSocket(ThriftSocket thriftSocket, String host, int port) {
		if (thriftSocket == null)
			return;
		ObjectPool<ThriftSocket> pool = poolMap.get(ServerUtil.buildCacheKey(host, port));
		if (pool != null)
			pool.returnObject(thriftSocket);
	}

	@Override
	public synchronized void releaseAllThriftSocket() {
		if (poolMap == null || poolMap.isEmpty()) {
			log.debug("Release unsuccessful .");
			return;
		}
		poolMap.forEach((k, v) -> v.close());
		poolMap.clear();
	}

	@AllArgsConstructor
	class ThriftSocketFactory extends BasePooledObjectFactory<ThriftSocket> {

		private String host;

		private int port;

		private int time;

		@Override
		public ThriftSocket create() throws Exception {
			return new ThriftSocket(host, port, time);
		}

		@Override
		public PooledObject<ThriftSocket> wrap(ThriftSocket thriftSocket) {
			return new DefaultPooledObject<>(thriftSocket);
		}

	}

}
