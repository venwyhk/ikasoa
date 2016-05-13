package com.ikamobile.ikasoa.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikamobile.ikasoa.core.STException;

/**
 * Bean工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class BeanUtil {

	private static final Logger LOG = LoggerFactory.getLogger(BeanUtil.class);

	/**
	 * 对象属性拷贝方法
	 * <p>
	 * 在Thrift对象与系统对象的转换可能会用到,所以这里提供了一个工具方法.
	 * <p>
	 * 如果对象结构比较复杂,更建议使用类似Dozer这样的工具进行映射转换.
	 * 
	 * @param target
	 *            输出的对象
	 * @param source
	 *            原始对象
	 * @exception STException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void copyProperties(Object target, Object source) throws STException {
		Class sourceClz = source.getClass();
		Class targetClz = target.getClass();
		Field[] fields = sourceClz.getDeclaredFields();
		if (fields.length == 0) {
			fields = sourceClz.getSuperclass().getDeclaredFields();
		}
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			Field targetField = null;
			try {
				targetField = targetClz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				try {
					targetField = targetClz.getSuperclass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException e1) {
					throw new STException(e1);
				} catch (SecurityException e2) {
					throw new STException(e2);
				}
			}
			if (fields[i].getType() == targetField.getType()) {
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method getMethod;
				Method setMethod;
				try {
					try {
						getMethod = sourceClz.getDeclaredMethod(getMethodName, new Class[] {});
					} catch (NoSuchMethodException e) {
						getMethod = sourceClz.getSuperclass().getDeclaredMethod(getMethodName, new Class[] {});
					}
					try {
						setMethod = targetClz.getDeclaredMethod(setMethodName, fields[i].getType());
					} catch (NoSuchMethodException e) {
						setMethod = targetClz.getSuperclass().getDeclaredMethod(setMethodName, fields[i].getType());
					}
					Object result = getMethod.invoke(source, new Object[] {});
					setMethod.invoke(target, result);
				} catch (Exception e) {
					LOG.warn("Object copy failed !", e);
				}
			} else {
				throw new STException("Object copy failed ! Attribute type error !");
			}
		}
	}

}
