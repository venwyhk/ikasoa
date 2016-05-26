package com.ikamobile.ikasoa.core.cache.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU缓存实现
 * 
 * @version 0.3.1
 */
public class LRUCacheImpl<K, V> extends AbstractCacheImpl<K, V> {

	public LRUCacheImpl(int cacheSize, long defaultExpire) {

		super(cacheSize, defaultExpire);

		// linkedHash已经实现LRU算法
		// 是通过双向链表来实现,当某个位置被命中,通过调整链表的指向将该位置调整到头位置,新加入的内容直接放在链表头,如此一来最近被命中的内容就向链表头移动,需要替换时链表最后的位置就是最近最少使用的位置
		this.cacheMap = new LinkedHashMap<K, CacheObject<K, V>>(cacheSize + 1, 1f, true) {

			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, CacheObject<K, V>> eldest) {

				return LRUCacheImpl.this.removeEldestEntry(eldest);
			}

		};
	}

	private boolean removeEldestEntry(Map.Entry<K, CacheObject<K, V>> eldest) {

		if (cacheSize == 0)
			return false;

		return size() > cacheSize;
	}

	/**
	 * 只需要实现清除过期对象就可以了,linkedHashMap已经实现LRU
	 */
	@Override
	protected int eliminateCache() {

		if (!isNeedClearExpiredObject()) {
			return 0;
		}

		Iterator<CacheObject<K, V>> iterator = cacheMap.values().iterator();
		int count = 0;
		while (iterator.hasNext()) {
			CacheObject<K, V> cacheObject = iterator.next();

			if (cacheObject.isExpired()) {
				iterator.remove();
				count++;
			}
		}

		return count;
	}

}