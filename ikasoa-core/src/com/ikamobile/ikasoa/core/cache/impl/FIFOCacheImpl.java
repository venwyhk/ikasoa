package com.ikamobile.ikasoa.core.cache.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * FIFO缓存实现
 * 
 * @version 0.3.1
 */
public class FIFOCacheImpl<K, V> extends AbstractCacheImpl<K, V> {

	public FIFOCacheImpl(int cacheSize, long defaultExpire) {
		super(cacheSize, defaultExpire);
		cacheMap = new LinkedHashMap<K, CacheObject<K, V>>(cacheSize + 1);
	}

	@Override
	protected int eliminateCache() {

		int count = 0;
		K firstKey = null;

		Iterator<CacheObject<K, V>> iterator = cacheMap.values().iterator();
		while (iterator.hasNext()) {
			CacheObject<K, V> cacheObject = iterator.next();

			if (cacheObject.isExpired()) {
				iterator.remove();
				count++;
			} else {
				if (firstKey == null) {
					firstKey = cacheObject.key;
				}
			}
		}

		// 删除过期对象还是满,就继续删除链表第一个
		if (firstKey != null && isFull()) {
			cacheMap.remove(firstKey);
		}

		return count;
	}

}