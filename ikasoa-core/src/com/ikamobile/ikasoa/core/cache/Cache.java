package com.ikamobile.ikasoa.core.cache;

/**
 * 缓存接口
 * 
 * @version 0.3.1
 */
public interface Cache<K, V> {

	/**
	 * 返回当前缓存的大小
	 * 
	 * @return int
	 */
	int size();

	/**
	 * 返回默认存活时间
	 * 
	 * @return long
	 */
	long getDefaultExpire();

	/**
	 * 向缓存添加value对象,其在缓存中生存时间为默认值
	 * 
	 * @param key
	 * @param value
	 */
	void put(K key, V value);

	/**
	 * 向缓存添加value对象,并指定存活时间
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 *            过期时间
	 */
	void put(K key, V value, long expire);

	/**
	 * 查找缓存对象
	 * 
	 * @param key
	 * @return V
	 */
	V get(K key);

	/**
	 * 淘汰对象
	 * 
	 * @return int 被淘汰对象数量
	 */
	int eliminate();

	/**
	 * 缓存是否已经满
	 * 
	 * @return boolean
	 */
	boolean isFull();

	/**
	 * 删除缓存对象
	 * 
	 * @param key
	 */
	void remove(K key);

	/**
	 * 清除所有缓存对象
	 */
	void clear();

	/**
	 * 返回缓存大小
	 * 
	 * @return int
	 */
	int getCacheSize();

	/**
	 * 缓存中是否为空
	 * 
	 * @return boolean
	 */
	boolean isEmpty();

}