package com.ikasoa.core.utils;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;

/**
 * Bean工具单元测试
 */
public class BeanUtilTest extends TestCase {

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
			BeanUtil.copyProperties(classB, classA);
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
