package com.ikamobile.ikasoa.core.cache.impl;

import java.util.HashMap;
import java.util.Iterator;

/**
 * LFU缓存实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.1
 */
public class LFUCacheImpl<K, V> extends AbstractCacheImpl<K, V> {

	public LFUCacheImpl(int cacheSize, long defaultExpire) {
		super(cacheSize, defaultExpire);
		cacheMap = new HashMap<K, CacheObject<K, V>>(cacheSize + 1);
	}

	/**
	 * 实现删除过期对象 和 删除访问次数最少的对象
	 */
	@Override
	protected int eliminateCache() {
		Iterator<CacheObject<K, V>> iterator = cacheMap.values().iterator();
		int count = 0;
		long minAccessCount = Long.MAX_VALUE;
		while (iterator.hasNext()) {
			CacheObject<K, V> cacheObject = iterator.next();

			if (cacheObject.isExpired()) {
				iterator.remove();
				count++;
				continue;
			} else {
				minAccessCount = Math.min(cacheObject.accessCount, minAccessCount);
			}
		}

		if (count > 0) {
			return count;
		}

		if (minAccessCount != Long.MAX_VALUE) {

			iterator = cacheMap.values().iterator();

			while (iterator.hasNext()) {
				CacheObject<K, V> cacheObject = iterator.next();
				cacheObject.accessCount -= minAccessCount;
				if (cacheObject.accessCount <= 0) {
					iterator.remove();
					count++;
				}

			}

		}

		return count;
	}

}