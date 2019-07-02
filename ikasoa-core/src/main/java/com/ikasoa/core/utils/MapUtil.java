package com.ikasoa.core.utils;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

	public static <K, V> Map<K, V> newHashMap(Map<? extends K, ? extends V> m) {
		return new HashMap<>(m);
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

	public static Map<String, String> arrayToMap(String[][] array) {
		final Map<String, String> map = newHashMap((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			final String[] entry = array[i];
			if (entry.length < 2)
				throw new IllegalArgumentException(String.format("Array element %d, has a length less than 2 .", i));
			map.put(entry[0], entry[1]);
		}
		return map;
	}

	public static Map<String, Object> arrayToMap(Object[][] array) {
		final Map<String, Object> map = newHashMap((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			final Object[] entry = array[i];
			if (entry.length < 2)
				throw new IllegalArgumentException(String.format("Array element %d, has a length less than 2 .", i));
			map.put(entry[0].toString(), entry[1]);
		}
		return map;
	}

}
