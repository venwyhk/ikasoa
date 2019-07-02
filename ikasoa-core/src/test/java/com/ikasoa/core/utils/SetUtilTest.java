package com.ikasoa.core.utils;

import java.util.Set;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Map工具单元测试
 */
public class SetUtilTest extends TestCase {

	@Test
	public void testNewHashSet() {
		Set<Object> set1 = SetUtil.newHashSet();
		assertEquals(set1.size(), 0);
		Set<String> set2 = SetUtil.newHashSet(1);
		assertEquals(set2.size(), 0);
		set2.add("1");
		assertEquals(set2.size(), 1);
		Set<String> set3 = SetUtil.newHashSet("1", "2");
		assertEquals(set3.size(), 2);
	}

}
