package com.ikasoa.core.utils;

import java.util.Date;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Date工具单元测试
 */
public class DateUtilTest extends TestCase {

	@Test
	public void test() {
		Date date = DateUtil.now();
		assertEquals(DateUtil.stringToDate(DateUtil.dateToString(date)).toString(), date.toString());
	}

}
