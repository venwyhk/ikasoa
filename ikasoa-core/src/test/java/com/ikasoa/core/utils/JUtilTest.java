package com.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;

public class JUtilTest extends TestCase {

	@Test
	public void test() {
		JUtil.fur(0, 0, 0, i -> {
			assertTrue(i == 0);
		});
		JUtil.fur(1, 1, 0, i -> {
			assertTrue(i == 1);
		});
	}

	/**
	 * 对象拷贝测试
	 */
	@Test
	public void testCopyProperties() {
		ClassA classA = new ClassA();
		ClassB classB = new ClassB();
		classA.setStr1("string1");
		classA.setStr2("string2");
		classA.setInt1(123);
		try {
			JUtil.copyProperties(classB, classA);
		} catch (Exception e) {
			fail();
		}
		assertEquals(classA.getStr1(), classB.getStr1());
		assertEquals(classA.getStr2(), classB.getStr2());
		assertEquals(classA.getInt1(), classB.getInt1());
	}

	@Data
	class ClassA {
		private String str1;
		private String str2;
		private int int1;
	}

	@Data
	class ClassB {
		private String str1;
		private String str2;
		private int int1;
	}

}
