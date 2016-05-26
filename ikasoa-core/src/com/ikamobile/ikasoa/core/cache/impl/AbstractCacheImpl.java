package com.ikamobile.ikasoa.core.cache.impl;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.ikamobile.ikasoa.core.cache.Cache;

/**
 * 缓存抽象实现
 * 
 * @version 0.3.1
 */
public abstract class AbstractCacheImpl<K, V> implements Cache<K, V> {

	protected class CacheObject<K2, V2> {
		CacheObject(K2 key, V2 value, long ttl) {
			this.key = key;
			this.cachedObject = value;
			this.ttl = ttl;
			this.lastAccess = System.currentTimeMillis();
		}

		final K2 key;

		final V2 cachedObject;

		// 最后访问时间
		long lastAccess;

		// 访问次数
		long accessCount;

		// 对象存活时间
		long ttl;

		public boolean isExpired() {
			if (ttl == 0) {
				return false;
			}
			return lastAccess + ttl < System.currentTimeMillis();
		}

		public V2 getObject() {
			lastAccess = System.currentTimeMillis();
			accessCount++;
			return cachedObject;
		}
	}

	protected Map<K, CacheObject<K, V>> cacheMap;

	private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
	private final Lock readLock = cacheLock.readLock();
	private final Lock writeLock = cacheLock.writeLock();

	// 缓存大小 ,0为无限
	protected int cacheSize;

	// 是否设置默认过期时间
	protected boolean existCustomExpire;

	public int getCacheSize() {
		return cacheSize;
	}

	// 默认过期时间,0为永不过期
	protected long defaultExpire;

	public AbstractCacheImpl(int cacheSize, long defaultExpire) {
		this.cacheSize = cacheSize;
		this.defaultExpire = defaultExpire;
	}

	public long getDefaultExpire() {
		return defaultExpire;
	}

	protected boolean isNeedClearExpiredObject() {
		return defaultExpire > 0 || existCustomExpire;
	}

	public void put(K key, V value) {
		put(key, value, defaultExpire);
	}

	public void put(K key, V value, long expire) {
		writeLock.lock();

		try {
			CacheObject<K, V> co = new CacheObject<K, V>(key, value, expire);
			if (expire != 0) {
				existCustomExpire = true;
			}
			if (isFull()) {
				eliminate();
			}
			cacheMap.put(key, co);
		} finally {
			writeLock.unlock();
		}
	}

	public V get(K key) {
		readLock.lock();

		try {
			CacheObject<K, V> co = cacheMap.get(key);
			if (co == null) {
				return null;
			}
			if (co.isExpired() == true) {
				cacheMap.remove(key);
				return null;
			}

			return co.getObject();
		} finally {
			readLock.unlock();
		}
	}

	public final int eliminate() {
		writeLock.lock();
		try {
			return eliminateCache();
		} finally {
			writeLock.unlock();
		}
	}

	protected abstract int eliminateCache();

	public boolean isFull() {
		// 0为无限
		if (cacheSize == 0) {
			return false;
		}
		return cacheMap.size() >= cacheSize;
	}

	public void remove(K key) {
		writeLock.lock();
		try {
			cacheMap.remove(key);
		} finally {
			writeLock.unlock();
		}
	}

	public void clear() {
		writeLock.lock();
		try {
			cacheMap.clear();
		} finally {
			writeLock.unlock();
		}
	}

	public int size() {
		return cacheMap.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}
}