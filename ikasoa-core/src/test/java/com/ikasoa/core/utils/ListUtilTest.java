package com.ikasoa.core.utils;

import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * List工具单元测试
 */
public class ListUtilTest extends TestCase {

	@Test
	public void testNewArrayList() {
		List<Object> list1 = ListUtil.newArrayList();
		assertEquals(list1.size(), 0);
		List<String> list2 = ListUtil.newArrayList(1);
		assertEquals(list2.size(), 0);
		list2.add("1");
		assertEquals(list2.size(), 1);
		List<String> list3 = ListUtil.buildArrayList("1", "2");
		assertEquals(list3.size(), 2);
	}

	@Test
	public void testNewLinkedList() {
		List<Object> list1 = ListUtil.newLinkedList();
		assertEquals(list1.size(), 0);
		list1.add("1");
		assertEquals(list1.size(), 1);
		List<String> list2 = ListUtil.buildLinkedList("1", "2");
		assertEquals(list2.size(), 2);
	}

	@Test
	public void testIsEmpty() {
		assertTrue(ListUtil.isEmpty(null));
		assertTrue(ListUtil.isEmpty(ListUtil.newArrayList()));
	}

	@Test
	public void testForEach() {
		ListUtil.forEach(0, 2, ListUtil.buildArrayList("0", "2", "4"), (index, item) -> {
			assertTrue(index == StringUtil.toInt(item));
		});
		ListUtil.forEach(2, 1, ListUtil.buildArrayList("a", "b", "c"), (index, item) -> {
			assertTrue(index == 2);
			assertEquals(item, "c");
		});
	}

}
