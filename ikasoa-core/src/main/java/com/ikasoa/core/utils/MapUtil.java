package com.ikasoa.core.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import lombok.experimental.UtilityClass;

/**
 * Map工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.2
 */
@UtilityClass
public class MapUtil {

	public static <K, V> Map<K, V> newHashMap() {
		return new HashMap<>();
	}

	public static <K, V> Map<K, V> newHashMap(int initialCapacity) {
		return new HashMap<>(initialCapacity);
	}

	public static <K, V> Map<K, V> newHashMap(int initialCapacity, float loadFactor) {
		return new HashMap<>(initialCapacity, loadFactor);
	}

	public static <K, V> Map<K, V> newHashMap(Map<? extends K, ? extends V> m) {
		return new HashMap<>(m);
	}

	@SafeVarargs
	public static <K, V> Map<K, V> buildHashMap(V... values) {
		Map<K, V> map = newHashMap(values.length);
		buildMap(map, values);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Class<? extends Map> getHashMapClass() {
		return newHashMap(0).getClass();
	}

	public static <K, V> Map<K, V> newLinkedHashMap() {
		return new LinkedHashMap<>();
	}

	public static <K, V> Map<K, V> newLinkedHashMap(int initialCapacity) {
		return new LinkedHashMap<>(initialCapacity);
	}

	public static <K, V> Map<K, V> newLinkedHashMap(Map<? extends K, ? extends V> m) {
		return new LinkedHashMap<>(m);
	}

	@SafeVarargs
	public static <K, V> Map<K, V> buildLinkedHashMap(V... values) {
		Map<K, V> map = newLinkedHashMap(values.length);
		buildMap(map, values);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Class<? extends Map> getLinkedHashMapClass() {
		return newLinkedHashMap(0).getClass();
	}

	public static <K, V> Map<K, V> newIdentityHashMap() {
		return new IdentityHashMap<>();
	}

	public static <K, V> Map<K, V> newIdentityHashMap(int initialCapacity) {
		return new IdentityHashMap<>(initialCapacity);
	}

	public static <K, V> Map<K, V> newIdentityHashMap(Map<? extends K, ? extends V> m) {
		return new IdentityHashMap<>(m);
	}

	@SuppressWarnings("rawtypes")
	public static Class<? extends Map> getIdentityHashMapClass() {
		return newIdentityHashMap(0).getClass();
	}

	@SafeVarargs
	public static <K, V> Map<K, V> buildIdentityHashMap(V... values) {
		Map<K, V> map = newIdentityHashMap(values.length);
		buildMap(map, values);
		return map;
	}

	public static <K, V> TreeMap<K, V> newTreeMap() {
		return new TreeMap<>();
	}

	public static <K, V> TreeMap<K, V> newTreeMap(Comparator<? super K> comparator) {
		return new TreeMap<>(comparator);
	}

	public static <K, V> TreeMap<K, V> newTreeMap(Map<? extends K, ? extends V> m) {
		return new TreeMap<>(m);
	}

	public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> m) {
		return new TreeMap<>(m);
	}

	@SafeVarargs
	public static <K, V> TreeMap<K, V> buildTreeMap(V... values) {
		TreeMap<K, V> map = newTreeMap();
		buildMap(map, values);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Class<? extends TreeMap> getTreeMapClass() {
		return newTreeMap().getClass();
	}

	public static <K, V> Hashtable<K, V> newHashtable() {
		return new Hashtable<>();
	}

	public static <K, V> Hashtable<K, V> newHashtable(int initialCapacity) {
		return new Hashtable<>(initialCapacity);
	}

	public static <K, V> Hashtable<K, V> newHashtable(Map<? extends K, ? extends V> m) {
		return new Hashtable<>(m);
	}

	@SafeVarargs
	public static <K, V> Hashtable<K, V> buildHashtable(V... values) {
		Hashtable<K, V> map = newHashtable(values.length);
		buildMap(map, values);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Class<? extends Hashtable> getHashtableClass() {
		return newHashtable(0).getClass();
	}

	public static <E> Map<E, E> arrayToMap(E[][] array) {
		final Map<E, E> map = newHashMap((int) (array.length));
		JUtil.fur(0, array.length - 1, 1, i -> {
			final E[] entry = array[i];
			if (entry.length < 2)
				throw new IllegalArgumentException(String.format("Array element %d, has a length less than 2 .", i));
			map.put(entry[0], entry[1]);
		});
		return map;
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return ObjectUtil.isNull(map) || map.isEmpty();
	}

	@SuppressWarnings("unchecked")
	private void buildMap(@SuppressWarnings("rawtypes") Map map, Object[] objects) {
		ListUtil.forEach(0, 2, ListUtil.buildArrayList(objects), (index, item) -> {
			if (index + 1 < objects.length)
				map.put(objects[index], objects[index + 1]);
		});
	}

}
