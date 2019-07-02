package com.ikasoa.core.utils;

import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Map工具单元测试
 */
public class MapUtilTest extends TestCase {

	@Test
	public void testNewHashMap() {
		Map<String, Object> map1 = MapUtil.newHashMap();
		assertEquals(map1.size(), 0);
		Map<String, Object> map2 = MapUtil.newHashMap(1);
		assertEquals(map2.size(), 0);
		map2.put("a", 1);
		assertEquals(map2.size(), 1);
	}

	@Test
	public void testNewLinkedHashMap() {
		Map<String, Object> map1 = MapUtil.newLinkedHashMap();
		assertEquals(map1.size(), 0);
		Map<String, Object> map2 = MapUtil.newLinkedHashMap(1);
		assertEquals(map2.size(), 0);
		map2.put("a", 1);
		assertEquals(map2.size(), 1);
	}

	@Test
	public void testNewIdentityHashMap() {
		Map<String, Object> map1 = MapUtil.newIdentityHashMap();
		assertEquals(map1.size(), 0);
		Map<String, Object> map2 = MapUtil.newIdentityHashMap(1);
		assertEquals(map2.size(), 0);
		map2.put("a", 1);
		assertEquals(map2.size(), 1);
	}

	@Test
	public void testArrayToMap() {
		String[][] array1 = { { "a", "A" }, { "b", "B" } };
		Map<String, String> map1 = MapUtil.arrayToMap(array1);
		assertEquals(map1.size(), 2);
		assertEquals(map1.get("a"), "A");
		assertEquals(map1.get("b"), "B");
		Object[][] array2 = { { "a", 1 }, { "b", 2 } };
		Map<String, Object> map2 = MapUtil.arrayToMap(array2);
		assertEquals(map2.size(), 2);
		assertEquals(map2.get("a"), 1);
		assertEquals(map2.get("b"), 2);
	}

}
