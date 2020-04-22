package com.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Object工具单元测试
 */
public class ObjectUtilTest extends TestCase {

	@Test
	public void testIsNull() {
		assertTrue(ObjectUtil.isNull(null));
		assertFalse(ObjectUtil.isNull(""));
	}

	@Test
	public void testAndIsNull() {
		assertTrue(ObjectUtil.andIsNull(null, null));
		assertFalse(ObjectUtil.andIsNull(null, ""));
		assertFalse(ObjectUtil.andIsNull("", "0"));
	}

	@Test
	public void testOrIsNull() {
		assertTrue(ObjectUtil.orIsNull(null, null));
		assertTrue(ObjectUtil.orIsNull(null, ""));
		assertFalse(ObjectUtil.orIsNull("", "0"));
	}

	@Test
	public void testIsNotNull() {
		assertFalse(ObjectUtil.isNotNull(null));
		assertTrue(ObjectUtil.isNotNull(""));
	}

	@Test
	public void testEquals() {
		String o1 = new String("1");
		String o2 = new String("1");
		String o3 = new String("2");
		assertTrue(ObjectUtil.equals(o1, o1));
		assertTrue(ObjectUtil.equals(o1, "1"));
		assertTrue(ObjectUtil.equals(o1, o2));
		assertFalse(ObjectUtil.equals(o1, o3));
	}

	@Test
	public void testSame() {
		String o1 = new String("1");
		String o2 = new String("1");
		assertTrue(ObjectUtil.same(o1, o1));
		assertFalse(ObjectUtil.same(o1, o2));
	}

}
