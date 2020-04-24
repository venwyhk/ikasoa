package com.ikasoa.core.utils;

import lombok.experimental.UtilityClass;

/**
 * Object工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.2
 */
@UtilityClass
public class ObjectUtil {

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	public static boolean isNotNull(Object obj) {
		return obj != null;
	}

	public static boolean andIsNull(Object... objects) {
		return ListUtil.buildArrayList(objects).stream().filter(ObjectUtil::isNotNull).count() == 0;
	}

	public static boolean orIsNull(Object... objects) {
		return ListUtil.buildArrayList(objects).stream().filter(ObjectUtil::isNull).count() > 0;
	}

	public static boolean equals(Object obj1, Object obj2) {
		return andIsNull(obj1, obj2) ? true : !orIsNull(obj1, obj2) ? obj1.equals(obj2) : false;
	}

	public static boolean same(Object obj1, Object obj2) {
		return obj1 == obj2;
	}

}
