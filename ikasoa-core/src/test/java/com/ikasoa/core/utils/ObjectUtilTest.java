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
	public void testSame() {
		assertTrue(ObjectUtil.same("", ""));
		assertFalse(ObjectUtil.same("", "0"));
	}

}
