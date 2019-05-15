package com.ikasoa.rpc.utils;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * Map工具
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.3
 */
@UtilityClass
public class MapUtil {

	public Map<String, String> arrayToMap(String[][] array) {
		final Map<String, String> map = new HashMap<>((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			final String[] entry = array[i];
			if (entry.length < 2)
				throw new IllegalArgumentException(String.format("Array element %d, has a length less than 2 .", i));
			map.put(entry[0], entry[1]);
		}
		return map;
	}

	public Map<String, Object> arrayToMap(Object[][] array) {
		final Map<String, Object> map = new HashMap<>((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			final Object[] entry = array[i];
			if (entry.length < 2)
				throw new IllegalArgumentException(String.format("Array element %d, has a length less than 2 .", i));
			map.put(entry[0].toString(), entry[1]);
		}
		return map;
	}

}
